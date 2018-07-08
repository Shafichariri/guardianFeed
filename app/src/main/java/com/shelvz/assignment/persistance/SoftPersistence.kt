package com.shelvz.assignment.persistance

import android.content.Context
import com.shelvz.assignment.ShelvzApplication
import com.shelvz.assignment.kit.base.BaseApplication


object SoftPersistence {
    private const val preferenceName: String = "app_preferences"
    private val context: Context by lazy { BaseApplication.getInstance<ShelvzApplication>() }
    private val sp = context.getSharedPreferences(preferenceName, Context.MODE_PRIVATE)

    fun get(key: String, default: String): String {
        return sp.getString(key, default)
    }

    fun get(key: String, default: Int = 0): Int {
        return sp.getInt(key, default)
    }

    fun set(key: String, value: String?): Boolean {
        val value = value ?: return false

        return with(sp.edit()) {
            putString(key, value)
            commit()
        }
    }

    fun set(key: String, value: Int?): Boolean {
        val value = value ?: return false

        return with(sp.edit()) {
            putInt(key, value)
            commit()
        }
    }
}
