package com.project.findhere

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.project.findhere.models.FindPost

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class FindFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var firebaseDb : FirebaseFirestore
    private lateinit var findPosts : MutableList<FindPost>
    private lateinit var adapter: FindPostAdapter

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
        return inflater.inflate(R.layout.fragment_find, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        firebaseDb = FirebaseFirestore.getInstance()
        findPosts = mutableListOf()
        adapter = FindPostAdapter(this.requireContext(), findPosts)
        val rvfind: RecyclerView = this.requireView().findViewById(R.id.rvFind)
        rvfind.adapter = adapter
        rvfind.layoutManager = LinearLayoutManager(this.requireContext())
        rvfind.setHasFixedSize(true)
        val postsReference = firebaseDb
            .collection("findposts")
            .limit(20)
            .orderBy("time_ms",Query.Direction.DESCENDING)
        postsReference.addSnapshotListener { snapshot, exception ->
            if(exception != null || snapshot == null) {
                Log.e("FindFragment", "Exception when querying posts")
                return@addSnapshotListener
            }
            val postList = snapshot.toObjects(FindPost::class.java)
            findPosts.clear()
            findPosts.addAll(postList)
            adapter.notifyDataSetChanged()
            for(post in postList){
                Log.d("FindFragment","${post}")
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
         * @return A new instance of fragment FindFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FindFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}