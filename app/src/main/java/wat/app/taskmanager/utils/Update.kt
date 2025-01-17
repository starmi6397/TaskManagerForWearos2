package wat.app.taskmanager.utils

import android.app.Activity
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import wat.app.taskmanager.BuildConfig
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

// 定义一个Update对象
object Update {
    // 检查更新
    fun check(activity: Activity) {
        // 在新线程中执行
        Thread {
            try {
                // 创建一个URL对象，并打开连接
                val connection = URL("https://pastebin.com/raw/FjTceQ7F")
                    .openConnection() as HttpURLConnection
                // 设置请求方法为GET
                connection.requestMethod = "GET"
                // 如果响应码为200，表示请求成功
                if (connection.responseCode == HttpURLConnection.HTTP_OK) {
                    // 创建一个BufferedReader对象，用于读取响应内容
                    val reader =
                        BufferedReader(InputStreamReader(connection.inputStream))
                    // 创建一个StringBuilder对象，用于存储响应内容
                    val response = StringBuilder()
                    // 逐行读取响应内容
                    var line: String?
                    while ((reader.readLine().also { line = it }) != null) {
                        response.append(line)
                    }
                    // 关闭BufferedReader对象
                    reader.close()
                    // 将响应内容按@分割，获取新版本号和更新信息
                    val info = response.toString().split("@")
                    val newVersion = info[0].toInt()
                    if (newVersion > BuildConfig.VERSION_CODE) {
                        Handler(Looper.getMainLooper()).post {
                            Toast.makeText(
                                activity,
                                info[1].replace("\\n", "\n"),
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                }
                connection.disconnect()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }.start()
    }
}