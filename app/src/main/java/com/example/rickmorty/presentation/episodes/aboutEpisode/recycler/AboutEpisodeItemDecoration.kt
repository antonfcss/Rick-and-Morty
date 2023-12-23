package com.example.rickmorty.presentation.episodes.aboutEpisode.recycler

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class AboutEpisodeItemDecoration(
    private val spacing: Int,
    private val topSpacing: Int
) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State,
    ) {
        val layoutManager = parent.layoutManager as? GridLayoutManager ?: return
        val spanCount = layoutManager.spanCount
        val position = parent.getChildAdapterPosition(view)
        val column = position % spanCount
        val leftOffset = spacing - column * spacing / spanCount
        val rightOffset = (column + 1) * spacing / spanCount
        outRect.left = leftOffset
        outRect.right = rightOffset
        outRect.top = topSpacing
    }
}
