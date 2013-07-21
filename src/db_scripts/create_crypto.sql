/*L
  Copyright SAIC-F Inc.

  Distributed under the OSI-approved BSD 3-Clause License.
  See http://ncip.github.com/cadsr-password-change/LICENSE.txt for details.
L*/

--PLEASE run this in SBREXT schema!!!

CREATE OR REPLACE FUNCTION encrypt(key_string IN varchar2, input_string IN char)
RETURN varchar2
AS
    encrypted_string              VARCHAR2 ( 2048 );
BEGIN
   DBMS_OUTPUT.PUT_LINE ( 'Input String :' || input_string );
   Dbms_Obfuscation_Toolkit.DES3Encrypt ( input_string => input_string
                                        ,key_string => key_string
                                        ,encrypted_string => encrypted_string
                                       );
   DBMS_OUTPUT.PUT_LINE ( 'Encrypted string : ' || encrypted_string );
   DBMS_OUTPUT.PUT_LINE ( '' );

   RETURN encrypted_string;
END;
/

CREATE OR REPLACE FUNCTION decrypt(key_string IN varchar2, input_string IN char)
   RETURN varchar2
AS
   decrypted_string              VARCHAR2 ( 2048 );
BEGIN
   DBMS_OUTPUT.PUT_LINE ( 'Input String :' || input_string );
   Dbms_Obfuscation_Toolkit.DES3Decrypt ( input_string => input_string
                                        ,key_string => key_string
                                        ,decrypted_string => decrypted_string
                                       );
   DBMS_OUTPUT.PUT_LINE ( 'Decrypted output : ' || decrypted_string );
   DBMS_OUTPUT.PUT_LINE ( '' );

   RETURN decrypted_string;
END;
/
