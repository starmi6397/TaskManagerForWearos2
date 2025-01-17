package wat.app.taskmanager.screens.main

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
import androidx.core.graphics.drawable.toBitmap
import bakuen.wear.components.Shapes
import bakuen.wear.components.Text
import bakuen.wear.components.Theme


// 使用ExperimentalFoundationApi注解，允许使用实验性的API
@OptIn(ExperimentalFoundationApi::class)
// 定义一个可组合的函数，用于显示应用程序的单元格
@Composable
    // 应用程序信息
fun AppCell(
    info: AppInfo,
    onLongClick: (() -> Unit)? = null,
    onClick: () -> Unit = {}
) {
    // 创建一个水平排列的行，填充整个宽度，背景颜色为Theme.color.surfaceContainer，形状为Shapes.Cell
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = Theme.color.surfaceContainer, shape = Shapes.Cell)
            // 绑定点击事件和长按事件
            .combinedClickable(
                onClick = onClick,
                onLongClick = onLongClick
            )
            // 设置内边距
            .padding(horizontal = 12.dp, vertical = 6.dp),
        // 垂直居中对齐
        verticalAlignment = Alignment.CenterVertically,
        // 水平间隔为4dp
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        // 显示图标
        Image(
            modifier = Modifier.size(28.dp),
            bitmap = info.iconBitmap,
            contentDescription = null
        )
        // 显示文本
        Text(text = info.label)
    }
}