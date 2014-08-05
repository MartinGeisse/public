
SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";
CREATE DATABASE `papyros` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;
USE `papyros`;

-- -------------------------------------------------------------------------
-- - structure
-- -------------------------------------------------------------------------


-- users and related data
-- --------------------------

CREATE TABLE IF NOT EXISTS `dummy` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,

  -- data fields
  `foo` varchar(255) NOT NULL,
  
  -- indexes
  PRIMARY KEY (`id`)
  
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;






-- -------------------------------------------------------------------------
-- - constraints
-- -------------------------------------------------------------------------

-- dummy
-- ALTER TABLE `dummy` ADD CONSTRAINT `dummy_fk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE;





-- -------------------------------------------------------------------------
-- - static data
-- -------------------------------------------------------------------------

-- nothing yet






-- -------------------------------------------------------------------------
-- - test data
-- -------------------------------------------------------------------------

-- INSERT INTO `user` (`id`, `login_name`, `password_hash_hex`, `display_name`, `user_type`, `email`) VALUES
-- (1, 'system', '', 'System', 'SYSTEM', ''),
-- (2, 'martingeisse@googlemail.com', 'foobar', 'Martin Geisse', 'SUPERADMIN', 'martingeisse@googlemail.com');
