package com.example.alert;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
/**
 * Created by GloTech on 31-07-2017.
 */

public class ViewContactItems extends DialogFragment {
    private EditText name;
    private EditText phone;
    private EditText email;
    private DatePicker dp;
    private Button add;
    String selected;
    private final String TAG = "updatetodofragment";
    private long id;

    public ViewContactItems(){}

    public static ViewContactItems newInstance(String name, String phone ,String email, long id) {
        ViewContactItems f = new ViewContactItems();

        // Supply num input as an argument.
        Bundle args = new Bundle();

        args.putString("name", name);
        args.putString("phone", phone);
        args.putString("email", email);


        f.setArguments(args);

        return f;
    }

    //To have a way for the activity to get the data from the dialog
    public interface OnUpdateDialogCloseListener {
        void closeUpdateDialog( String name,String phone, String email,long id);//added category and action arguments along with other column values to pass on to main activity that executes the query to update item in database
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_contact, container, false);
        name = (EditText) view.findViewById(R.id.edit_name);
        phone = (EditText) view.findViewById(R.id.edit_phone);
        email = (EditText) view.findViewById(R.id.edit_email);
        add = (Button) view.findViewById(R.id.add);



        String namestr = getArguments().getString("name");
        String phonestr = getArguments().getString("phone");
        String emailstr = getArguments().getString("email");


        name.setText(namestr);
        phone.setText(phonestr);
        email.setText(emailstr);

        add.setText("OK");
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewContactItems.OnUpdateDialogCloseListener activity = (ViewContactItems.OnUpdateDialogCloseListener) getActivity();
                activity.closeUpdateDialog( name.getText().toString(), phone.getText().toString(),email.getText().toString(), id);
                ViewContactItems.this.dismiss();
            }
        });

        return view;
    }
}
