--trigger, fucntions, procedures, etc
drop sequence seq1;
drop sequence seq2;
--where categories will be collected upon insert of product
create or replace type vcarray as table of varchar2(20);
/

create sequence seq1 start with 1 increment by 1 nomaxvalue;
create sequence seq2 start with 1 increment by 1 nomaxvalue;


-- Procedure to add a new product to the DB
CREATE OR REPLACE PROCEDURE proc_putProduct
(
    pName in varchar2,
    pDescription in varchar2,
    pCategories in vcarray,
    pNumber_of_days in int,
    pSeller in varchar2,
    pMin_price in int,
    pAuction_ID out int
)
is
    pStart_date date;
    pSell_date date;
    cat_count int;
begin
    select max(auction_ID) into pAuction_ID from Product;
    pAuction_ID := pAuction_ID + 1;

    select max(c_date) into pStart_date from ourSysDate;

    pSell_date := pStart_date + pNumber_of_days; -- added so sell date will be end of auction date

    insert into Product(auction_id, name, description, seller, start_date, min_price, number_of_days, status, buyer, sell_date, amount)
    values(pAuction_ID, pName, pDescription, pSeller, pStart_date, pMin_price, pNumber_of_days, 'under auction', null, pSell_date, pMin_price);

    -- checks if category is a leaf and then inserts into belongsto table
    for i in 1..pCategories.count loop
        select count(name) into cat_count from Category where pCategories(i) = Category.name or pCategories(i) = Category.parent_category;
        if cat_count = 1
        then insert into BelongsTo values(pAuction_ID, pCategories(i));
        end if;
    end loop;

    return;

end proc_putProduct;
/

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

-- trigger to update the highest bid in the Product
CREATE OR REPLACE TRIGGER trig_updateHighBid
after insert on bidlog
for each row
begin
  update product
  set amount = :new.amount
  where auction_id = :new.auction_id;
end;
/


-- trigger to make new bidsn when row added to bidlog
CREATE OR REPLACE TRIGGER increment_bidsn
before INSERT ON bidlog
FOR EACH ROW
BEGIN
  select seq1.NEXTVAL
  into :new.bidsn
  from dual;
END;
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

-- sets status of an auction to closed when the system date goes past the number_of_days for the auction
CREATE OR REPLACE TRIGGER trig_closeAuctions
after update of c_date on ourSysDATE
for each row
begin
  update product
  set status = 'closed'
  where status = 'in auction' and start_date + number_of_days <= :new.c_date;
end;
/

COMMIT;
