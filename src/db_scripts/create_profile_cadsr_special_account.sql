-- run as sbrext

create profile "cadsr_special_account" limit
 password_life_time UNLIMITED
 password_verify_function password_change_prevent
/