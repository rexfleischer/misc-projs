

USE invalid1_boydsnest;

DROP TABLE IF EXISTS fcore_counter;
DROP TABLE IF EXISTS fcore_log;
DROP TABLE IF EXISTS fcore_message_tos;
DROP TABLE IF EXISTS fcore_message_owners;
DROP TABLE IF EXISTS fcore_messages;
DROP TABLE IF EXISTS fcore_forum_post;
DROP TABLE IF EXISTS fcore_forum;
DROP TABLE IF EXISTS dtBlob;
DROP TABLE IF EXISTS dtMBlob;
DROP TABLE IF EXISTS dtText;
DROP TABLE IF EXISTS dtMText;
DROP TABLE IF EXISTS dataTypeBase;


CREATE TABLE fcore_counter
(
countID     int unsigned    NOT NULL AUTO_INCREMENT,
name        char(20)        NOT NULL,
count       int unsigned    DEFAULT 0,
time        timestamp       DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

PRIMARY KEY(countID),
UNIQUE KEY(name),
INDEX(countID, name)
)engine=InnoDB;



CREATE TABLE fcore_log
(
logID       int unsigned    NOT NULL AUTO_INCREMENT,
origin      char(20)        NOT NULL,
time        timestamp       DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
message     char(255)       NOT NULL,

PRIMARY KEY(logID),
INDEX(logID, origin, time)
)engine=InnoDB;




















CREATE TABLE dataTypeBase
(
dtID        int unsigned    NOT NULL    AUTO_INCREMENT,
origin_id   int unsigned    NOT NULL,
origin_type char(40)        NOT NULL,
dataType    char(40)        NOT NULL,
timeMade    datetime,
lastUpdate  timestamp       DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
metaData    char(255),

PRIMARY KEY(dtID),
INDEX(dtID, origin_type, origin_id, timeMade, lastUpdate)
)engine=InnoDB;


CREATE TABLE dtBlob
(
dtID        int unsigned    NOT NULL,
content     blob            NOT NULL,

PRIMARY KEY(dtID),
INDEX(dtID),
FOREIGN KEY (dtID) REFERENCES dataTypeBase(dtID) ON DELETE CASCADE
)engine=InnoDB;


CREATE TABLE dtMBlob
(
dtID        int unsigned    NOT NULL,
content     mediumblob      NOT NULL,

PRIMARY KEY(dtID),
INDEX(dtID),
FOREIGN KEY (dtID) REFERENCES dataTypeBase(dtID) ON DELETE CASCADE
)engine=InnoDB;


CREATE TABLE dtText
(
dtID        int unsigned    NOT NULL,
content     text            NOT NULL,

PRIMARY KEY(dtID),
INDEX(dtID),
FOREIGN KEY (dtID) REFERENCES dataTypeBase(dtID) ON DELETE CASCADE
)engine=InnoDB;


CREATE TABLE dtMText
(
dtID        int unsigned    NOT NULL,
content     blob            NOT NULL,

PRIMARY KEY(dtID),
INDEX(dtID),
FOREIGN KEY (dtID) REFERENCES dataTypeBase(dtID) ON DELETE CASCADE
)engine=InnoDB;




















CREATE TABLE fcore_messages
(
message_id      int unsigned    NOT NULL AUTO_INCREMENT,
time_sent       timestamp       DEFAULT CURRENT_TIMESTAMP,
title           char(40),
message         text,
function        char(255),
origin_type     char(40),
origin_id       int unsigned    NOT NULL,

PRIMARY KEY(message_id),
INDEX(message_id, time_sent, origin_type, origin_id)
)engine=InnoDB;


CREATE TABLE fcore_message_tos
(
message_id      int unsigned    NOT NULL,
origin_type     char(40)        NOT NULL,
origin_id       int unsigned    NOT NULL,
seen            tinyint(1)      DEFAULT 0,

INDEX(message_id, origin_id),
FOREIGN KEY (message_id) REFERENCES fcore_messages(message_id) ON DELETE CASCADE
)engine=InnoDB;


CREATE TABLE fcore_message_owners
(
message_id      int unsigned    NOT NULL,
origin_type     char(40)        NOT NULL,
origin_id       int unsigned    NOT NULL,

INDEX(message_id, origin_id),
FOREIGN KEY (message_id) REFERENCES fcore_messages(message_id) ON DELETE CASCADE
)engine=InnoDB;




















CREATE TABLE fcore_forum
(
forumID         int unsigned    NOT NULL AUTO_INCREMENT,
postDataType    char(40),
timeMade        datetime,
lastUsed        timestamp       DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
origin_type     char(40),
origin_id       int unsigned,
metadata        text,


PRIMARY KEY(forumID),
INDEX(forumID, origin_type, origin_id)
)engine=InnoDB;


CREATE TABLE fcore_forum_post
(
postID      int unsigned    NOT NULL AUTO_INCREMENT,
forumID     int unsigned    NOT NULL,
post_parent int unsigned    NOT NULL,
post_order  tinyint         DEFAULT 0,
origin_type char(40),
origin_id   int unsigned,
timeMade    timestamp       DEFAULT CURRENT_TIMESTAMP,
metadata    char(255),

PRIMARY KEY(postID),
INDEX(postID, forumID, origin_type, origin_id, post_parent),
FOREIGN KEY (forumID) REFERENCES fcore_forum(forumID) ON DELETE CASCADE
)engine=InnoDB;