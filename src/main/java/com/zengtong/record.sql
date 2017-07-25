CREATE TABLE `SummerCamp`.`Ticket` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `user_id` INT NOT NULL,
  `ticket` VARCHAR(45) NOT NULL,
  `status` INT NOT NULL,
  `expired` DATE NOT NULL,
  PRIMARY KEY (`id`));

CREATE TABLE `SummerCamp`.`comment` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `entity_type` INT NOT NULL,
  `entity_id` INT NOT NULL,
  `content` VARCHAR(45) NULL,
  `pic_url` VARCHAR(45) NULL,
  `status` INT NULL,
  `create_date` DATETIME NULL,
  `user_id` INT NULL,
  `like_count` INT NULL,
  `reply_count` INT NULL,
  PRIMARY KEY (`id`));
