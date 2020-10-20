package com.example.githubuserappv03.db

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.example.githubuserappv03.db.UserContract.UserColumns.Companion.COLUMN_NAME_USERNAME
import com.example.githubuserappv03.db.UserContract.UserColumns.Companion.TABLE_NAME
import com.example.githubuserappv03.db.UserContract.UserColumns.Companion._ID
import java.sql.SQLException

class FavoriteHelper(context: Context) {

    private var userHelper : UserHelper = UserHelper(context)
    private lateinit var database: SQLiteDatabase

    companion object {
        private const val DATABASE_TABLE = TABLE_NAME


        private var INSTANCE : FavoriteHelper? = null
        fun getInstance(context: Context): FavoriteHelper =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: FavoriteHelper(context)
            }

    }

    init {
        userHelper = UserHelper(context)
    }

    @Throws(SQLException::class)
    fun open() {
        database = userHelper.writableDatabase
    }

    fun close() {
        userHelper.close()

        if (database.isOpen) {
            database.close()
        }
    }

    fun queryAll() : Cursor {
        return database.query(
            DATABASE_TABLE,
            null,
            null,
            null,
            null,
            null,
            "$_ID ASC"
        )
    }

    fun queryByUsername(username: String): Boolean {
        val cursor = database.query(
            DATABASE_TABLE,
            null,
            "$COLUMN_NAME_USERNAME = ?",
            arrayOf(username),
            null,
            null,
            null,
            null
        )
        return cursor.count > 0
    }


    fun insert(values: ContentValues?): Long {
        return database.insert(DATABASE_TABLE, null, values)
    }

    fun deleteByUsername(id: String): Int {
        return database.delete(DATABASE_TABLE, "$COLUMN_NAME_USERNAME = '$id'", null)
    }


}