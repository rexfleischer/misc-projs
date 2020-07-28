CREATE USER 'melissa'@'localhost' IDENTIFIED BY 'hey! this is melissa!';

GRANT SELECT,INSERT,UPDATE,DELETE PRIVILEGES ON 
melissadb.* TO 
'melissa'@'localhost';