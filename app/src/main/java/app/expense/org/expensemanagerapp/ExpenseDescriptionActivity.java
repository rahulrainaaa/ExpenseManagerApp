package app.expense.org.expensemanagerapp;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
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
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        textPrice = findViewById(R.id.textPrice);
        textTime = findViewById(R.id.textTime);
        textCategory = findViewById(R.id.textCategory);
        textAccount = findViewById(R.id.textAccount);
        imgExpense = findViewById(R.id.imageExpense);
        appBarLayout = findViewById(R.id.app_bar);

        final int position = getIntent().getIntExtra("expensePosition",0);
        final Expense expense = Constants.expenseData.get(position);
        getSupportActionBar().setTitle(expense.spenton);
        appBarLayout.setBackgroundColor(Color.parseColor("#" + expense.color.trim()));
        textPrice.setText("Rs." + expense.price);
        textTime.setText("" + expense.datetime);
        textAccount.setText("" + expense.account);
        textCategory.setText("" + expense.category);
        String filePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/expmgr/" + expense.image;
        File file = new File(filePath);
        if ((file.exists()) && (!expense.image.trim().isEmpty()))
        {
            Bitmap myBitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
            imgExpense.setImageBitmap(myBitmap);
        }

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Are you sure to delete?", Snackbar.LENGTH_LONG)
                        .setAction("Delete ?", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        //Delete this selected enty.
                        mydatabase = openOrCreateDatabase(Constants.dbname, MODE_PRIVATE, null);
                        mydatabase.execSQL("Delete from expense where id = " + expense.id);
                        String filename = "" + expense.image;
                        //Getting all expenses data from db and holding in model object.
                        Cursor expenseSet = mydatabase.rawQuery(Constants.selectExpense, null);
                        Constants.expenseData = null;
                        Constants.expenseData = new ArrayList<Expense>();
                        while(expenseSet.moveToNext())
                        {
                            // (id, spenton, price, datetime, account, category, image, indicator)
                            Expense expense = new Expense();
                            expense.id = Integer.parseInt(expenseSet.getString(0));
                            expense.spenton = expenseSet.getString(1);
                            expense.price = expenseSet.getString(2);
                            expense.datetime = expenseSet.getString(3);
                            expense.account = expenseSet.getString(4);
                            expense.category = expenseSet.getString(5);
                            expense.image = expenseSet.getString(6);
                            expense.color = expenseSet.getString(7);

                            Constants.expenseData.add(expense);
                        }

                        mydatabase.close();
                        if(!filename.trim().isEmpty())
                        {
                            File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/expmgr/" + filename);
                            if(file.exists())
                            {
                                file.delete();
                            }
                        }

                        Toast.makeText(getApplicationContext(), "Expense Deleted.", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(ExpenseDescriptionActivity.this, DashboardActivity.class));
                        finish();

                    }
                }).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(ExpenseDescriptionActivity.this, DashboardActivity.class));
        super.onBackPressed();
    }
}
