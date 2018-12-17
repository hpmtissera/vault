package com.fandoco.vault.auth

import com.fandoco.vault.dataSource
import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.UUIDTable
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.StdOutSqlLogger
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*

object UserRepository {
    init {
        Database.connect(dataSource())
    }

    object UserTable : UUIDTable("user") {
        val name = UserTable.text("name")
        val username = UserTable.text("username")
        val password = UserTable.text("password")
    }

    class UserRow(id: EntityID<UUID>) : UUIDEntity(id) {
        companion object : UUIDEntityClass<UserRow>(UserTable)

        var name by UserTable.name
        var username by UserTable.username
        var password by UserTable.password

    }

    fun getUserByUsername(username: String): User? {
        var user: User? = null
        transaction {
            addLogger(StdOutSqlLogger)

            val row = UserTable
                    .select { UserTable.username eq username }.singleOrNull()

            if (row != null) {
                user = User(
                        row[UserTable.id].toString(),
                        row[UserTable.name],
                        row[UserTable.username],
                        row[UserTable.password])
            }
        }
        return user
    }


}