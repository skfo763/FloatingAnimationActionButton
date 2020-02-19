package com.skfo763.floating_animation_actionbutton

import android.content.Context
import android.view.MenuItem
import android.view.SubMenu
import androidx.appcompat.view.menu.MenuBuilder
import androidx.appcompat.view.menu.MenuItemImpl
import java.lang.IllegalStateException

class FloatingSubActionButtonMenu constructor(context: Context): MenuBuilder(context) {

    companion object {
        const val MAX_ITEM_COUNT = 10
        const val UNSUPPORTED_ERROR_MSG = "FloatingAnimationActionButton does not support submenus"
        const val OVERFLOW_MAX_ITEM = "Maximum number of items supported by FloatingAnimationActionButton is $MAX_ITEM_COUNT Limit can be checked with FloatingAnimationActionButton#getMaxItemCount()"
    }

    override fun addSubMenu(
        group: Int,
        id: Int,
        categoryOrder: Int,
        title: CharSequence?
    ): SubMenu {
        throw UnsupportedOperationException(UNSUPPORTED_ERROR_MSG)
    }

    override fun addInternal(
        group: Int,
        id: Int,
        categoryOrder: Int,
        title: CharSequence?
    ): MenuItem {
        if(size() + 1 > MAX_ITEM_COUNT) {
            throw IllegalStateException(OVERFLOW_MAX_ITEM)
        }
        stopDispatchingItemsChanged()
        val item = super.addInternal(group, id, categoryOrder, title)
        if(item is MenuItemImpl) { item.isExclusiveCheckable = true }
        startDispatchingItemsChanged()
        return item
    }
}