package com.most.messenger

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.most.messenger.ui.navigation.MostApp
import com.most.messenger.ui.theme.MostTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MostTheme {
                MostApp()
            }
        }
    }
}
