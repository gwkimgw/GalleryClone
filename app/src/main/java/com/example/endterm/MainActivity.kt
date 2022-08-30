package com.example.endterm

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import com.example.endterm.Model.ContentDTO
import com.example.endterm.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*
import androidx.recyclerview.widget.GridLayoutManager
import androidx.viewpager2.widget.ViewPager2
import me.relex.circleindicator.CircleIndicator3

class MainActivity : AppCompatActivity() {
    var storage: FirebaseStorage? = null
    var photoUri : Uri? = null
    var auth: FirebaseAuth? = null
    var firestore : FirebaseFirestore? = null
    lateinit var binding: ActivityMainBinding
    lateinit var indicator: CircleIndicator3

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ActivityCompat.requestPermissions(this
            , arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 1)

        storage = FirebaseStorage.getInstance()
        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        var userList = arrayListOf<ImageElement>()

        val ImageAdapter = MyAdapter(this, userList)
        binding.recyclerView.adapter = ImageAdapter

        val requestGalleryLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()) {
            try {
                val inputStream = contentResolver.openInputStream(it.data!!.data!!)
                photoUri = it.data?.data
                inputStream?.close()
                if(photoUri != null) {
                    ImageAdapter.addItem(ImageElement(photoUri.toString()))
                    binding.imagePreview.setImageURI(photoUri)
                    contentUpload()
                    Toast.makeText(this, "Photo added and Uploaded", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception){
                e.printStackTrace()
            }
        }
        binding.uploadphotoBtn.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            requestGalleryLauncher.launch(intent)
        }
    }
    fun contentUpload() {
        var timestamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        var imageFileName = "Image_" + timestamp + "_png"

        var storageRef = storage?.reference?.child("images")?.child(imageFileName)

        storageRef?.putFile(photoUri!!)?.addOnSuccessListener {
            storageRef.downloadUrl.addOnSuccessListener { uri ->
                var contentDTO = ContentDTO()
                contentDTO.imageUrl = uri.toString()
                contentDTO.uid = auth?.currentUser?.uid
                contentDTO.userId = auth?.currentUser?.email
                contentDTO.timestamp = System.currentTimeMillis()
                firestore?.collection("images")?.document()?.set(contentDTO)
                setResult(Activity.RESULT_OK)
            }
        }
    }
}