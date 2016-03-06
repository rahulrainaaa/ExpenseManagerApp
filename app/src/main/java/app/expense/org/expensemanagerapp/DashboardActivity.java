package app.expense.org.expensemanagerapp;

import android.app.ActionBar;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import app.expense.org.Model.Account;
import app.expense.org.fragment.AccountViewFragment;
import app.expense.org.fragment.CategoryViewFramgent;
import app.expense.org.fragment.ExpenseViewFragment;
import app.expense.org.fragment.ReminderViewFragment;
import app.expense.org.utils.Constants;

/**
 * DashboardActivity Class reponsible for handling the Dashboard UI.
 * Navigation Bar included for selection between Expense, Category, Account and Reminders.
 * Fragments corresponding to the selection in navigation bar is loaded in the main dashboard screen.
 */
public class DashboardActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    //UIControls declarations.
    FloatingActionButton fab;
    DrawerLayout drawer;
    ActionBarDrawerToggle toggle;
    NavigationView navigationView;

    //Global variables and flags
    int selectionFlag = 0;  //0=exp, 1= cat, 2=acc and 3=rem.
    SQLiteDatabase mydatabase = null;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ListView listView;

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //create a new expense or category or account or reminder depending on flag.
                if (selectionFlag == 0) {
                    //Add new expense.
                    startActivity(new Intent(DashboardActivity.this, ExpenseCreateActivity.class));
                    finish();
                } else if (selectionFlag == 1) {
                    //Add new categoty
                    //startActivity(new Intent(DashboardActivity.this, CategoryCreateActivity.class));
                    //finish();
                    createCategory();

                } else if (selectionFlag == 2) {
                    //Add new account
                    startActivity(new Intent(DashboardActivity.this, AccountCreateActivitiy.class));
                    finish();
                } else if (selectionFlag == 3) {
                    //Add new reminder
                    startActivity(new Intent(DashboardActivity.this, ReminderCreateActvity.class));
                    finish();
                } else {
                    Snackbar.make(null, R.string.errorSelectionFlagValue, Snackbar.LENGTH_SHORT).show();
                }


            }
        });

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ExpenseViewFragment fragment = new ExpenseViewFragment();
        FragmentManager fm = getFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        if (!fragmentTransaction.isEmpty())
        {
            fragmentTransaction.remove(fragment);
        }

        fragmentTransaction.replace(R.id.fragment_switch, fragment);
        fragmentTransaction.commit();

    }

    @Override
    public void onBackPressed() {

        if (drawer.isDrawerOpen(GravityCompat.START))
        {
            drawer.closeDrawer(GravityCompat.START);
        }
        else
        {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.dashboard, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_about) {
            Toast.makeText(getApplicationContext(), "About", Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_expense)
        {
            //View Expenses Fragment load
            selectionFlag = 0;

            ExpenseViewFragment fragment = new ExpenseViewFragment();
            FragmentManager fm = getFragmentManager();
            FragmentTransaction fragmentTransaction = fm.beginTransaction();
            if (!fragmentTransaction.isEmpty())
            {
                fragmentTransaction.remove(fragment);
            }

            fragmentTransaction.replace(R.id.fragment_switch, fragment);
            fragmentTransaction.commit();

        }
        else if (id == R.id.nav_category)
        {
            //View Categories fragment load
            selectionFlag = 1;

            CategoryViewFramgent fragment = new CategoryViewFramgent();
            FragmentManager fm = getFragmentManager();
            FragmentTransaction fragmentTransaction = fm.beginTransaction();
            if (!fragmentTransaction.isEmpty())
            {
                fragmentTransaction.remove(fragment);
            }

            fragmentTransaction.replace(R.id.fragment_switch, fragment);
            fragmentTransaction.commit();

        }
        else if (id == R.id.nav_account)
        {
            //View Accounts fragment load
            selectionFlag = 2;

            AccountViewFragment fragment = new AccountViewFragment();
            FragmentManager fm = getFragmentManager();
            FragmentTransaction fragmentTransaction = fm.beginTransaction();
            if (!fragmentTransaction.isEmpty())
            {
                fragmentTransaction.remove(fragment);
            }

            fragmentTransaction.replace(R.id.fragment_switch, fragment);
            fragmentTransaction.commit();

        }
        else if (id == R.id.nav_reminder)
        {
            //View Reminders fragment load
            selectionFlag = 3;

            ReminderViewFragment fragment = new ReminderViewFragment();
            FragmentManager fm = getFragmentManager();
            FragmentTransaction fragmentTransaction = fm.beginTransaction();
            if (!fragmentTransaction.isEmpty())
            {
                fragmentTransaction.remove(fragment);
            }

            fragmentTransaction.replace(R.id.fragment_switch, fragment);
            fragmentTransaction.commit();

        }
        else if (id == R.id.nav_category_filer)
        {
            //Filter by category.
            createListViewSelectionPopup(0);
        }
        else if (id == R.id.nav_account_filter)
        {
            //Filter by account.
            createListViewSelectionPopup(1);
        }
        else
        {
            //Unreachable block
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }



    private void createListViewSelectionPopup(final int filter)
    {
        ListView lw = new ListView(getApplicationContext());
        ArrayAdapter<String> adapter = null;

        if (filter == 0)
        {
            Constants.filterCategory = new ArrayList<String>();
            adapter = new ArrayAdapter<String>(DashboardActivity.this, android.R.layout.simple_list_item_checked, Constants.categories);
        }
        else if (filter == 1)
        {
            Constants.filterAccount = new ArrayList<String>();
            adapter = new ArrayAdapter<String>(DashboardActivity.this, android.R.layout.simple_list_item_checked, Constants.account);
        }
        lw.setAdapter(adapter);

        lw.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                CheckedTextView check = (CheckedTextView) view;
                TextView textItem = (TextView) view;
                String item = textItem.getText().toString();
                //Toast.makeText(getApplicationContext(), "Selected: " + item, Toast.LENGTH_LONG).show();
                //Check filter 0=category and 1=account
                if (filter == 0) {
                    if (check.isChecked()) {
                        check.setChecked(false);
                        //remove item from filterCategory.
                        for (int i = 0; i < Constants.filterCategory.size(); i++) {
                            if (Constants.filterCategory.get(i).trim().equalsIgnoreCase(item.trim())) {
                                Constants.filterCategory.remove(i);
                                break;
                            }
                        }
                    } else {
                        check.setChecked(true);
                        //add item to filterAccount.
                        Constants.filterCategory.add(item);

                    }
                } else if (filter == 1) {
                    if (check.isChecked()) {
                        check.setChecked(false);
                        //remove item from filterAccount.
                        for (int i = 0; i < Constants.filterAccount.size(); i++) {
                            if (Constants.filterAccount.get(i).trim().equalsIgnoreCase(item.trim())) {
                                Constants.filterAccount.remove(i);
                                break;
                            }
                        }
                    } else {
                        check.setChecked(true);
                        //add item to filterAccount.
                        Constants.filterAccount.add(item);
                    }
                }

                //check.setCheckMarkDrawable(android.R.drawable.checkbox_on_background);
            }
        });

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(DashboardActivity.this);
        alertDialogBuilder.setPositiveButton("OK", null);
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setIcon(android.R.drawable.ic_search_category_default);
        String filterType = (filter==0) ? "Category " : "Account ";
        alertDialogBuilder.setTitle(filterType + "filter");
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.setView(lw);
        alertDialog.show();
    }

    /**
     * @method: createCategory
     * @desc: Creates input AlertDialog to inout category string and then inserts in the SQLite DB.
     */
    private void createCategory()
    {
        //Create AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("New Category Name");
        builder.setCancelable(false);

        // Set up the EditText.
        final EditText input = new EditText(this);

        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        //Save the new Category added.
                        String newCategoryText = input.getText().toString();



                        try {

                            mydatabase = openOrCreateDatabase(Constants.dbname, MODE_PRIVATE, null);
                            mydatabase.execSQL("INSERT INTO category VALUES ('" + newCategoryText + "')");

                            Cursor categorySet = mydatabase.rawQuery("Select name from category", null);
                            Constants.categories = null;
                            Constants.categories = new ArrayList<String>();
                            while (categorySet.moveToNext())
                            {
                                Constants.categories.add(categorySet.getString(0));
                            }

                            Toast.makeText(getApplicationContext(), "Category Added Sucessfully.", Toast.LENGTH_SHORT).show();

                            //Reload View Categories fragment load.
                            selectionFlag = 1;
                            CategoryViewFramgent fragment = new CategoryViewFramgent();
                            FragmentManager fm = getFragmentManager();
                            FragmentTransaction fragmentTransaction = fm.beginTransaction();
                            if (!fragmentTransaction.isEmpty()) {
                                fragmentTransaction.remove(fragment);
                            }
                            fragmentTransaction.replace(R.id.fragment_switch, fragment);
                            fragmentTransaction.commit();

                        }
                        catch (Exception e)
                        {
                            Toast.makeText(getApplicationContext(), "Failed: Cannot add this category.", Toast.LENGTH_LONG).show();
                        }
                        finally {
                            if(mydatabase.isOpen())
                            {
                                mydatabase.close();
                            }
                        }

                    }
                }

        );

            builder.setNegativeButton("Discard", new DialogInterface.OnClickListener()

                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            dialog.cancel();
                        }
                    }

            );

            builder.show();
        }

    }
