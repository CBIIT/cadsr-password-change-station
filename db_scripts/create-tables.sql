CREATE TABLE securityQnA (
	id number(19) NOT NULL,
	username varchar2(30) NOT NULL,
	password varchar2(30) NOT NULL,
	question varchar2(500) NOT NULL,
	answer varchar2(500) NOT NULL,
	updated_date date NOT NULL
);

ALTER TABLE securityQnA ADD CONSTRAINT pk_securityQnA PRIMARY KEY (
	id
);

CREATE UNIQUE INDEX inx_qna ON securityQnA (
	question
);

CREATE SEQUENCE seq_securityQnA;