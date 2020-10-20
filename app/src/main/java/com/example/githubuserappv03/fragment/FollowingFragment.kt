package com.example.githubuserappv03.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubuserappv03.DetailActivity
import com.example.githubuserappv03.R
import com.example.githubuserappv03.User
import com.example.githubuserappv03.adapter.FollowAdapter
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import kotlinx.android.synthetic.main.fragment_following.*
import org.json.JSONArray
import java.lang.Exception


class FollowingFragment : Fragment() {

    companion object {
        private val ARG_USERNAME = "username"

        fun newInstance(username: String?): Fragment{
            val fragment =
                FollowingFragment()
            val bundle = Bundle()
            bundle.putString(ARG_USERNAME, username)
            fragment.arguments = bundle
            return fragment
        }
    }

    private lateinit var followAdapter: FollowAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        followAdapter = FollowAdapter()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_following, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val username = arguments?.getString(ARG_USERNAME)
        getFollowingByUsername(username)


    }

    private fun getFollowingByUsername(username: String?){
        showLoading(true)
        val listItems = ArrayList<User>()

        val client = AsyncHttpClient()

        client.addHeader("Authorization", "token 220327da9f883bc210e4b30706de31aabea9ea85")
        client.addHeader("User-Agent", "request")

        val url = "https://api.github.com/users/${username}/following"

        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Array<out Header>?, responseBody: ByteArray) {
                try {
                    //parsing JSON
                    val result = String(responseBody)
                    val jsonArray = JSONArray(result)

                    for (i in 0 until jsonArray.length()){
                        val following = jsonArray.getJSONObject(i)
                        val followingItems = User(
                            username = following.getString("login"),
                            avatar = following.getString("avatar_url"),
                            company = "",
                            follower = "",
                            following = "",
                            location = "",
                            repository = ""
                        )
                        listItems.add(followingItems)
                    }
                    showRecyclerList(listItems)
                    showLoading(false)


                } catch (e: Exception){
                    Log.d("Exception", e.message.toString())
                }
            }

            override fun onFailure(statusCode: Int, headers: Array<out Header>?, responseBody: ByteArray?, error: Throwable) {
                Log.d("onFailure", error.message.toString())
            }

        })
    }

    private fun showRecyclerList(list: ArrayList<User>) {
        rv_user_following.layoutManager = LinearLayoutManager(activity)
        followAdapter.setData(list)
        rv_user_following.setHasFixedSize(true)
        rv_user_following.adapter = followAdapter

        followAdapter.setOnItemClickCallback(object : FollowAdapter.OnItemClickCallback{
            override fun onItemClicked(post: User) {
                showSelectedPost(post)
            }
        })
    }

    private fun showSelectedPost(post: User){
        val moveToDetailedIntent = Intent(activity, DetailActivity::class.java)
            .apply {
                putExtra(DetailActivity.EXTRA_USER, post.username)
            }
        startActivity(moveToDetailedIntent)

    }

    private fun showLoading(state: Boolean) {
        if (state) {
            fg_progressBar.visibility = View.VISIBLE
        } else {
            fg_progressBar.visibility = View.GONE
        }
    }

}
