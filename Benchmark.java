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
  List<String> cats5 = new ArrayList<String>();
  List<String> cats6 = new ArrayList<String>();
  List<String> cats7 = new ArrayList<String>();
  List<String> cats8 = new ArrayList<String>();
  List<String> cats9 = new ArrayList<String>();
  List<String> cats10 = new ArrayList<String>();

  public static void main(String[] args)
  {
    Driver driver = new Driver(args);
  }

  public Benchmark(String[] args)
  {
    MyAuction auction = new MyAuction(args[0], args[1]);
    dbcon = auction.getConnection();
    driverTest(auction);
  }

  public void benchmarkTest(MyAuction auction){
    System.out.println("Beginning Benchmark.")
    System.out.println("Press enter to continue");
    sin.nextLine();

    System.out.println("Adding 1000 administrators");
    System.out.println("Press enter to continue");
    sin.nextLine();
    addAdministrators(auction);

    System.out.println("Adding 1000 Customers");
    System.out.println("Press enter to continue");
    sin.nextLine();
    addCustomers(auction);

    System.out.println("Adding 1000 Products");
    System.out.println("Press enter to continue");
    sin.nextLine();
    addCats();
    addProducts(auction);

    System.out.println("Placing 10 bids on each of the 1000 products");
    System.out.println("Press enter to continue");
    sin.nextLine();
    bidOnProducts(auction);

    System.out.println("\nBrowsing Products; listing all categories products in varying orders");
    System.out.println("Press Enter to display");
    sin.nextLine();
    browseProducts(auction);

    System.out.println("\nSearching Products; searching for products with \"filler\" in description 100 times");
    System.out.println("Press Enter to display");
    sin.nextLine();
    findWord(auction, "%filler%");

    System.out.println("\nSearching Products; searching for products with \"jawn\" in description 100 times");
    System.out.println("Press Enter to display");
    sin.nextLine();
    findWord(auction, "%jawn%");

    System.out.println("\nSearching Products; searching for products with \"hello\" in description 100 times. This is not in any description");
    System.out.println("Press Enter to display");
    sin.nextLine();
    findWord(auction, "%hello%");

    System.out.println("\nNow going to close all of the auctions posted by ctest1 and ctest5");
    System.out.println("Press Enter to display");
    sin.nextLine();
    closeAuctions(auction);

    System.out.println("\nDisplay suggestions for each customer 100 times each");
    System.out.println("Press Enter to display");
    sin.nextLine();
    giveSuggestions(auction);

    System.out.println("\nDisplay the full product inventory from admin view 1000 times.");
    System.out.println("Press Enter to display");
    sin.nextLine();
    displayProductsAdmin(auction);

    System.out.println("\nDisplay the customer inventory of the customer you enter 1000");
    System.out.println("Press Enter to continue");
    sin.nextLine();
    System.out.println("\nEnter a customer's name:")
    String name = sin.nextLine();
    displayProductsCustomer(auction, name);

    System.out.println("\nDisplay top five leaf catagories from the past month 1000 times");
    System.out.println("Press Enter to continue");
    sin.nextLine();
    displayLeaf(auction);

    System.out.println("\nDisplay top five root catagories from the past month 1000 times");
    System.out.println("Press Enter to continue");
    sin.nextLine();
    displayRoot(auction);

    System.out.println("\nDisplay top five bidders from the past month 1000 times");
    System.out.println("Press Enter to continue");
    sin.nextLine();
    displayBidders(auction);

    System.out.println("\nDisplay top five buyers from the past month 1000 times");
    System.out.println("Press Enter to continue");
    sin.nextLine();
    displayBuyers(auction);
  }

  private void displayBuyers(MyAuction auction) {
    for (int i = 0; i < 1000; i++) {
      auction.topBuy(1, 5);
    }
  }

  private void displayBidders(MyAuction auction) {
    for (int i = 0; i < 1000; i++) {
      auction.topBid(1, 5);
    }
  }

  private void displayLeaf(MyAuction auction) {
    for (int i = 0; i < 1000; i++) {
      auction.topLeaf(1, 5);
    }
  }

  private void displayRoot(MyAuction auction) {
    for (int i = 0; i < 1000; i++) {
      auction.topRoot(1, 5);
    }
  }
  private void displayProductsCustomer(MyAuction auction, String name) {
    for (int i = 0; i < 1000; i++) {
      auction.customerInventory(name);
    }
  }

  private void displayProductsAdmin(MyAuction auction) {
    for (int i = 0; i < 1000; i++) {
      auction.fullInventory();
    }
  }

  private void giveSuggestions(MyAuction auction){
    for (int i = 1; i <= 10; i++) {
      auction.currentUser = "ctest" + i;
      for (int j = 1; j <= 100; j++) {
        auction.makeSuggestion();
      }
    }
  }
  private void closeAuctions(MyAuction auction) {
    auction.currentUser = "ctest1";
    for (int i = 0; i < 100; i++) {
      try{
        PreparedStatement ps = dbcon.prepareStatement("update product set status = 'closed' where auction_id = " + i);
        ps.executeQuery();
      } catch(SQLException e){
        System.err.println("error");
        System.exit(1);
      }

      auction.sellProduct(i);
    }

    auction.currentUser = "ctest5";
    for (int i = 401; i <= 500; i++) {
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
    for (int i = 0; i < 100; i++) {
      auction.findProducts(word, "%%");
    }
  }

  private void browseProducts(MyAuction auction) {
    for (int i = 11; i < 111; i++){
      for (int j = 1; j <= 3; j++) {
        auction.productsFromCategory("cats"+i, j)
      }
    }
  }

  private void bidOnProducts(MyAuction auction) {
    for (int i = 0; i <= 100; i++) {
      for (int j = 1; j <= 10; j++) {
        auction.bid(i, "ctest" + i, i+10);
      }
    }
    for(int i = 101, i <= 200; i++) {
      for (int j = 1; j <= 10; j++) {
        auction.bid(i, "ctest" + i, i);
      }
    }
    for(int i = 201, i <= 300; i++) {
      for (int j = 1; j <= 10; j++) {
        auction.bid(i, "ctest" + i, i);
      }
    }
    for(int i = 301, i <= 400; i++) {
      for (int j = 1; j <= 10; j++) {
        auction.bid(i, "ctest" + i, i);
      }
    }
    for(int i = 401, i <= 500; i++) {
      for (int j = 1; j <= 10; j++) {
        auction.bid(i, "ctest" + i, i);
      }
    }
    for(int i = 501, i <= 600; i++) {
      for (int j = 1; j <= 10; j++) {
        auction.bid(i, "ctest" + i, i);
      }
    }
    for(int i = 601, i <= 700; i++) {
      for (int j = 1; j <= 10; j++) {
        auction.bid(i, "ctest" + i, i);
      }
    }
    for(int i = 701, i <= 800; i++) {
      for (int j = 1; j <= 10; j++) {
        auction.bid(i, "ctest" + i, i);
      }
    }
    for(int i = 801, i <= 900; i++) {
      for (int j = 1; j <= 10; j++) {
        auction.bid(i, "ctest" + i, i);
      }
    }
    for(int i = 901, i < 1000; i++) {
      for (int j = 1; j <= 10; j++) {
        auction.bid(i, "ctest" + i, i);
      }
    }
  }

  private void addProducts(MyAuction auction) {
    for (int i = 0; i < 1000; i++) {
      if (i <= 100){
        auction.putToAuction("ptest" + i, "filler description", cats1.toArray(), 5, "ctest1", 10);
      }
      else if (i > 100 && i <= 200) {
        auction.putToAuction("ptest" + i, "other filler description", cats2.toArray(), 6, "ctest2", 20);
      }
      else if (i > 200 && i <= 300) {
        auction.putToAuction("ptest" + i, "dont fill description", cats3.toArray(), 7, "ctest3", 30);
      }
      else if (i > 300 && i <= 400) {
        auction.putToAuction("ptest" + i, "jawn description", cats4.toArray(), 8, "ctest4", 40);
      }
      else if (i > 400 && i <= 500) {
        auction.putToAuction("ptest" + i, "other jawn description", cats5.toArray(), 9, "ctest5", 50);
      }
      else if (i > 500 && i <= 600) {
        auction.putToAuction("ptest" + i, "filler description", cats6.toArray(), 10, "ctest6", 60);
      }
      else if (i > 600 && i <= 700) {
        auction.putToAuction("ptest" + i, "dont fill description", cats7.toArray(), 11, "ctest7", 70);
      }
      else if (i > 700 && i <= 800) {
        auction.putToAuction("ptest" + i, "jawn description", cats8.toArray(), 12, "ctest8", 80);
      }
      else if (i > 800 && i <= 900) {
        auction.putToAuction("ptest" + i, "elite description", cats9.toArray(), 7, "ctest9", 90);
      }
      else {
        auction.putToAuction("ptest" + i, "elite description", cats10.toArray(), 6, "ctest10", 100);
      }

    }
  }

  private void addCats() {
    for (int i = 1; i < 10; i++){
      cats1.add("cats"+(i+10));
    }
    for (int i = 1; i < 10; i++){
      cats2.add("cats"+(i+20));
    }
    for (int i = 1; i < 10; i++){
      cats3.add("cats"+(i+30));
    }
    for (int i = 1; i < 10; i++){
      cats4.add("cats"+(i+40));
    }
    for (int i = 1; i < 10; i++){
      cats5.add("cats"+(i+50));
    }
    for (int i = 1; i < 10; i++){
      cats6.add("cats"+(i+60));
    }
    for (int i = 1; i < 10; i++){
      cats7.add("cats"+(i+70));
    }
    for (int i = 1; i < 10; i++){
      cats8.add("cats"+(i+80));
    }
    for (int i = 1; i < 10; i++){
      cats9.add("cats"+(i+90));
    }
    for (int i = 1; i < 10; i++){
      cats10.add("cats"+(i+100));
    }
  }

  private void addCustomers(MyAuction auction) {
    for (int i = 0; i < 1000; i++){
      auction.addCustomer("ctest" + i, "test" + i, "ctest"+ i, "testaddr" + i, "ctest" + i + "@test.com");
    }
  }

  private void addAdministrators(MyAuction auction) {
    for (int i = 0; i < 1000; i++){
      auction.addAdministrator("atest" + i, "test" + i, "atest"+ i, "testaddr" + i, "atest" + i + "@test.com");
    }
  }

  public void driverTest(MyAuction auction){


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
