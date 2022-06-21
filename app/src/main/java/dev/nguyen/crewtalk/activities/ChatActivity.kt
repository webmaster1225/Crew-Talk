package dev.nguyen.crewtalk.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import dev.nguyen.crewtalk.databinding.ActivityChatBinding

class ChatActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChatBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)


    }
}