# Simple project to run sling pipes 

Just a simple project to execute a sling pipe from servlet.

## Modules

The main parts of the template are:

* core: Java bundle containing the servlet
* ui.apps: contains the /apps &/etc  parts of the project

## How to build


If you have a running AEM instance you can build and package the whole project and deploy into AEM with  

    mvn clean install -Pdeploy
    
