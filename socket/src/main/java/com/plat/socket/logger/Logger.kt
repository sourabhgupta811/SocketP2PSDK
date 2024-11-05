package com.plat.socket.logger

import android.util.Log

object Logger {
    fun d(message:String){
        Log.d("",message)
    }
}

suspend fun logSuspendExecution(name:String, block:suspend ()->Unit){
    Logger.d(name.plus("------start"))
    block.invoke()
    Logger.d(name.plus("------end"))
}

fun logExecution(name:String, block:()->Unit){
    Logger.d(name.plus("------start"))
    block.invoke()
    Logger.d(name.plus("------end"))
}