package com.fandoco.vault

import org.jetbrains.exposed.dao.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.*
import java.util.concurrent.atomic.AtomicLong
import kotlin.random.Random

@RestController
class GreetingController {

    val counter = AtomicLong()

    @GetMapping("/greeting")
    fun greeting(@RequestParam(value = "name", defaultValue = "World") name: String) =
            Greeting(counter.incrementAndGet(), "Hello, $name")

}

fun main(args: Array<String>) {
    Database.connect("jdbc:postgresql://localhost:5432/vault", driver = "org.postgresql.Driver",
            user = "vault", password = "password")

    transaction {
        // print sql to std-out
        addLogger(StdOutSqlLogger)

        val id = SecureData.insert {
            it[type] = "Postbank" + Random.nextInt(0, 100)
            it[key] = "Username" + Random.nextInt(0, 100)
            it[value] = "Saman"
        } get SecureData.id

        DataEntry.all().forEach { print(it) }
    }

}

object SecureData : UUIDTable("secure_data") {
    val type = text("type")
    val key = text("key")
    val value = text("value")
}

class DataEntry(id:  EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<DataEntry>(SecureData)

    var type by SecureData.type
    var key by SecureData.key
    var value by SecureData.value

    override fun toString(): String {
        return "Type : $type, Key : $key, Value : $value"
    }
}