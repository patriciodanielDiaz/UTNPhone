-- phpMyAdmin SQL Dump
-- version 4.8.5
-- https://www.phpmyadmin.net/
--
-- Servidor: 127.0.0.1:3306
-- Tiempo de generación: 07-06-2020 a las 04:14:27
-- Versión del servidor: 5.7.26
-- Versión de PHP: 7.2.18

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "-03:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de datos: `utnphone`
--

DELIMITER $$
--
-- Procedimientos
--
DROP PROCEDURE IF EXISTS `sp_delete_line`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `sp_delete_line` (IN `pnumber` VARCHAR(50))  BEGIN
	
	Declare id_remove int default 0;
    Declare is_remove boolean default false;
    
		SELECT idline into id_remove
	    FROM lines_users lu 
	    WHERE lu.linenumber=pnumber;
	
	if (id_remove = 0) then
		
		SIGNAL sqlstate '11111' SET MESSAGE_TEXT = 'line does not exist', 			MYSQL_ERRNO = 9999;
	else 
		UPDATE lines_users SET is_available= 0
		WHERE idline=id_remove;
        set is_remove = true;
		
	end if;
    select is_remove;

END$$

DROP PROCEDURE IF EXISTS `sp_insert_common_user`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `sp_insert_common_user` (IN `username` VARCHAR(50), IN `pass` VARCHAR(50), IN `firstname` VARCHAR(50), IN `lastname` VARCHAR(50), IN `dni` VARCHAR(50), IN `city` VARCHAR(50))  BEGIN
	Declare cityId int;

    set cityId = (SELECT id 
					FROM cities c 
					WHERE c.city = city);

	INSERT users (user, password, name, lastname, dni, idcity) 
    VALUES (username, pass, firstname, lastname,dni,cityId);
END$$

DROP PROCEDURE IF EXISTS `sp_update_common_user`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `sp_update_common_user` (IN `username` VARCHAR(50), IN `pass` VARCHAR(50), IN `firstname` VARCHAR(50), IN `lastname` VARCHAR(50), IN `dni` VARCHAR(50), IN `city` VARCHAR(50), IN `idUser` INT)  BEGIN
	Declare cityId int;

    set cityId = (SELECT id 
					FROM cities c 
					WHERE c.city = city);

UPDATE users SET user=username, password=pass, name=firstname, lastname=lastname, dni=dni, idcity=cityId 
WHERE id=iduser;

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
  `costprice` float DEFAULT NULL,
  `total` float DEFAULT NULL,
  `idinvoice` int(11) DEFAULT NULL,
  `idrate` int(11) DEFAULT NULL,
  `create_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `update_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`idcall`),
  KEY `idinvoice` (`idinvoice`),
  KEY `idrate` (`idrate`),
  KEY `origincall` (`origincall`),
  KEY `destinationcall` (`destinationcall`),
  KEY `origincity` (`origincity`),
  KEY `destinationcity` (`destinationcity`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=latin1;

--
-- Volcado de datos para la tabla `calls`
--

INSERT INTO `calls` (`idcall`, `origincall`, `destinationcall`, `origincity`, `destinationcity`, `durationtime`, `price`, `costprice`, `total`, `idinvoice`, `idrate`, `create_at`, `update_at`) VALUES
(1, 1, 2, 1, 1, '02:00:00', NULL, NULL, NULL, 1, 1, '2020-05-11 03:33:25', '2020-06-01 05:16:55'),
(2, 1, 2, 1, 1, '02:00:00', NULL, NULL, NULL, 2, 1, '2020-06-01 05:16:00', '2020-06-04 04:43:14'),
(3, 2, 1, 1, 1, '00:03:20', 12.6, 12.8, 4.65, 3, 1, '2020-06-04 03:52:48', '2020-06-04 04:43:31');

--
-- Disparadores `calls`
--
DROP TRIGGER IF EXISTS `tbi_calls_create`;
DELIMITER $$
CREATE TRIGGER `tbi_calls_create` BEFORE INSERT ON `calls` FOR EACH ROW begin
        
        set new.create_at = CURRENT_TIMESTAMP;
        set new.update_at = CURRENT_TIMESTAMP;

end
$$
DELIMITER ;
DROP TRIGGER IF EXISTS `tbu_calls_update`;
DELIMITER $$
CREATE TRIGGER `tbu_calls_update` BEFORE UPDATE ON `calls` FOR EACH ROW begin
        
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
(1, 1, 'Mar del plata', 223, '2020-05-11 02:59:02', '2020-05-11 02:59:02'),
(2, 1, 'Área Metropolitana', 11, '2020-05-11 02:59:02', '2020-05-11 02:59:02'),
(3, 1, '25 de Mayo', 2345, '2020-05-11 02:59:02', '2020-05-11 02:59:02'),
(4, 1, 'Bahía Blanca', 291, '2020-05-11 02:59:02', '2020-05-11 02:59:02'),
(5, 1, 'Chacabuco', 2352, '2020-05-11 02:59:02', '2020-05-11 02:59:02'),
(6, 1, 'Escobar', 3488, '2020-05-11 02:59:02', '2020-05-11 02:59:02'),
(7, 1, 'Maipu', 2268, '2020-05-11 02:59:02', '2020-05-11 02:59:02'),
(8, 1, 'Necochea', 2262, '2020-05-11 02:59:02', '2020-05-11 02:59:02'),
(9, 1, 'Pinamar', 2254, '2020-05-11 02:59:02', '2020-05-11 02:59:02'),
(10, 1, 'Zarate', 3487, '2020-05-11 02:59:03', '2020-05-11 02:59:03');

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
-- Estructura de tabla para la tabla `invoices`
--

DROP TABLE IF EXISTS `invoices`;
CREATE TABLE IF NOT EXISTS `invoices` (
  `idinvoice` int(11) NOT NULL AUTO_INCREMENT,
  `iduser` int(11) NOT NULL,
  `totalcalls` int(11) NOT NULL,
  `total` float NOT NULL,
  `cost` float NOT NULL,
  `paymentdate` date NOT NULL,
  `expiration` date NOT NULL,
  `state` tinyint(4) NOT NULL,
  `create_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `update_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`idinvoice`),
  KEY `iduser` (`iduser`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=latin1;

--
-- Volcado de datos para la tabla `invoices`
--

INSERT INTO `invoices` (`idinvoice`, `iduser`, `totalcalls`, `total`, `cost`, `paymentdate`, `expiration`, `state`, `create_at`, `update_at`) VALUES
(1, 1, 1, 10, 10, '2020-05-31', '2020-05-31', 0, '2020-05-18 23:26:10', '2020-05-18 23:26:10'),
(2, 1, 1, 10.5, 12.65, '2020-05-01', '2020-05-01', 0, '2020-06-04 03:48:44', '2020-06-04 03:48:44'),
(3, 2, 1, 4.65, 98.4, '2020-01-01', '2020-01-01', 0, '2020-01-01 03:49:46', '2020-06-04 04:33:17');

--
-- Disparadores `invoices`
--
DROP TRIGGER IF EXISTS `tbi_invoices_create`;
DELIMITER $$
CREATE TRIGGER `tbi_invoices_create` BEFORE INSERT ON `invoices` FOR EACH ROW begin
        
        set new.create_at = CURRENT_TIMESTAMP;
        set new.update_at = CURRENT_TIMESTAMP;

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
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=latin1;

--
-- Volcado de datos para la tabla `lines_users`
--

INSERT INTO `lines_users` (`idline`, `linenumber`, `idtype`, `iduser`, `create_at`, `update_at`, `is_available`) VALUES
(1, '5893239', 2, 1, '2020-05-11 03:19:49', '2020-06-07 02:03:04', 1),
(2, '4841271', 1, 2, '2020-05-11 03:20:16', '2020-05-11 03:20:16', 1),
(3, '554433', 2, 1, '2020-06-06 05:42:57', '2020-06-06 05:42:57', 1);

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
(1, 'Residencial', '2020-05-11 03:17:36', '2020-05-11 03:17:36'),
(2, 'Celular', '2020-05-11 03:17:36', '2020-05-11 03:17:36');

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
  `create_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `update_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `province` (`province`)
) ENGINE=InnoDB AUTO_INCREMENT=30 DEFAULT CHARSET=latin1;

--
-- Volcado de datos para la tabla `provinces`
--

INSERT INTO `provinces` (`id`, `province`, `create_at`, `update_at`) VALUES
(1, 'Buenos Aires', '2020-05-11 02:38:25', '2020-05-11 02:38:25'),
(2, 'Catamarca', '2020-05-11 02:38:25', '2020-05-11 02:38:25'),
(3, 'Chaco', '2020-05-11 02:38:25', '2020-05-11 02:38:25'),
(4, 'Chubut', '2020-05-11 02:38:25', '2020-05-11 02:38:25'),
(5, 'Cordoba', '2020-05-11 02:38:26', '2020-05-11 02:38:26'),
(6, 'Corrientes', '2020-05-11 02:38:26', '2020-05-11 02:38:26'),
(7, 'Entre Rios', '2020-05-11 02:38:26', '2020-05-11 02:38:26'),
(8, 'Formosa', '2020-05-11 02:38:26', '2020-05-11 02:38:26'),
(9, 'Jujuy', '2020-05-11 02:38:26', '2020-05-11 02:38:26'),
(10, 'La Pampa', '2020-05-11 02:38:26', '2020-05-11 02:38:26'),
(11, 'La Rioja', '2020-05-11 02:38:26', '2020-05-11 02:38:26'),
(12, 'Mendoza', '2020-05-11 02:38:26', '2020-05-11 02:38:26'),
(13, 'Misiones', '2020-05-11 02:38:26', '2020-05-11 02:38:26'),
(14, 'Neuquen', '2020-05-11 02:38:26', '2020-05-11 02:38:26'),
(15, 'Rio Negro', '2020-05-11 02:38:26', '2020-05-11 02:38:26'),
(16, 'Salta', '2020-05-11 02:38:26', '2020-05-11 02:38:26'),
(17, 'San Juan', '2020-05-11 02:38:26', '2020-05-11 02:38:26'),
(18, 'San Luis', '2020-05-11 02:38:26', '2020-05-11 02:38:26'),
(20, 'Santa Fe', '2020-05-11 02:38:26', '2020-05-11 02:38:26'),
(21, 'Santiago del Estero', '2020-05-11 02:38:26', '2020-05-11 02:38:26'),
(22, 'Tierra del fuego', '2020-05-11 02:38:26', '2020-05-11 02:38:26'),
(23, 'Tucuman', '2020-05-11 02:38:26', '2020-05-11 02:38:26'),
(29, 'Santa Cruz', '2020-05-11 02:38:26', '2020-05-11 02:38:26');

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
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;

--
-- Volcado de datos para la tabla `rates`
--

INSERT INTO `rates` (`idrate`, `type`, `pricexmin`, `costprice`, `origincity`, `destinationcity`, `create_at`, `update_at`) VALUES
(1, 'local', 10, 5, 1, 1, '2020-05-11 03:23:04', '2020-05-11 03:23:04'),
(2, 'local', 12.5, 5, 1, 9, '2020-05-11 03:24:09', '2020-05-11 03:24:09');

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
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=latin1;

--
-- Volcado de datos para la tabla `users`
--

INSERT INTO `users` (`id`, `user`, `password`, `name`, `lastname`, `dni`, `idcity`, `usertype`, `create_at`, `update_at`, `is_available`) VALUES
(1, 'Patricio', '123456', 'Patricio', 'Diaz', '333333', 1, 'cliente', '2020-05-11 03:13:47', '2020-06-05 03:05:44', 1),
(2, 'Mariano', '123456', 'Mariano', 'Zanier', '333222', 1, 'cliente', '2020-05-11 03:16:52', '2020-06-05 03:06:05', 1),
(3, 'admin', '111111', 'admin', 'admin', '111111', 1, 'empleado', '2020-06-05 03:06:45', '2020-06-05 03:06:45', 1),
(7, 'antena', '000000', 'an', 'tena', '000000', 1, 'empleado', '2020-06-07 03:50:26', '2020-06-07 03:50:26', 1);

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
  ADD CONSTRAINT `invoices_ibfk_1` FOREIGN KEY (`iduser`) REFERENCES `users` (`id`);

--
-- Filtros para la tabla `lines_users`
--
ALTER TABLE `lines_users`
  ADD CONSTRAINT `lines_users_ibfk_1` FOREIGN KEY (`idtype`) REFERENCES `linetypes` (`idtype`),
  ADD CONSTRAINT `lines_users_ibfk_2` FOREIGN KEY (`iduser`) REFERENCES `users` (`id`);

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
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
