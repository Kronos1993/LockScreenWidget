package com.kronos.lockscreenwidget.activities

import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.Gravity
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.kronos.lockscreenwidget.R
import com.kronos.lockscreenwidget.activities.adapters.AboutPage.AboutPage
import com.kronos.lockscreenwidget.activities.adapters.AboutPage.Element
import java.util.*

class AboutActivity : AppCompatActivity() {

    private val context: Context = this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //simulateDayNight(/* DAY */ 3);
        val aboutPage: View = AboutPage(this)
            .isRTL(false)
            .setImage(R.mipmap.app_icon)
            .addEmail("mguerral1993@gmail.com") //.addWebsite("http://medyo.github.io/")
            //.addFacebook("the.medy")
            //.addTelegram("")
            .addTwitter("MarcosOctavio93") //.addYoutube("UCdPQtdWIsg7_pi4mrRu46vA")
            .addPlayStore("com.beesource.etecsainternet") //.addInstagram("medyo80")
            .addGitHub("Kronos1993")
            .addItem(getVersionElement())
            .addItem(getCopyRightsElement())
            .create()
        setContentView(aboutPage)
        setTitle(R.string.about_us)
    }


    fun getCopyRightsElement(): Element {
        val copyRightsElement = Element()
        val copyrights = String.format(
            getString(R.string.copy_right),
            Calendar.getInstance()[Calendar.YEAR]
        )
        copyRightsElement.setTitle(copyrights)
        copyRightsElement.setIconDrawable(R.drawable.about_icon_copy_right)
        copyRightsElement.setIconTint(R.color.colorWhite)
        copyRightsElement.setGravity(Gravity.CENTER)
        copyRightsElement.setOnClickListener(View.OnClickListener {
            Toast.makeText(
                this,
                copyrights,
                Toast.LENGTH_SHORT
            ).show()
        })
        return copyRightsElement
    }

    fun getVersionElement(): Element? {
        val versionElement = Element()
        val version = "Versión: " + getVersionName(context)
        versionElement.setTitle(version)
        versionElement.setIconDrawable(R.drawable.ic_version_icon)
        versionElement.setIconTint(R.color.colorPrimary)
        versionElement.setOnClickListener(View.OnClickListener {
            Toast.makeText(
                this,
                version,
                Toast.LENGTH_SHORT
            ).show()
        })
        return versionElement
    }

    fun getVersionName(ctx: Context): String {
        return try {
            ctx.packageManager.getPackageInfo(ctx.packageName, 0).versionName
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
            ""
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        onBackPressed()
        return super.onOptionsItemSelected(item)
    }


}