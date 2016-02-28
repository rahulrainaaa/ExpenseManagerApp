package app.expense.org.expensemanagerapp;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import java.text.SimpleDateFormat;
import java.util.Date;

import app.expense.org.utils.Constants;

public class ExpenseCreateActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, SeekBar.OnSeekBarChangeListener {

    /**
     * Constants and Global variables.
     */
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
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        txtSpenton = (EditText)findViewById(R.id.txtspenton);
        txtPrice = (EditText)findViewById(R.id.txtprice);
        datepicker = (DatePicker)findViewById(R.id.datepicker_expense);
        timePicker = (TimePicker)findViewById(R.id.timepicker_expense);

        spinnerAccount = (Spinner)findViewById(R.id.spinner_account);
        spinnerCategory =(Spinner)findViewById(R.id.spinner_category);

        accountAdapter = new ArrayAdapter<String>(ExpenseCreateActivity.this, android.R.layout.simple_spinner_dropdown_item, Constants.account);
        categoryAdapter = new ArrayAdapter<String>(ExpenseCreateActivity.this, android.R.layout.simple_spinner_dropdown_item, Constants.categories);

        spinnerAccount.setAdapter(accountAdapter);
        spinnerCategory.setAdapter(categoryAdapter);

        spinnerAccount.setOnItemSelectedListener(this);
        spinnerCategory.setOnItemSelectedListener(this);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
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
                //expense data not valid--perform check
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
        String regex = "[a-zA-z\\s]*";
        try
        {

            if(tSpentOn.equalsIgnoreCase(""))
            {
                txtSpenton.setError("Cannot be empty");
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
                txtPrice.setError("Enter the price");
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
        Toast.makeText(getApplicationContext(), "Expense saved.", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(ExpenseCreateActivity.this, DashboardActivity.class));
        finish();
    }

    private void getExpenseColor()
    {
        //color picker dialog.
        LayoutInflater inflater = getLayoutInflater();
        View v = inflater.inflate(R.layout.dialog_color_rgb_select, null);

        colorCanvas = (ImageView)v.findViewById(R.id.imageViewMixcolor);

        SeekBar seekBarRed = (SeekBar)v.findViewById(R.id.seekBarR);
        SeekBar seekBarGreen = (SeekBar)v.findViewById(R.id.seekBarG);
        SeekBar seekBarBlue = (SeekBar)v.findViewById(R.id.seekBarB);

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
