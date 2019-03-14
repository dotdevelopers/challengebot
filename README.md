# CHALLENGEBOT

running this requires several Environment variables:

|environment variable|expected value|
|-|-|
|chbot_token|the discord bot token|
|chbot_SQL_name|the username to log into the SQL server with|
|chbot_SQL_pass|the corresponding password|
|chbot_SQL_host|the host the SQL server is running on (usually `localhost`)|
|chbot_SQL_port|the port the SQL server is listening on|
|chbot_SQL_db|the database to use for storing submissions, ratings, etc|
|chbot_mysql|defines wether mysql is used instead of mariadb. `true`, `t`, `y` or `yes` to indicate a truthy value, everything else is falsey.|

The expected database setup is:
```sql
CREATE TABLE pendingsubmissions (  Username varchar(255) NOT NULL,  Submitted TIMESTAMP,  Text varchar(2047),  UNIQUE KEY unique_UN (Username) ) ENGINE=InnoDB DEFAULT CHARSET=utf8;
```