package app.expense.org.fragment;

import android.app.Fragment;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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

import java.util.ArrayList;

import app.expense.org.Model.Account;
import app.expense.org.adapter.AccountViewAdapter;
import app.expense.org.expensemanagerapp.R;
import app.expense.org.utils.Constants;

/**
 * Created by user on 30/1/16.
 */
public class AccountViewFragment extends Fragment {

    ListView listView;
    AccountViewAdapter adapter;
    SQLiteDatabase mydatabase = null;

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
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                Snackbar.make(view, "Desription: " + Constants.accountData.get(position).desc, Snackbar.LENGTH_LONG)
                        .setAction("Delete ?", new View.OnClickListener() {

                            @Override
                            public void onClick(View v) {

                                //Delete selected Account from DB and clear arrayList.
                                mydatabase = getActivity().openOrCreateDatabase(Constants.dbname, getActivity().MODE_PRIVATE, null);
                                mydatabase.execSQL("Delete from account where id = " + Constants.accountData.get(position).id);
                                Constants.accountData = null;
                                Constants.account = null;
                                Constants.filterAccount = null;
                                Constants.filterAccount = new ArrayList<String>();
                                //Getting all accounts from db and holding it in Constants.
                                Cursor accountSet = mydatabase.rawQuery("Select * from account", null);
                                Constants.accountData = new ArrayList<Account>();
                                Constants.account = new ArrayList<String>();
                                while (accountSet.moveToNext())
                                {
                                    //Create the Account Model object and insert the data values in it.
                                    Account account = new Account();
                                    account.id = Integer.parseInt(accountSet.getString(0));
                                    account.name = accountSet.getString(1);
                                    account.accountType = accountSet.getString(2);
                                    account.desc = accountSet.getString(3);
                                    Constants.accountData.add(account);
                                    Constants.account.add("" + accountSet.getString(1));
                                }
                                mydatabase.close();

                                adapter = new AccountViewAdapter(getActivity(), Constants.accountData);
                                listView.setAdapter(adapter);

                                Toast.makeText(getActivity().getApplicationContext(), "Account deleted.", Toast.LENGTH_SHORT).show();
                            }
                        }).show();
            }
        });

        return view;
    }

}
