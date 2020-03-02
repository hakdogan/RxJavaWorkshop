DROP TABLE IF EXISTS speaker;
DROP TABLE IF EXISTS passengers;

CREATE TABLE speaker (
  id INT AUTO_INCREMENT  PRIMARY KEY,
  name VARCHAR(250) NOT NULL,
  title VARCHAR(250) NOT NULL,
  approve BOOLEAN DEFAULT false NOT NULL,
  retracted BOOLEAN DEFAULT false NOT NULL,
  mail VARCHAR(250) NOT NULL,
  updateTime TIMESTAMP NOT NULL
);

CREATE TABLE passengers (
  id INT AUTO_INCREMENT  PRIMARY KEY,
  speaker VARCHAR(250) NOT NULL,
  sunnyWeather BOOLEAN DEFAULT false NOT NULL,
  flightCostLimit LONG NOT NULL,
  hotelCostLimit LONG NOT NULL,
  certainDays VARCHAR(250) NOT NULL
);

INSERT INTO passengers (speaker, sunnyWeather, flightCostLimit, hotelCostLimit, certainDays) VALUES
  ('Hüseyin Akdoğan', true, 2000, 500, '2020-02-10,2020-02-11,2020-02-12,2020-02-13'),
  ('Altuğ Bilgin Altıntaş', true, 2500, 700, '2020-02-12,2020-02-13,2020-02-14,2020-02-15');