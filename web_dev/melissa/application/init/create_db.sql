DROP DATABASE IF EXISTS melissadb;

CREATE DATABASE melissadb;

USE melissadb;

CREATE TABLE users
(
userid          bigint(20) unsigned NOT NULL AUTO_INCREMENT,
username        char(40)        NOT NULL,
password        char(40)        NOT NULL,
salt            char(3)         NOT NULL,
createdWhen 	datetime,

is_admin        tinyint(1) 	DEFAULT 0,

PRIMARY KEY(userid),
UNIQUE KEY(username),
INDEX(userid, username)

)engine=InnoDB;



CREATE TABLE page
(
name VARCHAR(20) NOT NULL,
data MEDIUMTEXT NOT NULL,

PRIMARY KEY (name)

)engine=InnoDB;




insert into users
SET
userid      = 1,
username    = 'melissa',
password    = SHA1(CONCAT('abc', SHA1('passw0rd'))),
salt        = 'abc',
createdWhen = NOW(),

is_admin    = 1;