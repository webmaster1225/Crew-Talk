package dev.nguyen.crewtalk

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import dev.nguyen.crewtalk.databinding.ActivityLoginBinding
import dev.nguyen.crewtalk.databinding.ActivityRegisterBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar: Toolbar = binding.toolbarLogin
        setSupportActionBar(toolbar)
        // the "!!" will throw NullPointerException if the value is null
        supportActionBar!!.title = "Login" // this will help prevent showing default app's name in the action bar

        supportActionBar!!.setDisplayHomeAsUpEnabled(true) // --> back button

        // Click the back button
        toolbar.setNavigationOnClickListener {
            val intent = Intent(this, WelcomeActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}