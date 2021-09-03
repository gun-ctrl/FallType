package com.example.falltype

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import java.time.format.DecimalStyle

/**
 *@Description
 *@Author PC
 *@QQ 1578684787
 */
class ProgressVIew:View {
    private var cx = 0f
    private var cy = 0f
    private var radius = 0f
    var lineWidth = 5f
    set(value) {
        field = value
        bgPaint.strokeWidth = value
        fgPaint.strokeWidth = value
    }
    var bgCircleColor = Color.GREEN
    set(value) {
        field = value
        bgPaint.color = value
    }
    var fgCircleColor  = Color.LTGRAY
    set(value) {
        field = value
        fgPaint.color = value
    }
    private val bgPaint = Paint().apply {
        style = Paint.Style.STROKE
        strokeWidth = lineWidth
        color = bgCircleColor
    }
    private val fgPaint = Paint().apply {
        style = Paint.Style.STROKE
        strokeWidth = lineWidth
        color = fgCircleColor
    }
    private val textPaint = Paint().apply {
        color = Color.BLACK
        textSize = 100f
        textAlign = Paint.Align.CENTER
    }
    //进度变化因子
    var progress = 0f
        set(value) {
       field = value
        invalidate()
        }
    //代码创建这个View时被调用
    constructor(context: Context):super(context){}
    //xml中添加
    constructor(context: Context,attrs:AttributeSet?):super(context,attrs){
        parseAttrs(context,attrs)
    }
    constructor(context: Context,attrs: AttributeSet?,style:Int):super(context,attrs,style){
        parseAttrs(context,attrs)
    }
//解析自定义属性的值
    @SuppressLint("Recycle")
    private fun parseAttrs(context: Context, attrs:AttributeSet?){
        /**使用obtainStyledAttributes从attrs中解析所有的属性
         * resId：R.styleable.ProgressVIew,attrs 指定从哪个样式中解析
         * attrs:解析的来源
         * 返回值：TypedArray
         * 必须使用recycle()回收
         */
        context.obtainStyledAttributes(attrs,R.styleable.ProgressVIew).apply {
            //获取线的宽度 defValue为默认值
            lineWidth = this.getFloat(R.styleable.ProgressVIew_lineWidth,5f)
            //获取背景颜色
            bgCircleColor = this.getColor(R.styleable.ProgressVIew_backgroundCircleColor,Color.BLUE)
            fgCircleColor = this.getColor(R.styleable.ProgressVIew_foregroundCircleColor,Color.BLUE)
            this.recycle()
        }
    }
    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        cx = measuredWidth/2f
        cy = measuredHeight/2f
        radius = if (measuredWidth<=measuredHeight){
            measuredWidth/2f - lineWidth
        }else{
            measuredHeight/2f -lineWidth
        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        //绘制背景圆
        canvas?.drawCircle(cx,cy,radius,bgPaint)
        //绘制进度条
        canvas?.drawArc(cx-radius,cy-radius,cx+radius,cy+radius,270f,progress*360,false,fgPaint)

        val text = "${(progress*100).toInt()}%"
        val metrics = textPaint.fontMetrics
        val space = (metrics.descent-metrics.ascent)/2 - metrics.descent
        canvas?.drawText(text,cx,cy+space,textPaint)

    }
}