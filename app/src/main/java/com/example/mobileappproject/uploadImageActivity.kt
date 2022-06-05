package com.example.mobileappproject

import android.app.Activity
import android.content.Intent
import android.graphics.ImageDecoder
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.IOException
import java.util.*

class uploadImageActivity : AppCompatActivity() {

    private val PICK_IMAGE_REQUEST = 71
    private var filePath: Uri? = null
    private var firebaseStore: FirebaseStorage? = null
    private var storageReference: StorageReference? = null
    private lateinit var imagePreview: ImageView
    private lateinit var btn_choose_image: Button
    private lateinit var btn_upload_image: Button

    private lateinit var pickImageActivityLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upload_image)

        btn_choose_image = findViewById(R.id.btn_choose_image)
        btn_upload_image = findViewById(R.id.btn_upload_image)
        imagePreview = findViewById(R.id.image_preview)

        firebaseStore = FirebaseStorage.getInstance()
        storageReference = FirebaseStorage.getInstance().reference

        setupPickImageLauncher()
        btn_choose_image.setOnClickListener { launchGallery() }
        btn_upload_image.setOnClickListener { uploadImage() }
    }

    private fun launchGallery() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        pickImageActivityLauncher.launch(intent)
    }

    private fun setupPickImageLauncher() {
        pickImageActivityLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK && it.data !=null) {
                if (it.data != null) {
                    filePath = it.data!!.data
                    try {
                        val source = ImageDecoder.createSource(contentResolver, filePath as Uri)
                        val bitmap = ImageDecoder.decodeBitmap(source)
                        imagePreview.setImageBitmap(bitmap)
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
            }
        }
    }

    private fun uploadImage(){
        if(filePath != null){
            val ref = storageReference?.child("myImages/" + UUID.randomUUID().toString())
            val uploadTask = ref?.putFile(filePath!!)
            Toast.makeText(this,"Upload Successfully", Toast.LENGTH_SHORT).show()
        }else{
            Toast.makeText(this, "Please Upload an Image", Toast.LENGTH_SHORT).show()
        }
    }
}