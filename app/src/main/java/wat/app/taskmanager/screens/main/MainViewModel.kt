package wat.app.taskmanager.screens.main

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import bakuen.lib.protostore.getStore
import bakuen.wear.components.SimpleViewModel
import wat.app.taskmanager.utils.Const
import wat.app.taskmanager.utils.PackageUtils.killApp
import wat.app.taskmanager.utils.PackageUtils.launchApp
import wat.app.taskmanager.shizuku.ShizukuAPI
import wat.app.taskmanager.shizuku.ShizukuTools
import wat.app.taskmanager.appContext
import wat.app.taskmanager.prefs.Settings

// 定义主界面的ViewModel
class MainViewModel : SimpleViewModel<MainEvent>() {

    // 定义主界面的状态
    var state by mutableStateOf(MainState(shizuku = ShizukuTools.state))
        private set

    // 更新应用列表
    fun updateAppList() {
        ShizukuTools.requireBinder {
            val pm = appContext.packageManager
            state = state.copy(
                runningAppProgress = ShizukuAPI.ACTIVITY_MANAGER.runningAppProcesses
                    .filter {
                        !(Const.ignoreApps.contains(it.processName) ||
                                killedApps.contains(it.processName) ||
                                getStore<Settings>().hidePackages.contains(it.processName))
                    }
                    .mapNotNull {
                        runCatching {
                            val info = pm.getApplicationInfo(it.processName, 0)
                            AppInfo(
                                packageName = it.processName,
                                icon = info.loadIcon(pm),
                                label = info.loadLabel(pm).toString()
                            )
                        }.getOrNull()
                    }
            )
        }
    }

    // 定义已结束的应用集合
    private val killedApps = mutableSetOf<String>()
    // 处理主界面的事件
    override fun dispatch(event: MainEvent) {
        when (event) {
            is MainEvent.Open -> appContext.launchApp(event.packageName)
            is MainEvent.Kill -> {
                appContext.killApp(event.packageName)
                killedApps.add(event.packageName)
                updateAppList()
            }
        }
    }
}