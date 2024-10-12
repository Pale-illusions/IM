create database if not exists IM;

use IM;

DROP TABLE IF EXISTS video;
CREATE TABLE video
(
    `id` bigint not null auto_increment primary key,
    `title` varchar(20) not null comment '视频标题',
    `description` varchar(255) null comment '视频简介',
    `url` varchar(255) null comment '视频地址',
    `user_id` bigint not null comment '视频作者id',
    `score` double not null default 0 comment '视频分数',
    `delete_status` INT NOT NULL DEFAULT 0 COMMENT '逻辑删除 0 正常 / 1 删除',
    `create_time` DATETIME NOT NULL DEFAULT Now() COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT Now() on update NOW() COMMENT '修改时间'
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '视频表' ROW_FORMAT = Dynamic;
ALTER TABLE
    video ADD INDEX `video_author_id_index`(`user_id`);
ALTER TABLE
    video ADD INDEX `video_create_time_index`(`create_time`);
ALTER TABLE
    video ADD INDEX `video_update_time_index`(`update_time`);

DROP TABLE IF EXISTS `comment`;
CREATE TABLE `comment`(
    `id` bigint not null auto_increment primary key,
    `type` int not null comment '评论类型 0 视频 / 1 评论',
    `video_id` bigint not null comment '视频id',
    `target_id` bigint not null comment '目标id',
    `user_id` bigint not null comment '评论者id',
    `content` varchar(255) not null comment '内容',
    `delete_status` INT NOT NULL DEFAULT 0 COMMENT '逻辑删除 0 正常 / 1 删除',
    `create_time` DATETIME NOT NULL DEFAULT Now() COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT Now() on update NOW() COMMENT '修改时间'
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '评论表' ROW_FORMAT = Dynamic;
ALTER TABLE
    `comment` ADD INDEX `comment_user_id_index`(`user_id`);
ALTER TABLE
    `comment` ADD INDEX `comment_create_time_index`(`create_time`);
ALTER TABLE
    `comment` ADD INDEX `comment_update_time_index`(`update_time`);

DROP TABLE IF EXISTS `follow`;
CREATE TABLE `follow`(
    `id` bigint not null auto_increment primary key,
    `follower_id` bigint not null comment '粉丝id',
    `followee_id` bigint not null  comment '目标id',
    `status` INT NOT NULL DEFAULT 0 COMMENT '关注状态 0 关注 / 1 未关注', # 这个 status 本质上是逻辑删除，查询是否关注时，不存在即为未关注，存在即为关注
    `create_time` DATETIME NOT NULL DEFAULT Now() COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT Now() on update NOW() COMMENT '修改时间'
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '关注表' ROW_FORMAT = Dynamic;
ALTER TABLE
    `follow` ADD INDEX `follow_follower_id_index` (`follower_id`);
ALTER TABLE
    `follow` ADD INDEX `follow_followee_id_index` (`followee_id`);
ALTER TABLE
    `follow` ADD INDEX `follow_follower_followee_index` (`follower_id`, `followee_id`); # 关注列表
ALTER TABLE
    `follow` ADD INDEX `follow_followee_follower_index` (`followee_id`, `follower_id`); # 粉丝列表
ALTER TABLE
    `follow` ADD INDEX `follow_create_time_index`(`create_time`);
ALTER TABLE
    `follow` ADD INDEX `follow_update_time_index`(`update_time`);
ALTER TABLE follow
    ADD UNIQUE KEY unique_followee_follower (followee_id, follower_id);
ALTER TABLE follow
    ADD UNIQUE KEY unique_follower_followee (follower_id, followee_id);

Drop TABLE IF EXISTS `tag`;
CREATE TABLE `tag` (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    tag_name VARCHAR(255) UNIQUE NOT NULL comment '标签名称'
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '标签表' ROW_FORMAT = Dynamic;

DROP TABLE IF EXISTS `video_tag`;
CREATE TABLE `video_tag` (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    video_id BIGINT NOT NULL comment '视频id',
    tag_id BIGINT NOT NULL comment '标签id'
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '视频标签表' ROW_FORMAT = Dynamic;


ALTER TABLE
    `video` ADD CONSTRAINT `video_user_id_foreign` FOREIGN KEY(`user_id`) REFERENCES `user`(`id`);
ALTER TABLE
    `comment` ADD CONSTRAINT `comment_video_id_foreign` FOREIGN KEY(`video_id`) REFERENCES `video`(`id`);
ALTER TABLE
    `comment` ADD CONSTRAINT `comment_user_id_foreign` FOREIGN KEY(`user_id`) REFERENCES `user`(`id`);
ALTER TABLE
    `follow` ADD CONSTRAINT `follow_follower_id_foreign` FOREIGN KEY(`follower_id`) REFERENCES `user`(`id`);
ALTER TABLE
    `follow` ADD CONSTRAINT `follow_followee_id_foreign` FOREIGN KEY(`followee_id`) REFERENCES `user`(`id`);
ALTER TABLE
    `video_tag` ADD CONSTRAINT `video_tag_video_id_foreign` FOREIGN KEY (`video_id`) REFERENCES `video`(`id`);
ALTER TABLE
    `video_tag` ADD CONSTRAINT `video_tag_tag_id_foreign` FOREIGN KEY (`tag_id`) REFERENCES `tag`(`id`);
