
SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";
CREATE DATABASE `forum` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;
USE `forum`;

-- -------------------------------------------------------------------------
-- - structure
-- -------------------------------------------------------------------------


-- conversations
-- --------------------------

CREATE TABLE IF NOT EXISTS `conversation` (
	`id` bigint(20) NOT NULL AUTO_INCREMENT,

	-- data fields
	`name` varchar(255) NOT NULL,

	-- indexes
	PRIMARY KEY (`id`)

) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=1;

CREATE TABLE IF NOT EXISTS `post_base` (
	`id` bigint(20) NOT NULL AUTO_INCREMENT,

	-- data fields
	`conversation_id` bigint(20) NOT NULL,
	`order_index` integer NOT NULL,
	`author_name` varchar(255) NOT NULL,
	`author_identicon_code` bigint(20) NOT NULL,
	`author_ip_address` varchar(255) NOT NULL,

	-- indexes
	PRIMARY KEY (`id`)

) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=1;

CREATE TABLE IF NOT EXISTS `post_text` (
	`id` bigint(20) NOT NULL AUTO_INCREMENT,

	-- data fields
	`post_base_id` bigint(20) NOT NULL,
	`text` varchar(255) NOT NULL,

	-- indexes
	PRIMARY KEY (`id`)

) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=1;

CREATE TABLE IF NOT EXISTS `post_image` (
	`id` bigint(20) NOT NULL AUTO_INCREMENT,

	-- data fields
	`post_base_id` bigint(20) NOT NULL,
	`content_type` varchar(255) NOT NULL,
	`data` longblob NOT NULL,

	-- indexes
	PRIMARY KEY (`id`)

) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=1;






-- -------------------------------------------------------------------------
-- - constraints
-- -------------------------------------------------------------------------

-- conversations
ALTER TABLE `post_base` ADD CONSTRAINT `post_base_fk_1` FOREIGN KEY (`conversation_id`) REFERENCES `conversation` (`id`) ON DELETE CASCADE;
ALTER TABLE `post_text` ADD CONSTRAINT `post_text_fk_1` FOREIGN KEY (`post_base_id`) REFERENCES `post_base` (`id`) ON DELETE CASCADE;
ALTER TABLE `post_image` ADD CONSTRAINT `post_image_fk_1` FOREIGN KEY (`post_base_id`) REFERENCES `post_base` (`id`) ON DELETE CASCADE;





-- -------------------------------------------------------------------------
-- - static data
-- -------------------------------------------------------------------------

-- nothing yet






-- -------------------------------------------------------------------------
-- - test data
-- -------------------------------------------------------------------------

INSERT INTO `conversation` (`id`, `name`) VALUES
(1, 'test');

INSERT INTO `post_base` (`id`, `conversation_id`, `order_index`, `author_name`, `author_identicon_code`, `author_ip_address`) VALUES
(1, 1, 0, 'Martin', 0, '127.0.0.1'),
(2, 1, 1, 'Bob', 42, '127.0.0.1'),
(3, 1, 2, 'Martin', 0, '127.0.0.1');

INSERT INTO `post_text` (`id`, `post_base_id`, `text`) VALUES
(1, 1, 'Hello World!'),
(2, 2, 'Hello there!'),
(3, 3, 'sup?');
