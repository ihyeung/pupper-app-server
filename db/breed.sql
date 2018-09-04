create table DATABASENAME.breed
(
	id int auto_increment
		primary key,
	breed varchar(50) null,
	size enum('toy', 'small', 'mid', 'large', 'xlarge') null,
	constraint breed_breed_uindex
		unique (breed)
)
;

