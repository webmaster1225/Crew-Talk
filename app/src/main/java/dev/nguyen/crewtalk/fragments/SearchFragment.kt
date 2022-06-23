package dev.nguyen.crewtalk.fragments

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import dev.nguyen.crewtalk.adapters.UserAdapter
import dev.nguyen.crewtalk.models.Users
import dev.nguyen.crewtalk.R
import dev.nguyen.crewtalk.activities.ChatActivity


class SearchFragment : Fragment() {

    private var userAdapter: UserAdapter? = null
    private var mUsers: List<Users>? = null
    private var recyclerView: RecyclerView? = null
    private var searchEditText: EditText? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        // Inflate the fragment_search layout for this fragment
        val view: View =  inflater.inflate(R.layout.fragment_search, container, false)

        // declare Views
        recyclerView = view.findViewById(R.id.searchList)
        searchEditText = view.findViewById(R.id.searchUsersET)
        mUsers = ArrayList()

        recyclerView!!.setHasFixedSize(true)
        recyclerView!!.layoutManager = LinearLayoutManager(context)

        // retrieveAllUser() will
        // 1. retrieve user and add them to mUsers array
        // 2. Apply userAdapter to recyclerView's adapter to show the users
        // 3. show users on the searchFragment layout
        retrieveAllUser()

        // make searchUsersET EditText view a live search
        searchEditText!!.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(cs: CharSequence?, start: Int, before: Int, count: Int) {
                // searchForUser() will
                // 1. retrieve user and add them to mUsers array
                // 2. Apply userAdapter to recyclerView's adapter to show the users
                // 3. show users on the searchFragment layout
                // basically same as retrieveAllUser() but searchForUser() shows only relevant users
                searchForUser(cs.toString().toLowerCase())
            }

            override fun afterTextChanged(s: Editable?) {

            }
        })



        return view
    }

    private fun retrieveAllUser() {
        // current user
        val firebaseUserID = FirebaseAuth
            .getInstance()
            .currentUser!!
            .uid

        // all the users in the database
        val refUsers = FirebaseDatabase
            .getInstance()
            .reference
            .child("Users")


        refUsers.addValueEventListener(object : ValueEventListener{

            override fun onDataChange(snapshot: DataSnapshot) {

                // Clear mUsers first
                (mUsers as ArrayList<Users>).clear()

                // if searchEditText == "" -> show all users
                if (searchEditText!!.text.toString().equals("")) {
                    // for every user/snapshot in the DataSnapShot -> add every user to the mUsers arrayList
                    for (dataSnapshot: DataSnapshot in snapshot.children) {
                        // retrieve user's info from snapshot value
                        val user: Users? = dataSnapshot.getValue(Users::class.java)

                        // except your own account, add all the found results to the mUsers account array list
                        if (!(user!!.getUid().equals(firebaseUserID))) {
                            (mUsers as ArrayList<Users>).add(user)
                        }
                    }

                    // init userAdapter with given values
                    userAdapter = UserAdapter(context!!, mUsers!!, false)
                    // Apply userAdapter to recyclerView's adapter to show the users
                    recyclerView!!.adapter = userAdapter
                    userAdapter!!.setOnItemClickListener(object: UserAdapter.onItemClickListener{
                        override fun onItemClick(position: Int) {
                            val intent = Intent(activity, ChatActivity::class.java)
                            intent.putExtra("userID", (mUsers as ArrayList<Users>)[position].getUid())
                            startActivity(intent)
                        }

                    })
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    private fun searchForUser(str: String) {

        // current user
        val firebaseUserID = FirebaseAuth
            .getInstance()
            .currentUser!!
            .uid

        val queryUsers = FirebaseDatabase
            .getInstance()
            .reference
            .child("Users")
            .orderByChild("search") // "search" is an attribute from user object
            .startAt(str)
            .endAt(str + "\uf8ff")

        queryUsers.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(p0: DataSnapshot) {
                // Clear mUsers first
                (mUsers as ArrayList<Users>).clear()

                // for every user/snapshot in the DataSnapShot -> add every user to the mUsers arrayList
                for (snapshot in p0.children) {
                    // retrieve user's info from snapshot value
                    val user: Users? = snapshot.getValue(Users::class.java)

                    // except your own account, add all the found results to the mUsers account array list
                    if (!(user!!.getUid().equals(firebaseUserID))) {
                        (mUsers as ArrayList<Users>).add(user)
                    }
                }

                // init userAdapter with given values
                userAdapter = UserAdapter(context!!, mUsers!!, false)
                recyclerView!!.adapter = userAdapter
                userAdapter!!.setOnItemClickListener(object: UserAdapter.onItemClickListener{
                    override fun onItemClick(position: Int) {
                        val intent = Intent(activity, ChatActivity::class.java)
                        intent.putExtra("userId", (mUsers as ArrayList<Users>)[position].getUid())
                        startActivity(intent)
                    }

                })
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })


    }

}