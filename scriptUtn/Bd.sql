SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET GLOBAL time_zone = "-03:00";
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


-- Restricciones para tablas volcadas
-
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





