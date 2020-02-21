package com.skfo763.floating_animation_actionbutton

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.view.SupportMenuInflater
import androidx.appcompat.view.menu.MenuBuilder
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.internal.ViewUtils

class FloatingAnimationActionButton
@JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyle: Int = 0):
        ConstraintLayout(context, attrs, defStyle) {

    private var selectedListener: OnSubButtonClickListener? = null
    private var menu: MenuBuilder = FloatingSubActionButtonMenu(context)
    private var menuInflater: MenuInflater? = null

    private var iconRes: Drawable? = null
    private var btnColor = Color.parseColor("#AAAAAA")
    private var buttonSize: Float
    private var iconSize: Float
    private var subButtonSize: Float
    private var subButtonMargin: Float
    private var subButtonColor = Color.parseColor("#654321")
    private var subButtonIconSize: Float
    private var _isOpened = false


    var isOpened: Boolean get() = _isOpened

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
            iconRes = typedArray.getDrawable(R.styleable.FloatingAnimationActionButton_icon)
            iconSize = typedArray.getDimension(R.styleable.FloatingAnimationActionButton_iconSize, R.dimen.fab_main_icon_size.toFloat())
            btnColor = typedArray.getColor(R.styleable.FloatingAnimationActionButton_buttonBackgroundColor, 0xAAAAAA)
            buttonSize = typedArray.getDimension(R.styleable.FloatingAnimationActionButton_buttonSize, R.dimen.fab_main_button_margin_top.toFloat())
            subButtonSize = typedArray.getDimension(R.styleable.FloatingAnimationActionButton_subButtonSize, R.dimen.fab_sub_button_size.toFloat())
            subButtonColor = typedArray.getColor(R.styleable.FloatingAnimationActionButton_subButtonColor, 0x654321)
            subButtonMargin = typedArray.getDimension(R.styleable.FloatingAnimationActionButton_subButtonMargin, R.dimen.fab_sub_button_margin.toFloat())
            subButtonIconSize = typedArray.getDimension(R.styleable.FloatingAnimationActionButton_subButtonIconSize, R.dimen.fab_sub_icon_size.toFloat())

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