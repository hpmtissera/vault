package com.fandoco.vault

import com.fandoco.vault.auth.User
import com.fandoco.vault.auth.UserRepository
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@RestController
@CrossOrigin(origins = ["*"])
class SecureDataController {

    @GetMapping("/data")
    fun getDataByType(@RequestParam(value = "type") type: String) : List<SecureDataEntry> {
        SecurityContextHolder.getContext().authentication
        return SecureDataRepository.getEntries(getAuthenticatedUser(), type)
    }

    @GetMapping("/types")
    fun getAllTypes() : List<String> {
        return SecureDataRepository.getAllTypes(getAuthenticatedUser())
    }

    @PostMapping("/types")
    fun addType(@RequestBody body: Map<String, String>): String {
        return SecureDataRepository.addType(getAuthenticatedUser(), body["name"]!!)
    }

    @DeleteMapping("/types")
    fun deleteType(@RequestBody body: Map<String, String>) {
        return SecureDataRepository.deleteTypeByName(getAuthenticatedUser(), body["name"]!!)
    }


    @PostMapping("/data")
    fun addData(@RequestBody body: Map<String, String>): String {
        return SecureDataRepository.addEntry(getAuthenticatedUser(), body["type"]!!, body["key"]!!, body["value"]!!)
    }

    @PutMapping("/data")
    fun updateData(@RequestBody body: Map<String, String>) {
        return SecureDataRepository.updateEntry(getAuthenticatedUser(), body["type"]!!, body["key"]!!, body["value"]!!)
    }

    @DeleteMapping("/data")
    fun deleteData(@RequestBody body: Map<String, String>) {
        return SecureDataRepository.deleteEntry(getAuthenticatedUser(), body["type"]!!, body["key"]!!)
    }

    @RequestMapping("/logout", method = [RequestMethod.POST])
    fun logoutPage(request: HttpServletRequest, response: HttpServletResponse): String {
        val auth = SecurityContextHolder.getContext().authentication
        if (auth != null) {
            SecurityContextLogoutHandler().logout(request, response, auth)
        }
        return "redirect:/"
    }

    private fun getAuthenticatedUser(): User {
        val userName = SecurityContextHolder.getContext().authentication.principal
        val user = UserRepository.getUserByUsername(userName as String);
        return user!!
    }

}