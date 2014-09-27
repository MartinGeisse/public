
SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";
CREATE DATABASE `slave_services` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;
USE `slave_services`;

-- -------------------------------------------------------------------------
-- - structure (Babel)
-- -------------------------------------------------------------------------

CREATE TABLE IF NOT EXISTS `message_family` (
	`id` bigint(20) NOT NULL AUTO_INCREMENT,

	-- identification
	`domain` varchar(255) NOT NULL,
	`message_key` varchar(255) NOT NULL,

	-- original text in "developer's english"
	`developer_text` varchar(65535) NOT NULL,

	-- indexes
	PRIMARY KEY (`id`),
	UNIQUE INDEX `main_index` (`domain`, `message_key`)

) ENGINE=InnoDB	DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;

CREATE TABLE IF NOT EXISTS `message_translation` (
	`id` bigint(20) NOT NULL AUTO_INCREMENT,

	-- identification
	`message_family_id` bigint(20) NOT NULL,
	`language_key` varchar(255) NOT NULL,

	-- data
	`text` varchar(65535) NOT NULL,

	-- indexes
	PRIMARY KEY (`id`),
	UNIQUE INDEX `main_index` (`message_family_id`, `language_key`)

) ENGINE=InnoDB	DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;

-- -------------------------------------------------------------------------
-- - structure (Papyros)
-- -------------------------------------------------------------------------

CREATE TABLE IF NOT EXISTS `template_family` (
	`id` bigint(20) NOT NULL AUTO_INCREMENT,

	-- identification
	`key` varchar(255) NOT NULL,

	-- data
	`name` varchar(255) NOT NULL,
	`schema` mediumtext NOT NULL,

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

CREATE TABLE IF NOT EXISTS `template_preview_data_set` (
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
ALTER TABLE `message_translation` ADD CONSTRAINT `message_translation_fk_1` FOREIGN KEY (`message_family_id`) REFERENCES `message_family` (`id`) ON DELETE CASCADE;
ALTER TABLE `template` ADD CONSTRAINT `template_fk_1` FOREIGN KEY (`template_family_id`) REFERENCES `template_family` (`id`) ON DELETE CASCADE;
ALTER TABLE `template_preview_data_set` ADD CONSTRAINT `template_preview_data_set_fk_1` FOREIGN KEY (`template_family_id`) REFERENCES `template_family` (`id`) ON DELETE CASCADE;





-- -------------------------------------------------------------------------
-- - static data
-- -------------------------------------------------------------------------

-- nothing yet






-- -------------------------------------------------------------------------
-- - test data
-- -------------------------------------------------------------------------

INSERT INTO `slave_services`.`message_family` (`id`, `domain`, `message_key`, `developer_text`) VALUES
(1, 'admin', 'admin.foo.title', 'this is the title of the foo page'),
(2, 'admin', 'admin.bar', 'text for the bottom bar'),
(3, 'app', 'app.murx.sonstwas', 'placeholder text for the yellow box');

INSERT INTO `slave_services`.`message_translation` (`id`, `message_family_id`, `language_key`, `text`) VALUES
(1, 1, 'en-us', 'This is a title.'),
(2, 2, 'en-us', 'This text has not been translated yet.'),
(3, 1, 'de-de', 'Das hier ist eine Titelzeile.'),
(4, 3, 'de-de', 'noch ein anderer text'),
(5, 3, 'fr-fr', 'pourtant, un autre texte');

INSERT INTO `template_family` (`id`, `key`, `name`, `schema`) VALUES
(1, 'order.confirmation.consumer', 'Bestellbestätigung (Endkunde)', 'null'),
(2, 'order.confirmation.merchant', 'Bestellbestätigung (Händler)', 'null');

INSERT INTO `template` (`id`, `template_family_id`, `language_key`, `content`) VALUES
(1, 1, 'en', 'Hello {{name}}! Thank you for your order.'),
(2, 1, 'de', 'Hallo {{name}}! Vielen Dank für Ihre Bestellung.'),
(3, 2, 'en', 'A new order has been placed.'),
(4, 2, 'de', 'Eine neue Bestellung wurde aufgegeben.');

INSERT INTO `template_preview_data_set` (`id`, `template_family_id`, `preview_data_key`, `order_index`, `name`, `data`) VALUES
(1, 1, 'eins', 0, 'Testdaten Eins', '{"name": "Max Mustermann"}'),
(2, 1, 'zwei', 1, 'Testdaten Zwei', '{"name": "Thomas Test"}');
