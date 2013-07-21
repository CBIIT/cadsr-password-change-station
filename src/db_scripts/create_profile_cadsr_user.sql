/*L
  Copyright SAIC-F Inc.

  Distributed under the OSI-approved BSD 3-Clause License.
  See http://ncip.github.com/cadsr-password-change/LICENSE.txt for details.
L*/

-- run as sbrext

create profile "cadsr_user" limit
 password_life_time 60
 password_grace_time 0
 password_reuse_max 24
 password_reuse_time 1
 failed_login_attempts 6
 password_lock_time 60/1440
 password_verify_function password_verify_casdr_user
/