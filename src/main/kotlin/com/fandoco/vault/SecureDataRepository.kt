package com.fandoco.vault

import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.UUIDTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*
import kotlin.collections.ArrayList

fun main(args: Array<String>) {
    Database.connect("jdbc:postgresql://localhost:5432/vault", driver = "org.postgresql.Driver",
            user = "vault", password = "password")

    transaction { SecureDataTable.deleteAll() }

    val id1 = addEntry("Postbank", "Username", "Saman")
    val id2 = addEntry("Postbank", "Password", "Kamal")

    println("\nEntry added : $id1")
    println("\nEntry added : $id2")

    printAll()

    updateEntry(SecureDataEntry("Postbank", "Username", "Samankamal"))

    printAll()

    val entry = getEntry("Postbank", "Username")

    println("\n$entry\n")

    val postBankEntries = getEntries("Postbank")
    println("\n$postBankEntries\n")

    deleteEntry(SecureDataEntry("Postbank", "Username", null))

    printAll()

    println("UUID = $id2")
    println("UUID from String = ${UUID.fromString(id2)}")
    deleteEntry(id2)

    printAll()

}

fun printAll() {
    val entries = getAllEntries()
    when {
        entries.isEmpty() -> println("\nTable is empty.")
        else -> println(entries)
    }
}

fun addEntry(type: String, key: String, value: String): String {
    var id: String? = null
    transaction {
        // print sql to std-out
        addLogger(StdOutSqlLogger)

        val entityId = SecureDataTable.insert { row ->
            row[SecureDataTable.type] = type
            row[SecureDataTable.key] = key
            row[SecureDataTable.value] = value
        } get SecureDataTable.id

        id = entityId?.value.toString()

    }
    when (id) {
        null -> throw Exception("Error while trying to insert entry. {type: $type, key: $key, value: $value")
        else -> return id as String
    }
}

fun getEntry(id: String): SecureDataEntry? {
    var entry: SecureDataEntry? = null
    transaction {
        addLogger(StdOutSqlLogger)

        val row = SecureDataTable.select { SecureDataTable.id eq UUID.fromString(id) }.singleOrNull()
        if (row != null) {
            entry = SecureDataEntry(
                    row[SecureDataTable.id].toString(),
                    row[SecureDataTable.type],
                    row[SecureDataTable.key],
                    row[SecureDataTable.value])
        }
    }
    return entry
}

fun getEntry(type: String, key: String): SecureDataEntry? {
    var entry: SecureDataEntry? = null
    transaction {
        addLogger(StdOutSqlLogger)

        val row = SecureDataTable.select {
            (SecureDataTable.type eq type) and (SecureDataTable.key eq key)
        }.singleOrNull()

        if (row != null) {
            entry = SecureDataEntry(
                    row[SecureDataTable.id].toString(),
                    row[SecureDataTable.type],
                    row[SecureDataTable.key],
                    row[SecureDataTable.value])
        }
    }
    return entry
}

fun getEntries(type: String): List<SecureDataEntry> {
    val entries = ArrayList<SecureDataEntry>()
    transaction {
        addLogger(StdOutSqlLogger)

        SecureDataTable.select { SecureDataTable.type eq type }.forEach { row ->
            entries.add(SecureDataEntry(
                    row[SecureDataTable.id].toString(),
                    row[SecureDataTable.type],
                    row[SecureDataTable.key],
                    row[SecureDataTable.value]))
        }

    }
    return entries
}

fun getAllEntries(): List<SecureDataEntry> {
    val entries = ArrayList<SecureDataEntry>()
    transaction {
        SecureDataRow.all().forEach { entries.add(SecureDataEntry(it.id.toString(), it.type, it.key, it.value)) }
    }
    return entries
}

fun updateEntry(entry: SecureDataEntry) {
    updateEntry(entry.type, entry.key, entry.value)
}

fun updateEntry(type: String, key: String, value: String?) {
    transaction {
        addLogger(StdOutSqlLogger)

        SecureDataTable.update({ (SecureDataTable.type eq type) and (SecureDataTable.key eq key) })
        {
            it[SecureDataTable.value] = value
        }

    }
}

fun deleteEntry(entry: SecureDataEntry) {
    transaction {
        deleteEntry(entry.type, entry.key)
    }
}

fun deleteEntry(type: String, key: String) {
    transaction {
        addLogger(StdOutSqlLogger)

        SecureDataTable.deleteWhere {
            (SecureDataTable.type eq type) and (SecureDataTable.key eq key)
        }
    }
}

fun deleteEntry(id: String) {
    transaction {
        addLogger(StdOutSqlLogger)

        SecureDataTable.deleteWhere {
            SecureDataTable.id eq UUID.fromString(id)
        }
    }
}


object SecureDataTable : UUIDTable("secure_data") {
    val type = text("type")
    val key = text("key")
    val value = text("value").nullable()
}

class SecureDataRow(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<SecureDataRow>(SecureDataTable)

    var type by SecureDataTable.type
    var key by SecureDataTable.key
    var value by SecureDataTable.value

}

data class SecureDataEntry(val id: String?, val type: String, val key: String, val value: String?) {

    constructor(type: String, key: String, value: String?) : this(null, type, key, value)

    override fun toString(): String {
        return "\nType : $type" +
                "\nKey : $key" +
                "\nValue : $value\n"
    }
}