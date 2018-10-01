CREATE TABLE u0934995.UserCredentials
(
  id INT AUTO_INCREMENT,
  email VARCHAR(50),
  password VARCHAR(50),
  PRIMARY KEY(id),
  UNIQUE(email)
);