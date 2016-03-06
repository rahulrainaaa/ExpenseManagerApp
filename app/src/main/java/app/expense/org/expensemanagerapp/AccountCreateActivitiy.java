package app.expense.org.expensemanagerapp;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

import app.expense.org.Model.Account;
import app.expense.org.utils.Constants;

public class AccountCreateActivitiy extends AppCompatActivity {

    EditText txtAccount, txtType, txtDesc;
    String accountName = "";
    String accountType = "";
    String accountDescription = "";

    SQLiteDatabase mydatabase = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_create_activitiy);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //UI mapping done.
        txtAccount = (EditText)findViewById(R.id.editTextAccount);
        txtType = (EditText)findViewById(R.id.editTextType);
        txtDesc = (EditText)findViewById(R.id.editTextDesc);

        //Float button for addition.
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                accountName = txtAccount.getText().toString().trim();
                accountType = txtType.getText().toString().trim();
                accountDescription = txtDesc.getText().toString().trim();

                if(validateData())
                {
                    try
                    {
                        mydatabase = openOrCreateDatabase(Constants.dbname, MODE_PRIVATE, null);
                        mydatabase.execSQL("INSERT INTO account (name, type, desc) VALUES ('" + accountName + "','" + accountType + "','" + accountDescription + "')");

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

                        Toast.makeText(getApplicationContext(), "Account Added.", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(AccountCreateActivitiy.this, DashboardActivity.class));
                        finish();
                    }
                    catch (Exception e)
                    {
                       Snackbar.make(view, "Error: Cannot save this data.\nNote: Only alphabets and numbers allowed.", Snackbar.LENGTH_LONG).show();
                    }
                    finally
                    {
                        if(mydatabase.isOpen())
                        {
                            mydatabase.close();
                        }
                    }
                }

            }
        });
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(getApplicationContext(), "Changes Discarded.", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(AccountCreateActivitiy.this, DashboardActivity.class));
        finish();
    }

    /**
     * @method: validateData
     * @desc: validates the input data and sets error message for text box..
     * @return: valid = true, invalid = false (boolean)
     */
    private boolean validateData()
    {

        if(accountName.isEmpty())
        {
            txtAccount.setError("Enter Account Name.");
            return false;
        }

        txtAccount.setError(null);
        return true;
    }
}
