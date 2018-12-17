package com.fandoco.vault.auth

import org.springframework.context.annotation.Primary
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service

@Primary
@Service
class UserService : UserDetailsService {

    override fun loadUserByUsername(username: String): UserDetails? {
        return UserRepository.getUserByUsername(username)
    }

}