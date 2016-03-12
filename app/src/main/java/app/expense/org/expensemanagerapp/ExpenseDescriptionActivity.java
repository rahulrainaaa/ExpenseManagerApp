package app.expense.org.expensemanagerapp;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import app.expense.org.Model.Expense;
import app.expense.org.utils.Constants;

public class ExpenseDescriptionActivity extends AppCompatActivity {

    TextView textPrice, textTime, textCategory, textAccount;
    ImageView imgExpense;
    AppBarLayout appBarLayout;
    SQLiteDatabase mydatabase = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense_description);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        textPrice = (TextView)findViewById(R.id.textPrice);
        textTime = (TextView)findViewById(R.id.textTime);
        textCategory = (TextView)findViewById(R.id.textCategory);
        textAccount = (TextView)findViewById(R.id.textAccount);
        imgExpense = (ImageView)findViewById(R.id.imageExpense);
        appBarLayout = (AppBarLayout)findViewById(R.id.app_bar);

        final int position = getIntent().getIntExtra("expensePosition",0);
        final Expense expense = Constants.expenseData.get(position);
        getSupportActionBar().setTitle(expense.spenton);
        appBarLayout.setBackgroundColor(Color.parseColor("#" + expense.color.trim()));
        textPrice.setText("Rs." + expense.price);
        textTime.setText("" + expense.datetime);
        textAccount.setText("" + expense.account);
        textCategory.setText("" + expense.category);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Are you sure to delete?", Snackbar.LENGTH_LONG)
                        .setAction("Action", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        //Delete this selected enty.
                        mydatabase = openOrCreateDatabase(Constants.dbname, MODE_PRIVATE, null);
                        mydatabase.execSQL("Delete from expense where id = " + expense.id);

                        //Getting all expenses data from db and holding in model object.
                        Cursor expenseSet = mydatabase.rawQuery("Select * from expense", null);
                        Constants.expenseData = null;
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

                        mydatabase.close();
                        Toast.makeText(getApplicationContext(), "Expense Deleted.", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(ExpenseDescriptionActivity.this, DashboardActivity.class));
                        finish();

                    }
                }).show();
            }
        });
    }
}
