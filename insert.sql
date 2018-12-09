--insert sample records that test triggers and constraints

INSERT INTO ourSysDATE VALUES(SYSDATE);

INSERT INTO customer VALUES('aaaa', '1111', 'Anthony B', null, null);
INSERT INTO customer VALUES('bbbb', '2222', 'Jimmy M', null, null);
INSERT INTO customer VALUES('cccc', '3333', 'Matt V', null, null);

INSERT INTO administrator VALUES('dddd', '4444', 'Panos', null, null);

INSERT INTO product VALUES(1,'p1',null, 'aaaa', SYSDATE, 10, 20, 'under auction', 'bbbb', SYSDATE, 50);
INSERT INTO product VALUES(2,'p2',null, 'aaaa', SYSDATE, 10, 20, 'under auction', 'cccc', SYSDATE, 50);
INSERT INTO product VALUES(3,'p3',null, 'bbbb', SYSDATE, 10, 20, 'under auction', 'cccc', SYSDATE, 50);
INSERT INTO product VALUES(4,'p4',null, 'bbbb', SYSDATE, 10, 20, 'under auction', 'aaaa', SYSDATE, 50);
INSERT INTO product VALUES(5,'p5',null, 'cccc', SYSDATE, 10, 20, 'under auction', 'bbbb', SYSDATE, 50);

INSERT INTO bidlog VALUES(1,1,'cccc',SYSDATE, 40);
INSERT INTO bidlog VALUES(2,1,'bbbb',SYSDATE, 50);
INSERT INTO bidlog VALUES(3,2,'bbbb',SYSDATE, 60);
INSERT INTO bidlog VALUES(4,2,'cccc',SYSDATE, 50);
INSERT INTO bidlog VALUES(5,3,'aaaa',SYSDATE, 60);
INSERT INTO bidlog VALUES(6,3,'cccc',SYSDATE, 50);
INSERT INTO bidlog VALUES(7,4,'cccc',SYSDATE, 40);
INSERT INTO bidlog VALUES(8,4,'aaaa',SYSDATE, 50);
INSERT INTO bidlog VALUES(9,5,'aaaa',SYSDATE, 40);
INSERT INTO bidlog VALUES(10,5,'bbbb',SYSDATE, 50);

INSERT INTO category VALUES('cat1', null);
INSERT INTO category VALUES('cat2', null);
INSERT INTO category VALUES('cat3', 'cat1');
INSERT INTO category VALUES('cat4', 'cat1');
INSERT INTO category VALUES('cat5', 'cat2');

INSERT INTO belongsto VALUES(1,'cat3');
INSERT INTO belongsto VALUES(2,'cat3');
INSERT INTO belongsto VALUES(3,'cat4');
INSERT INTO belongsto VALUES(4,'cat4');
INSERT INTO belongsto VALUES(5,'cat5');

COMMIT;
