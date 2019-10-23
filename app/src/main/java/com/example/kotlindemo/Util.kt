package com.example.kotlindemo

import android.app.Activity
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import android.os.Parcelable
import android.util.Log
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException

object Util {

    fun startShareImage(activity: Activity) {
        //过滤出需要分享到对应的平台：微信好友、朋友圈、QQ好友。  可自行修改
        val targetApp = arrayOf("com.tencent.mm.ui.tools.ShareImgUI", "com.tencent.mm.ui.tools.ShareToTimeLineUI", "com.tencent.mobileqq.activity.JumpActivity")
        /** * 分享图片 */
        val bitmap = getImageFromAssetsFile(activity, "img_share.jpg")  //从assets目录中取到对应的文件，文件名自行修改
        val localImage = saveBitmap(bitmap!!, "share.jpg")    //分享前，需要先将图片存在本地（记得添加权限），文件名自行修改
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "image/*" //设置分享内容的类型：图片
        shareIntent.putExtra(Intent.EXTRA_STREAM, localImage)
        try {
            val resInfo = activity.packageManager.queryIntentActivities(shareIntent, 0)
            if (!resInfo.isEmpty()) {
                val targetedShareIntents = ArrayList<Intent>()
                for (info in resInfo) {
                    val targeted = Intent(Intent.ACTION_SEND)
                    targeted.type = "image/*"  //设置分享内容的类型
                    val activityInfo = info.activityInfo
                    //如果还需要分享至其它平台，可以打印出具体信息，然后找到对应的Activity名称，填入上面的数组中即可
//                  println("package = ${activityInfo.packageName}, activity = ${activityInfo.name}")

                    //进行过滤（只显示需要分享的平台）
                    if (targetApp.any { it == activityInfo.name }) {
                        val comp = ComponentName(activityInfo.packageName, activityInfo.name)
                        targeted.component = comp
                        targeted.putExtra(Intent.EXTRA_STREAM, localImage)
                        targetedShareIntents.add(targeted)
                    }
                }
                val chooserIntent = Intent.createChooser(targetedShareIntents.removeAt(0), "选择要分享到的平台")
                if (chooserIntent != null) {
                    chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, targetedShareIntents.toTypedArray<Parcelable>())
                    activity.startActivity(chooserIntent)
                }
            }
        } catch (e: Exception) {
            Log.e("Utils", "Unable to share image,  logs : " + e.toString())
        }
    }

    /** * 从Assets中读取图片  */
    private fun getImageFromAssetsFile(context: Context, fileName: String): Bitmap? {
        var image: Bitmap? = null
        val am = context.resources.assets
        try {
            val inputStream = am.open(fileName)
            image = BitmapFactory.decodeStream(inputStream)
            inputStream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return image
    }

    /** * 将图片存到本地  */
    private fun saveBitmap(bm: Bitmap, picName: String): Uri? {
        try {
            val dir = Environment.getExternalStorageDirectory().absolutePath + File.separator + picName
            val f = File(dir)
            if (!f.exists()) {
                f.parentFile.mkdirs()
                f.createNewFile()
            }
            val out = FileOutputStream(f)
            bm.compress(Bitmap.CompressFormat.JPEG, 90, out)
            out.flush()
            out.close()
            return Uri.fromFile(f)
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return null
    }
}