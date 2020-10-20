package com.example.githubuserappv03.db


import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.githubuserappv03.db.UserContract.UserColumns.Companion.TABLE_NAME
import com.example.githubuserappv03.db.UserContract.UserColumns.Companion._ID

internal class UserHelper (context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        private const val DATABASE_NAME = "dbfavorite"
        private const val DATABASE_VERSION = 1

        private val SQL_CREATE_TABLE_FAVORITE = "CREATE TABLE $TABLE_NAME" +
                " (${_ID} INTEGER PRIMARY KEY AUTOINCREMENT," +
                " ${UserContract.UserColumns.COLUMN_NAME_USERNAME} TEXT NOT NULL," +
                " ${UserContract.UserColumns.COLUMN_NAME_AVATAR_URL} TEXT," +
                " ${UserContract.UserColumns.COLUMN_NAME_COMPANY} TEXT," +
                " ${UserContract.UserColumns.COLUMN_NAME_LOCATION} TEXT)"
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_TABLE_FAVORITE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

}