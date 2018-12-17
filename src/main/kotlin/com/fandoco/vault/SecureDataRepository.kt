package com.fandoco.vault

import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.UUIDTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashSet

object SecureDataRepository {

    init {

        var fullDatabaseUrl = System.getenv("DATABASE_URL")

        if (fullDatabaseUrl.isNullOrBlank()) {
            fullDatabaseUrl = "postgres://vault:password@localhost:5432/vault"
        }

        val databaseUrl = fullDatabaseUrl.substringAfterLast("@")
        val usernameAndPassword = fullDatabaseUrl.substringAfter("postgres://").substringBeforeLast("@").split(":")


        Database.connect(dataSource())

    }

    fun deleteAll() {
        transaction {
            SecureDataTable.deleteAll()
            TypeTable.deleteAll()
        }
    }

    fun printAll() {

        val types = getAllTypes()
        when {
            types.isEmpty() -> println("\nTable is empty.")
            else -> println(types)
        }

        val entries = getAllEntries()
        when {
            entries.isEmpty() -> println("\nTable is empty.")
            else -> println(entries)
        }
    }

    fun addType(name: String): String {
        var id: String? = null
        transaction {
            // print sql to std-out
            addLogger(StdOutSqlLogger)

            val entityId = TypeTable.insert { row ->
                row[TypeTable.name] = name
            } get TypeTable.id

            id = entityId?.value.toString()

        }
        when (id) {
            null -> throw Exception("Error while trying to insert type. {name: $name")
            else -> return id as String
        }
    }

    fun addEntry(type: String, key: String, value: String): String {
        var id: String? = null
        transaction {
            // print sql to std-out
            addLogger(StdOutSqlLogger)

            val typeRow: TypeRow = getTypeByName(type) ?: throw Exception("Type $type does not exists!")

            val entityId = SecureDataTable.insert { row ->
                row[SecureDataTable.type] = typeRow.id
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

    fun getTypeIdByName(name: String): UUID? {
        var id: UUID? = null
        transaction {
            addLogger(StdOutSqlLogger)

            val row = TypeTable.select { TypeTable.name eq name }.singleOrNull()
            if (row != null) {
                id = row[TypeTable.id].value
            }
        }
        return id
    }

    fun getTypeByName(name: String): TypeRow? {
        var row: TypeRow? = null
        transaction {
            addLogger(StdOutSqlLogger)

            row = TypeRow.find { TypeTable.name eq name }.singleOrNull()
        }
        return row
    }


    fun getEntry(id: String): SecureDataEntry? {
        var entry: SecureDataEntry? = null
        transaction {
            addLogger(StdOutSqlLogger)

            val row = (TypeTable innerJoin SecureDataTable)
                    .slice(SecureDataTable.id, TypeTable.name, SecureDataTable.key, SecureDataTable.value)
                    .select { TypeTable.id eq SecureDataTable.type }.singleOrNull()

            if (row != null) {
                entry = SecureDataEntry(
                        row[SecureDataTable.id].toString(),
                        row[TypeTable.name],
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

            val row = (TypeTable innerJoin SecureDataTable)
                    .slice(SecureDataTable.id, TypeTable.name, SecureDataTable.key, SecureDataTable.value)
                    .select {
                        TypeTable.id eq SecureDataTable.type and (TypeTable.name eq type) and (SecureDataTable.key eq key)
                    }.singleOrNull()

            if (row != null) {
                entry = SecureDataEntry(
                        row[SecureDataTable.id].toString(),
                        row[TypeTable.name],
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

            (TypeTable innerJoin SecureDataTable)
                    .slice(SecureDataTable.id, TypeTable.name, SecureDataTable.key, SecureDataTable.value)
                    .select {
                        TypeTable.id eq SecureDataTable.type
                        TypeTable.name eq type
                    }.forEach { row ->
                        entries.add(SecureDataEntry(
                                row[SecureDataTable.id].toString(),
                                row[TypeTable.name],
                                row[SecureDataTable.key],
                                row[SecureDataTable.value]))
                    }

        }
        return entries
    }

    fun getAllEntries(): List<SecureDataEntry> {
        val entries = ArrayList<SecureDataEntry>()
        transaction {
            SecureDataRow.all().forEach { entries.add(SecureDataEntry(it.id.toString(), it.type.id.toString(), it.key, it.value)) }
        }
        return entries
    }

    fun getAllTypes(): List<String> {
        val types = HashSet<String>()
        transaction {
            addLogger(StdOutSqlLogger)
            TypeTable.slice(TypeTable.name).selectAll().forEach { row ->
                types.add(row[TypeTable.name])
            }

        }
        return types.toList()
    }

    fun updateEntry(entry: SecureDataEntry) {
        updateEntry(entry.type, entry.key, entry.value)
    }

    fun updateEntry(type: String, key: String, value: String?) {
        transaction {
            addLogger(StdOutSqlLogger)

            val typeId: UUID = getTypeIdByName(type) ?: throw Exception("Type $type does not exists!")

            SecureDataTable.update({ (SecureDataTable.type eq typeId) and (SecureDataTable.key eq key) })
            {
                it[SecureDataTable.value] = value
            }

        }
    }

    fun deleteTypeByName(type: String) {
        transaction {
            addLogger(StdOutSqlLogger)

            deleteEntries(type)

            val typeId: UUID = getTypeIdByName(type) ?: throw Exception("Type $type does not exists!")

            TypeTable.deleteWhere {
                TypeTable.id eq typeId
            }
        }
    }

    fun deleteEntries(type: String) {
        transaction {
            addLogger(StdOutSqlLogger)

            val typeId: UUID = getTypeIdByName(type) ?: throw Exception("Type $type does not exists!")

            SecureDataTable.deleteWhere {
                SecureDataTable.type eq typeId
            }
        }
    }

    fun deleteEntry(type: String, key: String) {
        transaction {
            addLogger(StdOutSqlLogger)

            val typeId: UUID = getTypeIdByName(type) ?: throw Exception("Type $type does not exists!")

            SecureDataTable.deleteWhere {
                (SecureDataTable.type eq typeId) and (SecureDataTable.key eq key)
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

    fun addTestData() {
        SecureDataRepository.addEntry("Postbank", "Username", "Saman")
        SecureDataRepository.addEntry("Postbank", "Password", "Kamal")
        SecureDataRepository.addEntry("Postbank", "AccountNumber", "23434343")
    }

}

object TypeTable : UUIDTable("type") {
    val name = text("name")
}

class TypeRow(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<TypeRow>(TypeTable)

    var name by TypeTable.name
}

object SecureDataTable : UUIDTable("secure_data") {
    val type = reference("type_id", TypeTable)
    val key = text("key")
    val value = text("value").nullable()
}

class SecureDataRow(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<SecureDataRow>(SecureDataTable)

    var type by TypeRow referencedOn SecureDataTable.type
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