package com.shelvz.assignment.models

/**
 * Created by shafic on 8/6/17.
 */

//Data Type that wraps anything, even nulls
data class Optional<out T>(val value: T?)
