package com.kronos.lockscreenwidget.widget

import android.app.Service
import android.app.admin.DevicePolicyManager
import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.preference.PreferenceManager
import android.util.Log
import android.widget.RemoteViews
import android.widget.Toast
import com.kronos.kotlinsplashscreen.util.GeneralUtilities
import com.kronos.lockscreenwidget.R

class LockScreenWidgetService : Service () {

    val lock = "LOCK"

    private lateinit var views: RemoteViews

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        super.onStart(intent, startId)
        Log.i(LockScreenWidgetProvider().WIDGETTAG, "onStartCommand")
        lockScreen(intent)
        stopSelf(startId)
        return START_STICKY
    }

    private fun lockScreen(intent: Intent) {
        Log.i(LockScreenWidgetProvider().WIDGETTAG, "This is the intent $intent")
        views = RemoteViews(this.packageName, R.layout.widget_layout)
        if (intent != null) {
            val requestedAction = intent.action
            Log.i(LockScreenWidgetProvider().WIDGETTAG, "This is the action $requestedAction")
            val widgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, 0)
            if (requestedAction != null && requestedAction == lock) {
                val perm = PreferenceManager.getDefaultSharedPreferences(this).getBoolean("permission_enabled",false)
                if (perm){
                    val devicePolicyManager = (this.getSystemService(Context.DEVICE_POLICY_SERVICE) as DevicePolicyManager?)!!
                    //todo investigar los flag que se pueden pasar aqui
                    devicePolicyManager.lockNow();
                }else
                    Toast.makeText(this,getString(R.string.permission_needed_explain_widget),Toast.LENGTH_SHORT).show()
            }
            val appWidgetMan = AppWidgetManager.getInstance(this)
            appWidgetMan.updateAppWidget(widgetId, views)
        }
        GeneralUtilities().updateWidget(applicationContext)

    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

}