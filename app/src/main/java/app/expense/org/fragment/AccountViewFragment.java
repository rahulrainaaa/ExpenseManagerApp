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
import android.widget.Toast;

import app.expense.org.adapter.AccountViewAdapter;
import app.expense.org.expensemanagerapp.R;
import app.expense.org.utils.Constants;

/**
 * Created by user on 30/1/16.
 */
public class AccountViewFragment extends Fragment {

    ListView listView;
    AccountViewAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.framgment_viewaccount, null);

        listView = (ListView)view.findViewById(R.id.listViewAccount);
        adapter = new AccountViewAdapter(getActivity(), Constants.accountData);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Snackbar.make(view, "Desription: " + Constants.accountData.get(position).desc, Snackbar.LENGTH_LONG)
                        .setAction("Delete?", new View.OnClickListener() {

                            @Override
                            public void onClick(View v) {

                                Toast.makeText(getActivity().getApplicationContext(), "To be deleted.", Toast.LENGTH_SHORT).show();
                            }
                        }).show();
            }
        });

        return view;
    }

}
