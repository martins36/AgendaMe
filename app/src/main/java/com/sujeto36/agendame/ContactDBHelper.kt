package com.sujeto36.agendame

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.SQLException
import android.database.sqlite.SQLiteConstraintException
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class ContactDBHelper (context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_ENTRIES)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL(SQL_DELETE_ENTRIES)
        onCreate(db)
    }

    @Throws(SQLiteConstraintException::class)
    fun insertContact(contact: ContactModel) {
        val db = writableDatabase
        val values = ContentValues()
        val alias = contact.alias

        values.put(DBContract.UserEntry.COLUMN_FIRST_NAME, contact.firstName)
        values.put(DBContract.UserEntry.COLUMN_LAST_NAME, contact.lastName)
        values.put(DBContract.UserEntry.COLUMN_TELEPHONE, contact.telephone)
        values.put(DBContract.UserEntry.COLUMN_PHONE, contact.phone)
        values.put(DBContract.UserEntry.COLUMN_ADDRESS, contact.address)
        if (alias == "") {
            values.put(DBContract.UserEntry.COLUMN_NAME, contact.lastName + " " + contact.firstName)
        }
        else {
            values.put(DBContract.UserEntry.COLUMN_NAME, contact.lastName + " " + contact.firstName + " ($alias)")
        }
        values.put(DBContract.UserEntry.COLUMN_ALIAS, alias)
        values.put(DBContract.UserEntry.COLUMN_STATUS, 1)

        db.insert(DBContract.UserEntry.TABLE_NAME, null, values)
        db.close()
    }

    @Throws(SQLiteConstraintException::class)
    fun deleteContact(contactId: Int) {
        val db = writableDatabase
        val selection = DBContract.UserEntry.COLUMN_ID + " LIKE ?"
        val selectionArgs = arrayOf(contactId.toString())
        val values = ContentValues()
        values.put(DBContract.UserEntry.COLUMN_STATUS, 0)
        db.update(DBContract.UserEntry.TABLE_NAME, values, selection, selectionArgs)
        db.close()
    }

    @Throws(SQLiteConstraintException::class)
    fun updateContact(contact: ContactModel){
        val db = writableDatabase
        val selection = DBContract.UserEntry.COLUMN_ID + " LIKE ?"
        val selectionArgs = arrayOf(contact.contactId.toString())
        val values = ContentValues()
        val alias = contact.alias

        values.put(DBContract.UserEntry.COLUMN_FIRST_NAME, contact.firstName)
        values.put(DBContract.UserEntry.COLUMN_LAST_NAME, contact.lastName)
        values.put(DBContract.UserEntry.COLUMN_TELEPHONE, contact.telephone)
        values.put(DBContract.UserEntry.COLUMN_PHONE, contact.phone)
        values.put(DBContract.UserEntry.COLUMN_ADDRESS, contact.address)
        if (alias == "") {
            values.put(DBContract.UserEntry.COLUMN_NAME, contact.lastName + " " + contact.firstName)
        }
        else {
            values.put(DBContract.UserEntry.COLUMN_NAME, contact.lastName + " " + contact.firstName + " ($alias)")
        }
        values.put(DBContract.UserEntry.COLUMN_ALIAS, alias)

        db.update(DBContract.UserEntry.TABLE_NAME, values, selection, selectionArgs)
        db.close()
    }

    @Throws(SQLiteConstraintException::class)
    fun readAllContact(): ArrayList<ContactModel> {
        val db = writableDatabase
        val contacts = ArrayList<ContactModel>()
        var cursor: Cursor? = null
        try {
            cursor = db.rawQuery("SELECT * FROM " + DBContract.UserEntry.TABLE_NAME
                    + " WHERE ${DBContract.UserEntry.COLUMN_STATUS} = 1", null)
        }
        catch (e: SQLException) {
            Log.e("error read all", e.toString())
        }
        var contactId: Int
        var firstName: String
        var lastName: String
        var telephone: String
        var phone: String
        var address: String
        var name: String
        var alias: String
        var status: Int

        if (cursor!!.moveToFirst()) {
            while (!cursor.isAfterLast) {
                contactId = cursor.getInt(cursor.getColumnIndex(DBContract.UserEntry.COLUMN_ID))
                firstName = cursor.getString(cursor.getColumnIndex(DBContract.UserEntry.COLUMN_FIRST_NAME))
                lastName = cursor.getString(cursor.getColumnIndex(DBContract.UserEntry.COLUMN_LAST_NAME))
                telephone = cursor.getString(cursor.getColumnIndex(DBContract.UserEntry.COLUMN_TELEPHONE))
                phone = cursor.getString(cursor.getColumnIndex(DBContract.UserEntry.COLUMN_PHONE))
                address = cursor.getString(cursor.getColumnIndex(DBContract.UserEntry.COLUMN_ADDRESS))
                name = cursor.getString(cursor.getColumnIndex(DBContract.UserEntry.COLUMN_NAME))
                alias = cursor.getString(cursor.getColumnIndex(DBContract.UserEntry.COLUMN_ALIAS))
                status = cursor.getInt(cursor.getColumnIndex(DBContract.UserEntry.COLUMN_STATUS))

                contacts.add(ContactModel(contactId, firstName, lastName, telephone, phone, address, name, alias, status))
                cursor.moveToNext()
            }
        }
        cursor.close()
        return contacts
    }

    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "AgendaMe.db"

        private const val SQL_CREATE_ENTRIES =
            "CREATE TABLE " + DBContract.UserEntry.TABLE_NAME + " (" +
                    DBContract.UserEntry.COLUMN_ID + " INTEGER PRIMARY KEY," +
                    DBContract.UserEntry.COLUMN_FIRST_NAME + " TEXT," +
                    DBContract.UserEntry.COLUMN_LAST_NAME + " TEXT," +
                    DBContract.UserEntry.COLUMN_TELEPHONE + " TEXT," +
                    DBContract.UserEntry.COLUMN_PHONE + " TEXT," +
                    DBContract.UserEntry.COLUMN_ADDRESS + " TEXT," +
                    DBContract.UserEntry.COLUMN_NAME + " TEXT," +
                    DBContract.UserEntry.COLUMN_ALIAS + " TEXT," +
                    DBContract.UserEntry.COLUMN_STATUS + " INTEGER)"

        private const val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " +DBContract.UserEntry.TABLE_NAME
    }
}