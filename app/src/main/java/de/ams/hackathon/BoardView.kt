package de.ams.hackathon

/* TODOs

1) Game states - run, finish (success/fail)
2) Select level, call engine start/stop
3) game ui (buttons, interactions)
4) graphics

 */

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.core.graphics.toColor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.lang.Integer.max
import java.lang.Integer.min
import java.lang.Math.sin
import kotlin.coroutines.CoroutineContext
import kotlin.math.roundToInt

class BoardView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : View(context, attrs, defStyleAttr), CoroutineScope {

    val engine = Engine()

    private val colors = arrayOf(
        Color.BLACK,    // fWall
        Color.WHITE,    // fEmpty
        Color.LTGRAY,
        Color.WHITE,    // fCoin
        Color.MAGENTA,
        0xFF2b6fc1.toInt(), // fDestination
        Color.GREEN,
        Color.LTGRAY,    // fOrigin
        Color.GREEN
    )

    private val paint = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.FILL
        color = colors[0]
    }

    private var router: Router = BasicRouter()

    private val updateJob = Job()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + updateJob

    private val rect = Rect()
    private var tick: UInt = 0U
    private var framesPerStep = 10 //  == 1 / speed
    private var frame = 0
    var running = true

    fun onPlayPressed() {
        running = !running
    }

    fun onToggleRouter() {
        engine.basic = !engine.basic

        router = if (engine.basic) BasicRouter() else AdvancedRouter()
        onLevelSelected(engine.level)
    }

    fun onBackPressed() {
        engine.reset()
    }

    fun onPlusPressed() {
        framesPerStep = max(1, framesPerStep - 1)
        frame = min(frame, framesPerStep - 1)
    }

    fun onMinusPressed() {
        framesPerStep += 1
    }

    /**
     * @param level Level 0 - 9
     **/
    fun onLevelSelected(level: Int) {
        // TODO NOT IMPLEMENTED YET
        running = false
        engine.level = level
        engine.reset()
        engine.path = router.solve(engine.columns, engine.columns, engine.world, engine.origin, engine.destination, engine.coinValue)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()

        launch {
            engine.reset()
            engine.path =
                router.solve(engine.columns, engine.columns, engine.world, engine.origin, engine.destination, engine.coinValue)

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

        tick++

        // draw world

        val size = width.toFloat() / engine.columns.toFloat()

        for (y in 0 until engine.rows) {
            for (x in 0 until engine.columns) {
                val thisTick = tick + (y * engine.columns + x).toUInt()
                val phase = ((thisTick % 50U).toFloat() / 50F * 2F * 3.1415F).toDouble()
                val delta = 0.5F + (0.5F * sin(phase).toFloat())

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
                        val clipped = delta * 0.8F
                        paint.color = 0xffff8000.toInt()
                        canvas.drawOval(x1 + size * clipped * 0.5F, y1, x2 - size * clipped * 0.5F, y2, paint)
                        paint.color = 0xffffb020.toInt()
                        canvas.drawOval(x1 + size * clipped * 0.5F + 1F, y1 + 1F, x2 - size * clipped * 0.5F - 1F, y2 - 1F, paint)
                    }
                    fDestination -> {
                        paint.color = 0xFF2b6fc1.toInt();
                    }
                    fOrigin -> {
                        paint.color = Color.LTGRAY;
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
            running = engine.step()
            frame = 0
        } else {
            // regular drawing frame
        }

        // draw player

        val d = if (running) frame.toDouble() / framesPerStep.toDouble() else 1.0
        val d1 = 1.0 - d

        val thisTick = tick
        val phase = ((thisTick % 20U).toFloat() / 20F * 2F * 3.1415F).toDouble()
        val delta = 0.5F + (0.5F * sin(phase).toFloat())

        val x1 =
            (engine.position.x.toDouble() * d + engine.lastPosition.x.toDouble() * d1) * size.toDouble()
        val x2 = x1 + size
        val y1 =
            (engine.position.y.toDouble() * d + engine.lastPosition.y.toDouble() * d1) * size.toDouble()
        val y2 = y1 + size

        val direction = -90 + 90 * engine.currentDirection

        val r = RectF(x1.toFloat(), y1.toFloat(), x2.toFloat(), y2.toFloat())
        paint.color = 0xffff2000.toInt()
        canvas.drawArc(r, direction + 45F * delta, 360F - 90F * delta , true, paint)
        // canvas.drawRect(rect, paint)

        paint.color = Color.BLACK
        paint.textSize = 60F

        if (running) {
            canvas.drawText("Moving to ${engine.position.x}:${engine.position.y}\nStep ${engine.step+1}/${engine.path.size}", 20F, 1200F, paint)
        } else {
            if (engine.step == engine.path.size) {
                val success = engine.destination == engine.position
                if (success) {
                    paint.color = 0xff008020.toInt()
                    canvas.drawText("BOOYAH you reached the destination!", 20F, 1200F, paint)
                    canvas.drawText("You walked ${engine.path.size} steps.", 20F, 1300F, paint)
                    canvas.drawText("Travel cost: \$${engine.path.size - engine.collected * engine.coinValue}", 20F, 1500F, paint)
                } else {
                    paint.color = Color.RED
                    canvas.drawText("You missed the target.", 20F, 1200F, paint)
                }
            } else {
                if (engine.dead) {
                    // probably dead
                    paint.color = Color.RED
                    canvas.drawText("You are out of bounds or ran into a wall", 20F, 1200F, paint)
                }
            }
        }

        canvas.drawText("Collected ${engine.collected} coins", 20F, 1400F, paint)
    }
}