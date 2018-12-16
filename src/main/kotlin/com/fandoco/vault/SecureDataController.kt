package com.fandoco.vault

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
        return SecureDataRepository.getEntries(type)
    }

    @GetMapping("/types")
    fun getAllTypes() : List<String> {
        return SecureDataRepository.getAllTypes()
    }

    @PostMapping("/types")
    fun addType(@RequestBody body: Map<String, String>): String {
        return SecureDataRepository.addType(body["name"]!!)
    }

    @DeleteMapping("/types")
    fun deleteType(@RequestBody body: Map<String, String>) {
        return SecureDataRepository.deleteTypeByName(body["name"]!!)
    }


    @PostMapping("/data")
    fun addData(@RequestBody body: Map<String, String>): String {
        return SecureDataRepository.addEntry(body["type"]!!, body["key"]!!, body["value"]!!)
    }

    @PutMapping("/data")
    fun updateData(@RequestBody body: Map<String, String>) {
        return SecureDataRepository.updateEntry(body["type"]!!, body["key"]!!, body["value"]!!)
    }

    @DeleteMapping("/data")
    fun deleteData(@RequestBody body: Map<String, String>) {
        return SecureDataRepository.deleteEntry(body["type"]!!, body["key"]!!)
    }

    @RequestMapping("/logout", method = [RequestMethod.POST])
    fun logoutPage(request: HttpServletRequest, response: HttpServletResponse): String {
        val auth = SecurityContextHolder.getContext().authentication
        if (auth != null) {
            SecurityContextLogoutHandler().logout(request, response, auth)
        }
        return "redirect:/"
    }

}