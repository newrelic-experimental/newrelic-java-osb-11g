[![New Relic Experimental header](https://github.com/newrelic/opensource-website/raw/master/src/images/categories/Experimental.png)](https://opensource.newrelic.com/oss-category/#new-relic-experimental).  
   
# New Relic Java Instrumentation for Oracle Service Bus 11G

Provides Java instrumentation for Oracle Service Bus 11G applications.   

## Installation

To install:

1. Download the latest release jar files.
2. In the New Relic Java directory (the one containing newrelic.jar), create a directory named extensions if it does not already exist.
3. Copy the downloaded jars into the extensions directory.
4. Restart the application.   


## Getting Started
  
Once installed the New Relic Java Agent will start tracing elements in OSB not captured by the Java Agent out of the box.   This includes things like Pipelines and Transports.   Although the list of components is fairly through there still may be components that still need instrumentation.


## Building

**Because Oracle Service Bus is not an open source framework, it is assumed that you have access to a valid OSB instance in order to build the extensons.  You will need to copy jar files from your installation to the lib directory of the extension that you want to build.  Each lib directory contains a holder.txt file that contains the list of necessary jar files from your installation.**  
   

To build the extension jars from source, follow these steps:
### Build single extension
To build a single extension with name *extension*, do the following:
1. Set an environment variable *NEW_RELIC_EXTENSIONS_DIR* and set its value to the directory where you want the jar file built.
2. Check the lib directory for the list of OSB 11g jars to include in order to build.  The list is in holder.txt.
3. Run the command: ./gradlew *extension*:clean *extension*:install
### Build all extensions
To build all extensions, do the following:
1. Set an environment variable *NEW_RELIC_EXTENSIONS_DIR* and set its value to the directory where you want the jar file built.
2. Check the lib directory for the list of OSB 11g jars to include in order to build for each extension.  The list is in holder.txt.
3. Run the command: ./gradlew clean install.  

#### Example - Building OSB-Http-11g extension
1.  Set an environment variable *NEW_RELIC_EXTENSIONS_DIR*
2.  Add the following jar files to the lib directory:  com.bea.core.weblogic.web.api.jar, com.bea.core.weblogic.workmanager.jar, http-transport-wls.jar, oracle.servicebus.configfwk.jar, sb-kernel-api.jar
3.  Run this command:  ./gradlew OSB-Http-11g:clean OSB-Http-11g:install

## Support

New Relic has open-sourced this project. This project is provided AS-IS WITHOUT WARRANTY OR DEDICATED SUPPORT. Issues and contributions should be reported to the project here on GitHub.

>We encourage you to bring your experiences and questions to the [Explorers Hub](https://discuss.newrelic.com) where our community members collaborate on solutions and new ideas.    
>
## Contributing
We encourage your contributions to improve [project name]! Keep in mind when you submit your pull request, you'll need to sign the CLA via the click-through using CLA-Assistant. You only have to sign the CLA one time per project.
If you have any questions, or to execute our corporate CLA, required if your contribution is on behalf of a company,  please drop us an email at opensource@newrelic.com.
   
**A note about vulnerabilities**

As noted in our [security policy](../../security/policy), New Relic is committed to the privacy and security of our customers and their data. We believe that providing coordinated disclosure by security researchers and engaging with the security community are important means to achieve our security goals.

If you believe you have found a security vulnerability in this project or any of New Relic's products or websites, we welcome and greatly appreciate you reporting it to New Relic through [HackerOne](https://hackerone.com/newrelic).   
  
## License
New Relic Java Instrumentation for Oracle Service Bus 11G is licensed under the [Apache 2.0](http://apache.org/licenses/LICENSE-2.0.txt) License.

