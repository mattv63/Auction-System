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

CREATE OR REPLACE TRIGGER trig_updateHighBid
-- to do
after insert on bidlog
for each row
begin
  update product
  set amount = :new.amount
  where auction_id = :new.auction_id;
end;
/


CREATE OR REPLACE FUNCTION func_productCount; 



CREATE OR REPLACE FUNCTION func_bidCount;



CREATE OR REPLACE FUNCTION func_buyingAmount;



CREATE OR REPLACE TRIGGER trig_closeAuctions
-- to do
after update of current_time on system_time
for each row
begin
  update product 
  set status = 'closed'
  where status = 'underauction' and number_of_days <= :new.current_time;
end;
/


