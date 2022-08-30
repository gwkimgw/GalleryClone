package com.example.endterm

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import androidx.core.net.toUri
import com.example.endterm.databinding.ActivityEnlargeBinding

class EnlargeActivity : AppCompatActivity() {
    lateinit var binding: ActivityEnlargeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityEnlargeBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        var receiveData1 = intent.getStringExtra("image_uri")

        if (receiveData1 != "") {
            binding.imageView1.setImageURI(receiveData1?.toUri())
        }
    }

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        super.onSaveInstanceState(outState, outPersistentState)
        outState.putString("image_uri", intent.getStringExtra("image_uri"))
    }
}