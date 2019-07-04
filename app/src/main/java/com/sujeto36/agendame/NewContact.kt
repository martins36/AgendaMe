package com.sujeto36.agendame

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.wajahatkarim3.easyvalidation.core.view_ktx.nonEmpty
import kotlinx.android.synthetic.main.activity_new_contact.*

class NewContact : AppCompatActivity() {

    private lateinit var contactDBHelper : ContactDBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_contact)
        contactDBHelper = ContactDBHelper(this)

        button_save.setOnClickListener {
            text_input_first_name.nonEmpty() {
                text_input_first_name.error = "Campo requerido"
            }
            text_input_telephone.nonEmpty() {
                text_input_telephone.error = "Campo requerido"
            }

            if (text_input_first_name.nonEmpty() && text_input_telephone.nonEmpty()) {

                val contact = ContactModel(
                    text_input_first_name.text.toString(),
                    text_input_last_name.text.toString(),
                    text_input_telephone.text.toString(),
                    text_input_phone.text.toString(),
                    text_input_address.text.toString(),
                    text_input_alias.text.toString())

                contactDBHelper.insertContact(contact)
                Toast.makeText(this, "Contacto guardado", Toast.LENGTH_SHORT).show()
                finishActivity()
            }
        }
        button_cancel.setOnClickListener { finishActivity() }
    }

    private fun finishActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun onBackPressed() {
        finishActivity()
    }

}
