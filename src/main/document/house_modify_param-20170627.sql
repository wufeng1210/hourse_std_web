/*2017年6月26日21:55:57*/

ALTER TABLE hourse_info CHANGE preLendUserId preLendUserMobile VARCHAR(200) DEFAULT '' COMMENT '预租人手机号';

ALTER TABLE hourse_info CHANGE nowLendUserId nowLendUserMobile VARCHAR(200) DEFAULT '' COMMENT '已租人手机号';

