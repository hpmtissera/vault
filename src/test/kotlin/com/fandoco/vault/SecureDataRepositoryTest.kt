package com.fandoco.vault

import com.fandoco.vault.auth.UserRepository
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.TestInstance

const val RAKUTEN_BANK = "Rakuten Bank"

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class SecureDataRepositoryTest {

    @Test
    fun testRepository() {

        SecureDataRepository.deleteAll()
        UserRepository.deleteAll()

        // Add user
        UserRepository.addUser("Administrator", "admin", "$2a\$10\$xMtxPuXs9CXtb4mnA2SG0.91D0Oc4a2hgFW9wLitoBYQD.u.uaDUq")

        val user = UserRepository.getUserByUsername("admin")!!

        // Add types
        SecureDataRepository.addType(user, "Post Bank")
        SecureDataRepository.addType(user, RAKUTEN_BANK)

        // Get types
        val type1 = SecureDataRepository.getTypeByName(user, "Post Bank")
        assertEquals("Post Bank", type1?.name)

        // Add entry
        val id1 = SecureDataRepository.addEntry(user, "Post Bank", "Username", "Saman")
        val id2 = SecureDataRepository.addEntry(user, RAKUTEN_BANK, "Password", "Kamal")
        val id3 = SecureDataRepository.addEntry(user, RAKUTEN_BANK, "Username", "Sunil")

        // Get entry by type
        val rakutenBankEntries = SecureDataRepository.getEntries(user, RAKUTEN_BANK)
        assertEquals(2, rakutenBankEntries.size)

        assertEquals(RAKUTEN_BANK, rakutenBankEntries[0].type)
        assertEquals("Password", rakutenBankEntries[0].key)
        assertEquals("Kamal", rakutenBankEntries[0].value)

        assertEquals(RAKUTEN_BANK, rakutenBankEntries[1].type)
        assertEquals("Username", rakutenBankEntries[1].key)
        assertEquals("Sunil", rakutenBankEntries[1].value)


        // Update entry
        SecureDataRepository.updateEntry(user, SecureDataEntry("Post Bank", "Username", "Samankamal"))

        val updatedEntry = SecureDataRepository.getEntry(user, "Post Bank", "Username")
        assertEquals("Samankamal", updatedEntry?.value)

        // Delete entry
        SecureDataRepository.deleteEntry(user, "Post Bank", "Username")

        assertEquals(0, SecureDataRepository.getEntries(user, "Post Bank").size)

        SecureDataRepository.deleteAll()
        UserRepository.deleteAll()
    }
}