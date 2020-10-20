package com.example.favapp.helper

import android.database.Cursor
import com.example.favapp.User
import com.example.favapp.db.UserContract

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