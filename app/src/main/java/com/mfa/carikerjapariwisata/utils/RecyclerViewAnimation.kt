package com.mfa.carikerjapariwisata.utils

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Recycler


class RecyclerViewAnimation : LinearLayoutManager {
    private val mShrinkAmount = 0.15f
    private val mShrinkDistance = 0.9f

    constructor(context: Context?, orientation: Int, reverseLayout: Boolean) : super(
        context,
        orientation,
        reverseLayout
    )

    override fun scrollVerticallyBy(
        dy: Int,
        recycler: Recycler,
        state: RecyclerView.State
    ): Int {
        val orientation = orientation
        return if (orientation == VERTICAL) {
            val scrolled = super.scrollVerticallyBy(dy, recycler, state)
            val midpoint = height / 2f
            val d0 = 0f
            val d1 = mShrinkDistance * midpoint
            val s0 = 1f
            val s1 = 1f - mShrinkAmount
            for (i in 0 until childCount) {
                val child: View? = getChildAt(i)
                val childMidpoint =
                    (child?.let { getDecoratedBottom(it) }?.plus(getDecoratedTop(child)))?.div(2f)
                val d =
                    Math.min(d1, Math.abs(midpoint - childMidpoint!!))
                val scale = s0 + (s1 - s0) * (d - d0) / (d1 - d0)
                child.setScaleX(scale)
                child.setScaleY(scale)
            }
            scrolled
        } else {
            0
        }
    }

    override fun scrollHorizontallyBy(
        dx: Int,
        recycler: Recycler,
        state: RecyclerView.State
    ): Int {
        val orientation = orientation
        return if (orientation == HORIZONTAL) {
            val scrolled = super.scrollHorizontallyBy(dx, recycler, state)
            val midpoint = width / 2f
            val d0 = 0f
            val d1 = mShrinkDistance * midpoint
            val s0 = 1f
            val s1 = 1f - mShrinkAmount
            for (i in 0 until childCount) {
                val child: View? = getChildAt(i)
                val childMidpoint =
                    (child?.let { getDecoratedRight(it) }
                        ?.plus(child?.let { getDecoratedLeft(it) }!!))?.div(2f)
                val d = Math.min(d1, Math.abs(midpoint - childMidpoint!!))
                val scale = s0 + (s1 - s0) * (d - d0) / (d1 - d0)
                child.setScaleX(scale)
                child.setScaleY(scale)
            }
            scrolled
        } else {
            0
        }
    }
}