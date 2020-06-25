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

/*
--
-- Disparadores `provinces`
--*/
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