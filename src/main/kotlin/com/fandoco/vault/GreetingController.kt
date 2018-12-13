package com.fandoco.vault

import org.jetbrains.exposed.dao.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.*
import java.util.concurrent.atomic.AtomicLong
import javax.xml.crypto.Data
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

    addEntry("Postbank" + Random.nextInt(0, 100), "Username" + Random.nextInt(0, 100), "Saman")
}

fun addEntry(type: String, key: String, value: String): String {
    var id: String? = null
    transaction {
        // print sql to std-out
        addLogger(StdOutSqlLogger)

        val entityId = SecureData.insert {
            it[SecureData.type] = type
            it[SecureData.key] = key
            it[SecureData.value] = value
        } get SecureData.id

        DataEntry.all().forEach { print(it) }
        id = entityId?.value.toString()

    }
    when (id) {
        null -> throw Exception("Error while trying to insert entry. {type: $type, key: $key, value: $value")
        else -> return id as String
    }
}

fun getEntry(type: String, key: String) {
    transaction {
        addLogger(StdOutSqlLogger)

        val it = SecureData.select { (SecureData.type eq type) and (SecureData.key eq key) }.single()
        val entry = DataEntry(it[SecureData.id])
        entry.type = it[SecureData.type]
        entry.key = it[SecureData.key]
        entry.value = it[SecureData.value]
    }
}

fun updateEntry(type: String, key: String, value: String) {
    transaction {
        addLogger(StdOutSqlLogger)

        SecureData.update {
            it[SecureData.type] = type
            it[SecureData.key] = key
            it[SecureData.value] = value
        }

    }
}

fun deleteEntry(type: String, key: String) {
    transaction {
        addLogger(StdOutSqlLogger)

        SecureData.deleteWhere {
            (SecureData.type eq type) and (SecureData.key eq key)
        }
    }
}

object SecureData : UUIDTable("secure_data") {
    val type = text("type")
    val key = text("key")
    val value = text("value")
}

class DataEntry(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<DataEntry>(SecureData)

    var type by SecureData.type
    var key by SecureData.key
    var value by SecureData.value

    override fun toString(): String {
        return "\nType : $type, Key : $key, Value : $value"
    }
}