package com.lukaslechner.coroutineusecasesonandroid.playground.coroutine_builders

import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking


fun main() = runBlocking<Unit>{

    /**
     * Using launch coroutine builder
     */
//    val startTime = System.currentTimeMillis()
//
//    delay(1000) // suspend function blocking main thread via runBlocking
//    println("Time elapsed after first delay ${elapsedMills(startTime)}ms")
//
//    launch {
//        val result1 = networkCall(1)
//        println("Result received: $result1 after ${elapsedMills(startTime)}ms")
//    }// doesn't block main thread so next line of code execute immediately
//
//    println("Time elapsed after first delay and first launch block ${elapsedMills(startTime)}ms")
//    delay(2000) // suspend function blocking main thread via runBlocking
//    println("Time elapsed after second delay ${elapsedMills(startTime)}ms")
//
//    launch {
//        val result2 = networkCall(2)
//        println("Result received: $result2 after ${elapsedMills(startTime)}ms")
//    } // doesn't block main thread so next line of code execute immediately
//
//    println("Time elapsed after second dealy and second launch block ${elapsedMills(startTime)}ms")

    /**
     * Using async coroutine builder
     */

        val startTime = System.currentTimeMillis()

    delay(1000) // suspend function blocking main thread via runBlocking
    println("Time elapsed after first delay ${elapsedMills(startTime)}ms")

    val deferred1 = async {
        val result1 = networkCall(1)
        println("Result received: $result1 after ${elapsedMills(startTime)}ms")
        result1
    }// doesn't block main thread so next line of code execute immediately
    println("Time elapsed after first delay and first async block ${elapsedMills(startTime)}ms")
    
//    delay(2000) // suspend function blocking main thread via runBlocking

    println("Time elapsed after second delay ${elapsedMills(startTime)}ms")
    val deferred2 = async {
        val result2 = networkCall(2)
        println("Result received: $result2 after ${elapsedMills(startTime)}ms")
        result2
    } // doesn't block main thread so next line of code execute immediately
    println("Time elapsed after second dealy and second async block ${elapsedMills(startTime)}ms")

    val resultList = listOf(deferred1.await(), deferred2.await())
    println("Result list: $resultList after ${elapsedMills(startTime)}")

}

// In concurrent programming avoid using shared mutable states whenever possible.

suspend fun networkCall(number: Int): String {
    delay(500)
    return "Result $number"
}

fun elapsedMills(startTime: Long) = System.currentTimeMillis() - startTime