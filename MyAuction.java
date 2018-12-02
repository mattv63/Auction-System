//cs1555 final project
//Anthony Baionno
//James Mansmann
//Matthew Viego

import java.util.*;
import java.io.*;
import java.sql.*;
import java.text.*;

public class MyAuction
{
  int menu;
  Scanner scan;
  private Connection dbcon;
  private String username;
  private String password;

  public static void main(String[] args)
  {
    MyAuction auction = new MyAuction(args[0], args[1]);
  }

  public MyAuction(String firstArg, String secondArg)
  {
    username = firstArg;
    password = secondArg;
    scan = new Scanner(System.in);
    menu = 0;

    try
    {
      DriverManager.registerDriver (new oracle.jdbc.driver.OracleDriver());
      String url = "jdbc:oracle:thin:@class3.cs.pitt.edu:1521:dbclass";
      dbcon = DriverManager.getConnection(url, username, password);
    }
    catch (SQLException e)
    {
      System.out.println("SQLException");
      System.exit(0);
    }

    menus(menu);
  }

  //displays the menus
  public void menus(int menu)
  {
    int choice = 0;
    String str;

    //selects the correct menu
    if (menu == 0)
    {
      //main menu
      System.out.println("Main Menu");
      System.out.println("---------");
      System.out.println("1. Administrator");
      System.out.println("2. Customer");

      //get input
      str = getChoice();
      choice = Integer.parseInt(str);

      //chooses the correct login
      if (choice == 1)
      {
        System.out.println("\nAdministrator Login");
        login(choice);
      }
      else if (choice == 2)
      {
        System.out.println("\nCustomer Login");
        login(choice);
      }
      else
      {
        System.out.println("Not a valid option. Try again.");
        menus(0);
      }
    }
    else if (menu == 1)
    {
      //administrator menu
      System.out.println("\nAdministrator");
      System.out.println("-------------");
      System.out.println("1. New User Registration");
      System.out.println("2. Update System Date");
      System.out.println("3. Product Statistics");
      System.out.println("4. Statistics");

      //get input
      str = getChoice();
      choice = Integer.parseInt(str);

      //chooses the correct option
      if (choice == 1)
      {
        System.out.println("\nNew User Registration");
        newUser();
      }
      else if (choice == 2)
      {
        System.out.println("\nUpdate System Date");
        updateSysdate();
      }
      else if (choice == 3)
      {
        System.out.println("\nProduct Statistics");
        prodStats();
      }
      else if (choice == 4)
      {
        System.out.println("\nStatistics");
        stats();
      }
      else
      {
        System.out.println("Not a valid option. Try again.");
        menus(0);
      }
    }
    else if (menu == 2)
    {
      //customer menu
      System.out.println("\nCustomer");
      System.out.println("--------");
      System.out.println("1. Browse Products");
      System.out.println("2. Search Products");
      System.out.println("3. Auction Product");
      System.out.println("4. Bid on Product");
      System.out.println("5. Sell Product");
      System.out.println("6. Suggestions");

      //get input
      str = getChoice();
      choice = Integer.parseInt(str);

      //chooses the correct option
      if (choice == 1)
      {
        System.out.println("\nBrowse Products");
      }
      else if (choice == 2)
      {
        System.out.println("\nSearch Products");
      }
      else if (choice == 3)
      {
        System.out.println("\nAuction Product");
      }
      else if (choice == 4)
      {
        System.out.println("\nBid on Product");
      }
      else if (choice == 5)
      {
        System.out.println("\nSell Product");
      }
      else if (choice == 6)
      {
        System.out.println("\nSuggestions");
      }
      else
      {
        System.out.println("Not a valid option. Try again.");
        menus(0);
      }
    }
  }

  //gets user input
  public String getChoice()
  {
    String str;
    str = scan.nextLine();
    return str;
  }

  //gets the username and password of the user
  public void login(int choice)
  {
    String user;
    String pass;

    System.out.println("Please enter your login information.");
    System.out.print("Username: ");
    user = getChoice();
    System.out.print("Password: ");
    pass = getChoice();

    if (choice == 1)
    {
      adminLogin(user, pass);
    }
    else if (choice == 2)
    {
      custLogin(user, pass);
    }
  }

  //checks the username and password with the admin table
  public void adminLogin(String user, String pass)
  {
    try
    {
      boolean valid = false;
      ResultSet result;
      Statement st;
      //gets all of the possible usernames and passwords
      st = dbcon.createStatement();
      result = st.executeQuery("SELECT login, password FROM Administrator");

      //checks username and password was valid
      String name;
      String word;
      while(result.next())
      {
        name = result.getString("login");
        word = result.getString("password");
        if(user.equals(name) && pass.equals(word))
        {
          valid = true;
          break;
        }
      }


      //call menus to display admin menu
      if (valid)
      {
        menus(1);
      }
      else
      {
        System.out.println("Not a valid username, password combination");
      }
    }
    catch (SQLException e)
    {
      System.out.println("SQLException");
      System.exit(0);
    }
  }
  //checks the username and password with the customer table
  public void custLogin(String user, String pass)
  {
    try
      {
      boolean valid = false;
      ResultSet result;
      Statement st;
      //gets all of the possible usernames and passwords
      st = dbcon.createStatement();
      result = st.executeQuery("SELECT login, password FROM Customer");

      //checks username and password was valid
      String name;
      String word;
      while(result.next())
      {
        name = result.getString("login");
        word = result.getString("password");
        if(user.equals(name) && pass.equals(word))
        {
          valid = true;
          break;
        }
      }

      //call menus to display admin menu
      if (valid)
      {
        menus(2);
      }
      else
      {
        System.out.println("Not a valid username, password combination");
      }
    }
    catch (SQLException e)
    {
      System.out.println("SQLException");
      System.exit(0);
    }
  }

  public void newUser()
  {
    //get all necessary information about the new user
    System.out.println("1. New Administrator Registration\n2. New Customer Registration");
    String str = getChoice();
    int reg = Integer.parseInt(str);
    while (true)
    {
      if (reg == 1 || reg == 2)
      {
        break;
      }
      else
      {
        System.out.println("Please enter a 1 for Administrator or 2 for Customer");
        System.out.println("1. New Administrator Registration\n2. New Customer Registration");
        str = getChoice();
        reg = Integer.parseInt(str);
      }
    }

    System.out.println("Please enter the following information.");
    System.out.print("Name: ");
    String name = getChoice();
    System.out.print("Address: ");
    String addr = getChoice();
    System.out.print("Email Address: ");
    String eaddr = getChoice();
    System.out.print("Username: ");
    String user = getChoice();
    System.out.print("Password: ");
    String pass = getChoice();

    try
    {
      if (reg == 1)
      {
        boolean valid = false;
        ResultSet result;
        Statement st;
        PreparedStatement pst;
        int count = 0;

        while (!valid)
        {
          count = 0;
          //gets all of the usernames in the db
          st = dbcon.createStatement();
          result = st.executeQuery("SELECT login FROM Administrator");

          while(result.next())
          {
            String un = result.getString("login");
            if(user.equals(un))
            {
              count ++;
            }
          }
          if (count > 0)
          {
            System.out.println("Username already in use, please try another.");
            System.out.print("Username: ");
            user = getChoice();
            valid = false;
          }
          else if (count == 0)
          {
            valid = true;
          }
        }
        pst = dbcon.prepareStatement("INSERT INTO Administrator VALUES(?, ?, ?, ?, ?)");
        pst.setString(1, user);
        pst.setString(2, pass);
        pst.setString(3, name);
        pst.setString(4, addr);
        pst.setString(5, eaddr);
        result = pst.executeQuery();
      }
      else if (reg == 2)
      {
        boolean valid = false;
        ResultSet result;
        Statement st;
        PreparedStatement pst;
        int count = 0;

        while (!valid)
        {
          count = 0;
          //gets all of the usernames in the db
          st = dbcon.createStatement();
          result = st.executeQuery("SELECT login FROM Customer");

          while(result.next())
          {
            String un = result.getString("login");
            if(user.equals(un))
            {
              count ++;
            }
          }
          if (count > 0)
          {
            System.out.println("Username already in use, please try another.");
            System.out.print("Username: ");
            user = getChoice();
            valid = false;
          }
          else if (count == 0)
          {
            valid = true;
          }
        }
        pst = dbcon.prepareStatement("INSERT INTO Customer VALUES(?, ?, ?, ?, ?)");
        pst.setString(1, user);
        pst.setString(2, pass);
        pst.setString(3, name);
        pst.setString(4, addr);
        pst.setString(5, eaddr);
        result = pst.executeQuery();
      }
    }
    catch (SQLException e)
    {
      System.out.println("SQLException");
      System.exit(0);
    }

    System.out.println("Successfully added User");
    menus(1);
  }

  public void updateSysdate()
  {
    boolean valid = false;
    System.out.println("Please enter the new date (mm.dd/yyyy hh:mi:ss)");
    String date = "";
    String format = "mm.dd/yyyy hh:mm:ss";
    DateFormat df = new SimpleDateFormat(format);
    ResultSet result;
    PreparedStatement pst;

    while (!valid)
    {
      date = getChoice();
      try
      {
        df.parse(date);
        valid = true;
      }
      catch (ParseException e)
      {
        System.out.println("Incorrect date format");
        System.out.println("Please enter the new date (mm.dd/yyyy hh:mi:ss)");
        valid = false;
      }
    }

    try
    {
      pst = dbcon.prepareStatement("UPDATE ourSysDATE SET c_date = (to_date(?,'mm.dd/yyyy hh:mi:ss'))");
      pst.setString(1, date);
      pst.executeUpdate();
      System.out.println("System Date successfully updated");
    }
    catch (SQLException e)
    {
      System.out.println("SQLException");
      System.exit(0);
    }

    menus(1);
  }

  public void prodStats()
  {
    System.out.println("1. Full Inventory\n2. Customer Inventory");
    String str = getChoice();
    int inv = Integer.parseInt(str);
    while (true)
    {
      if (inv == 1 || inv == 2)
      {
        break;
      }
      else
      {
        System.out.println("Please enter a 1 for Full Inventory or 2 for Customer Inventory");
        System.out.println("1. Full Inventory\n2. Customer Inventory");
        str = getChoice();
        inv = Integer.parseInt(str);
      }
    }
    try
    {
      ResultSet result = null;
      Statement st;
      PreparedStatement pst;

      if (inv == 1)
      {
        // full inventory
        st = dbcon.createStatement();
        result = st.executeQuery("SELECT p.name, p.status, b.amount AS highest_bid, b.bidder FROM product p, bidlog b WHERE p.auction_id = b.auction_id AND p.amount = b.amount AND p.status != 'sold' UNION SELECT p.name, p.status, p.amount AS highest_bid, p.buyer FROM product p WHERE p.status = 'sold'");
      }
      else if (inv == 2)
      {
        //specific customer
        System.out.println("Enter the Customer's username");
        String seller = getChoice();
        pst = dbcon.prepareStatement("SELECT p.name, p.status, b.amount AS highest_bid, b.bidder FROM product p, bidlog b WHERE p.auction_id = b.auction_id AND p.amount = b.amount AND p.status != 'sold' AND p.seller = ? UNION SELECT p.name, p.status, p.amount AS highest_bid, p.buyer FROM product p WHERE p.status = 'sold' AND p.seller = ?");
        pst.setString(1, seller);
        pst.setString(2, seller);
        result = pst.executeQuery();
      }

      //print results
      System.out.print("PRODUCT NAME        ");
      System.out.print("PRODUCT STATUS      ");
      System.out.print("HIGHEST BID         ");
      System.out.print("BIDDER/BUYER        ");
      System.out.println("\n------------        --------------      -----------         ------------        ");
      while (result.next())
      {
        System.out.printf("%-20s %-20s %-20s %-20s\n", result.getString(1), result.getString(2), result.getString(3), result.getString(4));
      }

      menus(1);
    }
    catch (SQLException e)
    {
      System.out.println("SQLException");
      System.exit(0);
    }
  }

  public void stats()
  {
    System.out.println("\nSelect option");
    System.out.println("-------------");
    System.out.println("1. Top Leaf Categories");
    System.out.println("2. Top Root Categories");
    System.out.println("3. Top Bidders");
    System.out.println("4. Top Buyers");

    //get input
    String str = getChoice();
    int choice = Integer.parseInt(str);

    System.out.print("Number of Months to search: ");
    str = getChoice();
    int months = Integer.parseInt(str);
    System.out.print("Number of Results: ");
    str = getChoice();
    int k = Integer.parseInt(str);

    //select a category to view
    if (choice == 1)
    {
      topLeaf(months, k);
    }
    else if (choice == 2)
    {
      topRoot(months, k);
    }
    else if (choice == 3)
    {
      topBid(months, k);
    }
    else if (choice == 4)
    {
      topBuy(months, k);
    }
  }

  public void topLeaf(int months, int k)
  {
    try
    {
      PreparedStatement pst;
      ResultSet result;

      pst = dbcon.prepareStatement("SELECT * FROM (SELECT c.name, func_productcount(?, c.name) AS products FROM category c WHERE func_productcount(?, c.name) IS NOT NULL AND c.parent_category IS NOT NULL ORDER BY products desc) WHERE ROWNUM <= ?");
      pst.setInt(1, months);
      pst.setInt(2, months);
      pst.setInt(3, k);
      result = pst.executeQuery();

      System.out.println("Top " + k + " Highest Volume Leaf Categories");
      System.out.println("------------------------------------");
      System.out.print("CATEGORY            ");
      System.out.print("NUMBER OF PRODUCTS  ");
      System.out.println("\n--------            ------------------    ");

      while (result.next())
      {
        System.out.printf("%-20s%-20s\n", result.getString(1), result.getString(2));
      }

      menus(1);
    }
    catch (SQLException e)
    {
      System.out.println("SQLException");
      System.exit(0);
    }
  }

  public void topRoot(int months, int k)
  {
    try
    {
      PreparedStatement pst;
      ResultSet result;

      pst = dbcon.prepareStatement("SELECT * FROM (SELECT c.name, func_productcount(?, c.name) AS products FROM category c WHERE func_productcount(?, c.name) IS NOT NULL AND c.parent_category IS NULL ORDER BY products desc) WHERE ROWNUM <= ?");
      pst.setInt(1, months);
      pst.setInt(2, months);
      pst.setInt(3, k);
      result = pst.executeQuery();
      System.out.println("Top " + k + " Highest Volume Root Categories");
      System.out.println("------------------------------------");
      System.out.print("CATEGORY            ");
      System.out.print("NUMBER OF PRODUCTS  ");
      System.out.println("\n--------            ------------------    ");
      while (result.next())
      {
        System.out.printf("%-20s%-20s\n", result.getString(1), result.getString(2));
      }

      menus(1);

    }
    catch (SQLException e)
    {
      System.out.println("SQLException");
      System.exit(0);
    }
  }

  public void topBid(int months, int k)
  {
    try
    {
      PreparedStatement pst;
      ResultSet result;

      pst = dbcon.prepareStatement("SELECT * FROM (SELECT c.login, func_bidCount(?, c.login) AS bids FROM customer c WHERE func_bidCount(?, c.login) IS NOT NULL ORDER BY bids desc) WHERE ROWNUM <= ?");
      pst.setInt(1, months);
      pst.setInt(2, months);
      pst.setInt(3, k);
      result = pst.executeQuery();
      System.out.println("Top " + k + " Most Active Bidders");
      System.out.println("-------------------------");
      System.out.print("USERNAME            ");
      System.out.print("NUMBER OF BIDS      ");
      System.out.println("\n--------            --------------        ");
      while (result.next())
      {
        System.out.printf("%-20s%-20s\n", result.getString(1), result.getString(2));
      }

      menus(1);

    }
    catch (SQLException e)
    {
      System.out.println("SQLException");
      System.exit(0);
    }
  }

  public void topBuy(int months, int k)
  {
    try
    {
      PreparedStatement pst;
      ResultSet result;

      pst = dbcon.prepareStatement("SELECT * FROM (SELECT c.login, func_buyingAmount(?, c.login) AS amount FROM customer c WHERE func_buyingAmount(?, c.login) IS NOT NULL ORDER BY amount desc) WHERE ROWNUM <= ?");
      pst.setInt(1, months);
      pst.setInt(2, months);
      pst.setInt(3, k);
      result = pst.executeQuery();
      System.out.println("\nTop " + k + " Most Active Buyers");
      System.out.println("------------------------");
      System.out.print("USERNAME            ");
      System.out.print("AMOUNT SPENT        ");
      System.out.println("\n--------            ------------          ");
      while (result.next())
      {
        System.out.printf("%-20s%-20s\n", result.getString(1), result.getString(2));
      }

      menus(1);
    }
    catch (SQLException e)
    {
      System.out.println("SQLException");
      System.exit(0);
    }
  }

}
