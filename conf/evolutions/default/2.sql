#---!Ups
ALTER TABLE `screenbuddy_dev`.`service` ADD COLUMN `logo` VARCHAR(255) NULL AFTER `name`;

#---!Downs
ALTER TABLE `screenbuddy_dev`.`service` DROP COLUMN `logo`;