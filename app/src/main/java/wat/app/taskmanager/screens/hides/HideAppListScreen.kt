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
    // 当settings.hidePackages的大小发生变化时，重新获取隐藏的应用列表
    LaunchedEffect(settings.hidePackages.size) {
        state = state.copy(hideList = settings.hidePackages.map {
            // 获取应用的图标和标签
            val info = pm.getApplicationInfo(it, 0)
            AppInfo(
                packageName = it,
                icon = info.loadIcon(pm),
                label = info.loadLabel(pm).toString()
            )
        })
    }
    // 渲染隐藏应用列表的UI
    HideAppListScreenUI(state = state, dispatch = {
        // 根据事件类型进行不同的处理
        when (it) {
            is Event.Remove -> {
                dispatch(Event.Remove(it.packageName))
                // 重新获取隐藏的应用列表
                state = state.copy(hideList = settings.hidePackages.map {
                    val info = pm.getApplicationInfo(it, 0)
                    AppInfo(
                        packageName = it,
                        icon = info.loadIcon(pm),
                        label = info.loadLabel(pm).toString()
                    )
                })
            }
        }
    })
}

// 定义一个状态类，包含隐藏的应用列表
private data class State(
    val hideList: List<AppInfo> = listOf()
)

// 定义一个事件类，包含移除应用的事件
private sealed class Event {
    class Remove(val packageName: String) : Event()
}

// 渲染隐藏应用列表的UI
@Composable
private fun HideAppListScreenUI(state: State, dispatch: (Event) -> Unit) {
    // 使用ScreenColumn布局
    ScreenColumn {
        // 添加标题
        Title {
            Text(text = "被隐藏的应用")
        }
        // 如果隐藏的应用列表为空，则显示提示信息
        state.hideList.let {
            if (it.isEmpty()) Text(text = "没有被隐藏的应用！")
            // 否则，遍历隐藏的应用列表，并使用SwipeToDismiss组件实现滑动删除功能
            // 如果列表中没有元素，则不执行任何操作
            else it.forEach {
                // 创建一个可滑动删除的组件
                SwipeToDismiss(
                    // 设置滑动速度
                    velocity = 40.dp
                    // 设置滑动方向为反向
                    reverse = true,
                    // 设置滑动删除时的回调函数
                    onDismiss = { dispatch(Event.Remove(it.packageName))}
                ) {
                    // 在可滑动删除的组件中显示应用信息
                    AppCell(info = it)
                }
            }
        }
    }
}
