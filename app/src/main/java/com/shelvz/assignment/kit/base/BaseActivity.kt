package com.shelvz.assignment.kit.base

import android.content.Context
import android.support.v7.app.AppCompatActivity

/**
 * Created by shafic on 8/20/17.
 */

abstract class BaseActivity : AppCompatActivity() {


    /** Call Children fragments on Back pressed
     * */
    override fun onBackPressed() {
        if (!BaseFragment.handleBackPressed(supportFragmentManager)) {
            super.onBackPressed()
        }
    }

    override fun attachBaseContext(base: Context) {
        var base = base

        //Handle custom fonts if needed
        //base = CalligraphyContextWrapper.wrap(base)

        super.attachBaseContext(base)
    }
}
