DROP DATABASE IF EXISTS simulation;
CREATE DATABASE simulation;
USE simulation;

DROP USER IF EXISTS 'appuser'@'localhost';
CREATE USER 'appuser'@'localhost' IDENTIFIED BY '123';
GRANT SELECT, INSERT, DROP, UPDATE, DELETE, CREATE ON simulation.* TO 'appuser'@'localhost';