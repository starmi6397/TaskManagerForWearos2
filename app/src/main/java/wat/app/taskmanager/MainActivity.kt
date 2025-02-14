package wat.app.taskmanager

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.ui.graphics.Color
import bakuen.lib.navigator.NavHost
import bakuen.wear.components.AutoSize
import bakuen.wear.components.Surface
import bakuen.wear.components.rememberViewModel
import wat.app.taskmanager.screens.main.MainScreen
import wat.app.taskmanager.screens.main.MainViewModel
import wat.app.taskmanager.shizuku.ShizukuTools
import wat.app.taskmanager.utils.Update

// 声明一个全局的Context变量，用于在整个应用程序中共享
lateinit var appContext: Context
    private set
class MainActivity : ComponentActivity() {

    // 在Activity创建时调用
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appContext = applicationContext
        Update.check(this)
        ShizukuTools.requestPermission()
        setContent {
            AutoSize(designWidth = 192f) {
                NavHost(initScreen = { MainScreen(rememberViewModel { MainViewModel() }) }) {
                    Surface(color = Color.Black, fillMaxSize = true) {
                        it()
                    }
                }
            }
        }
    }

    override fun onPause() {
        super.onPause()
        finish()
    }
}