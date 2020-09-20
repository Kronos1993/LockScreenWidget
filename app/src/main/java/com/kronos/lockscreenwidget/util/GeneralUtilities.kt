package com.kronos.kotlinsplashscreen.util

import android.app.Activity
import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.telephony.SmsManager
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityCompat
import com.kronos.lockscreenwidget.widget.LockScreenWidgetProvider
import java.io.File

class GeneralUtilities {

    /*Generic method for navigate from any class to anywhere*/
    fun navigate(context: Context, clazz: Class<*>?) {
        val intentHome = Intent(context, clazz)
        intentHome.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        context.startActivity(intentHome)
    }

    fun navigate(context: Context, clazz: Class<*>?, key: String?, extra: String?) {
        val intentHome = Intent(context, clazz)
        intentHome.putExtra(key, extra)
        intentHome.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        context.startActivity(intentHome)
    }

    fun navigate(context: Context, clazz: Class<*>?, extra: Bundle?) {
        val intentHome = Intent(context, clazz)
        intentHome.putExtras(extra!!)
        intentHome.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        context.startActivity(intentHome)
    }

    fun navigateFlagNewTask(context: Context, clazz: Class<*>?) {
        val intentHome = Intent(context, clazz)
        intentHome.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intentHome)
    }

    fun rateApp(context: Context, view: View?) {
        try {
            context.startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("market://id?" + context.packageName)
                )
            )
        } catch (e: Exception) {
            context.startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/sttore/apps/details?id=" + context.packageName)
                )
            )
        }
    }

    fun sendSMS(context: Context, number: String, sms: String) {
        val smsManager = SmsManager.getDefault()
        smsManager.sendTextMessage(number, null, sms, null, null)
    }


    fun setTheme(sharedPreferencesSetings: SharedPreferences) {
        val theme =
            sharedPreferencesSetings.getString("theme", "MODE_NIGHT_FOLLOW_SYSTEM")
        if (theme.equals("MODE_NIGHT_NO", ignoreCase = true)) {
            AppCompatDelegate.setDefaultNightMode(
                AppCompatDelegate.MODE_NIGHT_NO
            )
        } else if (theme.equals("MODE_NIGHT_YES", ignoreCase = true)) {
            AppCompatDelegate.setDefaultNightMode(
                AppCompatDelegate.MODE_NIGHT_YES
            )
        } else if (theme.equals("MODE_NIGHT_FOLLOW_SYSTEM", ignoreCase = true)) {
            AppCompatDelegate.setDefaultNightMode(
                AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
            )
        } else if (theme.equals("MODE_NIGHT_AUTO_BATTERY", ignoreCase = true)) {
            AppCompatDelegate.setDefaultNightMode(
                AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY
            )
        }
    }


    fun updateWidget(context: Context) {
        val intent = Intent(context, LockScreenWidgetProvider::class.java)
        intent.action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
        val ids = AppWidgetManager.getInstance(context).getAppWidgetIds(ComponentName(context, LockScreenWidgetProvider::class.java))
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids)
        context.sendBroadcast(intent)
    }

    fun shareAPK(activity: Activity) {
        val packageManager = activity.packageManager
        val applicationInfo: ApplicationInfo
        try {
            applicationInfo = activity.applicationContext.applicationInfo
            val filePath = applicationInfo.sourceDir
            val sendIntent = Intent(Intent.ACTION_SEND)
            sendIntent.type = "*/*"
            sendIntent.putExtra(
                Intent.EXTRA_STREAM,
                Uri.fromFile(File(filePath))
            )
            activity.startActivity(Intent.createChooser(sendIntent, "Compartir mediante:"))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun validatePermiso(context: Context, perm: String): Boolean {
        var permiso_granted = false
        if (ActivityCompat.checkSelfPermission(
                context!!,
                perm!!
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            permiso_granted = true
        }
        return permiso_granted
    }

    fun getVersionName(ctx: Context): String? {
        return try {
            ctx.packageManager.getPackageInfo(ctx.packageName, 0).versionName
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
            ""
        }
    }

}