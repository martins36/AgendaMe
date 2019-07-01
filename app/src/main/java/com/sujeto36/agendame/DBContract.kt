package com.sujeto36.agendame

import android.provider.BaseColumns

object DBContract {

    class UserEntry : BaseColumns {
        companion object {
            const val TABLE_NAME = "CONTACT"
            const val COLUMN_ID = "id"
            const val COLUMN_FIRST_NAME = "first_name"
            const val COLUMN_LAST_NAME = "last_name"
            const val COLUMN_TELEPHONE = "telephone"
            const val COLUMN_PHONE = "phone"
            const val COLUMN_ADDRESS = "address"
            const val COLUMN_NAME = "name"
            const val COLUMN_STATUS = "status"
        }
    }
}