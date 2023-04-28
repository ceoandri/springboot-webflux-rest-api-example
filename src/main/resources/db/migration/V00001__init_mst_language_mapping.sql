create table mst_language_mapping (
	mapping		varchar(50) primary key	not null,
	id			varchar					not null,
	en			varchar					not null
);

insert into mst_language_mapping(mapping, id, en) values ('GREETINGS', 'Halo', 'Hello');
insert into mst_language_mapping(mapping, id, en) values ('GOOD_BYE', 'Selamat tinggal', 'Good bye');
insert into mst_language_mapping(mapping, id, en) values ('SEE_YOU', 'Sampai jumpa', 'See you');
