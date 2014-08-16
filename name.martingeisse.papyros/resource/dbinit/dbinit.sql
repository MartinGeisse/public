
SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";
CREATE DATABASE `papyros` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;
USE `papyros`;

-- -------------------------------------------------------------------------
-- - structure
-- -------------------------------------------------------------------------

CREATE TABLE IF NOT EXISTS `template_family` (
	`id` bigint(20) NOT NULL AUTO_INCREMENT,

	-- identification
	`key` varchar(255) NOT NULL,

	-- data
	`name` varchar(255) NOT NULL,

	-- indexes
	PRIMARY KEY (`id`),
	UNIQUE INDEX `key_index` (`key`)
	
) ENGINE=InnoDB	DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;

CREATE TABLE IF NOT EXISTS `template` (
	`id` bigint(20) NOT NULL AUTO_INCREMENT,

	-- identification
	`template_family_id` bigint(20) NOT NULL,
	`language_key` varchar(255) NOT NULL,
	
	-- data
	`content` mediumtext NOT NULL,
	
	-- indexes
	PRIMARY KEY (`id`),
	UNIQUE INDEX `language_key_index` (`template_family_id`, `language_key`)
	
) ENGINE=InnoDB	DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;

CREATE TABLE IF NOT EXISTS `preview_data_set` (
	`id` bigint(20) NOT NULL AUTO_INCREMENT,

	-- identification
	`template_family_id` bigint(20) NOT NULL,
	`preview_data_key` varchar(255) NOT NULL,
	`order_index` int NOT NULL,
	
	-- data
	`name` varchar(255) NOT NULL,
	`data` mediumtext NOT NULL,
	
	-- indexes
	PRIMARY KEY (`id`),
	UNIQUE INDEX `preview_data_key_index` (`template_family_id`, `preview_data_key`),
	INDEX `order_index_index` (`template_family_id`, `order_index`)
	
) ENGINE=InnoDB	DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;







-- -------------------------------------------------------------------------
-- - constraints
-- -------------------------------------------------------------------------

-- dummy
ALTER TABLE `template` ADD CONSTRAINT `template_fk_1` FOREIGN KEY (`template_family_id`) REFERENCES `template_family` (`id`) ON DELETE CASCADE;
ALTER TABLE `preview_data_set` ADD CONSTRAINT `preview_data_set_fk_1` FOREIGN KEY (`template_family_id`) REFERENCES `template_family` (`id`) ON DELETE CASCADE;





-- -------------------------------------------------------------------------
-- - static data
-- -------------------------------------------------------------------------

-- nothing yet






-- -------------------------------------------------------------------------
-- - test data
-- -------------------------------------------------------------------------

INSERT INTO `template_family` (`id`, `key`, `name`) VALUES
(1, 'order.confirmation.consumer', 'Bestellbestätigung (Endkunde)'),
(2, 'order.confirmation.merchant', 'Bestellbestätigung (Händler)');

INSERT INTO `template` (`id`, `template_family_id`, `language_key`, `content`) VALUES
(1, 1, 'en', 'Thank you for your order.'),
(2, 1, 'de', 'Vielen Dank für Ihre Bestellung.'),
(3, 2, 'en', 'A new order has been placed.'),
(4, 2, 'de', 'Eine neue Bestellung wurde aufgegeben.');

INSERT INTO `preview_data_set` (`id`, `template_family_id`, `preview_data_key`, `order_index`, `name`, `data`) VALUES
(1, 1, 'eins', 0, 'Testdaten Eins', '{"foo": "bar", "baz": "blubber"}'),
(2, 1, 'zwei', 1, 'Testdaten Zwei', '{"foo": "eins", "baz": "zwei"}');
