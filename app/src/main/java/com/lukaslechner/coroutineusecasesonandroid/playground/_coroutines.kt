package com.lukaslechner.coroutineusecasesonandroid.playground

import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.runBlocking

fun main() = runBlocking {
    println("main starts")
    joinAll(
        async {
            coroutine(1, 500)
        },
        async{
            coroutine(2, 300)
        }
    )

    println("Main ends")
}


suspend fun coroutine(number: Int, delay: Long = 500){
    println("Coroutine $number starts work.")
    delay(delay)
    println("Coroutine $number has finished.")
}