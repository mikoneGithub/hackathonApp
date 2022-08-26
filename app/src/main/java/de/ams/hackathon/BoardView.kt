package de.ams.hackathon

/* TODOs

1) Game states - run, finish (success/fail)
2) Select level, call engine start/stop
3) game ui (buttons, interactions)
4) graphics

 */

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.util.Log
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
        Color.GREEN,
        Color.GREEN,    // fOrigin
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

    private val framesPerStep = 15 //  == 1 / speed
    private var frame = 0
    private var running = true

    fun onPlayPressed() {
        // TODO NOT IMPLEMENTED YET
        Log.d("BoardView", "onPlayPressed")
    }

    fun onPausePressed() {
        // TODO NOT IMPLEMENTED YET
        Log.d("BoardView", "onPausePressed")
    }

    fun onBackPressed() {
        // TODO NOT IMPLEMENTED YET
        Log.d("BoardView", "onBackPressed")
    }

    /**
     * @param level Level 0 - 9
     **/
    fun onLevelSelected(level: Int) {
        // TODO NOT IMPLEMENTED YET
        Log.d("BoardView", "onLevelSelected $level")
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()

        launch {
            engine.reset()
            engine.path =
                router.solve(width, height, engine.world, engine.origin, engine.destination)

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

        val size = width.toFloat() / engine.columns.toFloat()

        for (y in 0 until engine.rows) {
            for (x in 0 until engine.columns) {
                val x1 = x * size
                val x2 = x1 + size
                val y1 = y * size
                val y2 = y1 + size
                rect.set(x1.roundToInt(), y1.roundToInt(), x2.roundToInt(), y2.roundToInt())

                val type = engine.world[x][y]
                paint.color = colors[type]
                canvas.drawRect(rect, paint)

                when (type) {
                    fCoin -> {
                        paint.color = Color.YELLOW
                        canvas.drawCircle((x1 + x2) / 2F, (y1 + y2) / 2F, size / 2F, paint)
                    }
                    fDestination -> {
                        paint.color = Color.RED
                        canvas.drawCircle((x1 + x2) / 2F, (y1 + y2) / 2F, size / 2F, paint)
                    }
                    fOrigin -> {
                        paint.color = Color.CYAN
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

        val x1 =
            (engine.position.x.toDouble() * d + engine.lastPosition.x.toDouble() * d1) * size.toDouble()
        val x2 = x1 + size
        val y1 =
            (engine.position.y.toDouble() * d + engine.lastPosition.y.toDouble() * d1) * size.toDouble()
        val y2 = y1 + size

        rect.set(x1.roundToInt(), y1.roundToInt(), x2.roundToInt(), y2.roundToInt())
        paint.color = Color.GREEN
        canvas.drawRect(rect, paint)

        paint.color = Color.BLACK
        paint.textSize = 60F
        canvas.drawText("${engine.position}", 20F, 1200F, paint)
    }
}