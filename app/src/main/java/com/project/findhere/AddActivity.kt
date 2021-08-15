package com.project.findhere

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.bumptech.glide.Glide
import com.google.android.material.button.MaterialButton
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class AddActivity : AppCompatActivity() {

    val takePhoto = 1
    val fromAlbum = 2
    lateinit var imageUri: Uri
    lateinit var  outputImage: File
    private lateinit var tvDatePicker : TextView
    private lateinit var  btnDatePicker : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)
        
        //add backButton

        val button: MaterialButton = findViewById(R.id.add_backButton)
        button.setOnClickListener() {
            onBackPressed()
        }//
        
        //addPhotoPicker

        val takePhotoBtn: Button = findViewById(R.id.add_button_takePhoto)
        takePhotoBtn.setOnClickListener{
            outputImage = File(externalCacheDir,"output_image.jpg")
            if (outputImage.exists()){
                outputImage.delete()
            }
            outputImage.createNewFile()
            imageUri = FileProvider.getUriForFile(this,"com.project.findhere",outputImage)

            val intent = Intent("android.media.action.IMAGE_CAPTURE")
            intent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri)
            startActivityForResult(intent,takePhoto)
        }

        val fromAlbumBtn : Button = findViewById(R.id.add_button_fromAlbum)
        fromAlbumBtn.setOnClickListener{
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
            intent.addCategory(Intent.CATEGORY_OPENABLE)
            intent.type="image/*"
            startActivityForResult(intent, fromAlbum)
        }//
        
        //add DatePicker

        tvDatePicker = findViewById(R.id.add_tvDate)
        btnDatePicker = findViewById(R.id.add_btnDatePicker)

        val myCalendar = Calendar.getInstance()
        
        val datePicker = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            myCalendar.set(Calendar.YEAR,year)
            myCalendar.set(Calendar.MONTH,month)
            myCalendar.set(Calendar.DAY_OF_MONTH,dayOfMonth)
            updateLable(myCalendar)
        }

        btnDatePicker.setOnClickListener{
            DatePickerDialog(this,datePicker,myCalendar.get(Calendar.YEAR),myCalendar.get(Calendar.MONTH),myCalendar.get(Calendar.DAY_OF_MONTH)).show()
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            takePhoto -> {
                if (resultCode == Activity.RESULT_OK) {
                    val bitmap = BitmapFactory.decodeStream(contentResolver.openInputStream(imageUri))
                    val imageView : ImageView = findViewById(R.id.add_imageView)
                    imageView.setImageBitmap(rotateIfRequired(bitmap))
                }
            }

            fromAlbum -> {
                if(resultCode == Activity.RESULT_OK && data != null){
                    data.data?.let {uri ->
                        val bitmap = getBitmapFromUri(uri)
                        val imageView : ImageView = findViewById(R.id.add_imageView)
                        imageView.setImageBitmap(bitmap)
                    }
                }
            }
        }
    }

    private fun rotateIfRequired(bitmap: Bitmap):Bitmap{
        val exif = ExifInterface(outputImage.path)
        val orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION,ExifInterface.ORIENTATION_NORMAL)
        return when (orientation){
            ExifInterface.ORIENTATION_ROTATE_90 -> rotateBitmap(bitmap,90)
            ExifInterface.ORIENTATION_ROTATE_180 -> rotateBitmap(bitmap,180)
            ExifInterface.ORIENTATION_ROTATE_270 -> rotateBitmap(bitmap,270)
            else -> bitmap
        }
    }

    private  fun rotateBitmap(bitmap: Bitmap,degree: Int): Bitmap{
        val matrix = Matrix()
        matrix.postRotate(degree.toFloat())
        val rotatedBitmap = Bitmap.createBitmap(bitmap,0,0,bitmap.width,bitmap.height,matrix,true)
        bitmap.recycle()
        return rotatedBitmap
    }

    private fun getBitmapFromUri(uri: Uri) = contentResolver.openFileDescriptor(uri,"r")?.use {
        BitmapFactory.decodeFileDescriptor(it.fileDescriptor)
    }

    private fun updateLable(myCalendar: Calendar) {
        val myFormat = "yyyy-MM-dd"
        val sdf = SimpleDateFormat(myFormat,Locale.CHINA)
        tvDatePicker.setText(sdf.format(myCalendar.time))
    }
}