
SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";
DROP DATABASE `miner`;
CREATE DATABASE `miner` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;
USE `miner`;

-- -------------------------------------------------------------------------
-- - structure
-- -------------------------------------------------------------------------


-- user account data
-- --------------------------

CREATE TABLE IF NOT EXISTS `user_account` (
	`id` bigint(20) NOT NULL AUTO_INCREMENT,
	`username` varchar(255) NOT NULL,
	`password_hash` varchar(255) NOT NULL,
	`deleted` tinyint(1) NOT NULL DEFAULT 0,
	PRIMARY KEY (`id`)
) ENGINE=InnoDB	DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;



-- player data
-- --------------------------

CREATE TABLE IF NOT EXISTS `player` (
	`id` bigint(20) NOT NULL AUTO_INCREMENT,
	`user_account_id` bigint(20) NOT NULL,
	`name` VARCHAR(255) NOT NULL,
	`faction_id` bigint(20) NOT NULL,
	`x` decimal(10,2) NOT NULL,
	`y` decimal(10,2) NOT NULL,
	`z` decimal(10,2) NOT NULL,
	`left_angle` decimal(5,2) NOT NULL,
	`up_angle` decimal(5,2) NOT NULL,
	`coins` bigint(20) NOT NULL,
	`deleted` tinyint(1) NOT NULL DEFAULT 0,
	PRIMARY KEY (`id`)
) ENGINE=InnoDB	DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;

CREATE TABLE IF NOT EXISTS `player_awarded_achievement` (
	`id` bigint(20) NOT NULL AUTO_INCREMENT,
	`player_id` bigint(20) NOT NULL,
	`achievement_code` varchar(255) NOT NULL,
	PRIMARY KEY (`id`),
	UNIQUE INDEX `main` (`player_id`, `achievement_code`)
) ENGINE=InnoDB	DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;

CREATE TABLE IF NOT EXISTS `player_inventory_slot` (
	`id` bigint(20) NOT NULL AUTO_INCREMENT,
	`player_id` bigint(20) NOT NULL,
	`equipped` tinyint(1) NOT NULL,
	`index` integer NOT NULL,
	`type` integer NOT NULL,
	`quantity` integer NOT NULL,
	PRIMARY KEY (`id`),
	INDEX `main` (`player_id`, `equipped`, `index`)
) ENGINE=InnoDB	DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;



-- faction data
-- --------------------------

CREATE TABLE IF NOT EXISTS `faction` (
	`id` bigint(20) NOT NULL,
	`score` bigint(20) NOT NULL,
	`divine_power` bigint(20) NOT NULL,
	PRIMARY KEY (`id`)
) ENGINE=InnoDB	DEFAULT CHARSET=utf8;








-- -------------------------------------------------------------------------
-- - data
-- -------------------------------------------------------------------------


-- factions
-- --------------------------

INSERT INTO `faction` (`id`, `score`, `divine_power`) VALUES
(0, 0, 0),
(1, 0, 0),
(2, 0, 0),
(3, 0, 0);


-- users, players and related data
-- --------------------------

INSERT INTO `user_account` (`id`, `username`, `password_hash`) VALUES
(1, 'martin', '$2a$12$.5KM.jQ/TnPn7bMET7.lO.CnGxUzssEr8w590eYQYl8XRkui2OCg6');

INSERT INTO	`player` (`id`, `user_account_id`, `name`, `faction_id`, `coins`) VALUES
(1,	1, 'Big Boss', 1, 123);




-- -------------------------------------------------------------------------
-- - constraints
-- -------------------------------------------------------------------------

-- player data
ALTER TABLE `player` ADD CONSTRAINT `player_ibfk_1` FOREIGN KEY (`user_account_id`) REFERENCES `user_account` (`id`);
ALTER TABLE `player` ADD CONSTRAINT `player_ibfk_2` FOREIGN KEY (`faction_id`) REFERENCES `faction` (`id`);
ALTER TABLE `player_awarded_achievement` ADD CONSTRAINT `player_awarded_achievement_ibfk_1` FOREIGN KEY (`player_id`) REFERENCES `player` (`id`);
ALTER TABLE `player_inventory_slot` ADD CONSTRAINT `player_inventory_slot_ibfk_1` FOREIGN KEY (`player_id`) REFERENCES `player` (`id`);
