CREATE TABLE IF NOT EXISTS reservation (
    reservation_id INT AUTO_INCREMENT PRIMARY KEY, -- 自增主鍵
    customer_name VARCHAR(255) NOT NULL, -- 客戶名稱，不可為空
    customer_phone_number VARCHAR(255) NOT NULL, -- 客戶電話號碼，不可為空
    customer_email VARCHAR(255) NOT NULL, -- 客戶電子郵件，不可為空
    customer_gender ENUM('先生', '小姐') NOT NULL, -- 使用 ENUM 表示性別
    reservation_people INT NOT NULL, -- 預約人數，不可為空
    reservation_date DATE NOT NULL, -- 預約日期，合併自 reservation_management
    reservation_starttime TIME NOT NULL, -- 預約開始時間，合併自 reservation_management
    reservation_endingtime TIME NOT NULL -- 預約結束時間，合併自 reservation_management
);
ALTER TABLE reservation CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;


CREATE TABLE IF NOT EXISTS table_management (
  table_number varchar(20) NOT NULL,
  table_capacity int DEFAULT NULL,
  table_status enum('可使用', '訂位中', '用餐中') DEFAULT '可使用',  -- 更新為中文枚舉值，並設定默認值
  reservation_id INT,  -- reservation_id 欄位，連接到 reservation 表的主鍵
  PRIMARY KEY (table_number),
  CONSTRAINT fk_table_reservation FOREIGN KEY (reservation_id) REFERENCES reservation (reservation_id) -- 外鍵約束，連接到 reservation 表
);

CREATE TABLE IF NOT EXISTS pos_project.reservation_table (
    reservation_id INT,
    table_number VARCHAR(20),
    PRIMARY KEY (reservation_id, table_number),
    FOREIGN KEY (reservation_id) REFERENCES pos_project.reservation (reservation_id),
    FOREIGN KEY (table_number) REFERENCES pos_project.table_management (table_number)
);

CREATE TABLE IF NOT EXISTS operating_hours (
    id INT NOT NULL AUTO_INCREMENT,
    day_of_week ENUM('星期一', '星期二', '星期三', '星期四', '星期五', '星期六', '星期日') NOT NULL, -- 更新為枚舉值
    opening_time TIME NOT NULL,
    closing_time TIME NOT NULL,
    dining_duration INT NOT NULL, -- 用餐時間，以分鐘為單位
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS waitlist (
    waitlist_id INT AUTO_INCREMENT PRIMARY KEY, 
    customer_name VARCHAR(255) NOT NULL,        
    customer_phone_number VARCHAR(15) NOT NULL, 
    customer_email VARCHAR(255) NOT NULL,      
    customer_gender ENUM('先生', '小姐') NOT NULL, 
    waitlist_people INT NOT NULL,               
    waiting_date DATE NOT NULL,                 
    wait_time TIME NOT NULL,                  
    waitlist_order INT NOT NULL          
);

CREATE TABLE IF NOT EXISTS categories (
  category_id int NOT NULL AUTO_INCREMENT,
  category varchar(45) DEFAULT NULL,
  workstation_id int DEFAULT NULL,
  PRIMARY KEY (`category_id`)
);

CREATE TABLE IF NOT EXISTS menu_items (
  meal_name varchar(45) NOT NULL,
  category_id int DEFAULT NULL,
  workstation_id int DEFAULT NULL,
  price int DEFAULT '0',
  available tinyint DEFAULT '1',
  picture_name longtext,
  PRIMARY KEY (`meal_name`)
);

CREATE TABLE IF NOT EXISTS options (
  option_title varchar(45) NOT NULL,
  cg_id int NOT NULL,
  option_content varchar(45) NOT NULL,
  option_type enum('checkbox','radio') NOT NULL,
  extra_price int DEFAULT '0',
  PRIMARY KEY (`option_title`,`cg_id`,`option_content`)
);

CREATE TABLE IF NOT EXISTS combo_items (
  combo_name varchar(45) NOT NULL,
  combo_detail varchar(1000) DEFAULT NULL,
  discount_amount int DEFAULT NULL,
  category_id int DEFAULT NULL,
  PRIMARY KEY (`combo_name`)
);


CREATE TABLE IF NOT EXISTS checkout_list (
  order_id varchar(60) NOT NULL,
  table_number varchar(20) DEFAULT NULL,
  total_price int DEFAULT NULL,
  pay_type varchar(45) DEFAULT NULL,
  checkout tinyint DEFAULT '0',
  checkout_time datetime DEFAULT NULL,
  PRIMARY KEY (`order_id`)
);

CREATE TABLE  IF NOT EXISTS `orders` (
  `id` int NOT NULL AUTO_INCREMENT,
  `order_id` varchar(60) DEFAULT NULL,
  `order_meal_id` varchar(60) DEFAULT NULL,
  `combo_name` varchar(60) DEFAULT NULL,
  `meal_name` varchar(60) DEFAULT NULL,
  `options` varchar(60) DEFAULT NULL,
  `workstation_id` int DEFAULT NULL,
  `price` int DEFAULT NULL,
  `meal_status` enum('準備中','待送餐點','已送達') DEFAULT NULL,
  `table_number` varchar(20) DEFAULT NULL,
  `order_time` datetime DEFAULT NULL,
  `checkout` tinyint DEFAULT '0',
  PRIMARY KEY (`id`)
);

CREATE TABLE IF NOT EXISTS checkout_list_history (
  order_id varchar(60) NOT NULL,
  table_number varchar(20) DEFAULT NULL,
  total_price int DEFAULT NULL,
  pay_type varchar(45) DEFAULT NULL,
  checkout tinyint DEFAULT '0',
  checkout_time datetime DEFAULT NULL,
  PRIMARY KEY (`order_id`)
);

CREATE TABLE IF NOT EXISTS orders_history (
  `id` int NOT NULL AUTO_INCREMENT,
  `order_id` varchar(60) DEFAULT NULL,
  `order_meal_id` varchar(60) DEFAULT NULL,
  `combo_name` varchar(60) DEFAULT NULL,
  `meal_name` varchar(60) DEFAULT NULL,
  `options` varchar(60) DEFAULT NULL,
  `workstation_id` int DEFAULT NULL,
  `price` int DEFAULT NULL,
  `meal_status` enum('準備中','待送餐點','已送達') DEFAULT NULL,
  `table_number` varchar(20) DEFAULT NULL,
  `order_time` datetime DEFAULT NULL,
  `checkout` tinyint DEFAULT '0',
  PRIMARY KEY (`id`)
);

CREATE TABLE IF NOT EXISTS workstation (
  workstation_id int NOT NULL AUTO_INCREMENT,
  workstation_name varchar(20) DEFAULT NULL,
  PRIMARY KEY (`workstation_id`)
);

CREATE TABLE IF NOT EXISTS announce (
  announce_id int NOT NULL AUTO_INCREMENT,
  announce_title varchar(60) DEFAULT NULL,
  announce_content text,
  announce_picture_name longtext,
  announce_starttime date DEFAULT NULL,
  announce_endtime date DEFAULT NULL,
  PRIMARY KEY (`announce_id`)
);

CREATE TABLE IF NOT EXISTS member (
  member_id int NOT NULL AUTO_INCREMENT,
  pwd varchar(100) NOT NULL,
  name varchar(45) NOT NULL,
  phone varchar(20) NOT NULL,
  birthday date NOT NULL,
  email varchar(90) NOT NULL,
  total_spending_amount int NOT NULL,
  member_level varchar(20) NOT NULL,
  verification_code varchar(6) DEFAULT NULL,
  verification_code_expiry datetime DEFAULT NULL,
  PRIMARY KEY (`member_id`)
);

CREATE TABLE IF NOT EXISTS staff (
  staff_number varchar(45) NOT NULL,
  pwd varchar(100) NOT NULL,
  name varchar(45) NOT NULL,
  phone varchar(45) NOT NULL,
  authorization varchar(45) NOT NULL,
  email varchar(90) NOT NULL,
  verification_code varchar(45) DEFAULT NULL,
  verification_code_expiry varchar(45) DEFAULT NULL,
  error_count int NOT NULL DEFAULT '0',
  block_time datetime DEFAULT NULL,
  first_login tinyint DEFAULT '1',
  PRIMARY KEY (`staff_number`)
);


CREATE TABLE IF NOT EXISTS authorization (
  authorization_id int NOT NULL AUTO_INCREMENT,
  authorization_name varchar(45) NOT NULL,
  authorization_item varchar(90) NOT NULL,
  PRIMARY KEY (`authorization_id`)
);