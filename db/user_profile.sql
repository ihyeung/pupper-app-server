CREATE TABLE u0934995.user_profile
(
 id INT AUTO_INCREMENT,
 account_id INT,
 name_first VARCHAR(20),
 name_last VARCHAR(20),
 sex CHAR(1),
 birthdate DATE,
 zip CHAR(5),
 date_join DATE,
 last_login DATE,
 -- user_profile_id INT,
PRIMARY KEY(id),
-- # UNIQUE(account_id), //add this back later, it makes it too troublesome to test with this constraint
-- # UNIQUE(user_profile_id), //id for table containing other user data
FOREIGN KEY (account_id) REFERENCES u0934995.UserCredentials(id)
-- FOREIGN KEY (user_profile_id) REFERENCES u0934995.UserProfile(id)
);

