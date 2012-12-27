
SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";
DROP DATABASE `webide`;
CREATE DATABASE `webide` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;
USE `webide`;

-- -------------------------------------------------------------------------
-- - structure
-- -------------------------------------------------------------------------

CREATE TABLE IF NOT EXISTS `files` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255)  NOT NULL,
  `contents` blob NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;

CREATE TABLE IF NOT EXISTS `markers` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `origin` varchar(255)  NOT NULL,
  `meaning` varchar(255)  NOT NULL,
  `file_id` bigint(20) NOT NULL,
  `line` bigint(20) NOT NULL COMMENT '1-based',
  `column` bigint(20) NOT NULL COMMENT '1-based',
  `message` varchar(4096)  NOT NULL,
  PRIMARY KEY (`id`),
  KEY `markers_file_id` (`file_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;

CREATE TABLE IF NOT EXISTS `users` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `login_name` varchar(255)  NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;

CREATE TABLE IF NOT EXISTS `plugins` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;

CREATE TABLE IF NOT EXISTS `plugin_bundles` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `plugin_id` bigint(20) NOT NULL,
  `descriptor` MEDIUMTEXT NOT NULL,
  PRIMARY KEY (`id`),
  KEY `plugin_bundles_plugin_id` (`plugin_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;

CREATE TABLE IF NOT EXISTS `declared_extension_points` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `plugin_bundle_id` bigint(20) NULL,
  `name` varchar(255)  NOT NULL,
  PRIMARY KEY (`id`),
  KEY `declared_extension_points_plugin_bundle_id` (`plugin_bundle_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;

CREATE TABLE IF NOT EXISTS `declared_extensions` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `plugin_bundle_id` bigint(20) NOT NULL,
  `extension_point_name` varchar(255)  NOT NULL,
  `descriptor` MEDIUMTEXT NOT NULL,
  PRIMARY KEY (`id`),
  KEY `declared_extensions_plugin_bundle_id` (`plugin_bundle_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;

CREATE TABLE IF NOT EXISTS `user_plugins` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NOT NULL,
  `plugin_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `user_plugins_user_id` (`user_id`),
  KEY `user_plugins_plugin_id` (`plugin_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;

CREATE TABLE IF NOT EXISTS `extension_bindings` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NOT NULL,
  `declared_extension_point_id` bigint(20) NOT NULL,
  `declared_extension_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `extension_bindings_user_id` (`user_id`),
  KEY `extension_bindings_declared_extension_point_id` (`declared_extension_point_id`),
  KEY `extension_bindings_declared_extension_id` (`declared_extension_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;

CREATE TABLE IF NOT EXISTS `plugin_bundle_states` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NOT NULL,
  `plugin_bundle_id` bigint(20) NOT NULL,
  `section` int NOT NULL,
  `data` LONGBLOB NOT NULL,
  PRIMARY KEY (`id`),
  KEY `plugin_bundle_states_user_id` (`user_id`),
  KEY `plugin_bundle_states_plugin_bundle_id` (`plugin_bundle_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;

-- -------------------------------------------------------------------------
-- - data
-- -------------------------------------------------------------------------

INSERT INTO  `users` (`id`, `login_name`) VALUES
(NULL,  'martin');

INSERT INTO `files` (`id`, `name`, `contents`) VALUES
(NULL, 'Helper.java', 0x7075626c696320636c6173732048656c706572207b0d0a0d0a20207075626c69632073746174696320766f69642068656c702829207b0d0a2020202053797374656d2e6f75742e7072696e746c6e282248656c7065722069732068656c70696e672122293b0d0a20207d0d0a0d0a7d),
(NULL, 'Main.java', 0x0d0a7075626c696320636c617373204d61696e207b0d0a20207075626c69632073746174696320766f6964206d61696e28537472696e675b5d206172677329207b0d0a2020202053797374656d2e6f75742e7072696e746c6e282248656c6c6f20576f726c642122293b0d0a2020202048656c7065722e68656c7028293b0d0a20207d0d0a7d0d0a);

INSERT INTO `declared_extension_points` (`id`, `plugin_bundle_id`, `name`) VALUES
(NULL , NULL ,  'webide.context_menu.resource');

INSERT INTO `plugins` (`id`) VALUES
(NULL);

INSERT INTO `plugin_bundles` (`id`, `plugin_id`, `descriptor`) VALUES
(NULL, '1', '{"extensions": {"webide.context_menu.resource": ["Hello World!", "Hello again!"]}}');

INSERT INTO  `user_plugins` (`id`, `user_id`, `plugin_id`) VALUES
(NULL, '1', '1');

-- -------------------------------------------------------------------------
-- - constraints
-- -------------------------------------------------------------------------

ALTER TABLE `markers` ADD CONSTRAINT `markers_ibfk_1` FOREIGN KEY (`file_id`) REFERENCES `files` (`id`) ON DELETE CASCADE;
ALTER TABLE `plugin_bundles` ADD CONSTRAINT `plugin_bundles_ibfk_1` FOREIGN KEY (`plugin_id`) REFERENCES `plugins` (`id`) ON DELETE CASCADE;
ALTER TABLE `declared_extension_points` ADD CONSTRAINT `declared_extension_points_ibfk_1` FOREIGN KEY (`plugin_bundle_id`) REFERENCES `plugin_bundles` (`id`) ON DELETE CASCADE;
ALTER TABLE `declared_extensions` ADD CONSTRAINT `declared_extensions_ibfk_1` FOREIGN KEY (`plugin_bundle_id`) REFERENCES `plugin_bundles` (`id`) ON DELETE CASCADE;
ALTER TABLE `extension_bindings` ADD CONSTRAINT `extension_bindings_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE;
ALTER TABLE `extension_bindings` ADD CONSTRAINT `extension_bindings_ibfk_2` FOREIGN KEY (`declared_extension_point_id`) REFERENCES `declared_extension_points` (`id`) ON DELETE CASCADE;
ALTER TABLE `extension_bindings` ADD CONSTRAINT `extension_bindings_ibfk_3` FOREIGN KEY (`declared_extension_id`) REFERENCES `declared_extensions` (`id`) ON DELETE CASCADE;
ALTER TABLE `plugin_bundle_states` ADD CONSTRAINT `plugin_bundle_states_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE;
ALTER TABLE `plugin_bundle_states` ADD CONSTRAINT `plugin_bundle_states_ibfk_2` FOREIGN KEY (`plugin_bundle_id`) REFERENCES `plugin_bundles` (`id`) ON DELETE CASCADE;
ALTER TABLE `user_plugins` ADD CONSTRAINT `user_plugins_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE;
ALTER TABLE `user_plugins` ADD CONSTRAINT `user_plugins_ibfk_2` FOREIGN KEY (`plugin_id`) REFERENCES `plugins` (`id`) ON DELETE CASCADE;
