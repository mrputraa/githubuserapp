package com.example.githubuserappv03.provider

import android.content.ContentProvider
import android.content.ContentValues
import android.content.Context
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import com.example.githubuserappv03.db.FavoriteHelper
import com.example.githubuserappv03.db.UserContract.AUTHORITY
import com.example.githubuserappv03.db.UserContract.UserColumns.Companion.CONTENT_URI
import com.example.githubuserappv03.db.UserContract.UserColumns.Companion.TABLE_NAME

class FavoriteProvider : ContentProvider() {

    companion object {
        private const val USER = 1
        private val sUriMatcher = UriMatcher(UriMatcher.NO_MATCH)
        private lateinit var favHelper: FavoriteHelper

        init {
            // content://com.example.githubuserappv03/favorites
            sUriMatcher.addURI(AUTHORITY, TABLE_NAME, USER)
        }
    }


    override fun onCreate(): Boolean {
        favHelper = FavoriteHelper.getInstance(context as Context)
        favHelper.open()
        return true
    }

    override fun query(uri: Uri, strings: Array<String>?, s: String?, strings1: Array<String>?, s1: String?): Cursor? {
        val cursor: Cursor?
        when (sUriMatcher.match(uri)) {
            USER -> cursor = favHelper.queryAll()
            else -> cursor = null
        }
        return cursor
    }

    override fun getType(uri: Uri): String? {
        return null
    }

    override fun insert(uri: Uri, contentValues: ContentValues?): Uri? {
        val added: Long = when (USER) {
            sUriMatcher.match(uri) -> favHelper.insert(contentValues)
            else -> 0
        }
        context?.contentResolver?.notifyChange(CONTENT_URI, null)
        return Uri.parse("$CONTENT_URI/$added")
    }

    override fun update(uri: Uri, contentValues: ContentValues?, s: String?, strings: Array<String>?): Int {
        return 0
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        return 0
    }

}











