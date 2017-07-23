CREATE TABLE `SummerCamp`.`Ticket` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `user_id` INT NOT NULL,
  `ticket` VARCHAR(45) NOT NULL,
  `status` INT NOT NULL,
  `expired` DATE NOT NULL,
  PRIMARY KEY (`id`));
