package dev.nguyen.crewtalk.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import dev.nguyen.crewtalk.Models.Users
import dev.nguyen.crewtalk.R
import dev.nguyen.crewtalk.databinding.ActivityLoginBinding

class UserAdapter (
    // "private val" added here help declare the variables -- no need to init the vars inside the class body
    private val mContext: Context,
    private val mUsers: List<Users>,
    private val isChatCheck: Boolean,
) : RecyclerView.Adapter<UserAdapter.ViewHolder?>() {


    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var userameTxt : TextView = view.findViewById(R.id.username)
        var profileImageView : CircleImageView = view.findViewById(R.id.profile_image)
        var onlineTxt : TextView = view.findViewById(R.id.image_online)
        var offlineTxt : TextView = view.findViewById(R.id.image_offline)
        var lastMessageTxt : TextView = view.findViewById(R.id.message_last)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // The layout inflater knows how to inflate
        // an XML layout into a hierarchy of view objects.
        val adapterLayout: View = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.user_search_item_layout, parent, false) // adapterLayout holds a reference to the list item view

        return UserAdapter.ViewHolder(adapterLayout)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user: Users = mUsers[position]
        holder.userameTxt.text = user.getUserName()
        Picasso.get().load(user.getProfile()).placeholder(R.drawable.profile_picture).into(holder.profileImageView)
    }

    override fun getItemCount(): Int {
        return mUsers.size
    }
}