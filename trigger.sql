--trigger, fucntions, procedures, etc

CREATE OR REPLACE PROCEDURE proc_putProduct;



CREATE OR REPLACE TRIGGER trig_bidTimeUpdate;



CREATE OR REPLACE TRIGGER trig_updateHighBid;
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



CREATE OR REPLACE TRIGGER trig_closeAuctions;
-- to do
after update of current_time on system_time
for each row
begin
  update product 
  set status = 'closed'
  where status = 'underauction' and number_of_days <= :new.current_time;
end;
/


