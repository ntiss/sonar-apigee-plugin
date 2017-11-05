# Sonar Apigee Plugin


This SonarQube Plugin is designed to test Apigee apiproxies.
The goals is to help  API developpers in doing a static analysis and providing issues to the Sonar engine.

The rules are based on the [apigeecs/bundle-linter](https://github.com/apigeecs/bundle-linter) and on this [document about best-practices](https://docs.apigee.com/api-services/content/best-practices-api-proxy-design-and-development)
All rules are not yet implemented ([see below](#implemented-rules))

## Usage

### Build

    mvn clean install
  
You'll get a jar file in the target directory

### Installation
Like any Sonar plugin :
 * put the file sonar-apigee-plugin-X.X.X.jar in the directory $SONARQUBE_HOME/extensions/plugins.
 * restart the server

And :
 * activate all rules in the sonar way profile
 
## Dependencies
To work, this plugin needs sonar-xml-plugin and sonar-python-plugin installed.
Thus, you can also have the measures for theses languages.

## Why this plugin ?
 * because companies, like mine, prefer using a centralized platform like Sonar, instead of an standalone tool
 * because I'm mainly a java developper 
 
## Why are there packages `org.sonar.plugins.xml.*` and `org.sonar.plugins.python.*` ?
 * Because Sonar plugins are isolated in their own classloader ([See here](https://docs.sonarqube.org/pages/viewpage.action?pageId=5312387))
 * Because XML and Python plugin doesn't export any classes in a package `org.sonar.plugins.<plugin key>.api.*` 
 * Because it's not possible to add custom rules in Java (not XPath) in existing plugin ([according to this thread](https://groups.google.com/forum/#!searchin/sonarqube/plugin/sonarqube/A5xyZuHpZO0/fALCTY9hAQAJ))
 * Because I found this easier to do and I have no idea on how to do better for the moment
 
I could have add dependencies to these artefacts in the pom.xml but in this case, the plugin growth and become too big (almost 20MB)

## Implemented Rules

Work in progress.

