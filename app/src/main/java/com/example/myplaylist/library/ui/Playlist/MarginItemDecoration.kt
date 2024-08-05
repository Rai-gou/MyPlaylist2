package com.example.myplaylist.library.ui.Playlist

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MarginItemDecoration(
    private val marginBetweenItems: Int,
    private val marginToScreenEdges: Int,
    private val marginTopBetweenItems: Int
) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)
        val position = parent.getChildAdapterPosition(view)
        val spanCount = (parent.layoutManager as GridLayoutManager).spanCount
        val isTopRow = position < spanCount

        outRect.top = if (isTopRow) marginTopBetweenItems else marginBetweenItems

        if (position % spanCount == 0) {
            outRect.left = marginToScreenEdges
            outRect.right = marginBetweenItems / 2
        } else {
            outRect.left = marginBetweenItems / 2
            outRect.right = marginToScreenEdges
        }

        // Bottom margin for last row items
        if (position >= state.itemCount - spanCount) {
            outRect.bottom = marginBetweenItems
        }
    }
}