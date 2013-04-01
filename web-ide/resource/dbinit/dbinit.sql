
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

CREATE TABLE IF NOT EXISTS `markers` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `workspace_id` bigint(20) NOT NULL,
  `path` varchar(4096) NOT NULL,
  `origin` varchar(255)  NOT NULL,
  `meaning` varchar(255)  NOT NULL,
  `line` bigint(20) NOT NULL COMMENT '1-based',
  `column` bigint(20) NOT NULL COMMENT '1-based',
  `message` varchar(4096)  NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `workspace_id` (`workspace_id`)
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
  `descriptor` MEDIUMTEXT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `workspace_id_and_base_path` (`workspace_id`, `trigger_base_path`),
  INDEX `workspace_builder_id` (`workspace_builder_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;




-- available plugins
-- --------------------------

CREATE TABLE IF NOT EXISTS `plugin_versions` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `plugin_public_id` varchar(255) NOT NULL,
  `staging_workspace_id` bigint(20) NULL,
  `is_unpacked` tinyint(1) NOT NULL,
  `is_active` tinyint(1) NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `plugin_versions_main` (`plugin_public_id`, `staging_workspace_id`, `is_active`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;

CREATE TABLE IF NOT EXISTS `plugin_bundles` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `plugin_version_id` bigint(20) NOT NULL,
  `descriptor` MEDIUMTEXT NOT NULL,
  `jarfile` LONGBLOB NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `plugin_bundles_plugin_version_id` (`plugin_version_id`)
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
  `plugin_version_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `builtin_plugins_plugin_version_id` (`plugin_version_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;

CREATE TABLE IF NOT EXISTS `user_installed_plugins` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NOT NULL,
  `plugin_public_id` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `main` (`user_id`, `plugin_public_id`)
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

INSERT INTO `webide`.`workspace_builders` (`id`, `workspace_id`, `plugin_bundle_id`, `staging_path`, `builder_name`, `builder_class`) VALUES
(NULL, '1', '1', NULL, 'name.martingeisse.webide.features.java', 'name.martingeisse.webide.features.java.compiler.JavaBuilder'),
(NULL, '1', '1', NULL, 'name.martingeisse.webide.features.pde', 'name.martingeisse.webide.features.pde.PluginBuilder'),
(NULL, '1', '1', NULL, 'name.martingeisse.webide.features.verilog', 'name.martingeisse.webide.features.verilog.compiler.VerilogBuilder');

INSERT INTO `webide`.`workspace_build_triggers` (`id`, `workspace_id`, `workspace_builder_id`, `trigger_base_path`, `path_pattern`, `buildscript_path`, `descriptor`) VALUES
(NULL, '1', '1', '/myplugin/src', '.*', '/myplugin/build.json', '{"sourcePath": "/myplugin/src", "binaryPath": "/myplugin/bin"}'),
(NULL, '1', '2', '/myplugin', '.*', '/myplugin/build.json', '{"descriptorFilePath": "/myplugin/plugin.json", "binPath": "/myplugin/bin", "bundleFilePath": "/myplugin/plugin.jar"}'),
(NULL, '1', '3', '/verilog/src', '.*', '/verilog/build.json', '{"sourcePath": "/verilog/src", "binaryPath": "/verilog/bin"}');



-- available plugins
-- --------------------------

INSERT INTO `plugin_versions` (`id`, `plugin_public_id`, `is_unpacked`, `is_active`) VALUES
(NULL, 'builtin', 0, 1);

INSERT INTO `plugin_bundles` (`id`, `plugin_version_id`, `descriptor`, `jarfile`) VALUES
(NULL, 1, '{"extension_points": [
	{"name": "webide.context_menu.resource", "on_change_cleared_section": 0},
	{"name": "webide.editor.family", "on_change_cleared_section": null},
	{"name": "webide.editor.codemirror.mode", "on_change_cleared_section": null},
	{"name": "webide.editor", "on_change_cleared_section": null},
	{"name": "webide.editor.association", "on_change_cleared_section": null}
], "extensions": {
	"webide.editor.family": [
		{"id": "simple", "class": "name.martingeisse.webide.editor.SimpleEditorFamily"}
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
		{"id": "webide.editors.verilog", "family": "codemirror", "mode": "text/x-verilog"},
		{"id": "webide.editors.vcd", "family": "simple", "class": "name.martingeisse.webide.features.verilog.wave.WaveEditor"}
	],
	"webide.editor.association": [
		{"target_type": "filename_pattern", "target_spec": ".*\\.java", "editor": "webide.editors.java"},
		{"target_type": "filename_pattern", "target_spec": ".*\\.html", "editor": "webide.editors.html"},
		{"target_type": "filename_pattern", "target_spec": ".*\\.htm", "editor": "webide.editors.html"},
		{"target_type": "filename_pattern", "target_spec": ".*\\.v", "editor": "webide.editors.verilog"},
		{"target_type": "filename_pattern", "target_spec": ".*\\.vcd", "editor": "webide.editors.vcd"}
	],
	"webide.context_menu.resource": [
		{"class": "name.martingeisse.webide.features.verilog.simulator.VerilogSimulatorMenuDelegate", "name": "Simulate"}
	]
}}', 0);



-- activated plugins
-- --------------------------

INSERT INTO  `builtin_plugins` (`id`, `plugin_version_id`) VALUES
(NULL, '1');

-- INSERT INTO  `user_installed_plugins` (`id`, `user_id`, `plugin_id`) VALUES
-- (NULL, '1', '2');



-- plugin run-time information
-- --------------------------



-- -------------------------------------------------------------------------
-- - constraints
-- -------------------------------------------------------------------------

-- workspaces
ALTER TABLE `markers` ADD CONSTRAINT `markers_ibfk_1` FOREIGN KEY (`workspace_id`) REFERENCES `workspaces` (`id`) ON DELETE CASCADE;
ALTER TABLE `workspace_resource_deltas` ADD CONSTRAINT `workspace_resource_deltas_ibfk_1` FOREIGN KEY (`workspace_id`) REFERENCES `workspaces` (`id`) ON DELETE CASCADE;
ALTER TABLE `workspace_tasks` ADD CONSTRAINT `workspace_tasks_ibfk_1` FOREIGN KEY (`workspace_id`) REFERENCES `workspaces` (`id`) ON DELETE CASCADE;
ALTER TABLE `workspace_builders` ADD CONSTRAINT `workspace_builders_ibfk_1` FOREIGN KEY (`workspace_id`) REFERENCES `workspaces` (`id`) ON DELETE CASCADE;
ALTER TABLE `workspace_builders` ADD CONSTRAINT `workspace_builders_ibfk_2` FOREIGN KEY (`plugin_bundle_id`) REFERENCES `plugin_bundles` (`id`) ON DELETE CASCADE;
ALTER TABLE `workspace_build_triggers` ADD CONSTRAINT `workspace_build_triggers_ibfk_1` FOREIGN KEY (`workspace_id`) REFERENCES `workspaces` (`id`) ON DELETE CASCADE;
ALTER TABLE `workspace_build_triggers` ADD CONSTRAINT `workspace_build_triggers_ibfk_2` FOREIGN KEY (`workspace_builder_id`) REFERENCES `workspace_builders` (`id`) ON DELETE CASCADE;

-- available plugins
ALTER TABLE `plugin_versions` ADD CONSTRAINT `plugin_versions_ibfk_1` FOREIGN KEY (`staging_workspace_id`) REFERENCES `workspaces` (`id`) ON DELETE CASCADE;
ALTER TABLE `plugin_bundles` ADD CONSTRAINT `plugin_bundles_ibfk_1` FOREIGN KEY (`plugin_version_id`) REFERENCES `plugin_versions` (`id`) ON DELETE CASCADE;
ALTER TABLE `declared_extension_points` ADD CONSTRAINT `declared_extension_points_ibfk_1` FOREIGN KEY (`plugin_bundle_id`) REFERENCES `plugin_bundles` (`id`) ON DELETE CASCADE;
ALTER TABLE `declared_extensions` ADD CONSTRAINT `declared_extensions_ibfk_1` FOREIGN KEY (`plugin_bundle_id`) REFERENCES `plugin_bundles` (`id`) ON DELETE CASCADE;

-- activated plugins
ALTER TABLE `builtin_plugins` ADD CONSTRAINT `builtin_plugins_ibfk_1` FOREIGN KEY (`plugin_version_id`) REFERENCES `plugin_versions` (`id`) ON DELETE CASCADE;
ALTER TABLE `user_installed_plugins` ADD CONSTRAINT `user_installed_plugins_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE;

-- plugin run-time information
ALTER TABLE `extension_bindings` ADD CONSTRAINT `extension_bindings_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE;
ALTER TABLE `extension_bindings` ADD CONSTRAINT `extension_bindings_ibfk_2` FOREIGN KEY (`declared_extension_point_id`) REFERENCES `declared_extension_points` (`id`) ON DELETE CASCADE;
ALTER TABLE `extension_bindings` ADD CONSTRAINT `extension_bindings_ibfk_3` FOREIGN KEY (`declared_extension_id`) REFERENCES `declared_extensions` (`id`) ON DELETE CASCADE;
ALTER TABLE `plugin_bundle_states` ADD CONSTRAINT `plugin_bundle_states_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE;
ALTER TABLE `plugin_bundle_states` ADD CONSTRAINT `plugin_bundle_states_ibfk_2` FOREIGN KEY (`plugin_bundle_id`) REFERENCES `plugin_bundles` (`id`) ON DELETE CASCADE;

