create sequence category_seq start with 1 increment by 50;
create sequence product_seq start with 1 increment by 50;
create table category (creation_date date, creation_time time(6), last_modified_date date, last_modified_time time(6), id bigint not null, creation_username varchar(255), last_modified_username varchar(255), name varchar(255), primary key (id));
create table category_products (categories_id bigint not null, products_id bigint not null, primary key (categories_id, products_id));
create table product (amount_units smallint check (amount_units between 0 and 7), creation_date date, creation_time time(6), last_modified_date date, last_modified_time time(6), price float(53), id bigint not null, quantity bigint, creation_username varchar(255), last_modified_username varchar(255), name varchar(255), primary key (id));
alter table if exists category_products add constraint FKe9irm5a62pmolhvr468cip3v3 foreign key (products_id) references product;
alter table if exists category_products add constraint FKt6hhwypmhqewuadwp2yin9i7e foreign key (categories_id) references category;