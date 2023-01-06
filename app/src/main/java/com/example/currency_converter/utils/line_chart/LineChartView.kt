package com.example.currency_converter.utils.line_chart

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.example.currency_converter.utils.Metrics.convertDpToPixel
import com.example.currency_converter.utils.*
import com.example.currency_converter.R

typealias SelectedMarkerChangedListener = (marker: Marker, field: LineChartField) -> Unit

class LineChartView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0,
) : View(
    context,
    attrs,
    defStyleAttr,
    defStyleRes,
) {

    private val listener: FieldChangedListener = {
        invalidate()
    }

    var actionListener: SelectedMarkerChangedListener? = null

    var lineChartField: LineChartField? = null
        set(value) {
            field = value
            field?.let { chartField ->
                chartField.listener = listener
                calculateParameters(chartField, guidelineTextPaint)
                setPrimaryColors(chartField)
                invalidate()
            }
        }

    private var motionEvent: MotionEvent? = null

    private val chartLinePath: Path = Path()
    private val markerLinePath: Path = Path()
    private val gradientPath: Path = Path()

    private val textRect: Rect = Rect()

    private val chartLinePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeWidth = convertDpToPixel(2f, context)
    }

    private val markerPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
    }

    private val guidelinePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeWidth = convertDpToPixel(1f, context)
    }

    private val guidelineTextPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        textSize = convertDpToPixel(12f, context)
    }

    private val infoTextPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        textSize = convertDpToPixel(12f, context)
    }

    private val gradientPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
    }

    private val markerLinePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeWidth = convertDpToPixel(1f, context)
        pathEffect = DashPathEffect(floatArrayOf(LINE_SPACING, LINE_SPACING), 0f)
    }

    private val rectBorderPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeWidth = BORDER_WIDTH
        color = context.getColor(R.color.grey)
    }

    private val rectFillPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        color = context.getColor(R.color.white)
    }

    private var startBackgroundColor: Int = context.getColor(R.color.grey_alpha_50)
        set(value) {
            field = value
            invalidate()
        }

    private val endBackgroundColor: Int = context.getColor(android.R.color.transparent)

    private var chartLineColor: Int = context.getColor(R.color.grey)
        set(value) {
            field = value
            chartLinePaint.color = value
            invalidate()
        }

    private var markerColor: Int = context.getColor(R.color.grey)
        set(value) {
            field = value
            markerPaint.color = value
            invalidate()
        }

    private var guidelineColor: Int = context.getColor(R.color.white_smoke)
        set(value) {
            field = value
            guidelinePaint.color = value
            invalidate()
        }

    private var markerLineColor: Int = context.getColor(R.color.grey)
        set(value) {
            field = value
            markerLinePaint.color = value
            invalidate()
        }

    private var infoTextColor: Int = context.getColor(R.color.black)
        set(value) {
            field = value
            infoTextPaint.color = value
            invalidate()
        }

    private var guidelineTextColor: Int = context.getColor(R.color.grey)
        set(value) {
            field = value
            guidelineTextPaint.color = value
            invalidate()
        }

    init {
        context.theme.obtainStyledAttributes(R.styleable.LineChartView).apply {
            try {
                guidelineColor = getColor(
                    R.styleable.LineChartView_guidelineColor,
                    context.getColor(R.color.white_smoke),
                )
                markerLineColor = getColor(
                    R.styleable.LineChartView_markerLineColor,
                    context.getColor(R.color.grey),
                )
                infoTextColor = getColor(
                    R.styleable.LineChartView_infoTextColor,
                    context.getColor(R.color.black),
                )
                guidelineTextColor = getColor(
                    R.styleable.LineChartView_guidelineTextColor,
                    context.getColor(R.color.grey),
                )
            } finally {
                recycle()
            }
        }
    }

    private fun setPrimaryColors(chartField: LineChartField) {
        when (chartField.developmentType) {
            DevelopmentType.REGRESS -> {
                startBackgroundColor = context.getColor(R.color.neon_red_alpha_50)
                chartLineColor = context.getColor(R.color.neon_red)
                markerColor = context.getColor(R.color.neon_red)
            }
            DevelopmentType.PROGRESS -> {
                startBackgroundColor = context.getColor(R.color.green_alpha_50)
                chartLineColor = context.getColor(R.color.green)
                markerColor = context.getColor(R.color.green)
            }
            DevelopmentType.STAGNATION -> {
                startBackgroundColor = context.getColor(R.color.grey_alpha_50)
                chartLineColor = context.getColor(R.color.grey)
                markerColor = context.getColor(R.color.grey)
            }
        }
    }

    private fun getXPositionByMarkerIndex(index: Int, xUnit: Float, offset: Float): Float {
        return PADDING_HORIZONTAL + offset + xUnit * index
    }

    private fun getYPositionByMarkerValue(
        chartField: LineChartField,
        value: Double,
        yUnit: Float,
    ): Float {
        return (height - yUnit * (value - chartField.roundedLow) - PADDING_VERTICAL).toFloat()
    }

    private fun getTextWidth(text: String, paint: Paint): Float {
        paint.getTextBounds(text, 0, text.length, textRect)
        return textRect.width().toFloat()
    }

    private fun getTextHeight(text: String, paint: Paint): Float {
        paint.getTextBounds(text, 0, text.length, textRect)
        return textRect.height().toFloat()
    }

    private fun calculateParameters(chartField: LineChartField, textPaint: Paint) {
        chartField.apply {
            val maxAxisValue = roundedHigh.toFormattedString() + SPACE_SYMBOL
            val guidelineValueLength = getTextWidth(maxAxisValue, textPaint) + RECT_PADDING

            val xUnit = (width - guidelineValueLength - PADDING_HORIZONTAL * 2) / (rates.size - 1)
            val yUnit = (height - PADDING_VERTICAL * 2).div(roundedDifference).toFloat()

            val markers = rates.mapIndexed { index, marker ->
                Marker(
                    marker.value,
                    marker.date,
                    Position(
                        getXPositionByMarkerIndex(index, xUnit, guidelineValueLength),
                        getYPositionByMarkerValue(this, marker.value, yUnit),
                    ),
                )
            }

            val firstMarkerPositionX = markers.first().position.x
            val lastMarkerPositionX = markers.last().position.x

            val topMarkerPositionY = getYPositionByMarkerValue(this, roundedHigh, yUnit)
            val bottomMarkerPositionY = getYPositionByMarkerValue(this, roundedLow, yUnit)

            chartParameters = ChartParameters(
                markers,
                xUnit,
                yUnit,
                firstMarkerPositionX,
                lastMarkerPositionX,
                topMarkerPositionY,
                bottomMarkerPositionY,
            )
        }
    }

    private fun drawGradient(
        chartField: LineChartField,
        canvas: Canvas,
        paint: Paint,
        path: Path,
    ) {
        chartField.chartParameters?.let { parameters ->
            path.reset()
            path.moveTo(parameters.firstMarkerPositionX, parameters.bottomMarkerPositionY)

            for (marker in parameters.markers) {
                path.lineTo(marker.position.x, marker.position.y)
            }

            path.lineTo(parameters.lastMarkerPositionX, parameters.bottomMarkerPositionY)
            path.lineTo(parameters.firstMarkerPositionX, parameters.bottomMarkerPositionY)

            paint.shader = LinearGradient(
                parameters.firstMarkerPositionX,
                parameters.topMarkerPositionY,
                parameters.firstMarkerPositionX,
                parameters.bottomMarkerPositionY,
                startBackgroundColor,
                endBackgroundColor,
                Shader.TileMode.CLAMP,
            )

            canvas.drawPath(path, paint)
        }
    }

    private fun drawGuidelines(
        chartField: LineChartField,
        canvas: Canvas,
        linePaint: Paint,
        textPaint: Paint,
    ) {
        val parameters = chartField.chartParameters ?: return

        val step = chartField.roundedDifference / (NUMBER_OF_GUIDELINES - 1)
        val guidelines: MutableList<Double> = mutableListOf<Double>().apply {
            addAll(
                listOf(
                    chartField.roundedLow,
                    chartField.roundedLow + step,
                    chartField.roundedHigh - step,
                    chartField.roundedHigh,
                )
            )
        }

        for (guideline in guidelines) {
            val formattedRate =  guideline.toFormattedString()

            val textWidth = getTextWidth(formattedRate, textPaint)
            val textHeight = getTextHeight(formattedRate, textPaint)

            val startX = PADDING_HORIZONTAL
            val startY = getYPositionByMarkerValue(chartField, guideline, parameters.yUnit)

            canvas.drawLine(
                startX + textWidth,
                startY,
                parameters.lastMarkerPositionX,
                startY,
                linePaint,
            )
            canvas.drawText(formattedRate, startX, startY + textHeight / 3f, textPaint)
        }
    }

    private fun drawChartLine(
        chartField: LineChartField,
        canvas: Canvas,
        linePaint: Paint,
        path: Path,
    ) {
        val parameters = chartField.chartParameters ?: return

        path.reset()

        parameters.markers.forEachIndexed { index, marker ->
            if (index == 0) {
                path.moveTo(marker.position.x, marker.position.y)
            } else {
                path.lineTo(marker.position.x, marker.position.y)
            }
        }

        canvas.drawPath(path, linePaint)
    }

    private fun drawMarkerLine(
        chartField: LineChartField,
        marker: Marker,
        canvas: Canvas,
        linePaint: Paint,
        linePath: Path,
    ) {
        chartField.chartParameters?.let { parameters ->
            linePath.reset()
            linePath.moveTo(marker.position.x, parameters.topMarkerPositionY)
            linePath.lineTo(marker.position.x, parameters.bottomMarkerPositionY)

            canvas.drawPath(linePath, linePaint)
        }
    }

    private fun drawMarkerInfo(
        chartField: LineChartField,
        marker: Marker,
        canvas: Canvas,
        markerPaint: Paint,
        rectBorderPaint: Paint,
        rectFillPaint: Paint,
        rectInfoTextPaint: Paint,
    ) {
        chartField.chartParameters?.let { parameters ->
            val rate = marker.value.toFormattedString()
            val date = marker.date.toSpecialDateFormat(chartField.dateFormatPattern)
            val text = "$rate  $date"

            val rateWidth = getTextWidth(text, rectInfoTextPaint)
            val rateHeight = getTextHeight(text, rectInfoTextPaint)
            val halfRateWidth = rateWidth / 2

            var rectLeft = parameters.firstMarkerPositionX
            var rectRight = parameters.lastMarkerPositionX

            var rectTop = getYPositionByMarkerValue(chartField, chartField.roundedHigh, parameters.yUnit) + RECT_PADDING
            var rectBottom = rectTop + rateHeight + TEXT_PADDING * 2

            if (marker.position.y < rectBottom) {
                rectBottom = getYPositionByMarkerValue(chartField, chartField.roundedLow, parameters.yUnit) - RECT_PADDING
                rectTop = rectBottom - rateHeight - TEXT_PADDING * 2
            }

            if (marker.position.x >= width / 2) {
                if (marker.position.x + halfRateWidth + TEXT_PADDING < parameters.lastMarkerPositionX) {
                    rectRight = marker.position.x + halfRateWidth + TEXT_PADDING
                }
                rectLeft = rectRight - rateWidth - TEXT_PADDING * 2
            } else {
                if (marker.position.x - halfRateWidth - TEXT_PADDING > parameters.firstMarkerPositionX) {
                    rectLeft = marker.position.x - halfRateWidth - TEXT_PADDING
                }
                rectRight = rectLeft + rateWidth + TEXT_PADDING * 2
            }

            rectLeft -= RECT_PADDING
            rectRight += RECT_PADDING

            canvas.drawCircle(marker.position.x, marker.position.y, MARKER_RADIUS, markerPaint)

            val rect = RectF(rectLeft, rectTop, rectRight, rectBottom)

            canvas.drawRoundRect(rect, CORNER_RADIUS, CORNER_RADIUS, rectFillPaint)
            canvas.drawRoundRect(rect, CORNER_RADIUS, CORNER_RADIUS, rectBorderPaint)

            drawTextCentered(canvas, text, rect.centerX(), rect.centerY(), rectInfoTextPaint)
        }
    }

    private fun drawTextCentered(
        canvas: Canvas,
        text: String,
        centerX: Float,
        centerY: Float,
        paint: Paint,
    ) {
        val x = centerX - paint.measureText(text) / 2
        val y = centerY - (paint.descent() + paint.ascent()) / 2

        canvas.drawText(text, x, y, paint)
    }

    private fun updateSelectedMarker(field: LineChartField, x: Float) {
        field.chartParameters?.let { parameters ->
            for (i in 1 until parameters.markers.size) {
                val previousMarker = parameters.markers[i - 1]
                val currentMarker = parameters.markers[i]

                if (x >= previousMarker.position.x && x <= currentMarker.position.x) {
                    val marker = getNearestMarker(previousMarker, currentMarker, x)

                    if (field.selectedMarker != marker) {
                        actionListener?.invoke(marker, field)
                    }
                }
            }
        }
    }

    private fun getNearestMarker(firstMarker: Marker, secondMarker: Marker, x: Float): Marker {
        val distanceBetweenFirstMarkerX = x - firstMarker.position.x
        val distanceBetweenSecondMarkerX = secondMarker.position.x - x

        return if (distanceBetweenFirstMarkerX > distanceBetweenSecondMarkerX) {
            secondMarker
        } else {
            firstMarker
        }
    }

    private fun checkRangeX(chartParameters: ChartParameters, x: Float): Boolean {
        return x > chartParameters.firstMarkerPositionX && x < chartParameters.lastMarkerPositionX
    }

    private fun checkRangeY(chartParameters: ChartParameters, y: Float): Boolean {
        return y > chartParameters.topMarkerPositionY && y < chartParameters.bottomMarkerPositionY
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val paddingHorizontal = paddingStart + paddingEnd
        val paddingVertical = paddingTop + paddingBottom

        val minWidth = suggestedMinimumWidth + paddingHorizontal
        val minHeight = suggestedMinimumHeight + paddingVertical

        val desiredHeight = minHeight.coerceAtMost(height + paddingVertical)
        val desiredWidth = minWidth.coerceAtMost(width + paddingHorizontal)

        setMeasuredDimension(
            resolveSize(desiredWidth, widthMeasureSpec),
            resolveSize(desiredHeight, heightMeasureSpec),
        )
    }

    override fun onSizeChanged(width: Int, height: Int, oldWidth: Int, oldHeight: Int) {
        super.onSizeChanged(width, height, oldWidth, oldHeight)
        lineChartField?.let { calculateParameters(it, guidelineTextPaint) }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val chartField: LineChartField = lineChartField ?: return false

        chartField.chartParameters?.let { parameters ->
            val x = event.x
            val y = event.y

            motionEvent = event

            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    isPressed = checkRangeY(parameters, y)
                    return true
                }
                MotionEvent.ACTION_MOVE -> {
                    if (checkRangeX(parameters, x) && isPressed) {
                        updateSelectedMarker(chartField, x)
                    }
                }
                MotionEvent.ACTION_UP -> {
                    return performClick()
                }
            }
        }
        return false
    }

    override fun performClick(): Boolean {
        if (super.performClick()) {
            return true
        }

        val chartField: LineChartField = lineChartField ?: return false
        val chartParameters: ChartParameters = chartField.chartParameters ?: return false

        val x = motionEvent?.x ?: 0f

        return if (checkRangeX(chartParameters, x) && isPressed) {
            updateSelectedMarker(chartField, x)
            true
        } else {
            false
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        lineChartField?.let { chartField ->
            drawGuidelines(chartField, canvas, guidelinePaint, guidelineTextPaint)
            drawGradient(chartField, canvas, gradientPaint, gradientPath)
            drawChartLine(chartField, canvas, chartLinePaint, chartLinePath)

            chartField.selectedMarker?.let { marker ->
                drawMarkerLine(chartField, marker, canvas, markerLinePaint, markerLinePath)
                drawMarkerInfo(
                    chartField,
                    marker,
                    canvas,
                    markerPaint,
                    rectBorderPaint,
                    rectFillPaint,
                    infoTextPaint,
                )
            }
        }
    }

    private companion object {
        private const val SPACE_SYMBOL = "_"
        private const val NUMBER_OF_GUIDELINES = 4

        private val MARKER_RADIUS = convertDpToPixel(4f, null)

        private val LINE_SPACING = convertDpToPixel(2f, null)

        private val CORNER_RADIUS = convertDpToPixel(4f, null)
        private val BORDER_WIDTH = convertDpToPixel(0.5f, null)

        private val TEXT_PADDING = convertDpToPixel(8f, null)
        private val RECT_PADDING = convertDpToPixel(2f, null)

        private val PADDING_VERTICAL = convertDpToPixel(8f, null)
        private val PADDING_HORIZONTAL = convertDpToPixel(16f, null)
    }
}