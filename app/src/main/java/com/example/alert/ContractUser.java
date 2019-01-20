package com.example.alert;

import android.provider.BaseColumns;


public class ContractUser {
    public static class USER implements BaseColumns {
        public static final String TABLE_NAME = "alert_user_details";
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_EMAIL = "email";
        public static final String COLUMN_NAME_PASSWORD = "password";
        public static final String COLUMN_NAME_ADDRESS = "address";


    }
}
