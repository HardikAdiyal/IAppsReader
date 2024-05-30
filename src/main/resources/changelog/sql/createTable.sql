-- liquibase formatted sql
-- changeset hardik:1 runOnChange:true

create table epaperinfo(
	id SERIAL PRIMARY KEY,
	paper_name varchar(40) not null,
	version BIGINT not null,
	width BIGINT not null,
	height BIGINT not null,
	dpi BIGINT not null,
	uploaded_date timestamp not null,
	updated_date timestamp,
	file_name varchar(50) not null
)