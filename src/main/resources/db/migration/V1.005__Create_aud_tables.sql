create sequence revinfo_seq start with 1 increment by 50;
create table category_aud (rev integer not null, revtype smallint, id bigint not null, name varchar(255), primary key (rev, id));
create table category_products_aud (rev integer not null, revtype smallint, categories_id bigint not null, products_id bigint not null, primary key (rev, categories_id, products_id));
create table product_aud (amount_units smallint check (amount_units between 0 and 7), price float(53), rev integer not null, revtype smallint, id bigint not null, quantity bigint, size bigint, brand varchar(255), color varchar(255), description varchar(255), name varchar(255), primary key (rev, id));
create table revinfo (rev integer not null, revtstmp bigint, primary key (rev));
alter table if exists category_aud add constraint FKc9m640crhsib2ws80um6xuk1w foreign key (rev) references revinfo;
alter table if exists category_products_aud add constraint FKdfsjnvgkc4eexhptnbxray0em foreign key (rev) references revinfo;
alter table if exists product_aud add constraint FK9vwllld6jlw5xys1ay911oh1x foreign key (rev) references revinfo;
