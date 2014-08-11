
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
	`preview_data` mediumtext NOT NULL,

	-- indexes
	PRIMARY KEY (`id`)
	
) ENGINE=InnoDB	DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;

CREATE TABLE IF NOT EXISTS `template` (
	`id` bigint(20) NOT NULL AUTO_INCREMENT,

	-- identification
	`template_family_id` bigint(20) NOT NULL,
	`language_key` varchar(255) NOT NULL,
	
	-- data
	`content` mediumtext NOT NULL,
	
	-- indexes
	PRIMARY KEY (`id`)
	
) ENGINE=InnoDB	DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;

CREATE TABLE IF NOT EXISTS `preview_data_set` (
	`id` bigint(20) NOT NULL AUTO_INCREMENT,

	-- identification
	`template_family_id` bigint(20) NOT NULL,
	
	-- data
	`data` mediumtext NOT NULL,
	
	-- indexes
	PRIMARY KEY (`id`)
	
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

INSERT INTO `template_family` (`id`, `key`, `preview_data`) VALUES
(1, 'foo', 'null');

INSERT INTO `template` (`id`, `template_family_id`, `language_key`, `content`) VALUES
(1, 1, 'en', 'This is a test template.');

INSERT INTO `preview_data_set` (`id`, `template_family_id`, `data`) VALUES
(1, 1, '{"foo": "bar", "baz": "blubber"}');

