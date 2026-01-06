package com.rp199.service

import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlin.random.Random

class SuspendingService {
    suspend fun doWork(){
        val random = Random.nextInt()
        if(random % 2 == 0){
            throw RuntimeException("It failed")
        }
        delay(2_000L)
    }
    fun doWorkBlocking() = runBlocking{ doWork() }
}