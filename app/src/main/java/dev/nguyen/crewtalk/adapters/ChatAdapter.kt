package dev.nguyen.crewtalk.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import de.hdodenhof.circleimageview.CircleImageView
import dev.nguyen.crewtalk.models.Chats
import dev.nguyen.crewtalk.R

class ChatAdapter (
    private val mContext: Context,
    private val mChats: List<Chats>,
) : RecyclerView.Adapter<ChatAdapter.ViewHolder?>() {

    private val MESSAGE_TYPE_LEFT = 0
    private val MESSAGE_TYPE_RIGHT = 1
    private lateinit var senderID: String
    private lateinit var receiverID: String

    var firebaseUser: FirebaseUser? = null
    var refUsers: DatabaseReference? = null


    override fun getItemViewType(position: Int): Int {
        firebaseUser = FirebaseAuth.getInstance().currentUser
        return if (mChats[position].getSenderId() == firebaseUser!!.uid) {
            MESSAGE_TYPE_RIGHT
        } else {
            MESSAGE_TYPE_LEFT
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return if (viewType == MESSAGE_TYPE_RIGHT) {
            Log.d("viewHolder-1", viewType.toString())
            val view =
                LayoutInflater
                    .from(parent.context)
                    .inflate(R.layout.chat_right, parent, false)
            ViewHolder(view)
        } else {
            Log.d("viewHolder-2", viewType.toString())
            val view =
                LayoutInflater
                    .from(parent.context)
                    .inflate(R.layout.chat_left, parent, false)
            ViewHolder(view)
        }
    }

    override fun getItemCount(): Int {
        return mChats.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val chat: Chats = mChats[position]
        holder.txtUserName.text = chat.getMessage()

        // get firebase user
//        refUsers = FirebaseDatabase.getInstance().reference.child("Users").child()
//        Picasso.get().load(user.getProfile()).placeholder(R.drawable.profile_picture).into(holder.imgUser)
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val txtUserName: TextView = view.findViewById(R.id.tvMessage)
        val imgUser: CircleImageView = view.findViewById(R.id.tvUserProf)
    }

}
