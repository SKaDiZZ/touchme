package com.virtuooza.samir.touchme

import android.Manifest
import android.app.Activity
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.widget.FrameLayout
import kotlinx.android.synthetic.main.activity_main.*
import android.content.Intent
import android.view.View
import android.graphics.BitmapFactory
import android.provider.MediaStore

class MainActivity : AppCompatActivity() {

    private val MY_PERMISSIONS_WRITE_EXTERNAL_STORAGE = 1
    private val RESULT_LOAD_IMAGE = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Check for permissions and request if needed
        ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), MY_PERMISSIONS_WRITE_EXTERNAL_STORAGE)

        // Initiate layout params
        init()

    }

    // Initiate layout params for props to correctly place them in FrameLayout at application start
    // SetOnTouchListener for your props
    fun init() {

        val mustacheParams = FrameLayout.LayoutParams(250, 250)
        mustacheParams.width = 250
        mustacheParams.height = 250
        mustacheParams.leftMargin = 20
        mustacheParams.topMargin = 50
        mustacheParams.bottomMargin = -250
        mustacheParams.rightMargin = -250
        mustache.layoutParams = mustacheParams
        mustache.setOnTouchListener(VirtuoozaTouch())

        val santashatParams = FrameLayout.LayoutParams(250, 250)
        santashatParams.width = 250
        santashatParams.height = 250
        santashatParams.leftMargin = 300
        santashatParams.topMargin = 50
        santashatParams.bottomMargin = -250
        santashatParams.rightMargin = -250
        santaHat.layoutParams = santashatParams
        santaHat.setOnTouchListener(VirtuoozaTouch())

        val sunglassesParams = FrameLayout.LayoutParams(250, 250)
        sunglassesParams.width = 250
        sunglassesParams.height = 250
        sunglassesParams.leftMargin = 600
        sunglassesParams.topMargin = 50
        sunglassesParams.bottomMargin = -250
        sunglassesParams.rightMargin = -250
        sunglasses.layoutParams = sunglassesParams
        sunglasses.setOnTouchListener(VirtuoozaTouch())

    }

    // Choose image from gallery
    fun openImage(view: View) {
        val intent = Intent(
                Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                intent.type = "image/*"
                startActivityForResult(intent, RESULT_LOAD_IMAGE)
     }

    // Get image from gallery and place it in imagePreview ImageView
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == Activity.RESULT_OK && null != data) {
            val selectedImage = data.data
            val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)

            val cursor = contentResolver.query(selectedImage!!,
                    filePathColumn, null, null, null)
            cursor!!.moveToFirst()

            val columnIndex = cursor.getColumnIndex(filePathColumn[0])
            val picturePath = cursor.getString(columnIndex)
            cursor.close()

            imagePreview.setImageBitmap(BitmapFactory.decodeFile(picturePath))
        }
    }
}
