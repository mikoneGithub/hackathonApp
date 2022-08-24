package de.ams.hackathon

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext
import kotlin.math.roundToInt

class BoardView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : View(context, attrs, defStyleAttr), CoroutineScope {

    private val engine = Engine()

    private val colors = arrayOf(
        Color.BLACK,    // fWall
        Color.WHITE,    // fEmpty
        Color.RED,
        Color.WHITE,   // fCoin
        Color.MAGENTA,
        Color.GREEN,    // fDestination
        Color.GREEN
    )

    private val paint = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.FILL
        color = colors[0]
    }

    private val router = BasicRouter()

    private val updateJob = Job()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + updateJob

    private val rect = Rect()

    val framesPerStep = 15 //  == 1 / speed
    var frame = 0
    var running = true

    // TODO KON: Hier hätte ich gerne die Callbacks von den Buttons
    fun pauseTapped() {}
    // undsoweiterundsofort

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()

        launch {
            engine.path = router.solve(width, height, engine.world, engine.origin, engine.destination)

            while (true) {
                invalidate()
                delay(30L)
            }
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        updateJob.cancel()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // draw world

        val size = canvas.width / engine.columns

        for (y in 0..engine.rows - 1) {
            for (x in 0..engine.columns - 1) {
                val x1 = x * size
                val x2 = x1 + size
                val y1 = y * size
                val y2 = y1 + size
                val r = Rect(x1, y1, x2, y2)

                val type = engine.world[x][y]
                paint.color = colors[type]
                canvas.drawRect(r, paint)

                when (type) {
                    fCoin -> {
                        paint.color = Color.YELLOW
                        canvas.drawCircle((x1 + x2) / 2F, (y1 + y2) / 2F, size / 2F, paint)
                    }
                }
            }
        }

        // step world

        if (running) {
            frame++
        }

        if (frame == framesPerStep) {
            // step frame
            engine.step()
            frame = 0
        } else {
            // regular drawing frame
        }

        // draw player

        val d = frame.toDouble() / framesPerStep.toDouble()
        val d1 = 1.0 - d

        val x1 = (engine.position.x.toDouble() * d + engine.lastPosition.x.toDouble() * d1) * size.toDouble()
        val x2 = x1 + size
        val y1 = (engine.position.y.toDouble() * d + engine.lastPosition.y.toDouble() * d1) * size.toDouble()
        val y2 = y1 + size

        val r = Rect(x1.roundToInt(), y1.roundToInt(), x2.roundToInt(), y2.roundToInt())
        paint.color = Color.GREEN
        canvas.drawRect(r, paint)

        paint.setColor(Color.BLACK);
        paint.setTextSize(60F);
        canvas.drawText("${engine.position}", 20F, 1200F, paint)
    }
}