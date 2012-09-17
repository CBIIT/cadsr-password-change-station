--run as sbrext

DECLARE
	   username varchar2(30);
	   new_profile varchar2(100) := '"cadsr_user"';
	   assign_profile_command varchar2(1000);
	   
	   do_change BOOLEAN := TRUE;
	   skipped_string varchar2(100) := '   (not executed)';

    cursor c1 is
      select ua_name
      from user_accounts_view uav, dba_users db
	  where uav.ua_name = db.username
          and UPPER(ua_name) not in ('CADSRPASSWORDCHANGE', 'CDEBROWSER', 'CDECURATE', 'FORMBUILDER', 'FREESTYLESEARCH', 'SENTINEL',
                   'SBR', 'SBREXT', 'CADSRADMIN', 'CADSR_API', 'UMLLDR', 'THE_SIW', 'BLKLDR', 'GUEST');

BEGIN

	dbms_output.enable( 1000000 );
		
	IF (do_change) THEN
	 	skipped_string := '';
	END IF;


    FOR user_accounts_view_rec in c1
    LOOP
        username := user_accounts_view_rec.ua_name;
		assign_profile_command := 'alter user '||username||' profile '||new_profile;
		IF (do_change) THEN
		   execute immediate assign_profile_command;
		END IF;
							   
		dbms_output.put_line( assign_profile_command || skipped_string);
    END LOOP;

END;
/