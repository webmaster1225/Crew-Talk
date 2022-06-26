package dev.nguyen.crewtalk.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import com.google.firebase.auth.FirebaseAuth
import dev.nguyen.crewtalk.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var mAuth: FirebaseAuth


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

        // get auth instance
        mAuth = FirebaseAuth.getInstance()

        // hit login btn
        binding.loginBtn.setOnClickListener {
            loginUser()
        }
    }

    private fun loginUser() {
        val email: String = binding.emailLogin.text.toString()
        val password: String = binding.passwordLogin.text.toString()

         if (email == ("")) {
            Toast.makeText(this, "Email can not be empty", Toast.LENGTH_SHORT).show()
        } else if (password == ("")) {
            Toast.makeText(this, "Password can not be empty", Toast.LENGTH_SHORT).show()
        } else {
            mAuth
                .signInWithEmailAndPassword(email, password)
                .addOnCompleteListener{task ->
                    if (task.isSuccessful) {
                        // success
                        // send user to main activity
                        val intent = Intent(this, MainActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(intent)
                        finish()
                    } else {
                        // fail
                        // the double-bang (!!) is the not-null assertion operator => There is at least an exception
                        Toast.makeText(this, "Error Message: "+task.exception!!.message.toString(), Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }
}