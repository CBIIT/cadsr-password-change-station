set PATH=C:\instantclient_10_2;%PATH%

:sqlplus TANJ/TANJ@(description=(address_list=(address=(protocol=TCP)(host=137.187.181.4)(port=1551)))(connect_data=(service_name=DSRDEV)))

sqlplus TANJ/TANJ@dsrdev

pause