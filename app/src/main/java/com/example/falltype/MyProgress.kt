package com.example.falltype

import android.animation.AnimatorSet
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

/**
 *@Description
 *@Author PC
 *@QQ 1578684787
 */
class MyProgress:View {

    private var mWidth = 0f
    private var mHeight = 0f
    //矩形的圆角
    private var cornerRadius = 0f
    //矩形的画笔
    private var mRectPaint = Paint().apply {
        style = Paint.Style.FILL
        color = Color.MAGENTA
    }
    //勾勾或叉叉的Paint
    private val resultPaint = Paint().apply {
        color = Color.WHITE
        style = Paint.Style.STROKE
        strokeWidth =5f
    }
    private var cx = 0f
    private var cy = 0f
    private var markWidth = 0f
    //path
    private val resultPath = Path()
    //遮罩的矩形区域
    private var coverRect = Rect()
    //进程变化因子
    var progress = 40f
    set(value) {
        //记录外部传递过来的值
        field = value
        //判断下载是否完毕
        if (value == 1.0f){
            //下载完毕
            startFinishAnim()
        }
        //重新绘制
        invalidate()
    }
    //遮罩层的动画因子
    private var clipWidth = 0f
    //定义状态
    companion object{
        const val ON = 0
        const val OFF = 1
        const val SUCCESS = 2
        const val FAILURE = 3
    }
    //记录当前的状态
    private var mState = ON
    //记录下载的结果
    var resultStatus = SUCCESS

    //回调事件
    var mCallBack:((Int)->Unit) ?= null

    //向中间靠拢的变化因子
    private var transx = 0f
    //代码创建这个View时被调用
    constructor(context: Context):super(context){}
    //xml中添加
    constructor(context: Context, attrs: AttributeSet?):super(context,attrs){
    }
    constructor(context: Context, attrs: AttributeSet?, style:Int):super(context,attrs,style){
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mWidth = width.toFloat()
        mHeight = height.toFloat()

        //绘制勾勾或者叉叉
        markWidth = mHeight/3
         cx = mWidth/2
         cy = mHeight/2
        with(resultPath) {
            if (resultStatus == SUCCESS) {
                //勾勾
                //起点
                moveTo(cx - markWidth / 2, cy)
                //拐点
                lineTo(cx - markWidth / 8, cy + markWidth / 2)
                //终点
                lineTo(cx + markWidth / 2, cy - markWidth / 4)
            } else {
                //叉叉
                //第一条
                moveTo(cx-markWidth/2,cy-markWidth/2)
                lineTo(cx+markWidth/2,cy+markWidth/2)
                //第二条
                moveTo(cx-markWidth/2,cy+markWidth/2)
                lineTo(cx+markWidth/2,cy-markWidth/2)
            }
        }
    }

    override fun onDraw(canvas: Canvas?) {
        //绘制矩形区域
        mRectPaint.color = Color.MAGENTA
        canvas?.drawRoundRect(transx,0f,mWidth-transx,mHeight,cornerRadius,cornerRadius,mRectPaint)
        //绘制进度矩形区域
        mRectPaint.color = Color.CYAN
        canvas?.drawRoundRect(transx,0f,progress*mWidth-transx,mHeight,cornerRadius,cornerRadius,mRectPaint)

        //判断是否靠拢了 在中心形成圆
        if (transx == mWidth/2-mHeight/2){
            //绘制勾勾或叉叉
            canvas?.drawPath(resultPath,resultPaint)

            //绘制遮罩层
            //确定遮罩层的矩形区域
            coverRect.set(
                    ((cx-markWidth/2).toInt()+clipWidth).toInt(),
                    (cy-markWidth/2).toInt(),
                    (cx+markWidth/2).toInt()+8,
                    (cy+markWidth/2).toInt()+8
            )
            canvas?.drawRect(coverRect,mRectPaint)
        }

    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event?.action == MotionEvent.ACTION_DOWN){
            //将当前的点击事件传递给外部
            mCallBack?.let{
                //将当前状态传递过去
                it(mState)
                //更改状态值
                mState = if (mState == ON) OFF else ON
            }
        }
        return true
    }
    private fun startFinishAnim(){
        //两端变成半圆形
        val changeToHalfCircle = ValueAnimator.ofFloat(0f,mHeight/2).apply {
            duration = 1000
            addUpdateListener {
                cornerRadius = it.animatedValue as Float
                invalidate()
            }
        }
        val sideToCenterAnim = ValueAnimator.ofFloat(0f,mWidth/2 - mHeight/2).apply {
            duration = 1000
            addUpdateListener {
                    transx = it.animatedValue as Float
                    invalidate()
            }
        }
        val clipAnim = ValueAnimator.ofFloat(0f,markWidth).apply {
            duration = 1000
            addUpdateListener {
                    clipWidth = it.animatedValue as Float
                    invalidate()
            }
        }
        //设置动画先后顺序
        AnimatorSet().apply {
            playSequentially(changeToHalfCircle,sideToCenterAnim,clipAnim)
            start()
        }
    }
}