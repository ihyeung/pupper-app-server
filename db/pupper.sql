CREATE TABLE u0934995.pupper
(
  id INT AUTO_INCREMENT,
  user_id_fk INT,
  name VARCHAR(20),
  gender CHAR(1),
  birthdate DATE,
  lifestage ENUM ('PUPPY', 'YOUNG', 'ADULT', 'MATURE'),
  breed_id_fk INT,
  is_neutered BIT,
  energy ENUM ('MIN', 'LOW', 'MED', 'HIGH', 'EXTREME'),
  PRIMARY KEY(id),
  FOREIGN KEY (user_id_fk) REFERENCES user_profile(id),
  FOREIGN KEY (breed_id_fk) REFERENCES breed(id)
);