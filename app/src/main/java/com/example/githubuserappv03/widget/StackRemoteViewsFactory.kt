package com.example.githubuserappv03.widget

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Binder
import android.util.Log
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import androidx.core.os.bundleOf
import com.bumptech.glide.Glide
import com.example.githubuserappv03.R
import com.example.githubuserappv03.db.FavoriteHelper
import com.example.githubuserappv03.helper.MappingHelper

internal class StackRemoteViewsFactory(private val mContext: Context): RemoteViewsService.RemoteViewsFactory {

    private val mWidgetItems = ArrayList<Bitmap>()
    private lateinit var favHelper: FavoriteHelper


    override fun onDataSetChanged() {
        favHelper = FavoriteHelper.getInstance(mContext)
        favHelper.open()

        val identifyToken = Binder.clearCallingIdentity()
        Binder.restoreCallingIdentity(identifyToken)

        try {
            val cursorSearch = favHelper.queryAll()
            val cursor = MappingHelper.mapCursorToArrayList(cursorSearch)
            if (cursor.size > 0) {
                mWidgetItems.clear()
                for (i in 0 until cursor.size) {
                    try {
                        val avatar = cursor[i].avatar.toString()
                        val bitmap = Glide.with(mContext)
                            .asBitmap()
                            .load(avatar)
                            .submit()
                            .get()
                        mWidgetItems.add(bitmap)
                    } catch (e: Exception) {
                        Log.d("ErrorWidget", e.message.toString())
                    }
                }
            }
        } catch (e: IllegalStateException) {
            Log.d("ErrorWidget", "${e.message}")
        }
        favHelper.close()
    }

    override fun onCreate() {
        favHelper = FavoriteHelper.getInstance(mContext)
    }

    override fun onDestroy() {

    }

    override fun getViewAt(position: Int): RemoteViews {
        val remote = RemoteViews(mContext.packageName, R.layout.widget_item)
        remote.setImageViewBitmap(R.id.imageView, mWidgetItems[position])

        val extras = bundleOf(UserBannerWidget.EXTRA_ITEM to position)

        val fillInIntent = Intent()
        fillInIntent.putExtras(extras)

        remote.setOnClickFillInIntent(R.id.imageView, fillInIntent)
        return remote
    }

    override fun getLoadingView(): RemoteViews? = null

    override fun getItemId(p0: Int): Long = 0

    override fun hasStableIds(): Boolean = false

    override fun getCount(): Int = mWidgetItems.size

    override fun getViewTypeCount(): Int = 1



}