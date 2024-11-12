package com.akkarimzai.task10.repositories

import com.akkarimzai.task10.entities.Token
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface TokenRepository : JpaRepository<Token, Long> {
    fun existsByToken(token: String): Boolean
}