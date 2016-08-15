## Air Ticket Reservation System

This project is an assignment that I made as one of the steps required to be hired by a company as a Java Chief Architect.
Unfortunately I didn't have time to complete the entire project and I wasn't hired, but I realized that it would be nice to try to complete it (when I have time...) and share it over the Internet. In the docs folder you will find the requirements and the Visio files for the diagrams.

This file contains the following information:

1. Instructions to install and configure prerequisites or dependencies
2. Instructions to create and initialize the database
3. Assumptions made
4. Requirements that you have not covered in this implementation
5. Instructions to configure and prepare the source code to build and run properly

### 1. Instructions to install and configure prerequisites or dependencies 

#### Install Java EE application server Wildfly

I used Wildfly *Version* `10.1.0.CR1` *Date* `2016-07-28` because it has already included newest version of *Jackson* library that has support for serialization and deserialization of JDK8 new date and time types (see [[WFLY-5916]](https://issues.jboss.org/browse/WFLY-5916) - jackson-datatype-jsr310 and jackson-datatype-jdk8 ).

Download link at 
[http://download.jboss.org/wildfly/10.1.0.CR1/wildfly-10.1.0.CR1.tar.gz]()

Simply extract your chosen download to the directory of your choice, you can add this path to an OS environment variable, let's call it `$JBOSS_HOME` (Wildfly is the OpenSource community version of JBoss Enterprise Application Server).

More information at
[https://docs.jboss.org/author/display/WFLY10/Getting+Started+Guide]()

#### Add an admin user to Wildfly

```sh
% cd $JBOSS_HOME/bin
% ./add-user.sh

What type of user do you wish to add?
 a) Management User (mgmt-users.properties)
 b) Application User (application-users.properties)
(a): a
... 
Username : admin
Password :
What groups do you want this user to belong to? (Please enter a comma separated list, or leave blank for none)[  ]:
...
Is this new user going to be used for one AS process to connect to another AS process?
e.g. for a slave host controller connecting to the master or for a Remoting connection for server to server EJB calls.
yes/no? no

```

#### Install and Run Postgresql 9.4 with Vagrant

You can use the following command to run a Vagrant machine with Postgresql already installed and configured for use:

```
cd /project/directory/sql
vagrant up
```

Follow the instruction on screen to access to the machine and connect to Postgresql

#### Add Postgresql Drivers to Wildfly 10

1) Go to `/tmp` and get the JDBC driver from [http://jdbc.postgresql.org]():

```sh
cd /tmp
wget –tries=0 –continue https://jdbc.postgresql.org/download/postgresql-9.4.1207.jar
```

2) Run Wildfly, change directory to $JBOSS_HOME/bin.

```sh
cd $JBOSS_HOME/bin
./standalone.sh
```

3) Load the jboss cli and deploy the jar from the command line:

```sh
cd $JBOSS_HOME/bin
./jboss-cli.sh
[disconnected /] connect
[standalone@localhost:9990 /] deploy /tmp/postgresql-9.4.1207.jar
[standalone@localhost:9990 /] exit
```

4) Configure the connection with jboss console (select the driver installed

-	load the console at [http://localhost:9990/console]()
- 	Go to Configuration->Subsystems->Datasources->Non-XA->Add
-  Step 1/3: Datasource Attributes
	-  JNDI name, example: `java:jboss/datasources/PostgresDataSource`
-  Step 2/3: JDBC Driver
	- Choose Detected Driver tab and then `postgresql-9.4.1207.jar`
-	Step 3/3: Connection Settings
	-  Connection URL: `jdbc:postgresql://localhost:15432/database?ApplicationName=airticket`
	-  Username: `database`
	-  Password: `qwerty123.1`

### 2. Restore DB

```sh
cd sql
vagrant ssh
cd /mnt/bootstrap
sudo su postgres -c "psql -f airticket-start.sql"
```

### 3. Assumptions made 

I have assumed that this is an airline company enterprise product and not a search engine for best flight rates. I thought that the technology behind it should be standard and stable over time. I wanted to ensure the necessary productivity of the team without sacrificing the UX/UI. For those reasons, Java EE 7 platform has been chosen and JSF and/or HTML5/CSS3/Javascript can be used to design UI/UX. Rest services are developed for every business function present into the backend modules.

### 4. Requirements that you have not covered in your implementation

TODOs:

- protect rest services with JWT (Javascript Web Token)
- framework classes for Google OAuth 2.0
- framework classes for PDF generation
- security with LDAP or Database (even if, as stated by the Design Document, backoffice security is implemented via web.xml and Wildfly configuration)
- credit card classes

### 5. Instructions to configure and prepare the source code to build and run properly

#### Unit Tests

Go to the airticket project and type

mvn test

#### Create WAR

mvn clean package

after created the package, take note of the directory of the generated war:  
it should be in a directory like the following: 

<unzipped project dir>/Crossover_AirTicketReservation/airticket/target/airticket.war

and copy it into the following dir:

wildfly-10.1.0.CR1/standalone/deployments

#### System Tests

After that the WAR is correctly deployed, you can execute the integration tests.

Go to the folder 

airticket-st

and type the following:

mvn test

** PLEASE DO NOT CARE ABOUT THE LOG EXCPETION: javax.persistence.OptimisticLockException: Row was updated or deleted by another transaction  **

because it is managed in the code and it is not an error (see the crud test case)

#### Run Web Application

go to:

http://localhost:8080/airticket/

