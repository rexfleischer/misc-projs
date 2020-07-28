
USE invalid1_boydsnest;

DROP TABLE IF EXISTS userLog;
DROP TABLE IF EXISTS userManual;
DROP TABLE IF EXISTS errorLog;
DROP TABLE IF EXISTS loginLog;
DROP TABLE IF EXISTS messages;
DROP TABLE IF EXISTS pageFollowers;
DROP TABLE IF EXISTS pageForums;
DROP TABLE IF EXISTS pageRights;
DROP TABLE IF EXISTS dataTypeBase;
DROP TABLE IF EXISTS dtAssociate;
DROP TABLE IF EXISTS dtBlob;
DROP TABLE IF EXISTS dtMBlob;
DROP TABLE IF EXISTS dtText;
DROP TABLE IF EXISTS dtMText;
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
canDownload 	tinyint(1) 	DEFAULT 0,
canUpload 	tinyint(1) 	DEFAULT 0,
canMessage 	tinyint(1) 	DEFAULT 0,
canCDSelf 	tinyint(1) 	DEFAULT 0,
canCDOther 	tinyint(1) 	DEFAULT 0,
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

CREATE TABLE pages
(
pageID      bigint(20) unsigned NOT NULL AUTO_INCREMENT,
userID      bigint(20) unsigned NOT NULL,
childOf     bigint(20) unsigned,
title       char(255)           NOT NULL,
content     mediumtext,
rank        tinyint(4)          DEFAULT 0,
views       int(10)             DEFAULT 0,
forumType   tinyint(4),
isPrivate   tinyint(1)          DEFAULT 0,
hasFollwers tinyint(1)          DEFAULT 0,

PRIMARY KEY(pageID),
INDEX(pageID, userID, childOf, title),
FOREIGN KEY (userID) REFERENCES users(userID) ON DELETE CASCADE,
FOREIGN KEY (pageID) REFERENCES pages(pageID) ON DELETE CASCADE,
FOREIGN KEY (childOf) REFERENCES pages(pageID) ON DELETE CASCADE
)engine=InnoDB;

create table userLog
(
logID       bigint(20) unsigned NOT NULL,
timeLogged  timestamp           DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
userID      bigint(20) unsigned NOT NULL,
message     char(255)           NOT NULL,

PRIMARY KEY(logID),
INDEX(logID)
)engine=InnoDB;

CREATE TABLE userManual
(
pageID      bigint(20) unsigned NOT NULL AUTO_INCREMENT,
content     mediumblob,
title       char(40),
rank        tinyint(4),

PRIMARY KEY(pageID),
INDEX(pageID)
)engine=InnoDB;

CREATE TABLE errorLog
(
logID       bigint(20) unsigned NOT NULL AUTO_INCREMENT,
timeLogged  timestamp           DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
message     char(255)           NOT NULL,

PRIMARY KEY(logID),
INDEX(logID)
)engine=InnoDB;

CREATE TABLE loginLog
(
logID           bigint(20) unsigned NOT NULL AUTO_INCREMENT,
timeLogged      timestamp           DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
userAttempt     char(40)            NOT NULL,
passAttempt     char(40)            NOT NULL,

PRIMARY KEY(logID),
INDEX(logID)
)engine=InnoDB;

CREATE TABLE messages
(
messageID       bigint(20) unsigned NOT NULL AUTO_INCREMENT,
userFrom        bigint(20) unsigned NOT NULL,
userTo          bigint(20) unsigned NOT NULL,
timeSent        timestamp           DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
title           char(40),
message         text,
seen            tinyint(1)          DEFAULT 0,
userFromDeleted tinyint(1)          DEFAULT 0,
userToDeleted 	tinyint(1)          DEFAULT 0,
function        char(30),
extra           char(255),

PRIMARY KEY(messageID),
INDEX(messageID, userFrom, userTo),
FOREIGN KEY (userFrom) REFERENCES users(userID) ON DELETE CASCADE,
FOREIGN KEY (userTo) REFERENCES users(userID) ON DELETE CASCADE
)engine=InnoDB;

CREATE TABLE pageFollowers
(
pageID  bigint(20) unsigned NOT NULL,
userID 	bigint(20) unsigned NOT NULL,

INDEX(pageID, userID),
FOREIGN KEY (userID) REFERENCES users(userID) ON DELETE CASCADE,
FOREIGN KEY (pageID) REFERENCES pages(pageID) ON DELETE CASCADE
)engine=InnoDB;

CREATE TABLE pageForums
(
commentID   bigint(20) unsigned NOT NULL AUTO_INCREMENT,
childOf     bigint(20) unsigned NOT NULL,
pageID      bigint(20) unsigned NOT NULL,
userID      bigint(20) unsigned NOT NULL,
comment     text,
timeSaid    timestamp           DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

PRIMARY KEY(commentID),
INDEX(commentID, pageID, userID),
FOREIGN KEY (userID) REFERENCES users(userID) ON DELETE CASCADE,
FOREIGN KEY (pageID) REFERENCES pages(pageID) ON DELETE CASCADE,
FOREIGN KEY (childOf) REFERENCES pages(pageID) ON DELETE CASCADE
)engine=InnoDB;

CREATE TABLE pageRights
(
pageID      bigint(20) unsigned NOT NULL,
userID      bigint(20) unsigned NOT NULL,
userRight   tinyint(4)          DEFAULT 1,

INDEX(pageID, userID),
FOREIGN KEY (userID) REFERENCES users(userID) ON DELETE CASCADE,
FOREIGN KEY (pageID) REFERENCES pages(pageID) ON DELETE CASCADE
)engine=InnoDB;

CREATE TABLE dataTypeBase
(
dtID        bigint(20) unsigned NOT NULL,
dataType    char(40)    NOT NULL,
title       char(40),
timeMade    datetime,
lastUpdate  timestamp   DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
header      char(255),

PRIMARY KEY(dtID),
INDEX(dtID)
)engine=InnoDB;

CREATE TABLE dtAssociate
(
dtID            bigint(20) unsigned NOT NULL,
ownerTable      char(40)            NOT NULL,
ownerID         bigint(20) unsigned NOT NULL,

PRIMARY KEY(dtID),
INDEX(dtID)
)engine=InnoDB;

CREATE TABLE dtBlob
(
dtID        bigint(20) unsigned NOT NULL,
content     blob                NOT NULL,

PRIMARY KEY(dtID),
INDEX(dtID)
)engine=InnoDB;

CREATE TABLE dtMBlob
(
dtID        bigint(20) unsigned NOT NULL,
content     mediumblob          NOT NULL,

PRIMARY KEY(dtID),
INDEX(dtID)
)engine=InnoDB;

CREATE TABLE dtText
(
dtID        bigint(20) unsigned NOT NULL,
content     text                NOT NULL,

PRIMARY KEY(dtID),
INDEX(dtID)
)engine=InnoDB;

CREATE TABLE dtMText
(
dtID        bigint(20) unsigned NOT NULL,
content     blob                NOT NULL,

PRIMARY KEY(dtID),
INDEX(dtID)
)engine=InnoDB;

insert into users
SET
userID      = 1,
username    = 'Guest',
password    = '',
salt        = '',
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

insert into pages
SET
pageID      = 0,
userID      = 1,
childOf     = 0,
title       = 'root';