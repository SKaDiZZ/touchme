package com.virtuooza.samir.touchme

import android.graphics.drawable.BitmapDrawable
import android.view.MotionEvent
import android.view.View
import android.view.animation.LinearInterpolator
import android.widget.FrameLayout
import android.widget.ImageView

class VirtuoozaTouch: View.OnTouchListener {

    // Modes
    private val NONE = 0
    private val DRAG = 1
    private val ZOOM = 2

    // Starting mode
    private var mode = NONE

    // FrameLayout params
    private var params: FrameLayout.LayoutParams? = null

    // Start width and height
    private var startWidth: Int = 0
    private var startHeight: Int = 0

    // Track distance when zooming
    private var oldDist = 1f

    // Distance difference when zooming
    private var scalediff: Float = 0.toFloat()

    // position X axis (horizontal)
    private var x = 0f
    // position Y axis (vertical)
    private var y = 0f

    // motionEvent direction X axis
    private var dx = 0f
    // motionEvent direction Y axis
    private var dy = 0f

    // motionEvent rotation
    private var d = 0f
    private var newRot = 0f
    private var angle = 0f

    override fun onTouch(view: View, motionEvent: MotionEvent): Boolean {

        // View is ImageView
        val v = view as ImageView

        // Enable antialiasing on images
        (v.drawable as BitmapDrawable).setAntiAlias(true)

        when (motionEvent.action and MotionEvent.ACTION_MASK) {

            MotionEvent.ACTION_DOWN -> {
                params = v.layoutParams as FrameLayout.LayoutParams
                startWidth = params!!.width
                startHeight = params!!.height
                dx = motionEvent.rawX - params!!.leftMargin
                dy = motionEvent.rawY - params!!.topMargin

                // set mode to DRAG
                mode = DRAG
            }

            MotionEvent.ACTION_POINTER_DOWN -> {
                oldDist = spacing(motionEvent)
                // set mode to ZOOM
                if (oldDist > 10f) mode = ZOOM
                d = rotation(motionEvent)
            }

            MotionEvent.ACTION_UP -> {}
            MotionEvent.ACTION_POINTER_UP -> mode = NONE
            MotionEvent.ACTION_MOVE -> if (mode == DRAG) {

                x = motionEvent.rawX
                y = motionEvent.rawY

                params?.leftMargin = (x - dx).toInt()
                params?.topMargin = (y - dy).toInt()

                params?.rightMargin = 0
                params?.bottomMargin = 0
                params?.rightMargin = params!!.leftMargin + 1 * params!!.width
                params?.bottomMargin = params!!.topMargin + 1 * params!!.height

                v.layoutParams = params


            } else if (mode == ZOOM) {
                if (motionEvent.pointerCount == 2) {
                    newRot = rotation(motionEvent)
                    val r = newRot - d
                    angle = r

                    x = motionEvent.rawX
                    y = motionEvent.rawY

                    val newDist = spacing(motionEvent)
                    if (newDist > 10f) {
                        val scale = newDist / oldDist * v.scaleX
                        if (scale > 0.6) {
                            scalediff = scale
                            v.scaleX = scale
                            v.scaleY = scale
                        }
                    }
                    v.animate().rotationBy(angle).setDuration(0).setInterpolator(LinearInterpolator()).start()
                    x = motionEvent.rawX
                    y = motionEvent.rawY
                    params?.leftMargin = (x - dx + scalediff).toInt()
                    params?.topMargin = (y - dy + scalediff).toInt()
                    params?.rightMargin = 0
                    params?.bottomMargin = 0
                    params?.rightMargin = params!!.leftMargin + 5 * params!!.width
                    params?.bottomMargin = params!!.topMargin + 10 * params!!.height
                    v.layoutParams = params
                }
            }
        }
        return true
    }

    // Get distance between two fingers and calculate midpoint to scale and rotate from
    private fun spacing(event: MotionEvent): Float {
        val x = event.getX(0) - event.getX(1)
        val y = event.getY(0) - event.getY(1)
        return Math.sqrt((x * x + y * y).toDouble()).toFloat()
    }

    private fun rotation(event: MotionEvent): Float {
        val deltaX = (event.getX(0) - event.getX(1)).toDouble()
        val deltaY = (event.getY(0) - event.getY(1)).toDouble()
        val radians = Math.atan2(deltaY, deltaX)
        return Math.toDegrees(radians).toFloat()
    }
}
