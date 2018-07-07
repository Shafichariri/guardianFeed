package com.shelvz.assignment.repositories

import android.content.Context

interface Repository<T> {
    val context: Context
    fun delete()
}
