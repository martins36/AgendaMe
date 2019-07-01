package com.sujeto36.agendame

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.widget.ArrayAdapter

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var contactsDBHelper: ContactDBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        contactsDBHelper = ContactDBHelper(this)
        requestPermission()

        //cargar la lista
        val listItems: ArrayList<ContactModel> = contactsDBHelper.readAllContact()
        listItems.sortBy { it.name }
        val listNames: ArrayList<String> = ArrayList()
        for (i in 0 until listItems.size)
            listNames.add(listItems[i].name)
        val adapter = ArrayAdapter(this, android.R.layout.simple_expandable_list_item_1, listNames)
        list_view_contacts.adapter = adapter

        list_view_contacts.setOnItemClickListener { _, _, position, _ ->
            val selectedContact = listItems[position]
            val intent = ViewContact.newIntent(this, selectedContact)
            startActivity(intent)
            finish()
        }

        searchBar.addTextChangeListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {

            }

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                //SEARCH FILTER
                adapter.getFilter().filter(charSequence)
            }

            override fun afterTextChanged(editable: Editable) {

            }
        })

        fab.setOnClickListener {
            val intent = Intent(this, NewContact::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun requestPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            if (!ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.CALL_PHONE)) {
                ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.CALL_PHONE), 42)
            }
        }
    }

}
