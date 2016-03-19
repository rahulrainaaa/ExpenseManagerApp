package app.expense.org.adapter;

import android.app.Activity;
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
public class CategoryViewAdapter extends ArrayAdapter<String> {

    private Activity activity;
    private LayoutInflater inflater;
    private ArrayList<String> list;

    public CategoryViewAdapter(Activity activity, ArrayList<String> list) {
        super(activity, R.layout.listviewitem_expense_view, list);

        this.activity = activity;
        this.inflater = activity.getLayoutInflater();
        this.list = list;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = inflater.inflate(R.layout.listviewitem_expense_view, null);

        return view;
    }
}
