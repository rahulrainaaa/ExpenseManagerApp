package app.expense.org.expensemanagerapp;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import java.util.ArrayList;

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
        mydatabase.execSQL("CREATE TABLE IF NOT EXISTS expense(id INTEGER PRIMARY KEY NOT NULL, spenton VARCHAR, price VARCHAR, datetime VARCHAR, account VARCHAR, category VARCHAR, image VARCHAR, indicator color);");
        mydatabase.execSQL("CREATE TABLE IF NOT EXISTS category(name VARCHAR);");
        mydatabase.execSQL("CREATE TABLE IF NOT EXISTS account(id INTEGER PRIMARY KEY NOT NULL, name VARCHAR, type VARCHAR, desc VARCHAR);");

        mydatabase.execSQL("Delete from category");
        mydatabase.execSQL("Delete from account");
        mydatabase.execSQL("INSERT INTO expense (spenton, price, datetime, account, category, image, indicator) VALUES ('Pizza','450','30 Jan 15 03:15 PM', 'Paytm', 'Food', '', 'red' )");

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
            mydatabase.execSQL("INSERT INTO account (name, type, desc) VALUES ('Saving Account','Saving Account','Saving Account transaction using debit card or online banking.')");
            mydatabase.execSQL("INSERT INTO account (name, type, desc) VALUES ('Credit Card','Loan Account','Credit card bank account for transaction.')");
            mydatabase.execSQL("INSERT INTO account (name, type, desc) VALUES ('Paytm','Paytm Account','Paytm Account for online transactions.')");

        }


        //Getting all categories from db and holding it in Constants.
        Cursor categorySet = mydatabase.rawQuery("Select name from category", null);
        Constants.categories = new ArrayList<String>();
        while (categorySet.moveToNext())
        {
            Constants.categories.add(categorySet.getString(0));
        }

        //Getting all accounts from db and holding it in Constants.
        Cursor accountSet = mydatabase.rawQuery("Select name from account", null);
        Constants.account = new ArrayList<String>();
        while (accountSet.moveToNext())
        {
            Constants.account.add(accountSet.getString(0));
        }

        //Setting few default colors
        Constants.indColor = new ArrayList<String>();
        Constants.indColor.add("Red");
        Constants.indColor.add("Blue");
        Constants.indColor.add("Green");
        Constants.indColor.add("Gray");
        Constants.indColor.add("Violet");
        Constants.indColor.add("Margenta");
        Constants.indColor.add("Orange");
        Constants.indColor.add("Yellow");
        Constants.indColor.add("Brown");
        Constants.indColor.add("Sky blue");

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
                //Toast.makeText(getApplicationContext(), "Timer", Toast.LENGTH_SHORT).show();
            }
        }, 1500);




    }
}