package com.example.myplaylist.library.ui.Playlist

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MarginItemDecoration(
    private val marginBetweenItems: Int,
    private val marginToScreenEdges: Int
) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val position = parent.getChildAdapterPosition(view)
        val spanCount = (parent.layoutManager as GridLayoutManager).spanCount
        val column = position % spanCount

        outRect.left = if (column == 0) {
            marginToScreenEdges
        } else {
            marginBetweenItems / 2
        }

        outRect.right = if (column == spanCount - 1) {
            marginToScreenEdges
        } else {
            marginBetweenItems / 2
        }

        if (position >= spanCount) {
            outRect.top = marginBetweenItems
        } else {
            outRect.top = 0
        }

        outRect.bottom = marginBetweenItems
    }
}