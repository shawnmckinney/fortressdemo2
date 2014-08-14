This is free and unencumbered software released into the public domain.
___________________________________________________________________________________
###################################################################################
README for Fortress Demo #2 RBAC Security Sample
Last updated: August 14, 2014
___________________________________________________________________________________
###################################################################################
# Prerequisites
###################################################################################
1. Internet access to retrieve dependencies from online Maven repo.
2. Java SDK Version 7 (or greater), Apache Ant 1.8 (or greater) and Apache Maven installed to target environment.
3. Firefox browser installed for automated unit testing with Apache Solenium.
4. All other packages will be covered in the help document.

###################################################################################
# Instructions for downloading application and generating the installation document
###################################################################################
1. Download package: https://github.com/shawnmckinney/fortressdemo2/archive/master.zip
2. Extract
3. Set JAVA_HOME
4. Set M2_HOME
5. Set ANT_HOME
6. Run this command from the root package:
$ $M2_HOME/bin/mvn javadoc:javadoc
7. Open the help document: package home / target/site/apidocs/overview-summary.html
8. Follow the steps under 'Sections required for installation'