package com.lukaslechner.coroutineusecasesonandroid.playground

import kotlin.concurrent.thread


fun main(){
    println("main starts")


    threadRoutine(1, 500)
    threadRoutine(2, 300)
    Thread.sleep(1000)
    println("main ends")
}

private fun threadRoutine(number: Int, delay: Long){
    thread {
        println("Routine $number starts work on ${Thread.currentThread().name}")
        Thread.sleep(delay)
        println("Routine $number has finished on ${Thread.currentThread().name}")
    }
}