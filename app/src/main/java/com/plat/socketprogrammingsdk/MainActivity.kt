package com.plat.socketprogrammingsdk

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.plat.socket.startServer
import com.plat.socketprogrammingsdk.databinding.ActivityMainBinding
import kotlinx.coroutines.launch

class MainActivity: AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        lifecycleScope.launch {
            startServer()
        }
    }
}