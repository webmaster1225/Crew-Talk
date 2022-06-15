package dev.nguyen.crewtalk.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import dev.nguyen.crewtalk.Adapters.UserAdapter
import dev.nguyen.crewtalk.Models.Users
import dev.nguyen.crewtalk.R

class SearchFragment : Fragment() {


    private var userAdapter: UserAdapter? = null
    private var mUsers: List<Users>? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val view: View =  inflater.inflate(R.layout.fragment_search, container, false)

        mUsers = ArrayList()
        retrieveAllUser()

        return view
    }

    private fun retrieveAllUser() {
        val firebaseUserID = FirebaseAuth
            .getInstance()
            .currentUser!!
            .uid


        val refUsers = FirebaseDatabase
            .getInstance()
            .reference
            .child("Users")

        refUsers.addValueEventListener(object : ValueEventListener{
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
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    private fun searchForUser(str: String) {
        val firebaseUserID = FirebaseAuth
            .getInstance()
            .currentUser!!
            .uid

        val queryUsers = FirebaseDatabase
            .getInstance()
            .reference
            .child("Users")
            .orderByChild("search")
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
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })


    }

}