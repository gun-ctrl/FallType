package com.example.falltype

import android.animation.ValueAnimator
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.renderscript.Sampler
import com.example.falltype.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var mbinding:ActivityMainBinding

    private var mDownloadAnim:ValueAnimator?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mbinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mbinding.root)


        //用代码来改变对象属性的值
//        mbinding.progress.lineWidth = 20f
//        mbinding.progress.bgCircleColor = resources.getColor(R.color.main_purple,null)

        mbinding.mProgress.resultStatus = MyProgress.SUCCESS
        downLoadData()
        //监听控键的点击事件
        mbinding.mProgress.mCallBack = { state->
            if (state == MyProgress.ON){
                //下载
               if (mDownloadAnim!!.isPaused){
                   mDownloadAnim?.resume()
               }else{
                   mDownloadAnim?.start()
               }
            }else{
                //暂停
                mDownloadAnim?.pause()
            }
        }
    }
    //下载数据
    private fun downLoadData(){
        //属性动画模拟下载数据
        mDownloadAnim =  ValueAnimator.ofFloat(0f,1.0f).apply {
            duration = 2000
            addUpdateListener {
                //将当前的进度传递给自定义View
                mbinding.mProgress.progress = it.animatedValue as Float
            }
        }
    }
}