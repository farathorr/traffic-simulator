DROP
DATABASE IF EXISTS simulation;
CREATE
DATABASE simulation;
USE simulation;
CREATE TABLE results(
    id INT NOT NULL AUTO_INCREMENT,
    carCount    INT(3)  NOT NULL,
    averageTime DECIMAL(3, 2)   NOT NULL,
    simulationTime DECIMAL (3, 2) NOT NULL,
    simulationLevel VARCHAR(50) NOT NULL,
    PRIMARY KEY(id)
);
CREATE TABLE level_variables(
    id INT NOT NULL AUTO_INCREMENT,
    levelId INT,
    servicePointName VARCHAR(50) NOT NULL,
    eventInterval DECIMAL(4,2) NOT NULL,
    leadTime DECIMAL(4,2) NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (levelId) REFERENCES results(id)
);
DROP USER IF EXISTS 'appuser'@'localhost';
CREATE USER 'appuser'@'localhost' IDENTIFIED BY '123';
GRANT SELECT, INSERT, DROP, UPDATE, DELETE, CREATE ON simulation.* TO 'appuser'@'localhost';