package com.sujeto36.agendame

class ContactModel (
    val contactId: Int,
    val firstName: String,
    val lastName: String,
    val telephone: String,
    val phone: String,
    val address: String,
    val name: String,
    val alias: String,
    val status: Int) {

    constructor(firstName: String, lastName: String, telephone: String, phone: String, address: String, alias: String) :
            this(0, firstName, lastName, telephone, phone, address, "",alias, 0)
}