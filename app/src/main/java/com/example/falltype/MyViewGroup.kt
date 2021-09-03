package com.example.falltype

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import kotlin.math.max


/**
 *@Description
 *@Author PC
 *@QQ 1578684787
 */
class MyViewGroup:ViewGroup{
    //间距
    private val space = 30
    //容器中的所有控件
    private var totalLineViews = mutableListOf<MutableList<View>>()
    private var allLineHeight = mutableListOf<Int>()
    constructor(context: Context,attrs:AttributeSet?):super(context,attrs){

    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        //测量自己 -> 提供的子控件进行测量自己的（限制条件）
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        //获取父容器的限制尺寸
        val parentWidthSize = MeasureSpec.getSize(widthMeasureSpec)
        val parentHeightSize = MeasureSpec.getSize(heightMeasureSpec)


        //记录当前这一行的高度和宽度
        var currentLineWidth = space
        var currentLineHeight = 0

        //当前最大宽度/高度（最终父容器的尺寸）
        var resultWidth = 0
        var resultHeight = space
        //记录当前这一行的所有子视图
        var lineViews = mutableListOf<View>()
        for(i in 0 until childCount){
            //确定子控件
            val child = getChildAt(i)

            //获取子控件的布局参数（xml中设置的layout-width layout-height）
            val lp = child.layoutParams
            //确定子控件的measureSpec
            val widthSpec = getChildMeasureSpec(widthMeasureSpec,2*space,lp.width)
            val heightSpec = getChildMeasureSpec(heightMeasureSpec,2*space,lp.height)
            child.measure(widthSpec,heightSpec)
            //判断这个控件是当前行还是下一行
            if (currentLineWidth + child.measuredWidth + space <= parentWidthSize){
                //添加到当前行
                lineViews.add(child)
                //改变当前行的宽度
                currentLineWidth += child.measuredWidth + space
                //改变当前行的宽度
                currentLineHeight = max(currentLineHeight,child.measuredHeight)
            }else{
                //先保存上一行的数据
               totalLineViews.add(lineViews)
                //确定最大宽度
                resultWidth = max(resultWidth,currentLineWidth)
                //确定最大高度
                resultHeight += currentLineHeight + space
                //保存上一行的高度
                allLineHeight.add(currentLineHeight)
                //重置
                lineViews = mutableListOf()
                //将该控件添加到新的一行
                lineViews.add(child)
                currentLineHeight = child.measuredHeight
                currentLineWidth = space + child.measuredWidth

            }
            //判断是否还有最后一行
            if (lineViews.size > 0 ){
                //先保存上一行的数据
                totalLineViews.add(lineViews)
                //确定最大宽度
                resultWidth = max(resultWidth,currentLineWidth)
                //确定最大高度
                resultHeight += currentLineHeight + space
                allLineHeight.add(currentLineHeight)
            }

        }

        //设置父容器的尺寸
        setMeasuredDimension(resultWidth,resultHeight)
    }

    override fun onLayout(p0: Boolean, p1: Int, p2: Int, p3: Int, p4: Int) {
        var left = space
        var top = space
        var right = 0
        var bottom = 0
        val row = totalLineViews.size
        for (i  in 0 until row){
            //遍历每一行
            val count = totalLineViews[i].size
            for (j in 0 until count){
                //取出每一行的控件
                val child = totalLineViews[i][j]
                right = left+child.measuredWidth
                bottom = top+child.measuredHeight
                child.layout(left,top,right,bottom)
                left += child.measuredWidth+space
            }
            //确定下一行的top
            top += allLineHeight[i] + space
            //下一行的left从头开始
            left = space
        }
    }

}