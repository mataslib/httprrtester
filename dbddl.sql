-- STRUKTURA

-- Vytvoreni generatoru id pro testy
create
sequence test_id_seq;

-- Vytvoreni tabulky s testy
create table test
(
    id                integer      default nextval('test_id_seq'::regclass) not null
        constraint test_pkey
        primary key,
    test_definition   text,
    name              varchar(255),
    dbuser            varchar(255) default CURRENT_USER                     not null,
    shared_for_read   boolean      default false,
    shared_for_update boolean      default false
);
