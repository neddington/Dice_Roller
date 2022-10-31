package com.zybooks.gesturedemo

import android.os.Bundle
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GestureDetectorCompat
import com.zybooks.diceroller.R

const val TAG = "GestureDemo"

class MainActivity : AppCompatActivity() {

    private lateinit var gestureDetector: GestureDetectorCompat

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        gestureDetector = GestureDetectorCompat(this,
            object: GestureDetector.OnGestureListener {

                override fun onDown(e: MotionEvent): Boolean {
                    Log.d(TAG, "onDown")
                    return true
                }

                override fun onShowPress(e: MotionEvent) {
                    Log.d(TAG, "onShowPress")
                }

                override fun onSingleTapUp(e: MotionEvent): Boolean {
                    Log.d(TAG, "onSingleTapUp")
                    return false
                }

                override fun onScroll(e1: MotionEvent, e2: MotionEvent, distanceX: Float,
                                      distanceY: Float): Boolean {
                    Log.d(TAG, "onScroll")
                    return false
                }

                override fun onLongPress(e: MotionEvent) {
                    Log.d(TAG, "onLongPress")
                }

                override fun onFling(e1: MotionEvent, e2: MotionEvent, velocityX: Float,
                                     velocityY: Float): Boolean {
                    Log.d(TAG, "onFling")
                    return true
                }
            })
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event != null) {
            gestureDetector.onTouchEvent(event)
        }
        return super.onTouchEvent(event)
    }
}