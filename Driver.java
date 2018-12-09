//cs1555 final project
//Anthony Baionno
//James Mansmann
//Matthew Viego

import java.util.*;
import java.io.*;
import java.sql.*;
import java.text.*;
import oracle.sql.*;

public class Driver {

  public static Scanner sin = new Scanner(System.in);
  private Connection dbcon;

  public static void main(String[] args)
  {
    Driver driver = new Driver(args);
  }

  public Driver(String[] args)
  {
    MyAuction auction = new MyAuction(args[0], args[1]);
    dbcon = auction.getConnection();
    driverTest(auction);
  }

  public void driverTest(MyAuction auction){
    System.out.println("Driver test will now begin. Make sure that schema.sql, trigger.sql and insert.sql have both been run.");
    System.out.println("Press enter to continue");
    sin.nextLine();

    System.out.println("Adding an administrator");
    System.out.println("Press enter to continue");
    sin.nextLine();
    auction.addAdministrator("atest1", "test", "atest1", "testtesttest", "test@test.com");

    System.out.println("Adding Customers");
    System.out.println("Press enter to continue");
    sin.nextLine();
    auction.addCustomer("ctest1", "test", "ctest1", "testetstest", "test@test.com");
    auction.addCustomer("ctest2", "test", "ctest2", "testetstest", "test@test.com");
    auction.addCustomer("ctest3", "test", "ctest3", "testetstest", "test@test.com");
    auction.addCustomer("ctest4", "test", "ctest4", "testetstest", "test@test.com");
    auction.addCustomer("ctest5", "test", "ctest5", "testetstest", "test@test.com");
    auction.addCustomer("ctest6", "test", "ctest6", "testetstest", "test@test.com");
    auction.addCustomer("ctest7", "test", "ctest7", "testetstest", "test@test.com");
    auction.addCustomer("ctest8", "test", "ctest8", "testetstest", "test@test.com");
    auction.addCustomer("ctest9", "test", "ctest9", "testetstest", "test@test.com");

    List<String> cats1 = new ArrayList<String>();
    List<String> cats2 = new ArrayList<String>();

    cats1.add("cat3");
    cats1.add("cat4");
    cats2.add("cat5");

    System.out.println("Adding Products");
    System.out.println("Press enter to continue");
    sin.nextLine();
    auction.putToAuction("ptest1", "filler description", cats1.toArray(), 5, "ctest1", 20);
    auction.putToAuction("ptest2", "filler description", cats1.toArray(), 5, "ctest2", 30);
    auction.putToAuction("ptest3", "filler description", cats1.toArray(), 5, "ctest2", 40);
    auction.putToAuction("ptest4", "filler description wow", cats1.toArray(), 5, "ctest3", 30);
    auction.putToAuction("ptest5", "filler description", cats1.toArray(), 5, "ctest5", 20);
    auction.putToAuction("ptest6", "filler description", cats2.toArray(), 5, "ctest6", 10);
    auction.putToAuction("ptest7", "filler description", cats2.toArray(), 5, "ctest6", 25);
    auction.putToAuction("ptest8", "filler description", cats2.toArray(), 5, "ctest6", 5);
    auction.putToAuction("ptest9", "filler description", cats2.toArray(), 5, "ctest9", 100);

    System.out.println("Users will now bid on products");
    System.out.println("Press enter to continue");
    sin.nextLine();
    auction.bid(1, "ctest1", 100);
    auction.bid(2, "ctest2", 120);
    auction.bid(3, "ctest3", 80);
    auction.bid(4, "ctest4", 120);
    auction.bid(5, "ctest1", 100);
    auction.bid(6, "ctest2", 40);
    auction.bid(7, "ctest5", 60);
    auction.bid(8, "ctest6", 80);
    auction.bid(1, "ctest2", 120);
    auction.bid(2, "ctest6", 200);

    System.out.println("\nBrowsing Products in category 3 in descending price order");
    System.out.println("Press Enter to display");
    sin.nextLine();
    auction.productsFromCategory("cat3", 1);

    System.out.println("\nBrowsing Products in category 5 in ascending price order");
    System.out.println("Press Enter to display");
    sin.nextLine();
    auction.productsFromCategory("cat5", 2);

    System.out.println("\nBrowsing Products in category 4 in alphabetical order");
    System.out.println("Press Enter to display");
    sin.nextLine();
    auction.productsFromCategory("cat4", 3);

    System.out.println("\nBrowsing Products in category 10 in ascending price order. (There are no products in this category)");
    System.out.println("Press Enter to display");
    sin.nextLine();
    auction.productsFromCategory("cat10", 1);

    System.out.println("\nWe will now search for product by one or two keywords");
    System.out.println("Searching for word 'filler'");
    System.out.println("Press Enter to display");
    sin.nextLine();
    auction.findProducts("%filler%", "%%");

    System.out.println("Searching for word 'wow'");
    System.out.println("Press Enter to display");
    sin.nextLine();
    auction.findProducts("%wow%", "%%");

    System.out.println("Searching for word 'hello'. This word is not in any description");
    System.out.println("Press Enter to display");
    sin.nextLine();
    auction.findProducts("%hello%", "%%");

    System.out.println("\nWe will now manually move Auction 8 status to close and have the user ctest6 sell their product. When asked which product to sell, press 1");
    System.out.println("Press Enter to continue");
    sin.nextLine();
    try{
      PreparedStatement ps = dbcon.prepareStatement("update product set status = 'closed' where auction_id = 8");
      ps.executeQuery();
    } catch(SQLException e){
      System.err.println("error");
      System.exit(1);
    }
    auction.currentUser = "ctest6";
    auction.sellProduct(8);

    System.out.println("\nNow we will display suggestions for user ctest1. Ctest1 has one bid on auction1");
    System.out.println("Press Enter to continue");
    sin.nextLine();
    auction.currentUser = "ctest1";
    auction.makeSuggestion();

    System.out.println("\nNow we will update the System Date. The new date will be 01.09/2019 12:12:12");
    System.out.println("Press Enter to continue");
    sin.nextLine();
    auction.newDate("01.09/2019 12:12:12");

    System.out.println("\nWe will now display the full product inventory from an admin view.");
    System.out.println("Press Enter to display");
    sin.nextLine();
    auction.fullInventory();

    System.out.println("\nWe will now display the a customer's inventory. Enter a customer's name when prompted to");
    System.out.println("Press Enter to continue");
    sin.nextLine();
    System.out.println("\nEnter a customer's name:");
    String name = sin.nextLine();
    auction.customerInventory(name);

    System.out.println("\nWe will now display the top 5 leaf categories");
    System.out.println("Press Enter to continue");
    sin.nextLine();
    auction.topLeaf(1, 5);

    System.out.println("\nWe will now display the top 5 root categories");
    System.out.println("Press Enter to continue");
    sin.nextLine();
    auction.topRoot(1, 5);

    System.out.println("\nWe will now display the top 5 most active bidders");
    System.out.println("Press Enter to continue");
    sin.nextLine();
    auction.topBid(1, 5);

    System.out.println("\nWe will now display the top 5 most active buyers");
    System.out.println("Press Enter to continue");
    sin.nextLine();
    auction.topBuy(1, 5);
  }
}
