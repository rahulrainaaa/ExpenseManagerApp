package app.expense.org.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import app.expense.org.adapter.ExpenseViewAdapter;
import app.expense.org.expensemanagerapp.ExpenseDescriptionActivity;
import app.expense.org.expensemanagerapp.R;

/**
 * Created by user on 30/1/16.
 */
public class ExpenseViewFragment extends Fragment implements AdapterView.OnItemClickListener {

    ListView lw;
    ArrayList<String> list;
    ExpenseViewAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.framgment_viewexpense, null);

        lw = (ListView)view.findViewById(R.id.listViewExpense);
        list = new ArrayList<String>();
        for(int i = 0; i < 30; i++)
        {
            list.add("item " + i);
        }
        adapter = new ExpenseViewAdapter(getActivity(), list);
        lw.setAdapter(adapter);
        lw.setOnItemClickListener(this);

        return view;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        startActivity(new Intent(getActivity(), ExpenseDescriptionActivity.class));
        //Toast.makeText(getActivity().getApplicationContext(), "item selected", Toast.LENGTH_SHORT).show();
    }
}
