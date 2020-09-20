package com.kronos.lockscreenwidget.activities

import android.app.Activity
import android.app.ActivityManager
import android.app.admin.DevicePolicyManager
import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.preference.PreferenceManager
import android.provider.Settings
import android.view.View
import android.widget.ImageButton
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.kronos.kotlinsplashscreen.util.GeneralUtilities
import com.kronos.lockscreenwidget.R
import com.kronos.lockscreenwidget.activities.admin.DeviceAdmin

class PermissionWidgetActivity : AppCompatActivity() {

    lateinit var switcherPermission: Switch
    lateinit var fabButton : FloatingActionButton
    lateinit var imageBtnAbout : ImageButton
    lateinit var textExpPermiso : TextView
    lateinit var sharedPreferences: SharedPreferences

    val PERMISSIONS_REQUEST_CODE = 1240
    val RESULT_ENABLE = 11
    var appWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_permission)

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)

        val intent = intent
        val extras = intent.extras
        if (extras != null) {
                appWidgetId = extras.getInt(
                AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID
            )
        }

        val result = Intent()
        result.putExtra(
            AppWidgetManager.EXTRA_APPWIDGET_ID,
            appWidgetId
        )
        setResult(Activity.RESULT_CANCELED, result)

        if (appWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish()
        }

        if(checkAndRequestPermisions()){
            init()
        }
    }

    fun init(){
        var compName : ComponentName = ComponentName(this, DeviceAdmin::class.java)
        var devicePolicyManager = getSystemService(DEVICE_POLICY_SERVICE) as DevicePolicyManager
        var activityManager = getSystemService(ACTIVITY_SERVICE) as ActivityManager
        switcherPermission = findViewById(R.id.swicht_permission)
        textExpPermiso = findViewById(R.id.textViewPermisosExplicacion)

        if(sharedPreferences.getBoolean("permission_enabled",false)){
            GeneralUtilities().updateWidget(this.baseContext)
            val result = Intent()
            result.putExtra(
                AppWidgetManager.EXTRA_APPWIDGET_ID,
                appWidgetId
            )
            setResult(Activity.RESULT_OK, result)
            switcherPermission.isChecked = sharedPreferences.getBoolean("permission_enabled",false)
            switcherPermission.setText(getString(R.string.quitar_permisos))
            textExpPermiso.setText(getString(R.string.porque_el_permiso))
        }else{
            GeneralUtilities().updateWidget(this.baseContext)
            val result = Intent()
            result.putExtra(
                AppWidgetManager.EXTRA_APPWIDGET_ID,
                appWidgetId
            )
            setResult(Activity.RESULT_CANCELED, result)
            switcherPermission.isChecked = sharedPreferences.getBoolean("permission_enabled",false)
            switcherPermission.setText(getString(R.string.otorgar_permisos))
            textExpPermiso.setText(getString(R.string.permiso_perdido))
        }
        switcherPermission.setOnCheckedChangeListener { compoundButton, b ->
            run {
                if (b) {
                    val intent = Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN)
                    intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, compName)
                    intent.putExtra(
                        DevicePolicyManager.EXTRA_ADD_EXPLANATION,
                        getString(R.string.why_this_perm)
                    )
                    switcherPermission.setText(getString(R.string.quitar_permisos))
                    startActivityForResult(intent, RESULT_ENABLE)
                } else{
                    GeneralUtilities().updateWidget(this.baseContext)
                    val result = Intent()
                    result.putExtra(
                        AppWidgetManager.EXTRA_APPWIDGET_ID,
                        appWidgetId
                    )
                    setResult(Activity.RESULT_CANCELED, result)

                    sharedPreferences.edit().putBoolean("permission_enabled",false).commit()
                    devicePolicyManager.removeActiveAdmin(compName);
                    switcherPermission.setText(getString(R.string.otorgar_permisos))
                    textExpPermiso.setText(getString(R.string.permiso_perdido))
                    Toast.makeText(this, getString(R.string.permission_needed_explain), Toast.LENGTH_SHORT).show()
                }
            }
        }

        fabButton = findViewById(R.id.fab_finish)
        fabButton.setOnClickListener { view -> finish() }
        imageBtnAbout = findViewById(R.id.imageButtonAbout)
        imageBtnAbout.visibility= View.GONE
    }

    fun checkAndRequestPermisions(): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if(!Settings.canDrawOverlays(this)){
                var intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:${this.packageName}"))
                startActivityForResult(intent, PERMISSIONS_REQUEST_CODE)
            }
        } else {

        }

        return true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            RESULT_ENABLE ->{
                if (resultCode == Activity.RESULT_OK) {
                    sharedPreferences.edit().putBoolean("permission_enabled",true).commit()
                    textExpPermiso.setText(getString(R.string.porque_el_permiso))
                    switcherPermission.setText(getString(R.string.quitar_permisos))
                    //GeneralUtilities().navigate(this,MainActivity::class.java)
                    GeneralUtilities().updateWidget(this.baseContext)
                    val result = Intent()
                    result.putExtra(
                        AppWidgetManager.EXTRA_APPWIDGET_ID,
                        appWidgetId
                    )
                    setResult(Activity.RESULT_OK, result)
                } else {
                    GeneralUtilities().updateWidget(this.baseContext)
                    val result = Intent()
                    result.putExtra(
                        AppWidgetManager.EXTRA_APPWIDGET_ID,
                        appWidgetId
                    )
                    setResult(Activity.RESULT_CANCELED, result)
                    switcherPermission.setText(getString(R.string.otorgar_permisos))
                    sharedPreferences.edit().putBoolean("permission_enabled",false).commit()
                    textExpPermiso.setText(getString(R.string.permiso_perdido))
                }
            }
            PERMISSIONS_REQUEST_CODE ->{
                if (resultCode == Activity.RESULT_OK) {
                    sharedPreferences.edit().putBoolean("overlay_enabled",true).commit()
                    init()
                } else {
                    sharedPreferences.edit().putBoolean("overlay_enabled",false).commit()
                }
            }

        }
        super.onActivityResult(requestCode, resultCode, data)
    }
}
