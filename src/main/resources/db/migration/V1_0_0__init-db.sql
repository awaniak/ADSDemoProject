create table conference_priority
(
    id int not null
        primary key,
    priority varchar(255) null
);

create table conference_type
(
    id int not null
        primary key,
    type varchar(255) null
);

create table conference
(
    id bigint not null
        primary key,
    conference_date_time datetime(6) null,
    description varchar(255) null,
    title varchar(20) null,
    priority_id int null,
    type_id int null,
    constraint FK5lyywej1h0h5d1kv1vwgk6m98
        foreign key (priority_id) references conference_priority (id),
    constraint FKpeh1bq15oxrpmn15rr31kdh5r
        foreign key (type_id) references conference_type (id)
);
