package com.example.falltype

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

/**
 *@Description
 *@Author PC
 *@QQ 1578684787
 */
class PulseLoadingView:View {
    //圆的半径
    private var radius = 0f
    //第一个圆的坐标
    private var cx = 0f
    private var cy = 0f
    //画笔
    private val mPaint = Paint().apply {
        color = context.resources.getColor(R.color.main_purple,null)
        style = Paint.Style.FILL
    }
    //动画对象
    private  var mAnimators = mutableListOf<ValueAnimator>()
    //保存延时的时间
    private var delays = arrayOf(100L,200L,300L)
    //保存每个圆缩放的比例
    private val scales = arrayOf(0.2f,0.2f,0.2f)
    constructor(context: Context):super(context){}
    constructor(context: Context,attrs:AttributeSet?):super(context,attrs){}
    constructor(context: Context,attrs: AttributeSet?,style:Int):super(context,attrs,style){}

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        //两种情况设置圆的半径
        radius = if ((measuredHeight/2)*7 <= measuredWidth){
            measuredHeight/2f
        }else{
            measuredWidth/7f

        }
        cy = measuredHeight/2f
        cx = measuredWidth/2f -2.5f*radius
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        for (i in 0..2){
            canvas?.save()
            canvas?.translate(cx+i*2.5f*radius,cy)
            canvas?.scale(scales[i],scales[i])
            canvas?.drawCircle(0f,0f,radius,mPaint)
            canvas?.restore()
        }

//一般的画圆方式
//        canvas?.drawCircle(cx,cy,radius,mPaint)
//        canvas?.drawCircle(cx+2.5f*radius,cy,radius,mPaint)
//        canvas?.drawCircle(cx+5f*radius,cy,radius,mPaint)

    }
    private fun createAnimator(){
        for (i in 0..2) {
            ValueAnimator.ofFloat(0.2f, 1f).apply {
                duration = 600
                startDelay = delays[i] // 设置每个圆对应的延迟时间
                repeatCount = ValueAnimator.INFINITE
                addUpdateListener {
                    scales[i] = it.animatedValue as Float
                    invalidate()
                }
                mAnimators.add(this)
            }
        }
    }
    private fun start(){
        for (item in mAnimators){
            item.start()
        }
    }
    private fun stop(){
        for (item in mAnimators){
            item.end()
        }
    }
   //启动动画
    fun show(){
        createAnimator()
       start()
    }

    //隐藏动画
    fun hide(){
        stop()
    }

}