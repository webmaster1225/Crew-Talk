package dev.nguyen.crewtalk.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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
import dev.nguyen.crewtalk.fragments.SearchFragment
import dev.nguyen.crewtalk.models.Chats
import dev.nguyen.crewtalk.models.Users

class ChatActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChatBinding

    // Firebase users
    var refUsers: DatabaseReference? = null
    var firebaseUser: FirebaseUser? = null

    private var chatList = ArrayList<Chats>()
    private var recyclerView: RecyclerView? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        recyclerView = binding.chatRecyclerView
        recyclerView!!.setHasFixedSize(true)
        recyclerView!!.layoutManager = LinearLayoutManager(this)

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
        refUsers = FirebaseDatabase.getInstance().reference.child("Users").child(userID!!)

        // Update username and user profile pic based on refUsers
        refUsers!!.addValueEventListener(object: ValueEventListener{
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

        hashMap.put("senderID", senderID)
        hashMap.put("receiverID", receiverID)
        hashMap.put("message", message)

        reference!!.child("Chat").push().setValue(hashMap)
    }

    private fun readMessage(senderId: String, receiverId: String) {
        val databaseReference: DatabaseReference = FirebaseDatabase.getInstance().reference.child("Chat")

        databaseReference.addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                chatList.clear()

                // loop through snapshot's children to get chats
                for (dataSnapshot: DataSnapshot in snapshot.children) {
                    val chat = dataSnapshot.getValue(Chats::class.java)
                    if ((chat!!.senderId.equals(senderId) && chat.receiverId.equals(receiverId)) ||
                        (chat.senderId.equals(receiverId) && chat.receiverId.equals(senderId))
                    ) {
                        chatList.add(chat)
                    }
                }

                val chatAdapter = ChatAdapter(this@ChatActivity, chatList)
                recyclerView!!.adapter = chatAdapter
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }
}