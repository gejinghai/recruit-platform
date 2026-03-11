-- 创建数据库
CREATE DATABASE IF NOT EXISTS recruit_db DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE recruit_db;

-- 招聘信息表
CREATE TABLE IF NOT EXISTS `job_info` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `title` varchar(100) DEFAULT NULL COMMENT '招聘标题',
  `company_name` varchar(100) DEFAULT NULL COMMENT '企业名称',
  `salary` varchar(50) DEFAULT NULL COMMENT '薪资',
  `work_type` varchar(50) DEFAULT NULL COMMENT '班次',
  `work_time` varchar(50) DEFAULT NULL COMMENT '工作时间',
  `welfare` text COMMENT '福利待遇(JSON)',
  `description` text COMMENT '企业介绍',
  `requirements` text COMMENT '招聘要求',
  `collection_time` varchar(50) DEFAULT NULL COMMENT '集合时间',
  `address` varchar(200) DEFAULT NULL COMMENT '面试地址',
  `status` tinyint DEFAULT 1 COMMENT '状态 0-下架 1-上架',
  `view_count` int DEFAULT 0 COMMENT '浏览量',
  `is_top` tinyint DEFAULT 0 COMMENT '是否置顶',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_status` (`status`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='招聘信息表';

-- 管理员表
CREATE TABLE IF NOT EXISTS `admin` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `username` varchar(50) NOT NULL COMMENT '用户名',
  `password` varchar(100) NOT NULL COMMENT '密码',
  `role` varchar(20) DEFAULT 'editor' COMMENT '角色',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='管理员表';

-- 广告位表
CREATE TABLE IF NOT EXISTS `ad_position` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `position_key` varchar(50) NOT NULL COMMENT '位置标识',
  `position_name` varchar(100) NOT NULL COMMENT '位置名称',
  `width` int DEFAULT 750 COMMENT '建议宽度',
  `height` int DEFAULT 300 COMMENT '建议高度',
  `status` tinyint DEFAULT 1 COMMENT '状态',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_position_key` (`position_key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='广告位表';

-- 广告内容表
CREATE TABLE IF NOT EXISTS `ad_content` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `position_id` bigint NOT NULL COMMENT '广告位ID',
  `title` varchar(100) DEFAULT NULL COMMENT '广告标题',
  `image_url` varchar(200) NOT NULL COMMENT '图片地址',
  `link_url` varchar(200) DEFAULT NULL COMMENT '跳转链接',
  `sort_order` int DEFAULT 0 COMMENT '排序',
  `status` tinyint DEFAULT 1 COMMENT '状态',
  `start_time` datetime DEFAULT NULL COMMENT '开始时间',
  `end_time` datetime DEFAULT NULL COMMENT '结束时间',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_position_id` (`position_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='广告内容表';

-- 初始化广告位
INSERT INTO `ad_position` (`position_key`, `position_name`, `width`, `height`) VALUES
('home_banner', '首页轮播', 750, 300),
('home_middle', '首页中部', 750, 200),
('job_list_ad', '列表信息流', 750, 150),
('job_detail_bottom', '详情底部', 750, 200);

-- 初始化管理员 (密码: admin123，使用BCrypt加密)
INSERT INTO `admin` (`username`, `password`, `role`) VALUES
('admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EH', 'admin');
