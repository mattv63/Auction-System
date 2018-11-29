//cs1555 final project
//Anthony Baionno
//James Mansmann
//Matthew Viego

import java.util.*;
import java.io.*;
import java.sql.*;

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
      System.out.println("1. New Customer Registration");
      System.out.println("2. Update System Date");
      System.out.println("3. Product Statistics");
      System.out.println("4. Statistics");

      //get input
      str = getChoice();
      choice = Integer.parseInt(str);

      //chooses the correct login
      if (choice == 1)
      {
        System.out.println("\nNew Customer Registration");
      }
      else if (choice == 2)
      {
        System.out.println("\nUpdate System Date");
      }
      else if (choice == 3)
      {
        System.out.println("\nProduct Statistics");
      }
      else if (choice == 4)
      {
        System.out.println("\nStatistics");
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

      //chooses the correct login
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
    catch(SQLException e)
    {

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
    catch(SQLException e)
    {

    }
  }

}
