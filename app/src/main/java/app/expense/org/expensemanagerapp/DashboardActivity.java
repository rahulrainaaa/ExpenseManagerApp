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
import android.util.Log;
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
import app.expense.org.Model.Expense;
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
                    Constants.appNavState = 0;
                    startActivity(new Intent(DashboardActivity.this, ExpenseCreateActivity.class));
                    finish();
                } else if (selectionFlag == 1) {
                    Constants.appNavState = 1;
                    createCategory();

                } else if (selectionFlag == 2) {
                    //Add new account
                    Constants.appNavState = 2;
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

        if(Constants.appNavState == 0)
        {
            loadExpenseFragment();
        }
        else if(Constants.appNavState == 1)
        {
            loadCategoryFragment();
        }
        else if (Constants.appNavState == 2)
        {
            loadAccountFragment();
        }
        else if (Constants.appNavState == 3)
        {
            loadReminderFragment();
        }

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
            loadExpenseFragment();
        }
        else if (id == R.id.nav_category)
        {
            loadCategoryFragment();
        }
        else if (id == R.id.nav_account)
        {
            loadAccountFragment();
        }
        else if (id == R.id.nav_reminder)
        {
            loadReminderFragment();
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

    /**
     * @method: createListViewSelectionPopup
     * @desc: creates dialog box for category and account filter selections.
     * @param filter
     */
    private void createListViewSelectionPopup(final int filter)
    {
        ListView lw = new ListView(getApplicationContext());
        ArrayAdapter<String> adapter = null;

        if (filter == 0)
        {
            Constants.filterCategory = null;
            Constants.filterCategory = new ArrayList<String>();
            adapter = new ArrayAdapter<String>(DashboardActivity.this, android.R.layout.simple_list_item_checked, Constants.categories);
        }
        else if (filter == 1)
        {
            Constants.filterAccount = null;
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
        alertDialogBuilder.setPositiveButton("Filter", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                mydatabase = openOrCreateDatabase(Constants.dbname, MODE_PRIVATE, null);

                //Getting all expenses data from db and holding in model object.
                String filterStringAccount = "";
                String filterStringCategory = "";
                String expenseFilterQueryString = "Select * from expense where ";
                boolean flagBoth = false;
                int i = 0;
                for (i = 0; i < Constants.filterAccount.size(); i++)
                {
                    if(i == 0)
                    {
                        filterStringAccount = "" + Constants.filterAccount.get(i).toString();
                    }
                    else
                    {
                        filterStringAccount = filterStringAccount + "," + Constants.filterAccount.get(i).toString();
                    }
                }
                String addAccountFilter = "";
                if(i > 0)
                {
                    addAccountFilter = "account IN (" + filterStringAccount + ") ";
                    expenseFilterQueryString = expenseFilterQueryString + "" + addAccountFilter;
                    flagBoth = true;
                }

                for (i = 0; i < Constants.filterCategory.size(); i++)
                {
                    if(i == 0)
                    {
                        filterStringCategory = "'" + Constants.filterCategory.get(i).toString() + "'";
                    }
                    else
                    {
                        filterStringCategory = filterStringCategory + ",'" + Constants.filterCategory.get(i).toString() + "'";
                    }
                }

                String addCategoryFilter = "";
                if(i > 0)
                {
                    addCategoryFilter = "category IN (" + filterStringCategory + ")";
                    if(flagBoth)
                    {
                        expenseFilterQueryString = expenseFilterQueryString + " AND " + addCategoryFilter;
                    }
                    else
                    {
                        expenseFilterQueryString = expenseFilterQueryString + " " + addCategoryFilter;
                    }
                }


                System.out.print(expenseFilterQueryString + "\n\n\n\n\n\n\n\n\n\n\n\n");
                Log.d("SQLite", expenseFilterQueryString + "\n\n\n\n\n\n\n\n\n\n\n\n");
                Log.e("SQLite", expenseFilterQueryString + "\n\n\n\n\n\n\n\n\n\n\n\n");
                Cursor expenseSet = mydatabase.rawQuery(expenseFilterQueryString, null);
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
            }
        });
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
                            while (categorySet.moveToNext()) {
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

                        } catch (Exception e) {
                            Toast.makeText(getApplicationContext(), "Failed: Cannot add this category.", Toast.LENGTH_LONG).show();
                        } finally {
                            if (mydatabase.isOpen()) {
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

    /**
     * @method: loadExpenseFragment
     * @desc: loads Expense Framment
     */
    public void loadExpenseFragment()
    {
        //View Expenses Fragment load
        selectionFlag = 0;
        Constants.appNavState = 0;
        getSupportActionBar().setTitle("Expense");
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

    /**
     * @method: loadCategoryFragment
     * @desc: loads Category Framment
     */
    public void loadCategoryFragment()
    {
        //View Categories fragment load
        getSupportActionBar().setTitle("Category");
        selectionFlag = 1;
        Constants.appNavState = 1;
        Constants.appNavState = 1;
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

    /**
     * @method: loadAccountFragment
     * @desc: loads Account Framment
     */
    public void loadAccountFragment()
    {
        //View Accounts fragment load
        getSupportActionBar().setTitle("Account");
        selectionFlag = 2;
        Constants.appNavState = 2;
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

    /**
     *@method: loadReminderFragment
     *@desc: loads Reminder Fragment
     */
    public void loadReminderFragment()
    {
        //View Reminders fragment load
        getSupportActionBar().setTitle("Reminder");
        selectionFlag = 3;
        Constants.appNavState = 3;
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

}
