package app.expense.org.Model;

/**
 * Created by user on 31/1/16.
 */
public class Account {

    public int id = 0;
    public String name = "";
    public String accountType = "";
    public String desc = "";

    public Account(){}

    public Account(int id, String name, String accountType, String desc)
    {
        this.id = id;
        this.name = name;
        this.accountType = accountType;
        this.desc = desc;
    }


}
