package com.example.alert;

/**
 * Created by GloTech on 31-07-2017.
 */
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

public class ViewContact extends AppCompatActivity implements  AddContact_Fragment.OnDialogCloseListener, ViewContactItems.OnUpdateDialogCloseListener {


    FloatingActionButton  button;
    RecyclerView rv;
     DBhelperContact helper;
     Cursor cursor;
     SQLiteDatabase db;
    ViewContact_Adapter adapter;
    //User sign up page details
    String user_name,user_password,user_email,user_address;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_contact);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Getting intent from MainActivity
        Intent main_act_intent = getIntent();
        user_name = main_act_intent.getStringExtra("user_name");
        user_email = main_act_intent.getStringExtra("user_email");
        user_address = main_act_intent.getStringExtra("user_address");

        button = (FloatingActionButton) findViewById(R.id.addContacts);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getSupportFragmentManager();
                AddContact_Fragment frag = new AddContact_Fragment();
                frag.show(fm, "addcontactfragment");
            }
        });


        rv = (RecyclerView) findViewById(R.id.rv);
        rv.setLayoutManager(new LinearLayoutManager(this));


    }
    @Override
    protected void onStop() {
        super.onStop();
        if (db != null) db.close();
        if (cursor != null) cursor.close();
    }

    @Override
    protected void onStart() {
        super.onStart();
        helper = new DBhelperContact(this);
        db = helper.getWritableDatabase();
        cursor = getAllContact(db);
        execute(cursor);

    }
    @Override
    public void closeDialog(String name, String phone, String email) {
        addContact(db, name, phone,email);
        cursor = getAllContact(db);
        adapter.swapCursor(cursor);
    }
    protected void execute(Cursor cursor){
        adapter = new ViewContact_Adapter(cursor, new ViewContact_Adapter.ItemClickListener() {

            @Override
            public void onItemClick(int pos, String name, String phone,String email, long id) {
                 FragmentManager fm = getSupportFragmentManager();
                ViewContactItems frag = ViewContactItems.newInstance(name,phone,email, id);
                frag.show(fm, "updatecontactfragment");
            }


            @Override
            public void onDelete(int pos,long id) {
                removeContact(db,id);
                adapter.swapCursor(getAllContact(db));
            }

        });



        rv.setAdapter(adapter);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                long id = (long) viewHolder.itemView.getTag();
                removeContact(db, id);
                adapter.swapCursor(getAllContact(db));
            }

        }).attachToRecyclerView(rv);
    }
    public static Cursor getAllContact(SQLiteDatabase db) {
        Cursor c = db.rawQuery("SELECT * FROM "+ContractContact.CONTACTS.TABLE_NAME+"",null);
        return c;
    }


    private long addContact(SQLiteDatabase db, String name, String phone, String email) {
        ContentValues cv = new ContentValues();
        cv.put(ContractContact.CONTACTS.COLUMN_NAME_NAME, name);
        cv.put(ContractContact.CONTACTS.COLUMN_NAME_EMAIL, email);
        cv.put(ContractContact.CONTACTS.COLUMN_NAME_PHONE,phone);
        return db.insert(ContractContact.CONTACTS.TABLE_NAME, null, cv);
    }
    private int updateContact(SQLiteDatabase db, String name, String phone, String email, long id){

        ContentValues cv = new ContentValues();
        cv.put(ContractContact.CONTACTS.COLUMN_NAME_NAME, name);
        cv.put(ContractContact.CONTACTS.COLUMN_NAME_EMAIL, email);
        cv.put(ContractContact.CONTACTS.COLUMN_NAME_PHONE,phone);

        return db.update(ContractContact.CONTACTS.TABLE_NAME, cv, ContractContact.CONTACTS._ID + "=" + id, null);
    }
    private boolean removeContact(SQLiteDatabase db, long id) {
        return db.delete(ContractContact.CONTACTS.TABLE_NAME, ContractContact.CONTACTS._ID + "=" + id, null) > 0;
    }

    @Override
    public void closeUpdateDialog( String name,String phone, String email,long id) {
        updateContact(db, name,phone,email, id);
        adapter.swapCursor(getAllContact(db));
    }

}
