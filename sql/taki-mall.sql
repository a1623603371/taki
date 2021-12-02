/*
 Navicat Premium Data Transfer

 Source Server         : 192.168.33.11
 Source Server Type    : MySQL
 Source Server Version : 50734
 Source Host           : 192.168.33.11:3306
 Source Schema         : taki-mall

 Target Server Type    : MySQL
 Target Server Version : 50734
 File Encoding         : 65001

 Date: 02/12/2021 11:10:17
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for auth_account
-- ----------------------------
DROP TABLE IF EXISTS `auth_account`;
CREATE TABLE `auth_account`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `username` varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户名，（英文或者拼音大小写）',
  `password` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '密码',
  `phone` varchar(11) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '手机',
  `email` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '邮箱',
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '员工姓名',
  `avatar` varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '头像图片地址',
  `status` tinyint(1) NOT NULL DEFAULT 0 COMMENT '状态（0:正常，1：禁止登陆）',
  `remake` varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '账号说明',
  `last_login_ip` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '最后的登陆ip',
  `last_login_time` datetime(0) NULL DEFAULT NULL COMMENT '最后登陆时间',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  `password_error_count` tinyint(1) NULL DEFAULT 0 COMMENT '密码错误次数',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of auth_account
-- ----------------------------

-- ----------------------------
-- Table structure for auth_account_role_relationship
-- ----------------------------
DROP TABLE IF EXISTS `auth_account_role_relationship`;
CREATE TABLE `auth_account_role_relationship`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `account_id` bigint(20) NOT NULL COMMENT '账号id',
  `role_id` bigint(20) NOT NULL COMMENT '角色id',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `update_time` datetime(0) NOT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of auth_account_role_relationship
-- ----------------------------

-- ----------------------------
-- Table structure for auth_menu
-- ----------------------------
DROP TABLE IF EXISTS `auth_menu`;
CREATE TABLE `auth_menu`  (
  `id` bigint(20) NOT NULL,
  `parent_id` bigint(20) NULL DEFAULT NULL COMMENT '上级id',
  `meun_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '菜单名称',
  `meun_code` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '菜单编码',
  `meun_inco` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '菜单图标',
  `path` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '菜单路由url',
  `url` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '功能url',
  `meun_type` tinyint(1) NULL DEFAULT NULL COMMENT '菜单类型 （菜单，按钮）',
  `sort_num` int(4) NOT NULL DEFAULT 100 COMMENT '排序',
  `authority` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '权限标识',
  `hide` tinyint(1) NULL DEFAULT NULL COMMENT '是否隐藏（0，否，1 是）',
  `deleted` tinyint(1) NULL DEFAULT NULL COMMENT '是否删除（0，否，1 是）',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `update_time` datetime(0) NOT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of auth_menu
-- ----------------------------

-- ----------------------------
-- Table structure for auth_role
-- ----------------------------
DROP TABLE IF EXISTS `auth_role`;
CREATE TABLE `auth_role`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `code` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '角色编号',
  `name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '角色名称',
  `remark` varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '角色备注',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `update_time` datetime(0) NOT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of auth_role
-- ----------------------------

-- ----------------------------
-- Table structure for auth_role_meun_relationship
-- ----------------------------
DROP TABLE IF EXISTS `auth_role_meun_relationship`;
CREATE TABLE `auth_role_meun_relationship`  (
  `id` bigint(20) NOT NULL,
  `role_id` bigint(20) NOT NULL COMMENT '角色id',
  `meun_id` bigint(20) NOT NULL COMMENT '菜单权限id',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `update_time` datetime(0) NOT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of auth_role_meun_relationship
-- ----------------------------

-- ----------------------------
-- Table structure for cart
-- ----------------------------
DROP TABLE IF EXISTS `cart`;
CREATE TABLE `cart`  (
  `id` bigint(20) NOT NULL,
  `user_account_id` bigint(20) NOT NULL COMMENT '用户账号',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `update_time` datetime(0) NOT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of cart
-- ----------------------------

-- ----------------------------
-- Table structure for cart_item
-- ----------------------------
DROP TABLE IF EXISTS `cart_item`;
CREATE TABLE `cart_item`  (
  `id` bigint(20) NOT NULL,
  `cart_id` bigint(20) NOT NULL COMMENT '购物车id',
  `goods_sku_id` bigint(20) NOT NULL COMMENT 'skuid',
  `purchase_quantity` bigint(20) NOT NULL COMMENT '购买数量',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `update_time` datetime(0) NOT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of cart_item
-- ----------------------------

-- ----------------------------
-- Table structure for comment_aggregate
-- ----------------------------
DROP TABLE IF EXISTS `comment_aggregate`;
CREATE TABLE `comment_aggregate`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `goods_id` bigint(20) NOT NULL COMMENT '商品id',
  `total_comment_count` bigint(20) NOT NULL COMMENT '品论总数',
  `good_comment__count` bigint(20) NOT NULL COMMENT '好评数量',
  `good_comment_rate` decimal(10, 2) NOT NULL COMMENT '好评比例',
  `show_pictures_comment_count` bigint(20) NOT NULL COMMENT '晒图评论数量',
  `medium_comment_count` bigint(20) NOT NULL COMMENT '中评数量',
  `bad_comment_count` bigint(20) NOT NULL COMMENT '差评数量',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `update_time` datetime(0) NOT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of comment_aggregate
-- ----------------------------

-- ----------------------------
-- Table structure for comment_info
-- ----------------------------
DROP TABLE IF EXISTS `comment_info`;
CREATE TABLE `comment_info`  (
  `id` bigint(20) NOT NULL,
  `user_account_id` bigint(20) NOT NULL COMMENT '用户账号id',
  `username` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户昵称',
  `order_info_id` bigint(20) NOT NULL COMMENT '订单信息ID',
  `order_item_id` bigint(20) NOT NULL COMMENT '订单条目ID',
  `goods_id` bigint(20) NOT NULL COMMENT '商品ID',
  `goods_sku_id` bigint(20) NOT NULL COMMENT 'SKUID',
  `goods_sku_sale_properties` varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '商品sku的销售属性',
  `total_score` tinyint(4) NOT NULL COMMENT '总评分，1~5',
  `goods_score` tinyint(4) NOT NULL COMMENT '商品评分，1~5',
  `customer_service_score` tinyint(4) NOT NULL COMMENT '客服评分，1~5',
  `logistics_score` tinyint(4) NOT NULL COMMENT '物流评分，1~5',
  `comment_content` varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '评论内容',
  `is_show_pictures` tinyint(4) NOT NULL COMMENT '是否晒图，1：晒图，0：未晒图',
  `is_default_comment` tinyint(4) NOT NULL COMMENT '是否系统自动给的默认评论，1：是默认评论，0：不是默认评论',
  `comment_status` tinyint(4) NOT NULL COMMENT '评论状态，1：待审核，2：已审核，3：审核不通过',
  `comment_type` tinyint(4) NOT NULL COMMENT '评论类型，1：好评，总分是4分以上；2：中评，3分；3：差评，1~2分',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `update_time` datetime(0) NOT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of comment_info
-- ----------------------------

-- ----------------------------
-- Table structure for comment_picture
-- ----------------------------
DROP TABLE IF EXISTS `comment_picture`;
CREATE TABLE `comment_picture`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `comment_info_id` bigint(20) NOT NULL COMMENT '评论ID',
  `comment_picture_path` varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '评论晒图的图片路径',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `update_time` datetime(0) NOT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of comment_picture
-- ----------------------------

-- ----------------------------
-- Table structure for commodity_brand
-- ----------------------------
DROP TABLE IF EXISTS `commodity_brand`;
CREATE TABLE `commodity_brand`  (
  `id` bigint(20) UNSIGNED ZEROFILL NOT NULL AUTO_INCREMENT,
  `chinese_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '中文名称',
  `english_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '英文名称',
  `allas_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '品牌别名',
  `logo_path` varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT ' log 图片路径',
  `auth_voucher_path` varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '品牌授权凭证的图片路径',
  `location` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '品牌所在地',
  `remark` varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '品牌备注',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `update_time` datetime(0) NOT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of commodity_brand
-- ----------------------------

-- ----------------------------
-- Table structure for commodity_category_perperty_relationship
-- ----------------------------
DROP TABLE IF EXISTS `commodity_category_perperty_relationship`;
CREATE TABLE `commodity_category_perperty_relationship`  (
  `id` bigint(20) NOT NULL,
  `category_id` bigint(20) NOT NULL COMMENT '类目id',
  `property_id` bigint(20) NOT NULL COMMENT '属性id',
  `is_required` tinyint(4) NOT NULL COMMENT '属性是否必填 1：是必填，0：非必填',
  `property_types` varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '属性类型 1 关键属性 2.销售属性 3.非关键属性 4：导购属性，这里可以有多个值拼接在一起，比如1,2，2,4',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `update_time` datetime(0) NOT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of commodity_category_perperty_relationship
-- ----------------------------

-- ----------------------------
-- Table structure for commodity_categroy
-- ----------------------------
DROP TABLE IF EXISTS `commodity_categroy`;
CREATE TABLE `commodity_categroy`  (
  `id` bigint(20) NOT NULL,
  `parent_id` bigint(20) NULL DEFAULT NULL COMMENT '上级id',
  `name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '类目名称',
  `description` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '类目描述',
  `is_leaf` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否是节点 1：是，0：否',
  `image_url` varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '类目图片',
  `is_show` tinyint(1) NOT NULL DEFAULT 1 COMMENT '是否显示1：是，0：否',
  `sort` int(4) UNSIGNED ZEROFILL NOT NULL DEFAULT 0100 COMMENT '排序',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `update_time` datetime(0) NOT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of commodity_categroy
-- ----------------------------

-- ----------------------------
-- Table structure for commodity_goods
-- ----------------------------
DROP TABLE IF EXISTS `commodity_goods`;
CREATE TABLE `commodity_goods`  (
  `id` bigint(20) NOT NULL,
  `category_id` bigint(20) NOT NULL COMMENT '类目id',
  `brand_id` bigint(20) UNSIGNED ZEROFILL NOT NULL COMMENT '品牌id',
  `code` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'SPU编码',
  `name` varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '商品名称',
  `sub_name` varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '商品副名称',
  `gross_weigth` decimal(10, 2) NOT NULL COMMENT '商品毛重 单位 g',
  `length` decimal(10, 2) NOT NULL COMMENT '商品长度 单位 cm',
  `width` decimal(10, 2) NOT NULL COMMENT '外包装宽，单位是cm',
  `height` decimal(10, 2) NOT NULL COMMENT '外包装高，单位是cm',
  `status` tinyint(4) NOT NULL COMMENT '商品状态，1：待审核，2：待上架，3：审核未通过，4：已上架',
  `service_guarantees` varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '服务保证',
  `package_list` varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '包装清单',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `update_time` datetime(0) NOT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of commodity_goods
-- ----------------------------

-- ----------------------------
-- Table structure for commodity_goods_detail
-- ----------------------------
DROP TABLE IF EXISTS `commodity_goods_detail`;
CREATE TABLE `commodity_goods_detail`  (
  `id` bigint(20) NOT NULL,
  `goods_id` bigint(20) NOT NULL COMMENT '商品id',
  `detail_content` longtext CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '商品详情',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `update_time` datetime(0) NOT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of commodity_goods_detail
-- ----------------------------

-- ----------------------------
-- Table structure for commodity_goods_detail_picture
-- ----------------------------
DROP TABLE IF EXISTS `commodity_goods_detail_picture`;
CREATE TABLE `commodity_goods_detail_picture`  (
  `id` bigint(20) NOT NULL,
  `goods_detail_id` bigint(20) NOT NULL COMMENT '商品详情id',
  `picture_path` varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '商品详情图片路径',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `update_time` datetime(0) NOT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of commodity_goods_detail_picture
-- ----------------------------

-- ----------------------------
-- Table structure for commodity_goods_picture
-- ----------------------------
DROP TABLE IF EXISTS `commodity_goods_picture`;
CREATE TABLE `commodity_goods_picture`  (
  `id` bigint(20) NOT NULL,
  `goods_id` bigint(20) NOT NULL COMMENT '商品id',
  `picture_path` varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '图片路径地址',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `update_time` datetime(0) NOT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of commodity_goods_picture
-- ----------------------------

-- ----------------------------
-- Table structure for commodity_goods_property_value
-- ----------------------------
DROP TABLE IF EXISTS `commodity_goods_property_value`;
CREATE TABLE `commodity_goods_property_value`  (
  `id` bigint(20) NOT NULL,
  `type` tinyint(1) NOT NULL COMMENT '属性值的类型，1：类目直接关联的属性的值；2：类目的属性组关联的属性的值',
  `goods_id` bigint(20) NOT NULL COMMENT '商品id',
  `relation_id` bigint(20) NOT NULL COMMENT '如果type是1，那么这里是类目与属性关联关系的ID；如果type是2，那么这里是类目的属性组与属性的关联关系的id',
  `property_value` varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '如果是可选的，那么直可能就是：黑色,白色，多个值串起来的；如果是输入的，那么就是直接输入的一个值',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `update_time` datetime(0) NOT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of commodity_goods_property_value
-- ----------------------------

-- ----------------------------
-- Table structure for commodity_goods_sku
-- ----------------------------
DROP TABLE IF EXISTS `commodity_goods_sku`;
CREATE TABLE `commodity_goods_sku`  (
  `id` bigint(20) NOT NULL,
  `goods_id` bigint(20) NOT NULL COMMENT '商品id',
  `sku_code` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'sku 编码',
  `purchase_price` decimal(18, 2) NOT NULL COMMENT '采购价格',
  `sale_price` decimal(18, 2) NOT NULL COMMENT '销售价格',
  `discount_price` decimal(18, 2) NOT NULL COMMENT '折扣价格',
  `sale_properties` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '销售属性',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `update_time` datetime(0) NOT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of commodity_goods_sku
-- ----------------------------

-- ----------------------------
-- Table structure for commodity_goods_sku_sale_property
-- ----------------------------
DROP TABLE IF EXISTS `commodity_goods_sku_sale_property`;
CREATE TABLE `commodity_goods_sku_sale_property`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `goods_sku_id` bigint(20) NOT NULL COMMENT '商品skuId',
  `relation_id` bigint(20) NOT NULL COMMENT '类目和属性关联关系Id',
  `property_value` varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'sku对应的销售属性的实际值',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `update_time` datetime(0) NOT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of commodity_goods_sku_sale_property
-- ----------------------------

-- ----------------------------
-- Table structure for commodity_property
-- ----------------------------
DROP TABLE IF EXISTS `commodity_property`;
CREATE TABLE `commodity_property`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `property_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '属性名称',
  `property_desc` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '属性描述',
  `input_type` tinyint(1) NOT NULL COMMENT '输入方式（1.多选，2.输入）',
  `intput_value` varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '输入方式是可选，那么需要提供一些供选择的值范围',
  `sort` tinyint(4) NOT NULL COMMENT '排序',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `update_time` datetime(0) NOT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of commodity_property
-- ----------------------------

-- ----------------------------
-- Table structure for commodity_property_group
-- ----------------------------
DROP TABLE IF EXISTS `commodity_property_group`;
CREATE TABLE `commodity_property_group`  (
  `id` bigint(20) NOT NULL,
  `name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '属性分组',
  `category_id` bigint(20) NOT NULL COMMENT '类目id',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `update_time` datetime(0) NOT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of commodity_property_group
-- ----------------------------

-- ----------------------------
-- Table structure for commodity_property_group_relationshop
-- ----------------------------
DROP TABLE IF EXISTS `commodity_property_group_relationshop`;
CREATE TABLE `commodity_property_group_relationshop`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `property_group_id` bigint(20) NOT NULL COMMENT '属性分组id',
  `property_id` bigint(20) NOT NULL COMMENT '属性id',
  `is_required` tinyint(4) NOT NULL COMMENT '是否必填 1：是必填，0：非必填',
  `property_types` varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '属性类型 1：关键属性，2：销售属性，3：非关键属性，4：导购属性，这里可以有多个值拼接在一起，比如1,2，2,4',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `update_Time` datetime(0) NOT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of commodity_property_group_relationshop
-- ----------------------------

-- ----------------------------
-- Table structure for costomer_return_goods
-- ----------------------------
DROP TABLE IF EXISTS `costomer_return_goods`;
CREATE TABLE `costomer_return_goods`  (
  `id` bigint(20) NOT NULL,
  `order_info_id` bigint(20) NOT NULL COMMENT '订单ID',
  `order_on` varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '订单编号',
  `status` tinyint(4) NOT NULL COMMENT '退货工单状态，1：待审核，2：审核不通过，3：待寄送退货商品，4：退货商品待收货，5：退货待入库，6：退货已入库，7：完成退款',
  `return_goods_reason` tinyint(4) NOT NULL COMMENT '退货原因，1：质量不好，2：商品不满意，3：买错了，4：无理由退货',
  `return_goods_remark` varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '退货备注',
  `return_goods_logistics_code` varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '退货快递单号',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `update_time` datetime(0) NOT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of costomer_return_goods
-- ----------------------------

-- ----------------------------
-- Table structure for finance_logistics_settlement_detail
-- ----------------------------
DROP TABLE IF EXISTS `finance_logistics_settlement_detail`;
CREATE TABLE `finance_logistics_settlement_detail`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `order_info_id` bigint(20) NOT NULL COMMENT '订单id',
  `order_on` varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '订单编号',
  `total_settlement_amount` decimal(8, 2) NOT NULL COMMENT '结算金额',
  `bank_account` varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '银行账号',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `update_time` datetime(0) NOT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of finance_logistics_settlement_detail
-- ----------------------------

-- ----------------------------
-- Table structure for finance_ourchase_settlement_order
-- ----------------------------
DROP TABLE IF EXISTS `finance_ourchase_settlement_order`;
CREATE TABLE `finance_ourchase_settlement_order`  (
  `id` bigint(20) NOT NULL,
  `purchase_order_id` bigint(20) NOT NULL COMMENT '采购单Id',
  `purchase_input_order_id` bigint(20) NOT NULL COMMENT '采购入库单Id',
  `supplier_id` bigint(20) NOT NULL COMMENT '供应商Id',
  `expect_arrival_time` datetime(0) NOT NULL COMMENT '预计到达时间',
  `actual_arrival_time` datetime(0) NOT NULL COMMENT '实际到货时间',
  `purchase_contactor` varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '采购联系人',
  `purchase_contactor_phone_number` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '采购联系人电话',
  `purchase_contactor_email` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '采购联系人邮箱',
  `purchase_order_remark` varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '采购单备注说明',
  `purchaser` varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '采购员',
  `status` tinyint(4) NOT NULL COMMENT '采购入库单状态，1：编辑中，2：待审核，3：已完成',
  `total_settlement_amount` decimal(20, 2) NOT NULL COMMENT '总结算金额',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `update_time` datetime(0) NOT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of finance_ourchase_settlement_order
-- ----------------------------

-- ----------------------------
-- Table structure for finance_purchase_settlement_order_item
-- ----------------------------
DROP TABLE IF EXISTS `finance_purchase_settlement_order_item`;
CREATE TABLE `finance_purchase_settlement_order_item`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `purchase_settlement_order_id` bigint(20) NOT NULL COMMENT '采购结算单ID',
  `goods_sku_id` bigint(20) NOT NULL COMMENT 'SKUID',
  `purchase_count` bigint(20) NOT NULL COMMENT '采购数量',
  `purchase_price` decimal(20, 2) NOT NULL COMMENT '采购价格',
  `qualified_count` bigint(20) NOT NULL COMMENT '合格商品数量',
  `arrival_count` bigint(20) NOT NULL COMMENT '到货数量',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `update_time` datetime(0) NOT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of finance_purchase_settlement_order_item
-- ----------------------------

-- ----------------------------
-- Table structure for findnce_return_goods_refund_detail
-- ----------------------------
DROP TABLE IF EXISTS `findnce_return_goods_refund_detail`;
CREATE TABLE `findnce_return_goods_refund_detail`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `order_id` bigint(20) NOT NULL COMMENT '订单id',
  `order_on` varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '订单编号',
  `return_goods_worksheet_id` bigint(20) NOT NULL COMMENT '退货工单ID',
  `return_goods_warehose_entry_order_id` bigint(20) NOT NULL COMMENT '退货入库单ID',
  `user_account_id` bigint(20) NOT NULL COMMENT '用户账号id',
  `refundl_amount` decimal(20, 2) NOT NULL COMMENT '退款金额',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `update_time` datetime(0) NOT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of findnce_return_goods_refund_detail
-- ----------------------------

-- ----------------------------
-- Table structure for inventory_goods_stock
-- ----------------------------
DROP TABLE IF EXISTS `inventory_goods_stock`;
CREATE TABLE `inventory_goods_stock`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `goods_sku_id` bigint(20) NOT NULL COMMENT 'SKU ID',
  `sale_stock_quantity` bigint(20) NOT NULL COMMENT '销售库存',
  `locked_stock_quantity` bigint(20) NOT NULL COMMENT '锁定库存',
  `saled_stock_quantity` bigint(20) NOT NULL COMMENT '已销售库存',
  `stock_status` tinyint(4) NOT NULL COMMENT '库存状态，0：无库存，1：有库存',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `update_time` datetime(0) NOT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of inventory_goods_stock
-- ----------------------------

-- ----------------------------
-- Table structure for inventory_offine_sotck_update_message
-- ----------------------------
DROP TABLE IF EXISTS `inventory_offine_sotck_update_message`;
CREATE TABLE `inventory_offine_sotck_update_message`  (
  `id` bigint(20) NOT NULL,
  `message_id` varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '消息id',
  `operation` tinyint(4) NOT NULL COMMENT '操作类型',
  `parameter` longtext CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '参数，json格式',
  `parameter_clazz` varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '参数类型',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `update_time` datetime(0) NOT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of inventory_offine_sotck_update_message
-- ----------------------------

-- ----------------------------
-- Table structure for logistics_freight_template
-- ----------------------------
DROP TABLE IF EXISTS `logistics_freight_template`;
CREATE TABLE `logistics_freight_template`  (
  `Id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '模板名称',
  `type` tinyint(4) NOT NULL COMMENT '运费模板类型，1：固定运费，2：满X元包邮，3：自定义规则',
  `rule` varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '运费模板的规则',
  `remark` varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '运费模板的说明备注',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `update_time` datetime(0) NOT NULL COMMENT '修改时间',
  PRIMARY KEY (`Id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of logistics_freight_template
-- ----------------------------

-- ----------------------------
-- Table structure for membership
-- ----------------------------
DROP TABLE IF EXISTS `membership`;
CREATE TABLE `membership`  (
  `uid` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `username` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户名称',
  `account` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户账号',
  `password` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '密码',
  `phone` varchar(11) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '手机号',
  `email` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '邮箱',
  `qq` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'QQ',
  `wx` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '微信',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `update_time` datetime(0) NOT NULL COMMENT '修改时间',
  PRIMARY KEY (`uid`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of membership
-- ----------------------------
INSERT INTO `membership` VALUES (1, '', '17573404517', '', '17573404517', NULL, NULL, NULL, '2021-11-26 23:48:06', '2021-11-26 23:48:06');
INSERT INTO `membership` VALUES (2, '', 'string', '', 'string', NULL, NULL, NULL, '2021-11-26 16:48:06', '2021-11-26 16:48:06');

-- ----------------------------
-- Table structure for membership_delivery_address
-- ----------------------------
DROP TABLE IF EXISTS `membership_delivery_address`;
CREATE TABLE `membership_delivery_address`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_account_id` bigint(20) NOT NULL COMMENT '用户id',
  `province` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '省',
  `city` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '市',
  `district` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '区',
  `consignee` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '收货人',
  `address` varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '收货地址',
  `phone` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '手机号',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `update_time` datetime(0) NOT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of membership_delivery_address
-- ----------------------------

-- ----------------------------
-- Table structure for membership_detail
-- ----------------------------
DROP TABLE IF EXISTS `membership_detail`;
CREATE TABLE `membership_detail`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `membership_id` bigint(20) NOT NULL COMMENT '用户id',
  `real_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '真实姓名',
  `sex` tinyint(1) NULL DEFAULT NULL COMMENT '性别',
  `birthday` datetime(0) NULL DEFAULT NULL COMMENT '生日',
  `id_number` int(11) NULL DEFAULT NULL,
  `address` varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '家庭地址',
  `crate_time` datetime(0) NOT NULL COMMENT '创建时间',
  `update_time` datetime(0) NOT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of membership_detail
-- ----------------------------

-- ----------------------------
-- Table structure for membership_integral
-- ----------------------------
DROP TABLE IF EXISTS `membership_integral`;
CREATE TABLE `membership_integral`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_account_id` bigint(20) NOT NULL COMMENT '会员id',
  `integral` decimal(20, 2) NOT NULL COMMENT '积分',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `update_time` datetime(0) NOT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of membership_integral
-- ----------------------------

-- ----------------------------
-- Table structure for membership_integral_deatail
-- ----------------------------
DROP TABLE IF EXISTS `membership_integral_deatail`;
CREATE TABLE `membership_integral_deatail`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_account_id` bigint(20) NOT NULL COMMENT '用户账号id',
  `old_member_integral` decimal(20, 2) NOT NULL COMMENT '变动之前的用户积分',
  `updated_memeber_integral` decimal(20, 2) NOT NULL COMMENT '本次变动的会员积分',
  `new_member_integral` decimal(20, 2) NOT NULL COMMENT '本次变动之后的会员积分',
  `update_reason` varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '积分变动说明',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `update_time` datetime(0) NOT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of membership_integral_deatail
-- ----------------------------

-- ----------------------------
-- Table structure for membership_member_level
-- ----------------------------
DROP TABLE IF EXISTS `membership_member_level`;
CREATE TABLE `membership_member_level`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_account_id` bigint(20) NOT NULL COMMENT '用户账号id',
  `level` tinyint(4) NOT NULL COMMENT '等级 0，1，2，3，4，5，6',
  `level_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '等级名称',
  `growth_value` bigint(20) NOT NULL COMMENT '成长值',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `update_time` datetime(0) NOT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of membership_member_level
-- ----------------------------

-- ----------------------------
-- Table structure for membership_member_level_datail
-- ----------------------------
DROP TABLE IF EXISTS `membership_member_level_datail`;
CREATE TABLE `membership_member_level_datail`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_account_id` bigint(20) NOT NULL COMMENT '用户账号id',
  `old_growth_value` bigint(20) NOT NULL COMMENT '本次变动之前的成长值',
  `new_growth_value` bigint(20) NOT NULL COMMENT '本次变动之后的成长值',
  `updated_growth_value` bigint(20) UNSIGNED ZEROFILL NOT NULL COMMENT '本次变动的成长值',
  `old_member_level` tinyint(4) NOT NULL COMMENT '本次变动之前的等级',
  `new_member_level` tinyint(4) NOT NULL COMMENT '本次变动之后的等级',
  `update_reason` varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '变动原因',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `update_time` datetime(0) NOT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of membership_member_level_datail
-- ----------------------------

-- ----------------------------
-- Table structure for order_info
-- ----------------------------
DROP TABLE IF EXISTS `order_info`;
CREATE TABLE `order_info`  (
  `id` bigint(20) NOT NULL,
  `order_on` varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '订单编号',
  `user_account_id` bigint(20) NOT NULL COMMENT '用户id',
  `username` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户名',
  `order_status` tinyint(4) NOT NULL COMMENT '订单状态 1：待付款，2：已取消，3：待发货，4：待收货，5：已完成，6：售后中（退货申请待审核），7：交易关闭（退货审核不通过），8：交易中（待寄送退货商品），9：售后中（退货商品待收货），10：售后中（退货待入库），11：（1）售后中（退货已入库），12：交易关闭（完成退款）',
  `consignee` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '收货人',
  `delivery_address` varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '收货地址',
  `consignee_cell_phone_number` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '收货人电话',
  `freigth` decimal(20, 2) NOT NULL COMMENT '运费',
  `pay_type` tinyint(4) NOT NULL COMMENT '支付方式 1.支付宝，2微信，3银联',
  `payment_code` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '支付方式代码',
  `coupon` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '优惠券信息',
  `total_amount` decimal(20, 2) NOT NULL COMMENT '订单总金额',
  `discount_amount` decimal(20, 2) NOT NULL COMMENT '折扣金额',
  `coupon_amount` decimal(20, 2) NOT NULL COMMENT '优惠券金额',
  `payable_amount` decimal(20, 2) NOT NULL COMMENT '应付金额，订单总金额 - 促销活动折扣金额 - 优惠券抵扣金额 + 运费',
  `invoice_title` varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '发票抬头',
  `taxpayer_id` varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '纳税人号',
  `order_comment` varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '订单备注',
  `is_published_comment` tinyint(4) NOT NULL COMMENT '是否评论',
  `confirm_receipt_time` datetime(0) NULL DEFAULT NULL COMMENT '确认收货时间',
  `pay_time` datetime(0) NULL DEFAULT NULL COMMENT '支付时间',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `update_time` datetime(0) NOT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of order_info
-- ----------------------------

-- ----------------------------
-- Table structure for order_item
-- ----------------------------
DROP TABLE IF EXISTS `order_item`;
CREATE TABLE `order_item`  (
  `id` bigint(20) NOT NULL,
  `order_info_id` bigint(20) NOT NULL COMMENT '订单id',
  `order_on` varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '订单编号',
  `goods_id` bigint(20) NOT NULL COMMENT '商品id',
  `goods_sku_id` bigint(20) NOT NULL COMMENT '商品sku id',
  `goods_sku_code` varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '商品编号',
  `goods_name` varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '商品名称',
  `sale_propperties` varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '商品属性',
  `goods_gross_weigth` decimal(8, 2) NOT NULL COMMENT '商品毛重',
  `purchase_quantity` bigint(20) NOT NULL COMMENT '购买数量',
  `purchase_price` decimal(20, 2) NOT NULL COMMENT '购买金额',
  `promotion_activity_id` bigint(20) NOT NULL COMMENT '促销id',
  `goods_length` decimal(8, 2) NOT NULL COMMENT '商品长度',
  `goods_width` decimal(8, 2) NOT NULL COMMENT '商品宽度',
  `goods_height` decimal(8, 2) NOT NULL COMMENT '商品高度',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `update_time` datetime(0) NOT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of order_item
-- ----------------------------

-- ----------------------------
-- Table structure for order_operate_log
-- ----------------------------
DROP TABLE IF EXISTS `order_operate_log`;
CREATE TABLE `order_operate_log`  (
  `id` bigint(20) NOT NULL,
  `order_info_id` bigint(20) NOT NULL COMMENT '订单id',
  `operate_type` tinyint(4) NOT NULL COMMENT '操作类型，1：创建订单，2：手动取消订单，3：自动取消订单，4：支付订单，5：手动确认收货，6：自动确认收货，7：商品发货，8：申请退货，9：退货审核不通过，10：退货审核通过，11：寄送退货商品，12：确认收到退货，13：退货已入库，14：完成退款',
  `operate_content` varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '操作内容',
  `data` text CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '请求json',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `update_time` datetime(0) NOT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of order_operate_log
-- ----------------------------

-- ----------------------------
-- Table structure for order_return_goods_apply
-- ----------------------------
DROP TABLE IF EXISTS `order_return_goods_apply`;
CREATE TABLE `order_return_goods_apply`  (
  `id` bigint(20) NOT NULL,
  `order_info_id` bigint(20) NOT NULL COMMENT '订单id',
  `return_goods_reason` tinyint(4) NOT NULL COMMENT '退货原因，1：质量不好，2：商品不满意，3：买错了，4：无理由退货',
  `return_goods_comment` varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '退货备注',
  `return_goods_apply_status` tinyint(4) NOT NULL COMMENT '退货记录状态，1：待审核，2：审核不通过，3：审核通过',
  `return_goods_logistic_code` varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '退货快递单号',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `update_time` datetime(0) NOT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of order_return_goods_apply
-- ----------------------------

-- ----------------------------
-- Table structure for pay_transaction
-- ----------------------------
DROP TABLE IF EXISTS `pay_transaction`;
CREATE TABLE `pay_transaction`  (
  `id` bigint(20) NOT NULL,
  `order_info_id` bigint(20) NOT NULL COMMENT '订单id',
  `order_on` varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '订单编号',
  `payble_amount` decimal(20, 2) NOT NULL COMMENT '订单总金额',
  `user_account_id` bigint(20) NOT NULL COMMENT '用户账号id',
  `user_pay_account` varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户支付账号',
  `transaction_channel` tinyint(4) NOT NULL COMMENT '支付通道 1：支付宝，2：微信',
  `transaction_number` varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '交易支付流水',
  `finish_pay_time` datetime(0) NOT NULL COMMENT '第三方支付完成时间',
  `response_code` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '交易渠道返回状态码',
  `status` tinyint(4) NOT NULL COMMENT '支付状态，1：待支付，2：支付成功，3：支付失败，4：交易关闭；5：退款',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `update_time` datetime(0) NOT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of pay_transaction
-- ----------------------------

-- ----------------------------
-- Table structure for promotion_activity
-- ----------------------------
DROP TABLE IF EXISTS `promotion_activity`;
CREATE TABLE `promotion_activity`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '促销活动名称',
  `start_time` datetime(0) NOT NULL COMMENT '促销活动开始时间',
  `end_time` datetime(0) NOT NULL COMMENT '促销活动结束时间',
  `remark` varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '促销活动说明备注',
  `status` tinyint(4) NOT NULL COMMENT '促销活动的状态，1：启用，2：停用',
  `rule` varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '促销活动的规则',
  `type` tinyint(4) NOT NULL COMMENT '促销活动的类型，1：满减；2：多买优惠；3：单品促销；4：满赠促销；5：赠品促销',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `update_time` datetime(0) NOT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of promotion_activity
-- ----------------------------

-- ----------------------------
-- Table structure for promotion_activity_goods_relation
-- ----------------------------
DROP TABLE IF EXISTS `promotion_activity_goods_relation`;
CREATE TABLE `promotion_activity_goods_relation`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `promotion_activity_id` bigint(20) NOT NULL COMMENT '促销活动ID',
  `goods_id` bigint(20) NOT NULL COMMENT '关联的某个商品sku的ID，如果将这个字段的值设置为-1，那么代表针对全部商品',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `update_time` datetime(0) NOT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of promotion_activity_goods_relation
-- ----------------------------

-- ----------------------------
-- Table structure for promotion_coupon
-- ----------------------------
DROP TABLE IF EXISTS `promotion_coupon`;
CREATE TABLE `promotion_coupon`  (
  `id` bigint(20) NOT NULL,
  `name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '优惠券名称',
  `type` tinyint(4) NULL DEFAULT NULL COMMENT '优惠券类型，1：现金券，2：满减券',
  `rule` varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '优惠券规则',
  `valid_start_time` datetime(0) NOT NULL COMMENT '有效开始时间',
  `valid_end_time` datetime(0) NOT NULL COMMENT '有效结束时间',
  `give_out_count` bigint(20) NOT NULL COMMENT '优惠券发行数量',
  `received_count` bigint(20) NOT NULL COMMENT '优惠券已经领取的数量',
  `give_out_type` tinyint(4) NOT NULL COMMENT '优惠券发放方式，1：可发放可领取，2：仅可发放，3：仅可领取',
  `status` tinyint(4) NOT NULL COMMENT '优惠券状态，1：未开始；2：发放中，3：已发完；4：已过期',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `update_time` datetime(0) NOT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of promotion_coupon
-- ----------------------------

-- ----------------------------
-- Table structure for promotion_coupon_achieve
-- ----------------------------
DROP TABLE IF EXISTS `promotion_coupon_achieve`;
CREATE TABLE `promotion_coupon_achieve`  (
  `id` bigint(20) NOT NULL,
  `coupon_id` bigint(20) NOT NULL COMMENT '优惠券ID',
  `user_account_id` bigint(20) NOT NULL COMMENT '用户账号ID',
  `is_used` tinyint(4) NOT NULL COMMENT '是否使用过这个优惠券，1：使用了，0：未使用',
  `used_time` datetime(0) NULL DEFAULT NULL COMMENT '使用优惠券时间',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `update_time` datetime(0) NOT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of promotion_coupon_achieve
-- ----------------------------

-- ----------------------------
-- Table structure for purchase_order
-- ----------------------------
DROP TABLE IF EXISTS `purchase_order`;
CREATE TABLE `purchase_order`  (
  `id` bigint(20) NOT NULL,
  `supplier_id` bigint(20) NOT NULL COMMENT '供应商ID',
  `expect_arrival_time` datetime(0) NOT NULL COMMENT '预计到货时间',
  `contactor` varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '联系人',
  `contactor_phone_number` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '联系人电话',
  `contactor_email` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '联系人邮箱',
  `remark` varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '说明备注',
  `purchaser` varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '采购员',
  `status` tinyint(4) NOT NULL COMMENT '采购单状态，1：编辑中，2：待审核，3：已审核，4：待入库，5：待结算，6：已完成',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `update_time` datetime(0) NOT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of purchase_order
-- ----------------------------

-- ----------------------------
-- Table structure for purchase_order_item
-- ----------------------------
DROP TABLE IF EXISTS `purchase_order_item`;
CREATE TABLE `purchase_order_item`  (
  `id` bigint(20) NOT NULL,
  `purchase_order_id` bigint(20) NOT NULL COMMENT '采购单Id',
  `goods_sku_id` bigint(20) NOT NULL COMMENT 'SKUID',
  `purchase_count` bigint(20) NOT NULL COMMENT '采购数量',
  `purchase_price` decimal(20, 2) NOT NULL COMMENT '采购价格',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `update_time` datetime(0) NOT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of purchase_order_item
-- ----------------------------

-- ----------------------------
-- Table structure for purchase_supplier
-- ----------------------------
DROP TABLE IF EXISTS `purchase_supplier`;
CREATE TABLE `purchase_supplier`  (
  `id` bigint(20) NOT NULL,
  `name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '供应商名称',
  `company_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '公司名称',
  `company_address` varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '公司地址',
  `contactor` varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '联系人',
  `contactor_phone_number` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '联系人电话',
  `settlement_period` tinyint(4) NOT NULL COMMENT '账期，1：周结算，2：月结算，3：季度结算',
  `bank_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '银行名称',
  `bank_account` varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '银行账户',
  `bank_account_holder` varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '开户人',
  `invoice_title` varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '发票抬头',
  `taxpayer_id` varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '纳税人识别号',
  `business_scope` varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '经营范围',
  `remark` varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '说明备注',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `update_time` datetime(0) NOT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of purchase_supplier
-- ----------------------------

-- ----------------------------
-- Table structure for scedule_goods_allcation_stock_detail
-- ----------------------------
DROP TABLE IF EXISTS `scedule_goods_allcation_stock_detail`;
CREATE TABLE `scedule_goods_allcation_stock_detail`  (
  `id` bigint(20) NOT NULL,
  `goods_sku_id` bigint(20) NOT NULL COMMENT '商品SKUID',
  `goods_alloction_id` bigint(20) NOT NULL COMMENT '货位库存',
  `put_on_time` datetime(0) NOT NULL COMMENT '上架时间',
  `put_on_quantity` bigint(20) NOT NULL COMMENT '上架商品数量',
  `current_stock_quantity` bigint(20) NOT NULL COMMENT '当前库存数量',
  `locked_stock_quantity` bigint(20) NOT NULL COMMENT '锁定当前库存数量',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `update_time` datetime(0) NOT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of scedule_goods_allcation_stock_detail
-- ----------------------------

-- ----------------------------
-- Table structure for schedule_goods_allocation_stock
-- ----------------------------
DROP TABLE IF EXISTS `schedule_goods_allocation_stock`;
CREATE TABLE `schedule_goods_allocation_stock`  (
  `id` bigint(20) NOT NULL,
  `goods_allocation_id` bigint(20) NOT NULL COMMENT '货位Id',
  `goods_sku_id` bigint(20) NOT NULL COMMENT 'SKUID',
  `available_stock_quantity` bigint(20) NOT NULL COMMENT '可用库存数量',
  `locked_sotck_quantity` bigint(20) NOT NULL COMMENT '锁定库存数量',
  `output_stock_quantity` bigint(20) NOT NULL COMMENT '出库数量',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `update_time` datetime(0) NOT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of schedule_goods_allocation_stock
-- ----------------------------

-- ----------------------------
-- Table structure for schedule_goods_stock
-- ----------------------------
DROP TABLE IF EXISTS `schedule_goods_stock`;
CREATE TABLE `schedule_goods_stock`  (
  `id` bigint(20) NOT NULL,
  `goods_sku_id` bigint(20) NOT NULL COMMENT 'SKUID',
  `available_stock_quantity` bigint(20) NOT NULL COMMENT '可用库存',
  `locked_stock_quantity` bigint(20) NOT NULL COMMENT '锁定库存',
  `output_stock_quantity` bigint(20) NOT NULL COMMENT '已出库库存',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `updte_time` datetime(0) NOT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of schedule_goods_stock
-- ----------------------------

-- ----------------------------
-- Table structure for schedule_order_picking_item
-- ----------------------------
DROP TABLE IF EXISTS `schedule_order_picking_item`;
CREATE TABLE `schedule_order_picking_item`  (
  `id` bigint(20) NOT NULL,
  `order_info_id` bigint(20) NOT NULL COMMENT '订单ID',
  `order_item_id` bigint(20) NOT NULL COMMENT '订单条目ID',
  `goods_allocation_id` bigint(20) NOT NULL COMMENT '货位ID',
  `goods_sku_id` bigint(20) NOT NULL COMMENT '商品SKU ID',
  `picking_count` bigint(20) NOT NULL COMMENT '拣货数量',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `update_time` datetime(0) NOT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of schedule_order_picking_item
-- ----------------------------

-- ----------------------------
-- Table structure for schedule_order_send_out_detail
-- ----------------------------
DROP TABLE IF EXISTS `schedule_order_send_out_detail`;
CREATE TABLE `schedule_order_send_out_detail`  (
  `id` bigint(20) NOT NULL,
  `order_info_id` bigint(20) NOT NULL COMMENT '订单Id',
  `order_item_id` bigint(20) NOT NULL COMMENT '订单条目Id',
  `goods_allocation_stock_detail_id` bigint(20) NOT NULL COMMENT '货位库存明细ID',
  `send_out_count` bigint(20) NOT NULL COMMENT '发货数量',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `update_time` datetime(0) NOT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of schedule_order_send_out_detail
-- ----------------------------

-- ----------------------------
-- Table structure for wms_goods_allocation
-- ----------------------------
DROP TABLE IF EXISTS `wms_goods_allocation`;
CREATE TABLE `wms_goods_allocation`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `code` varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '货位编号',
  `remark` varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '说明备注',
  `gmt_create` datetime(0) NOT NULL COMMENT '创建时间',
  `gmt_modified` datetime(0) NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = 'WMS中心的货位表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wms_goods_allocation
-- ----------------------------

-- ----------------------------
-- Table structure for wms_goods_allocation_stock
-- ----------------------------
DROP TABLE IF EXISTS `wms_goods_allocation_stock`;
CREATE TABLE `wms_goods_allocation_stock`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `goods_allocation_id` bigint(20) NOT NULL COMMENT '货位ID',
  `goods_sku_id` bigint(20) NOT NULL COMMENT '商品sku ID',
  `available_stock_quantity` bigint(20) NOT NULL COMMENT '可用库存数量',
  `locked_stock_quantity` bigint(20) NOT NULL COMMENT '锁定库存数量',
  `output_stock_quantity` bigint(20) NOT NULL COMMENT '已出库库存数量',
  `gmt_create` datetime(0) NOT NULL COMMENT '创建时间',
  `gmt_modified` datetime(0) NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '仓库中的货位库存' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wms_goods_allocation_stock
-- ----------------------------

-- ----------------------------
-- Table structure for wms_goods_allocation_stock_detail
-- ----------------------------
DROP TABLE IF EXISTS `wms_goods_allocation_stock_detail`;
CREATE TABLE `wms_goods_allocation_stock_detail`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `goods_sku_id` bigint(20) NOT NULL COMMENT '商品sku id',
  `goods_allocation_id` bigint(20) NOT NULL COMMENT '货位id',
  `put_on_time` datetime(0) NOT NULL COMMENT '上架时间',
  `put_on_quantity` bigint(20) NOT NULL COMMENT '上架多少件商品',
  `current_stock_quantity` bigint(20) NOT NULL COMMENT '当前这一批上架的商品还有多少件库存',
  `locked_stock_quantity` bigint(20) NOT NULL COMMENT '当前这一批商品被锁定的库存',
  `gmt_create` datetime(0) NOT NULL COMMENT '创建时间',
  `gmt_modified` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wms_goods_allocation_stock_detail
-- ----------------------------

-- ----------------------------
-- Table structure for wms_goods_stock
-- ----------------------------
DROP TABLE IF EXISTS `wms_goods_stock`;
CREATE TABLE `wms_goods_stock`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `goods_sku_id` bigint(20) NOT NULL COMMENT '商品sku ID',
  `available_stock_quantity` bigint(20) NOT NULL COMMENT '可用库存数量',
  `locked_stock_quantity` bigint(20) NOT NULL COMMENT '锁定库存数量',
  `output_stock_quantity` bigint(20) NOT NULL COMMENT '已出库库存数量',
  `gmt_create` datetime(0) NOT NULL COMMENT '创建时间',
  `gmt_modified` datetime(0) NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_goods_sku_id`(`goods_sku_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '调度中心的商品库存' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wms_goods_stock
-- ----------------------------

-- ----------------------------
-- Table structure for wms_logistic_order
-- ----------------------------
DROP TABLE IF EXISTS `wms_logistic_order`;
CREATE TABLE `wms_logistic_order`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `sale_delivery_order_id` bigint(20) NOT NULL COMMENT '销售出库单id',
  `logistic_code` varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '物流单号',
  `content` varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '物流单内容',
  `gmt_create` datetime(0) NOT NULL COMMENT '创建时间',
  `gmt_modified` datetime(0) NOT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_sale_delivery_order_id`(`sale_delivery_order_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wms_logistic_order
-- ----------------------------

-- ----------------------------
-- Table structure for wms_purchase_input_order
-- ----------------------------
DROP TABLE IF EXISTS `wms_purchase_input_order`;
CREATE TABLE `wms_purchase_input_order`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `purchase_order_id` bigint(20) NOT NULL COMMENT '采购单id',
  `supplier_id` bigint(20) NOT NULL COMMENT '供应商ID',
  `expect_arrival_time` datetime(0) NOT NULL COMMENT '预计到货时间',
  `actual_arrival_time` datetime(0) NULL DEFAULT NULL COMMENT '实际到货时间',
  `purchase_contactor` varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '采购联系人',
  `purchase_contactor_phone_number` varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '采购联系电话',
  `purchase_contactor_email` varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '采购联系邮箱',
  `purchase_order_remark` varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '采购单的说明备注',
  `purchaser` varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '采购员',
  `status` tinyint(4) NOT NULL COMMENT '采购入库单状态，1：编辑中，2：待审核，3：已入库，4：待结算，5：已完成',
  `gmt_create` datetime(0) NOT NULL COMMENT '创建时间',
  `gmt_modified` datetime(0) NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = 'wms中心的采购入库单' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wms_purchase_input_order
-- ----------------------------

-- ----------------------------
-- Table structure for wms_purchase_input_order_item
-- ----------------------------
DROP TABLE IF EXISTS `wms_purchase_input_order_item`;
CREATE TABLE `wms_purchase_input_order_item`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `purchase_input_order_id` bigint(20) NOT NULL COMMENT '采购入库单ID',
  `goods_sku_id` bigint(20) NOT NULL COMMENT '商品SKU ID',
  `purchase_count` bigint(20) NOT NULL COMMENT '采购数量',
  `purchase_price` bigint(20) NOT NULL COMMENT '采购价格',
  `qualified_count` bigint(20) NULL DEFAULT NULL COMMENT '合格商品的数量',
  `arrival_count` bigint(20) NULL DEFAULT NULL COMMENT '到货的商品数量',
  `gmt_create` datetime(0) NOT NULL COMMENT '创建时间',
  `gmt_modified` datetime(0) NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = 'wms中心的采购入库单条目表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wms_purchase_input_order_item
-- ----------------------------

-- ----------------------------
-- Table structure for wms_purchase_input_order_put_on_item
-- ----------------------------
DROP TABLE IF EXISTS `wms_purchase_input_order_put_on_item`;
CREATE TABLE `wms_purchase_input_order_put_on_item`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `purchase_input_order_item_id` bigint(20) NOT NULL COMMENT '采购入库单条目ID',
  `goods_allocation_id` bigint(20) NOT NULL COMMENT '货位ID',
  `goods_sku_id` bigint(20) NOT NULL COMMENT '商品sku id',
  `put_on_shelves_count` bigint(20) NOT NULL COMMENT '上架数量',
  `gmt_create` datetime(0) NOT NULL COMMENT '创建时间',
  `gmt_modified` datetime(0) NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '采购入库单条目关联的上架条目' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wms_purchase_input_order_put_on_item
-- ----------------------------

-- ----------------------------
-- Table structure for wms_return_goods_input_order
-- ----------------------------
DROP TABLE IF EXISTS `wms_return_goods_input_order`;
CREATE TABLE `wms_return_goods_input_order`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `return_goods_worksheet_id` bigint(20) NOT NULL COMMENT '退货工单id',
  `user_account_id` char(10) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户账号ID',
  `order_id` bigint(20) NOT NULL COMMENT '订单ID',
  `order_no` varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '订单编号，随机生成的UUID',
  `status` tinyint(4) NOT NULL COMMENT '退货入库单状态，1：编辑中，2：待审核：3：已完成',
  `consignee` varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '收货人',
  `delivery_address` varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '收货地址',
  `consignee_cell_phone_number` varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '收货人电话号码',
  `freight` decimal(8, 2) NOT NULL COMMENT '运费',
  `pay_type` tinyint(4) NOT NULL COMMENT '支付方式，1：支付宝，2：微信',
  `total_amount` decimal(8, 2) NOT NULL COMMENT '订单总金额',
  `discount_amount` decimal(8, 2) NOT NULL COMMENT '促销活动折扣金额',
  `coupon_amount` decimal(8, 2) NOT NULL COMMENT '优惠券抵扣金额',
  `payable_amount` decimal(8, 2) NOT NULL COMMENT '应付金额，订单总金额 - 促销活动折扣金额 - 优惠券抵扣金额 + 运费',
  `invoice_title` varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '发票抬头',
  `taxpayer_id` varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '纳税人识别号',
  `order_comment` varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '订单备注',
  `return_goods_reason` tinyint(4) NOT NULL COMMENT '退货原因，1：质量不好，2：商品不满意，3：买错了，4：无理由退货',
  `return_goods_remark` varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '退货备注',
  `arrival_time` datetime(0) NULL DEFAULT NULL COMMENT '到货时间',
  `gmt_create` datetime(0) NOT NULL COMMENT '创建时间',
  `gmt_modified` datetime(0) NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = 'wms中心的退货入库单' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wms_return_goods_input_order
-- ----------------------------

-- ----------------------------
-- Table structure for wms_return_goods_input_order_item
-- ----------------------------
DROP TABLE IF EXISTS `wms_return_goods_input_order_item`;
CREATE TABLE `wms_return_goods_input_order_item`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `return_goods_input_order_id` bigint(20) NOT NULL COMMENT '退货入库单ID',
  `goods_sku_id` bigint(20) NOT NULL COMMENT '商品sku ID',
  `goods_sku_code` varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '商品sku编号',
  `goods_name` varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '商品名称',
  `sale_properties` varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '销售属性，机身颜色:白色,内存容量:256G',
  `goods_gross_weight` decimal(8, 2) NOT NULL COMMENT '商品毛重',
  `purchase_quantity` bigint(20) NOT NULL COMMENT '购买数量',
  `purchase_price` decimal(8, 2) NOT NULL COMMENT '商品购买价格',
  `promotion_activity_id` bigint(20) NULL DEFAULT NULL COMMENT '促销活动ID',
  `goods_length` decimal(8, 2) NOT NULL COMMENT '商品长度',
  `goods_width` decimal(8, 2) NOT NULL COMMENT '商品宽度',
  `goods_height` decimal(8, 2) NOT NULL COMMENT '商品高度',
  `qualified_count` bigint(20) NULL DEFAULT NULL COMMENT '合格商品数量',
  `arrival_count` bigint(20) NULL DEFAULT NULL COMMENT '商品到货数量',
  `gmt_create` datetime(0) NOT NULL COMMENT '创建时间',
  `gmt_modified` datetime(0) NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '退货入库单条目' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wms_return_goods_input_order_item
-- ----------------------------

-- ----------------------------
-- Table structure for wms_return_goods_input_order_put_on_item
-- ----------------------------
DROP TABLE IF EXISTS `wms_return_goods_input_order_put_on_item`;
CREATE TABLE `wms_return_goods_input_order_put_on_item`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `return_goods_input_order_item_id` bigint(20) NOT NULL COMMENT '退货入库单条目ID',
  `goods_allocation_id` bigint(20) NOT NULL COMMENT '货位ID',
  `goods_sku_id` bigint(20) NULL DEFAULT NULL COMMENT '商品sku id',
  `put_on_shelves_count` bigint(20) NOT NULL COMMENT '上架数量',
  `gmt_create` datetime(0) NOT NULL COMMENT '创建时间',
  `gmt_modified` datetime(0) NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '退货入库单条目关联的上架条目' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wms_return_goods_input_order_put_on_item
-- ----------------------------

-- ----------------------------
-- Table structure for wms_sale_delivery_order
-- ----------------------------
DROP TABLE IF EXISTS `wms_sale_delivery_order`;
CREATE TABLE `wms_sale_delivery_order`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `order_id` bigint(20) NOT NULL COMMENT '订单ID',
  `order_no` varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '订单编号，随机生成的UUID',
  `user_account_id` char(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户账号ID',
  `consignee` varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '收货人',
  `delivery_address` varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '收货地址',
  `consignee_cell_phone_number` varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '收货人电话号码',
  `freight` decimal(8, 2) NOT NULL COMMENT '运费',
  `pay_type` tinyint(4) NOT NULL COMMENT '支付方式，1：支付宝，2：微信',
  `total_amount` decimal(8, 2) NOT NULL COMMENT '订单总金额',
  `discount_amount` decimal(8, 2) NOT NULL COMMENT '促销活动折扣金额',
  `coupon_amount` decimal(8, 2) NOT NULL COMMENT '优惠券抵扣金额',
  `payable_amount` decimal(8, 2) NOT NULL COMMENT '应付金额，订单总金额 - 促销活动折扣金额 - 优惠券抵扣金额 + 运费',
  `invoice_title` varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '发票抬头',
  `taxpayer_id` varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '纳税人识别号',
  `order_comment` varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '订单备注',
  `status` tinyint(4) NOT NULL COMMENT '销售出库单的状态，1：编辑中，2：待审核，3：已完成',
  `delivery_time` datetime(0) NULL DEFAULT NULL COMMENT '发货时间',
  `gmt_create` datetime(0) NOT NULL COMMENT '创建时间',
  `gmt_modified` datetime(0) NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_order_id`(`order_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = 'wms中心的销售出库单' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wms_sale_delivery_order
-- ----------------------------

-- ----------------------------
-- Table structure for wms_sale_delivery_order_item
-- ----------------------------
DROP TABLE IF EXISTS `wms_sale_delivery_order_item`;
CREATE TABLE `wms_sale_delivery_order_item`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `sale_delivery_order_id` bigint(20) NOT NULL COMMENT '销售出库单ID',
  `goods_sku_id` bigint(20) NOT NULL COMMENT '商品sku ID',
  `goods_sku_code` varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '商品sku编号',
  `goods_name` varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '商品名称',
  `sale_properties` varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '销售属性，机身颜色:白色,内存容量:256G',
  `goods_gross_weight` decimal(8, 2) NOT NULL COMMENT '商品毛重',
  `purchase_quantity` bigint(20) NOT NULL COMMENT '购买数量',
  `purchase_price` decimal(8, 2) NOT NULL COMMENT '商品购买价格',
  `promotion_activity_id` bigint(20) NULL DEFAULT NULL COMMENT '促销活动ID',
  `goods_length` decimal(8, 2) NOT NULL COMMENT '商品长度',
  `goods_width` decimal(8, 2) NOT NULL COMMENT '商品宽度',
  `goods_height` decimal(8, 2) NOT NULL COMMENT '商品高度',
  `gmt_create` datetime(0) NOT NULL COMMENT '创建时间',
  `gmt_modified` datetime(0) NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_sale_delivery_order_id`(`sale_delivery_order_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = 'wms中心的销售出库单条目' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wms_sale_delivery_order_item
-- ----------------------------

-- ----------------------------
-- Table structure for wms_sale_delivery_order_picking_item
-- ----------------------------
DROP TABLE IF EXISTS `wms_sale_delivery_order_picking_item`;
CREATE TABLE `wms_sale_delivery_order_picking_item`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `sale_delivery_order_item_id` bigint(20) NOT NULL COMMENT '销售出库单条目ID',
  `goods_allocation_id` bigint(20) NOT NULL COMMENT '货位ID',
  `goods_sku_id` bigint(20) NOT NULL COMMENT '商品sku id',
  `picking_count` bigint(20) NOT NULL COMMENT '发多少件商品',
  `gmt_create` datetime(0) NOT NULL COMMENT '创建时间',
  `gmt_modified` datetime(0) NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_sale_delivery_order_item_id`(`sale_delivery_order_item_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '销售出库单的拣货条目' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wms_sale_delivery_order_picking_item
-- ----------------------------

-- ----------------------------
-- Table structure for wms_sale_delivery_order_send_out_detail
-- ----------------------------
DROP TABLE IF EXISTS `wms_sale_delivery_order_send_out_detail`;
CREATE TABLE `wms_sale_delivery_order_send_out_detail`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `sale_delivery_order_item_id` bigint(20) NOT NULL COMMENT '销售出库单条目id',
  `goods_allocation_stock_detail_id` bigint(20) NOT NULL COMMENT '货位库存明细id',
  `send_out_count` bigint(20) NOT NULL COMMENT '发货数量',
  `gmt_create` datetime(0) NOT NULL COMMENT '创建时间',
  `gmt_modified` datetime(0) NOT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_sale_delivery_order_item_id`(`sale_delivery_order_item_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wms_sale_delivery_order_send_out_detail
-- ----------------------------

-- ----------------------------
-- Table structure for wms_send_out_order
-- ----------------------------
DROP TABLE IF EXISTS `wms_send_out_order`;
CREATE TABLE `wms_send_out_order`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `sale_delivery_order_id` bigint(20) NOT NULL COMMENT '销售出库单id',
  `user_account_id` bigint(20) NOT NULL COMMENT '用户账号ID',
  `username` varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户名',
  `order_id` bigint(20) NOT NULL COMMENT '订单id',
  `order_no` varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '订单编号，随机生成的UUID',
  `consignee` varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '收货人',
  `delivery_address` varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '收货地址',
  `consignee_cell_phone_number` varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '收货人电话号码',
  `freight` decimal(8, 2) NOT NULL COMMENT '运费',
  `pay_type` tinyint(4) NOT NULL COMMENT '支付方式，1：支付宝，2：微信',
  `total_amount` decimal(8, 2) NOT NULL COMMENT '订单总金额',
  `discount_amount` decimal(8, 2) NOT NULL COMMENT '促销活动折扣金额',
  `coupon_amount` decimal(8, 2) NOT NULL COMMENT '优惠券抵扣金额',
  `payable_amount` decimal(8, 2) NOT NULL COMMENT '应付金额，订单总金额 - 促销活动折扣金额 - 优惠券抵扣金额 + 运费',
  `invoice_title` varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '发票抬头',
  `taxpayer_id` varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '纳税人识别号',
  `order_comment` varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '订单备注',
  `gmt_create` datetime(0) NOT NULL COMMENT '创建时间',
  `gmt_modified` datetime(0) NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_sale_delivery_order_id`(`sale_delivery_order_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '订单信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wms_send_out_order
-- ----------------------------

-- ----------------------------
-- Table structure for wms_send_out_order_item
-- ----------------------------
DROP TABLE IF EXISTS `wms_send_out_order_item`;
CREATE TABLE `wms_send_out_order_item`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `send_out_order_id` bigint(20) NOT NULL COMMENT '发货单id',
  `goods_id` bigint(20) NOT NULL COMMENT '商品id',
  `goods_sku_id` bigint(20) NOT NULL COMMENT '商品sku ID',
  `goods_sku_code` varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '商品sku编号',
  `goods_name` varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '商品名称',
  `sale_properties` varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '销售属性，机身颜色:白色,内存容量:256G',
  `goods_gross_weight` decimal(8, 2) NOT NULL COMMENT '商品毛重',
  `purchase_quantity` bigint(20) NOT NULL COMMENT '购买数量',
  `purchase_price` decimal(8, 2) NOT NULL COMMENT '商品购买价格',
  `goods_length` decimal(8, 2) NOT NULL COMMENT '商品长度',
  `goods_width` decimal(8, 2) NOT NULL COMMENT '商品宽度',
  `goods_height` decimal(8, 2) NOT NULL COMMENT '商品高度',
  `gmt_create` datetime(0) NOT NULL COMMENT '创建时间',
  `gmt_modified` datetime(0) NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_send_out_order_id`(`send_out_order_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '订单商品条目' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wms_send_out_order_item
-- ----------------------------

SET FOREIGN_KEY_CHECKS = 1;
