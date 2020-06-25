
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

