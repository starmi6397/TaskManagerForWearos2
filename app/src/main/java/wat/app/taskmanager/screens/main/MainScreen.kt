package wat.app.taskmanager.screens.main

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.key
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import bakuen.lib.navigator.Navigator
import bakuen.lib.protostore.rememberStore
import bakuen.wear.components.Space
import bakuen.wear.components.SwipeToDismiss
import bakuen.wear.components.Text
import wat.app.taskmanager.prefs.Settings
import wat.app.taskmanager.utils.ScreenColumn
import wat.app.taskmanager.shizuku.ShizukuState
import wat.app.taskmanager.screens.about.AboutScreen
import wat.app.taskmanager.screens.detail.AppDetailScreen
import wat.app.taskmanager.utils.Title

// 主屏幕
@Composable
fun MainScreen(model: MainViewModel) {
    // 当设置中的隐藏包列表发生变化时，更新应用列表
    LaunchedEffect(rememberStore<Settings>().value.hidePackages.size) {
        model.updateAppList()
    }
    // 渲染主屏幕UI
    MainScreenUI(state = model.state, dispatch = model::dispatch)
}

// 主屏幕UI
@Composable
private fun MainScreenUI(state: MainState, dispatch: (MainEvent)->Unit) {
    // 如果Shizuku权限被授予
    if (state.shizuku == ShizukuState.GRANTED) {
        // 渲染应用列表
        ScreenColumn {
            // 渲染标题
            Title {
                // 渲染信息文本
                InfoText(onClick = {
                    // 点击信息文本时，跳转到关于页面
                    Navigator.forward { AboutScreen() }
                })
            }
            // 如果没有运行中的应用
            if (state.runningAppProgress.isEmpty()) {
                // 显示提示信息
                Text(text = "当前没有运行中的应用")
            } else state.runningAppProgress.forEach {
                // 为每个应用渲染一个key
                key(it.packageName) {
                    // 渲染可滑动删除的应用单元格
                    SwipeToDismiss(
                        velocity = 40.dp, // 设置滑动速度
                        reverse = true, // 设置滑动方向为反向
                        onDismiss = { dispatch(MainEvent.Kill(it.packageName)) }) { // 设置滑动后触发的操作，即关闭应用
                        // 渲染应用单元格
                        AppCell(
                            info = it, // 应用信息
                            onClick = { dispatch(MainEvent.Open(it.packageName)) }, // 设置点击事件，即打开应用
                            onLongClick = { Navigator.forward { AppDetailScreen(info = it) } } // 设置长按事件，即跳转到应用详情页面
                        )
                    }
                }
            }
        }
    } else {
        // 如果Shizuku权限没有被授予或服务不可用
        Text(
            modifier = Modifier
                .fillMaxSize()
                .wrapContentSize(),
            text = if (state.shizuku == ShizukuState.NOT_GRANTED) "请授予 Shizuku 权限！"
            else "Shizuku 服务不可用！"
        )
    }
}