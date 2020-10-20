package com.example.favapp.db

import android.net.Uri
import android.provider.BaseColumns

object UserContract {

    const val AUTHORITY = "com.example.githubuserappv03"
    const val SCHEME = "content"


    internal class UserColumns : BaseColumns {
        companion object {
            const val TABLE_NAME = "favorites"
            const val _ID = "_id"
            const val COLUMN_NAME_AVATAR_URL = "avatar_url"
            const val COLUMN_NAME_USERNAME = "username"
            const val COLUMN_NAME_COMPANY = "company"
            const val COLUMN_NAME_LOCATION = "location"

            // untuk membuat URI content://com.example.githubuserappv03/favorites
            val CONTENT_URI: Uri = Uri.Builder().scheme(SCHEME)
                .authority(AUTHORITY)
                .appendPath(TABLE_NAME)
                .build()

        }
    }
}