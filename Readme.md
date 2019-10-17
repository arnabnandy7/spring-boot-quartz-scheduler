# Spring Boot Quartz Scheduler Example

I forked this repository which was using REST call to schedule email.  I did not need to send email. Instead I needed to trigger an event ( logger ).  

I have also extended the project with CRON scheduler, which I needed for my current work project.

## Requirements

1. Java - 1.8.x, Java 11

2. Maven - 3.x.x

3. MySQL - 5.x.x

## Steps to Setup

**1. Clone the application**



**2. Create MySQL database**

```bash
create database quartz_demo
```

**3. Change MySQL username and password as per your MySQL installation**

open `src/main/resources/application.properties`, and change `spring.datasource.username` and `spring.datasource.password` properties as per your mysql installation

Make sure you set port correctly.  I have different from default as my machine had both installed MySQL and Docker version

**4. Create Quartz Tables**

The project stores all the scheduled Jobs in MySQL database. You'll need to create the tables that Quartz uses to store Jobs and other job-related data. Please create Quartz specific tables by executing the `quartz_tables.sql` script located inside `src/main/resources` directory.



```bash
mysql> source <PATH_TO_QUARTZ_TABLES.sql>
```

**6. Build and run the app using maven**

Finally, You can run the app by typing the following command from the root directory of the project -

```bash
mvn spring-boot:run
```
Application contains build in cron job which logs the even every 15 seconds.

## Scheduling an Event using the /scheduleJob API

```bash
curl -X POST \
  http://localhost:8081/scheduleJob \
  -H 'Accept: */*' \
  -H 'Content-Type: application/json' \
  -H 'Cookie: JSESSIONID=32A02F6E2D185C8702E7BA6016A913E3' \
  -H 'Host: localhost:8081' \
  -d '{
	"dateTime": "2019-10-02T15:14:00",
	"timeZone": "America/Los_Angeles"
}'

# Output
{
    "success": true,
    "jobId": "38aace30-ccf8-4c25-bca8-25f0f58b3c2a",
    "jobGroup": "rest-jobs",
    "message": "Scheduled Successfully!"
}
```

**7. Running SpringBoot Quartz with MSSQL Server**

Run MSSQL as docker: 
```docker run -e 'ACCEPT_EULA=Y' -e 'SA_PASSWORD=2019October$$$' -p 1433:1433 -d mcr.microsoft.com/mssql/server:2017-latest```

Create database `quartz_schema` in MSSQL server

Run DDL script tables_sqlServer.sql provided with quartz jar file.

Add dependency to gradle ( or maven ) to import the driver:
```compile group: 'com.microsoft.sqlserver', name: 'mssql-jdbc', version: '8.1.0.jre11-preview'```

Change application.yml to connect to database:
```spring:
  datasource:
    password: 2019October$$$
    url: jdbc:sqlserver://localhost:1433;database=quartz_schema;SelectMethod=cursor
    username: sa
  jpa:
    hibernate:
      ddl-auto: update
    properties:
      hibernate:
        dialect: org.hibernate.dialect.SQLServer2008Dialect
  quartz:
    jdbc:
      schema: classpath:org/quartz/impl/jdbcjobstore/tables_sqlServer.sql
    job-store-type: jdbc
    properties:
      org:
        quartz:
          jobStore:
            isClustered: true
          scheduler:
            instanceId: AUTO
          threadPool:
            threadCount: 5

org.quartz.jobStore.driverDelegateClass: org.quartz.impl.jdbcjobstore.MSSQLDelegate```
