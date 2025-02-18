package com.example.errorhandlingex.handler

import android.app.Activity
import android.app.Application
import android.content.Intent
import android.os.Bundle
import android.os.Process
import com.example.presentation.error_handling.ErrorHandlingActivity
import java.io.PrintWriter
import java.io.StringWriter
import kotlin.system.exitProcess

class TodayFortuneExceptionHandler(
    application: Application,
    private val crashlyticsExceptionHandler: Thread.UncaughtExceptionHandler
) : Thread.UncaughtExceptionHandler {

    private var lastActivity: Activity? = null
    private var activityCount = 0

    init {
        application.registerActivityLifecycleCallbacks(
            object : SimpleActivityLifecycleCallbacks() {
                override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
                    if (isSkipActivity(activity)) {
                        return
                    }
                    lastActivity = activity
                }

                override fun onActivityStarted(activity: Activity) {
                    if (isSkipActivity(activity)) {
                        return
                    }
                    activityCount++
                    lastActivity = activity
                }

                override fun onActivityStopped(activity: Activity) {
                    if (isSkipActivity(activity)) {
                        return
                    }
                    activityCount--
                    if (activityCount < 0) {
                        lastActivity = null
                    }
                }
            })
    }

    private fun isSkipActivity(activity: Activity) = activity is ErrorHandlingActivity

    override fun uncaughtException(thread: Thread, throwable: Throwable) {
        lastActivity?.run {
            val stringWriter = StringWriter()
            throwable.printStackTrace(PrintWriter(stringWriter))

            startErrorActivity(this, stringWriter.toString())
        }

        Process.killProcess(Process.myPid())    // 현재 프로세스 즉시 종료

        // error log Crashlytics에 전송
        crashlyticsExceptionHandler.uncaughtException(thread, throwable)

        exitProcess(-1) // 메인 프로세스 종료
    }

    private fun startErrorActivity(activity: Activity, errorText: String) = activity.run {
        val errorActivityIntent = Intent(this, ErrorHandlingActivity::class.java)
            .apply {
                putExtra(ErrorHandlingActivity.EXTRA_INTENT, intent)
                putExtra(ErrorHandlingActivity.EXTRA_ERROR_TEXT, errorText)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            }
        startActivity(errorActivityIntent)
        finish()
    }
}