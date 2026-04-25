package com.most.messenger.app

import android.app.Application
import android.util.Log
import com.google.firebase.FirebaseApp

class MostApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)

        val options = FirebaseApp.getInstance().options
        Log.i(
            "MostApplication",
            "Firebase initialized. projectId=${options.projectId}, appId=${options.applicationId}"
        )
    }
}
