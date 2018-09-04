create table DATABASENAME.pupper
(
	id int auto_increment
		primary key,
	name varchar(20) null,
	gender bit null,
	birth_month tinyint null,
	birth_year int null,
	stage enum('PUPPY', 'YOUNG', 'ADULT', 'MATURE') null,
	is_neutered bit null,
	match_profile_id int null,
	breed int null,
	size enum('TOY', 'SMALL', 'MID', 'LARGE', 'XLARGE') null,
	energy enum('MINIMAL', 'LOW', 'MODERATE', 'HIGH', 'EXTREME') null,
	owner_id int null,
	constraint pupper_breed_id_fk
		foreign key (breed) references DATABASENAME.breed (id)
			on update cascade on delete set null,
	constraint pupper_matching_profile_id_fk
		foreign key (match_profile_id) references DATABASENAME.matching_profile (id)
			on update cascade on delete set null,
	constraint pupper_owner_user_id_fk
		foreign key (owner_id) references DATABASENAME.user (profile_id)
)
;

