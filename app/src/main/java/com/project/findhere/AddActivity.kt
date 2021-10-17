package com.project.findhere

import android.app.Activity
import android.app.DatePickerDialog
import android.content.ContentValues.TAG
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.project.findhere.models.FindPost
import com.project.findhere.models.FoundPost
import com.project.findhere.models.User
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import java.util.jar.Manifest

class AddActivity : AppCompatActivity() {

    lateinit var imageUri: Uri
    lateinit var outputImage: File
    private var signedInUser: User? = null
    private lateinit var signedInUserId: String
    private lateinit var tvDatePicker: TextView
    private lateinit var btnDatePicker: Button
    private lateinit var storageReference: StorageReference
    private lateinit var fireStoreDb: FirebaseFirestore
    private lateinit var firebaseAuth: FirebaseAuth
    private val takePhoto = 1
    private val fromAlbum = 2
    private val CAMERA_PERMISSION_REQ = 1000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)
        storageReference = FirebaseStorage.getInstance().reference
        firebaseAuth = FirebaseAuth.getInstance()

        val selectPosition = intent.getSerializableExtra("TabSelected") as Int

        Log.d("SelectedPosision", "${selectPosition}")

        //get user&id

        fireStoreDb = FirebaseFirestore.getInstance()
        fireStoreDb.collection("users")
            .document(firebaseAuth.currentUser?.uid as String)
            .get()
            .addOnSuccessListener { userSnapshot ->
                signedInUser = userSnapshot.toObject(User::class.java)
                Log.i(TAG, "signed in user: $signedInUser")

            }
            .addOnFailureListener { exception ->
                Log.i(TAG, "Failure fetching signed in user", exception)
            }
        signedInUserId = firebaseAuth.currentUser?.uid as String
        //

        //add backButton

        val button: MaterialButton = findViewById(R.id.add_backButton)
        button.setOnClickListener() {
            onBackPressed()
        }//

        //addPhotoPicker
        val takePhotoBtn: Button = findViewById(R.id.add_button_takePhoto)
        takePhotoBtn.setOnClickListener {
            if (checkSelfPermission(android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                handleCameraBtnClick()
            }
            else{
                requestPermissions(arrayOf(android.Manifest.permission.CAMERA),CAMERA_PERMISSION_REQ)
            }
        }

        val fromAlbumBtn: Button = findViewById(R.id.add_button_fromAlbum)
        fromAlbumBtn.setOnClickListener {
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
            intent.addCategory(Intent.CATEGORY_OPENABLE)
            intent.type = "image/*"
            startActivityForResult(intent, fromAlbum)
        }//

        //add DatePicker

        tvDatePicker = findViewById(R.id.add_tvDate)
        btnDatePicker = findViewById(R.id.add_btnDatePicker)

        val myCalendar = Calendar.getInstance()

        val datePicker = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            myCalendar.set(Calendar.YEAR, year)
            myCalendar.set(Calendar.MONTH, month)
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            updateLable(myCalendar)
        }

        btnDatePicker.setOnClickListener {
            DatePickerDialog(
                this,
                datePicker,
                myCalendar.get(Calendar.YEAR),
                myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }//

        //add submit button
        val btnSubmit: MaterialButton = findViewById(R.id.add_submit)
        btnSubmit.setOnClickListener {
            if (selectPosition == 0) {
                handleFoundBtnClick()
            } else {
                handleFindBtnClick()
            }
        }//

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when(requestCode){
            CAMERA_PERMISSION_REQ -> {
                if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    handleCameraBtnClick()
                }
                else{
                    Log.d("AddActivity","CAMERA DENIED")
                    Toast.makeText(baseContext,"未允许使用相机",Toast.LENGTH_SHORT).show()
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private fun handleCameraBtnClick() {
        outputImage = File(externalCacheDir, "output_image.jpg")
        if (outputImage.exists()) {
            outputImage.delete()
        }
        outputImage.createNewFile()
        imageUri = FileProvider.getUriForFile(this, "com.project.findhere", outputImage)

        val intent = Intent("android.media.action.IMAGE_CAPTURE")
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
        startActivityForResult(intent, takePhoto)
    }

    private fun handleFoundBtnClick() {

        val imageView: ImageView = findViewById(R.id.add_imageView)
        val tvDate: TextView = findViewById(R.id.add_tvDate)
        if (tvDate.text.isBlank()) {
            Toast.makeText(this, "日期不能为空", Toast.LENGTH_SHORT).show()
            return
        }

        val tvPlace: TextInputEditText = findViewById(R.id.add_PlaceET)
        if (tvPlace.text?.isBlank() == true) {
            Toast.makeText(this, "地点不能为空", Toast.LENGTH_SHORT).show()
            return
        }

        val tvContent: EditText = findViewById(R.id.add_ContentET)
        if (tvContent.text.isBlank()) {
            Toast.makeText(this, "描述不能为空", Toast.LENGTH_SHORT).show()
            return
        }

        val tvName: TextInputEditText = findViewById(R.id.add_NameET)
        if (tvName.text?.isBlank() == true) {
            Toast.makeText(this, "物品名称不能为空", Toast.LENGTH_SHORT).show()
            return
        }

        val btnSubmit: MaterialButton = findViewById(R.id.add_submit)
        btnSubmit.isEnabled = false
        val photoReference =
            storageReference.child("images/${System.currentTimeMillis()}-photo.jpg")
        // upload photo to firebase
        photoReference.putFile(imageUri)
            .continueWithTask { photoUploadTask ->
                Log.i(TAG, "uploaded bytes: ${photoUploadTask.result?.bytesTransferred}")
                //retrieve image url of the uploaded image
                photoReference.downloadUrl
            }.continueWithTask { downloadUrlTask ->
                // create a post object with the image URL and add that to the posts collection
                val post = FoundPost(
                    signedInUser,
                    signedInUserId,
                    System.currentTimeMillis(),
                    tvDate.text.toString(),
                    tvName.text.toString(),
                    downloadUrlTask.result.toString(),
                    tvPlace.text.toString(),
                    tvContent.text.toString()
                )
                fireStoreDb.collection("foundposts").add(post)
            }.addOnCompleteListener { postCreationTask ->
                btnSubmit.isEnabled = true
                if (!postCreationTask.isSuccessful) {
                    Log.e(TAG, "Exception during Firebase operations", postCreationTask.exception)
                    Toast.makeText(this, "上传失败", Toast.LENGTH_SHORT).show()
                }
                tvContent.text.clear()
                tvPlace.text?.clear()
                imageView.setImageResource(0)
                Toast.makeText(this, "上传成功", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
    }

    private fun handleFindBtnClick() {

        val imageView: ImageView = findViewById(R.id.add_imageView)
        val tvDate: TextView = findViewById(R.id.add_tvDate)
        if (tvDate.text.isBlank()) {
            Toast.makeText(this, "日期不能为空", Toast.LENGTH_SHORT).show()
            return
        }

        val tvPlace: TextInputEditText = findViewById(R.id.add_PlaceET)
        if (tvPlace.text?.isBlank() == true) {
            Toast.makeText(this, "地点不能为空", Toast.LENGTH_SHORT).show()
            return
        }

        val tvContent: EditText = findViewById(R.id.add_ContentET)
        if (tvContent.text.isBlank()) {
            Toast.makeText(this, "描述不能为空", Toast.LENGTH_SHORT).show()
            return
        }

        val tvName: TextInputEditText = findViewById(R.id.add_NameET)
        if (tvName.text?.isBlank() == true) {
            Toast.makeText(this, "物品名称不能为空", Toast.LENGTH_SHORT).show()
            return
        }

        val btnSubmit: MaterialButton = findViewById(R.id.add_submit)
        btnSubmit.isEnabled = false
        val photoReference =
            storageReference.child("images/${System.currentTimeMillis()}-photo.jpg")
        // upload photo to firebase
        photoReference.putFile(imageUri)
            .continueWithTask { photoUploadTask ->
                Log.i(TAG, "uploaded bytes: ${photoUploadTask.result?.bytesTransferred}")
                //retrieve image url of the uploaded image
                photoReference.downloadUrl
            }.continueWithTask { downloadUrlTask ->
                // create a post object with the image URL and add that to the posts collection
                val post = FindPost(
                    signedInUser,
                    signedInUserId,
                    System.currentTimeMillis(),
                    tvDate.text.toString(),
                    tvName.text.toString(),
                    downloadUrlTask.result.toString(),
                    tvPlace.text.toString(),
                    tvContent.text.toString()
                )
                fireStoreDb.collection("findposts").add(post)
            }.addOnCompleteListener { postCreationTask ->
                btnSubmit.isEnabled = true
                if (!postCreationTask.isSuccessful) {
                    Log.e(TAG, "Exception during Firebase operations", postCreationTask.exception)
                    Toast.makeText(this, "上传失败", Toast.LENGTH_SHORT).show()
                }
                tvContent.text.clear()
                tvPlace.text?.clear()
                imageView.setImageResource(0)
                Toast.makeText(this, "上传成功", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            takePhoto -> {
                if (resultCode == Activity.RESULT_OK) {
                    val bitmap =
                        BitmapFactory.decodeStream(contentResolver.openInputStream(imageUri))
                    val imageView: ImageView = findViewById(R.id.add_imageView)
                    imageView.setImageBitmap(rotateIfRequired(bitmap))
                }
            }

            fromAlbum -> {
                if (resultCode == Activity.RESULT_OK && data != null) {
                    data.data?.let { uri ->
                        val bitmap = getBitmapFromUri(uri)
                        val imageView: ImageView = findViewById(R.id.add_imageView)
                        imageView.setImageBitmap(bitmap)
                    }
                }
            }
        }
    }

    private fun rotateIfRequired(bitmap: Bitmap): Bitmap {
        val exif = ExifInterface(outputImage.path)
        val orientation =
            exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)
        return when (orientation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> rotateBitmap(bitmap, 90)
            ExifInterface.ORIENTATION_ROTATE_180 -> rotateBitmap(bitmap, 180)
            ExifInterface.ORIENTATION_ROTATE_270 -> rotateBitmap(bitmap, 270)
            else -> bitmap
        }
    }

    private fun rotateBitmap(bitmap: Bitmap, degree: Int): Bitmap {
        val matrix = Matrix()
        matrix.postRotate(degree.toFloat())
        val rotatedBitmap =
            Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
        bitmap.recycle()
        return rotatedBitmap
    }

    private fun getBitmapFromUri(uri: Uri) = contentResolver.openFileDescriptor(uri, "r")?.use {
        BitmapFactory.decodeFileDescriptor(it.fileDescriptor)
    }

    private fun updateLable(myCalendar: Calendar) {
        val myFormat = "yyyy-MM-dd"
        val sdf = SimpleDateFormat(myFormat, Locale.CHINA)
        tvDatePicker.setText(sdf.format(myCalendar.time))
    }
}