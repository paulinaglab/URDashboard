package com.gogreenyellow.pglab.urdashboard.ui

import android.content.Context
import android.graphics.Rect
import android.view.Gravity
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.PopupWindow
import com.gogreenyellow.pglab.urdashboard.R

/**
 * Created by Paulina on 19.02.2018.
 */
class SimpleMenu(var context: Context?, options: Array<String>, var selectionIndex: Int) : PopupWindow(context, null, 0, R.style.Widget_AppCompat_Light_PopupMenu) {

    var listView: ListView

    init {
        val adapter = ArrayAdapter<String>(context, R.layout.simple_menu_item_checkable, options)

        listView = ListView(context)
        listView.adapter = adapter
        listView.choiceMode = ListView.CHOICE_MODE_SINGLE
        listView.setItemChecked(selectionIndex, true)
        val listPadding = context!!.resources.getDimensionPixelSize(R.dimen.simple_menu_padding)
        listView.setPadding(0, listPadding, 0, listPadding)
        listView.dividerHeight = 0

        width = context!!.resources.getDimensionPixelSize(R.dimen.simple_menu_dropdown_width)

        contentView = listView
        isFocusable = true
    }

    fun show(parentView: View, anchorBounds: Rect) {
        val listPadding = context!!.resources.getDimensionPixelSize(R.dimen.simple_menu_padding)
        val listItemHeight = context!!.resources.getDimensionPixelSize(R.dimen.simple_menu_item_height)

        // Set start position (for selectionIndex = 0)
        var posY = anchorBounds.top - listPadding + (anchorBounds.height() - listItemHeight) / 2

        // Move the drop-down list up depending on selected item's index
        posY -= listItemHeight * selectionIndex;

        showAtLocation(parentView, Gravity.TOP or Gravity.START, anchorBounds.left, posY)
    }

    fun setOnItemClickListener(listener: AdapterView.OnItemClickListener) {
        listView.setOnItemClickListener(listener)
    }
}