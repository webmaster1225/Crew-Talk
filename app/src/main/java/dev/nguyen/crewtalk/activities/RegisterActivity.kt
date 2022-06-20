package dev.nguyen.crewtalk.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import dev.nguyen.crewtalk.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var mAuth: FirebaseAuth
    private lateinit var refUser: DatabaseReference

    // userID is unique awmong the firebase users
    private var fireBaseUserId: String = "" // this is retrieved from the authentication process

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
            val intent = Intent(this, WelcomeActivity::class.java)
            startActivity(intent)
            finish()
        }

        mAuth = FirebaseAuth.getInstance()

        binding.registerBtn.setOnClickListener {
            registerUser()
        }
    }

    private fun registerUser() {
        val username: String = binding.usernameRegister.text.toString()
        val email: String = binding.emailRegister.text.toString()
        val password: String = binding.passwordRegister.text.toString()

        if (username.equals("")) {
            Toast.makeText(this, "Username can not be empty", Toast.LENGTH_SHORT).show()
        } else if (email.equals("")) {
            Toast.makeText(this, "Email can not be empty", Toast.LENGTH_SHORT).show()
        } else if (password.equals("")) {
            Toast.makeText(this, "Password can not be empty", Toast.LENGTH_SHORT).show()
        } else {
            mAuth
                .createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener{ task ->
                    if (task.isSuccessful) {
                        // success

                        // First get the user id after authentication process
                        fireBaseUserId = mAuth.currentUser!!.uid

                        // Now create reference user
                        refUser = FirebaseDatabase
                            .getInstance()
                            .reference
                            .child("Users")
                            .child(fireBaseUserId)

                        val userHashMap = HashMap<String, Any>()
                        userHashMap["uid"] = fireBaseUserId
                        userHashMap["username"] = username
                        userHashMap["profile"] = "https://firebasestorage.googleapis.com/v0/b/crew-talk-ded86.appspot.com/o/profile_picture.png?alt=media&token=31c9640d-0fd7-41b7-a5da-c57d4deb0cb8"
                        userHashMap["cover"] = "https://firebasestorage.googleapis.com/v0/b/crew-talk-ded86.appspot.com/o/cover_picture.jpeg?alt=media&token=1b2805cc-a241-4eae-8440-c108039e5fdc"
                        userHashMap["status"] = "offline"
                        userHashMap["search"] = username.toLowerCase()
                        userHashMap["facebook"] = "https://m.facebook.com"
                        userHashMap["instagram"] = "https://m.instagram.com"
                        userHashMap["website"] = "https://namnguyen31.com"


                        refUser
                            .updateChildren(userHashMap)
                            .addOnCompleteListener{task ->
                                if (task.isSuccessful) {
                                    // if success -> navigate user to main activity page
                                    val intent = Intent(this, MainActivity::class.java)
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                                    startActivity(intent)
                                    finish()
                                }
                            }
                    } else {
                        // fail
                        // the double-bang (!!) is the not-null assertion operator => There is at least an exception
                        Toast.makeText(this, "Error Message: "+task.exception!!.message.toString(), Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }
}