/*L
  Copyright SAIC-F Inc.

  Distributed under the OSI-approved BSD 3-Clause License.
  See http://ncip.github.com/cadsr-password-change/LICENSE.txt for details.
L*/

-- run as sys

CREATE OR REPLACE FUNCTION password_change_prevent (username_in VARCHAR2
                                        , password VARCHAR2
                                        , old_password varchar2)
    RETURN boolean
    IS

    BEGIN

        if (UPPER(username_in) in ('CADSRPASSWORDCHANGE', 'CDEBROWSER', 'CDECURATE', 'FORMBUILDER', 'FREESTYLESEARCH', 'SENTINEL',
                   'SBR', 'SBREXT', 'CADSRADMIN', 'CADSR_API', 'UMLLDR', 'THE_SIW', 'BLKLDR', 'GUEST', 'SITESCOPE')) then
            raise_application_error(-20010, 'Account is a special account, password cannot be changed.');
        end if;


        RETURN(true);

    END;
/