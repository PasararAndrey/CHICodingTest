package dev.chicodingtest.ui.animals.adapters

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class ListItemDecorator(
    private val horizontalOffset: Int = 0,
    private val outerVerticalOffset: Int = 0,
    private val innerVerticalOffset: Int = 0,
) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)
        val currentPosition = parent.getChildAdapterPosition(view).takeIf { it != RecyclerView.NO_POSITION } ?: return
        with(outRect) {
            left = horizontalOffset
            right = horizontalOffset
            top = if (isFirstItem(currentPosition)) outerVerticalOffset else innerVerticalOffset
            bottom = if (isLastItem(currentPosition, state.itemCount)
            ) outerVerticalOffset else innerVerticalOffset
        }
    }

    private fun isFirstItem(position: Int): Boolean {
        return position == 0
    }

    private fun isLastItem(position: Int, itemCount: Int): Boolean {
        return position == (itemCount - 1)
    }
}