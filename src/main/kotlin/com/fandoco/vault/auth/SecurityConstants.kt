package com.fandoco.vault.auth

import java.util.concurrent.TimeUnit

@JvmField
val EXPIRATION_TIME: Long = TimeUnit.DAYS.toMillis(10)
const val TOKEN_PREFIX = "Bearer "
const val HEADER_STRING = "Authorization"