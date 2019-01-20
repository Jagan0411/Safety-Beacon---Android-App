package com.example.alert;


import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;


public class AddContact_Fragment extends DialogFragment {

    private EditText name;
    private EditText email;
    private EditText phone;

    private Button add;

    private final String TAG = "addcontactfragment";

    public interface OnDialogCloseListener {
        void closeDialog(String name,String phone,String email);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_contact, container, false);
        name = (EditText) view.findViewById(R.id.edit_name);
        phone = (EditText) view.findViewById(R.id.edit_phone);
        email = (EditText) view.findViewById(R.id.edit_email);

        add = (Button) view.findViewById(R.id.add);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OnDialogCloseListener activity = (OnDialogCloseListener) getActivity();
                activity.closeDialog(name.getText().toString(),phone.getText().toString(), email.getText().toString());
                AddContact_Fragment.this.dismiss();
            }
        });

        return view;
    }

}
