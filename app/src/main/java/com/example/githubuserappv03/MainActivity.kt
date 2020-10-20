package com.example.githubuserappv03

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.githubuserappv03.adapter.ListUserAdapter
import com.example.githubuserappv03.fragment.SettingFragment
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject

class MainActivity : AppCompatActivity() {
    private lateinit var userAdapter: ListUserAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        userAdapter = ListUserAdapter()
        showLoading(false)
        showHint(true)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.options_menu, menu)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu.findItem(R.id.search).actionView as SearchView
        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.queryHint = resources.getString(R.string.search_hint)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                getDataUserFormApi(query)
                return true
            }
            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.setting) {
            val sIntent = Intent(this@MainActivity, SettingActivity::class.java)
            startActivity(sIntent)
        }
        if (item.itemId == R.id.favorite){
            val fIntent = Intent(this@MainActivity, FavoriteActivity::class.java)
            startActivity(fIntent)
        }
        return super.onOptionsItemSelected(item)
    }

    private fun getDataUserFormApi(username: String) {
        showHint(false)
        showLoading(true)
        val listItems = ArrayList<User>()
        val client = AsyncHttpClient()

        client.addHeader("Authorization", "token 220327da9f883bc210e4b30706de31aabea9ea85")
        client.addHeader("User-Agent", "request")

        val url = "https://api.github.com/search/users?q=${username}"

        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Array<Header>, responseBody: ByteArray) {
                try {
                    //parsing JSON
                    val result = String(responseBody)
                    val responseObject = JSONObject(result)
                    val list = responseObject.getJSONArray("items")

                    for (i in 0 until list.length()) {
                        val user = list.getJSONObject(i)
                        val userItems = User(
                            username = user.getString("login"),
                            avatar = user.getString("avatar_url"),
                            company = "",
                            follower = "",
                            following = "",
                            location = "",
                            repository = ""
                        )
                        listItems.add(userItems)
                    }
                    showRecyclerList(listItems)
                    showLoading(false)

                } catch (e: Exception) {
                    Log.d("Exception", e.message.toString())
                }
            }
            override fun onFailure(statusCode: Int, headers: Array<Header>, responseBody: ByteArray?, error: Throwable) {
                Log.d("onFailure", error.message.toString())
            }

        })

    }

    private fun showRecyclerList(list: ArrayList<User>) {
        rv_user.layoutManager = LinearLayoutManager(this)
        userAdapter.setData(list)
        rv_user.setHasFixedSize(true)
        rv_user.adapter = userAdapter

        userAdapter.setOnItemClickCallback(object : ListUserAdapter.OnItemClickCallback{
            override fun onItemClicked(post: User) {
                showSelectedPost(post)
            }
        })
    }

    private fun showSelectedPost(post: User){
        val moveToDetailedIntent = Intent(this@MainActivity, DetailActivity::class.java)
            .apply {
                putExtra(DetailActivity.EXTRA_USER, post.username)
            }
        startActivity(moveToDetailedIntent)

    }

    private fun showLoading(state: Boolean) {
        if (state) {
            ma_progressBar.visibility = View.VISIBLE
        } else {
            ma_progressBar.visibility = View.GONE
        }
    }

    private fun showHint(state: Boolean) {
        if (state) {
            main_hint.visibility = View.VISIBLE
        } else {
            main_hint.visibility = View.GONE
        }
    }

}
