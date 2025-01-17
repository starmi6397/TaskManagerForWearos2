package wat.app.taskmanager.screens.hides

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import bakuen.lib.navigator.SwipeToDismiss
import bakuen.lib.protostore.getStore
import bakuen.lib.protostore.rememberStore
import bakuen.lib.protostore.setStore
import bakuen.wear.components.Text
import wat.app.taskmanager.prefs.Settings
import wat.app.taskmanager.screens.main.AppCell
import wat.app.taskmanager.screens.main.AppInfo
import wat.app.taskmanager.utils.ScreenColumn
import wat.app.taskmanager.utils.Title

// 定义一个隐藏应用列表的屏幕
@Composable
fun HideAppListScreen() {
    // 获取当前上下文的包管理器
    val pm = LocalContext.current.packageManager
    // 使用rememberStore获取Settings对象
    var settings by rememberStore<Settings>()
    // 使用remember创建一个可变的状态
    var state by remember { mutableStateOf(State()) }
    LaunchedEffect(settings.hidePackages.size) {
        state = state.copy(hideList = settings.hidePackages.map {
            val info = pm.getApplicationInfo(it, 0)
            AppInfo(
                packageName = it,
                icon = info.loadIcon(pm),
                label = info.loadLabel(pm).toString()
            )
        })
    }
    HideAppListScreenUI(state = state, dispatch = {
        when (it) {
            is Event.Remove -> settings =
                settings.copy(hidePackages = settings.hidePackages.minus(it.packageName))
        }
    })
}

private data class State(
    val hideList: List<AppInfo> = listOf()
)

private sealed class Event {
    class Remove(val packageName: String) : Event()
}

@Composable
private fun HideAppListScreenUI(state: State, dispatch: (Event) -> Unit) {
    ScreenColumn {
        Title {
            Text(text = "被隐藏的应用")
        }
        state.hideList.let {
            if (it.isEmpty()) Text(text = "没有被隐藏的应用！")
            else it.forEach {
                SwipeToDismiss(
                    velocity = 40.dp,
                    reverse = true,
                    onDismiss = { dispatch(Event.Remove(it.packageName)) }
                ) {
                    AppCell(info = it)
                }
            }
        }
    }
}