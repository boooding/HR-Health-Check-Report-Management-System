#!/bin/bash
# start a mysql db
if [ "$1" = "db" ]
  then
    echo 'Init db'
    docker stop hrk-mysql
    docker run --rm --name hrk-mysql -e MYSQL_ROOT_PASSWORD=hrkhrkhrk3 -e MYSQL_DATABASE=hr_health_check -e MYSQL_USER=czm -e MYSQL_PASSWORD=123 -p 13306:3306 -d mysql:8.0.23
    sleep 16
    docker exec -i hrk-mysql sh -c 'exec mysql -u"$MYSQL_USER" -p"$MYSQL_PASSWORD" -D "$MYSQL_DATABASE"' < dump.sql
  else
    echo 'Use set up db'
fi
java -jar server-*.jar
