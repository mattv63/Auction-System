//cs1555 final project
//Anthony Baionno
//James Mansmann
//Matthew Viego

import java.util.*;
import java.io.*;
import java.sql.*;
import java.text.*;
import oracle.sql.*;

public class Benchmark {

  public static Scanner sin = new Scanner(System.in);
  private Connection dbcon;
  List<String> cats1 = new ArrayList<String>();
  List<String> cats2 = new ArrayList<String>();
  List<String> cats3 = new ArrayList<String>();
  List<String> cats4 = new ArrayList<String>();

  public static void main(String[] args)
  {
    Benchmark driver = new Benchmark(args);
  }

  public Benchmark(String[] args)
  {
    MyAuction auction = new MyAuction(args[0], args[1]);
    dbcon = auction.getConnection();
    benchmarkTest(auction);
  }

  public void benchmarkTest(MyAuction auction){
    System.out.println("Beginning Benchmark.");
    System.out.println("Press enter to continue");
    sin.nextLine();

    System.out.println("Adding 10 administrators");
    System.out.println("Press enter to continue");
    sin.nextLine();
    addAdministrators(auction);

    System.out.println("Adding 10 Customers");
    System.out.println("Press enter to continue");
    sin.nextLine();
    addCustomers(auction);

    System.out.println("Adding 20 Products");
    System.out.println("Press enter to continue");
    sin.nextLine();
    addCats();
    addProducts(auction);

    System.out.println("Placing a few bids on each of the products");
    System.out.println("Press enter to continue");
    sin.nextLine();
    bidOnProducts(auction);

    System.out.println("\nBrowsing Products; listing all categories products in varying orders");
    System.out.println("Press Enter to display");
    sin.nextLine();
    browseProducts(auction);

    System.out.println("\nSearching Products; searching for products with \"filler\" in description 10 times");
    System.out.println("Press Enter to display");
    sin.nextLine();
    findWord(auction, "%filler%");

    System.out.println("\nSearching Products; searching for products with \"jawn\" in description 10 times");
    System.out.println("Press Enter to display");
    sin.nextLine();
    findWord(auction, "%jawn%");

    System.out.println("\nSearching Products; searching for products with \"hello\" in description 10 times. This is not in any description");
    System.out.println("Press Enter to display");
    sin.nextLine();
    findWord(auction, "%hello%");

    System.out.println("\nNow going to close all of the auctions posted by ctest1");
    System.out.println("Press Enter to display");
    sin.nextLine();
    closeAuctions(auction);

    System.out.println("\nDisplay suggestions for ctest1 10 times each");
    System.out.println("Press Enter to display");
    sin.nextLine();
    giveSuggestions(auction);

    System.out.println("\nDisplay the full product inventory from admin view 5 times.");
    System.out.println("Press Enter to display");
    sin.nextLine();
    displayProductsAdmin(auction);

    System.out.println("\nDisplay the customer inventory of the customer you enter 5 times");
    System.out.println("Press Enter to continue");
    sin.nextLine();
    System.out.println("\nEnter a customer's name:");
    String name = sin.nextLine();
    displayProductsCustomer(auction, name);

    System.out.println("\nDisplay top five leaf catagories from the past month 5 times");
    System.out.println("Press Enter to continue");
    sin.nextLine();
    displayLeaf(auction);

    System.out.println("\nDisplay top five root catagories from the past month 5 times");
    System.out.println("Press Enter to continue");
    sin.nextLine();
    displayRoot(auction);

    System.out.println("\nDisplay top five bidders from the past month 5 times");
    System.out.println("Press Enter to continue");
    sin.nextLine();
    displayBidders(auction);

    System.out.println("\nDisplay top five buyers from the past month 5 times");
    System.out.println("Press Enter to continue");
    sin.nextLine();
    displayBuyers(auction);
  }

  private void displayBuyers(MyAuction auction) {
    for (int i = 0; i < 5; i++) {
      auction.topBuy(1, 5);
    }
  }

  private void displayBidders(MyAuction auction) {
    for (int i = 0; i < 5; i++) {
      auction.topBid(1, 5);
    }
  }

  private void displayLeaf(MyAuction auction) {
    for (int i = 0; i < 5; i++) {
      auction.topLeaf(1, 5);
    }
  }

  private void displayRoot(MyAuction auction) {
    for (int i = 0; i < 5; i++) {
      auction.topRoot(1, 5);
    }
  }
  private void displayProductsCustomer(MyAuction auction, String name) {
    for (int i = 0; i < 5; i++) {
      System.out.println(i);
      auction.customerInventory(name);
    }
  }

  private void displayProductsAdmin(MyAuction auction) {
    for (int i = 0; i < 5; i++) {
      System.out.println(i);
      auction.fullInventory();
    }
  }

  private void giveSuggestions(MyAuction auction){
      for (int j = 1; j <= 5; j++) {
        System.out.println(j);
        auction.makeSuggestion();
      }
  }
  private void closeAuctions(MyAuction auction) {
    auction.currentUser = "ctest2";
    for (int i = 1; i <= 4; i++) {
      try{
        PreparedStatement ps = dbcon.prepareStatement("update product set status = 'closed' where auction_id = " + i);
        ps.executeQuery();
      } catch(SQLException e){
        System.err.println("error");
        System.exit(1);
      }

      auction.sellProduct(i);
    }

  }

  private void findWord(MyAuction auction, String word){
    for (int i = 0; i < 10; i++) {
      auction.findProducts(word, "%%");
    }
  }

  private void browseProducts(MyAuction auction) {
    for (int i = 3; i <= 5; i++){
      for (int j = 1; j <= 3; j++) {
        System.out.println("i: " + i + " j: " + j);
        auction.productsFromCategory("cat"+i, j);
      }
    }
  }

  private void bidOnProducts(MyAuction auction) {
    for (int i = 1; i <= 5; i++) {
      for (int j = 2; j <= 3; j++) {
        System.out.println("bid " + i + " ctest" + j + " " + (i+11+j));
        auction.bid(i, "ctest" + j, i+11+j);
      }
    }
    for(int i = 6; i <= 10; i++) {
      for (int j = 3; j <= 4; j++) {
        auction.bid(i, "ctest" + j, i+20+j);
      }
    }
    for(int i = 11; i <= 15; i++) {
      for (int j = 4; j <= 5; j++) {
        auction.bid(i, "ctest" + j, i+30+j);
      }
    }
    for(int i = 16; i <= 19; i++) {
      for (int j = 5; j <= 6; j++) {
        auction.bid(i, "ctest" + j, i+40+j);
      }
    }
  }

  private void addProducts(MyAuction auction) {
    for (int i = 0; i < 20; i++) {
      if (i <= 4){
        auction.putToAuction("ptest" + i, "filler description", cats1.toArray(), 5, "ctest1", 10);
      }
      else if (i >= 5 && i <= 9) {
        auction.putToAuction("ptest" + i, "other filler description",cats1.toArray(), 6, "ctest2", 20);
      }
      else if (i >= 10 && i <= 14) {
        auction.putToAuction("ptest" + i, "dont fill description",cats2.toArray(), 7, "ctest3", 30);
      }
      else if (i >= 15 && i <= 19) {
        auction.putToAuction("ptest" + i, "jawn description", cats2.toArray(), 8, "ctest4", 40);
      }
    }
  }

  private void addCats() {
    cats1.add("cat3");
    cats1.add("cat4");
    cats2.add("cat5");
  }

  private void addCustomers(MyAuction auction) {
    for (int i = 0; i < 10; i++){
      auction.addCustomer("ctest" + i, "test" + i, "ctest"+ i, "testaddr" + i, "ctest" + i + "@test.com");
    }
  }

  private void addAdministrators(MyAuction auction) {
    for (int i = 0; i < 10; i++){
      auction.addAdministrator("atest" + i, "test" + i, "atest"+ i, "testaddr" + i, "atest" + i + "@test.com");
    }
  }
}
