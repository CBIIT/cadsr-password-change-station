--run as sbrext

update sbrext.tool_options_view_ext set value = 'Your password for the caDSR account ${userid} is about to expire on ${expiryDate}.  To change your password, you can either login to the Password Change Station by visiting https://${webHost}, contact the NCI Helpdesk at ncicb@pop.nci.nih.gov or toll free phone number: 888-478-4423.'
where Tool_name = 'PasswordChangeStation' and Property = 'EMAIL.INTRO'
/
commit
/
