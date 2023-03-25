

--------------------------------------- TABELAS  --------------------------------------------------------------
CREATE TABLE token_url (
                           pk     SERIAL,
                           barrel int,
                           token1 VARCHAR(512),
                           url     VARCHAR(4096),
                           contador  INTEGER DEFAULT 0,
                           PRIMARY KEY(pk)
);

CREATE TABLE url_url (
                         pk     SERIAL,
                         barrel int,
                         url1 VARCHAR(4096),
                         url2 VARCHAR(4096),
                         PRIMARY KEY(pk)
);

CREATE TABLE url_info (
                          pk     SERIAL,
                          url VARCHAR(4096),
                          titulo VARCHAR(512),
                          citacao VARCHAR(4096),
                          PRIMARY KEY(pk)
);

CREATE TABLE info_client (
                             pk     SERIAL,
                             username VARCHAR(512),
                             pass VARCHAR(512),
                             PRIMARY KEY(pk)
);

CREATE TABLE Queue_url (
                           pk     SERIAL,
                           barrel int,
                           url VARCHAR(4096),
                           executed boolean,
                           PRIMARY KEY(pk)
);


CREATE INDEX ON token_url (token1,barrel);
CREATE INDEX ON url_url (url1);
CREATE INDEX ON url_url (url2);
CREATE INDEX ON url_info (url);
------------------------------------------------------------------------------------------------------------------------


-- Conta Tokens de todos os barrels:

select barrel as conta, count(distinct(token1))
from token_url group by barrel order by count(token1) DESC;


delete from Queue_url;
delete from token_url;
delete from url_url;
delete from url_info;