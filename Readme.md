# Spring Boot Quartz Scheduler Example

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

## Scheduling an Email using the /scheduleEmail API

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
