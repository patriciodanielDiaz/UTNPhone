-------------------------------------------------indices-----------------------------------------------------------------------

--INDEX RANGE SCAN

--tabla creada (date_index) para aumentar la performance al crear un indice BTREE
EXPLAIN EXTENDED SELECT * FROM calls c WHERE c.origincall=2 and c.create_at between "2020-01-01" and "2020-07-12";
CREATE INDEX idx_call_date ON calls(date_index) USING BTREE;

--tabla creada (date_index) para aumentar la performance al crear un indice BTREE
--INDEX RANGE SCAN
EXPLAIN EXTENDED SELECT i.* FROM invoices i inner join lines_users li on li.idline=i.idline where li.linenumber="+54 (9) 223 154211100" and i.create_at between "2020-01-01" and "2020-07-12" ORDER BY i.state ASC;
CREATE INDEX idx_invoice_date ON invoices(date_index) USING BTREE;

--show INDEX from calls

--Crear indice BTREE para "between" para fechas
--Craer indice  HASHTABLE para buscar los numeros de telefonos o nombres cuando haces "="

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
-------------------------------------------------------------------------------------------------------------



-- phpMyAdmin SQL Dump
-- version 4.8.5
-- https://www.phpmyadmin.net/
--
-- Servidor: 127.0.0.1:3306
-- Tiempo de generación: 21-06-2020 a las 06:45:40
-- Versión del servidor: 5.7.26
-- Versión de PHP: 7.2.18

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET GLOBAL time_zone = "-03:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de datos: `utnphone`
--

DELIMITER $$
-- author mariano
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

-- author mariano
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

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `calls`
--

DROP TABLE IF EXISTS `calls`;
CREATE TABLE IF NOT EXISTS `calls` (
  `idcall` int(11) NOT NULL AUTO_INCREMENT,
  `origincall` int(11) NOT NULL,
  `destinationcall` int(11) NOT NULL,
  `origincity` int(11) DEFAULT NULL,
  `destinationcity` int(11) DEFAULT NULL,
  `durationtime` time NOT NULL,
  `price` float DEFAULT NULL,
  `total` float DEFAULT NULL,
  `idinvoice` int(11) DEFAULT NULL,
  `idrate` int(11) DEFAULT NULL,
  `create_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `date_index` date DEFAULT NULL,
  `update_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`idcall`),
  KEY `idinvoice` (`idinvoice`),
  KEY `idrate` (`idrate`),
  KEY `origincall` (`origincall`),
  KEY `destinationcall` (`destinationcall`),
  KEY `origincity` (`origincity`),
  KEY `destinationcity` (`destinationcity`),
  KEY `idx_call_date` (`date_index`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=37 DEFAULT CHARSET=latin1;

--
-- Volcado de datos para la tabla `calls`
--

INSERT INTO `calls` (`idcall`, `origincall`, `destinationcall`, `origincity`, `destinationcity`, `durationtime`, `price`, `total`, `idinvoice`, `idrate`, `create_at`, `date_index`, `update_at`) VALUES
(36, 2, 1, 1, 1, '00:06:00', 1.5, 9, NULL, 1, '2020-06-21 06:39:53', '2020-06-21', '2020-06-21 06:39:53');

--
-- Disparadores `calls`
--
DROP TRIGGER IF EXISTS `tai_call_complete`;
DELIMITER $$
CREATE TRIGGER `tai_call_complete` BEFORE INSERT ON `calls` FOR EACH ROW BEGIN

	Declare v_idRate int default -1; 
	Declare v_cost float default -1; 
	Declare v_price float default -1;
	Declare v_total float default -1;
	Declare v_idOriginCity int default -1;
	Declare v_idDestinCity int default -1;

	select c.id into v_idOriginCity
		from cities c
		inner join users u on c.id=u.idcity
		inner join lines_users li on u.id=li.iduser
 		where li.idline=new.origincall;
	select c.id into v_idDestinCity from cities c 
		inner join users u on c.id=u.idcity 
		inner join lines_users li on u.id=li.iduser
 		where li.idline=new.destinationcall;
	
	select r.idrate into v_idRate from rates r where r.origincity = v_idOriginCity and r.destinationcity= v_idDestinCity;
	
	if (v_idRate = -1) then
		SIGNAL sqlstate '11111' SET MESSAGE_TEXT = 'rate does not exist', MYSQL_ERRNO = 9999;
	end if;
	
	set  v_price = (select pricexmin from rates where idrate=v_idRate);
	set  v_cost = (select costprice from rates where idrate=v_idRate);
	set  v_total = (v_price/60)*(TIME_TO_SEC(new.durationtime));

	set new.origincity=v_idOriginCity;
	set new.destinationcity=v_idDestinCity;
	SET new.idrate=v_idRate;
	set new.price=v_price; 
	set new.total=v_total;
	
END
$$
DELIMITER ;
DROP TRIGGER IF EXISTS `tbi_call_add`;
DELIMITER $$
CREATE TRIGGER `tbi_call_add` BEFORE INSERT ON `calls` FOR EACH ROW BEGIN
	IF (new.create_at > now()) then
		signal sqlstate '10001'
		SET MESSAGE_TEXT = 'La fecha no puede ser mayor al dia de hoy',
		MYSQL_ERRNO = 2.2;
	END IF;
    
    set new.update_at = CURRENT_TIMESTAMP;
    set new.date_index = CURRENT_TIMESTAMP;
    
END
$$
DELIMITER ;
DROP TRIGGER IF EXISTS `tbu_call_update`;
DELIMITER $$
CREATE TRIGGER `tbu_call_update` BEFORE UPDATE ON `calls` FOR EACH ROW begin
        
        set new.update_at = CURRENT_TIMESTAMP;

end
$$
DELIMITER ;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `cities`
--

DROP TABLE IF EXISTS `cities`;
CREATE TABLE IF NOT EXISTS `cities` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `idprovince` int(11) NOT NULL,
  `city` varchar(100) NOT NULL,
  `prefix` int(11) NOT NULL,
  `create_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `update_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `city` (`city`),
  UNIQUE KEY `prefix` (`prefix`),
  KEY `idprovince` (`idprovince`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=latin1;

--
-- Volcado de datos para la tabla `cities`
--

INSERT INTO `cities` (`id`, `idprovince`, `city`, `prefix`, `create_at`, `update_at`) VALUES
(1, 1, 'Mar del plata', 223, '2020-05-11 22:59:02', '2020-05-11 22:59:02'),
(2, 1, 'Área Metropolitana', 11, '2020-05-11 22:59:02', '2020-05-11 22:59:02'),
(3, 1, '25 de Mayo', 2345, '2020-05-11 22:59:02', '2020-05-11 22:59:02'),
(4, 1, 'Bahía Blanca', 291, '2020-05-11 22:59:02', '2020-05-11 22:59:02'),
(5, 1, 'Chacabuco', 2352, '2020-05-11 22:59:02', '2020-05-11 22:59:02'),
(6, 1, 'Escobar', 3488, '2020-05-11 22:59:02', '2020-05-11 22:59:02'),
(7, 1, 'Maipu', 2268, '2020-05-11 22:59:02', '2020-05-11 22:59:02'),
(8, 1, 'Necochea', 2262, '2020-05-11 22:59:02', '2020-05-11 22:59:02'),
(9, 1, 'Pinamar', 2254, '2020-05-11 22:59:02', '2020-05-11 22:59:02'),
(10, 1, 'Zarate', 3487, '2020-05-11 22:59:03', '2020-05-11 22:59:03');

--
-- Disparadores `cities`
--
DROP TRIGGER IF EXISTS `tbi_cities_create`;
DELIMITER $$
CREATE TRIGGER `tbi_cities_create` BEFORE INSERT ON `cities` FOR EACH ROW begin
        
        set new.create_at = CURRENT_TIMESTAMP;
        set new.update_at = CURRENT_TIMESTAMP;

end
$$
DELIMITER ;
DROP TRIGGER IF EXISTS `tbu_cities_update`;
DELIMITER $$
CREATE TRIGGER `tbu_cities_update` BEFORE UPDATE ON `cities` FOR EACH ROW begin
        
        set new.update_at = CURRENT_TIMESTAMP;

end
$$
DELIMITER ;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `countries`
--

DROP TABLE IF EXISTS `countries`;
CREATE TABLE IF NOT EXISTS `countries` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `country` varchar(50) NOT NULL,
  `prefix` varchar(50) NOT NULL,
  `create_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;

--
-- Volcado de datos para la tabla `countries`
--

INSERT INTO `countries` (`id`, `country`, `prefix`, `create_at`, `update_at`) VALUES
(1, 'Argentina', '+54', '2020-06-09 18:41:55', '2020-06-09 18:41:55');

--
-- Disparadores `countries`
--
DROP TRIGGER IF EXISTS `tbi_countries_create`;
DELIMITER $$
CREATE TRIGGER `tbi_countries_create` BEFORE INSERT ON `countries` FOR EACH ROW begin
        
        set new.create_at = CURRENT_TIMESTAMP;
        set new.update_at = CURRENT_TIMESTAMP;

end
$$
DELIMITER ;
DROP TRIGGER IF EXISTS `tbu_country_update`;
DELIMITER $$
CREATE TRIGGER `tbu_country_update` BEFORE UPDATE ON `countries` FOR EACH ROW begin
        
        set new.create_at = CURRENT_TIMESTAMP;
        set new.update_at = CURRENT_TIMESTAMP;

end
$$
DELIMITER ;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `invoices`
--

DROP TABLE IF EXISTS `invoices`;
CREATE TABLE IF NOT EXISTS `invoices` (
  `idinvoice` int(11) NOT NULL AUTO_INCREMENT,
  `iduser` int(11) NOT NULL,
  `idline` int(11) DEFAULT NULL,
  `total_calls` int(11) NOT NULL,
  `total` float NOT NULL,
  `cost` float NOT NULL,
  `date_issued` date NOT NULL,
  `expiration` date NOT NULL,
  `state` tinyint(4) NOT NULL,
  `create_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `date_index` date DEFAULT NULL,
  `update_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`idinvoice`),
  KEY `iduser` (`iduser`),
  KEY `fk_invoices_lines` (`idline`),
  KEY `idx_invoice_date` (`date_index`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=53 DEFAULT CHARSET=latin1;

--
-- Disparadores `invoices`
--
DROP TRIGGER IF EXISTS `tbi_invoices_create`;
DELIMITER $$
CREATE TRIGGER `tbi_invoices_create` BEFORE INSERT ON `invoices` FOR EACH ROW begin
        
        set new.create_at = CURRENT_TIMESTAMP;
        set new.update_at = CURRENT_TIMESTAMP;
        set new.date_index = CURRENT_TIMESTAMP;

end
$$
DELIMITER ;
DROP TRIGGER IF EXISTS `tbu_invoices_update`;
DELIMITER $$
CREATE TRIGGER `tbu_invoices_update` BEFORE UPDATE ON `invoices` FOR EACH ROW begin
        
        set new.update_at = CURRENT_TIMESTAMP;

end
$$
DELIMITER ;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `lines_users`
--

DROP TABLE IF EXISTS `lines_users`;
CREATE TABLE IF NOT EXISTS `lines_users` (
  `idline` int(11) NOT NULL AUTO_INCREMENT,
  `linenumber` varchar(50) NOT NULL,
  `idtype` int(11) NOT NULL,
  `iduser` int(11) NOT NULL,
  `create_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `update_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `is_available` tinyint(1) NOT NULL DEFAULT '1',
  PRIMARY KEY (`idline`),
  UNIQUE KEY `linenumber` (`linenumber`),
  KEY `idtype` (`idtype`),
  KEY `iduser` (`iduser`)
) ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=latin1;

--
-- Volcado de datos para la tabla `lines_users`
--

INSERT INTO `lines_users` (`idline`, `linenumber`, `idtype`, `iduser`, `create_at`, `update_at`, `is_available`) VALUES
(1, '+54 (9) 223 155331154', 2, 1, '2020-05-11 22:10:49', '2020-06-21 22:26:57', 1),
(2, '+54 (9) 223 154211100', 2, 2, '2020-05-11 22:20:16', '2020-06-15 22:54:11', 1),
(17, '+54 (9) 2262 154211136', 2, 8, '2020-06-14 22:47:37', '2020-06-21 22:27:12', 1),
(21, '+54 (9) 2254 154112269', 2, 10, '2020-06-16 22:08:21', '2020-06-21 22:27:28', 1),
(22, '+54 (9) 223 155420014', 2, 2, '2020-06-19 22:18:08', '2020-06-21 22:27:37', 1);

--
-- Disparadores `lines_users`
--
DROP TRIGGER IF EXISTS `tbi_lines_users_create`;
DELIMITER $$
CREATE TRIGGER `tbi_lines_users_create` BEFORE INSERT ON `lines_users` FOR EACH ROW begin
        
        set new.create_at = CURRENT_TIMESTAMP;
        set new.update_at = CURRENT_TIMESTAMP;

end
$$
DELIMITER ;
DROP TRIGGER IF EXISTS `tbu_lines_users_update`;
DELIMITER $$
CREATE TRIGGER `tbu_lines_users_update` BEFORE UPDATE ON `lines_users` FOR EACH ROW begin
        
        set new.update_at = CURRENT_TIMESTAMP;
 
end
$$
DELIMITER ;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `linetypes`
--

DROP TABLE IF EXISTS `linetypes`;
CREATE TABLE IF NOT EXISTS `linetypes` (
  `idtype` int(11) NOT NULL AUTO_INCREMENT,
  `type` varchar(50) NOT NULL,
  `create_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `update_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`idtype`),
  UNIQUE KEY `type` (`type`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;

--
-- Volcado de datos para la tabla `linetypes`
--

INSERT INTO `linetypes` (`idtype`, `type`, `create_at`, `update_at`) VALUES
(1, 'Residencial', '2020-05-11 23:17:36', '2020-05-11 23:17:36'),
(2, 'Celular', '2020-05-11 23:17:36', '2020-05-11 23:17:36');

--
-- Disparadores `linetypes`
--
DROP TRIGGER IF EXISTS `tbi_linetypes_create`;
DELIMITER $$
CREATE TRIGGER `tbi_linetypes_create` BEFORE INSERT ON `linetypes` FOR EACH ROW begin
        
        set new.create_at = CURRENT_TIMESTAMP;
        set new.update_at = CURRENT_TIMESTAMP;

end
$$
DELIMITER ;
DROP TRIGGER IF EXISTS `tbu_linetypes_update`;
DELIMITER $$
CREATE TRIGGER `tbu_linetypes_update` BEFORE UPDATE ON `linetypes` FOR EACH ROW begin
        
        set new.update_at = CURRENT_TIMESTAMP;

end
$$
DELIMITER ;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `provinces`
--

DROP TABLE IF EXISTS `provinces`;
CREATE TABLE IF NOT EXISTS `provinces` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `province` varchar(50) NOT NULL,
  `idcountry` int(11) NOT NULL,
  `create_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `update_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `province` (`province`),
  KEY `fk_countries_provinces` (`idcountry`)
) ENGINE=InnoDB AUTO_INCREMENT=30 DEFAULT CHARSET=latin1;

--
-- Volcado de datos para la tabla `provinces`
--

INSERT INTO `provinces` (`id`, `province`, `idcountry`, `create_at`, `update_at`) VALUES
(1, 'Buenos Aires', 1, '2020-05-11 13:38:25', '2020-06-09 18:55:53'),
(2, 'Catamarca', 1, '2020-05-11 13:38:25', '2020-06-09 18:55:53'),
(3, 'Chaco', 1, '2020-05-11 13:38:25', '2020-06-09 18:55:53'),
(4, 'Chubut', 1, '2020-05-11 13:38:25', '2020-06-09 18:55:53'),
(5, 'Cordoba', 1, '2020-05-11 13:38:26', '2020-06-09 18:55:53'),
(6, 'Corrientes', 1, '2020-05-11 13:38:26', '2020-06-09 18:55:53'),
(7, 'Entre Rios', 1, '2020-05-11 13:38:26', '2020-06-09 18:55:53'),
(8, 'Formosa', 1, '2020-05-11 13:38:26', '2020-06-09 18:55:53'),
(9, 'Jujuy', 1, '2020-05-11 13:38:26', '2020-06-09 18:55:53'),
(10, 'La Pampa', 1, '2020-05-11 13:38:26', '2020-06-09 18:55:53'),
(11, 'La Rioja', 1, '2020-05-11 13:38:26', '2020-06-09 18:55:53'),
(12, 'Mendoza', 1, '2020-05-11 13:38:26', '2020-06-09 18:55:53'),
(13, 'Misiones', 1, '2020-05-11 13:38:26', '2020-06-09 18:55:53'),
(14, 'Neuquen', 1, '2020-05-11 13:38:26', '2020-06-09 18:55:53'),
(15, 'Rio Negro', 1, '2020-05-11 13:38:26', '2020-06-09 18:55:53'),
(16, 'Salta', 1, '2020-05-11 13:38:26', '2020-06-09 18:55:53'),
(17, 'San Juan', 1, '2020-05-11 13:38:26', '2020-06-09 18:55:53'),
(18, 'San Luis', 1, '2020-05-11 13:38:26', '2020-06-09 18:55:53'),
(20, 'Santa Fe', 1, '2020-05-11 13:38:26', '2020-06-09 18:55:53'),
(21, 'Santiago del Estero', 1, '2020-05-11 13:38:26', '2020-06-09 18:55:53'),
(22, 'Tierra del fuego', 1, '2020-05-11 13:38:26', '2020-06-09 18:55:53'),
(23, 'Tucuman', 1, '2020-05-11 13:38:26', '2020-06-09 18:55:53'),
(29, 'Santa Cruz', 1, '2020-05-11 13:38:26', '2020-06-09 18:55:53');

--
-- Disparadores `provinces`
--
DROP TRIGGER IF EXISTS `tbi_provinces_create`;
DELIMITER $$
CREATE TRIGGER `tbi_provinces_create` BEFORE INSERT ON `provinces` FOR EACH ROW begin
        
        set new.create_at = CURRENT_TIMESTAMP;
        set new.update_at = CURRENT_TIMESTAMP;

end
$$
DELIMITER ;
DROP TRIGGER IF EXISTS `tbu_provinces_update`;
DELIMITER $$
CREATE TRIGGER `tbu_provinces_update` BEFORE UPDATE ON `provinces` FOR EACH ROW begin
        
        set new.update_at = CURRENT_TIMESTAMP;

end
$$
DELIMITER ;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `rates`
--

DROP TABLE IF EXISTS `rates`;
CREATE TABLE IF NOT EXISTS `rates` (
  `idrate` int(11) NOT NULL AUTO_INCREMENT,
  `type` varchar(50) NOT NULL,
  `pricexmin` float NOT NULL,
  `costprice` float NOT NULL,
  `origincity` int(11) NOT NULL,
  `destinationcity` int(11) NOT NULL,
  `create_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `update_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`idrate`),
  KEY `origincity` (`origincity`),
  KEY `destinationcity` (`destinationcity`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=latin1;

--
-- Volcado de datos para la tabla `rates`
--

INSERT INTO `rates` (`idrate`, `type`, `pricexmin`, `costprice`, `origincity`, `destinationcity`, `create_at`, `update_at`) VALUES
(1, 'local', 1.5, 0.5, 1, 1, '2020-05-11 23:11:04', '2020-06-21 00:25:43'),
(2, 'local', 2.5, 1.5, 1, 8, '2020-05-11 23:11:09', '2020-06-21 00:25:53'),
(3, 'local', 5.5, 3, 8, 1, '2020-06-14 23:40:36', '2020-06-21 00:24:26'),
(4, 'local', 1.5, 0.5, 8, 8, '2020-06-15 23:19:06', '2020-06-15 00:19:20'),
(5, 'local', 4.2, 1.75, 1, 9, '2020-06-15 23:56:55', '2020-06-21 00:25:00'),
(6, 'local', 6.25, 3.15, 9, 1, '2020-06-15 23:57:32', '2020-06-21 00:25:12'),
(7, 'local', 2.25, 0.75, 9, 9, '2020-06-15 23:58:31', '2020-06-21 00:24:43');

--
-- Disparadores `rates`
--
DROP TRIGGER IF EXISTS `tbi_rates_create`;
DELIMITER $$
CREATE TRIGGER `tbi_rates_create` BEFORE INSERT ON `rates` FOR EACH ROW begin
        
        set new.create_at = CURRENT_TIMESTAMP;
        set new.update_at = CURRENT_TIMESTAMP;

end
$$
DELIMITER ;
DROP TRIGGER IF EXISTS `tbu_rates_update`;
DELIMITER $$
CREATE TRIGGER `tbu_rates_update` BEFORE UPDATE ON `rates` FOR EACH ROW begin
        
        set new.update_at = CURRENT_TIMESTAMP;

end
$$
DELIMITER ;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `users`
--

DROP TABLE IF EXISTS `users`;
CREATE TABLE IF NOT EXISTS `users` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user` varchar(50) NOT NULL,
  `password` varchar(50) NOT NULL,
  `name` varchar(50) NOT NULL,
  `lastname` varchar(50) NOT NULL,
  `dni` varchar(10) NOT NULL,
  `idcity` int(11) NOT NULL,
  `usertype` enum('cliente','empleado') NOT NULL DEFAULT 'cliente',
  `create_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `update_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `is_available` tinyint(1) DEFAULT '1',
  PRIMARY KEY (`id`),
  UNIQUE KEY `user` (`user`),
  UNIQUE KEY `dni` (`dni`),
  KEY `idcity` (`idcity`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=latin1;

--
-- Volcado de datos para la tabla `users`
--

INSERT INTO `users` (`id`, `user`, `password`, `name`, `lastname`, `dni`, `idcity`, `usertype`, `create_at`, `update_at`, `is_available`) VALUES
(1, 'Patricio', '111111', 'Patricio', 'diaz', '3756546', 1, 'cliente', '2020-05-11 21:13:47', '2020-06-21 20:22:44', 1),
(2, 'Mariano', '123456', 'Mariano', 'Zanier', '333333', 1, 'cliente', '2020-05-11 21:16:52', '2020-06-18 20:37:47', 1),
(3, 'admin', '111111', 'admin', 'admin', '111111', 1, 'empleado', '2020-06-05 21:06:45', '2020-06-05 21:06:45', 1),
(7, 'antena', '000000', 'an', 'tena', '000000', 1, 'empleado', '2020-06-07 21:50:26', '2020-06-07 21:50:26', 1),
(8, 'Juan', '999999', 'Juan', 'Perez', '77777777', 8, 'cliente', '2020-06-14 21:45:37', '2020-06-21 21:23:14', 1),
(10, 'PepePinamar', 'PepePinamar', 'Pepe', 'Grillo', '23456789', 9, 'cliente', '2020-06-15 21:00:17', '2020-06-21 21:23:36', 1);

--
-- Disparadores `users`
--
DROP TRIGGER IF EXISTS `tbi_users_create`;
DELIMITER $$
CREATE TRIGGER `tbi_users_create` BEFORE INSERT ON `users` FOR EACH ROW begin
        
        set new.create_at = CURRENT_TIMESTAMP;
        set new.update_at = CURRENT_TIMESTAMP;

end
$$
DELIMITER ;
DROP TRIGGER IF EXISTS `tbu_users_update`;
DELIMITER $$
CREATE TRIGGER `tbu_users_update` BEFORE UPDATE ON `users` FOR EACH ROW begin
        
        set new.update_at = CURRENT_TIMESTAMP;

end
$$
DELIMITER ;

-- --------------------------------------------------------

--
-- Estructura Stand-in para la vista `v_calls`
-- (Véase abajo para la vista actual)
--
DROP VIEW IF EXISTS `v_calls`;
CREATE TABLE IF NOT EXISTS `v_calls` (
`id` int(11)
,`origin_number` varchar(50)
,`destination_number` varchar(50)
,`origin_city` varchar(100)
,`destination_city` varchar(100)
,`duration_time` time
,`price` float
,`total` float
,`date_call` timestamp
);

-- --------------------------------------------------------

--
-- Estructura Stand-in para la vista `v_invoices`
-- (Véase abajo para la vista actual)
--
DROP VIEW IF EXISTS `v_invoices`;
CREATE TABLE IF NOT EXISTS `v_invoices` (
`id` int(11)
,`dni` varchar(10)
,`line_number` varchar(50)
,`total_calls` int(11)
,`total` float
,`cost` float
,`date_issued` date
,`expiration` date
,`state` varchar(6)
,`create_at` timestamp
);

-- --------------------------------------------------------

--
-- Estructura Stand-in para la vista `v_user_profiles`
-- (Véase abajo para la vista actual)
--
DROP VIEW IF EXISTS `v_user_profiles`;
CREATE TABLE IF NOT EXISTS `v_user_profiles` (
`name` varchar(50)
,`lastname` varchar(50)
,`dni` varchar(10)
,`city` varchar(100)
,`province` varchar(50)
,`country` varchar(50)
);

-- --------------------------------------------------------

--
-- Estructura para la vista `v_calls`
--
DROP TABLE IF EXISTS `v_calls`;

CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `v_calls`  AS  select `c`.`idcall` AS `id`,`li_1`.`linenumber` AS `origin_number`,`li_2`.`linenumber` AS `destination_number`,`c1`.`city` AS `origin_city`,`c2`.`city` AS `destination_city`,`c`.`durationtime` AS `duration_time`,`c`.`price` AS `price`,`c`.`total` AS `total`,`c`.`create_at` AS `date_call` from ((((`calls` `c` join `lines_users` `li_1` on((`li_1`.`idline` = `c`.`origincall`))) join `lines_users` `li_2` on((`li_2`.`idline` = `c`.`destinationcall`))) join `cities` `c1` on((`c1`.`id` = `c`.`origincity`))) join `cities` `c2` on((`c2`.`id` = `c`.`destinationcity`))) ;

-- --------------------------------------------------------

--
-- Estructura para la vista `v_invoices`
--
DROP TABLE IF EXISTS `v_invoices`;

CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `v_invoices`  AS  select `i`.`idinvoice` AS `id`,`u`.`dni` AS `dni`,`l`.`linenumber` AS `line_number`,`i`.`total_calls` AS `total_calls`,`i`.`total` AS `total`,`i`.`cost` AS `cost`,`i`.`date_issued` AS `date_issued`,`i`.`expiration` AS `expiration`,if((`i`.`state` = 0),'impago','pago') AS `state`,`i`.`create_at` AS `create_at` from ((`invoices` `i` join `users` `u` on((`i`.`iduser` = `u`.`id`))) join `lines_users` `l` on((`l`.`idline` = `i`.`idline`))) ;

-- --------------------------------------------------------

--
-- Estructura para la vista `v_user_profiles`
--
DROP TABLE IF EXISTS `v_user_profiles`;

CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `v_user_profiles`  AS  select `users`.`name` AS `name`,`users`.`lastname` AS `lastname`,`users`.`dni` AS `dni`,`cities`.`city` AS `city`,`provinces`.`province` AS `province`,`countries`.`country` AS `country` from (((`users` join `cities` on((`cities`.`id` = `users`.`idcity`))) join `provinces` on((`provinces`.`id` = `cities`.`idprovince`))) join `countries` on((`countries`.`id` = `provinces`.`idcountry`))) ;

--
-- Restricciones para tablas volcadas
--

--
-- Filtros para la tabla `calls`
--
ALTER TABLE `calls`
  ADD CONSTRAINT `calls_ibfk_1` FOREIGN KEY (`idinvoice`) REFERENCES `invoices` (`idinvoice`),
  ADD CONSTRAINT `calls_ibfk_2` FOREIGN KEY (`idrate`) REFERENCES `rates` (`idrate`),
  ADD CONSTRAINT `calls_ibfk_3` FOREIGN KEY (`origincall`) REFERENCES `lines_users` (`idline`),
  ADD CONSTRAINT `calls_ibfk_4` FOREIGN KEY (`destinationcall`) REFERENCES `lines_users` (`idline`),
  ADD CONSTRAINT `calls_ibfk_5` FOREIGN KEY (`origincity`) REFERENCES `cities` (`id`),
  ADD CONSTRAINT `calls_ibfk_6` FOREIGN KEY (`destinationcity`) REFERENCES `cities` (`id`);

--
-- Filtros para la tabla `cities`
--
ALTER TABLE `cities`
  ADD CONSTRAINT `cities_ibfk_1` FOREIGN KEY (`idprovince`) REFERENCES `provinces` (`id`);

--
-- Filtros para la tabla `invoices`
--
ALTER TABLE `invoices`
  ADD CONSTRAINT `fk_invoices_lines` FOREIGN KEY (`idline`) REFERENCES `lines_users` (`idline`),
  ADD CONSTRAINT `invoices_ibfk_1` FOREIGN KEY (`iduser`) REFERENCES `users` (`id`);

--
-- Filtros para la tabla `lines_users`
--
ALTER TABLE `lines_users`
  ADD CONSTRAINT `lines_users_ibfk_1` FOREIGN KEY (`idtype`) REFERENCES `linetypes` (`idtype`),
  ADD CONSTRAINT `lines_users_ibfk_2` FOREIGN KEY (`iduser`) REFERENCES `users` (`id`);

--
-- Filtros para la tabla `provinces`
--
ALTER TABLE `provinces`
  ADD CONSTRAINT `fk_countries_provinces` FOREIGN KEY (`idcountry`) REFERENCES `countries` (`id`);

--
-- Filtros para la tabla `rates`
--
ALTER TABLE `rates`
  ADD CONSTRAINT `rates_ibfk_1` FOREIGN KEY (`origincity`) REFERENCES `cities` (`id`),
  ADD CONSTRAINT `rates_ibfk_2` FOREIGN KEY (`destinationcity`) REFERENCES `cities` (`id`);

--
-- Filtros para la tabla `users`
--
ALTER TABLE `users`
  ADD CONSTRAINT `users_ibfk_1` FOREIGN KEY (`idcity`) REFERENCES `cities` (`id`);

DELIMITER $$
--
-- Eventos
--
DROP EVENT `event1`$$
CREATE DEFINER=`root`@`localhost` EVENT `event1` ON SCHEDULE EVERY 1 MONTH STARTS '2020-07-01 02:00:00' ON COMPLETION PRESERVE ENABLE DO BEGIN
  
  call sp_generate_all_invoices();

END$$

DELIMITER ;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
