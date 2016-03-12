package app.expense.org.utils;

import android.graphics.Color;

import java.util.ArrayList;

import app.expense.org.Model.Account;
import app.expense.org.Model.Expense;

/**
 * @desc: Constants Class for holding static data members.
 */
public class Constants
{
    //values for holding filter values
    public static ArrayList<String> filterCategory = null;
    public static ArrayList<String> filterAccount = null;

    //contains all values
    public static ArrayList<String> categories = null;
    public static ArrayList<String> account = null;

    //constains model class of all expenses and all accounts.
    public static ArrayList<Expense> expenseData = null;
    public static ArrayList<Account> accountData = null;

    //Application's database name.
    public static String dbname = "expensemanagerdbxxxxx";


}
