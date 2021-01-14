DROP DATABASE IF EXISTS eshop_database;

CREATE DATABASE eshop_database
CHARACTER SET utf8
COLLATE utf8_general_ci;

USE eshop_database;

CREATE TABLE Users(
User_ID int AUTO_INCREMENT,
Login varchar(45) NOT NULL UNIQUE,
First_Name varchar(30),
Last_Name varchar(30) NOT NULL,
Email varchar(45) NOT NULL UNIQUE,
Password varchar(255) NOT NULL,
Avatar_ref varchar(255) NOT NULL DEFAULT 'ava-def.jpeg',
Notification boolean DEFAULT false,
PRIMARY KEY (User_ID));

CREATE TABLE Roles(
Role_ID int AUTO_INCREMENT,
Role_Name varchar(45) NOT NULL UNIQUE,
PRIMARY KEY(Role_ID));

CREATE TABLE Role_Users(
Role_Users_ID int AUTO_INCREMENT,
User_ID int NOT NULL,
Role_ID int NOT NULL,
PRIMARY KEY(Role_Users_ID),
FOREIGN KEY(User_ID) REFERENCES Users(User_ID)
ON DELETE CASCADE,
FOREIGN KEY(Role_ID) REFERENCES Roles(Role_ID)
ON DELETE CASCADE);

CREATE TABLE Products(
Product_ID int AUTO_INCREMENT,
Product_Name varchar(255) NOT NULL,
Category_ID int NOT NULL,
Producer_ID int NOT NULL,
Product_Price double NOT NULL,
Product_Count int DEFAULT 0,
PRIMARY KEY(Product_ID));

CREATE TABLE Product_Categories(
Category_ID int AUTO_INCREMENT,
Category_Name varchar(255) NOT NULL UNIQUE,
PRIMARY KEY(Category_ID));

CREATE TABLE Product_Producers(
Producer_ID int AUTO_INCREMENT,
Producer_Name varchar(255) NOT NULL UNIQUE,
PRIMARY KEY(Producer_ID));

CREATE TABLE Product_Descriptions(
Description_ID int AUTO_INCREMENT,
Product_ID int NOT NULL,
Description_Body varchar(1000) NOT NULL,
PRIMARY KEY(Description_ID),
FOREIGN KEY (Product_ID) REFERENCES Products(Product_ID)
ON DELETE CASCADE);

/*Orders*/
CREATE TABLE Order_Statuses(
Order_Status_ID int AUTO_INCREMENT,
Order_Status_Name varchar(45) NOT NULL,
PRIMARY KEY(Order_Status_ID)
);

CREATE TABLE Order_Details(
Order_Detail_ID int AUTO_INCREMENT,
Order_Detail_Message varchar(255) NOT NULL UNIQUE,
PRIMARY KEY(Order_Detail_ID)
);

CREATE TABLE Order_Payment_Types(
Order_Payment_Type_ID int AUTO_INCREMENT,
Order_Payment_Type_Name varchar(45) NOT NULL,
PRIMARY KEY(Order_Payment_Type_ID)
);


CREATE TABLE Orders(
Order_ID int AUTO_INCREMENT,
Order_Status_ID int NOT NULL,
Order_Date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
User_ID int NOT NULL,
Order_Detail_ID int NOT NULL,
Order_Payment_Type_ID int NOT NULL,
PRIMARY KEY(Order_ID),
FOREIGN KEY(Order_Status_ID) REFERENCES Order_Statuses(Order_Status_ID),
FOREIGN KEY(User_ID) REFERENCES Users(User_ID),
FOREIGN KEY(Order_Detail_ID) REFERENCES Order_Details(Order_Detail_ID),
FOREIGN KEY(Order_Payment_Type_ID) REFERENCES Order_Payment_Types(Order_Payment_Type_ID)
);

CREATE TABLE Order_Product_Sets(
Order_Product_Set_ID int AUTO_INCREMENT,
Order_ID int NOT NULL,
Product_ID int NOT NULL,
Product_Count int NOT NULL DEFAULT 1,
PRIMARY KEY(Order_Product_Set_ID),
FOREIGN KEY(Order_ID) REFERENCES Orders(Order_ID)
ON DELETE CASCADE
);

/*Filling User table*/
INSERT INTO Users (Login,First_Name,Last_Name,Email,Password,Avatar_ref,Notification) VALUES("filla","Mikle","Donavan","mikle@mail.com","mikle",'ava-def.jpeg',true);
INSERT INTO Users (Login,First_Name,Last_Name,Email,Password,Avatar_ref,Notification) VALUES("nike","Josh","McMayer","josh@mail.com","josh",'ava-def.jpeg',true);
INSERT INTO Users (Login,First_Name,Last_Name,Email,Password,Avatar_ref,Notification) VALUES("vor","Kory","Belouer","kory@mail.com","kory",'ava-def.jpeg',false);

/*Filling Roles table*/
INSERT INTO Roles(Role_Name) VALUES("admin");
INSERT INTO Roles(Role_Name) VALUES("user");

/*Filling Role_Users table*/
INSERT INTO Role_Users(User_ID,Role_ID) VALUES(1,1);
INSERT INTO Role_Users(User_ID,Role_ID) VALUES(2,2);
INSERT INTO Role_Users(User_ID,Role_ID) VALUES(3,1);
INSERT INTO Role_Users(User_ID,Role_ID) VALUES(3,2);

/*Filling Product_Categories table*/
INSERT INTO Product_Categories (Category_Name) VALUES("phone");
INSERT INTO Product_Categories (Category_Name) VALUES("tv");

/*Filling Product_Producers table*/
INSERT INTO Product_Producers (Producer_Name) VALUES("LG");
INSERT INTO Product_Producers (Producer_Name) VALUES("Samsung");
INSERT INTO Product_Producers (Producer_Name) VALUES("Meizu");

/*Filling Products table*/
INSERT INTO Products (Product_Name,Producer_ID,Category_ID,Product_Price,Product_Count)
VALUES("Lopata",3,1,171.5,1);
INSERT INTO Products (Product_Name,Producer_ID,Category_ID,Product_Price,Product_Count)
VALUES("Yashik",3,2,464.55,23);
INSERT INTO Products (Product_Name,Producer_ID,Category_ID,Product_Price,Product_Count)
VALUES("Topor",3,2,86.55,23);
INSERT INTO Products (Product_Name,Producer_ID,Category_ID,Product_Price,Product_Count)
VALUES("Pol",3,2,47.55,23);
INSERT INTO Products (Product_Name,Producer_ID,Category_ID,Product_Price,Product_Count)
VALUES("Telephon",3,2,111.55,23);
INSERT INTO Products (Product_Name,Producer_ID,Category_ID,Product_Price,Product_Count)
VALUES("Ezhik",3,2,89.55,23);
INSERT INTO Products (Product_Name,Producer_ID,Category_ID,Product_Price,Product_Count)
VALUES("Matsugi",3,2,67.55,23);
INSERT INTO Products (Product_Name,Producer_ID,Category_ID,Product_Price,Product_Count)
VALUES("Yorishi",3,1,98.5,1);
INSERT INTO Products (Product_Name,Producer_ID,Category_ID,Product_Price,Product_Count)
VALUES("Pakunok",3,2,55.55,23);
INSERT INTO Products (Product_Name,Producer_ID,Category_ID,Product_Price,Product_Count)
VALUES("Vila",2,2,86.81,23);
INSERT INTO Products (Product_Name,Producer_ID,Category_ID,Product_Price,Product_Count)
VALUES("Sovok",2,2,325.12,23);
INSERT INTO Products (Product_Name,Producer_ID,Category_ID,Product_Price,Product_Count)
VALUES("Kirpich",1,2,167.55,23);
INSERT INTO Products (Product_Name,Producer_ID,Category_ID,Product_Price,Product_Count)
VALUES("Lom",1,2,89.55,23);
INSERT INTO Products (Product_Name,Producer_ID,Category_ID,Product_Price,Product_Count)
VALUES("Telik",1,2,345.55,23);


/*Filling Product_Descriptions table*/
INSERT INTO Product_Descriptions (Description_Body, Product_ID) VALUES("This product could impress you!",1);
INSERT INTO Product_Descriptions (Description_Body, Product_ID) VALUES("This product could impress you so much!",2);
INSERT INTO Product_Descriptions (Description_Body, Product_ID) VALUES("This product could impress you so much!",3);
INSERT INTO Product_Descriptions (Description_Body, Product_ID) VALUES("This product could impress you so much!",4);
INSERT INTO Product_Descriptions (Description_Body, Product_ID) VALUES("This product could impress you so much!",5);
INSERT INTO Product_Descriptions (Description_Body, Product_ID) VALUES("This product could impress you so much!",6);
INSERT INTO Product_Descriptions (Description_Body, Product_ID) VALUES("This product could impress you so much!",7);
INSERT INTO Product_Descriptions (Description_Body, Product_ID) VALUES("This product could impress you!",8);
INSERT INTO Product_Descriptions (Description_Body, Product_ID) VALUES("This product could impress you so much!",9);
INSERT INTO Product_Descriptions (Description_Body, Product_ID) VALUES("This product could impress you so much!",10);
INSERT INTO Product_Descriptions (Description_Body, Product_ID) VALUES("This product could impress you so much!",11);
INSERT INTO Product_Descriptions (Description_Body, Product_ID) VALUES("This product could impress you so much!",12);
INSERT INTO Product_Descriptions (Description_Body, Product_ID) VALUES("This product could impress you so much!",13);
INSERT INTO Product_Descriptions (Description_Body, Product_ID) VALUES("This product could impress you so much!",14);

/*Filling of Order_Statuses*/
INSERT INTO Order_Statuses(Order_Status_Name) VALUES("accepted");
INSERT INTO Order_Statuses(Order_Status_Name) VALUES("canceled");
INSERT INTO Order_Statuses(Order_Status_Name) VALUES("confirmed");
INSERT INTO Order_Statuses(Order_Status_Name) VALUES("in_progress");
INSERT INTO Order_Statuses(Order_Status_Name) VALUES("sent_out");
INSERT INTO Order_Statuses(Order_Status_Name) VALUES("finished");

/*Filling of Order_Details*/
INSERT INTO Order_Details(Order_Detail_Message) VALUES("Order sent out by client");
INSERT INTO Order_Details(Order_Detail_Message) VALUES("Canceled at the request of the bank");
INSERT INTO Order_Details(Order_Detail_Message) VALUES("Canceled at the request of the police");

/*Filling of Order_Payment_Types */
INSERT INTO Order_Payment_Types(Order_Payment_Type_Name) VALUES("card");
INSERT INTO Order_Payment_Types(Order_Payment_Type_Name) VALUES("apple pay");

/*Filling of Orders*/
INSERT INTO Orders(Order_Status_ID,User_ID,Order_Detail_ID,Order_Payment_Type_ID) VALUES(1,1,1,1);


/*Filling of Orders*/
INSERT INTO Orders(Order_Status_ID,User_ID,Order_Detail_ID,Order_Payment_Type_ID) VALUES(1,1,1,1);

/*Filling of Order_Product_Sets*/
INSERT INTO Order_Product_Sets(Order_ID,Product_ID,Product_Count) VALUES(1,1,3);
INSERT INTO Order_Product_Sets(Order_ID,Product_ID,Product_Count) VALUES(1,2,3);
INSERT INTO Order_Product_Sets(Order_ID,Product_ID,Product_Count) VALUES(1,3,6);
INSERT INTO Order_Product_Sets(Order_ID,Product_ID,Product_Count) VALUES(1,4,2);

