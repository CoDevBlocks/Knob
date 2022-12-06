package com.codevblocks.android.knob

import android.content.Context
import android.graphics.*
import android.graphics.Paint.Style
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.TypedValue
import android.view.MotionEvent
import android.view.View
import kotlin.math.atan2
import kotlin.math.floor
import kotlin.math.max
import kotlin.math.min
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class Knob@JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    enum class ProgressMode {
        CONTINUOUS,
        STEP,
        SUBSTEP
    }

    companion object {
        private const val DEFAULT_KNOB_SIZE_DP = 200

        private const val DEFAULT_FILL_COLOR = Color.TRANSPARENT
        private const val DEFAULT_FILL_START_ANGLE = 0F
        private const val DEFAULT_FILL_SWEEP_ANGLE = 360F

        private const val DEFAULT_TRACK_STROKE_COLOR = 0x80000000.toInt() // 0xFF000000.toInt()
        private const val DEFAULT_TRACK_STROKE_WIDTH_DP = 10
        private val DEFAULT_TRACK_STROKE_CAP = Paint.Cap.ROUND
        private const val DEFAULT_TRACK_STROKE_OFFSET_DP = 0

        private val DEFAULT_PROGRESS_MODE = ProgressMode.CONTINUOUS
        private const val DEFAULT_MAX_PROGRESS = 100F
        private const val DEFAULT_PROGRESS = 0F
        private const val DEFAULT_PROGRESS_STROKE_COLOR = 0xB0000000.toInt() // 0xFFFFFFFF.toInt()
        private const val DEFAULT_PROGRESS_STROKE_WIDTH_DP = 6
        private val DEFAULT_PROGRESS_STROKE_CAP = Paint.Cap.ROUND
        private const val DEFAULT_PROGRESS_STROKE_OFFSET_DP = 0
        private const val DEFAULT_PROGRESS_START_ANGLE = DEFAULT_FILL_START_ANGLE
        private const val DEFAULT_PROGRESS_SWEEP_ANGLE = DEFAULT_FILL_SWEEP_ANGLE

        private const val DEFAULT_THUMB_OFFSET_DP = 0
        private const val DEFAULT_THUMB_ANCHOR_X = 0.5F
        private const val DEFAULT_THUMB_ANCHOR_Y = 0.5F
        private const val DEFAULT_THUMB_ROTATION = true

        private const val DEFAULT_STEP_COUNT = 2
        private const val DEFAULT_STEP_OFFSET_DP = 0
        private const val DEFAULT_STEP_ANCHOR_X = 0.5F
        private const val DEFAULT_STEP_ANCHOR_Y = 0.5F

        private const val DEFAULT_SUBSTEP_COUNT = 0
        private const val DEFAULT_SUBSTEP_OFFSET_DP = 0
        private const val DEFAULT_SUBSTEP_ANCHOR_X = 0.5F
        private const val DEFAULT_SUBSTEP_ANCHOR_Y = 0.5F

        private const val DEFAULT_TOUCH_THRESHOLD_DP = 0
        private const val DEFAULT_TOUCH_OFFSET_DP = 0
    }

    private var initialized: Boolean = false

    var knobSize: Int by interceptedUIProperty(0, updateMeasurementsOnChange = true) { value -> max(0, value) }

    var fillColor: Int by uiProperty(DEFAULT_FILL_COLOR)
    var fillStartAngle: Float by interceptedUIProperty(DEFAULT_FILL_START_ANGLE) { value -> if (value.isFinite()) max(0F, value) % 360F else DEFAULT_FILL_START_ANGLE }
    var fillSweepAngle: Float by interceptedUIProperty(DEFAULT_FILL_SWEEP_ANGLE) { value -> if (value.isFinite()) min(360F, max(0F, value)) else DEFAULT_FILL_SWEEP_ANGLE }

    var trackStrokeColor: Int by uiProperty(DEFAULT_TRACK_STROKE_COLOR)
    var trackStrokeWidth: Int by interceptedUIProperty(0, updateMeasurementsOnChange = true) { value -> max(0, value) }
    var trackStrokeCap: Paint.Cap by uiProperty(DEFAULT_TRACK_STROKE_CAP, updateMeasurementsOnChange = true)
    var trackStrokeOffset: Int by uiProperty(0, updateMeasurementsOnChange = true)

    var stepCount: Int  by interceptedUIProperty(DEFAULT_STEP_COUNT) { value -> max(2, value).also { post { progress = progress } } }
    var stepDrawable: Drawable? by uiProperty(updateMeasurementsOnChange = true)
    var stepOffset: Int by uiProperty(DEFAULT_STEP_OFFSET_DP)
    var stepAnchorX: Float by interceptedUIProperty(DEFAULT_STEP_ANCHOR_X) { value -> if (value.isFinite()) min(1F, max(0F, value)) else DEFAULT_STEP_ANCHOR_X }
    var stepAnchorY: Float by interceptedUIProperty(DEFAULT_STEP_ANCHOR_Y, updateMeasurementsOnChange = true) { value -> if (value.isFinite()) min(1F, max(0F, value)) else DEFAULT_STEP_ANCHOR_Y }

    var substepCount: Int  by interceptedUIProperty(DEFAULT_SUBSTEP_COUNT) { value -> max(2, value).also { post { progress = progress } } }
    var substepDrawable: Drawable? by uiProperty(updateMeasurementsOnChange = true)
    var substepOffset: Int by uiProperty(DEFAULT_SUBSTEP_OFFSET_DP)
    var substepAnchorX: Float by interceptedUIProperty(DEFAULT_SUBSTEP_ANCHOR_X) { value -> if (value.isFinite()) min(1F, max(0F, value)) else DEFAULT_SUBSTEP_ANCHOR_X }
    var substepAnchorY: Float by interceptedUIProperty(DEFAULT_SUBSTEP_ANCHOR_Y, updateMeasurementsOnChange = true) { value -> if (value.isFinite()) min(1F, max(0F, value)) else DEFAULT_SUBSTEP_ANCHOR_Y }

    var progressMode: ProgressMode by reactiveUIProperty(DEFAULT_PROGRESS_MODE) { progress = progress }
    var maxProgress: Float by interceptedUIProperty(DEFAULT_MAX_PROGRESS) { value -> (if (value.isFinite()) max(0F, value) else DEFAULT_MAX_PROGRESS).also { progress = min(progress, it) } }
    var progress: Float  by interceptedUIProperty(DEFAULT_PROGRESS) { value ->
        val progressValue = min(if (value.isFinite()) value else DEFAULT_PROGRESS, maxProgress)
        when (progressMode) {
            ProgressMode.CONTINUOUS -> progressValue
            ProgressMode.STEP -> (maxProgress / (stepCount - (if (progressSweepAngle < 360F) 1 else 0))).let { progressStep -> progressStep * floor((progressValue + (progressStep * 0.5F)) / progressStep) }
            ProgressMode.SUBSTEP -> (maxProgress / (substepCount - (if (progressSweepAngle < 360F) 1 else 0))).let { progressStep -> progressStep * floor((progressValue + (progressStep * 0.5F)) / progressStep) }
        }.also { newProgress ->
            if (newProgress.compareTo(progress) != 0) {
                post {
                    progressListener?.onProgressChanged(this@Knob, progress)
                }
            }
        }
    }
    var progressStrokeColor: Int by uiProperty(DEFAULT_PROGRESS_STROKE_COLOR)
    var progressStrokeWidth: Int by interceptedUIProperty(0, updateMeasurementsOnChange = true) { value -> max(0, value) }
    var progressStrokeCap: Paint.Cap by uiProperty(DEFAULT_PROGRESS_STROKE_CAP)
    var progressStrokeOffset: Int by uiProperty(0, updateMeasurementsOnChange = true)
    var progressStartAngle: Float by interceptedUIProperty(DEFAULT_PROGRESS_START_ANGLE, updateMeasurementsOnChange = true) { value -> if (value.isFinite()) max(0F, value) % 360F else DEFAULT_PROGRESS_START_ANGLE }
    var progressSweepAngle: Float by interceptedUIProperty(DEFAULT_PROGRESS_SWEEP_ANGLE, updateMeasurementsOnChange = true) { value -> if (value.isFinite()) min(360F, max(0F, value)) else DEFAULT_PROGRESS_SWEEP_ANGLE }

    var thumbDrawable: Drawable?  by uiProperty(updateMeasurementsOnChange = true)
    var thumbOffset: Int  by uiProperty(0, updateMeasurementsOnChange = true)
    var thumbAnchorX: Float by interceptedUIProperty(DEFAULT_THUMB_ANCHOR_X) { value -> if (value.isFinite()) min(1F, max(0F, value)) else DEFAULT_THUMB_ANCHOR_Y }
    var thumbAnchorY: Float by interceptedUIProperty(DEFAULT_THUMB_ANCHOR_Y, updateMeasurementsOnChange = true) { value -> if (value.isFinite()) min(1F, max(0F, value)) else DEFAULT_THUMB_ANCHOR_Y }
    var thumbRotation: Boolean by uiProperty(DEFAULT_THUMB_ROTATION)

    var touchThreshold: Int by interceptedUIProperty(0, updateMeasurementsOnChange = true) { value -> max(0, value) }
    var touchOffset: Int by uiProperty(0, updateMeasurementsOnChange = true)

    private var debug: Boolean by uiProperty(false)
    private var debugDrawBounds: Boolean by uiProperty(false)
    private var debugDrawTrack: Boolean by uiProperty(false)
    private var debugDrawTouchRadius: Boolean by uiProperty(false)
    private var debugDrawTouchArea: Boolean by uiProperty(false)

    init {
        val resources = context.resources;
        val displayMetrics = resources.displayMetrics

        knobSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DEFAULT_KNOB_SIZE_DP.toFloat(), displayMetrics).toInt()

        trackStrokeWidth = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DEFAULT_TRACK_STROKE_WIDTH_DP.toFloat(), displayMetrics).toInt()
        trackStrokeOffset = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DEFAULT_TRACK_STROKE_OFFSET_DP.toFloat(), displayMetrics).toInt()

        progressStrokeWidth = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DEFAULT_PROGRESS_STROKE_WIDTH_DP.toFloat(), displayMetrics).toInt()
        progressStrokeOffset = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DEFAULT_PROGRESS_STROKE_OFFSET_DP.toFloat(), displayMetrics).toInt()

        thumbOffset = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DEFAULT_THUMB_OFFSET_DP.toFloat(), displayMetrics).toInt()

        stepOffset = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DEFAULT_STEP_OFFSET_DP.toFloat(), displayMetrics).toInt()

        touchThreshold = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DEFAULT_TOUCH_THRESHOLD_DP.toFloat(), displayMetrics).toInt()
        touchOffset = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DEFAULT_TOUCH_OFFSET_DP.toFloat(), displayMetrics).toInt()

        context.obtainStyledAttributes(attrs, R.styleable.Knob, 0, 0).apply {
            knobSize = getDimensionPixelSize(R.styleable.Knob_knobSize, knobSize)

            fillColor = getColor(R.styleable.Knob_fillColor, fillColor)
            fillStartAngle = getFloat(R.styleable.Knob_fillStartAngle, fillStartAngle)
            fillSweepAngle = getFloat(R.styleable.Knob_fillSweepAngle, fillSweepAngle)

            trackStrokeColor = getColor(R.styleable.Knob_trackStrokeColor, trackStrokeColor)
            trackStrokeWidth = getDimensionPixelSize(R.styleable.Knob_trackStrokeWidth, trackStrokeWidth)
            trackStrokeCap = Paint.Cap.values()[getInteger(R.styleable.Knob_trackStrokeCap, trackStrokeCap.ordinal)]
            trackStrokeOffset = getDimensionPixelSize(R.styleable.Knob_trackStrokeOffset, trackStrokeOffset)

            stepCount = getInteger(R.styleable.Knob_stepCount, stepCount)
            stepDrawable = getDrawable(R.styleable.Knob_stepDrawable)?.apply {
                setBounds(0, 0, intrinsicWidth, intrinsicHeight)
            }
            stepOffset = getDimensionPixelSize(R.styleable.Knob_stepOffset, stepOffset)
            stepAnchorX = getFraction(R.styleable.Knob_stepAnchorX, 1, 1, stepAnchorX)
            stepAnchorY = getFraction(R.styleable.Knob_stepAnchorY, 1, 1, stepAnchorY)

            substepCount = getInteger(R.styleable.Knob_substepCount, substepCount)
            substepDrawable = getDrawable(R.styleable.Knob_substepDrawable)?.apply {
                setBounds(0, 0, intrinsicWidth, intrinsicHeight)
            }
            substepOffset = getDimensionPixelSize(R.styleable.Knob_substepOffset, substepOffset)
            substepAnchorX = getFraction(R.styleable.Knob_substepAnchorX, 1, 1, substepAnchorX)
            substepAnchorY = getFraction(R.styleable.Knob_substepAnchorY, 1, 1, substepAnchorY)

            progressMode = ProgressMode.values()[getInteger(R.styleable.Knob_progressMode, DEFAULT_PROGRESS_MODE.ordinal)]
            maxProgress = getFloat(R.styleable.Knob_maxProgress, maxProgress)
            progress = getFloat(R.styleable.Knob_progress, progress)
            progressStrokeColor = getColor(R.styleable.Knob_progressStrokeColor, progressStrokeColor)
            progressStrokeWidth = getDimensionPixelSize(R.styleable.Knob_progressStrokeWidth, progressStrokeWidth)
            progressStrokeCap = Paint.Cap.values()[getInteger(R.styleable.Knob_progressStrokeCap, progressStrokeCap.ordinal)]
            progressStrokeOffset = getDimensionPixelSize(R.styleable.Knob_progressStrokeOffset, progressStrokeOffset)

            progressStartAngle = getFloat(R.styleable.Knob_progressStartAngle, progressStartAngle)
            progressSweepAngle = getFloat(R.styleable.Knob_progressSweepAngle, progressSweepAngle)

            thumbDrawable = getDrawable(R.styleable.Knob_thumbDrawable)?.apply {
                setBounds(0, 0, intrinsicWidth, intrinsicHeight)
            }
            thumbOffset = getDimensionPixelSize(R.styleable.Knob_thumbOffset, thumbOffset)
            thumbAnchorX = getFraction(R.styleable.Knob_thumbAnchorX, 1, 1, thumbAnchorX)
            thumbAnchorY = getFraction(R.styleable.Knob_thumbAnchorY, 1, 1, thumbAnchorY)
            thumbRotation = getBoolean(R.styleable.Knob_thumbRotation, thumbRotation)

            touchThreshold = getDimensionPixelSize(R.styleable.Knob_touchThreshold, touchThreshold)
            touchOffset = getDimensionPixelSize(R.styleable.Knob_touchOffset, touchOffset)

            debug = getBoolean(R.styleable.Knob_debug, debug)
            debugDrawBounds = getBoolean(R.styleable.Knob_debug_drawBounds, debugDrawBounds)
            debugDrawTrack = getBoolean(R.styleable.Knob_debug_drawTrack, debugDrawTrack)
            debugDrawTouchRadius = getBoolean(R.styleable.Knob_debug_drawTouchRadius, debugDrawTouchRadius)
            debugDrawTouchArea = getBoolean(R.styleable.Knob_debug_drawTouchArea, debugDrawTouchArea)
        }.recycle()

        initialized = true
    }

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val drawingOffset = PointF()
    private val drawingBounds = RectF()
    private val drawingBoundsCenter = PointF()

    private val trackPath = Path()
    private val trackPathMeasure = PathMeasure()
    private val pathPosition = FloatArray(2)
    private val pathTangent = FloatArray(2)
    private val transformMatrix = Matrix()
    private val arcRect = RectF()

    private val touchPath = Path()

    private var activeTouchPointerId = MotionEvent.INVALID_POINTER_ID
    private var activeTouchPoint = PointF(Float.NaN, Float.NaN)

    var progressListener: OnProgressListener? = null

    init {
        updateMeasurements()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var width = MeasureSpec.getSize(widthMeasureSpec)
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)

        var height = MeasureSpec.getSize(heightMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)

        if (widthMode != MeasureSpec.EXACTLY) {
            val preferredWidth = drawingBounds.width().toInt() +
                    paddingLeft +
                    paddingRight

            width = if (widthMode == MeasureSpec.AT_MOST) min(width, preferredWidth) else preferredWidth
        }

        if (heightMode != MeasureSpec.EXACTLY) {
            val preferredHeight = drawingBounds.height().toInt() +
                    paddingTop +
                    paddingBottom

            height = if (heightMode == MeasureSpec.AT_MOST) min(height, preferredHeight) else preferredHeight
        }

        setMeasuredDimension(width, height)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (!isEnabled) {
            return false
        }

        val action = event.actionMasked
        val pointerIndex = event.actionIndex

        if (action == MotionEvent.ACTION_DOWN || action == MotionEvent.ACTION_POINTER_DOWN) {
            if (activeTouchPointerId == MotionEvent.INVALID_POINTER_ID) {
                val x = event.getX(pointerIndex)
                val y = event.getY(pointerIndex)

                val empty = Path().run {
                    addRect(x, y, x + 1, y + 1, Path.Direction.CW)
                    op(touchPath, Path.Op.INTERSECT)
                    isEmpty
                }

                if (touchPath.isEmpty || !empty) {
                    activeTouchPointerId = event.getPointerId(pointerIndex)
                    activeTouchPoint.set(x, y)
                    onTouch()
                }
            }
        } else if (action == MotionEvent.ACTION_MOVE) {
            if (activeTouchPointerId == event.getPointerId(pointerIndex)) {
                activeTouchPoint.set(event.getX(pointerIndex), event.getY(pointerIndex))
                onTouch()
            }
        } else if (action == MotionEvent.ACTION_UP ||
            action == MotionEvent.ACTION_POINTER_UP ||
            action == MotionEvent.ACTION_OUTSIDE ||
            action == MotionEvent.ACTION_CANCEL) {
            if (activeTouchPointerId == event.getPointerId(pointerIndex)) {
                activeTouchPointerId = MotionEvent.INVALID_POINTER_ID
                activeTouchPoint.set(Float.NaN, Float.NaN)
                invalidate()
            }
        }

        return activeTouchPointerId != MotionEvent.INVALID_POINTER_ID
    }

    private fun onTouch() {
        val angle = (180  + Math.toDegrees(atan2(drawingBoundsCenter.y - activeTouchPoint.y, drawingBoundsCenter.x - activeTouchPoint.x).toDouble()).toFloat()) % 360
        val sweep = (angle + 360 - progressStartAngle) % 360

        if (sweep < progressSweepAngle) {
            progress = sweep / progressSweepAngle * maxProgress
        } else {
            progress = if (sweep - progressSweepAngle < (360 - progressSweepAngle) * 0.5F) maxProgress else 0F
        }

        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        canvas.save()

        canvas.clipRect(
            paddingLeft.toFloat(),
            paddingTop.toFloat(),
            (width - paddingRight).toFloat(),
            (height - paddingBottom).toFloat()
        )

        canvas.translate(paddingLeft.toFloat(), paddingTop.toFloat())

        drawFill(canvas)
        drawTrack(canvas)
        substepDrawable?.let { drawable -> drawSteps(canvas, drawable, substepCount, substepOffset, substepAnchorX, substepAnchorY) }
        stepDrawable?.let { drawable -> drawSteps(canvas, drawable, stepCount, stepOffset, stepAnchorX, stepAnchorY) }
        drawProgress(canvas)
        drawThumb(canvas)

        canvas.restore()

        if (debug) {
            drawDebug(canvas)
        }
    }

    private fun drawDebug(canvas: Canvas) {
        if (debugDrawBounds) {
            paint.style = Style.STROKE
            paint.color = 0xFF000000.toInt()
            paint.strokeWidth = 2F

            canvas.drawRect(drawingBounds, paint)
        }

        if (debugDrawTrack) {
            paint.style = Style.STROKE
            paint.color = 0xFFFF0000.toInt()
            paint.strokeWidth = 8F

            canvas.save()
            canvas.translate(paddingLeft.toFloat(), paddingTop.toFloat())
            canvas.drawPath(trackPath, paint)
            canvas.restore()
        }

        if (debugDrawTouchRadius) {
            if (!activeTouchPoint.x.isNaN() && !activeTouchPoint.y.isNaN()) {
                paint.style = Style.STROKE
                paint.color = Color.RED
                paint.strokeWidth = 4F

                canvas.drawLine(drawingBoundsCenter.x, drawingBoundsCenter.y, activeTouchPoint.x, activeTouchPoint.y, paint)
            }
        }

        if (debugDrawTouchArea) {
            paint.style = Style.FILL
            paint.color = 0x4000FF00.toInt()

            canvas.drawPath(touchPath, paint)
        }
    }

    private fun drawFill(canvas: Canvas) {
        if (fillColor == 0) {
            return
        }

        paint.style = Style.FILL
        paint.color = fillColor

        canvas.save()
        canvas.translate(drawingOffset.x, drawingOffset.y)
        canvas.drawArc(arcRect.apply { set(0F, 0F, knobSize.toFloat(), knobSize.toFloat()) }, fillStartAngle, fillSweepAngle, false, paint)
        canvas.restore()
    }

    private fun drawTrack(canvas: Canvas) {
        if (trackStrokeColor == 0) {
            return
        }

        paint.style = Style.STROKE
        paint.color = trackStrokeColor
        paint.strokeWidth = trackStrokeWidth.toFloat()
        paint.strokeCap = trackStrokeCap

        canvas.save()
        canvas.translate(drawingOffset.x - trackStrokeOffset, drawingOffset.y - trackStrokeOffset)
        canvas.drawArc(
            arcRect.apply { set(
                0F,
                0F,
                knobSize + trackStrokeOffset * 2F,
                knobSize + trackStrokeOffset * 2F
            ) },
            progressStartAngle,
            progressSweepAngle,
            false,
            paint
        )
        canvas.restore()
    }

    private fun drawProgress(canvas: Canvas) {
        if (progressStrokeColor == 0) {
            return
        }

        paint.style = Style.STROKE
        paint.color = progressStrokeColor
        paint.strokeWidth = progressStrokeWidth.toFloat()
        paint.strokeCap = progressStrokeCap

        canvas.save()
        canvas.translate(drawingOffset.x - progressStrokeOffset, drawingOffset.y - progressStrokeOffset)
        canvas.drawArc(
            arcRect.apply { set(
                0F,
                0F,
                knobSize + progressStrokeOffset * 2F,
                knobSize + progressStrokeOffset * 2F
            ) },
            progressStartAngle,
            progressSweepAngle * (progress / maxProgress),
            false,
            paint
        )
        canvas.restore()
    }

    private fun drawThumb(canvas: Canvas) {
        thumbDrawable?.let { drawable ->
            trackPathMeasure.getPosTan((progress / maxProgress) * trackPathMeasure.length, pathPosition, pathTangent)

            canvas.save()
            canvas.concat(transformMatrix.apply {
                reset()

                val rotation = Math.toDegrees(atan2(pathTangent[1], pathTangent[0]).toDouble()).toFloat()

                if (!thumbRotation) {
                    postRotate(
                        -rotation,
                        drawable.bounds.width() * 0.5F,
                        drawable.bounds.height() * 0.5F)
                }

                postTranslate(
                    pathPosition[0] - drawable.bounds.width() * thumbAnchorX,
                    pathPosition[1] - thumbOffset - drawable.bounds.height() * thumbAnchorY)
                postRotate(
                    rotation,
                    pathPosition[0],
                    pathPosition[1])
            })
            drawable.draw(canvas)
            canvas.restore()
        }
    }

    private fun drawSteps(canvas: Canvas, drawable: Drawable, count: Int, offset: Int, anchorX: Float, anchorY: Float) {
        if (count >= 2) {
            val stepProgress = maxProgress / (count - (if (progressSweepAngle < 360F) 1 else 0))
            repeat(count) { iteration ->
                trackPathMeasure.getPosTan((iteration * stepProgress / maxProgress) * trackPathMeasure.length, pathPosition, pathTangent)

                canvas.save()
                canvas.concat(transformMatrix.apply {
                    reset()
                    postTranslate(
                        pathPosition[0] - drawable.bounds.width() * anchorX,
                        pathPosition[1] - offset - drawable.bounds.height() * anchorY)
                    postRotate(
                        Math.toDegrees(atan2(pathTangent[1], pathTangent[0]).toDouble()).toFloat(),
                        pathPosition[0],
                        pathPosition[1])
                })
                drawable.draw(canvas)
                canvas.restore()
            }
        }
    }

    override fun setPadding(left: Int, top: Int, right: Int, bottom: Int) {
        super.setPadding(left, top, right, bottom)
        updateMeasurements()
    }

    override fun setPaddingRelative(left: Int, top: Int, right: Int, bottom: Int) {
        super.setPaddingRelative(left, top, right, bottom)
        updateMeasurements()
    }

    private fun updateMeasurements() {
        if (!initialized) {
            return
        }

        val maxOffset = maxOf(
            0F,
            trackStrokeWidth * 0.5F + trackStrokeOffset,
            progressStrokeWidth * 0.5F + progressStrokeOffset,
            thumbDrawable?.let { drawable -> drawable.bounds.height() * thumbAnchorY + thumbOffset } ?: 0F,
            stepDrawable?.let { drawable -> drawable.bounds.height() * stepAnchorY + stepOffset } ?: 0F,
            substepDrawable?.let { drawable -> drawable.bounds.height() * substepAnchorY + substepOffset } ?: 0F,
            touchThreshold * 0.5F + touchOffset,
        )

        drawingOffset.set(maxOffset, maxOffset)

        drawingBounds.set(0F, 0F, knobSize + drawingOffset.x * 2, knobSize + drawingOffset.y * 2)
        drawingBounds.offset(paddingLeft.toFloat(), paddingTop.toFloat())
        drawingBoundsCenter.set(drawingBounds.centerX(), drawingBounds.centerY())

        trackPath.reset()
        if (progressSweepAngle < 360) {
            trackPath.arcTo(arcRect.apply { set(0F, 0F, knobSize.toFloat(), knobSize.toFloat()) }, progressStartAngle, progressSweepAngle, false)
        } else {
            trackPath.arcTo(arcRect.apply { set(0F, 0F, knobSize.toFloat(), knobSize.toFloat()) }, progressStartAngle, 359.999F, false)
        }

        trackPath.offset(drawingOffset.x, drawingOffset.y)
        trackPathMeasure.setPath(trackPath, false)

        touchPath.reset()
        val _touchThreshold = touchThreshold.toFloat() * 0.5F
        if (_touchThreshold > 0) {
            touchPath.addArc(arcRect.apply { set(-_touchThreshold - touchOffset, -_touchThreshold - touchOffset, knobSize.toFloat() + _touchThreshold + touchOffset, knobSize.toFloat() + _touchThreshold + touchOffset) }, 0F, 360F)
            touchPath.op(
                Path().apply {
                    addArc(arcRect.apply { set(_touchThreshold - touchOffset, _touchThreshold - touchOffset, knobSize.toFloat() - _touchThreshold + touchOffset, knobSize.toFloat() - _touchThreshold + touchOffset) }, 0F, 360F )
                },
                Path.Op.DIFFERENCE
            )
            touchPath.offset(paddingLeft + drawingOffset.x, paddingTop + drawingOffset.y)
        }

        requestLayout()
    }

    private open class UIPropertyDelegate<T>(
        private var value: Any? = null,
        private val updateMeasurementsOnChange: Boolean = false
    ) : ReadWriteProperty<Knob, T> {

        override fun getValue(thisRef: Knob, property: KProperty<*>): T {
            @Suppress("UNCHECKED_CAST")
            return value as T
        }

        override fun setValue(thisRef: Knob, property: KProperty<*>, value: T) {
            this.value = value

            if (updateMeasurementsOnChange) thisRef.updateMeasurements()

            thisRef.invalidate()
        }

    }

    private fun <T> uiProperty(value: Any? = null, updateMeasurementsOnChange: Boolean = false): UIPropertyDelegate<T> {
        return UIPropertyDelegate(value, updateMeasurementsOnChange)
    }

    private inline fun <T> interceptedUIProperty(value: T? = null, updateMeasurementsOnChange: Boolean = false, crossinline intercept: (T) -> T): UIPropertyDelegate<T> {
        return object : UIPropertyDelegate<T>(value, updateMeasurementsOnChange) {
            override fun setValue(thisRef: Knob, property: KProperty<*>, value: T) {
                super.setValue(thisRef, property, intercept(value))
            }
        }
    }

    private inline fun <T> reactiveUIProperty(value: T? = null, updateMeasurementsOnChange: Boolean = false, crossinline reaction: () -> Unit): UIPropertyDelegate<T> {
        return object : UIPropertyDelegate<T>(value, updateMeasurementsOnChange) {
            override fun setValue(thisRef: Knob, property: KProperty<*>, value: T) {
                super.setValue(thisRef, property, value)
                reaction()
            }
        }
    }

    interface OnProgressListener {

        fun onProgressChanged(knob: Knob, progress: Float)

    }

}