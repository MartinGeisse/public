-- phpMyAdmin SQL Dump
-- version 3.2.4
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: Dec 12, 2012 at 09:44 PM
-- Server version: 5.1.44
-- PHP Version: 5.3.1

SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";

--
-- Database: `webide`
--

-- --------------------------------------------------------

--
-- Table structure for table `files`
--

CREATE TABLE IF NOT EXISTS `files` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET latin1 COLLATE latin1_general_cs NOT NULL,
  `contents` blob NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=3 ;

--
-- Dumping data for table `files`
--

INSERT INTO `files` (`id`, `name`, `contents`) VALUES
(1, 'Helper.java', 0x636c6173732048656c706572207b0d0a0d0a20207075626c69632073746174696320766f69642068656c702829207b0d0a2020202053797374656d2e6f75742e7072696e746c6e282248656c7065722069732068656c70696e672122293b0d0a20207d0d0a0d0a7d),
(2, 'Main.java', 0x636c617373204d61696e207b0d0a20207075626c69632073746174696320766f6964206d61696e28537472696e675b5d206172677329207b0d0a2020202053797374656d2e6f75742e7072696e746c6e282248656c6c6f20576f726c642122293b0d0a2020202048656c7065722e68656c7028293b0d0a20207d0d0a7d);

-- --------------------------------------------------------

--
-- Table structure for table `markers`
--

CREATE TABLE IF NOT EXISTS `markers` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `origin` varchar(255) CHARACTER SET latin1 COLLATE latin1_general_cs NOT NULL,
  `meaning` varchar(255) CHARACTER SET latin1 COLLATE latin1_general_cs NOT NULL,
  `file_id` bigint(20) NOT NULL,
  `line` bigint(20) NOT NULL COMMENT '1-based',
  `column` bigint(20) NOT NULL COMMENT '1-based',
  `message` varchar(4096) CHARACTER SET latin1 COLLATE latin1_general_cs NOT NULL,
  PRIMARY KEY (`id`),
  KEY `markers_file_id` (`file_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

--
-- Dumping data for table `markers`
--


--
-- Constraints for dumped tables
--

--
-- Constraints for table `markers`
--
ALTER TABLE `markers`
  ADD CONSTRAINT `markers_ibfk_1` FOREIGN KEY (`file_id`) REFERENCES `files` (`id`);
