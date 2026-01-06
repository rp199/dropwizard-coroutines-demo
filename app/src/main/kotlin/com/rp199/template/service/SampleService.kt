package com.rp199.template.service

class SampleService {
    fun doStuff(message: String): SampleResponse {
        return SampleResponse(sampleValue = message)
    }
}

data class SampleResponse(
    val sampleValue: String
)