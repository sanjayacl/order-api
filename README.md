# We need to create the postgres database called '101digitalorders'

# build using below command
   mvn clean package spring-boot:repackage

# then change directory to target and run the application as below by passing the db crendtials

   java -jar order-api-0.0.1-SNAPSHOT.jar --spring.datasource.username=postgres --spring.datasource.password=admin
