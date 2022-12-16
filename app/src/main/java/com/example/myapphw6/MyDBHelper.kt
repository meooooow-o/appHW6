package com.example.myapphw6

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class MySQLiteOpenHelper (context: Context): SQLiteOpenHelper(context, name,null, version){
    companion object{
        private const val name = "myDatabase.bd"
        private const val version = 1
    }

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL("CREATE TABLE Phone(id text PRIMARY KEY, name text NOT NULL, phone text NOT NULL, address text, other text)")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS Phone")
        onCreate(db)
    }
}