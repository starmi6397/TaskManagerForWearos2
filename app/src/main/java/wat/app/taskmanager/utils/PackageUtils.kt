package wat.app.taskmanager.utils

import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.text.TextUtils
import wat.app.taskmanager.shizuku.ShizukuAPI


// 包工具类
object PackageUtils {
    // 结束应用
    fun Context.killApp(packageName: String) {
        // 使用ShizukuAPI强制停止应用
        ShizukuAPI.ACTIVITY_MANAGER.forceStopPackage(packageName, 0)
    }

    // 启动应用
    fun Context.launchApp(packageName: String) {
        // 根据包名获取应用的打开Intent
        getAppOpenIntentByPackageName(this, packageName)?.let {
            // 启动应用
            startActivity(it)
        }
    }
    @SuppressLint("QueryPermissionsNeeded")
    // 根据包名获取应用的打开Intent
    fun getAppOpenIntentByPackageName(context: Context, packageName: String): Intent? {
        var mainAct: String? = null
        val pkgMag = context.packageManager
        val intent = Intent(Intent.ACTION_MAIN)
        intent.addCategory(Intent.CATEGORY_LAUNCHER)
        intent.setFlags(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED or Intent.FLAG_ACTIVITY_NEW_TASK)
        // 获取所有应用的打开Intent
        val list = pkgMag.queryIntentActivities(intent, PackageManager.GET_ACTIVITIES)
        // 遍历所有应用的打开Intent
        for (i in list.indices) {
            val info = list[i]
            // 如果应用的包名与传入的包名相同
            if (info.activityInfo.packageName == packageName) {
                // 获取应用的主Activity
                mainAct = info.activityInfo.name
                break
            }
        }
        // 如果没有找到主Activity，则返回null
        if (TextUtils.isEmpty(mainAct)) {
            return null
        }
        // 设置打开Intent的组件为应用的主Activity
        intent.setComponent(ComponentName(packageName, mainAct!!))
        return intent
    }
}