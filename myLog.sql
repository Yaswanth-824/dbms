--         USER       --
CREATE TABLE users (
    user_id BIGINT PRIMARY KEY AUTO_INCREMENT,   
    username VARCHAR(50) NOT NULL UNIQUE,       
    email_id VARCHAR(100) NOT NULL UNIQUE,      
    fname VARCHAR(50),                           
    lname VARCHAR(50),                           
    gender VARCHAR(10) CHECK (gender IN ('Male', 'Female', 'Other')),
    password VARCHAR(255) NOT NULL,              
    dob DATE,                                   
    phone_number VARCHAR(15),                   
    role VARCHAR(50) DEFAULT 'USER'
);
--           Places     ---
CREATE TABLE travelPlaces (
    placeId BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(50),
    description VARCHAR(10000),
    cost BIGINT,
    noOfDays INT
);
CREATE TABLE placeImages (
    placeId BIGINT,
    image VARCHAR(255),
    PRIMARY KEY (placeId, image),
    FOREIGN KEY (placeId) REFERENCES travelPlaces(placeId)
);
USE Travels;
show Databases;
use Travel;
--      Reviews --

Create Table reviews(
	reviewId BIGINT Primary Key AUTO_INCREMENT,
    username Varchar(50),
    placeId BIGINT,
    description VarChar(10000),
    rating float Check (rating <= 5),
    Foreign Key (username) References bookings(uid),
    Foreign Key (placeId) References travelPlaces(placeId)
);

CREATE TABLE Hotels (
    HID INT PRIMARY KEY AUTO_INCREMENT,
    HName VARCHAR(100),
    HPhone VARCHAR(20),
    HLocation VARCHAR(255),
    placeId BIGINT,
    Foregin Key
); 

CREATE TABLE Residency (
    ResidencyID BIGINT PRIMARY KEY AUTO_INCREMENT,
	username varchar(50),  -- Foreign key for Users
    HID INT,     -- Foreign key for Hotels
    FOREIGN KEY (username) REFERENCES users(username) ON DELETE CASCADE,
    FOREIGN KEY (HID) REFERENCES Hotels(HID) ON DELETE CASCADE
);

CREATE TABLE Rooms (
    RoomID INT PRIMARY KEY AUTO_INCREMENT,
    HID INT,  -- Foreign key for Hotels
    RPrice DECIMAL(10, 2),
    RStatus VARCHAR(50),
    FOREIGN KEY (HID) REFERENCES Hotels(HID) ON DELETE CASCADE
    
);

CREATE TABLE Room_Bookings (
    RBID INT PRIMARY KEY AUTO_INCREMENT,
    RoomID INT,  -- Foreign key for Rooms
    username varchar(50),
    TotalDays INT,
    BookingStatus VARCHAR(50),
    FOREIGN KEY (RoomID) REFERENCES Rooms(RoomID) ON DELETE CASCADE,
    FOREIGN KEY (username)  references users(username)
    --
);

Alter table Room_Bookings add column username varchar(50) references users(username);

CREATE TABLE bookings (
    bid INT PRIMARY KEY AUTO_INCREMENT,       
    uid VARCHAR(50),  
    placeId varchar(50) references travelPlaces(placeId),
    bstatus VARCHAR(50),                      
    FOREIGN KEY (uid) REFERENCES users(username) 
);
Alter table bookings add column placeId varchar(50) references travelPlaces(placeId);
Alter table bookings drop column uid;
Alter table bookings add column bookingdate Date;

CREATE TABLE payments (
    pid INT PRIMARY KEY AUTO_INCREMENT,           
    bid INT,                                      
    username VARCHAR(50),                              
    status VARCHAR(50),                           
    mode_of_payment VARCHAR(50),                  
    payment_date DATE,                            
    FOREIGN KEY (bid) REFERENCES bookings(bid),   
    FOREIGN KEY (username) REFERENCES users(username)  
);

CREATE TABLE cancellation (
    cid INT PRIMARY KEY AUTO_INCREMENT,        
    pid INT,                                   
    cancel_date DATE,                          
    reason VARCHAR(255),                       
    refundable_amount DECIMAL(10, 2),          
    FOREIGN KEY (pid) REFERENCES payments(pid) 
);

select * from bookings;
Drop table reviews;
select * from reviews;
Alter Table reviews drop bid;



