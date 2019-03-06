package instashare.instashare;

import android.graphics.drawable.Icon;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ContactListAdapter extends RecyclerView.Adapter<ContactListAdapter.MyViewHolder> {


    private Contact[] contactlist;

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public View contactView;
        public MyViewHolder(View v) {

            super(v);
            contactView = v;
        }
    }

    public ContactListAdapter(Contact[] contactlist)
    {
        this.contactlist = contactlist;
    }

    @Override
    public int getItemCount() {
        return contactlist.length;
    }




    @Override
    public ContactListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.contact_list_layout, parent, false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;

    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final int tempposition = position;
        TextView username = (TextView)holder.contactView.findViewById(R.id.nameBox);
        TextView userphone = (TextView)holder.contactView.findViewById(R.id.phoneBox);
        ImageView userphot = (ImageView)holder.contactView.findViewById(R.id.contactImage);
        username.setText(contactlist[position].name);
        userphone.setText(contactlist[position].number);
        if(contactlist[position].image != null) {
            userphot.setImageBitmap(contactlist[position].image);
        }
        else
        {
            userphot.setImageResource(R.drawable.contact_default);
        }
    }


}
