-- run as sbrext

create profile "cadsr_user" limit
 password_life_time 60
 password_grace_time 0
 password_reuse_max 24
 password_reuse_time UNLIMITED
 failed_login_attempts 6
 password_lock_time 60/1440
 password_verify_function password_verify_casdr_user
/