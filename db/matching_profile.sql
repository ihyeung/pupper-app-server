CREATE TABLE u0934995.match_profile
(
  id INT AUTO_INCREMENT,
  user_id_fk INT,
  pup_id_fk INT,
  score FLOAT DEFAULT 100,
  last_login DATE,
  about VARCHAR(130),
  PRIMARY KEY (id),
  FOREIGN KEY (user_id_fk) REFERENCES user_profile(id),
  FOREIGN KEY (pup_id_fk) REFERENCES pupper(id)
);

