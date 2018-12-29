package com.fandoco.vault.auth

import com.fandoco.vault.dataSource
import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.UUIDTable
import org.jetbrains.exposed.sql.*
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

    fun addUser(name: String, username: String, password: String): String {
        var id: String? = null
        transaction {
            // print sql to std-out
            addLogger(StdOutSqlLogger)

            val entityId = UserTable.insert { row ->
                row[UserTable.name] = name
                row[UserTable.username] = username
                row[UserTable.password] = password
            } get UserTable.id

            id = entityId?.value.toString()

        }
        when (id) {
            null -> throw Exception("Error while trying to add user. {name: $name, username: $username, password: $password")
            else -> return id as String
        }
    }

    fun deleteAll() {
        transaction {
            UserTable.deleteAll()
        }
    }

}