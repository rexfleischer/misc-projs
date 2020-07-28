
USE invalid1_boydsnest;


DROP TABLE IF EXISTS scheme;
DROP TABLE IF EXISTS userManual;
DROP TABLE IF EXISTS pageFollowers;
DROP TABLE IF EXISTS pageForums;
DROP TABLE IF EXISTS pageRights;
DROP TABLE IF EXISTS pages;
DROP TABLE IF EXISTS users;



CREATE TABLE users
(
userID          bigint(20) unsigned NOT NULL AUTO_INCREMENT,
username        char(40)        NOT NULL,
password        char(40)        NOT NULL,
salt            char(3)         NOT NULL,
email           char(60)        NOT NULL,
secretQuestion 	char(255),
secretAnswer 	char(255),
masterNotes 	char(255),
schemeUsing     char(40),
canDownload 	tinyint(1) 	DEFAULT 0,
canUpload 	tinyint(1) 	DEFAULT 0,
canMessage 	tinyint(1) 	DEFAULT 0,
canCDSelf 	tinyint(1) 	DEFAULT 0,
canCDOther 	tinyint(1) 	DEFAULT 0,
canScheme       tinyint(1)      DEFAULT 0,
isFamily 	tinyint(1) 	DEFAULT 0,
isLogged 	tinyint(1) 	DEFAULT 0,
isMaster 	tinyint(1) 	DEFAULT 0,
isActive 	tinyint(1) 	DEFAULT 0,
isPermissioned 	tinyint(1) 	DEFAULT 0,
createdWhen 	datetime,
expiresWhen 	datetime,
lastUpdate 	timestamp 	DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
defaultRight 	tinyint(4) 	DEFAULT 0,

PRIMARY KEY(userID),
UNIQUE KEY(username, email),
INDEX(userID, username)
)engine=InnoDB;



CREATE TABLE pageRights
(
rightID     bigint(20) unsigned NOT NULL,
pageID      bigint(20) unsigned NOT NULL,
userID      bigint(20) unsigned NOT NULL,
userRight   tinyint(4)          DEFAULT 0,

PRIMARY KEY(rightID),
INDEX(rightID, pageID, userID),
FOREIGN KEY (userID) REFERENCES users(userID) ON DELETE CASCADE
)engine=InnoDB;



CREATE TABLE userManual
(
pageID      bigint(20) unsigned NOT NULL AUTO_INCREMENT,
content     mediumtext,
title       char(40),
rank        tinyint(4),

PRIMARY KEY(pageID),
INDEX(pageID)
)engine=InnoDB;



CREATE TABLE scheme
(
schemeID                int unsigned    NOT NULL AUTO_INCREMENT,
schemeName              char(40)        NOT NULL,

background_color            char(6)     NOT NULL,
background_color_accent     char(6)     NOT NULL,
background_color_accent_2   char(6)     NOT NULL,
main_menu_link_hover_color  char(6)     NOT NULL,
main_menu_link_color        char(6)     NOT NULL,
text_color                  char(6)     NOT NULL,
text_color_accent           char(6)     NOT NULL,
main_menu_index_pic         char(40)    NOT NULL,

PRIMARY KEY(schemeID),
UNIQUE KEY(schemeName),
INDEX(schemeID, schemeName)
)engine=InnoDB;



insert into users
SET
userID      = 1,
username    = 'guest',
password    = SHA1(CONCAT('asdf', SHA1(''))),
salt        = 'abc',
email       = '',
isActive    = 1,
createdWhen = NOW(),
expiresWhen = NOW(),
defaultRight = 0;

insert into users
SET
userID      = 2,
username    = 'admin',
password    = SHA1(CONCAT('abc', SHA1('sithl0rd'))),
salt        = 'abc',
email       = '',
canDownload = 1,
canUpload   = 1,
canMessage  = 1,
canCDSelf   = 1,
canCDOther  = 1,
canScheme   = 1,
isFamily    = 1,
isLogged    = 1,
isMaster    = 1,
isActive    = 1,
isPermissioned  = 1,
createdWhen  = NOW(),
expiresWhen  = NOW(),
defaultRight = 4;

insert into users
SET
userID      = 3,
username    = 'webmaster',
password    = SHA1(CONCAT('abc', SHA1('sithl0rd'))),
salt        = 'abc',
email       = '',
canDownload = 1,
canMessage  = 1,
isActive    = 1,
isPermissioned  = 1,
createdWhen  = NOW(),
expiresWhen  = NOW(),
defaultRight = 1;

insert into users
SET
userID      = 4,
username    = 'system',
password    = SHA1(CONCAT('asdf', SHA1(''))),
salt        = 'abc',
email       = '',
canMessage  = 1,
isActive    = 1,
isPermissioned  = 1,
createdWhen  = NOW(),
expiresWhen  = NOW(),
defaultRight = 0;

insert into scheme
SET
schemeName                  = 'default',
background_color            = '20b006',
background_color_accent     = 'FFFF00',
background_color_accent_2   = '',
main_menu_link_hover_color  = '00a000',
main_menu_link_color        = '000000',
text_color                  = 'ffffff',
text_color_accent           = '20b006',
main_menu_index_pic         = 'header.jpg';

insert into userManual
SET
pageID  = 1,
content = 'The introduction is empty... blame gregg',
title   = 'Introduction',
rank    = 1;