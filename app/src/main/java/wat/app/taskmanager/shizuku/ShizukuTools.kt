package wat.app.taskmanager.shizuku

import android.content.pm.PackageManager
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import rikka.shizuku.Shizuku

// 定义一个ShizukuTools对象，用于处理Shizuku的状态和权限
object ShizukuTools {
    // 获取Shizuku的状态
    private fun getShizukuState() = if (running) if (granted) ShizukuState.GRANTED else ShizukuState.NOT_GRANTED
    else ShizukuState.UNAVAILABLE
    // 定义一个可变的Shizuku状态变量
    var state by mutableStateOf(getShizukuState())
        private set
    // 初始化ShizukuTools对象，添加Shizuku的Binder接收和Binder死亡监听器
    init {
        Shizuku.addBinderReceivedListener {
            state = getShizukuState()
        }
        Shizuku.addBinderDeadListener {
            state = getShizukuState()
        }
    }
    // 获取Shizuku是否正在运行
    val running get() = Shizuku.pingBinder()

    // 要求Shizuku的Binder，如果Shizuku正在运行且已经授权，则执行block，否则添加Binder接收监听器
    fun requireBinder(block: ()->Unit) {
        if (running && granted) block()
        else Shizuku.addBinderReceivedListener(block)
    }

    // 获取Shizuku是否已经授权
    val granted get() = running && Shizuku.checkSelfPermission() == PackageManager.PERMISSION_GRANTED
    // 请求Shizuku的权限，如果Shizuku正在运行且没有授权，则添加权限请求结果监听器，并请求权限，否则执行callback
    fun requestPermission(callback: () -> Unit = {}) {
        state = getShizukuState()
        if (!running) return
        if (!granted) {
            Shizuku.addRequestPermissionResultListener { _, _ ->
                callback()
            }
            Shizuku.requestPermission(30433)
        } else callback()
    }
}