package alanis.jorge.sqliteprep

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper


class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "MyDatabase.db"
        private const val DATABASE_VERSION = 1
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val CREATE_TABLE_QUERY = "CREATE TABLE users (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, phone INTEGER, email TEXT)"
        db?.execSQL(CREATE_TABLE_QUERY)
    }


    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS users")
        onCreate(db)
    }

    fun addUser(name: String, phone: Long, email: String): Boolean {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put("name", name)
        contentValues.put("phone", phone)
        contentValues.put("email", email)


        val result = db.insert("users", null, contentValues)
        db.close()

        return result != -1L
    }

    fun getAllUsers(): Cursor {
        val db = this.readableDatabase
        return db.rawQuery("SELECT * FROM users", null)
    }

    fun updateUser(id: Int, name: String, phone: Long, email: String): Boolean {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put("name", name)
        contentValues.put("phone", phone)
        contentValues.put("email", email)



        val result = db.update("users", contentValues, "id = ?", arrayOf(id.toString()))
        db.close()

        return result > 0
    }

    fun deleteUser(id: Int): Boolean {
        val db = this.writableDatabase
        val result = db.delete("users", "id = ?", arrayOf(id.toString()))
        db.close()

        return result > 0
    }
}