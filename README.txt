This is free and unencumbered software released into the public domain.
___________________________________________________________________________________
###################################################################################
README for Wicket, Java EE, Spring and Fortress RBAC Security Sample
Last updated: July 30, 2014
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
9. Use the credentials defined in fortressdemo2-load-policy.xml (from step installing Fortress step 3):
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

Working on this...
