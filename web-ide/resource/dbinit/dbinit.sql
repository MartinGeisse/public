
SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";
DROP DATABASE `webide`;
CREATE DATABASE `webide` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;
USE `webide`;

-- -------------------------------------------------------------------------
-- - structure
-- -------------------------------------------------------------------------

CREATE TABLE IF NOT EXISTS `workspace_resources` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `type` varchar(255) NOT NULL,
  `parent_id` bigint(20) NULL,
  `contents` blob NOT NULL,
  PRIMARY KEY (`id`),
  KEY `workspace_resources_parent_id` (`parent_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;

CREATE TABLE IF NOT EXISTS `markers` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `origin` varchar(255)  NOT NULL,
  `meaning` varchar(255)  NOT NULL,
  `workspace_resource_id` bigint(20) NOT NULL,
  `line` bigint(20) NOT NULL COMMENT '1-based',
  `column` bigint(20) NOT NULL COMMENT '1-based',
  `message` varchar(4096)  NOT NULL,
  PRIMARY KEY (`id`),
  KEY `markers_workspace_resource_id` (`workspace_resource_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;

CREATE TABLE IF NOT EXISTS `users` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `login_name` varchar(255)  NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;

CREATE TABLE IF NOT EXISTS `plugins` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `is_unpacked` tinyint(1) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;

CREATE TABLE IF NOT EXISTS `plugin_bundles` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `plugin_id` bigint(20) NOT NULL,
  `descriptor` MEDIUMTEXT NOT NULL,
  `jarfile` LONGBLOB NOT NULL,
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

INSERT INTO `workspace_resources` (`id`, `name`, `type`, `parent_id`, `contents`) VALUES
(NULL, '', 'MOUNT_SPACE', NULL, 0),
(NULL, '', 'WORKSPACE_ROOT', 1, 0),
(NULL, 'test-project', 'PROJECT', 2, 0),
(NULL, 'src', 'FOLDER', 3, 0),
(NULL, 'Helper.java', 'FILE', 4, 0x7075626c696320636c6173732048656c706572207b0d0a0d0a20207075626c69632073746174696320766f69642068656c702829207b0d0a2020202053797374656d2e6f75742e7072696e746c6e282248656c7065722069732068656c70696e672122293b0d0a20207d0d0a0d0a7d),
(NULL, 'Main.java', 'FILE', 4, 0x0d0a7075626c696320636c617373204d61696e207b0d0a20207075626c69632073746174696320766f6964206d61696e28537472696e675b5d206172677329207b0d0a2020202053797374656d2e6f75742e7072696e746c6e282248656c6c6f20576f726c642122293b0d0a2020202048656c7065722e68656c7028293b0d0a20207d0d0a7d0d0a);

INSERT INTO `declared_extension_points` (`id`, `plugin_bundle_id`, `name`) VALUES
(NULL, NULL ,  'webide.context_menu.resource');

INSERT INTO `plugins` (`id`, `is_unpacked`) VALUES
(NULL, 0);

INSERT INTO `plugin_bundles` (`id`, `plugin_id`, `descriptor`, `jarfile`) VALUES
(NULL, 1, '{"extensions": {"webide.context_menu.resource": ["name.martingeisse.webide_plugin.MyRunnable1", "name.martingeisse.webide_plugin.MyRunnable2"]}}', 0x504b03040a0000080000b4899d41000000000000000000000000090004004d4554412d494e462ffeca0000504b03040a0000080800b3899d41953b36685f0000006b000000140000004d4554412d494e462f4d414e49464553542e4d46f34dcccb4c4b2d2ed10d4b2d2acecccfb35230d433e0e572cc4312712c484cce4855008a01252df48c78b99c8b52134b5253749d2a41eacdf40ce28d8d75930c8c754d8c4c740d0d7c8dcd8d0c14341c0b0a7252153cf392f53479b978b900504b03040a0000080000c2869d41000000000000000000000000050000006e616d652f504b03040a0000080000c2869d41000000000000000000000000120000006e616d652f6d617274696e6765697373652f504b03040a0000080000aa899d41000000000000000000000000200000006e616d652f6d617274696e6765697373652f7765626964655f706c7567696e2f504b03040a0000080800aa899d411e144e5bb9010000e8020000310000006e616d652f6d617274696e6765697373652f7765626964655f706c7567696e2f4d7952756e6e61626c65312e636c6173739552cb6ed340143d93a47171dd07a64d81f0486051870223d8a642aa2a5550199092b65b347146ce04676cc6631e9fc58ac7820fe0a310d749da4482052c7cafee9d73ce9d39d73f7f7dff01e029ee3ba830ec6b31917c228c553a962acf25ff20076a28df6449112bcd5f7eea155a8b41229f38a8316c8dc57bc113a163fe7a309691755067f017dd0b3843fd4069659f315483ce3943ed281d4a1755ac7970b0cab0192a2d5f15938134a733861fa69148ce8551653d6fd6ec48e50c8fc2ffb86a97869a42afc267688a441a1bec3d974992b666d8d6c961afbdd7e9bad846c3c10ec9ff455d456fa5e58555093f1c8b8f3df9ae90b93da3dac32eae336cc7d22e9d9c0a430d868741274c4dcc4526a2d1a58e2020ff03dd5dc34ddc72d06408fe95e3e136eed02a4496493d3c21ebfb9151194d6e06e16213472361fa254f47b25b6ec02dcff239d45f42f6ada15793696e3f2d4c248f5569fcd692a18f4b30da205fe95ba13fa84299d648f10a559c32a3bcf2e00bdccfd3638f627ddadcc03a456f06a06a9332c95f925f10ba4279ddbf1a7ec5b5fd6fb851c142648366023b141b288d2fc51a33c25cac0a1777d19adea13de5ddfb0d504b03040a0000080800b1899d411ec9cbd3c4010000f8020000310000006e616d652f6d617274696e6765697373652f7765626964655f706c7567696e2f4d7952756e6e61626c65322e636c6173739552db6ed340103d9ba47171dd0ba60d977271e0a10e0556ea6b1052550989ca809484be561b677136386bb35e73f92c9eb83cf0017c14629c8426123c8064cf6866ce39b33bb33f7e7efb0ee008f71cd4180eb5984a3e15c62a9d48551492bf97433592e7795a264af3e71f7ba5d66298ca23070d869d897827782a74c25f0e2732b60e9a0cfe32fb1bced07cacb4b24f18ea61e78ca171928da48b3a363c385867d88e94962fcae9509ac19ce147592cd2336154152f920d3b5605c3c3e83f8edaa5a6a6d4ebf0e98a2295c68607039209e813699105af4d360dec5806736e707adc6b1f74ba2e76d172b047edfed24dc56fa4e5a555293f9e880f3df9b694857d45b187abb8c6b09b48bb521908430986076127ca4cc2452ee2f1858e2020ff03ddddc00ddc74b0cf10fe2bc7c32ddca6d5883c977a744aabe8c746e5d4793f8c969b39190bd3af783a96dd6a236e552b16507f05d9b7866e4d4374fb596962f954558bd85919f0a30a8c3668cef4afd18baa91a7b592bd441127cfc8afddff0cf7d3acec916dce925bd824ebcd01146d9327f90bf23342d7c86ffa97a32fb872f815d76b588a6c514f608f6c0bd5e02bb1d69cb010abc3c51d04b333b467bcbbbf00504b010214030a0000080000b4899d41000000000000000000000000090004000000000000001000ed41000000004d4554412d494e462ffeca0000504b010214030a0000080800b3899d41953b36685f0000006b000000140000000000000000000000a4812b0000004d4554412d494e462f4d414e49464553542e4d46504b010214030a0000080000c2869d41000000000000000000000000050000000000000000001000ed41bc0000006e616d652f504b010214030a0000080000c2869d41000000000000000000000000120000000000000000001000ed41df0000006e616d652f6d617274696e6765697373652f504b010214030a0000080000aa899d41000000000000000000000000200000000000000000001000ed410f0100006e616d652f6d617274696e6765697373652f7765626964655f706c7567696e2f504b010214030a0000080800aa899d411e144e5bb9010000e8020000310000000000000000000000a4814d0100006e616d652f6d617274696e6765697373652f7765626964655f706c7567696e2f4d7952756e6e61626c65312e636c617373504b010214030a0000080800b1899d411ec9cbd3c4010000f8020000310000000000000000000000a481550300006e616d652f6d617274696e6765697373652f7765626964655f706c7567696e2f4d7952756e6e61626c65322e636c617373504b05060000000007000700fc010000680500000000);

INSERT INTO  `user_plugins` (`id`, `user_id`, `plugin_id`) VALUES
(NULL, '1', '1');

-- -------------------------------------------------------------------------
-- - constraints
-- -------------------------------------------------------------------------

ALTER TABLE `workspace_resources` ADD CONSTRAINT `workspace_resources_ibfk_1` FOREIGN KEY (`parent_id`) REFERENCES `workspace_resources` (`id`) ON DELETE CASCADE;
ALTER TABLE `markers` ADD CONSTRAINT `markers_ibfk_1` FOREIGN KEY (`workspace_resource_id`) REFERENCES `workspace_resources` (`id`) ON DELETE CASCADE;
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
