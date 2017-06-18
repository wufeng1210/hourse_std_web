/*2017年6月18日21:55:57*/
ALTER TABLE hourse_info ADD preLendUserId VARCHAR(200) DEFAULT '' COMMENT '预租人id';
            
ALTER TABLE hourse_info ADD nowLendUserId VARCHAR(200) DEFAULT '' COMMENT '已租人id';

ALTER TABLE activity_info CHANGE state STATUS VARCHAR(10) DEFAULT '0';
