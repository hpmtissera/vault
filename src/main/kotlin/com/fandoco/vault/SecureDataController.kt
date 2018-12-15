package com.fandoco.vault

import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.concurrent.atomic.AtomicLong

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

}