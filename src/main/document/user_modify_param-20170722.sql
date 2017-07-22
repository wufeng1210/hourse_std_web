/*2017年7月22日15:26:24*/

ALTER TABLE user_info CHANGE allow allowIds VARCHAR(20) DEFAULT '0' COMMENT '允许（1：绑定；0：不绑定）';

