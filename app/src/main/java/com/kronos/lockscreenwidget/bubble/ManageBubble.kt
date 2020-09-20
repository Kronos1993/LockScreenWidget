package com.kronos.lockscreenwidget.bubble

import android.app.Activity
import android.app.admin.DevicePolicyManager
import android.view.LayoutInflater
import android.widget.Toast
import com.kronos.lockscreenwidget.R
import com.kronos.lockscreenwidget.activities.bubbles.BubbleLayout
import com.kronos.lockscreenwidget.activities.bubbles.BubblesManager
import com.kronos.lockscreenwidget.activities.bubbles.OnInitializedCallback

class ManageBubble {

    lateinit var bubbleManager:BubblesManager

    fun initBubble(activity: Activity,devicePolicyManager : DevicePolicyManager) {
        bubbleManager = BubblesManager.Builder(activity)
            .setTrashLayout(R.layout.remove_bubble)
            .setInitializationCallback {
                OnInitializedCallback() {
                    addBubble(activity,bubbleManager,devicePolicyManager)
                }
            }
            .build()
        bubbleManager.initialize()
    }

    private fun addBubble(activity : Activity,manager:BubblesManager,devicePolicyManager : DevicePolicyManager){
        var bubbleView:BubbleLayout = LayoutInflater.from(activity).inflate(R.layout.layout_bubble,null) as BubbleLayout
        bubbleView.setOnBubbleClickListener { bubble ->
            run {
                devicePolicyManager.lockNow();
                Toast.makeText(activity.applicationContext, "click", Toast.LENGTH_LONG).show()
            }
        }
        bubbleView.setOnBubbleRemoveListener { bubble ->
            run {
                Toast.makeText(activity.applicationContext, "remove", Toast.LENGTH_LONG).show()
            }
        }
        bubbleView.setShouldStickToWall(true)
        manager.addBubble(bubbleView,60,20)
    }

}