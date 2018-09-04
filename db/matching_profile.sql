create table DATABASENAME.matching_profile
(
	id int auto_increment
		primary key,
	user_id int null,
	pup_id1 int null,
	pup_id2 int null,
	score float null,
	image varchar(50) null,
	bio varchar(100) null,
	constraint matching_profile_pupper_id_fk
		foreign key (pup_id1) references DATABASENAME.pupper (id)
			on update cascade on delete cascade,
	constraint matching_profile_pupper_id_fk_2
		foreign key (pup_id2) references DATABASENAME.pupper (id)
			on update cascade on delete cascade,
	constraint matching_profile_user_profile_id_fk
		foreign key (id) references DATABASENAME.user (profile_id)
)
;

