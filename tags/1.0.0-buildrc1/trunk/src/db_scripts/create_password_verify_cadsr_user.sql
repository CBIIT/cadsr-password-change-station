-- run as sys
-- note hardcoded assumptions and password requirements

CREATE OR REPLACE FUNCTION password_verify_casdr_user (username_in VARCHAR2
                                        , password VARCHAR2
                                        , old_password varchar2)
    RETURN boolean
    IS
    
        passwordMinLength   INTEGER;
	charTypeCount       INTEGER;
        upperArray          VARCHAR2(26);
        lowerArray          VARCHAR2(26);
        specialArray        VARCHAR2(50);
        digitArray          VARCHAR2(10);
	passwordLength      INTEGER;
        hasUpper            BOOLEAN;
        hasLower            BOOLEAN;
        hasSpecial          BOOLEAN;
        hasDigit            BOOLEAN;
        currentChar         CHAR;
        daysUntilExpiration NUMERIC(7,2);
        daysSinceLastChangeMin  NUMERIC(7,2);
        passwordLifetime    NUMERIC(7,2);
        username_copy       VARCHAR2(100);

    BEGIN

        -- hard-coded assumptions
        passwordLifetime   := 60;

        -- hard-coded password requirements
        passwordMinLength  := 8;
        upperArray         := 'ABCDEFGHIJKLMNOPQRSTUVWXYZ';
        lowerArray         := 'abcdefghijklmnopqrstuvwxyz';
        digitArray         := '0123456789';
        specialArray         := '_$#';
        daysSinceLastChangeMin := 1.0;

        passwordLength     := LENGTH(password);
        hasUpper           := FALSE;
        hasLower           := FALSE;
        hasSpecial         := FALSE;
        hasDigit           := FALSE;
        charTypeCount      := 0;

        -- check minimum password length

        IF (passwordLength < passwordMinLength) THEN
            raise_application_error(-20000, 'Password must be at least ' || passwordMinLength || ' characters.');
        END IF;

        -- check for types of characters in new password

        FOR i IN 1..passwordLength LOOP
            currentChar := SUBSTR(password,i,1);

	    IF INSTR(upperArray,currentChar) > 0 THEN
                hasUpper := TRUE;
                GOTO foundCharacter;
            END IF;

	    IF INSTR(lowerArray,currentChar) > 0 THEN
                hasLower := TRUE;
                GOTO foundCharacter;
            END IF;

	    IF INSTR(specialArray,currentChar) > 0 THEN
                hasSpecial := TRUE;
                GOTO foundCharacter;
            END IF;

	    IF INSTR(digitArray,currentChar) > 0 THEN
                hasDigit := TRUE;
                GOTO foundCharacter;
            END IF;

            raise_application_error(-20001, 'Password may only contain these characters ' || upperArray ||lowerArray || specialArray || digitArray);

            <<foundCharacter>>
            NULL;

        END LOOP;

	IF hasUpper = TRUE THEN
            charTypeCount:= charTypeCount + 1;
	END IF;

	IF hasLower = TRUE THEN
            charTypeCount:= charTypeCount + 1;
	END IF;

	IF hasSpecial = TRUE THEN
            charTypeCount:= charTypeCount + 1;
	END IF;

	IF hasDigit = TRUE THEN
            charTypeCount:= charTypeCount + 1;
	END IF;

        IF charTypeCount < 3 THEN
            raise_application_error(-20002, 'Password must contain characters from at least three of these groups: capital letters, lower case letters, numerics, and specials ->  ' || specialArray);
        END IF;


        -- check for minimum password age

        -- note: Expiring a password to force change on first use will set the expiry_date to the expired date.
        --       Therefore, the case where users change their password on the day their account is created
        --       can be handled the same as other situations

        -- note: Assigning a profile with password_life_time sets the expiry_date.
        --       We don't have to worry about null expiry_date.
        --       (And assigning a profile does not affect LOCKED or EXPIRED status.)

        SELECT (u.expiry_date - sysdate) into daysUntilExpiration from dba_users u where u.username = username_in;
        IF (passwordLifetime - daysUntilExpiration) < daysSinceLastChangeMin THEN
                raise_application_error(-20003, 'Password may not be changed less than ' || daysSinceLastChangeMin || ' day(s) from last password change date.' );
        END IF;


        -- check that password starts with a letter
        currentChar := SUBSTR(password,1,1);
        IF (INSTR(upperArray,currentChar) = 0) AND (INSTR(lowerArray,currentChar) = 0) THEN
                raise_application_error(-20004, 'Password must start with a letter.' );
        END IF;


        RETURN(true);

    END;
/