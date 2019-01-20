package com.example.alert;

/**
 * Created by GloTech on 31-07-2017.
 */

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

/**
 * Created by harish on 24-07-2017.
 */

public class ViewContact_Adapter extends RecyclerView.Adapter<ViewContact_Adapter.ItemHolder> {

    private Cursor cursor;
    private ItemClickListener listener;
    private String TAG = "vc_adapter";

    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.contact_items, parent, false);
        ItemHolder holder = new ItemHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ItemHolder holder, int position) {
        holder.bind(holder, position);
    }

    @Override
    public int getItemCount() {
        return cursor.getCount();
    }

    public interface ItemClickListener {
        void onItemClick(int pos, String name,String phone, String email, long id);
        void onDelete(int pos,long id);
    }

    public ViewContact_Adapter(Cursor cursor, ItemClickListener listener) {
        this.cursor = cursor;
        this.listener = listener;
    }

    public void swapCursor(Cursor newCursor){
        if (cursor != null) cursor.close();
        cursor = newCursor;
        if (newCursor != null) {
            // Force the RecyclerView to refresh
            this.notifyDataSetChanged();
        }
    }

    class ItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView name;
        String namestr;
        String phone;
        String email;
        Button delete;
        long id;


        ItemHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.name);
            delete = (Button) view.findViewById(R.id.delete);//getting delete button id from layout
            view.setOnClickListener(this);
        }

        public void bind(ItemHolder holder, int pos) {
            cursor.moveToPosition(pos);
            id = cursor.getLong(cursor.getColumnIndex(ContractContact.CONTACTS._ID));
            Log.d(TAG, "deleting id: " + id);
            namestr = cursor.getString(cursor.getColumnIndex(ContractContact.CONTACTS.COLUMN_NAME_NAME));
            phone = cursor.getString(cursor.getColumnIndex(ContractContact.CONTACTS.COLUMN_NAME_PHONE));
            email = cursor.getString(cursor.getColumnIndex(ContractContact.CONTACTS.COLUMN_NAME_EMAIL));
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) { // delete action listener that passes id to mainactivity to execute delete item query
                    int pos = getAdapterPosition();
                    listener.onDelete(pos, id);// onDelete is the method we need to implement in main activity
                }
            });


            name.setText(namestr);


            holder.itemView.setTag(id);
        }

        @Override
        public void onClick(View v) {
            int pos = getAdapterPosition();
            listener.onItemClick(pos, namestr,phone,email, id);

        }
    }

}
