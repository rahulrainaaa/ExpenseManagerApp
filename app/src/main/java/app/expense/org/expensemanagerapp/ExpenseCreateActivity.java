package app.expense.org.expensemanagerapp;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import app.expense.org.Model.Expense;
import app.expense.org.utils.Constants;

public class ExpenseCreateActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, SeekBar.OnSeekBarChangeListener {

    /**
     * Constants and Global variables.
     */
    SQLiteDatabase mydatabase = null;
    private int TAKE_PHOTO_CODE = 0;
    private String fileName = "";
    private String filePath = "";
    private boolean flagColorSet = false;
    private int red = 0;
    private int green = 0;
    private int blue = 0;

    /**
     * UI components & data Array declarations.
     */
    Toolbar toolbar;
    ImageView colorCanvas;
    EditText txtSpenton, txtPrice;
    DatePicker datepicker;
    TimePicker timePicker;
    Spinner spinnerAccount, spinnerCategory;
    ArrayAdapter<String> accountAdapter, categoryAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense_create);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        txtSpenton = findViewById(R.id.txtspenton);
        txtPrice = findViewById(R.id.txtprice);
        datepicker = findViewById(R.id.datepicker_expense);
        timePicker = findViewById(R.id.timepicker_expense);

        spinnerAccount = findViewById(R.id.spinner_account);
        spinnerCategory = findViewById(R.id.spinner_category);

        accountAdapter = new ArrayAdapter<String>(ExpenseCreateActivity.this, android.R.layout.simple_spinner_dropdown_item, Constants.account);
        categoryAdapter = new ArrayAdapter<String>(ExpenseCreateActivity.this, android.R.layout.simple_spinner_dropdown_item, Constants.categories);

        spinnerAccount.setAdapter(accountAdapter);
        spinnerCategory.setAdapter(categoryAdapter);

        spinnerAccount.setOnItemSelectedListener(this);
        spinnerCategory.setOnItemSelectedListener(this);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeypad();

                //Generate image file name.
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
                Date date = new Date();
                fileName = dateFormat.format(date) + ".jpg";      //filename = yyyy-MM-dd-HH-mm-ss.jpg

                //Create image dir if not present.
                String dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/expmgr/";
                File newdir = new File(dir);
                newdir.mkdirs();

                //Camera image file creation.
                filePath = dir + fileName + "";
                Log.d("Image file",filePath);
                File newfile = new File(filePath);
                try
                {
                    newfile.createNewFile();

                    //Open camera to take picture.
                    Uri outputFileUri = Uri.fromFile(newfile);
                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
                    startActivityForResult(cameraIntent, TAKE_PHOTO_CODE);
                }
                catch (IOException e)
                {
                    Snackbar.make(view, "Error: Cannot capture picture.", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                }
            }
        });
        hideKeypad();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        hideKeypad();
        return super.onTouchEvent(event);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.expense_create, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_save_expense)
        {
            if(validateExpenseDate())
            {
                txtSpenton.setError(null);
                txtPrice.setError(null);
                if(flagColorSet == false)
                {
                    getExpenseColor();
                }
                else
                {
                    saveExpenseData();
                }
            }
            else
            {
                Snackbar.make(toolbar, "Please check for valid data entered in data fields.", Snackbar.LENGTH_LONG).show();
            }

            return true;
        }
        else if(id == R.id.action_get_color)
        {
            getExpenseColor();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {

        hideKeypad();
        startActivity(new Intent(ExpenseCreateActivity.this, DashboardActivity.class));
        Toast.makeText(getApplicationContext(), "Changes Discarded.", Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        //Toast.makeText(getApplicationContext(), "on item selected.", Toast.LENGTH_SHORT).show();

        hideKeypad();

        switch (view.getId())
        {
            case R.id.spinner_account:
                //Toast.makeText(getApplicationContext(), "account item selected", Toast.LENGTH_SHORT).show();
                break;
            case R.id.spinner_category:
                //Toast.makeText(getApplicationContext(), "category item selected", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

        hideKeypad();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == TAKE_PHOTO_CODE && resultCode == RESULT_OK) {
            Log.d("CameraDemo", "Pic saved");
            Toast.makeText(getApplicationContext(), "Image Saved.", Toast.LENGTH_SHORT).show();
        }
        else
        {
            File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/expmgr/" + fileName + "");
            if(file.exists())
            {
                file.delete();
            }
            fileName = "";
        }
    }

    /**
     * @method: hideKeypad
     * @desc: hides the soft keypad.
     */
    private void hideKeypad()
    {

        View selectedView = this.getCurrentFocus();
        if (selectedView != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(selectedView.getWindowToken(), 0);
        }
    }

    /**
     * @method: validateExpenseDate
     * @desc: validate the data fields and shows the appropriate bugs on UI.
     */
    protected boolean validateExpenseDate()
    {

        String tSpentOn = "" + txtSpenton.getText().toString().trim();
        String tPrice = "" + txtPrice.getText().toString().trim();
        String regex = "[a-zA-z0-9\\s]*";   //alphanumeria and space.
        try
        {
            if(tSpentOn.equalsIgnoreCase(""))
            {
                txtSpenton.setError("Enter expense item");
                return false;   //string is empty
            }
            else
            {
                txtSpenton.setError(null);
            }
            if(!tSpentOn.matches(regex))
            {
                txtSpenton.setError("Support Alphabets & number only");
                return false;   //string doesn't matches the given regex pattern.
            }
            else
            {
                txtSpenton.setError(null);
            }
            if(tPrice.equalsIgnoreCase(""))
            {
                txtPrice.setError("Enter price");
                return false;
            }
            else
            {
                txtPrice.setError(null);
            }
            //int Price = Integer.parseInt(tPrice);
            float Price = Float.parseFloat(tPrice);
            return true;    //validation successful.
        }
        catch (Exception genericExpception)
        {
            System.out.println("genericException" + genericExpception.getMessage());
            Log.d("genericException", genericExpception.getMessage());
            txtSpenton.setText("" + genericExpception.getMessage());
           return false;        //validation failed due to some exception.
        }
    }

    /**
     * @method: saveExpenseData
     * @desc: saves the data in SQLite.
     */
    private void saveExpenseData()
    {
        //do SQLite operation to save data.
        mydatabase = openOrCreateDatabase(Constants.dbname, MODE_PRIVATE, null);

        String spenton = txtSpenton.getText().toString();
        String price = txtPrice.getText().toString();
        String datemonth = new DateFormatSymbols().getMonths()[datepicker.getMonth()];
        String setdate = datepicker.getDayOfMonth() + " " + datemonth + "," + datepicker.getYear();
        String settime = timePicker.getCurrentHour() + ":" + timePicker.getCurrentMinute();
        String datetime = "" + setdate + "  " + settime;
        String category = "" + Constants.categories.get(spinnerCategory.getSelectedItemPosition());
        String account = "" + Constants.account.get(spinnerAccount.getSelectedItemPosition());
        String hexcolor = String.format("%02x%02x%02x", red, green, blue);

        mydatabase.execSQL("INSERT INTO expense (spenton, price, datetime, account, category, image, indicator) VALUES ('" + spenton + "','" + price + "','" + datetime + "', '" + account + "', '" + category + "', '" + fileName + "', '" + hexcolor + "' )");

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

        Toast.makeText(getApplicationContext(), "Expense saved.", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(ExpenseCreateActivity.this, DashboardActivity.class));
        finish();
    }

    private void getExpenseColor()
    {
        //color picker dialog.
        LayoutInflater inflater = getLayoutInflater();
        View v = inflater.inflate(R.layout.dialog_color_rgb_select, null);

        colorCanvas = v.findViewById(R.id.imageViewMixcolor);

        SeekBar seekBarRed = v.findViewById(R.id.seekBarR);
        SeekBar seekBarGreen = v.findViewById(R.id.seekBarG);
        SeekBar seekBarBlue = v.findViewById(R.id.seekBarB);

        seekBarRed.setProgress(0);
        seekBarRed.incrementProgressBy(1);
        seekBarRed.setMax(255);

        seekBarGreen.setProgress(0);
        seekBarGreen.incrementProgressBy(1);
        seekBarGreen.setMax(255);

        seekBarBlue.setProgress(0);
        seekBarBlue.incrementProgressBy(1);
        seekBarBlue.setMax(255);

        seekBarRed.setOnSeekBarChangeListener(this);
        seekBarGreen.setOnSeekBarChangeListener(this);
        seekBarBlue.setOnSeekBarChangeListener(this);

        seekBarRed.setSoundEffectsEnabled(true);
        seekBarGreen.setSoundEffectsEnabled(true);
        seekBarBlue.setSoundEffectsEnabled(true);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ExpenseCreateActivity.this);
        alertDialogBuilder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //save button is clicked.

            }
        });
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setIcon(android.R.drawable.ic_menu_edit);
        alertDialogBuilder.setTitle("Expense Indicator Color");
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.setView(v);
        alertDialog.show();
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

        flagColorSet = true;
        switch (seekBar.getId())
        {
            case R.id.seekBarR:
                red = progress;
                break;
            case R.id.seekBarG:
                green = progress;
                break;
            case R.id.seekBarB:
                blue = progress;
                break;
        }
        colorCanvas.setBackgroundColor(Color.rgb(red, green, blue));
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}
