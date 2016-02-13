package app.expense.org.Model;

/**
 * Created by user on 31/1/16.
 */
public class Expense {


    public int id = 0;
    public String spenton = "";
    public String price = "";
    public String datetime = "";
    public String account = "";
    public String category = "";
    public String image = "";
    public String color = "";

    public Expense(){}

    public Expense(int id, String spenton, String price, String datetime, String account, String category, String image, String color)
    {
        this.id = id;
        this.spenton = spenton;
        this.price = price;
        this.datetime = datetime;
        this.account = account;
        this.category = category;
        this.image = image;
        this.color = color;
    }

}
