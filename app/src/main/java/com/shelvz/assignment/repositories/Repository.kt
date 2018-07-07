package com.shelvz.assignment.repositories

import android.content.Context
import io.reactivex.Flowable

interface Repository<T> {
    val context: Context
    fun delete(): Flowable<Unit>
}
