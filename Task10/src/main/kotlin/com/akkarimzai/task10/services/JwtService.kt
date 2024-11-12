package com.akkarimzai.task10.services

import com.akkarimzai.task10.entities.Token
import com.akkarimzai.task10.entities.User
import com.akkarimzai.task10.exceptions.BadRequestException
import com.akkarimzai.task10.exceptions.UnauthorizedException
import com.akkarimzai.task10.repositories.TokenRepository
import io.jsonwebtoken.Claims
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import java.security.Key
import java.util.*

@Service
class JwtService(
    private val repository: TokenRepository,
) {
    private val logger = KotlinLogging.logger {}

    @Value("\${security.signing.key}") private lateinit var jwtKey: String
    @Value("\${security.signing.ttl}") private var ttl: Long? = null

    fun extractUsername(token: String): String {
        return extractClaim(token, Claims::getSubject)
    }

    fun generateToken(userDetails: UserDetails, rememberUser: Boolean = false): String {
        val claims = HashMap<String, Any>()

        if (userDetails is User) {
            claims["id"] = userDetails.id!!
            claims["email"] = userDetails.email
            claims["role"] = userDetails.role
        }
        return generateToken(claims, userDetails, rememberUser)
    }

    fun generateToken(extraClaims: Map<String, Any>, userDetails: UserDetails, rememberUser: Boolean): String {
        val expirationTime = if (rememberUser) ttl!! * 6 else ttl!!
        return Jwts.builder().claims(extraClaims).subject(userDetails.username)
            .issuedAt(Date(System.currentTimeMillis()))
            .expiration(Date(System.currentTimeMillis() + expirationTime))
            .signWith(getSigningKey(), SignatureAlgorithm.HS256).compact();
    }

    fun isTokenValid(token: String, userDetails: UserDetails): Boolean {
        val username = extractUsername(token)
        return username == userDetails.username &&
                !isTokenExpired(token) &&
                !isLoggedOut(token)
    }

    fun isLoggedOut(token: String): Boolean {
        return repository.existsByToken(token)
    }

    fun logout(token: String) {
        if (repository.existsByToken(token)) {
            throw BadRequestException("User already logged out.")
        }

        val tkn = Token(
            token = token
        )
        repository.save(tkn)
    }

    fun isTokenExpired(token: String): Boolean {
        return try {
            extractExpiration(token).before(Date())
        } catch (_: Exception) {
            true
        }
    }

    private fun extractExpiration(token: String): Date {
        return extractClaim(token, Claims::getExpiration)
    }

    private fun <T> extractClaim(token: String, claimsResolver: (Claims) -> T): T {
        val claims = extractAllClaims(token)
        return claimsResolver(claims)
    }

    private fun extractAllClaims(token: String): Claims {
        return Jwts.parser().setSigningKey(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .body
    }

    private fun getSigningKey(): Key {
        val keyBytes = Decoders.BASE64.decode(jwtKey)
        return Keys.hmacShaKeyFor(keyBytes)
    }
}