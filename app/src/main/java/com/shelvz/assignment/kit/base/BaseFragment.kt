package com.shelvz.assignment.kit.base

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import com.shelvz.assignment.kit.OnBackPressedFragmentInterface

/**
 * Created by shafic on 8/20/17.
 */

abstract class BaseFragment : Fragment(), OnBackPressedFragmentInterface {

    companion object {
        /**
         * TODO: Logic needs update contact shafic or update function by sending also the fragment layout id.
         * */
        fun handleBackPressed(fm: FragmentManager): Boolean {
            val frags = fm.fragments

            //Filter out fragments that are not visible and do not conform to OnBackPressedFragmentInterface
            frags.filter { (it != null) && (it.isVisible) && (it is OnBackPressedFragmentInterface) }
                    .map { it as OnBackPressedFragmentInterface }
                    .forEach {
                        if (it.onBackPressed()) {
                            return true
                        }
                    }
            return false
        }
    }

    /** Default onBackPressed Implementation
     * By default the back action is not consume unless the fragment overrides it and return
     * true. If true was returned the parent activity will not consume again the action again.
     * And then we should handle the back action
     * */
    override fun onBackPressed(): Boolean {
        //Can consume if isVisible && isResumed are true Consume
        //If consume Activity will not perform back action
        return false
    }
}
