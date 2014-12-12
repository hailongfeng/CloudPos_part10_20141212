DROP TABLE IF EXISTS "Account";
CREATE TABLE `Account` (`accountID` INTEGER PRIMARY KEY ,`accountPrivilegeID` INTEGER,`firstName` VARCHAR DEFAULT (null) ,`account` VARCHAR,`pass` VARCHAR,`operatorNo` VARCHAR,`accountPhoto` VARCHAR, `lastName` VARCHAR, `sync` BOOL DEFAULT true, `phoneID` INTEGER);
INSERT INTO "Account" VALUES(1,1,'2','13888888888','7C4A8D09CA3762AF61E59520943DC26494F8941B','1',NULL,'Test','true',3);
DROP TABLE IF EXISTS "AccountPrivilege";
CREATE TABLE `AccountPrivilege` (`accountPrivilegeID` INTEGER primary key, `name` VARCHAR, `value` INTEGER );
DROP TABLE IF EXISTS "Address";
CREATE TABLE `Address` (`addressID` INTEGER primary key autoincrement, `addressInfo` VARCHAR, `longitude` INTEGER ,`latitude` INTEGER);
DROP TABLE IF EXISTS "BankCard";
CREATE TABLE `BankCard` (`bankCardId` INTEGER primary key autoincrement, `bankCardNo` VARCHAR, `validThrough` VARCHAR, `cardOwnerName` VARCHAR );
DROP TABLE IF EXISTS "Onsale";
CREATE TABLE `Onsale` (`onsaleID` INTEGER primary key autoincrement, `onsaleType` INTEGER, `onsaleName` VARCHAR, `value` VARCHAR , `enable` INTEGER, `syncFlag` INTEGER);
DROP TABLE IF EXISTS "OrderContent";
CREATE TABLE `OrderContent` (`orderContentID` INTEGER primary key autoincrement, `productID` INTEGER, `orderID` INTEGER, `count` INTEGER, `price` VARCHAR);
DROP TABLE IF EXISTS "OrderContentRemark";
CREATE TABLE `OrderContentRemark` (`orderContentID` INTEGER , `remark` VARCHAR );
DROP TABLE IF EXISTS "OrderContentToAttribute";
CREATE TABLE `OrderContentToAttribute` (`orderContentID` INTEGER, `productSubAttributeId` INTEGER, `price` VARCHAR);
DROP TABLE IF EXISTS "OrderContentToOnsale";
CREATE TABLE `OrderContentToOnsale` (`orderContentID` INTEGER , `onsaleID` INTEGER, `value` VARCHAR );
DROP TABLE IF EXISTS "OrderCustomerInfo";
CREATE TABLE `OrderCustomerInfo` (`orderProcessId` INTEGER primary key, `bankCardId` INTEGER, `signature` VARCHAR, `telephoneNo` VARCHAR, `emailInfo` VARCHAR );
DROP TABLE IF EXISTS "OrderProcess";
CREATE TABLE `OrderProcess` (`orderProcessId` INTEGER primary key autoincrement, `OrderProcessModeId` INTEGER, `orderID` INTEGER, `addressID` INTEGER , `createTime` VARCHAR , `traceNo` VARCHAR,  `referenceNo` VARCHAR, `authorizationNo` VARCHAR, `clearingDate` VARCHAR, `ackNo` VARCHAR, `price` VARCHAR, `issuerbankid` VARCHAR, `acqbank` VARCHAR, `batchid` VARCHAR);
DROP TABLE IF EXISTS "OrderProcessMode";
CREATE TABLE `OrderProcessMode` (`OrderProcessModeId` INTEGER primary key autoincrement, `name` VARCHAR, `value` INTEGER);
DROP TABLE IF EXISTS "OrderRemark";
CREATE TABLE `OrderRemark` (`orderID` INTEGER , `remark` VARCHAR , `sitindex` VARCHAR);
DROP TABLE IF EXISTS "OrderStatus";
CREATE TABLE `OrderStatus` (`orderStatusID` INTEGER primary key, `name` VARCHAR, `value` INTEGER);
DROP TABLE IF EXISTS "OrderToAccount";
CREATE TABLE `OrderToAccount` (`orderID` INTEGER PRIMARY KEY  NOT NULL  UNIQUE , `accountName` VARCHAR);
DROP TABLE IF EXISTS "OrderToOnsale";
CREATE TABLE `OrderToOnsale` (`orderID` INTEGER,`onsaleID` INTEGER, `value` VARCHAR );
DROP TABLE IF EXISTS "Orders";
CREATE TABLE `Orders` (`orderID` INTEGER PRIMARY KEY autoincrement, `billNo` VARCHAR ,`orderStatusID` INTEGER, `createTime` INTEGER , `modifyTime` INTEGER , `syncFlag` INTEGER);
DROP TABLE IF EXISTS "OrdersToPaymentType";
CREATE TABLE `OrdersToPaymentType` (`orderID` INTEGER primary key, `paymentTypeID` INTEGER, `amount` VARCHAR);
DROP TABLE IF EXISTS "PaymentType";
CREATE TABLE `PaymentType` (`paymentTypeID` INTEGER primary key autoincrement, `name` VARCHAR , `value` VARCHAR);
DROP TABLE IF EXISTS "Product";
CREATE TABLE `Product` (`productID` INTEGER primary key, `name` VARCHAR, `productPhoto` VARCHAR, `price` VARCHAR, `productCategoryID` INTEGER, `enable` INTEGER, "syncFlag" INTEGER DEFAULT 0);
DROP TABLE IF EXISTS "ProductAttribute";
CREATE TABLE "ProductAttribute" ("productAttributeID" INTEGER PRIMARY KEY ,"name" VARCHAR,"choiceType" INTEGER , "defaultChoice" INTEGER , `enable` INTEGER, "syncFlag" INTEGER DEFAULT 0);
DROP TABLE IF EXISTS "ProductCategory";
CREATE TABLE `ProductCategory` (`productCategoryID` INTEGER primary key not null, `name` VARCHAR , `enable` INTEGER, "syncFlag" INTEGER DEFAULT 0);
DROP TABLE IF EXISTS "ProductPinyin";
CREATE TABLE `ProductPinyin`  (`productId` INTEGER primary key , `productCategoryID` INTEGER ,`productName` VARCHAR, `firstChar` VARCHAR,`firstCharStr` VARCHAR, `allStr` VARCHAR, `enable` INTEGER );
DROP TABLE IF EXISTS "ProductSubAttribute";
CREATE TABLE `ProductSubAttribute` (`productSubAttributeId` INTEGER primary key not null, `productAttributeID` INTEGER, `name` VARCHAR, `priceAffect` VARCHAR, `enable` INTEGER, "syncFlag" INTEGER DEFAULT 0);
DROP TABLE IF EXISTS "ProductToAttribute";
CREATE TABLE `ProductToAttribute` (`productID` INTEGER , `productAttributeID` VARCHAR , `enable` INTEGER);
DROP TABLE IF EXISTS "ReverseAttribute";
CREATE TABLE `ReverseAttribute` (`reverseAttrId` INTEGER primary key autoincrement, `OrderProcessModeId` INTEGER ,`traceNo` VARCHAR , `authorizationNo` VARCHAR, `price` VARCHAR, `reason` VARCHAR, `icf55` BLOB, `icf55len` INTEGER, `icpan` VARCHAR, `icdate` VARCHAR, `iccsn` VARCHAR);
DROP TABLE IF EXISTS "Settings";
CREATE TABLE `Settings` (`cynovoServerDomain` CHAR, `cynovoServerPort` CHAR, `cardInfoLinkIP` CHAR, `cardInfoLinkPort` CHAR, `serialNumber` CHAR, `traceNo` CHAR);
INSERT INTO "Settings" VALUES('192.168.1.20','80','211.147.64.198','5801','000001','000001');
DROP TABLE IF EXISTS "Store";
CREATE TABLE `Store` (`storeID` INTEGER,`storeName` VARCHAR, `storeAddressID` INTEGER, `storePhoto` INTEGER, `companyAddressID` INTEGER,`companyName` VARCHAR, `companyPhoto` VARCHAR, `nickName` VARCHAR, `address` VARCHAR,`companyAddress` VARCHAR);
INSERT INTO "Store" VALUES(0,'',0,'',0,'','','','','');
DROP TABLE IF EXISTS "android_metadata";
CREATE TABLE android_metadata (locale TEXT);
INSERT INTO "android_metadata" VALUES('zh_CN');
DROP TABLE IF EXISTS "card";
CREATE TABLE `card` (`id` INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL , `card_no` VARCHAR NOT NULL , `member_no` INTEGER NOT NULL , `type_id` INTEGER, `balance` FLOAT, `onsale` FLOAT, `dead_line` DATETIME, `remark` VARCHAR);
DROP TABLE IF EXISTS "membership";
CREATE TABLE `membership` (`card_no` INTEGER PRIMARY KEY  NOT NULL , `shop_id` INTEGER NOT NULL , `name` VARCHAR NOT NULL , `phone` VARCHAR NOT NULL  UNIQUE );