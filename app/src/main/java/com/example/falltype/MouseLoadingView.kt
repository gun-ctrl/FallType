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
class MouseLoadingView:View {
    //小圆的半径
    private var ballRadius = 0f
    //嘴的半径
    private val mouseRadius get() = ballRadius*3
    //嘴的圆心坐标
    private  var cx = 0f
    private  var cy = 0f
    //张嘴的角度 动画因子
    private var mouseAngle = 0f
    //小球移动的动画因子
    private var ballTranslateX = 0f
    //动画对象
    private var mAnimators = mutableListOf<ValueAnimator>()
    //画笔
    private val mPaint = Paint().apply {
        style = Paint.Style.FILL
        color = context.resources.getColor(R.color.main_purple,null)
    }
    constructor(context: Context):super(context){}
    constructor(context: Context, attrs: AttributeSet?):super(context,attrs){}
    constructor(context: Context, attrs: AttributeSet?, style:Int):super(context,attrs,style){}

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        if (measuredWidth/8.5 <= measuredHeight/6){
            ballRadius =measuredWidth/8.5f
            cx = 3*ballRadius
            cy = (measuredHeight-6f*ballRadius)/2 + 3*ballRadius
        }else{
            ballRadius =measuredHeight/6f
            cx = (measuredWidth - 8.5f*ballRadius)/2 + 3*ballRadius
            cy = 3*ballRadius
        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        //绘制大嘴
        canvas?.drawArc(cx-mouseRadius,cy-mouseRadius,cx+mouseRadius,cy+mouseRadius,
                mouseAngle,360-2*mouseAngle,true,mPaint)
        //绘制小圆
        canvas?.drawCircle(cx+ballTranslateX,cy,ballRadius,mPaint)
    }

    private fun createAnim(){
        //大嘴动画
        ValueAnimator.ofFloat(0f,45f,0f).apply {
            duration = 650
            repeatCount = ValueAnimator.INFINITE
            addUpdateListener {
                mouseAngle = it.animatedValue as Float
                //刷新界面
                invalidate()
            }
            mAnimators.add(this)
        }
        //小球动画
        ValueAnimator.ofFloat(4.5f*ballRadius,0f).apply {
            duration = 650
            repeatCount = ValueAnimator.INFINITE
            addUpdateListener {
                ballTranslateX = it.animatedValue as Float
                //刷新界面
                invalidate()
            }
            mAnimators.add(this)
        }
    }
    //启动动画
    private fun start(){
        for (anim in mAnimators){
            anim.start()
        }
    }
    //动画暂停
    private fun stop(){
        for (anim in mAnimators){
            anim.end()
        }
    }
    //提供给外部使用
    //显示动画
    fun show(){
        createAnim()
        start()
    }
    //隐藏动画 暂停
    fun hide(){
        stop()
    }

}