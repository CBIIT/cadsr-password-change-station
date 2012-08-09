--run as sbrext

CREATE TABLE PASSWORD_NOTIFICATION (
	ua_name varchar2(30) NOT NULL,
	email_address varchar2(255) NOT NULL,
	email_subject varchar2(255) NOT NULL,
	email_body varchar2(2000) NOT NULL,
	date_modified date NOT NULL,
	attempted_count number(19),
	delivery_status varchar2(30) NOT NULL
);
