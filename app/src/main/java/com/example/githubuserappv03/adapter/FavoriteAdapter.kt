package com.example.githubuserappv03.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.githubuserappv03.R
import com.example.githubuserappv03.User
import kotlinx.android.synthetic.main.item_row_favorite.view.*

class FavoriteAdapter (private val activity: Activity) : RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder>() {

    private lateinit var onItemClickCallback: OnItemClickCallback

    var listFavorites = ArrayList<User>()
        set(listFavorites) {
            if (listFavorites.size > 0) {
                this.listFavorites.clear()
            }
            this.listFavorites.addAll(listFavorites)
            notifyDataSetChanged()
        }

    fun setData(newListData: List<User>?) {
        if (newListData == null) return
        listFavorites.clear()
        listFavorites.addAll(newListData)
        notifyDataSetChanged()
    }


    fun addItem(user: User){
        this.listFavorites.add(user)
        notifyItemInserted(this.listFavorites.size - 1)
    }

    fun removeItem(position: Int) {
        this.listFavorites.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, this.listFavorites.size)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteAdapter.FavoriteViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_row_favorite, parent, false)
        return FavoriteViewHolder(view)
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        val post = listFavorites[position]
        holder.bind(listFavorites[position])
        holder.itemView.setOnClickListener() {
            onItemClickCallback
                .onItemClicked(listFavorites[holder.adapterPosition])
        }
    }

    override fun getItemCount(): Int = listFavorites.size

    inner class FavoriteViewHolder (itemView: View) :RecyclerView.ViewHolder(itemView){
        fun bind(user: User) {
            with(itemView) {
                Glide.with(itemView.context)
                    .load(user.avatar)
                    .apply(RequestOptions().override(55, 55))
                    .into(img_item_fav_photo)
                tv_item_fav_name.text = user.username
                setOnClickListener() {
                    onItemClickCallback
                        .onItemClicked(user)
                }
            }
        }
    }

    interface OnItemClickCallback {
        fun onItemClicked(post: User)
    }

    fun setOnItemClickCallback(onItemClickCallback: FavoriteAdapter.OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }
}