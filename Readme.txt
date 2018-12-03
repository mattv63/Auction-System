Instructions:

Start up:
1. Compile with javac MyAuction.java
2. Run with java MyAuction dbusername dbpassword
3. Enter a 1 for Administrator or a 2 for Customer
4. Enter a valid username and password
5. Enter a number corresponding to a possible action

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
