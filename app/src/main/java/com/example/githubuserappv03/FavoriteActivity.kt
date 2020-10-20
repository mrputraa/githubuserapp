package com.example.githubuserappv03

import android.content.Intent
import android.database.ContentObserver
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubuserappv03.adapter.FavoriteAdapter
import com.example.githubuserappv03.adapter.ListUserAdapter
import com.example.githubuserappv03.db.FavoriteHelper
import com.example.githubuserappv03.db.UserContract.UserColumns.Companion.CONTENT_URI
import com.example.githubuserappv03.helper.MappingHelper
import com.google.android.material.snackbar.Snackbar
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.activity_favorite.*
import kotlinx.android.synthetic.main.item_row_favorite.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class FavoriteActivity : AppCompatActivity() {

    private lateinit var favHelper: FavoriteHelper
    private lateinit var favAdapter: FavoriteAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorite)

        supportActionBar?.setTitle(R.string.fav_list)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        favHelper = FavoriteHelper.getInstance(applicationContext)
        favHelper.open()

        rv_fav_user.layoutManager = LinearLayoutManager(this)
        rv_fav_user.setHasFixedSize(true)
        favAdapter = FavoriteAdapter(this)
        rv_fav_user.adapter = favAdapter

//        loadDataFromDB()

        val handlerThread = HandlerThread("DataObserver")
        handlerThread.start()
        val handler = Handler(handlerThread.looper)

        val myObserver = object : ContentObserver(handler) {
            override fun onChange(self: Boolean) {
                loadDataFromDB()
            }
        }
        contentResolver.registerContentObserver(CONTENT_URI, true, myObserver)
        if (savedInstanceState == null) {
            loadDataFromDB()
        }

    }


    private fun loadDataFromDB(){
        GlobalScope.launch(Dispatchers.Main) {
            fa_progressBar.visibility = View.VISIBLE
            val deferredFav = async(Dispatchers.IO) {
                val cursor = contentResolver?.query(CONTENT_URI, null, null, null, null)
//                val cursor = favHelper.queryAll()
                MappingHelper.mapCursorToArrayList(cursor)
            }
            fa_progressBar.visibility = View.INVISIBLE
            val favs = deferredFav.await()
            if (favs.size > 0) {
                val listItems = ArrayList<User>()
                for (i in 0 until favs.size) {
                    val data = favs[i]
                    val userItems = User(
                        avatar = data.avatar,
                        username = data.username,
                        company = data.company,
                        location = data.location,
                        following = "",
                        follower = "",
                        repository = ""
                    )
                    listItems.add(userItems)
                }
                favAdapter.setData(listItems)
                favAdapter.setOnItemClickCallback(object : FavoriteAdapter.OnItemClickCallback{
                    override fun onItemClicked(post: User) {
                        showSelectedPost(post)
                    }
                })
            } else {
                favAdapter.listFavorites = ArrayList()
                showSnackbarMessage("Tidak ada data saat ini")
            }
        }
    }

    private fun showSelectedPost(post: User){
        val moveToDetailedIntent = Intent(this@FavoriteActivity, DetailActivity::class.java)
            .apply {
                putExtra(DetailActivity.EXTRA_USER, post.username)
            }
        startActivity(moveToDetailedIntent)

    }

    override fun onDestroy() {
        super.onDestroy()
        favHelper.close()

    }

    private fun showSnackbarMessage(message: String) {
        Snackbar.make(rv_fav_user, message, Snackbar.LENGTH_SHORT).show()
    }
}
