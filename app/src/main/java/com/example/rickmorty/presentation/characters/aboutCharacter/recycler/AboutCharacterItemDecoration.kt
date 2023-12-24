package com.example.rickmorty.presentation.characters.aboutCharacter.recycler

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class AboutCharacterItemDecoration(
    private val spacing: Int
) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val position = parent.getChildAdapterPosition(view)
        val viewType = parent.adapter?.getItemViewType(position)
        if (viewType == AboutCharacterAdapter.ABOUT_CHARACTER_EPISODES) {
            with(outRect) {
                left = spacing
                right = spacing
                if (position < (parent.adapter?.itemCount ?: (0 - 1))) {
                    bottom = spacing
                }
            }
        } else {
            outRect.setEmpty()
        }
    }
}