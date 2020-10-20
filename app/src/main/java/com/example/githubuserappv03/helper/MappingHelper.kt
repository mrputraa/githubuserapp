package com.example.githubuserappv03.helper

import android.database.Cursor
import com.example.githubuserappv03.User
import com.example.githubuserappv03.db.UserContract

object MappingHelper {
    fun mapCursorToArrayList(favCursor: Cursor?) : ArrayList<User> {
        val favList = ArrayList<User>()

        favCursor?.apply {
            while (moveToNext()) {
                val username = getString(getColumnIndexOrThrow(UserContract.UserColumns.COLUMN_NAME_USERNAME))
                val avatar = getString(getColumnIndexOrThrow(UserContract.UserColumns.COLUMN_NAME_AVATAR_URL))
                val company = getString(getColumnIndexOrThrow(UserContract.UserColumns.COLUMN_NAME_COMPANY))
                val location = getString(getColumnIndexOrThrow(UserContract.UserColumns.COLUMN_NAME_LOCATION))
                favList.add(User(avatar, username, company, location, null, null, null))
            }
        }
        return favList
    }
}