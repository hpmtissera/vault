package com.fandoco.vault

import org.springframework.web.bind.annotation.*

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
    fun addType(@RequestBody type: String) {
    }

}