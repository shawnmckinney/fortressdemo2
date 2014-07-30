This is free and unencumbered software released into the public domain.
___________________________________________________________________________________
###################################################################################
README for Wicket, Java EE, Spring and Fortress RBAC Security Sample
Last updated: April 29, 2014
___________________________________________________________________________________
###################################################################################
# Prerequisites
###################################################################################
1. Internet access to retrieve dependencies from online Maven repo.
2. Java SDK Version 7 or beyond installed to target environment
3. Fortress, Sentry, Apache Ant, Maven, Tomcat and Sentry installed (instructions follow)
___________________________________________________________________________________
###################################################################################
# Section 0: Instructions for installing Fortress and configuring Tomcat to use it for container security
###################################################################################
Note: This demo uses Apache Tomcat, Maven, Ant, OpenLDAP and Fortress Realm to download, build and configure
target environment.  All of these dependencies (and more) will be installed to your machine after successful completion
of this section.

1. Download one of the Fortress Quickstart packages from: http://iamfortress.org/download
Fortress Quickstart packages includes everything you need but the Java JVM.

2. Follow these steps: http://www.jts.us/iamfortress/guides/README-QUICKSTART.html:
http://www.jts.us/iamfortress/guides/README-QUICKSTART.html

*** Note: This guide also in root of Fortress Quickstart download package.

Perform Steps I, II, III and V (only)
I. Instructions to extract and configure Fortress Builder Package to Target System
II. Instructions to run the Fortress Ant Build
III. Instructions to run the Builder to Install Symas OpenLDAP, configure and load with seed data
V. Instructions to install Commander Web UI Demo

***
Note: After completing Step V above Fortress' Web administration UI (Commander) will be deployed to Tomcat.
To use Admin UI:  http://localhost:8080/commander
at the login prompt enter: test/test
***

___________________________________________________________________________________
###################################################################################
# Section 1: Instructions for building and running this sample application
###################################################################################
1. Download package: https://github.com/shawnmckinney/fortressdemo2/archive/master.zip
2. Extract
3. Set JAVA_HOME
4. Set M2_Home
5. Set ANT_HOME
6. Run this command from the root package:
$ mvn install
7. Run this command from the root package:
$ mvn tomcat:deploy
8. Go to this URL:
http://localhost:8080/fortressdemo2
9. Use the credentials defined in demo-fortressproject-users.xml (from step installing Fortress step 3):
user1/password
user2/password
user3/password

Note: If you make changes and want to redeploy use this command:
$ mvn tomcat:redeploy
___________________________________________________________________________________
###################################################################################
# Section 2: Instructions for running automatic Selenium Tests
###################################################################################
1. Run Selenium Web driver test
>mvn verify -DskipTests=false
___________________________________________________________________________________
###################################################################################
# Section 3: Sample RBAC Policy with Fortress
###################################################################################
Dynamic Separation of Duty constraints restrict one role from set (ROLE_TEST1, ROLE_TEST2, and ROLE_TEST3) that may be activated at a time.
- user1 is assigned ROLE_TEST1, ROLE_TEST2, ROLE_TEST3 & ROLE_TEST_SUPER
- user2 is assigned ROLE_TEST2 & ROLE_TEST_USER
- user3 is assigned ROLE_TEST3 & ROLE_TEST_USER
- user1 default role activation is ROLE_TEST1 & ROLE_TEST_SUPER
- user1 can view all pages (granted by ROLE_TEST_SUPER) but can only view buttons corresponding with its activated role ( ROLE_TEST1, ROLE_TEST2 or ROLE_TEST3 )
- user1 can activate one and only one role from set ( ROLE_TEST1, ROLE_TEST2 or ROLE_TEST3 )
- user2 can only view Page2 buttons
- user3 can only view Page3 buttons

Rationale for policy:
Consider an example of three bank tellers (teller1, teller2, teller3) and three bank branches (branch1, branch, branch3).
One teller (teller1) may work in any of the three branches (branch1, branch2, branch3) but can only be active in one branch at a time.
The other two Tellers (teller2, teller3) are restricted to branch2 and branch3 respectively.
RBAC provides a means in which to enforce this policy following the principle of least privilege.