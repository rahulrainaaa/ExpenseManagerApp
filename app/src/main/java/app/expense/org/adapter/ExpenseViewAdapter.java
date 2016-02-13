package app.expense.org.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
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
public class ExpenseViewAdapter extends ArrayAdapter<String> {

    private Activity activity;
    private LayoutInflater inflater;
    private ArrayList<String> list;

    public ExpenseViewAdapter(Activity activity, ArrayList<String> list) {
        super(activity, R.layout.listviewitem_expense_view, list);

        this.activity = activity;
        this.inflater = activity.getLayoutInflater();
        this.list = list;

    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = inflater.inflate(R.layout.listviewitem_expense_view, null);
        TextView textPrice = (TextView)view.findViewById(R.id.textViewPrice);
        TextView textDateTime = (TextView)view.findViewById(R.id.textViewTime);
        TextView textAccount = (TextView)view.findViewById(R.id.textViewAccount);
        ImageView sideColor = (ImageView)view.findViewById(R.id.imageViewSideBar);

        String str = list.get(position).toString();

        if(position % 5 == 0)
        {
            sideColor.setBackgroundColor(Color.GREEN);
        }
        else if (position % 5 == 1)
        {
            sideColor.setBackgroundColor(Color.RED);
        }
        else if (position % 5 == 2)
        {
            sideColor.setBackgroundColor(Color.GRAY);
        }
        else if (position % 5 == 3)
        {
            sideColor.setBackgroundColor(Color.BLUE);
        }
        else if (position % 5 == 4)
        {
            sideColor.setBackgroundColor(Color.YELLOW);
        }



        textPrice.setText("Rs." + str);
        textDateTime.setText("12 Jan 2015, 11:10AM");
        textAccount.setText("Account Name" + str);

        return view;
    }
}
