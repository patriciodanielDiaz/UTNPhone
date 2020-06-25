-------------------------------------creacion de evento facturas -------------------------------------------------
SET GLOBAL event_scheduler = 1;

DELIMITER $$
CREATE EVENT event1
ON SCHEDULE EVERY '1' MONTH
STARTS '2020-07-01 02:00:00'
ON COMPLETION PRESERVE ENABLE
DO
BEGIN

  call sp_generate_all_invoices();

END$$
DELIMITER ;
