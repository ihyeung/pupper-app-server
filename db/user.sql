create table DATABASENAME.user
(
	profile_id int auto_increment
		primary key,
	account_id int null,
	first varchar(20) null,
	last varchar(20) null,
	gender bit null,
	birthdate date null,
	zip int null,
	location varchar(100) null,
	date_join date null,
	date_active date null,
	bio varchar(100) null,
	image varchar(100) null,
	constraint user_account_id_uindex
		unique (account_id),
	constraint user_credential_fk
		foreign key (account_id) references DATABASENAME.user_credential (id)
)
;

