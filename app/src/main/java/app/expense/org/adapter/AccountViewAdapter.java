package app.expense.org.adapter;

import android.app.Activity;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import app.expense.org.expensemanagerapp.R;


/**
 * Created by user on 13/1/16.
 */
public class AccountViewAdapter extends ArrayAdapter<String> {

    private Activity activity;
    private LayoutInflater inflater;
    private ArrayList<String> list;

    public AccountViewAdapter(Activity activity, ArrayList<String> list) {
        super(activity, R.layout.listviewitem_account_view, list);

        this.activity = activity;
        this.inflater = activity.getLayoutInflater();
        this.list = list;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = inflater.inflate(R.layout.listviewitem_account_view, null);

        TextView accountName = (TextView)view.findViewById(R.id.account_name);
        TextView accountDesc = (TextView)view.findViewById(R.id.account_desc);

        String str = list.get(position).toString();

        accountName.setText("" + str);
        accountDesc.setText("" + str);





        return view;
    }
}
