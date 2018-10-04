CREATE TABLE u0934995.breed
(
  id INT AUTO_INCREMENT,
  name VARCHAR(50),
  alt_name VARCHAR(50),
  size ENUM ('TOY', 'SMALL', 'MID', 'LARGE', 'XLARGE'),
  PRIMARY KEY(id),
  UNIQUE(name)
);