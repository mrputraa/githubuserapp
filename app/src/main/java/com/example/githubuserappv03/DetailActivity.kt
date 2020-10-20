package com.example.githubuserappv03

import android.content.ContentValues
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.githubuserappv03.adapter.FavoriteAdapter
import com.example.githubuserappv03.adapter.PagerAdapter
import com.example.githubuserappv03.db.FavoriteHelper
import com.example.githubuserappv03.db.UserContract
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import kotlinx.android.synthetic.main.activity_detail.*
import org.json.JSONObject

class DetailActivity : AppCompatActivity(), View.OnClickListener {

    companion object{
        const val EXTRA_USER = "extra_user"
    }
    private lateinit var userItem: User
    private lateinit var favoriteHelper: FavoriteHelper
    private lateinit var favoriteAdapter: FavoriteAdapter

    private var statusFavorite = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        supportActionBar?.elevation = 0f
        supportActionBar?.setTitle(R.string.detail)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val post = intent.getStringExtra(EXTRA_USER)
        val pagerAdapter = PagerAdapter(this, supportFragmentManager)

        pagerAdapter.username = post
        view_pager.adapter = pagerAdapter
        tab_layout_detail.setupWithViewPager(view_pager)

        favoriteAdapter = FavoriteAdapter(this)
        //inisiasi FavoriteHelper
        favoriteHelper = FavoriteHelper.getInstance(applicationContext)
        favoriteHelper.open()

        statusFavorite = favoriteHelper.queryByUsername(post!!)
        setStatusFavorite(statusFavorite)
        getDetailUserData(post!!)

        btn_favorite.setOnClickListener(this)
    }

    private fun getDetailUserData(username: String) {
        val url = "https://api.github.com/users/${username}"

        val client = AsyncHttpClient()
        client.addHeader("Authorization", "token 220327da9f883bc210e4b30706de31aabea9ea85")
        client.addHeader("User-Agent", "request")

        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Array<out Header>?, responseBody: ByteArray) {
                try {
                    val responseObject = JSONObject(String(responseBody))
                    userItem = User(
                        username = username,
                        avatar = responseObject.getString("avatar_url"),
                        company = responseObject.getString("company"),
                        location = responseObject.getString("location"),
                        follower = responseObject.getInt("followers").toString(),
                        following = responseObject.getInt("following").toString(),
                        repository = responseObject.getInt("public_repos").toString()
                    )
                    prepare(userItem)

                } catch (e: Exception) {
                    Toast.makeText(this@DetailActivity, e.message, Toast.LENGTH_SHORT).show()
                    e.printStackTrace()
                }
            }

            override fun onFailure(statusCode: Int, headers: Array<out Header>?, responseBody: ByteArray?, error: Throwable?) {
                val errorMessage = when (statusCode) {
                    401 -> "$statusCode : Bad Request"
                    403 -> "$statusCode : Forbidden"
                    404 -> "$statusCode : Not Found"
                    else -> "$statusCode : ${error?.message}"
                }
                Toast.makeText(this@DetailActivity, errorMessage, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun prepare(user: User) {
        Glide.with(this@DetailActivity)
            .load(user.avatar)
            .apply(RequestOptions().override(55, 55))
            .into(civ_post_image)
        tv_post_username.text = user.username
        val empty = getString(R.string.empty)
        tv_post_company.text = if (user.company.toString() != "null") user.company else empty
        tv_post_location.text = if (user.location.toString() != "null") user.location else empty
        if (user.repository.toString() != "null") user.repository else "0"
        val repoText = "${user.repository} Repositories"
        tv_post_repository.text = repoText
    }

    override fun onClick(view: View) {
        if (view.id == R.id.btn_favorite){
            if (!statusFavorite){
                val empty = getString(R.string.empty)
                userItem.company = if (userItem.company.toString() != "null") userItem.company else empty
                userItem.location = if (userItem.location.toString() != "null") userItem.location else empty

                val values = ContentValues()
                values.put(UserContract.UserColumns.COLUMN_NAME_AVATAR_URL, userItem.avatar)
                values.put(UserContract.UserColumns.COLUMN_NAME_USERNAME, userItem.username)
                values.put(UserContract.UserColumns.COLUMN_NAME_COMPANY, userItem.company)
                values.put(UserContract.UserColumns.COLUMN_NAME_LOCATION, userItem.location)

                favoriteHelper.insert(values)
                statusFavorite = true
                setStatusFavorite(statusFavorite)
                Toast.makeText(this, getString(R.string.fav_true), Toast.LENGTH_SHORT).show()
            }
            else {
                favoriteHelper.deleteByUsername(userItem.username.toString()).toLong()

                statusFavorite = false
                setStatusFavorite(statusFavorite)

                Toast.makeText(this, getString(R.string.fav_false), Toast.LENGTH_SHORT).show()
            }

        }
    }



    private fun setStatusFavorite(statusFavorite: Boolean) {
        if (statusFavorite) {
            btn_favorite.setImageResource(R.drawable.ic_favorite_true_24dp)
        }
        else {
            btn_favorite.setImageResource(R.drawable.ic_favorite_false_24dp)
        }
    }




}
