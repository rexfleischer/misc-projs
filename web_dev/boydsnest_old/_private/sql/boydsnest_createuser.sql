
GRANT SELECT ON
invalid1_boydsnest.*
TO 'invalid1_bnentry'@'localhost';

DROP USER 'invalid1_bnentry'@'localhost';

CREATE USER
'invalid1_bnentry'@'localhost'
IDENTIFIED BY 's0mePoo22';

GRANT SELECT,INSERT,UPDATE,DELETE ON
invalid1_boydsnest.*
TO 'invalid1_bnentry'@'localhost';