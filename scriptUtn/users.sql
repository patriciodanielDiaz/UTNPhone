-----------------------------------------------------usuarios----------------------------------------------------------

CREATE user 'infrastructure'@'localhost' identified by '000000';
GRANT INSERT ON utnphone.calls TO 'infrastructure'@'localhost';
FLUSH PRIVILEGES;
-----------------------------------------------------------
CREATE user 'backoffice'@'localhost' identified by '111111';
--user
GRANT SELECT ON utnphone.users TO 'backoffice'@'localhost';
GRANT INSERT ON utnphone.users TO 'backoffice'@'localhost';

GRANT EXECUTE ON PROCEDURE utnphone.sp_update_common_user TO 'backoffice'@'localhost';
--rates
GRANT INSERT ON utnphone.rates TO 'backoffice'@'localhost';
GRANT SELECT ON utnphone.rates TO 'backoffice'@'localhost';
--lines
GRANT SELECT ON utnphone.lines_users TO 'backoffice'@'localhost';
GRANT EXECUTE ON PROCEDURE utnphone.sp_create_line TO 'backoffice'@'localhost';
GRANT EXECUTE ON PROCEDURE utnphone.sp_delete_line TO 'backoffice'@'localhost';
--invoices
GRANT SELECT ON utnphone.lines_users TO 'backoffice'@'localhost';
--calls
GRANT SELECT ON utnphone.calls TO 'backoffice'@'localhost';
FLUSH PRIVILEGES;
-----------------------------------------------------------
CREATE user 'client'@'localhost' identified by '222222';
--users
GRANT SELECT ON utnphone.users TO 'client'@'localhost';
GRANT EXECUTE ON PROCEDURE utnphone.sp_update_common_user TO 'client'@'localhost';
GRANT EXECUTE ON PROCEDURE utnphone.sp_delete_user TO 'client'@'localhost';
--lines
GRANT SELECT ON utnphone.lines_users TO 'client'@'localhost';
--calls
GRANT SELECT ON utnphone.calls TO 'client'@'localhost';
--invoices
GRANT SELECT ON utnphone.invoices TO 'client'@'localhost';
FLUSH PRIVILEGES;