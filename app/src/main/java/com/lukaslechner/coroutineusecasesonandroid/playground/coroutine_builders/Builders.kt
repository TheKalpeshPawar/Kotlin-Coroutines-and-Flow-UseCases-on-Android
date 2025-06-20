package com.lukaslechner.coroutineusecasesonandroid.playground.coroutine_builders

import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

@OptIn(DelicateCoroutinesApi::class)
fun main(){

    // Global scope: A simple coroutine scope. Lives as long as the application is alive.

    // Launch function just calls the coroutine
    // main() function continues execution of next commands after launch.
////1.
//    GlobalScope.launch {
//        delay(500)
//        println("Printed from within Coroutine")
//    }
//    Thread.sleep(509)
////2.
//    runBlocking {
//        this.launch {
//            delay(500)
//            println("Printed from within Coroutine")
//        }
//    }
//// 3.
    runBlocking {
        val job = launch(
            start = CoroutineStart.LAZY
        ) {
            networkRequest()
            println("result received")
        }

        delay(200)
        // Starts the lazy initialized coroutine.
        job.start()

        job.join() // waits for the job to finish.

        println("end of runBlocking")
    }

    println("main ends")
}

suspend fun networkRequest(): String{
    delay(500)
    return "Result"
}