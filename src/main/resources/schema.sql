DROP TABLE IF EXISTS widgets;
 
CREATE TABLE widgets (
  id VARCHAR(250)  PRIMARY KEY,
  xCoordinate INT NOT NULL,
  yCoordinate INT NOT NULL,
  zIndex INT NOT NULL,
  width INT NOT NULL,
  height INT NOT NULL,
  modifiedAt BIGINT NOT NULL
   
);
