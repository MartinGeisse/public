
SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";
DROP DATABASE `webide`;
CREATE DATABASE `webide` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;
USE `webide`;

-- -------------------------------------------------------------------------
-- - structure
-- -------------------------------------------------------------------------


-- users and related data
-- --------------------------

CREATE TABLE IF NOT EXISTS `users` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `login_name` varchar(255)  NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX (`login_name`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;




-- workspaces
-- --------------------------

CREATE TABLE IF NOT EXISTS `workspaces` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `is_building` tinyint(1) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `name` (`name`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;

CREATE TABLE IF NOT EXISTS `workspace_resources` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `workspace_id` bigint(20) NOT NULL,
  `name` varchar(255) NOT NULL,
  `type` varchar(255) NOT NULL,
  `parent_id` bigint(20) NULL,
  `contents` blob NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `workspace_id` (`workspace_id`),
  UNIQUE INDEX `parent_id_name_unique` (`parent_id`, `name`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;

CREATE TABLE IF NOT EXISTS `markers` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `workspace_id` bigint(20) NOT NULL,
  `origin` varchar(255)  NOT NULL,
  `meaning` varchar(255)  NOT NULL,
  `workspace_resource_id` bigint(20) NOT NULL,
  `line` bigint(20) NOT NULL COMMENT '1-based',
  `column` bigint(20) NOT NULL COMMENT '1-based',
  `message` varchar(4096)  NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `workspace_id` (`workspace_id`),
  INDEX `markers_workspace_resource_id` (`workspace_resource_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;

CREATE TABLE IF NOT EXISTS `workspace_resource_deltas` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `workspace_id` bigint(20) NOT NULL,
  `path` varchar(4096) NOT NULL,
  `is_deep` tinyint(1) NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `workspace_id` (`workspace_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;

CREATE TABLE IF NOT EXISTS `workspace_tasks` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `workspace_id` bigint(20) NOT NULL,
  `command` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `workspace_id` (`workspace_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;

CREATE TABLE IF NOT EXISTS `workspace_builders` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `workspace_id` bigint(20) NOT NULL,
  `plugin_bundle_id` bigint(20) NOT NULL,
  `staging_path` varchar(4096) NULL,
  `builder_name` varchar(255) NOT NULL,
  `builder_class` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `workspace_id` (`workspace_id`),
  INDEX `plugin_bundle_id` (`plugin_bundle_id`),
  INDEX `staging_path` (`staging_path`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;

CREATE TABLE IF NOT EXISTS `workspace_build_triggers` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `workspace_id` bigint(20) NOT NULL,
  `workspace_builder_id` bigint(20) NOT NULL,
  `trigger_base_path` varchar(4096) NULL,
  `path_pattern` varchar(255) NULL,
  `buildscript_path` varchar(4096) NULL,
  PRIMARY KEY (`id`),
  INDEX `workspace_id_and_base_path` (`workspace_id`, `trigger_base_path`),
  INDEX `workspace_builder_id` (`workspace_builder_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;




-- available plugins
-- --------------------------

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
  INDEX `plugin_bundles_plugin_id` (`plugin_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;

CREATE TABLE IF NOT EXISTS `declared_extension_points` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `plugin_bundle_id` bigint(20) NOT NULL,
  `name` varchar(255)  NOT NULL,
  `on_change_cleared_section` INTEGER,
  PRIMARY KEY (`id`),
  INDEX `declared_extension_points_plugin_bundle_id` (`plugin_bundle_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;

CREATE TABLE IF NOT EXISTS `declared_extensions` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `plugin_bundle_id` bigint(20) NOT NULL,
  `extension_point_name` varchar(255)  NOT NULL,
  `descriptor` MEDIUMTEXT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `declared_extensions_plugin_bundle_id` (`plugin_bundle_id`),
  INDEX (`id`, `extension_point_name`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;




-- activated plugins
-- --------------------------

CREATE TABLE IF NOT EXISTS `builtin_plugins` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `plugin_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `user_plugins_plugin_id` (`plugin_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;

CREATE TABLE IF NOT EXISTS `user_installed_plugins` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NOT NULL,
  `plugin_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `user_plugins_user_id` (`user_id`),
  INDEX `user_plugins_plugin_id` (`plugin_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;

CREATE TABLE IF NOT EXISTS `workspace_staging_plugins` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `workspace_id` bigint(20) NOT NULL,
  `plugin_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `workspace_staging_plugins_workspace_id` (`workspace_id`),
  INDEX `workspace_staging_plugins_plugin_id` (`plugin_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;




-- plugin run-time information
-- --------------------------

CREATE TABLE IF NOT EXISTS `extension_bindings` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NOT NULL,
  `declared_extension_point_id` bigint(20) NOT NULL,
  `declared_extension_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `extension_bindings_user_id` (`user_id`),
  INDEX `extension_bindings_declared_extension_point_id` (`declared_extension_point_id`),
  INDEX `extension_bindings_declared_extension_id` (`declared_extension_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;

CREATE TABLE IF NOT EXISTS `plugin_bundle_states` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NOT NULL,
  `plugin_bundle_id` bigint(20) NOT NULL,
  `section` int NOT NULL,
  `data` LONGBLOB NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `plugin_bundle_states_plugin_bundle_id` (`plugin_bundle_id`),
  INDEX (`user_id`, `plugin_bundle_id`, `section`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;



-- -------------------------------------------------------------------------
-- - data
-- -------------------------------------------------------------------------


-- users and related data
-- --------------------------

INSERT INTO  `users` (`id`, `login_name`) VALUES
(NULL,  'martin');



-- workspaces
-- --------------------------

INSERT INTO `workspaces` (`id`, `name`, `is_building`) VALUES
(NULL, 'default', 0);

INSERT INTO `workspace_resources` (`id`, `workspace_id`, `name`, `type`, `parent_id`, `contents`) VALUES
(NULL, 1, '', 'FOLDER', NULL, 0),
(NULL, 1, 'test-project', 'FOLDER', 1, 0),
(NULL, 1, 'src', 'FOLDER', 2, 0),
(NULL, 1, 'Helper.java', 'FILE', 3, 0x7075626c696320636c6173732048656c706572207b0d0a0d0a20207075626c69632073746174696320766f69642068656c702829207b0d0a2020202053797374656d2e6f75742e7072696e746c6e282248656c7065722069732068656c70696e672122293b0d0a20207d0d0a0d0a7d),
(NULL, 1, 'Main.java', 'FILE', 3, 0x0d0a7075626c696320636c617373204d61696e207b0d0a20207075626c69632073746174696320766f6964206d61696e28537472696e675b5d206172677329207b0d0a2020202053797374656d2e6f75742e7072696e746c6e282248656c6c6f20576f726c642122293b0d0a2020202048656c7065722e68656c7028293b0d0a20207d0d0a7d0d0a),
(NULL, 1, 'MyMessage.java', 'FILE', 3, 0x2f2a2a0d0a202a20436f70797269676874202863292032303130204d617274696e204765697373650d0a202a0d0a202a20546869732066696c6520697320646973747269627574656420756e64657220746865207465726d73206f6620746865204d4954206c6963656e73652e0d0a202a2f0d0a0d0a0d0a696d706f7274206e616d652e6d617274696e6765697373652e7769636b65742e7574696c2e416a6178526571756573745574696c3b0d0a0d0a2f2a2a0d0a202a20546573742072756e6e61626c652074686174206765747320696e766f6b65642062792074686520776f726b62656e63682e0d0a202a2f0d0a7075626c696320636c617373204d794d65737361676520696d706c656d656e74732052756e6e61626c65207b0d0a0d0a092f2a20286e6f6e2d4a617661646f63290d0a09202a2040736565206a6176612e6c616e672e52756e6e61626c652372756e28290d0a09202a2f0d0a09404f766572726964650d0a097075626c696320766f69642072756e2829207b0d0a0909537472696e67206a617661736372697074203d2022616c65727428275468697320506c7567696e20776173206275696c7420696e2074686520776f726b73706163652127293b223b0d0a0909416a6178526571756573745574696c2e676574416a61785265717565737454617267657428292e617070656e644a617661536372697074286a617661736372697074293b0d0a097d0d0a090d0a7d0d0a),
(NULL, 1, 'plugin.json', 'FILE', 2, 0x7b0d0a0922657874656e73696f6e73223a207b0d0a0909227765626964652e636f6e746578745f6d656e752e7265736f75726365223a205b0d0a090909224d794d657373616765220d0a09095d0d0a097d0d0a7d0d0a),
(NULL, 1, 'index.html', 'FILE', 3, 0x3c3f786d6c2076657273696f6e3d22312e302220656e636f64696e673d225554462d3822203f3e0d0a3c68746d6c3e0d0a093c686561643e0d0a093c2f686561643e0d0a093c626f64793e0d0a09093c68313e48656c6c6f20776f726c64213c2f68313e0d0a093c2f626f64793e0d0a3c2f68746d6c3e0d0a),
(NULL, 1, 'verilog', 'FOLDER', 1, 0),
(NULL, 1, 'build.json', 'FILE', 9, 0),
(NULL, 1, 'test.v', 'FILE', 9, 0x6d6f64756c6520546573743b0d0a0d0a72656720612c623b0d0a0d0a696e697469616c20626567696e0d0a0961203d20303b0d0a09742837293b0d0a092333302061203d20313b0d0a09742833293b0d0a092334302061203d20303b0d0a09742835293b0d0a092332302061203d20313b0d0a09233530202466696e6973683b0d0a656e640d0a0d0a7461736b20743b0d0a09696e707574205b333a305d20726570656174436f756e743b0d0a09696e746567657220693b0d0a09626567696e0d0a0909666f722028693d303b20693c726570656174436f756e743b20693d692b312920626567696e0d0a090909233520623d313b0d0a090909233520623d303b0d0a0909656e640d0a09656e640d0a656e647461736b0d0a0d0a656e646d6f64756c650d0a);

INSERT INTO `webide`.`workspace_builders` (`id`, `workspace_id`, `plugin_bundle_id`, `staging_path`, `builder_name`, `builder_class`) VALUES
(NULL, '1', '1', NULL, 'name.martingeisse.webide.features.verilog', 'name.martingeisse.webide.features.verilog');

INSERT INTO `webide`.`workspace_build_triggers` (`id`, `workspace_id`, `workspace_builder_id`, `trigger_base_path`, `path_pattern`, `buildscript_path`) VALUES
(NULL, '1', '1', '/verilog', '.*\\.v', '/verilog/build.json');



-- available plugins
-- --------------------------

INSERT INTO `plugins` (`id`, `is_unpacked`) VALUES
(NULL, 0),
(NULL, 0);

INSERT INTO `plugin_bundles` (`id`, `plugin_id`, `descriptor`, `jarfile`) VALUES
(NULL, 1, '{"extension_points": [
	{"name": "webide.context_menu.resource", "on_change_cleared_section": 0},
	{"name": "webide.editor.family", "on_change_cleared_section": null},
	{"name": "webide.editor.codemirror.mode", "on_change_cleared_section": null},
	{"name": "webide.editor", "on_change_cleared_section": null},
	{"name": "webide.editor.association", "on_change_cleared_section": null}
], "extensions": {
	"webide.editor.family": [
		{"id": "codemirror", "class": "name.martingeisse.webide.editor.codemirror.CodeMirrorEditorFamily"}
	],
	"webide.editor.codemirror.mode": [
		{"id": "text/x-csrc", "anchor": "name.martingeisse.webide.editor.codemirror.modes.StandardCodeMirrorModes", "path": "clike.js"},
		{"id": "text/html", "anchor": "name.martingeisse.webide.editor.codemirror.modes.StandardCodeMirrorModes", "path": ["css.js", "javascript.js", "xml.js", "htmlmixed.js"]},
		{"id": "text/x-verilog", "anchor": "name.martingeisse.webide.editor.codemirror.modes.StandardCodeMirrorModes", "path": ["verilog.js"]}
	],
	"webide.editor": [
		{"id": "webide.editors.java", "family": "codemirror", "mode": "text/x-csrc"},
		{"id": "webide.editors.html", "family": "codemirror", "mode": "text/html"},
		{"id": "webide.editors.verilog", "family": "codemirror", "mode": "text/x-verilog"}
	],
	"webide.editor.association": [
		{"target_type": "filename_pattern", "target_spec": ".*\\.java", "editor": "webide.editors.java"},
		{"target_type": "filename_pattern", "target_spec": ".*\\.html", "editor": "webide.editors.html"},
		{"target_type": "filename_pattern", "target_spec": ".*\\.htm", "editor": "webide.editors.html"},
		{"target_type": "filename_pattern", "target_spec": ".*\\.v", "editor": "webide.editors.verilog"}
	]
}}', 0),
(NULL, 2, '{"extensions": {"webide.context_menu.resource": ["name.martingeisse.webide_plugin.MyRunnable1", "name.martingeisse.webide_plugin.MyRunnable2"]}}', 0x504b03040a0000080000b4899d41000000000000000000000000090004004d4554412d494e462ffeca0000504b03040a0000080800b3899d41953b36685f0000006b000000140000004d4554412d494e462f4d414e49464553542e4d46f34dcccb4c4b2d2ed10d4b2d2acecccfb35230d433e0e572cc4312712c484cce4855008a01252df48c78b99c8b52134b5253749d2a41eacdf40ce28d8d75930c8c754d8c4c740d0d7c8dcd8d0c14341c0b0a7252153cf392f53479b978b900504b03040a0000080000c2869d41000000000000000000000000050000006e616d652f504b03040a0000080000c2869d41000000000000000000000000120000006e616d652f6d617274696e6765697373652f504b03040a0000080000aa899d41000000000000000000000000200000006e616d652f6d617274696e6765697373652f7765626964655f706c7567696e2f504b03040a0000080800aa899d411e144e5bb9010000e8020000310000006e616d652f6d617274696e6765697373652f7765626964655f706c7567696e2f4d7952756e6e61626c65312e636c6173739552cb6ed340143d93a47171dd07a64d81f0486051870223d8a642aa2a5550199092b65b347146ce04676cc6631e9fc58ac7820fe0a310d749da4482052c7cafee9d73ce9d39d73f7f7dff01e029ee3ba830ec6b31917c228c553a962acf25ff20076a28df6449112bcd5f7eea155a8b41229f38a8316c8dc57bc113a163fe7a309691755067f017dd0b3843fd4069659f315483ce3943ed281d4a1755ac7970b0cab0192a2d5f15938134a733861fa69148ce8551653d6fd6ec48e50c8fc2ffb86a97869a42afc267688a441a1bec3d974992b666d8d6c961afbdd7e9bad846c3c10ec9ff455d456fa5e58555093f1c8b8f3df9ae90b93da3dac32eae336cc7d22e9d9c0a430d868741274c4dcc4526a2d1a58e2020ff03dd5dc34ddc72d06408fe95e3e136eed02a4496493d3c21ebfb9151194d6e06e16213472361fa254f47b25b6ec02dcff239d45f42f6ada15793696e3f2d4c248f5569fcd692a18f4b30da205fe95ba13fa84299d648f10a559c32a3bcf2e00bdccfd3638f627ddadcc03a456f06a06a9332c95f925f10ba4279ddbf1a7ec5b5fd6fb851c142648366023b141b288d2fc51a33c25cac0a1777d19adea13de5ddfb0d504b03040a0000080800b1899d411ec9cbd3c4010000f8020000310000006e616d652f6d617274696e6765697373652f7765626964655f706c7567696e2f4d7952756e6e61626c65322e636c6173739552db6ed340103d9ba47171dd0ba60d977271e0a10e0556ea6b1052550989ca809484be561b677136386bb35e73f92c9eb83cf0017c14629c8426123c8064cf6866ce39b33bb33f7e7efb0ee008f71cd4180eb5984a3e15c62a9d48551492bf97433592e7795a264af3e71f7ba5d66298ca23070d869d897827782a74c25f0e2732b60e9a0cfe32fb1bced07cacb4b24f18ea61e78ca171928da48b3a363c385867d88e94962fcae9509ac19ce147592cd2336154152f920d3b5605c3c3e83f8edaa5a6a6d4ebf0e98a2295c68607039209e813699105af4d360dec5806736e707adc6b1f74ba2e76d172b047edfed24dc56fa4e5a555293f9e880f3df9b694857d45b187abb8c6b09b48bb521908430986076127ca4cc2452ee2f1858e2020ff03ddddc00ddc74b0cf10fe2bc7c32ddca6d5883c977a744aabe8c746e5d4793f8c969b39190bd3af783a96dd6a236e552b16507f05d9b7866e4d4374fb596962f954558bd85919f0a30a8c3668cef4afd18baa91a7b592bd441127cfc8afddff0cf7d3acec916dce925bd824ebcd01146d9327f90bf23342d7c86ffa97a32fb872f815d76b588a6c514f608f6c0bd5e02bb1d69cb010abc3c51d04b333b467bcbbbf00504b010214030a0000080000b4899d41000000000000000000000000090004000000000000001000ed41000000004d4554412d494e462ffeca0000504b010214030a0000080800b3899d41953b36685f0000006b000000140000000000000000000000a4812b0000004d4554412d494e462f4d414e49464553542e4d46504b010214030a0000080000c2869d41000000000000000000000000050000000000000000001000ed41bc0000006e616d652f504b010214030a0000080000c2869d41000000000000000000000000120000000000000000001000ed41df0000006e616d652f6d617274696e6765697373652f504b010214030a0000080000aa899d41000000000000000000000000200000000000000000001000ed410f0100006e616d652f6d617274696e6765697373652f7765626964655f706c7567696e2f504b010214030a0000080800aa899d411e144e5bb9010000e8020000310000000000000000000000a4814d0100006e616d652f6d617274696e6765697373652f7765626964655f706c7567696e2f4d7952756e6e61626c65312e636c617373504b010214030a0000080800b1899d411ec9cbd3c4010000f8020000310000000000000000000000a481550300006e616d652f6d617274696e6765697373652f7765626964655f706c7567696e2f4d7952756e6e61626c65322e636c617373504b05060000000007000700fc010000680500000000);



-- activated plugins
-- --------------------------

INSERT INTO  `builtin_plugins` (`id`, `plugin_id`) VALUES
(NULL, '1');

INSERT INTO  `user_installed_plugins` (`id`, `user_id`, `plugin_id`) VALUES
(NULL, '1', '2');



-- plugin run-time information
-- --------------------------



-- -------------------------------------------------------------------------
-- - constraints
-- -------------------------------------------------------------------------

-- workspaces
ALTER TABLE `workspace_resources` ADD CONSTRAINT `workspace_resources_ibfk_1` FOREIGN KEY (`workspace_id`) REFERENCES `workspaces` (`id`) ON DELETE CASCADE;
ALTER TABLE `workspace_resources` ADD CONSTRAINT `workspace_resources_ibfk_2` FOREIGN KEY (`parent_id`) REFERENCES `workspace_resources` (`id`) ON DELETE CASCADE;
ALTER TABLE `markers` ADD CONSTRAINT `markers_ibfk_1` FOREIGN KEY (`workspace_id`) REFERENCES `workspaces` (`id`) ON DELETE CASCADE;
ALTER TABLE `markers` ADD CONSTRAINT `markers_ibfk_2` FOREIGN KEY (`workspace_resource_id`) REFERENCES `workspace_resources` (`id`) ON DELETE CASCADE;
ALTER TABLE `workspace_resource_deltas` ADD CONSTRAINT `workspace_resource_deltas_ibfk_1` FOREIGN KEY (`workspace_id`) REFERENCES `workspaces` (`id`) ON DELETE CASCADE;
ALTER TABLE `workspace_tasks` ADD CONSTRAINT `workspace_tasks_ibfk_1` FOREIGN KEY (`workspace_id`) REFERENCES `workspaces` (`id`) ON DELETE CASCADE;
ALTER TABLE `workspace_builders` ADD CONSTRAINT `workspace_builders_ibfk_1` FOREIGN KEY (`workspace_id`) REFERENCES `workspaces` (`id`) ON DELETE CASCADE;
ALTER TABLE `workspace_builders` ADD CONSTRAINT `workspace_builders_ibfk_2` FOREIGN KEY (`plugin_bundle_id`) REFERENCES `plugin_bundles` (`id`) ON DELETE CASCADE;
ALTER TABLE `workspace_build_triggers` ADD CONSTRAINT `workspace_build_triggers_ibfk_1` FOREIGN KEY (`workspace_id`) REFERENCES `workspaces` (`id`) ON DELETE CASCADE;
ALTER TABLE `workspace_build_triggers` ADD CONSTRAINT `workspace_build_triggers_ibfk_2` FOREIGN KEY (`workspace_builder_id`) REFERENCES `workspace_builders` (`id`) ON DELETE CASCADE;

-- available plugins
ALTER TABLE `plugin_bundles` ADD CONSTRAINT `plugin_bundles_ibfk_1` FOREIGN KEY (`plugin_id`) REFERENCES `plugins` (`id`) ON DELETE CASCADE;
ALTER TABLE `declared_extension_points` ADD CONSTRAINT `declared_extension_points_ibfk_1` FOREIGN KEY (`plugin_bundle_id`) REFERENCES `plugin_bundles` (`id`) ON DELETE CASCADE;
ALTER TABLE `declared_extensions` ADD CONSTRAINT `declared_extensions_ibfk_1` FOREIGN KEY (`plugin_bundle_id`) REFERENCES `plugin_bundles` (`id`) ON DELETE CASCADE;

-- activated plugins
ALTER TABLE `builtin_plugins` ADD CONSTRAINT `builtin_plugins_ibfk_1` FOREIGN KEY (`plugin_id`) REFERENCES `plugins` (`id`) ON DELETE CASCADE;
ALTER TABLE `user_installed_plugins` ADD CONSTRAINT `user_plugins_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE;
ALTER TABLE `user_installed_plugins` ADD CONSTRAINT `user_plugins_ibfk_2` FOREIGN KEY (`plugin_id`) REFERENCES `plugins` (`id`) ON DELETE CASCADE;
ALTER TABLE `workspace_staging_plugins` ADD CONSTRAINT `workspace_staging_plugins_ibfk_1` FOREIGN KEY (`workspace_id`) REFERENCES `workspaces` (`id`) ON DELETE CASCADE;
ALTER TABLE `workspace_staging_plugins` ADD CONSTRAINT `workspace_staging_plugins_ibfk_2` FOREIGN KEY (`plugin_id`) REFERENCES `plugins` (`id`) ON DELETE CASCADE;

-- plugin run-time information
ALTER TABLE `extension_bindings` ADD CONSTRAINT `extension_bindings_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE;
ALTER TABLE `extension_bindings` ADD CONSTRAINT `extension_bindings_ibfk_2` FOREIGN KEY (`declared_extension_point_id`) REFERENCES `declared_extension_points` (`id`) ON DELETE CASCADE;
ALTER TABLE `extension_bindings` ADD CONSTRAINT `extension_bindings_ibfk_3` FOREIGN KEY (`declared_extension_id`) REFERENCES `declared_extensions` (`id`) ON DELETE CASCADE;
ALTER TABLE `plugin_bundle_states` ADD CONSTRAINT `plugin_bundle_states_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE;
ALTER TABLE `plugin_bundle_states` ADD CONSTRAINT `plugin_bundle_states_ibfk_2` FOREIGN KEY (`plugin_bundle_id`) REFERENCES `plugin_bundles` (`id`) ON DELETE CASCADE;

