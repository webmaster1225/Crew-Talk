package dev.nguyen.crewtalk

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import dev.nguyen.crewtalk.databinding.ActivityMainBinding
import dev.nguyen.crewtalk.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar: Toolbar = binding.toolbarRegister
        setSupportActionBar(toolbar)
        // the "!!" will throw NullPointerException if the value is null
        supportActionBar!!.title = "Register" // this will help prevent showing default app's name in the action bar
        supportActionBar!!.setDisplayHomeAsUpEnabled(true) // BACK BTN
        toolbar.setNavigationOnClickListener {
            val intent = Intent(this@RegisterActivity, WelcomeActivity::class.java)
            startActivity(intent)
            finish()
        }

    }
}