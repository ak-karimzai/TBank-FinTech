package com.akkarimzai.task10.services

import com.akkarimzai.task10.entities.User
import com.akkarimzai.task10.exceptions.ConflictException
import com.akkarimzai.task10.exceptions.NotFoundException
import com.akkarimzai.task10.repositories.UserRepository
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service


@Service
class UserService(
    private val repository: UserRepository
) {
    fun save(user: User): User {
        return repository.save(user)
    }

    fun create(user: User): User {
        if (repository.existsByUsername(user.username)) {
            throw ConflictException("user-username", user.username)
        }

        if (repository.existsByEmail(user.email)) {
            throw ConflictException("user-email", user.email)
        }

        return save(user)
    }

    fun loadUserByUsername(username: String): User {
        return repository.findByUsername(username)
            ?: throw NotFoundException("user-username", username)
    }

    fun currentUser(): User {
        val username = SecurityContextHolder.getContext().authentication.name
        return loadUserByUsername(username)
    }

    fun userDetailsService(): UserDetailsService =
        UserDetailsService { username -> loadUserByUsername(username) }

    fun loadUserDetails(): (String) -> User {
        return this::loadUserByUsername
    }
}