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
    levelId INT,
    servicePointName VARCHAR(20) NOT NULL,
    eventInterval DECIMAL(4,2) NOT NULL,
    leadTime DECIMAL(4,2) NOT NULL,
    PRIMARY KEY (servicePointName),
    FOREIGN KEY (levelId) REFERENCES results(id)
);
DROP USER IF EXISTS 'appuser'@'localhost';
CREATE USER 'appuser'@'localhost' IDENTIFIED BY '123';
GRANT SELECT, INSERT ON simulation.results TO 'appuser'@'localhost';
GRANT SELECT, INSERT ON simulation.level_variables TO 'appuser'@'localhost';