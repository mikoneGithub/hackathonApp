package de.ams.hackathon

import android.content.Context
import android.content.res.Resources
import android.graphics.Canvas
import android.util.AttributeSet
import android.util.TypedValue
import com.google.android.material.card.MaterialCardView

class BoardView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : MaterialCardView(context, attrs, defStyleAttr) {


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val size = resources.displayMetrics.widthPixels - 2 * MARGIN
        setMeasuredDimension(size, size)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        // TODO @nils hier kannst du zeichnen
    }

    private companion object {
        val MARGIN = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            16f,
            Resources.getSystem().displayMetrics,
        ).toInt()
    }
}