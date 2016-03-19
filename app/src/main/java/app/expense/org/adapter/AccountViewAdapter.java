package app.expense.org.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import app.expense.org.Model.Account;
import app.expense.org.expensemanagerapp.R;


/**
 * Created by user on 13/1/16.
 */
public class AccountViewAdapter extends ArrayAdapter<Account> {

    private Activity activity;
    private LayoutInflater inflater;
    private ArrayList<Account> list;

    public AccountViewAdapter(Activity activity, ArrayList<Account> list) {
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

        accountName.setText("" + list.get(position).name.toString());
        accountDesc.setText("" + list.get(position).accountType.toString());

        return view;
    }
}
