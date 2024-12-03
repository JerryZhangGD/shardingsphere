
//doris代理配置
CREATE DATABASE ods;

use ods;


REGISTER STORAGE UNIT ods (
    HOST="10.1.7.171",
    PORT=9030,
    DB="ods",
    USER="admin",
    PASSWORD="admin123",
    PROPERTIES("maximumPoolSize"=10)
);

SHOW STORAGE UNITS;

CREATE DEFAULT SINGLE TABLE RULE RESOURCE = ods




-- 创建脱敏规则
CREATE MASK RULE from_SRM_PRE_srm_uat_sprm_pr_line (
    COLUMNS(
        (NAME=pr_requested_name, TYPE(NAME='MD5')),
        (NAME=item_name, TYPE(NAME='KEEP_FIRST_N_LAST_M', PROPERTIES("first-n"=3, "last-m"=4, "replace-char"="*")))
        
    )
);

SHOW MASK RULES FROM ods;

DROP MASK RULE IF EXISTS from_SRM_PRE_srm_uat_sprm_pr_line 


select * from from_SRM_PRE_srm_uat_sprm_pr_line;









DROP TABLE IF EXISTS t_user;
 

CREATE TABLE t_user (user_id BIGINT NOT NULL, user_name VARCHAR(50) NOT NULL, password VARCHAR(50) NOT NULL, email VARCHAR(50) NOT NULL, telephone VARCHAR(50) NOT NULL, creation_date DATE NOT NULL)
UNIQUE KEY(user_id)
DISTRIBUTED BY HASH (user_id) BUCKETS 10
PROPERTIES(
    "storage_medium" = "SSD",
    "storage_cooldown_time" = "2015-06-04 00:00:00"
);


INSERT INTO t_user(user_id, user_name, password, email, telephone, creation_date) values(10, 'zhangsan', '111111', 'zhangsan@gmail.com', '12345678900', '2017-08-08'),
(11, 'lisi', '222222', 'lisi@gmail.com', '12345678901', '2017-08-08'),
(12, 'wangwu', '333333', 'wangwu@gmail.com', '12345678902', '2017-08-08'),
(13, 'zhaoliu', '444444', 'zhaoliu@gmail.com', '12345678903', '2017-08-08'),
(14, 'zhuqi', '555555', 'zhuqi@gmail.com', '12345678904', '2017-08-08'),
(15, 'liba', '666666', 'liba@gmail.com', '12345678905', '2017-08-08'),
(16, 'wangjiu', '777777', 'wangjiu@gmail.com', '12345678906', '2017-08-08'),
(17, 'zhuda', '888888', 'zhuda@gmail.com', '12345678907', '2017-08-08'),
(18, 'suner', '999999', 'suner@gmail.com', '12345678908', '2017-08-08'),
(19, 'zhousan', '123456', 'zhousan@gmail.com', '12345678909', '2017-08-08'),
(20, 'tom', '234567', 'tom@gmail.com', '12345678910', '2017-08-08'),
(21, 'kobe', '345678', 'kobe@gmail.com', '12345678911', '2017-08-08'),
(22, 'jerry', '456789', 'jerry@gmail.com', '12345678912', '2017-08-08'),
(23, 'james', '567890', 'james@gmail.com', '12345678913', '2017-08-08'),
(24, 'wade', '012345', 'wade@gmail.com', '12345678914', '2017-08-08'),
(25, 'rose', '000000', 'rose@gmail.com', '12345678915', '2017-08-08'),
(26, 'bosh', '111222', 'bosh@gmail.com', '12345678916', '2017-08-08'),
(27, 'jack', '222333', 'jack@gmail.com', '12345678917', '2017-08-08'),
(28, 'jordan', '333444', 'jordan@gmail.com', '12345678918', '2017-08-08'),
(29, 'julie', '444555', 'julie@gmail.com', '12345678919', '2017-08-08');


SELECT * FROM t_user 























//mysql代理配置
CREATE DATABASE test_seatunnel;

use test_seatunnel


REGISTER STORAGE UNIT test_seatunnel (
    HOST="10.1.7.171",
    PORT=30504,
    DB="test_seatunnel",
    USER="root",
    PASSWORD="regEWv4Bqe",
    PROPERTIES("maximumPoolSize"=10)
);

SHOW STORAGE UNITS from test_seatunnel;

-- 创建脱敏规则
CREATE MASK RULE t_user (
    COLUMNS(
        (NAME=password, TYPE(NAME='MD5')),
        (NAME=email, TYPE(NAME='MASK_BEFORE_SPECIAL_CHARS', PROPERTIES("special-chars"="@", "replace-char"="*"))),
        (NAME=telephone, TYPE(NAME='KEEP_FIRST_N_LAST_M', PROPERTIES("first-n"=3, "last-m"=4, "replace-char"="*")))
    )
);

SHOW MASK RULES FROM test_seatunnel;



DROP TABLE IF EXISTS t_user;
 

CREATE TABLE t_user (user_id INT PRIMARY KEY, user_name VARCHAR(50) NOT NULL, password VARCHAR(50) NOT NULL, email VARCHAR(50) NOT NULL, telephone CHAR(50) NOT NULL, creation_date DATE NOT NULL);


INSERT INTO t_user(user_id, user_name, password, email, telephone, creation_date) values(10, 'zhangsan', '111111', 'zhangsan@gmail.com', '12345678900', '2017-08-08'),
(11, 'lisi', '222222', 'lisi@gmail.com', '12345678901', '2017-08-08'),
(12, 'wangwu', '333333', 'wangwu@gmail.com', '12345678902', '2017-08-08'),
(13, 'zhaoliu', '444444', 'zhaoliu@gmail.com', '12345678903', '2017-08-08'),
(14, 'zhuqi', '555555', 'zhuqi@gmail.com', '12345678904', '2017-08-08'),
(15, 'liba', '666666', 'liba@gmail.com', '12345678905', '2017-08-08'),
(16, 'wangjiu', '777777', 'wangjiu@gmail.com', '12345678906', '2017-08-08'),
(17, 'zhuda', '888888', 'zhuda@gmail.com', '12345678907', '2017-08-08'),
(18, 'suner', '999999', 'suner@gmail.com', '12345678908', '2017-08-08'),
(19, 'zhousan', '123456', 'zhousan@gmail.com', '12345678909', '2017-08-08'),
(20, 'tom', '234567', 'tom@gmail.com', '12345678910', '2017-08-08'),
(21, 'kobe', '345678', 'kobe@gmail.com', '12345678911', '2017-08-08'),
(22, 'jerry', '456789', 'jerry@gmail.com', '12345678912', '2017-08-08'),
(23, 'james', '567890', 'james@gmail.com', '12345678913', '2017-08-08'),
(24, 'wade', '012345', 'wade@gmail.com', '12345678914', '2017-08-08'),
(25, 'rose', '000000', 'rose@gmail.com', '12345678915', '2017-08-08'),
(26, 'bosh', '111222', 'bosh@gmail.com', '12345678916', '2017-08-08'),
(27, 'jack', '222333', 'jack@gmail.com', '12345678917', '2017-08-08'),
(28, 'jordan', '333444', 'jordan@gmail.com', '12345678918', '2017-08-08'),
(29, 'julie', '444555', 'julie@gmail.com', '12345678919', '2017-08-08');

SELECT * FROM t_user 



