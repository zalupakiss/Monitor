package com.example.screenoffauto

import android.accessibilityservice.AccessibilityService
import android.os.Handler
import android.os.Looper
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo

class ScreenOffService : AccessibilityService() {
    companion object { var instance: ScreenOffService? = null }
    private val handler = Handler(Looper.getMainLooper())

    override fun onServiceConnected() { super.onServiceConnected(); instance = this }
    override fun onUnbind(intent: android.content.Intent?): Boolean { instance = null; return super.onUnbind(intent) }
    override fun onAccessibilityEvent(event: AccessibilityEvent?) {}
    override fun onInterrupt() {}

    fun runMacro() {
        handler.postDelayed({
            moveDownAndClick(4)
            handler.postDelayed({
                moveDownAndClick(6)
                handler.postDelayed({
                    moveDownAndClick(1)
                }, 600)
            }, 600)
        }, 1200)
    }

    private fun moveDownAndClick(steps: Int) {
        val root = rootInActiveWindow ?: return
        var currentFocus = root.findFocus(AccessibilityNodeInfo.FOCUS_INPUT)
        for (i in 0 until steps) {
            val next = currentFocus?.focusSearch(android.view.View.FOCUS_DOWN)
            if (next != null) {
                next.performAction(AccessibilityNodeInfo.ACTION_FOCUS)
                currentFocus = next
            }
        }
        currentFocus?.performAction(AccessibilityNodeInfo.ACTION_CLICK)
    }
}
