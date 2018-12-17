package com.fandoco.vault

import org.postgresql.ds.PGSimpleDataSource
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Primary
import javax.sql.DataSource

@Bean
@Primary
fun dataSource(): DataSource {

    var fullDatabaseUrl = System.getenv("DATABASE_URL")

    if (fullDatabaseUrl.isNullOrBlank()) {
        fullDatabaseUrl = "postgres://vault:password@localhost:5432/vault"
    }

    val databaseUrl = fullDatabaseUrl.substringAfterLast("@")
    val usernameAndPassword = fullDatabaseUrl.substringAfter("postgres://").substringBeforeLast("@").split(":")

    val dataSource = PGSimpleDataSource()
    dataSource.setUrl("jdbc:postgresql://$databaseUrl")
    dataSource.user = usernameAndPassword[0]
    dataSource.password = usernameAndPassword[1]

    return dataSource
}