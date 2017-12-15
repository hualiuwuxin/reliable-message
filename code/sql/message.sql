/*
Navicat MySQL Data Transfer

Source Server         : 127.0.0.1
Source Server Version : 50720
Source Host           : 127.0.0.1:3306
Source Database       : message

Target Server Type    : MYSQL
Target Server Version : 50720
File Encoding         : 65001

Date: 2017-12-15 17:32:30
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for message
-- ----------------------------
DROP TABLE IF EXISTS `message`;
CREATE TABLE `message` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `dr` char(3) NOT NULL DEFAULT '0',
  `create_date` datetime NOT NULL,
  `update_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `destination` varchar(128) NOT NULL,
  `type` char(16) NOT NULL,
  `content` text NOT NULL,
  `status` char(16) NOT NULL,
  `send_time` int(11) NOT NULL,
  `confirm_time` int(11) NOT NULL,
  `confirm_url` varchar(128) NOT NULL,
  `uuid` char(32) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=218 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of message
-- ----------------------------
INSERT INTO `message` VALUES ('215', '0', '2017-12-15 17:05:38', '2017-12-15 17:06:01', 'user-service-insert', 'queue', '{\"name\":\"user59\",\"uuid\":\"3219ee8260d64b209806b37c6e677d06\"}', 'SEND_SUCCEED', '1', '1', '127.0.0.1:7701/queryOrder', '3219ee8260d64b209806b37c6e677d06');
INSERT INTO `message` VALUES ('216', '0', '2017-12-15 17:07:37', '2017-12-15 17:07:37', 'user-service-insert', 'queue', '{\"name\":\"user13\",\"uuid\":\"99f966f8d9e24720b552e960d1b44386\"}', 'SEND_SUCCEED', '1', '0', '127.0.0.1:7701/queryOrder', '99f966f8d9e24720b552e960d1b44386');
INSERT INTO `message` VALUES ('217', '0', '2017-12-15 17:07:39', '2017-12-15 17:07:39', 'user-service-insert', 'queue', '{\"name\":\"user4\",\"uuid\":\"74731d1de8b34d15998e3a0d90b74187\"}', 'SEND_SUCCEED', '1', '0', '127.0.0.1:7701/queryOrder', '74731d1de8b34d15998e3a0d90b74187');

-- ----------------------------
-- Table structure for message_confirm_record
-- ----------------------------
DROP TABLE IF EXISTS `message_confirm_record`;
CREATE TABLE `message_confirm_record` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `dr` char(3) NOT NULL DEFAULT '0',
  `create_date` datetime NOT NULL,
  `update_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `destination` varchar(128) NOT NULL,
  `status` char(16) NOT NULL,
  `confirm_time` int(11) NOT NULL,
  `confirm_url` varchar(128) NOT NULL,
  `uuid` char(32) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=102 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of message_confirm_record
-- ----------------------------
INSERT INTO `message_confirm_record` VALUES ('101', '0', '2017-12-15 17:06:00', '2017-12-15 17:06:00', 'user-service-insert', 'COMPLETE', '0', '127.0.0.1:7701/queryOrder', '3219ee8260d64b209806b37c6e677d06');

-- ----------------------------
-- Table structure for message_send_record
-- ----------------------------
DROP TABLE IF EXISTS `message_send_record`;
CREATE TABLE `message_send_record` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `dr` char(3) NOT NULL DEFAULT '0',
  `create_date` datetime NOT NULL,
  `update_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `status` char(16) NOT NULL,
  `send_time` int(11) NOT NULL,
  `uuid` char(32) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=13330 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of message_send_record
-- ----------------------------
INSERT INTO `message_send_record` VALUES ('13327', '0', '2017-12-15 17:06:00', '2017-12-15 17:06:00', 'SUCCEED', '0', '3219ee8260d64b209806b37c6e677d06');
INSERT INTO `message_send_record` VALUES ('13328', '0', '2017-12-15 17:07:37', '2017-12-15 17:07:37', 'SUCCEED', '0', '99f966f8d9e24720b552e960d1b44386');
INSERT INTO `message_send_record` VALUES ('13329', '0', '2017-12-15 17:07:39', '2017-12-15 17:07:39', 'SUCCEED', '0', '74731d1de8b34d15998e3a0d90b74187');
