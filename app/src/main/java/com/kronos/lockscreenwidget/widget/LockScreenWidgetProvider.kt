package com.kronos.lockscreenwidget.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.RemoteViews
import com.kronos.lockscreenwidget.R

class LockScreenWidgetProvider : AppWidgetProvider(){

    val WIDGETTAG = "LockScreenWidget"

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray?) {

        val componentName = ComponentName(context, LockScreenWidgetProvider::class.java)
        Log.i(WIDGETTAG, "onUpdate")
        val allWidgetIds = appWidgetManager.getAppWidgetIds(componentName)

        /*final int N = appWidgetIds.length;*/

        // Perform this loop procedure for each App Widget that belongs to this provider
        for (i in allWidgetIds.indices) {
            val appWidgetId = allWidgetIds[i]

            Log.i(WIDGETTAG, "updating widget[id] $appWidgetId")
            val views = RemoteViews(context.packageName, R.layout.widget_layout)

            val bundle = appWidgetManager.getAppWidgetOptions(appWidgetId)
            updateWidgetSize(bundle, views)

            val intentLock = Intent(context, LockScreenWidgetService::class.java)
            intentLock.action = LockScreenWidgetService().lock
            intentLock.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds)

            val pendingIntentLock = PendingIntent.getService(context, 0, intentLock, 0)

            views.setOnClickPendingIntent(R.id.widget_button_lock, pendingIntentLock)

            Log.i(WIDGETTAG, "pending intent set")

            // Tell the AppWidgetManager to perform an update on the current App Widget
            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
    }

    override fun onAppWidgetOptionsChanged(context: Context, appWidgetManager: AppWidgetManager, appWidgetId: Int, newOptions: Bundle) {
        val views = RemoteViews(context.packageName, R.layout.widget_layout)
        updateWidgetSize(newOptions, views)
        appWidgetManager.updateAppWidget(appWidgetId, views)
    }

    private fun updateWidgetSize(bundle: Bundle, views: RemoteViews) {
        val minWidth = bundle.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_WIDTH)
        val minHeight = bundle.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_HEIGHT)
        val maxWidth = bundle.getInt(AppWidgetManager.OPTION_APPWIDGET_MAX_WIDTH)
        val maxHeight = bundle.getInt(AppWidgetManager.OPTION_APPWIDGET_MAX_HEIGHT)
        /*if (maxHeight > 84) {
            views.setViewVisibility(R.id.layout_hide, View.VISIBLE)
        } else {
            views.setViewVisibility(R.id.layout_hide, View.GONE)
        }*/
    }


}