--run as sbrext
update sbr.user_accounts_view set enabled_ind = 'No' where ua_name like 'DEVELOPER%'
/