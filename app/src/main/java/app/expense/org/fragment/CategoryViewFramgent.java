package app.expense.org.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import app.expense.org.expensemanagerapp.R;
import app.expense.org.utils.Constants;

/**
 * Created by user on 30/1/16.
 */
public class CategoryViewFramgent extends Fragment{

    ListView listView;
    ArrayAdapter<String> adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.framgment_viewcategory, null);

        listView = (ListView)view.findViewById(R.id.listViewCategory);
        adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, Constants.categories);
        listView.setAdapter(adapter);

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Snackbar.make(view, "on category selected listener.", Snackbar.LENGTH_LONG).show();
                return false;
            }
        });

        return view;
    }
}
