DELIMITER $$
--
-- Procedimientos
--
DROP PROCEDURE IF EXISTS `crear_llamadas_de_patricio_a_mariano`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `crear_llamadas_de_patricio_a_mariano` (`pvueltas` INT)  BEGIN

    DECLARE vCount int DEFAULT 0;
    DECLARE vduration TIME;
    DECLARE vdate DATETIME;

    ciclo:LOOP 
    			if(vCount>pvueltas) 
				then LEAVE ciclo;
				end if;

				select (SEC_TO_TIME(ROUND( RAND() * (3540-0) + 0 ))) into vduration;
				select (CURRENT_TIMESTAMP - INTERVAL FLOOR(RAND() * 365) DAY) into vdate;
				INSERT INTO `calls`(`origincall`, `destinationcall`,`durationtime`,`create_at`) 
				VALUES (1,2,vduration,vdate);

				set vCount =vCount+1;
	end loop;
END$$

DROP PROCEDURE IF EXISTS `sp_create_line`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `sp_create_line` (IN `pIdUser` INT, IN `pTypeLine` INT, OUT `pidLine` INT)  BEGIN

	Declare v_idCity int;
	Declare v_number VARCHAR(50);
	
    SET SESSION TRANSACTION ISOLATION LEVEL REPEATABLE READ;
	select idcity into v_idCity from users where id=pIdUser;

	select concat(f_type_line_number(pTypeLine,v_idCity),f_generate_num()) into v_number;

	INSERT INTO lines_users(`linenumber`, `idtype`, `iduser`) 
	VALUES (v_number,pTypeLine,pIdUser);

	set pidLine = LAST_INSERT_ID();
    COMMIT;
END$$

DROP PROCEDURE IF EXISTS `sp_delete_line`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `sp_delete_line` (IN `pIdLine` VARCHAR(50), OUT `pIdRemove` INT)  BEGIN
	
	if (pIdLine = 0) then
		SIGNAL sqlstate '11111' SET MESSAGE_TEXT = 'line does not exist', 			MYSQL_ERRNO = 9999;
	else 
		UPDATE lines_users SET is_available= 0
		WHERE idline=pIdLine;
        
        call sp_generate_invoice_line (pIdLine);
        
        set pIdRemove=pidLine;
		
	end if;
    
END$$

DROP PROCEDURE IF EXISTS `sp_delete_user`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `sp_delete_user` (IN `pIduser` INT)  NO SQL
BEGIN

		DECLARE vIdLine int;
      	DECLARE vFinished int DEFAULT 0;
        
		DECLARE cur_lines CURSOR FOR select li.idline from lines_users li where li.iduser =pIduser;
	
		DECLARE CONTINUE HANDLER FOR NOT FOUND SET vFinished = 1;
       SET SESSION TRANSACTION ISOLATION LEVEL REPEATABLE READ;
        open cur_lines;
        FETCH cur_lines INTO vIdLine;
        WHILE (vFinished=0) DO
        
		call sp_generate_invoice_line(vIdLine);
        update lines_users set is_available=0 where idline=vIdLine; 
   
        FETCH cur_lines INTO vIdLine;
        END while;
    	
        close cur_lines;
       update users set is_available=0 where id=pIduser;
       commit;
 end$$

DROP PROCEDURE IF EXISTS `sp_generate_all_invoices`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `sp_generate_all_invoices` ()  BEGIN
    DECLARE vIdLine int;
    DECLARE vFinished int DEFAULT 0;

	DECLARE cur_lines CURSOR FOR select li.idline
								  from lines_users li
								  where li.is_available = 1 order by li.idline asc;
	
   
	DECLARE CONTINUE HANDLER FOR NOT FOUND SET vFinished = 1;
     SET SESSION TRANSACTION ISOLATION LEVEL REPEATABLE READ;
    open cur_lines;
    FETCH cur_lines INTO vIdLine;
    WHILE (vFinished=0) DO
        
		call sp_generate_invoice_line(vIdLine);
        
        FETCH cur_lines INTO  vIdLine;
    
    END while;
    close cur_lines;
    commit;
END$$

DROP PROCEDURE IF EXISTS `sp_generate_invoice_line`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `sp_generate_invoice_line` (IN `pIdline` INT)  BEGIN

	
    DECLARE vIdInvoice int;
    DECLARE vIdCall int;
    DECLARE vCant int default 0;
	DECLARE vTotal float;
    DECLARE vFinished int DEFAULT 0;
    DECLARE vSum float default 0;
	DECLARE vCost float default 0;
	DECLARE vPrice float default 0;
	DECLARE vTotalPrice float default 0;
    DECLARE vIdUser int;
 

	DECLARE cur_calls CURSOR FOR select c.idcall, c.total,((r.costprice/60)*TIME_TO_SEC(c.durationtime)) as price
								from calls c
								inner join rates r on c.idrate=r.idrate
								where c.idinvoice is null and c.origincall = pIdline order by c.idcall asc;
	
	DECLARE CONTINUE HANDLER FOR NOT FOUND SET vFinished = 1;
   SET SESSION TRANSACTION ISOLATION LEVEL REPEATABLE READ;
	 select iduser into vIdUser from lines_users where idline=pIdline;

	insert into invoices(iduser,idline,total_calls,total,cost,date_issued,expiration,state) values (vIdUser,pIdline,0,0,0,CURDATE(),DATE_ADD(CURDATE(),INTERVAL 15 DAY),0);
	
    set vIdInvoice = last_insert_id();

    open cur_calls;
    FETCH cur_calls INTO  vIdCall, vTotal, vPrice;
    WHILE (vFinished=0) DO
        
        SET vSum = vSum + vTotal;
		SET vTotalPrice = vTotalPrice +vPrice;
        SET vCant = vCant + 1;

        
        UPDATE calls set idinvoice = vIdInvoice where idcall = vIdCall;
        FETCH cur_calls INTO  vIdCall, vTotal, vPrice;
    END while;
    
    update invoices set total_calls = ROUND(vCant,2), total = vSum ,cost = ROUND(vTotalPrice,2) where idinvoice = vIdInvoice;
    close cur_calls;
COMMIT;

END$$

DROP PROCEDURE IF EXISTS `sp_insert_common_user`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `sp_insert_common_user` (IN `username` VARCHAR(50), IN `pass` VARCHAR(50), IN `firstname` VARCHAR(50), IN `lastname` VARCHAR(50), IN `dni` VARCHAR(50), IN `city` VARCHAR(50), OUT `pid` INT)  BEGIN
	Declare cityId int;

   	set cityId = (SELECT id 
					FROM cities c 
					WHERE c.city = city);

	INSERT users (user, password, name, lastname, dni, idcity, usertype) 
    	VALUES (username, pass, firstname, lastname,dni,cityId,"cliente");

		set pid = LAST_INSERT_ID();
END$$

DROP PROCEDURE IF EXISTS `sp_update_common_user`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `sp_update_common_user` (IN `username` VARCHAR(50), IN `pass` VARCHAR(50), IN `firstname` VARCHAR(50), IN `lastname` VARCHAR(50), IN `dni` VARCHAR(50), IN `city` VARCHAR(50), IN `idUser` INT, OUT `pid` INT)  BEGIN
	Declare cityId int;

    	set cityId = (SELECT id 
					FROM cities c 
					WHERE c.city = city);

	UPDATE users SET user=username, password=pass, name=firstname, lastname= lastname, dni=dni, idcity=cityId 
	WHERE id=iduser;

	set pid = iduser;

END$$


-- Funciones
--
DROP FUNCTION IF EXISTS `f_calls_add_idCities`$$
CREATE DEFINER=`root`@`localhost` FUNCTION `f_calls_add_idCities` (`pidCall` INT) RETURNS TINYINT(1) begin
        Declare v_idOriginCity int default -1;
		Declare v_idDestinCity int default -1;

		select c.id into v_idOriginCity
		from cities c
		inner join users u on c.id=u.idcity
		inner join lines_users li on u.id=li.iduser
 		where li.idline=(select origincall from calls c where c.idcall=pidCall);
		
		select c.id into v_idDestinCity
		from cities c
		inner join users u on c.id=u.idcity
		inner join lines_users li on u.id=li.iduser
 		where li.idline=(select destinationcall from calls c where c.idcall=pidCall);

		if (v_idOriginCity = -1 or v_idDestinCity =-1 ) then
			SIGNAL sqlstate '11111' SET MESSAGE_TEXT = 'city does not exist', MYSQL_ERRNO = 9999;
		else 
			UPDATE calls SET origincity = v_idOriginCity, destinationcity=v_idDestinCity
			WHERE idcall=pidCall;
		end if;
  return true;
end$$




DROP FUNCTION IF EXISTS `f_extract_number`$$
CREATE DEFINER=`root`@`localhost` FUNCTION `f_extract_number` (`pNumLine` VARCHAR(50)) RETURNS VARCHAR(10) CHARSET latin1 BEGIN
	Declare v_numReverse VARCHAR(50);
	Declare v_num VARCHAR(50);
	
	SELECT REVERSE(pNumLine) into v_numReverse;
	SELECT LEFT(v_numReverse,7) into v_num ;

	RETURN (select REVERSE(v_num));
END$$

DROP FUNCTION IF EXISTS `f_generate_num`$$
CREATE DEFINER=`root`@`localhost` FUNCTION `f_generate_num` () RETURNS VARCHAR(10) CHARSET latin1 BEGIN
	Declare num VARCHAR(10);

	SELECT random_num into num
	FROM (
     SELECT FLOOR(4000000+RAND() * (5999999-4000000)) AS random_num) AS numbers_mst_plus_1
	WHERE random_num NOT IN (SELECT f_extract_number(linenumber) FROM lines_users WHERE linenumber IS NOT NULL)
	LIMIT 1;

	RETURN num;
END$$

DROP FUNCTION IF EXISTS `f_type_line_number`$$
CREATE DEFINER=`root`@`localhost` FUNCTION `f_type_line_number` (`pIdTypeLine` INT, `pIdCity` INT) RETURNS VARCHAR(50) CHARSET latin1 BEGIN
	Declare num VARCHAR(50);
	Declare type_line VARCHAR(50);
	declare city_prefix int;
	declare country_prefix varchar(10);
	
	select linetypes.type into type_line from linetypes where linetypes.idtype=pIdTypeLine;
	
	select cities.prefix into city_prefix from cities where cities.id=pIdCity;
	
	select countries.prefix into country_prefix 
	from countries 
	inner join provinces on provinces.id=countries.id
	inner join cities on cities.id=pIdCity;

	if (type_line ="Celular") then 
		set num = concat(country_prefix," ","(9) ",city_prefix," ","15");
	else 
		set num =concat(country_prefix," ",city_prefix," ");
	end if;

	RETURN num;
END$$

DELIMITER ;
