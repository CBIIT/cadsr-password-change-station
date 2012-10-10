--DO NOT RUN this - this is just for development test!

create user TEST111 identified by TEST111 default tablespace USERS temporary tablespace TEMP account unlock
/
grant create session to TEST111
/
grant alter user to TEST111
/
grant select, insert, update, delete on sbrext.user_security_questions to TEST111
/
grant select on sbr.user_accounts_view to TEST111
/
--asign profile to an existing user
alter user TEST111 profile "cadsr_user"
/
--expire the users password to force the user to change it:
alter user TEST111 password expire
/