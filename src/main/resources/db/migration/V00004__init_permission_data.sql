create table public.mst_role (
	role_name	varchar	 primary key		not null,
	role_desc	varchar						not null
);

insert into public.mst_role (role_name, role_desc) values ('SUPER_ADMIN', 'Super Admin');
insert into public.mst_role (role_name, role_desc) values ('ADMIN', 'Admin');
insert into public.mst_role (role_name, role_desc) values ('GUEST', 'Guest');

create table public.mst_module (
	module_name	varchar	 primary key		not null,
	module_desc	varchar						not null
);

insert into public.mst_module (module_name, module_desc) values ('MST_LANGUAGE_MAPPING', 'Mst Language Mapping');

create table public.mst_access_type (
	access_type_name	varchar	 primary key		not null,
	access_type_desc	varchar						not null
);

insert into public.mst_access_type (access_type_name, access_type_desc) values ('RETRIEVE', 'Retrieve');
insert into public.mst_access_type (access_type_name, access_type_desc) values ('CREATE', 'Create');
insert into public.mst_access_type (access_type_name, access_type_desc) values ('UPDATE', 'Update');
insert into public.mst_access_type (access_type_name, access_type_desc) values ('DELETE', 'Delete');

create table public.permission (
	id					serial primary key			not null,
	role_name			varchar				 		not null,
	module_name			varchar						not null,
	access_type_name	varchar						not null
);

insert into public.permission (role_name, module_name, access_type_name) values ('SUPER_ADMIN', 'MST_LANGUAGE_MAPPING', 'RETRIEVE');
insert into public.permission (role_name, module_name, access_type_name) values ('SUPER_ADMIN', 'MST_LANGUAGE_MAPPING', 'CREATE');
insert into public.permission (role_name, module_name, access_type_name) values ('SUPER_ADMIN', 'MST_LANGUAGE_MAPPING', 'UPDATE');
insert into public.permission (role_name, module_name, access_type_name) values ('SUPER_ADMIN', 'MST_LANGUAGE_MAPPING', 'DELETE');
insert into public.permission (role_name, module_name, access_type_name) values ('ADMIN', 'MST_LANGUAGE_MAPPING', 'RETRIEVE');
insert into public.permission (role_name, module_name, access_type_name) values ('ADMIN', 'MST_LANGUAGE_MAPPING', 'CREATE');
insert into public.permission (role_name, module_name, access_type_name) values ('ADMIN', 'MST_LANGUAGE_MAPPING', 'UPDATE');
insert into public.permission (role_name, module_name, access_type_name) values ('GUEST', 'MST_LANGUAGE_MAPPING', 'RETRIEVE');

ALTER TABLE "permission" ADD FOREIGN KEY ("role_name") REFERENCES "mst_role" ("role_name");
ALTER TABLE "permission" ADD FOREIGN KEY ("module_name") REFERENCES "mst_module" ("module_name");
ALTER TABLE "permission" ADD FOREIGN KEY ("access_type_name") REFERENCES "mst_access_type" ("access_type_name");
