CREATE TABLE u0934995.user_profile
(
  id INT AUTO_INCREMENT,
  account_id_fk INT,
  name_first VARCHAR(20),
  name_last VARCHAR(20),
  sex CHAR(1),
  birthdate DATE,
  zip CHAR(5),
  date_join DATE,
  last_login DATE,
  PRIMARY KEY(id),
  UNIQUE(user_profile_id),
  FOREIGN KEY (account_id_fk) REFERENCES u0934995.UserCredentials(id)
);