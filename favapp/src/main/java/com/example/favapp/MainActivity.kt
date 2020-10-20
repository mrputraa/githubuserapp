package com.example.favapp

import android.database.ContentObserver
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.favapp.adapter.FavoriteAdapter
import com.example.favapp.db.UserContract
import com.example.favapp.helper.MappingHelper
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_favorite.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var favAdapter: FavoriteAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorite)

        supportActionBar?.setTitle(R.string.fav_list)

        rv_fav_user.layoutManager = LinearLayoutManager(this)
        rv_fav_user.setHasFixedSize(true)
        favAdapter = FavoriteAdapter(this)
        rv_fav_user.adapter = favAdapter

        val handlerThread = HandlerThread("DataObserver")
        handlerThread.start()
        val handler = Handler(handlerThread.looper)

        val myObserver = object : ContentObserver(handler) {
            override fun onChange(self: Boolean) {
                loadDataFromDB()
            }
        }
        contentResolver.registerContentObserver(UserContract.UserColumns.CONTENT_URI, true, myObserver)
        if (savedInstanceState == null) {
            loadDataFromDB()
        }

    }


    private fun loadDataFromDB(){
        GlobalScope.launch(Dispatchers.Main) {
            fa_progressBar.visibility = View.VISIBLE
            val deferredFav = async(Dispatchers.IO) {
                val cursor = contentResolver?.query(UserContract.UserColumns.CONTENT_URI, null, null, null, null)
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
                        showSnackbarMessage("Anda memilih " + post.username)
                    }
                })
            } else {
                favAdapter.listFavorites = ArrayList()
                showSnackbarMessage("Tidak ada data saat ini")
            }
        }
    }

    private fun showSnackbarMessage(message: String) {
        Snackbar.make(rv_fav_user, message, Snackbar.LENGTH_SHORT).show()
    }

}
