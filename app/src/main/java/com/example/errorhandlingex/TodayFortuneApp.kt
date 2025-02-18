package com.example.errorhandlingex

import android.app.Application
import com.example.errorhandlingex.handler.TodayFortuneExceptionHandler
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class TodayFortuneApp : Application() {
    override fun onCreate() {
        super.onCreate()

        setCrashHandler()
    }

    private fun setCrashHandler() {
        // Crashlytics 연동 완료
        val crashlyticsExceptionHandler = Thread.getDefaultUncaughtExceptionHandler() ?: return
        Thread.setDefaultUncaughtExceptionHandler(
            TodayFortuneExceptionHandler(
                this,
                crashlyticsExceptionHandler
            )
        )
    }
}