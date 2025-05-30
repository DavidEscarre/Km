package com.example.km.core.utils

import android.content.Context
import timber.log.Timber
import java.io.File
import java.util.Date

class FileLoggingTree(private val context: Context) : Timber.Tree() {
    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        val logFile = File(context.filesDir, "app_logs.txt")
        logFile.appendText("${Date()} [$tag] $message\n")
    }
}