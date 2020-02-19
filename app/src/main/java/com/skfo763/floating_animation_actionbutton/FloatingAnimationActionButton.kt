package com.skfo763.floating_animation_actionbutton

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.view.SupportMenuInflater
import androidx.appcompat.view.menu.MenuBuilder
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.internal.ViewUtils

class FloatingAnimationActionButton
@JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyle: Int = 0):
        ConstraintLayout(context, attrs, defStyle) {

    private var _isOpened = false
    var isOpened: Boolean get() = _isOpened

    private var selectedListener: OnSubButtonClickListener? = null
    private var menu: MenuBuilder = FloatingSubActionButtonMenu(context)

    private var menuInflater: MenuInflater? = null

    private val subItemMenuCallback = object: MenuBuilder.Callback {
        override fun onMenuItemSelected(menu: MenuBuilder?, item: MenuItem?): Boolean {
            if(item == null) return false
            selectedListener?.onSubButtonClicked(item)
            return true
        }
        override fun onMenuModeChange(menu: MenuBuilder?) {}
    }

    // get typedArray attributes from xml file
    init {
        this.isNestedScrollingEnabled = false
        val typedArray = context.theme.obtainStyledAttributes(
            attrs, R.styleable.FloatingAnimationActionButton, 0, 0)
        try {
            isOpened = typedArray.getBoolean(R.styleable.FloatingAnimationActionButton_isOpened, false)
        } finally {
            typedArray.recycle()
        }

        menu.setCallback(subItemMenuCallback)
    }

    fun inflateMenu(resId: Int) {
        getMenuInflater().inflate(resId, menu)
    }

    private fun getMenuInflater(): MenuInflater {
        return menuInflater?.let { it } ?:
        kotlin.run { return SupportMenuInflater(context).apply { menuInflater = this } }
    }

    fun getMaxSubButtonItemCount() = FloatingSubActionButtonMenu.MAX_ITEM_COUNT

    @SuppressLint("RestrictedApi")
    private fun applyWindowInsets() {
        ViewUtils.doOnApplyWindowInsets(this
        ) { view, insets, initialPadding ->
            initialPadding.bottom += insets.systemWindowInsetBottom
            initialPadding.applyToView(view)
            insets
        }
    }

    interface OnSubButtonClickListener {
        /**
         *  Called when some sub button is clicked
         *  @param item The selected button
         */
        fun onSubButtonClicked(item: MenuItem)
    }
}