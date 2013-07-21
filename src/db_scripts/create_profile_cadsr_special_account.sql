/*L
  Copyright SAIC-F Inc.

  Distributed under the OSI-approved BSD 3-Clause License.
  See http://ncip.github.com/cadsr-password-change/LICENSE.txt for details.
L*/

-- run as sbrext

create profile "cadsr_special_account" limit
 password_life_time UNLIMITED
 password_verify_function password_change_prevent
/