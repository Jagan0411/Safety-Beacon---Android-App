package com.example.alert;

import android.provider.BaseColumns;

public class ContractContact {
    public static class CONTACTS implements BaseColumns {
        public static final String TABLE_NAME = "alert_contact";
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_PHONE = "phone";
        public static final String COLUMN_NAME_EMAIL = "email";

    }
}
