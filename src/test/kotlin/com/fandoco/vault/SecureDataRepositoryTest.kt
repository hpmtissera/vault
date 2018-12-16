package com.fandoco.vault

import org.junit.Test
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.TestInstance

const val RAKUTEN_BANK = "Rakuten Bank"

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class SecureDataRepositoryTest {

    @Test
    fun testRepository() {

        SecureDataRepository.deleteAll()

        // Add types
        SecureDataRepository.addType("Post Bank")
        SecureDataRepository.addType(RAKUTEN_BANK)

        // Get types
        val type1 = SecureDataRepository.getTypeByName("Post Bank")
        assertEquals("Post Bank", type1?.name)

        // Add entry
        val id1 = SecureDataRepository.addEntry("Post Bank", "Username", "Saman")
        val id2 = SecureDataRepository.addEntry(RAKUTEN_BANK, "Password", "Kamal")
        val id3 = SecureDataRepository.addEntry(RAKUTEN_BANK, "Username", "Sunil")

        // Get entry by type
        val rakutenBankEntries = SecureDataRepository.getEntries(RAKUTEN_BANK)
        assertEquals(2, rakutenBankEntries.size)

        assertEquals(RAKUTEN_BANK, rakutenBankEntries[0].type)
        assertEquals("Password", rakutenBankEntries[0].key)
        assertEquals("Kamal", rakutenBankEntries[0].value)

        assertEquals(RAKUTEN_BANK, rakutenBankEntries[1].type)
        assertEquals("Username", rakutenBankEntries[1].key)
        assertEquals("Sunil", rakutenBankEntries[1].value)


        // Update entry
        SecureDataRepository.updateEntry(SecureDataEntry("Post Bank", "Username", "Samankamal"))

        val updatedEntry = SecureDataRepository.getEntry("Post Bank", "Username")
        assertEquals("Samankamal", updatedEntry?.value)

        // Delete entry
        SecureDataRepository.deleteEntry("Post Bank", "Username")

        assertEquals(0, SecureDataRepository.getEntries("Post Bank").size)

        SecureDataRepository.deleteAll()

    }
}