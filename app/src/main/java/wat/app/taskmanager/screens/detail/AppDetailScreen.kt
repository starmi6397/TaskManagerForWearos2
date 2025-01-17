package wat.app.taskmanager.screens.detail

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusModifier
import bakuen.lib.navigator.Navigator
import bakuen.lib.protostore.setStore
import bakuen.wear.components.Space
import bakuen.wear.components.SurfaceButton
import bakuen.wear.components.Text
import wat.app.taskmanager.prefs.Settings
import wat.app.taskmanager.utils.Const
import wat.app.taskmanager.utils.ScreenColumn
import wat.app.taskmanager.screens.main.AppInfo

// 定义一个可组合函数，用于显示应用的详细信息
@Composable
fun AppDetailScreen(info: AppInfo) {
    // 使用ScreenColumn布局，将内容垂直排列
    ScreenColumn {
        // 添加一个空白区域，用于填充顶部
        Space(size = Const.scrPaddingTop)
        // 显示应用的图标
        Image(bitmap = info.iconBitmap, contentDescription = null)
        // 显示应用的标签
        Text(text = info.label)
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "应用包名：${info.packageName}")
        }
        SurfaceButton(modifier = Modifier.clickable {
            setStore<Settings> { it.copy(hidePackages = it.hidePackages.plus(info.packageName)) }
            Navigator.navigateBack()
        }) {
            Text(text = "在列表中隐藏此应用")
        }
    }
}