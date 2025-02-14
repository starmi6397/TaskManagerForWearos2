package wat.app.taskmanager.screens.main

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.res.Resources
import android.os.BatteryManager
import androidx.compose.foundation.clickable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import bakuen.wear.components.Text
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.Date
import kotlin.math.roundToInt


@Composable
fun InfoText(onClick: ()->Unit = {}) {
    // 使用produceState函数来产生一个可观察的状态，用于显示当前时间
    val time by produceState(initialValue = "") {
        do {
            value =
                SimpleDateFormat("HH:mm", Resources.getSystem().configuration.locales[0]).format(
                    Date()
                )
            delay(1_000)
        } while (true)
    }
    // 获取当前上下文
    val context = LocalContext.current
    var batteryReceiver: BroadcastReceiver? = null //TODO 不优雅
    // 使用produceState函数来产生一个可观察的状态，用于显示当前电量
    val battery by produceState(initialValue = 0) {
        batteryReceiver = object : BroadcastReceiver() {
            override fun onReceive(p0: Context?, intent: Intent) {
                val level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
                val scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1)
                value = (level * 100 / scale.toFloat()).roundToInt()
            }
        }
        context.registerReceiver(batteryReceiver, IntentFilter(Intent.ACTION_BATTERY_CHANGED))
    }
    // 使用DisposableEffect函数来在组件销毁时取消注册广播接收器
    DisposableEffect(Unit) {
        onDispose {
            context.unregisterReceiver(batteryReceiver)
        }
    }
    Text(modifier = Modifier.clickable(onClick = onClick), text = "$time $battery%")
}
