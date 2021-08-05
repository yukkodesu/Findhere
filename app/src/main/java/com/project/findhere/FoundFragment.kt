package com.project.findhere

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.TransitionManager
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.project.findhere.models.Post

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class FoundFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null
    // TODO: Make recyclerview inflate in Found fragment
    private lateinit var firebaseDb : FirebaseFirestore
    private lateinit var posts : MutableList<Post>
    private lateinit var adapter : FoundPostAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_found, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        firebaseDb = FirebaseFirestore.getInstance()
        posts = mutableListOf()
        adapter = FoundPostAdapter(this.requireContext(),posts)
        val rvfound : RecyclerView = this.requireView().findViewById(R.id.rvFound)
        rvfound.adapter = adapter
        rvfound.layoutManager = LinearLayoutManager(this.requireContext())
        rvfound.setHasFixedSize(true)
        val postsReference = firebaseDb
            .collection("posts")
            .limit(20)
            .orderBy("time_ms", Query.Direction.DESCENDING)
        postsReference.addSnapshotListener { snapshot, exception ->
            if(exception != null || snapshot == null) {
                Log.e("FoundFragment", "Exception when querying posts")
                return@addSnapshotListener
            }
            val postList = snapshot.toObjects(Post::class.java)
            posts.clear()
            repeat(8){
                posts.addAll(postList)
            }
            adapter.notifyDataSetChanged()
            for(post in postList){
                Log.d("FoundFragment","${post}")
            }
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment FoundFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FoundFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}