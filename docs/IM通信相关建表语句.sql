create database if not exists IM;

use IM;

DROP TABLE IF EXISTS `offline_message`;
CREATE TABLE `offline_message`(
    `id` bigint not null auto_increment primary key,
    `user_id` bigint not null comment '消息发送对象uid',
    `type` int not null comment '消息类型',
    `data` json null comment '消息内容',
    `delete_status` INT NOT NULL DEFAULT 0 COMMENT '逻辑删除 0 正常 / 1 删除',
    `create_time` DATETIME NOT NULL DEFAULT Now() COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT Now() on update NOW() COMMENT '修改时间'
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '离线消息表' ROW_FORMAT = Dynamic;
ALTER TABLE
    `offline_message` ADD INDEX `offline_message_user_id_index`(`user_id`);
ALTER TABLE
    `offline_message` ADD INDEX `offline_message_create_time_index`(`create_time`);
ALTER TABLE
    `offline_message` ADD INDEX `offline_message_update_time_index`(`update_time`);

DROP TABLE IF EXISTS `user_apply`;
CREATE TABLE `user_apply`(
    `id` BIGINT  NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `user_id` BIGINT NOT NULL COMMENT '申请人uid',
    `type` INT NOT NULL COMMENT '申请类型 1 加好友',
    `target_id` BIGINT NOT NULL COMMENT '接收人uid',
    `msg` VARCHAR(64) NOT NULL COMMENT '申请信息',
    `status` INT NOT NULL DEFAULT 0 COMMENT '申请状态 0 待审批 / 1 同意 / 2 拒绝',
    `read_status` INT NOT NULL DEFAULT 0 COMMENT '阅读状态 0 未读 / 1 已读',
    `create_time` DATETIME NOT NULL DEFAULT Now() COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT Now() on update NOW() COMMENT '修改时间'
)  ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '用户申请表' ROW_FORMAT = Dynamic;
ALTER TABLE
    `user_apply` ADD INDEX `user_apply_target_id_read_status_index`(`target_id`, `read_status`);
ALTER TABLE
    `user_apply` ADD INDEX `user_apply_user_id_target_id_index`(`user_id`, `target_id`);
ALTER TABLE
    `user_apply` ADD INDEX `user_apply_create_time_index`(`create_time`);
ALTER TABLE
    `user_apply` ADD INDEX `user_apply_update_time_index`(`update_time`);

DROP TABLE IF EXISTS `user_friend`;
CREATE TABLE `user_friend`(
    `id` BIGINT  NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `user_id` BIGINT NOT NULL COMMENT '用户id',
    `friend_id` BIGINT NOT NULL COMMENT '好友id',
    `delete_status` INT NOT NULL DEFAULT 0 COMMENT '逻辑删除 0 正常 / 1 删除',
    `create_time` DATETIME NOT NULL DEFAULT Now() COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT Now() on update NOW() COMMENT '更新时间'
)  ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '用户好友表' ROW_FORMAT = Dynamic;
ALTER TABLE
    `user_friend` ADD INDEX `user_friend_user_id_friend_id_index`(`user_id`, `friend_id`);
ALTER TABLE
    `user_friend` ADD INDEX `user_friend_create_time_index`(`create_time`);
ALTER TABLE
    `user_friend` ADD INDEX `user_friend_update_time_index`(`update_time`);

DROP TABLE IF EXISTS `contact`;
CREATE TABLE `contact`(
    `id` BIGINT  NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `user_id` BIGINT NOT NULL COMMENT '用户id',
    `room_id` BIGINT NOT NULL COMMENT '房间id',
    `read_time` DATETIME NOT NULL DEFAULT Now() COMMENT '阅读到的时间',
    `active_time` DATETIME NULL COMMENT '消息最后更新的时间',
    `last_msg_id` BIGINT NULL COMMENT '会话最新消息id',
    `create_time` DATETIME NOT NULL DEFAULT Now() COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT Now() on update NOW() COMMENT '更新时间'
)  ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '会话表' ROW_FORMAT = Dynamic;
ALTER TABLE
    `contact` ADD INDEX `contact_room_id_read_time_index`(`room_id`, `read_time`);
ALTER TABLE
    `contact` ADD INDEX `contact_user_id_active_time_index`(`user_id`, `active_time`);
ALTER TABLE
    `contact` ADD INDEX `contact_create_time_index`(`create_time`);
ALTER TABLE
    `contact` ADD INDEX `contact_update_time_index`(`update_time`);
ALTER TABLE contact
    ADD UNIQUE KEY unique_user_room (user_id, room_id);

DROP TABLE IF EXISTS `role`;
CREATE TABLE `role`(
    `id` BIGINT  NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `name` VARCHAR(64) NOT NULL COMMENT '角色名称',
    `create_time` DATETIME NOT NULL DEFAULT Now() COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT Now() on update NOW() COMMENT '更新时间'
)  ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '角色表' ROW_FORMAT = Dynamic;
ALTER TABLE
    `role` ADD INDEX `role_create_time_index`(`create_time`);
ALTER TABLE
    `role` ADD INDEX `role_update_time_index`(`update_time`);

DROP TABLE IF EXISTS `room_friend`;
CREATE TABLE `room_friend`(
    `id` BIGINT  NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT 'id',
    `room_id` BIGINT NOT NULL COMMENT '房间id',
    `user_id1` BIGINT NOT NULL COMMENT 'uid1（更小的uid）',
    `user_id2` BIGINT NOT NULL COMMENT 'uid2（更大的uid）',
    `room_key` VARCHAR(64) NOT NULL COMMENT '房间key由两个uid拼接，先做排序uid1_uid2',
    `status` INT NOT NULL  COMMENT '房间状态 0正常 1禁用(删好友了禁用)',
    `create_time` DATETIME NOT NULL DEFAULT Now() COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT Now() on update NOW() COMMENT '修改时间'
)  ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '单聊表' ROW_FORMAT = Dynamic;
ALTER TABLE
    `room_friend` ADD INDEX `room_friend_room_id_index`(`room_id`);
ALTER TABLE
    `room_friend` ADD UNIQUE `room_friend_room_key_unique`(`room_key`);
ALTER TABLE
    `room_friend` ADD INDEX `room_friend_create_time_index`(`create_time`);
ALTER TABLE
    `room_friend` ADD INDEX `room_friend_update_time_index`(`update_time`);

DROP TABLE IF EXISTS `group_member`;
CREATE TABLE `group_member`(
    `id` BIGINT  NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT 'id',
    `group_id` BIGINT NOT NULL COMMENT '群聊id',
    `user_id` BIGINT NOT NULL COMMENT '成员uid',
    `role` INT NOT NULL default 3 COMMENT '成员角色 1群主 2管理员 3普通成员',
    `create_time` DATETIME NOT NULL DEFAULT Now() COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT Now() on update NOW() COMMENT '修改时间'
)  ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '群聊成员表' ROW_FORMAT = Dynamic;
ALTER TABLE
    `group_member` ADD INDEX `group_member_group_id_role_index`(`group_id`, `role`);
ALTER TABLE
    `group_member` ADD INDEX `group_member_create_time_index`(`create_time`);
ALTER TABLE
    `group_member` ADD INDEX `group_member_update_time_index`(`update_time`);

DROP TABLE IF EXISTS `room`;
CREATE TABLE `room`(
    `id` BIGINT  NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT 'id',
    `type` INT NOT NULL  COMMENT '会话类型 1 群聊 2 单聊',
    `active_time` DATETIME NOT NULL DEFAULT Now() COMMENT '最后活跃时间-排序',
    `last_msg_id` bigint null comment '最后一条消息id',
    `delete_status` int default 0 not null comment '逻辑删除 0 正常 / 1 删除',
    `create_time` DATETIME NOT NULL DEFAULT Now() COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT Now() on update NOW() COMMENT '修改时间'
)  ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '房间表' ROW_FORMAT = Dynamic;
ALTER TABLE
    `room` ADD INDEX `room_active_time_index`(`active_time`);
ALTER TABLE
    `room` ADD INDEX `room_create_time_index`(`create_time`);
ALTER TABLE
    `room` ADD INDEX `room_update_time_index`(`update_time`);

DROP TABLE IF EXISTS `user_role`;
CREATE TABLE `user_role`(
    `id` BIGINT  NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT 'id',
    `user_id` BIGINT NOT NULL COMMENT 'user id',
    `role_id` BIGINT NOT NULL COMMENT '角色 id',
    `create_time` DATETIME NOT NULL DEFAULT Now() COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT Now() on update NOW() COMMENT '更新时间'
)  ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '用户权限表' ROW_FORMAT = Dynamic;
ALTER TABLE
    `user_role` ADD INDEX `user_role_user_id_index`(`user_id`);
ALTER TABLE
    `user_role` ADD INDEX `user_role_role_id_index`(`role_id`);
ALTER TABLE
    `user_role` ADD INDEX `user_role_create_time_index`(`create_time`);
ALTER TABLE
    `user_role` ADD INDEX `user_role_update_time_index`(`update_time`);

DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`(
    `id` BIGINT  NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT '用户id',
    `name` VARCHAR(20) NULL COMMENT '用户昵称',
    `password` varchar(255) not null comment '用户密码',
    `avatar` VARCHAR(255) NULL COMMENT '用户头像',
    `sex` INT NULL  COMMENT '性别 1为男性，2为女性',
    `last_opt_time` DATETIME NOT NULL DEFAULT Now() COMMENT '最后上下线时间',
    `ip_info` JSON NULL COMMENT 'ip信息',
    `status` INT NULL DEFAULT '0'  COMMENT '在线状态 0 在线 / 1 下线',
    `create_time` DATETIME NOT NULL DEFAULT Now() COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT Now() on update NOW() COMMENT '修改时间'
)  ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '用户表' ROW_FORMAT = Dynamic;
ALTER TABLE
    `user` ADD UNIQUE `user_name_unique`(`name`);
ALTER TABLE
    `user` ADD INDEX `user_create_time_index`(`create_time`);
ALTER TABLE
    `user` ADD INDEX `user_update_time_index`(`update_time`);

DROP TABLE IF EXISTS `room_group`;
CREATE TABLE `room_group`(
    `id` BIGINT  NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT 'id',
    `room_id` BIGINT NOT NULL COMMENT '房间id',
    `name` VARCHAR(16) NOT NULL COMMENT '群名称',
    `avatar` VARCHAR(256) NULL COMMENT '群头像',
    `delete_status` INT NOT NULL DEFAULT '0'  COMMENT '逻辑删除(0-正常,1-删除)',
    `create_time` DATETIME NOT NULL DEFAULT Now() COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT Now() on update NOW() COMMENT '修改时间'
)  ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '群聊表' ROW_FORMAT = Dynamic;
ALTER TABLE
    `room_group` ADD INDEX `room_group_room_id_index`(`room_id`);
ALTER TABLE
    `room_group` ADD INDEX `room_group_create_time_index`(`create_time`);
ALTER TABLE
    `room_group` ADD INDEX `room_group_update_time_index`(`update_time`);

DROP TABLE IF EXISTS `message`;
CREATE TABLE `message`(
    `id` BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT 'id',
    `room_id` BIGINT NOT NULL COMMENT '会话表id',
    `from_uid` BIGINT NOT NULL COMMENT '消息发送者uid',
    `status` INT NOT NULL  COMMENT '消息状态 0正常 1删除',
    `type` INT NULL DEFAULT 1 COMMENT '消息类型',
    `extra` JSON NULL COMMENT '扩展信息',
    `create_time` DATETIME NOT NULL DEFAULT Now() COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT Now() on update NOW() COMMENT '修改时间'
)  ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '消息表' ROW_FORMAT = Dynamic;
ALTER TABLE
    `message` ADD INDEX `message_room_id_index`(`room_id`);
ALTER TABLE
    `message` ADD INDEX `message_from_uid_index`(`from_uid`);
ALTER TABLE
    `message` ADD INDEX `message_create_time_index`(`create_time`);
ALTER TABLE
    `message` ADD INDEX `message_update_time_index`(`update_time`);
ALTER TABLE
    `room_group` ADD CONSTRAINT `room_group_room_id_foreign` FOREIGN KEY(`room_id`) REFERENCES `room`(`id`);
ALTER TABLE
    `contact` ADD CONSTRAINT `contact_room_id_foreign` FOREIGN KEY(`room_id`) REFERENCES `room`(`id`);
ALTER TABLE
    `user_apply` ADD CONSTRAINT `user_apply_user_id_foreign` FOREIGN KEY(`user_id`) REFERENCES `user`(`id`);
ALTER TABLE
    `message` ADD CONSTRAINT `message_room_id_foreign` FOREIGN KEY(`room_id`) REFERENCES `room`(`id`);
ALTER TABLE
    `group_member` ADD CONSTRAINT `group_member_user_id_foreign` FOREIGN KEY(`user_id`) REFERENCES `user`(`id`);
ALTER TABLE
    `user_friend` ADD CONSTRAINT `user_friend_friend_id_foreign` FOREIGN KEY(`friend_id`) REFERENCES `user`(`id`);
ALTER TABLE
    `message` ADD CONSTRAINT `message_from_uid_foreign` FOREIGN KEY(`from_uid`) REFERENCES `user`(`id`);
ALTER TABLE
    `user_friend` ADD CONSTRAINT `user_friend_user_id_foreign` FOREIGN KEY(`user_id`) REFERENCES `user`(`id`);
ALTER TABLE
    `user_role` ADD CONSTRAINT `user_role_user_id_foreign` FOREIGN KEY(`user_id`) REFERENCES `user`(`id`);
ALTER TABLE
    `user_role` ADD CONSTRAINT `user_role_role_id_foreign` FOREIGN KEY(`role_id`) REFERENCES `role`(`id`);
ALTER TABLE
    `room_friend` ADD CONSTRAINT `room_friend_room_id_foreign` FOREIGN KEY(`room_id`) REFERENCES `room`(`id`);
ALTER TABLE
    `user_apply` ADD CONSTRAINT `user_apply_target_id_foreign` FOREIGN KEY(`target_id`) REFERENCES `user`(`id`);
ALTER TABLE
    `contact` ADD CONSTRAINT `contact_user_id_foreign` FOREIGN KEY(`user_id`) REFERENCES `user`(`id`);
ALTER TABLE
    `group_member` ADD CONSTRAINT `group_member_group_id_foreign` FOREIGN KEY(`group_id`) REFERENCES `room_group`(`id`);
ALTER TABLE
    `offline_message` ADD CONSTRAINT `offline_message_user_id_foreign` FOREIGN KEY(`user_id`) REFERENCES `user`(`id`);
# 系统用户，用于发送系统消息
insert into user (id, name, password) values (-1, 'system', '114514');