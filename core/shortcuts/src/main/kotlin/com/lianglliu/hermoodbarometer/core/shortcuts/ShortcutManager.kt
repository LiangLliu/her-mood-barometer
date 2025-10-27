package com.lianglliu.hermoodbarometer.core.shortcuts

interface ShortcutManager {
    /**
     * Add a shortcut to quickly record mood
     */
    fun addRecordMoodShortcut()

    /**
     * Remove all dynamic shortcuts
     */
    fun removeShortcuts()
}