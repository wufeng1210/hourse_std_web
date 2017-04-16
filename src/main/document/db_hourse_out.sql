CREATE DATABASE  db_hourse_out;

GRANT SELECT,INSERT,UPDATE,DELETE,CREATE,DROP,ALTER ON db_hourse_out.* TO db_hourse_out@localhost IDENTIFIED BY 'admin';

USE db_hourse_out;

CREATE TABLE hourse_info(
	hourseId INT NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT '主键(自增长)',
	userId INT NOT NULL COMMENT '外键(用户id)',
	hourseAddr VARCHAR(40) DEFAULT '' COMMENT '房屋地址',
	hourseImageUrl VARCHAR(20) DEFAULT '' COMMENT '房屋图片url地址',
	hourseImagePath VARCHAR(20) DEFAULT '' COMMENT '房屋图片路径',
	province VARCHAR(20) DEFAULT '' COMMENT '省份',
	city VARCHAR(20) DEFAULT '' COMMENT '城市',
	AREA VARCHAR(20) DEFAULT '' COMMENT '区域',
	residentialQuarters VARCHAR (20) DEFAULT '' COMMENT '房屋所在小区',
	roomNum INT DEFAULT 0 COMMENT '房间数量',
	toiletNum INT DEFAULT 0 COMMENT '卫生间数量',
	hallNum INT DEFAULT 0 COMMENT '大厅数量',
	kitchenNum INT DEFAULT 0 COMMENT '厨房数量',
	monthly INT DEFAULT 0 COMMENT '月租（元）',
	packingingLot BOOLEAN DEFAULT FALSE COMMENT '是否有车位', 
	rentingWay VARCHAR(20) DEFAULT '' COMMENT '租房方式',
	brokerMobile VARCHAR(20) DEFAULT '' COMMENT '经纪人手机号',
	brokerCode VARCHAR(20) DEFAULT '' COMMENT '经纪人编号',
	brokerName VARCHAR(20) DEFAULT '' COMMENT '经纪人姓名',
	areaCovered INT DEFAULT 0 COMMENT '占比面积（平方）',
	refrigerator BOOLEAN DEFAULT FALSE COMMENT '是否有冰箱',
	heater BOOLEAN DEFAULT FALSE COMMENT '是否有热水器',
	bed BOOLEAN DEFAULT TRUE COMMENT '是否有床',
	desk BOOLEAN DEFAULT TRUE COMMENT '是否有桌子',
	airConditioner BOOLEAN DEFAULT TRUE COMMENT '是否有空调',
	cabinet BOOLEAN DEFAULT TRUE COMMENT '是否有柜子',
	state VARCHAR(20) DEFAULT '' COMMENT '状态（0：未审核；1：审核通过；2：审核打回）',
	description VARCHAR(20) DEFAULT '' COMMENT '描述'
);

CREATE TABLE user_info(
	userId INT NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT '主键(自增长)',
	userName VARCHAR(20) NOT NULL COMMENT '用户名',
	passWord VARCHAR(20) NOT NULL COMMENT '用户密码',
	secretKey VARCHAR(20) NOT NULL COMMENT '密钥',
	userType VARCHAR(20) NOT NULL COMMENT '用户类型（1：前台用户；2：后台用户）',
	roleId INT NOT NULL COMMENT '外键（角色id）',
	userDescription VARCHAR(20) DEFAULT '',
	deptName VARCHAR(20) DEFAULT '' COMMENT '部门',
	agent VARCHAR(20) DEFAULT '' COMMENT '公司'
);

CREATE TABLE user_role(
	roleId INT NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT '主键(自增长)',
	roleName VARCHAR(20) NOT NULL COMMENT '角色名称',
	authIds VARCHAR(20) NOT NULL COMMENT '权限id集合',
	roleDescription VARCHAR(20) DEFAULT '' COMMENT '角色描述'
	
);
CREATE TABLE user_auth(
	authId INT NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT '主键(自增长)',
	authName VARCHAR(20) NOT NULL COMMENT '权限名称',
	authPath VARCHAR(20) NOT NULL COMMENT '权限跳转链接',
	parentId VARCHAR(20) NOT NULL COMMENT '父节点',
	authDescription VARCHAR(20) NOT NULL COMMENT '角色描述',
	state VARCHAR(20) COMMENT '是否展开子节点'
);

CREATE TABLE activity_info(
	activityId INT NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT '主键(自增长)',
	activityTitle VARCHAR(20) NOT NULL COMMENT '活动标题',
	activityImagePath VARCHAR(20) NOT NULL COMMENT '活动图片路径',
	activityImageUrl VARCHAR(20) NOT NULL COMMENT '活动图片地址',
	target VARCHAR(20) NOT NULL COMMENT '内链还是外链',
	state VARCHAR(20) COMMENT '状态（上架：1，下架：0）'
);