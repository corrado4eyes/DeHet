package com.corrado4eyes.dehet.util

import android.content.Context
import android.graphics.*
import android.graphics.drawable.ColorDrawable
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.corrado4eyes.dehet.R
import kotlin.math.absoluteValue

abstract class SwipeToDeleteCallBack(context: Context) :
    ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

    private val deleteIcon = ContextCompat.getDrawable(context, R.drawable.trash_bin)!!
    private val width = deleteIcon.intrinsicWidth
    private val height = deleteIcon.intrinsicHeight
    private val background = ColorDrawable()
    private val backgroundColor = Color.parseColor("#EA2027")
    private val clearPaint = Paint().apply { xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR) }

    private fun clearCanvas(c: Canvas?, left: Float, top: Float, right: Float, bottom: Float) {
        c?.drawRect(left, top, right, bottom, clearPaint)
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return false
    }



    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        val itemView = viewHolder.itemView
        val itemHeight = itemView.bottom - itemView.top
        val isCanceled = dX == 0f && !isCurrentlyActive

        if(isCanceled) {
            clearCanvas(c, itemView.right + dX, itemView.top.toFloat(),
                itemView.right.toFloat(), itemView.bottom.toFloat())
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
            return
        }

        background.color = backgroundColor
        background.setBounds(itemView.right + dX.toInt(),
            itemView.top, itemView.right, itemView.bottom)
        background.draw(c)

        // Increment size of the icon until half of the bar (so 0..0.5)
        val slidedDistance = dX.toInt()
        val mWidth = itemView.right
        val slidedPercentage = slidedDistance / mWidth.toFloat()

        var iconSizePercentage: Float
        var iconWidth = width
        var iconHeight = height

        // Since the swipe is to the left, I would have gotten negative values and the icon
        // would have been shown upside down. So I've added the call .absoluteValue, to avoid that.
        if ((slidedPercentage < 0 && slidedPercentage >= -0.5)) {
            iconSizePercentage = (slidedPercentage / 0.5).toFloat().absoluteValue
            iconWidth = (iconWidth * iconSizePercentage).toInt().absoluteValue
            iconHeight = (iconHeight * iconSizePercentage).toInt().absoluteValue
        }

        val deleteIconTop = itemView.top + (itemHeight - iconHeight) / 2
        val deleteIconMargin = (itemHeight - iconHeight ) / 2
        val deleteIconLeft = itemView.right - deleteIconMargin - iconWidth
        val deleteIconRight = itemView.right - deleteIconMargin
        val deleteIconBottom = deleteIconTop + iconWidth

        deleteIcon.setBounds(deleteIconLeft, deleteIconTop, deleteIconRight, deleteIconBottom)
        deleteIcon.draw(c)

        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
    }
}