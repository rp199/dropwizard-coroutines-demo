package com.rp199.service

import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking

class SuspendingService {
    suspend fun doWork(){
        delay(2_000L)
    }
    fun doWorkBlocking() = runBlocking{ doWork() }
}