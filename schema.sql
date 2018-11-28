--drop all tables
DROP TABLE ourSysDATE CASCADE CONSTRAINTS;
DROP TABLE Customer CASCADE CONSTRAINTS;
DROP TABLE Administrator CASCADE CONSTRAINTS;
DROP TABLE Product CASCADE CONSTRAINTS;
DROP TABLE Bidlog CASCADE CONSTRAINTS;
DROP TABLE Category CASCADE CONSTRAINTS;
DROP TABLE BelongsTo CASCADE CONSTRAINTS;

COMMIT;

-- create tables 
CREATE TABLE ourSysDATE
(
    c_date DATE,
    CONSTRAINT ourSysDATE_PK PRIMARY KEY (c_date)
);

CREATE TABLE Customer
(
    login VARCHAR2(10),
    password VARCHAR2(10),
    name VARCHAR2(20),
    address VARCHAR2(30),
    email VARCHAR2(20),
    CONSTRAINT Customer_PK PRIMARY KEY (login)
);

CREATE TABLE Administrator
(
    login VARCHAR2(10),
    password VARCHAR2(10),
    name VARCHAR2(20),
    address VARCHAR2(30),
    email VARCHAR2(20),
    CONSTRAINT Administrator_PK PRIMARY KEY (login)
);

CREATE TABLE Product
(
    auction_id INT,
    name VARCHAR2(20),
    description VARCHAR2(30),
    seller VARCHAR2(10),
    start_date DATE,
    min_price INT,
    number_of_days INT,
    status VARCHAR2(15) NOT NULL,
    buyer VARCHAR2(10),
    sell_date DATE,
    amount INT,
    CONSTRAINT Product_PK PRIMARY KEY (auction_id),
    CONSTRAINT Product_FK_seller FOREIGN KEY (seller) REFERENCES Customer (login),
    CONSTRAINT Product_FK_buyer FOREIGN KEY (buyer) REFERENCES Customer (login),
    CONSTRAINT Product_status CHECK (status = 'under auction' OR status = 'sold' OR status = 'withdrawn' OR status = 'closed')
);

CREATE TABLE Bidlog
(
    bidsn INT,
    auction_id INT,
    bidder VARCHAR2(10),
    bid_time DATE,
    amount INT, 
    CONSTRAINT Bidlog_PK PRIMARY KEY (bidsn),
    CONSTRAINT Bidlog_FK_auction_id FOREIGN KEY (auction_id) REFERENCES Product (auction_id),
    CONSTRAINT Bidlog_FK_bidder FOREIGN KEY (bidder) REFERENCES Customer (login)
);

CREATE TABLE Category
(
    name VARCHAR2(20),
    parent_category VARCHAR2(20),
    CONSTRAINT Category_PK PRIMARY KEY (name),
    CONSTRAINT Category_FK_parent_category FOREIGN KEY (parent_category) REFERENCES Category (name)
);

CREATE TABLE BelongsTo
(
    auction_id INT,
    category VARCHAR2(20),
    CONSTRAINT BelongsTo_PK PRIMARY KEY (auction_id, category),
    CONSTRAINT BelongsTo_FK_auction_id FOREIGN KEY (auction_id) REFERENCES Product (auction_id), 
    CONSTRAINT BelongsTo_FK_category FOREIGN KEY (category) REFERENCES Category (name)
);

COMMIT;
