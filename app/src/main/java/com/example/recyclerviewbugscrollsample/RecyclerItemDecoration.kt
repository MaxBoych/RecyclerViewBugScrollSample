package com.example.recyclerviewbugscrollsample

import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.max


interface SectionCallback {

    fun isSection(pos: Int): Boolean

    fun getSectionHeaderName(pos: Int): String?

    fun getSectionItemsAmount(pos: Int): Int?
}

class RecyclerItemDecoration(
    private val context: Context,
    private val headerOffset: Int,
    private val isSticky: Boolean,
    private val sectionCallback: SectionCallback?
) : RecyclerView.ItemDecoration() {

    private var headerView: View? = null
    private var title: TextView? = null
    private var amount: TextView? = null

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)

        val pos = parent.getChildAdapterPosition(view)
        if (sectionCallback != null && sectionCallback.isSection(pos)) {
            outRect.top = headerOffset
        }
    }

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDrawOver(c, parent, state)

        if (headerView == null) {
            headerView = inflateHeader(parent)
            title = headerView?.findViewById(R.id.header_name) as TextView
            amount = headerView?.findViewById(R.id.content_amount) as TextView
            fixLayoutSize(headerView!!, parent)
        }
        var prevTitle = ""
        for (i in 0 until parent.childCount) {
            val child = parent.getChildAt(i)
            val childPos = parent.getChildAdapterPosition(child)

            val title = sectionCallback?.getSectionHeaderName(childPos)
            val amount = sectionCallback?.getSectionItemsAmount(childPos)

            this.title!!.text = title
            this.amount!!.text = if (childPos != 0) {
                amount?.toString() ?: 0.toString()
            } else ""

            if (!prevTitle.equals(title, ignoreCase = true) ||
                (sectionCallback != null && sectionCallback.isSection(childPos))
            ) {
                drawHeader(c, child, headerView!!)
                prevTitle = title ?: "---"
            }
        }
    }

    private fun drawHeader(c: Canvas, child: View, headerView: View) {
        c.save()
        if (isSticky) {
            c.translate(0F, max(0, child.top - headerView.height).toFloat())
        } else {
            c.translate(0F, (child.top - headerView.height).toFloat())
        }
        headerView.draw(c)
        c.restore()
    }

    private fun fixLayoutSize(view: View, viewGroup: ViewGroup) {
        val widthSpec =
            View.MeasureSpec.makeMeasureSpec(viewGroup.width, View.MeasureSpec.EXACTLY)
        val heightSpec = View.MeasureSpec.makeMeasureSpec(
            viewGroup.height,
            View.MeasureSpec.UNSPECIFIED
        )
        val childWidth = ViewGroup.getChildMeasureSpec(
            widthSpec,
            viewGroup.paddingLeft + viewGroup.paddingRight,
            view.layoutParams.width
        )
        val childHeight = ViewGroup.getChildMeasureSpec(
            heightSpec,
            viewGroup.paddingTop + viewGroup.paddingBottom,
            view.layoutParams.height
        )
        view.measure(childWidth, childHeight)
        view.layout(0, 0, view.measuredWidth, view.measuredHeight)
    }

    private fun inflateHeader(recyclerView: RecyclerView): View {
        return LayoutInflater.from(context)
            .inflate(R.layout.content_section_header, recyclerView, false)
    }
}