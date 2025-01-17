package wat.app.taskmanager.utils

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import bakuen.lib.navigator.Navigator
import bakuen.wear.components.Space
import bakuen.wear.components.calculate
import bakuen.wear.components.condition
import bakuen.wear.components.wear.verticalRotaryScroll
import wat.app.taskmanager.screens.about.AboutScreen
import wat.app.taskmanager.screens.main.InfoText

// 定义一个可组合函数，用于创建一个垂直滚动的列
@Composable
inline fun ScreenColumn(center: Boolean = false, bg: Color? = null, content: @Composable ColumnScope.()->Unit) {
    // 创建一个列，填充整个屏幕
    Column(
        modifier = Modifier
            .fillMaxSize()
            // 如果bg不为空，则设置背景颜色
            .calculate { if (bg!=null) Modifier.background(color = bg) else null }
            // 设置水平方向的填充
            .padding(horizontal = Const.scrPaddingHor)
            // 如果center为true，则将内容居中
            .condition(center, Modifier.wrapContentSize(Alignment.Center))
            // 添加垂直滚动
            .verticalRotaryScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Const.colArrangement,
        // 垂直方向使用Const.colArrangement布局
        content = content
    )
}

@Composable
inline fun ColumnScope.Title(content: @Composable ColumnScope.()->Unit) {
// 定义一个可组合函数，用于创建一个标题
    Space(size = Const.scrPaddingTop)
    content()
    // 添加一个垂直方向的间距
    Space(size = 8.dp)
}    // 添加一个垂直方向的间距
