--run as sbrext

CREATE TABLE PASSWORD_NOTIFICATION (
    ua_name varchar2(30) NOT NULL,
    date_modified date NOT NULL,
    attempted_count number(19),
    processing_type varchar2(30),
    delivery_status varchar2(30),
    CONSTRAINT pk PRIMARY KEY (ua_name)
)
/
grant select, insert, update, delete on PASSWORD_NOTIFICATION to cadsrpasswordchange
/