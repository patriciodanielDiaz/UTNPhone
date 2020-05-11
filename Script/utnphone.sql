-- phpMyAdmin SQL Dump
-- version 4.8.5
-- https://www.phpmyadmin.net/
--
-- Servidor: 127.0.0.1:3306
-- Tiempo de generación: 11-05-2020 a las 03:38:46
-- Versión del servidor: 5.7.26
-- Versión de PHP: 7.2.18

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de datos: `utnphone`
--

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
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;

--
-- Volcado de datos para la tabla `calls`
--

INSERT INTO `calls` (`idcall`, `origincall`, `destinationcall`, `origincity`, `destinationcity`, `durationtime`, `price`, `costprice`, `total`, `idinvoice`, `idrate`, `create_at`, `update_at`) VALUES
(1, 2, 1, 1, 1, '02:00:00', NULL, NULL, NULL, NULL, NULL, '2020-05-11 03:33:25', '2020-05-11 03:33:25');

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
  KEY `idprovince` (`idprovince`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=latin1;

--
-- Volcado de datos para la tabla `cities`
--

INSERT INTO `cities` (`id`, `idprovince`, `city`, `prefix`, `create_at`, `update_at`) VALUES
(1, 2, 'Mar del plata', 223, '2020-05-11 02:59:02', '2020-05-11 02:59:02'),
(2, 2, 'Área Metropolitana', 11, '2020-05-11 02:59:02', '2020-05-11 02:59:02'),
(3, 2, '25 de Mayo', 2345, '2020-05-11 02:59:02', '2020-05-11 02:59:02'),
(4, 2, 'Bahía Blanca', 291, '2020-05-11 02:59:02', '2020-05-11 02:59:02'),
(5, 2, 'Chacabuco', 2352, '2020-05-11 02:59:02', '2020-05-11 02:59:02'),
(6, 2, 'Escobar', 3488, '2020-05-11 02:59:02', '2020-05-11 02:59:02'),
(7, 2, 'Maipu', 2268, '2020-05-11 02:59:02', '2020-05-11 02:59:02'),
(8, 2, 'Necochea', 2262, '2020-05-11 02:59:02', '2020-05-11 02:59:02'),
(9, 2, 'Pinamar', 2254, '2020-05-11 02:59:02', '2020-05-11 02:59:02'),
(10, 2, 'Zarate', 3487, '2020-05-11 02:59:03', '2020-05-11 02:59:03');

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
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

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
  PRIMARY KEY (`idline`),
  KEY `idtype` (`idtype`),
  KEY `iduser` (`iduser`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;

--
-- Volcado de datos para la tabla `lines_users`
--

INSERT INTO `lines_users` (`idline`, `linenumber`, `idtype`, `iduser`, `create_at`, `update_at`) VALUES
(1, '5893239', 2, 1, '2020-05-11 03:19:49', '2020-05-11 03:19:49'),
(2, '4841271', 1, 2, '2020-05-11 03:20:16', '2020-05-11 03:20:16');

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
  PRIMARY KEY (`idtype`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;

--
-- Volcado de datos para la tabla `linetypes`
--

INSERT INTO `linetypes` (`idtype`, `type`, `create_at`, `update_at`) VALUES
(1, 'Residencial', '2020-05-11 03:17:36', '2020-05-11 03:17:36'),
(2, 'Celular', '2020-05-11 03:17:36', '2020-05-11 03:17:36');

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
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=latin1;

--
-- Volcado de datos para la tabla `provinces`
--

INSERT INTO `provinces` (`id`, `province`, `create_at`, `update_at`) VALUES
(1, 'Buenos Aires', '2020-05-11 02:38:25', '2020-05-11 02:38:25'),
(2, 'Capital Federal', '2020-05-11 02:38:25', '2020-05-11 02:38:25'),
(3, 'Catamarca', '2020-05-11 02:38:25', '2020-05-11 02:38:25'),
(4, 'Chaco', '2020-05-11 02:38:25', '2020-05-11 02:38:25'),
(5, 'Chubut', '2020-05-11 02:38:25', '2020-05-11 02:38:25'),
(6, 'Cordoba', '2020-05-11 02:38:26', '2020-05-11 02:38:26'),
(7, 'Corrientes', '2020-05-11 02:38:26', '2020-05-11 02:38:26'),
(8, 'Entre Rios', '2020-05-11 02:38:26', '2020-05-11 02:38:26'),
(9, 'Formosa', '2020-05-11 02:38:26', '2020-05-11 02:38:26'),
(10, 'Jujuy', '2020-05-11 02:38:26', '2020-05-11 02:38:26'),
(11, 'La Pampa', '2020-05-11 02:38:26', '2020-05-11 02:38:26'),
(12, 'La Rioja', '2020-05-11 02:38:26', '2020-05-11 02:38:26'),
(13, 'Mendoza', '2020-05-11 02:38:26', '2020-05-11 02:38:26'),
(14, 'Misiones', '2020-05-11 02:38:26', '2020-05-11 02:38:26'),
(15, 'Neuquen', '2020-05-11 02:38:26', '2020-05-11 02:38:26'),
(16, 'Rio Negro', '2020-05-11 02:38:26', '2020-05-11 02:38:26'),
(17, 'Salta', '2020-05-11 02:38:26', '2020-05-11 02:38:26'),
(18, 'San Juan', '2020-05-11 02:38:26', '2020-05-11 02:38:26'),
(19, 'San Luis', '2020-05-11 02:38:26', '2020-05-11 02:38:26'),
(20, 'Santa Cruz', '2020-05-11 02:38:26', '2020-05-11 02:38:26'),
(21, 'Santa Fe', '2020-05-11 02:38:26', '2020-05-11 02:38:26'),
(22, 'Santiago del Estero', '2020-05-11 02:38:26', '2020-05-11 02:38:26'),
(23, 'Tierra del fuego', '2020-05-11 02:38:26', '2020-05-11 02:38:26'),
(24, 'Tucuman', '2020-05-11 02:38:26', '2020-05-11 02:38:26');

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

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `roles`
--

DROP TABLE IF EXISTS `roles`;
CREATE TABLE IF NOT EXISTS `roles` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `rol` varchar(50) DEFAULT NULL,
  `create_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `update_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;

--
-- Volcado de datos para la tabla `roles`
--

INSERT INTO `roles` (`id`, `rol`, `create_at`, `update_at`) VALUES
(1, 'empleado', '2020-05-11 02:40:18', '2020-05-11 02:40:18'),
(2, 'cliente', '2020-05-11 02:40:18', '2020-05-11 02:40:18');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `users`
--

DROP TABLE IF EXISTS `users`;
CREATE TABLE IF NOT EXISTS `users` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user` varchar(50) NOT NULL,
  `password` varchar(256) NOT NULL,
  `name` varchar(50) NOT NULL,
  `lastname` varchar(50) NOT NULL,
  `dni` varchar(10) NOT NULL,
  `idcity` int(11) NOT NULL,
  `idrole` int(11) NOT NULL,
  `usertype` enum('cliente','empleado') NOT NULL,
  `create_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `update_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idcity` (`idcity`),
  KEY `idrole` (`idrole`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;

--
-- Volcado de datos para la tabla `users`
--

INSERT INTO `users` (`id`, `user`, `password`, `name`, `lastname`, `dni`, `idcity`, `idrole`, `usertype`, `create_at`, `update_at`) VALUES
(1, 'patricio', '123456', 'Patricio', 'Diaz', '3333333', 1, 2, 'cliente', '2020-05-11 03:13:47', '2020-05-11 03:13:47'),
(2, 'mariano', '123456', 'Mariano', 'Zanier', '2222222', 1, 1, 'empleado', '2020-05-11 03:16:52', '2020-05-11 03:16:52');

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
  ADD CONSTRAINT `users_ibfk_1` FOREIGN KEY (`idcity`) REFERENCES `cities` (`id`),
  ADD CONSTRAINT `users_ibfk_2` FOREIGN KEY (`idrole`) REFERENCES `roles` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
