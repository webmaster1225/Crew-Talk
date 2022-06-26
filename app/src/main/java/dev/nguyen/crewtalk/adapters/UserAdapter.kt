package dev.nguyen.crewtalk.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import dev.nguyen.crewtalk.models.Users
import dev.nguyen.crewtalk.R

class UserAdapter (
    // "private val" added here help declare the variables -- no need to init the vars inside the class body
    private val mContext: Context,
    private val mUsers: List<Users>,
) : RecyclerView.Adapter<UserAdapter.ViewHolder?>() {

    private lateinit var mListener: onItemClickListener

    interface onItemClickListener {
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: onItemClickListener) {
        mListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // The layout inflater knows how to inflate
        // an XML layout into a hierarchy of view objects.

        // Declare layout_id for View Holder -- detect layout_id
        val adapterLayout: View = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.user_search_item_layout, parent, false) // adapterLayout holds a reference to the list item view

        return ViewHolder(adapterLayout, mListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user: Users = mUsers[position]
        holder.userNameTxt.text = user.getUserName()
        Picasso.get().load(user.getProfile()).placeholder(R.drawable.profile_picture).into(holder.profileImageView)
    }

    override fun getItemCount(): Int {
        return mUsers.size
    }

    class ViewHolder(view: View, listener: UserAdapter.onItemClickListener) : RecyclerView.ViewHolder(view) {
        var userNameTxt : TextView = view.findViewById(R.id.username)
        var profileImageView : CircleImageView = view.findViewById(R.id.profile_image)

        init {
            view.setOnClickListener{
                listener.onItemClick(adapterPosition)
            }
        }
    }
}
