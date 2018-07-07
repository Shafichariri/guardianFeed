package com.shelvz.assignment.kit.databinding

import android.app.Application
import android.arch.lifecycle.AndroidViewModel

/**
 * Created by shafic on 7/29/17.
 */
open class BaseViewModel : AndroidViewModel {
    constructor(application: Application) : super(application)
}
