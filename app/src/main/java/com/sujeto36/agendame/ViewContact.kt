package com.sujeto36.agendame

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.PopupMenu
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_view_contact.*

class ViewContact : AppCompatActivity() {

    private lateinit var contactsDBHelper: ContactDBHelper
    private lateinit var contact: ContactModel
    private lateinit var textAddress: String
    private lateinit var textName: String
    private lateinit var textTelephone: String
    private lateinit var textPhone: String
    private var idContact: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_contact)
        contactsDBHelper = ContactDBHelper(this)

        var isPhone = false
        idContact = intent.extras.getInt(EXTRA_ID)
        textTelephone = intent.extras.getString(EXTRA_TELEPHONE)
        textPhone = intent.extras.getString(EXTRA_PHONE)
        textAddress = intent.extras.getString(EXTRA_ADDRESS)
        textName = intent.extras.getString(EXTRA_NAME)
        contact = ContactModel(
            idContact,
            intent.extras.getString(EXTRA_FIRST_NAME),
            intent.extras.getString(EXTRA_LAST_NAME),
            textTelephone,
            textPhone,
            textAddress,
            textName,
            intent.extras.getString(EXTRA_ALIAS),
            0
        )


        text_view_telephone.text = textTelephone
        text_view_name.text = textName
        if (textPhone == "") {
            layout_phone.visibility = View.GONE
            isPhone = true
        }
        else
            text_view_phone.text = textPhone

        if (textAddress == "")
            layout_address.visibility = View.GONE
        else
            text_view_address.text = textAddress

        button_call.setOnClickListener { view ->
            if (isPhone)
                call(textTelephone)
            else
                showPopup(view)
        }

        button_back.setOnClickListener { finishActivity() }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_contact, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_edit -> {
                val intent = EditContact.newIntent(this, contact)
                startActivity(intent)
                finish()
                true
            }
            R.id.action_delete -> {
                deleteContact()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showPopup(view: View) {
        val popup: PopupMenu?
        popup = PopupMenu(this, view)
        popup.inflate(R.menu.menu_call)

        popup.setOnMenuItemClickListener(
            PopupMenu.OnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.menu_telephone -> {
                    call(textTelephone)
                }
                R.id.menu_phone -> {
                    call(textPhone)
                }
            }
            true
        })

        popup.show()
    }

    private fun call(number: String) {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.CALL_PHONE)){
                //vuelve solicitar el permiso si la primera vez lo rechazo
                ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.CALL_PHONE), 42)
            } else {
                Toast.makeText(this, "Usted denegó los permisos para llamar", Toast.LENGTH_SHORT).show()

            }
        }
        else {
            val intent = Intent(Intent.ACTION_CALL, Uri.parse("tel:$number"))
            startActivity(intent)
        }
    }

    private fun deleteContact(){
        val builder = AlertDialog.Builder(this)
        with(builder)
        {
            setTitle("Eliminar Contacto")
            setMessage("¿Desea eliminar este contacto?")
            setPositiveButton("Sí", DialogInterface.OnClickListener { _, _ ->
                contactsDBHelper.deleteContact(idContact)
                Toast.makeText(this@ViewContact, "Contacto eliminado", Toast.LENGTH_SHORT).show()
                finishActivity()
            })
            setNegativeButton("Cancelar", null)
            show()
        }
    }

    private fun finishActivity() {
        val intent = Intent(this, MainActivity::class.java)
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
            val intent = Intent(context, ViewContact::class.java)

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
