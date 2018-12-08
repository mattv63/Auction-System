//cs1555 final project
//Anthony Baionno
//James Mansmann
//Matthew Viego

import java.util.*;
import java.io.*;
import java.sql.*;
import java.text.*;
import oracle.sql.*;

public class MyAuction
{
  int menu;
  Scanner scan;
  private Connection dbcon;
  private String username;
  private String password;
  private String currentUser;

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
        browse_products();
        menus(2);
      }
      else if (choice == 2)
      {
        System.out.println("\nSearch Products");
        search_products();
        menus(2);
      }
      else if (choice == 3)
      {
        System.out.println("\nAuction Product");
        auction_product();
        menus(2);
      }
      else if (choice == 4)
      {
        System.out.println("\nBid on Product");
        placeBid();
        menus(2);

      }
      else if (choice == 5)
      {
        System.out.println("\nSell Product");
        sellProductSetup();
        menus(2);
      }
      else if (choice == 6)
      {
        System.out.println("\nSuggestions");
        makeSuggestion();
        menus(2);
      }
      else
      {
        System.out.println("Not a valid option. Try again.");
        menus(0);
      }
    }
  }

  public void auction_product(){

    // Get name and description of product
    System.out.println("You are putting a product up for auction. Please enter the following information about the product");
    System.out.println("Product name:");
    String name = getChoice();
    System.out.println("Product Description:");
    String description = getChoice();

    // Get product cateogories
    System.out.println("List product categories. Separate by spaces please.");
    String categories_input = getChoice();
    List<String> categories = Arrays.asList(categories_input.trim().split("\\s"));
    List<String> cats_copy = new ArrayList<String>(categories);
    List<String> not_valid = new ArrayList<String>();

    // Remove categories that are not leaf categories
    ResultSet r;
    for (String category: categories){
      try{
        Statement s = dbcon.createStatement();
        r = s.executeQuery("select count(name) from category where name = '"+ category + "' or parent_category = '"+ category +"'");
        r.next();
        if (r.getInt(1) != 1){
          cats_copy.remove(category);
          not_valid.add(category);
        }
      } catch(SQLException e){
        System.err.println("Error");
        System.exit(1);
      }
    }

    // Display invalid categories
    int invalid_category_count = not_valid.size();
    String invalid = "";
    if (invalid_category_count > 0){
      for (int i = 0; i < not_valid.size(); i++) {
        invalid += not_valid.get(i);
        invalid += ", ";
      }
    }

    if (invalid_category_count == 1){
      System.out.println(invalid + "is not a valid category");
    }
    else if(invalid_category_count > 1){
      System.out.println(invalid + "are not valid categories");
    }

    // Get days and minimum starting price
    System.out.println("Number of days for auction: ");
    String str = getChoice();
    int days = Integer.parseInt(str);
    System.out.println("Minimum starting price (this is optional. Default will be $0):");
    str = getChoice();
    int price = Integer.parseInt(str);

    int auction_id = putToAuction(name, description, cats_copy.toArray(), days, price);
    System.out.println("Auction was created. Your auction ID is " + auction_id + "");


  }

  public int putToAuction(String name, String description, Object[] categories, int days, int price) {
    try{
      CallableStatement auction = dbcon.prepareCall("begin proc_putProduct(?, ?, ?, ?, ?, ?, ?); end;");
      auction.registerOutParameter(7, Types.INTEGER);
      auction.setString(1, name);
      auction.setString(2, description);
      ArrayDescriptor desc = ArrayDescriptor.createDescriptor("VCARRAY", dbcon);
      auction.setArray(3, new ARRAY(desc, dbcon, categories));
      auction.setInt(4, days);
      auction.setString(5, currentUser);
      auction.setInt(6, price);
      auction.execute();
      return auction.getInt(7);
    } catch (SQLException e){
      System.err.println("Error");
      System.exit(1);
      return 0;
    }

  }

  // search for product
  public void search_products(){
    System.out.println("Search for up to two keywords. Separate words by a space");
    String[] keywords = getChoice().split("\\s");
    String first_keyword;
    String second_keyword;

    if (keywords.length < 1 || keywords.length > 2){
      System.out.println("Please enter up to TWO keywords");
      return;
    }

    first_keyword = keywords[0];

    if (keywords.length != 2){
      second_keyword = null;
    }

    else{
      second_keyword = keywords[1];
    }

    try {
      String q = "";
      ResultSet products;

      if (second_keyword != null){
        q = " and upper(description) like upper('%" + second_keyword + "%')";
      }

      Statement s = dbcon.createStatement();
      products = s.executeQuery("select auction_id, name, description from product where upper(description) like upper('%" + first_keyword + "%')" + q);
      if (!(products.isBeforeFirst())){
        products = null;
      }

      if (products != null){
        while(products.next()) {
					System.out.println("Auction ID: " + products.getInt(1));
          System.out.println("Product: " + products.getString(2));
          System.out.println("Description: " + products.getString(3) + "\n");
				}
      }
      else {
        System.out.println("No products found matching the keyword(s)");
      }
    } catch(SQLException e){
        System.err.println("Error");
        System.exit(1);
    }

  }

  public void browse_products() {
    try{
      List<String> categories = null;
      String category_choice = null;
      int choice = 0;
      String str;

      do {
        categories = getSubCategories(category_choice);
        if (categories != null){
          for (int i = 1; i <= categories.size(); i++) {
            System.out.println(" " + i + "." + categories.get(i - 1));
          }
          str = getChoice();
          choice = Integer.parseInt(str);
          category_choice = categories.get(choice - 1);
        }
      }while (categories != null);

      System.out.println("How would you like the products to be sorted?");
      System.out.println("1. Highest bid first");
      System.out.println("2. Lowest bid first");
      System.out.println("3. Alphabetically");
      str = getChoice();
      choice = Integer.parseInt(str);

      String q = null;
      if (choice == 1){
        q = "SELECT auction_id, name, description, amount FROM Product WHERE status = 'under auction' AND auction_id IN(SELECT auction_id FROM belongsto WHERE category = '" + category_choice + "') order by amount desc";
      }
      else if (choice == 2){
        q = "SELECT auction_id, name, description, amount FROM Product WHERE status = 'under auction' AND auction_id IN(SELECT auction_id FROM belongsto WHERE category = '" + category_choice + "') order by amount asc";
      }
      else {
        q = "SELECT auction_id, name, description, amount FROM Product WHERE status = 'under auction' AND auction_id IN(SELECT auction_id FROM belongsto WHERE category = '" + category_choice + "') order by name asc";
      }

      ResultSet products = null;

      try{
        Statement s = dbcon.createStatement();
        products = s.executeQuery(q);
      } catch (SQLException e) {
          System.err.println("Error");
          System.exit(1);
      }
      ResultSet highest_bid = null;

      if (products == null){
        System.out.println("No products found");
      }

      else {
				while (products.next()) {
          try {
            Statement st = dbcon.createStatement();
            highest_bid = st.executeQuery("select max(amount) from bidlog where auction_id = " + products.getInt(1));
          } catch (SQLException e){
            System.err.println("Error");
            System.exit(1);
          }

          highest_bid.next();
          System.out.println("Auction ID: " + products.getInt(1));
          System.out.println("Product: " + products.getString(2));
          System.out.println("Description: " + products.getString(3));
          System.out.println("Highest Bid: " + highest_bid.getInt(1));
				}
      }
    } catch (SQLException e){
      System.err.println("Error");
      System.exit(1);
    }
  }

  public List<String> getSubCategories(String parent_category){
    ResultSet subcats;
    List<String> categories = new ArrayList<String>();

    if (parent_category == null){
      try {
        Statement s = dbcon.createStatement();
        subcats = s.executeQuery("SELECT name FROM Category WHERE parent_category IS null");
      } catch(SQLException e){
          System.err.println("Error");
          System.exit(1);
          return null;
      }
    }
    else{
      try {
        Statement s = dbcon.createStatement();
        subcats = s.executeQuery("SELECT name FROM Category WHERE parent_category = '" + parent_category + "'");
        if (subcats.isBeforeFirst()){

        }
        else {
          subcats = null;
        }
      } catch(SQLException e){
          System.err.println("Error");
          System.exit(1);
          return null;
      }
    }

    if (subcats != null){
      try {
        while (subcats.next()) {
          categories.add(subcats.getString(1));
        }
        return categories;
      } catch (SQLException e) {
          System.err.println("Error");
          System.exit(1);
          return null;
      }
    }
    else {
      return null;
    }

  }

  public void makeSuggestion() {
    try {
      Statement st;
      ResultSet suggestions;
      st = dbcon.createStatement();
      //query to get back product id, name, description, and highest bid
      //for products that people who have bid on the same items as current user
      //have also bid on
      suggestions = st.executeQuery("select product.auction_id, product.name, product.description, product.amount from (" +
				"select friends.bidder, bids.auction_id from (select distinct bidder from bidlog b1 where not exists (" +
				"select distinct auction_id from bidlog b2 where bidder = '" + currentUser + "' and not exists (select distinct bidder, auction_id " +
				"from bidlog b3 where b1.bidder = b3.bidder and b2.auction_id = b3.auction_id)) and bidder <> '" + currentUser + "' and (" +
				"select count(auction_id) from bidlog where bidder = '" + currentUser + "') > 0) friends join bidlog bids on friends.bidder = bids.bidder " +
				"join product p on bids.auction_id = p.auction_id where bids.auction_id not in (select distinct auction_id from bidlog " +
				"where bidder = '" + currentUser + "') and p.status = 'underauction') t1 join product on t1.auction_id = product.auction_id " +
				"group by product.auction_id, product.name, product.description, product.amount order by count(bidder) desc");

      if (!suggestions.next()) {
        System.out.println("There were no suggestions found for you");
      }
      else {
        int i = 0;
        while(suggestions.next()) {
          i++;
          System.out.println("Suggestion " + i + ":");
          System.out.println("Product ID: " + suggestions.getInt(1));
          System.out.println("Product Name: " + suggestions.getString(2));
          System.out.println("Description: " + suggestions.getString(3));
          System.out.println("Highest Bid: " + suggestions.getInt(4));
          System.out.println("=============");
        }
      }
    } catch (SQLException e) {
      System.out.println("SQLException");
      System.exit(0);
    }
  }

  public void sellProductSetup() {
    try {
      ResultSet closedAuctions;
      Statement st;
      st = dbcon.createStatement();
      //query product table for all of the current user's closed auctions
      closedAuctions = st.executeQuery("select auction_id, name, from product where seller = '" + currentUser + "' and status = 'closed'");
      if (closedAuctions == null) {
        System.out.println("You do not have any closed auctions");
      }
      else {
        List<Integer> id = new ArrayList<Integer>();
        List<String> product = new ArrayList<String>();

        //store the id and a display string for each product
        while (closedAuctions.next()) {
          id.add(closedAuctions.getInt(1));
          product.add(closedAuctions.getString(2) + " >> ID: " + closedAuctions.getInt(1));
        }

        //get which number of the product to sell
        System.out.println("Your closed auctions:");
        for (int i = 1; i <= product.size(); i++) {
          System.out.println(i + ". " + product.get(i-1));
        }
        System.out.println("Enter number of product you'd like to sell");
        String str = getChoice();

        int a_id = id.get(Integer.parseInt(str) - 1);
        sellProduct(a_id);
      }
    }
    catch (SQLException e) {
      System.out.println("SQLException");
      System.exit(0);
    }
  }

  //method actually updates the DB when a product is sold
  public void sellProduct(int a_id) {
    try {
      ResultSet bidCount;
      Statement st;
      st = dbcon.createStatement();
      //query bidlog table for the number of bids on the product
      bidCount = st.executeQuery("select count(bidsn) as bids from bidlog where auction_id = " + a_id);
      bidCount.next();
      int bids = bidCount.getInt(1);
      if(bids > 0) {
        //get second or first highest bid (first if only one bid)
        int bidNum = 1;
        if (bids == 1)
          bidNum = 1;
        else
          bidNum = 2;
        st = dbcon.createStatement();
        ResultSet priceR = st.executeQuery("select amount from (select amount, rownum as rn from " +
          "(select amount from bidlog where auction_id = " + a_id + " " +
					"order by bid_time desc) where rownum <= 2) where rn = " + bidNum);
        priceR.next();
        int price = priceR.getInt(1);

        System.out.println("Would you like to sell your product for $" + price + "?");
        System.out.println("1. Sell Product");
        System.out.println("2. Withdraw Product");
        String str = getChoice();
        int choice = Integer.parseInt(str);
        if (choice == 1) {
          //update product table to set status to sold and sell amount to highest bid
          st = dbcon.createStatement();
          st.executeQuery("update product set status = 'sold', buyer = (select * from " +
            "(select bidder from bidlog where auction_id = " + a_id + " " +
						"order by bid_time desc) where rownum <= 1), sell_date = (select my_time from sys_time)," +
            " amount = " + price + " where auction_id = " + a_id);
          System.out.println("Sold Product " + a_id + " for $" + price);
        }
        else {
          //withdraw if selected
          st = dbcon.createStatement();
          st.executeQuery("update product set status = 'withdrawn' where auction_id = " + a_id);
          System.out.println("Withdrew Product " + a_id);
        }
      }
      else {
        //withdraw if no bids on auction
        st = dbcon.createStatement();
        st.executeQuery("update product set status = 'withdrawn' where auction_id = " + a_id);
        System.out.println("No bids were placed on your product (Auction ID = " + a_id + "). This auction has been withdrawn");
      }
    }
    catch (SQLException e) {
      System.out.println("SQLException");
      System.exit(0);
    }
  }

  public void placeBid() {
    System.out.println("\nEnter Auction ID");
    String str = getChoice();
    int a_id = Integer.parseInt(str);

    // retrieves highest bid on product and displays to user
    try {
      Statement st = dbcon.createStatement();
      ResultSet highest_bid = st.executeQuery("select max(amount) from bidlog where auction_id = " + a_id);
      highest_bid.next();
      int h_bid = highest_bid.getInt(1);
      System.out.println("Current highest bid is " + h_bid);
    } catch (SQLException e){
      System.out.println("Error");
      System.exit(0);
    }



    System.out.println("\nEnter bid amount");
    str = getChoice();
    int bid = Integer.parseInt(str);

    try {
      //lock table for inserts on bidlog
      dbcon.setAutoCommit(false);
			Statement locking = dbcon.createStatement();
			locking.execute("lock table bidlog in share row exclusive mode");

      //insert new row into bidlog
      PreparedStatement s = dbcon.prepareStatement("insert into bidlog values(1, ?, ?, (SELECT c_date FROM ourSysDATE), ?)");
      s.setInt(1, a_id);
      s.setString(2, currentUser);
      s.setInt(3, bid);
      s.executeQuery();

      dbcon.commit();
      dbcon.setAutoCommit(true);
      System.out.println("\nBid successful!") ;


    } catch (SQLException e) {
      System.out.println("SQLException");
      System.exit(0);
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
          currentUser = name;
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
      pst = dbcon.prepareStatement("UPDATE ourSysDATE SET c_date = (to_date(?,'mm.dd/yyyy hh24:mi:ss'))");
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
