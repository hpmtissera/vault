package com.fandoco.vault

import org.postgresql.ds.PGSimpleDataSource
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Primary
import javax.sql.DataSource

@Bean
@Primary
fun dataSource(): DataSource {

    val fullDatabaseUrl = getDatabaseUrl()

    val databaseUrl = fullDatabaseUrl.substringAfterLast("@")
    val usernameAndPassword = fullDatabaseUrl.substringAfter("postgres://").substringBeforeLast("@").split(":")

    val dataSource = PGSimpleDataSource()
    dataSource.setUrl("jdbc:postgresql://$databaseUrl")
    dataSource.user = usernameAndPassword[0]
    dataSource.password = usernameAndPassword[1]

    return dataSource
}