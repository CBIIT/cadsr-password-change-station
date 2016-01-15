--Execute the following 2 update statements in the SBREXT schema.

update sbrext.tool_options_view_ext set value='Your password for the caDSR account ${userid} is about to expire on ${expiryDate}.  To change your password, you can either login to the Password Change Station by visiting https://${webHost} or contact the NCI Helpdesk at ncicbiit@mail.nih.gov.' where Tool_name = 'PasswordChangeStation' and Property = 'EMAIL.INTRO';

update sbrext.tool_options_view_ext set value='http://cbiit.nci.nih.gov/ncip/biomedical-informatics-resources/interoperability-and-semantics/metadata-and-models' where Tool_name = 'PasswordChangeStation' and Property = 'LOGO.ROOT';