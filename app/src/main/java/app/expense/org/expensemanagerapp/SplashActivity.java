package app.expense.org.expensemanagerapp;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;

import app.expense.org.Model.Account;
import app.expense.org.Model.Expense;
import app.expense.org.utils.Constants;

/*
* SplashActivity Class handling the splash screen UI.
* */
public class SplashActivity extends AppCompatActivity {

    //Global Data members
    SQLiteDatabase mydatabase = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        //Open database 'ema' in private mode.
        mydatabase = openOrCreateDatabase(Constants.dbname, MODE_PRIVATE, null);

        //create following tables in database 'ema'
        mydatabase.execSQL("CREATE TABLE IF NOT EXISTS expense(id INTEGER PRIMARY KEY NOT NULL, spenton VARCHAR, price VARCHAR, datetime VARCHAR, account VARCHAR, category VARCHAR, image VARCHAR, indicator VARCHAR);");
        mydatabase.execSQL("CREATE TABLE IF NOT EXISTS category(name VARCHAR);");
        mydatabase.execSQL("CREATE TABLE IF NOT EXISTS account(id INTEGER PRIMARY KEY NOT NULL, name VARCHAR, type VARCHAR, desc VARCHAR);");

        mydatabase.execSQL("Delete from category where name = ''");
        mydatabase.execSQL("Delete from account where name = ''");
        //mydatabase.execSQL("Delete from expense");

        mydatabase.execSQL("INSERT INTO expense (spenton, price, datetime, account, category, image, indicator) VALUES ('Pizza','450','30 Jan 15 03:15 PM', 'Paytm', 'Food', '', '343434' )");

        //Check if there is any entry in Category table.
        Cursor resultSet = mydatabase.rawQuery("Select * from category",null);
        int total = resultSet.getCount();
        //Category table is empty. Enter some three categories.
        if (total == 0)
        {
            //Three app's default entry for category.
            mydatabase.execSQL("INSERT INTO category VALUES ('Food')");
            mydatabase.execSQL("INSERT INTO category VALUES ('Transport')");
            mydatabase.execSQL("INSERT INTO category VALUES ('Grocery')");

            //Three app's default entry for account.
            mydatabase.execSQL("INSERT INTO account (name, type, desc) VALUES ('Saving Account','Savings','Saving Account transaction using debit card or online banking.')");
            mydatabase.execSQL("INSERT INTO account (name, type, desc) VALUES ('Credit Card','Loan Account','Credit card bank account for transaction.')");
            mydatabase.execSQL("INSERT INTO account (name, type, desc) VALUES ('Paytm','Paytm Account','Paytm Account for online transactions.')");

        }

        //Getting all expenses data from db and holding in model object.
        Cursor expenseSet = mydatabase.rawQuery("Select * from expense", null);
        Constants.expenseData = new ArrayList<Expense>();
        while(expenseSet.moveToNext())
        {
        // (id, spenton, price, datetime, account, category, image, indicator)
            Expense expense = new Expense();
            expense.id = Integer.parseInt(expenseSet.getString(0).toString());
            expense.spenton = expenseSet.getString(1).toString();
            expense.price = expenseSet.getString(2).toString();
            expense.datetime = expenseSet.getString(3).toString();
            expense.account = expenseSet.getString(4).toString();
            expense.category = expenseSet.getString(5).toString();
            expense.image = expenseSet.getString(6).toString();
            expense.color = expenseSet.getString(7).toString();

            Constants.expenseData.add(expense);
        }

        //Getting all categories from db and holding it in Constants.
        Cursor categorySet = mydatabase.rawQuery("Select name from category", null);
        Constants.categories = new ArrayList<String>();
        while (categorySet.moveToNext())
        {
            Constants.categories.add(categorySet.getString(0));
        }

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

        //Handler for holdind few seconds at splash screen
        Handler handler = new Handler();

        handler.postDelayed(new Runnable() {

            @Override
            public void run() {

                mydatabase.close();
                Constants.filterAccount = new ArrayList<String>();
                Constants.filterCategory = new ArrayList<String>();
                startActivity(new Intent(SplashActivity.this, DashboardActivity.class));
                finish();

            }
        }, 1300);

    }

    @Override
    public void onBackPressed() {
        //Disable the back button for splash screen.

        finish();
    }
}