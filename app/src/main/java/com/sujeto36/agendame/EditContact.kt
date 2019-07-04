package com.sujeto36.agendame

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_new_contact.*

class EditContact : AppCompatActivity() {

    private lateinit var contactDBHelper : ContactDBHelper
    private lateinit var contact: ContactModel
    private lateinit var newContact: ContactModel
    private lateinit var textFirstName: String
    private lateinit var textLastName: String
    private lateinit var textAddress: String
    private lateinit var textTelephone: String
    private lateinit var textPhone: String
    private lateinit var textAlias: String
    private var idContact: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_contact)
        title_new_contact.setText(R.string.title_activity_edit_contact)
        contactDBHelper = ContactDBHelper(this)

        idContact = intent.extras.getInt(EXTRA_ID)
        textFirstName = intent.extras.getString(EXTRA_FIRST_NAME)
        textLastName = intent.extras.getString(EXTRA_LAST_NAME)
        textTelephone = intent.extras.getString(EXTRA_TELEPHONE)
        textPhone = intent.extras.getString(EXTRA_PHONE)
        textAddress = intent.extras.getString(EXTRA_ADDRESS)
        textAlias = intent.extras.getString(EXTRA_ALIAS)
        contact = ContactModel(
            idContact,
            textFirstName,
            textLastName,
            textTelephone,
            textPhone,
            textAddress,
            intent.extras.getString(EXTRA_NAME),
            textAlias,
            0
        )

        text_input_first_name.setText(textFirstName)
        text_input_last_name.setText(textLastName)
        text_input_telephone.setText(textTelephone)
        text_input_phone.setText(textPhone)
        text_input_address.setText(textAddress)
        text_input_alias.setText(textAlias)


        button_save.setOnClickListener {

            val alias = text_input_alias.text.toString()
            val name: String
            if (alias == "" && textLastName == "")
                name = text_input_first_name.text.toString()
            else if (alias == "")
                name = text_input_first_name.text.toString() + " " + text_input_last_name.text.toString()
            else
                name = text_input_first_name.text.toString() + " " + text_input_last_name.text.toString() + " ($alias)"

            newContact = ContactModel(
                idContact,
                text_input_first_name.text.toString(),
                text_input_last_name.text.toString(),
                text_input_telephone.text.toString(),
                text_input_phone.text.toString(),
                text_input_address.text.toString(),
                name,
                alias,
                0)

            contactDBHelper.updateContact(newContact)
            Toast.makeText(this, "Contacto actualizado", Toast.LENGTH_SHORT).show()
            val intent = ViewContact.newIntent(this, newContact)
            startActivity(intent)
            finish()
        }

        button_cancel.setOnClickListener { finishActivity() }
    }

    private fun finishActivity() {
        val intent = ViewContact.newIntent(this, contact)
        startActivity(intent)
        finish()
    }

    override fun onBackPressed() {
        finishActivity()
    }

    companion object {
        private const val EXTRA_ID = "id"
        private const val EXTRA_FIRST_NAME = "firstName"
        private const val EXTRA_LAST_NAME = "lastName"
        private const val EXTRA_TELEPHONE = "telephone"
        private const val EXTRA_PHONE = "phone"
        private const val EXTRA_ADDRESS = "address"
        private const val EXTRA_NAME = "name"
        private const val EXTRA_ALIAS = "alias"

        fun newIntent(context: Context, contactModel: ContactModel): Intent {
            val intent = Intent(context, EditContact::class.java)

            intent.putExtra(EXTRA_ID, contactModel.contactId)
            intent.putExtra(EXTRA_FIRST_NAME, contactModel.firstName)
            intent.putExtra(EXTRA_LAST_NAME, contactModel.lastName)
            intent.putExtra(EXTRA_TELEPHONE, contactModel.telephone)
            intent.putExtra(EXTRA_PHONE, contactModel.phone)
            intent.putExtra(EXTRA_ADDRESS, contactModel.address)
            intent.putExtra(EXTRA_NAME, contactModel.name)
            intent.putExtra(EXTRA_ALIAS, contactModel.alias)

            return intent
        }
    }
}
