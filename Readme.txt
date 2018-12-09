Instructions:

MyAuction.java

Start up:
1. Run schema.sql, trigger.sql and insert.sql in sqlplus
2. Compile with javac MyAuction.java
3. Run with java MyAuction dbusername dbpassword
4. Enter a 1 for Administrator or a 2 for Customer
5. Enter a valid username and password
6. Enter a number corresponding to a possible action

Administrator:
  New User:
  1. Select 1 from the Administrator menu
  2. Enter 1 to add a new Admin or 2 to add a new Customer
  3. Follow on screen instructions to enter information about the new user
  4. If the username entered is already in use, it will ask for another option
  5. The new user will then be entered into the correct table
  
  Update System Date:
  1. Select 2 from the Administrator menu
  2. Enter a date in the correct format (mm.dd/yyyy hh:mi:ss)
  3. If the date is in an incorrect format, it will ask the user to try again
  4. The ourSysDATE table will then be updated with the new date
  
  Product Statistics:
  1. Select 3 from the Administrator menu
  2. Enter 1 to view the full inventory or 2 to view a specific Customer's inventory
  3. If 2 is selected, the user will be prompted to enter a username
  4. The statistics will then be displayed
  
  Statistics:
  1. Select 4 from the Administrator menu
  2. Enter 1, 2, 3 or 4 to view the corresponding stats
  3. The user will be prompted to enter a number of months and a search results limit
  4. The statistics will then be displayed

Customer:
  Browse:
    1. Select 1 from the User menu
    2. Enter the number that corresponds with the parent category
    3. Enter the number the corresponds with the leaf category
    4. That products that belong to the category will be displayed
    
   Search:
    1. Select 2 from the User menu
    2. The user will be prompted to enter up the two keywords, separated by a space
    3. Products that have a description that correspond with the keyword(s) will be displayed
    4. If no products are found, the user will return to the main menu
    
   Auction:
    1. Select 3 from the User menu
    2. The user will be prompted to enter the product name, description, categories, days for auction and minimum price.
    3. The user will be notified which categories were not accepted as valid ones
    4. An auction id is returned to the user
    
  Bid:
    1. Select 4 from customer menu
    2. User prompted to enter auction ID and bid amount
    3. User will be notified whether the bid was successful or if there was an error
  Sell:
    1. Select 5 from customer menu
    2. User will be shown a list of their auctions that have a status of closed if there are any
    3. User enters which number from the list that they wish to sell
    4. User is asked if they wish to sell the product for the second highest bid (or highest if only one bid)
    5. User can choose to sell or withdraw the auction
    6. Database is updated with new status of the auction
  Suggestions:
    1. Select 6 from customer menu
    2. user will be shown suggestions for products to bid on based on similar users' bids
    

Driver.java
1. Run schema.sql, trigger.sql and insert.sql in sqlplus
2. Compile with javac Driver.java
3. Run with java Driver dbusername dbpassword
4. Press enter to move through the program
5. When prompted, enter a name or number to make a selection


Benchmark.java
1. Run schema.sql, trigger.sql and insert.sql in sqlplus
2. Compile with javac Benchmark.java
3. Run with java Benchmark dbusername dbpassword
4. Press enter to move through the program
5. When prompted, enter a name or number to make a selection
