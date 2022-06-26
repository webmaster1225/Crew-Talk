package dev.nguyen.crewtalk.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.LinearLayout
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import dev.nguyen.crewtalk.R
import dev.nguyen.crewtalk.adapters.ChatAdapter
import dev.nguyen.crewtalk.databinding.ActivityChatBinding
import dev.nguyen.crewtalk.models.Chats
import dev.nguyen.crewtalk.models.Users

class ChatActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChatBinding

    // Firebase users
    private var databaseReference: DatabaseReference? = null
    private var firebaseUser: FirebaseUser? = null

    private var chatList = ArrayList<Chats>()
    private var recyclerView: RecyclerView? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        recyclerView = binding.chatRecyclerView
        recyclerView!!.setHasFixedSize(true)
        recyclerView!!.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        // setup intent for back img button
        binding.icBack.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        // get the userID passed in from Intent from RecyclerView
        var intent = intent
        var userID = intent.getStringExtra("userID")

        // get current firebaseUser
        firebaseUser = FirebaseAuth.getInstance().currentUser

        // get refUser from Intent
        databaseReference = FirebaseDatabase.getInstance().reference.child("Users").child(userID!!)

        // Update username and user profile pic based on refUsers
        databaseReference!!.addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val user: Users? = snapshot.getValue(Users::class.java)
                    binding.userName.text = user!!.getUserName()
                    Picasso.get().load(user.getProfile()).placeholder(R.drawable.profile_picture).into(binding.profileImage)
                }
            }
            override fun onCancelled(error: DatabaseError) {}
        })


        binding.etSendBtn.setOnClickListener{
            var message: String = binding.etMsg.text.toString()

            if (message.isEmpty()) {
                Toast.makeText(this, "Message is empty", Toast.LENGTH_SHORT).show()
            } else {
                sendMessage(firebaseUser!!.uid,userID, message)
                binding.etMsg.setText("")
                Toast.makeText(this, "Message sent", Toast.LENGTH_SHORT).show()
            }
        }

        // readMessage is always there
        readMessage(firebaseUser!!.uid, userID)
    }

    private fun sendMessage(senderID: String, receiverID: String, message: String) {

        var reference: DatabaseReference? = FirebaseDatabase.getInstance().reference

        var hashMap: HashMap<String, String> = HashMap()
        hashMap["senderID"] = senderID
        hashMap["receiverID"] = receiverID
        hashMap["message"] = message

        reference!!.child("Chat").push().setValue(hashMap)
    }

    private fun readMessage(senderId: String, receiverId: String) {
        val databaseRef: DatabaseReference = FirebaseDatabase.getInstance().reference.child("Chat")

        databaseRef.addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                chatList.clear()

                // loop through snapshot's children to get chats
                for (dataSnapshot: DataSnapshot in snapshot.children) {
                    val chat = dataSnapshot.getValue(Chats::class.java)
                    if (((chat!!.getSenderId() == senderId) && (chat.getReceiverId() == receiverId)) ||
                        ((chat.getSenderId() == receiverId) && (chat.getReceiverId() == senderId))
                    ) {
                        chatList.add(chat)
                    }
                }

                chatList.forEach{
                    Log.d("chat", it.getMessage().toString())
                }

                val chatAdapter = ChatAdapter(this@ChatActivity, chatList)
                recyclerView!!.adapter = chatAdapter
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }
}