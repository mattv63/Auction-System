--trigger, fucntions, procedures, etc

CREATE OR REPLACE PROCEDURE proc_putProduct
(
    pAuction_ID out int,
    pName in varchar2,
    pDesciption in varchar2,
    pSeller in varchar2,
    pMin_price in int,
    pNumber_of_days in int
)
is
    pStart_date date;
begin
    select max(auction_ID) into pAuction_ID from Product;
    pAuction_ID := pAuction_ID + 1;
    
    select max(c_date) into pStart_date from ourSysDate;
    
    insert into Product(auction_id, name, desciption, seller, start_date, min_price, number_of_days, status)
    values(pAuction_ID, pName, pDescription, pSeller, pStart_date, pMin_price, pNumber_of_days, 'in auction');
end proc_putProduct;

-- When new bid is inserted, advances system time by five seconds.  
CREATE OR REPLACE TRIGGER trig_bidTimeUpdate
after insert on bidlog
declare new_date date;
begin
    select(max(c_date) + (5/24/60/60)) into new_date from ourSysDATE;
    update ourSysDATE
    set c_date = to_date(new_date, 'DD-MON-YYYY/HH12:MI:SS:AM');
end;
/

CREATE OR REPLACE TRIGGER trig_updateHighBid
after insert on bidlog
for each row
begin
  update product
  set amount = :new.amount
  where auction_id = :new.auction_id;
end;
/


--counts the number of products sold in the past (mon) months for specific categories (cat) 
CREATE OR REPLACE FUNCTION func_productCount(mon INT, cat VARCHAR2)
    RETURN INT
IS       
    currDate DATE;
    prodSold INT;
BEGIN
    SELECT c_date
    INTO currDate
    FROM ourSysDATE
    WHERE ROWNUM = 1
    ORDER BY c_date DESC;
    
    SELECT COUNT(p.auction_id)
    INTO prodSold
    FROM Product p JOIN BelongsTo b ON p.auction_id = b.auction_id
    WHERE b.category = cat
    AND p.status = 'sold'
    AND p.sell_date >= add_months(currDate, -mon);
    
    RETURN prodSold;
END;
/

--counts the number of bids a specific user (user) has placed in the past (mon) months 
CREATE OR REPLACE FUNCTION func_bidCount(mon INT, user VARCHAR2)
    RETURN INT
IS
    currDate DATE;
    numBids INT;
BEGIN
    SELECT  c_date
    INTO currDate
    FROM ourSysDATE
    WHERE ROWNUM = 1
    ORDER BY c_date DESC;
    
    SELECT COUNT(b.bidsn)
    INTO numBids
    FROM bidlog b, customer c
    WHERE b.bidder = c.login
    AND c.login = user 
    AND b.bid_time >= add_months(currDate, -mon);

    RETURN numBids;
END;
/


--calculates the total dollar amount a specific user (user) has spent in the past (mon) months, 
CREATE OR REPLACE FUNCTION func_buyingAmount(mon INT, user VARCHAR2)
    RETURN INT
IS
    currDate DATE;
    amountSpent INT;
BEGIN
        
    SELECT  c_date
    INTO currDate
    FROM ourSysDATE
    WHERE ROWNUM = 1
    ORDER BY c_date DESC;
    
    SELECT SUM(p.amount)
    INTO amountSpent
    FROM product p
    WHERE p.buyer = user 
    AND p.sell_date >= add_months(currDate, -mon);

    RETURN amountSpent;
END;
/


CREATE OR REPLACE TRIGGER trig_closeAuctions
after update of c_date on ourSysDATE
for each row
begin
  update product 
  set status = 'closed'
  where status = 'in auction' and start_date + number_of_days <= :new.c_date;
end;
/


