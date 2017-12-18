# Sonar Apigee Plugin


This SonarQube Plugin is designed to test Apigee apiproxies.
The goals is to help  API developpers in doing a static analysis and providing issues to the Sonar engine.

The rules are based on the [apigeecs/bundle-linter](https://github.com/apigeecs/bundle-linter) and on this [document about best-practices](https://docs.apigee.com/api-services/content/best-practices-api-proxy-design-and-development)
All rules are not yet implemented ([see below](#implemented-rules))

## Usage

### Dependencies
To work, th plugin sonar-xml-plugin MUST be installed.
As Apigee also deals with Javascript and Python, it would be pertinent to install sonar-python-plugin and sonar-javascript-plugin, but it's not mandatory.

### Build

    mvn clean install
  
You'll get a jar file in the target directory

### Installation
Like any Sonar plugin :
 * put the file sonar-apigee-plugin-X.X.X.jar in the directory $SONARQUBE_HOME/extensions/plugins.
 * restart the server
Finally :
 * activate all rules in the sonar way profile
 

## Why this plugin ?
 * because companies, like mine, prefer using a centralized platform like Sonar, instead of an standalone tool
 * because SonarQube provides a lot of tools, measures, issue management, ... out-of-the-box
 * because SonarQube is well integrated with CI platform like Jenkins. It's part of continuous delivery.
 
## Why is there package `org.sonar.plugins.xml.checks` ?

This plugin shares the classloader of sonar-xml-plugin thanks to the usage of `<basePlugin>`.
Adding the check classes in the package `org.sonar.plugins.xml.checks` is the only way to be able to use XmlSourceCode.getDocument(boolean) since this method is `protected`.

## Implemented Rules

The rule IDs come from the [apigeecs/bundle-linter](https://github.com/apigeecs/bundle-linter).
Other rules start from "500" to not interfer with the first rules. Example : PD500.

**Legend :**
>
>:white_check_mark: : implemented
>
>:heavy_multiplication_x: : not yet implemented
>
>:x: : won't be implemented. See details in Description column 


### Bundle level
| Status | Rule&nbsp;ID | Severity | Name | Description |
|:------:| ---- | -------- | ---- | ----------- |
|:heavy_multiplication_x:| BN001 | &nbsp; | Bundle folder structure correctness. | Bundles have a clear structure. |
|:heavy_multiplication_x:| BN002 | &nbsp; | Extraneous files. | Ensure each folder contains approrpriate resources in the bundle. |
|:white_check_mark:| BN003 | Major | Cache Coherence | A bundle that includes cache reads should include cache writes with the same keys. |
|:heavy_multiplication_x:| BN004 | &nbsp; | Unused variables. | Within a bundle variables created should be used in conditions, resource callouts, or policies. |
|:heavy_multiplication_x:| BN005 | &nbsp; | Unattached policies. | Unattached policies are dead code and should be removed from production bundles. |
|:heavy_multiplication_x:| BN006 | &nbsp; | Bundle size - policies. | Large bundles are a symptom of poor design. A high number of policies is predictive of an oversized bundle. |
|:heavy_multiplication_x:| BN007 | &nbsp; | Bundle size - resource callouts. | Large bundles are a symptom of poor design. A high number of resource callouts is indicative of underutilizing out of the box Apigee policies. |
|:heavy_multiplication_x:| BN008 | &nbsp; | IgnoreUnresolvedVariables and FaultRules | Use of IgnoreUnresolvedVariables without the use of FaultRules may lead to unexpected errors. |
|:heavy_multiplication_x:| BN009 | &nbsp; | Statistics Collector - duplicate policies | Warn on duplicate policies when no conditions are present or conditions are duplicates. |
|:white_check_mark:| BN500 | Info | Description length | The Description tag should have more than 5 chars to be useful. |

### Proxy Definition level
| Status | Rule&nbsp;ID | Severity | Name | Description |
|:------:| ---- | -------- | ---- | ----------- |
|:heavy_multiplication_x:| PD001 | &nbsp; | RouteRules to Targets | RouteRules should map to defined Targets |
|:white_check_mark:| PD002 | Blocker | Unreachable Route Rules - defaults | Only one RouteRule should be present without a condition |
|:white_check_mark:| PD003 | Blocker | Unreachable Route Rules | RouteRule without a condition should be last. |


### Target Definition level
| Status | Rule&nbsp;ID | Severity | Name | Description |
|:------:| ---- | -------- | ---- | ----------- |
|:white_check_mark:| TD001 | Critical | Mgmt Server as Target | Discourage calls to the Management Server from a Proxy via target. |
|:white_check_mark:| TD002 | Major | Use Target Servers | Encourage the use of target servers |

### Flow level
| Status | Rule&nbsp;ID | Severity | Name | Description |
|:------:| ---- | -------- | ---- | ----------- |
|:white_check_mark:| FL001 | Blocker | Unconditional Flows | Only one unconditional flow will get executed. Error if more than one was detected. |
|:white_check_mark:| FL500 | Critical | Default flow | A default flow should be defined to catch all requests on undefined resources. |
|:white_check_mark:| FL501 | Blocker | Unreachable flow | Flow without a condition should be last. |

### Step level
| Status | Rule&nbsp;ID | Severity | Name | Description |
|:------:| ---- | -------- | ---- | ----------- |
|:white_check_mark:| ST001 | Minor | Empty Step | Empty steps clutter the bundle. (Should never happen, Apigee already blocks this error form occuring.) |

### Policy level
| Status | Rule&nbsp;ID | Severity | Name | Description |
|:------:| ---- | -------- | ---- | ----------- |
|:white_check_mark:| PO001 | Major | JSON Threat Protection | A check for a body element must be performed before policy execution. |
|:white_check_mark:| PO002 | Major | XML Threat Protection | A check for a body element must be performed before policy execution. |
|:white_check_mark:| PO003 | Major | Extract Variables with JSONPayload | A check for a body element must be performed before policy execution. |
|:white_check_mark:| PO004 | Major | Extract Variables with XMLPayload | A check for a body element must be performed before policy execution. |
|:white_check_mark:| PO005 | Major | Extract Variables with FormParam | A check for a body element must be performed before policy execution. |
|:heavy_multiplication_x:| PO006 | &nbsp; | Policy Naming Conventions - default name | Policy names should not be default. |
|:white_check_mark:| PO007 | Minor | Policy Naming Conventions - type indication | It is recommended that the policy name include an indicator of the policy type. |
|:white_check_mark:| PO008 | Minor | Policy Name Attribute Conventions | It is recommended that the policy name attribute match the display name of the policy. |
|:white_check_mark:| PO009 | Critical | Service Callout Target - Mgmt Server | Targeting management server may result in higher than expected latency use with caution. |
|:heavy_multiplication_x:| PO010 | &nbsp; | Service Callout Target - Target Server | Encourage use of target servers. |
|:heavy_multiplication_x:| PO011 | &nbsp; | Service Callout Target - Dynamic URLs | Error on dynamic URLs in target server URL tag. |
|:heavy_multiplication_x:| PO012 | &nbsp; | Service Callout Target - Script Target Node | JSHint, ESLint. |
|:x:| PO013 | &nbsp; | Resource Call Out - Javascript | Analyzed by sonar-javascript-plugin. |
|:x:| PO014 | &nbsp; | Resource Call Out - Java |  Analyzed by sonar-java-plugin. |
|:x:| PO015 | &nbsp; | Resource Call Out - Python |  Analyzed by sonar-python-plugin. |
|:heavy_multiplication_x:| PO016 | &nbsp; | Statistics Collector - duplicate variables | Warn on duplicate variables. |
|:heavy_multiplication_x:| PO017 | &nbsp; | Misconfigured - FaultRules/Fault Rule in Policy | FaultRules are configured in ProxyEndpoints and TargetEndpoints. |
|:white_check_mark:| PO018 | Major | Regex Lookahead/Lookbehind are Expensive - Threat Protection Policy | Regular expressions that include lookahead or lookbehind perform slowly on large payloads and are typically not required.|
|:white_check_mark:| PO019 | Major | Reserved words as variables - ServiceCallout Request | Using "request" as the name of a Request may cause unexpected side effects.|
|:white_check_mark:| PO020 | Major | Reserved words as variables - ServiceCallout Response | Using "response" as the name of a Response may cause unexpected side effects.|
|:heavy_multiplication_x:| PO021 | &nbsp; | Statistics Collector - reserved variables | Warn on insertion of duplicate variables. |
|:heavy_multiplication_x:| PO022 | &nbsp; | Nondistributed Quota | When using nondistributed quota the number of allowed calls is influenced by the number of Message Processors (MPs) deployed. This may lead to higher than expected transactions for a given quota as MPs now autoscale. |
|:heavy_multiplication_x:| PO023 | &nbsp; | Quota Policy Reuse | When the same Quota policy is used more than once you must ensure that the conditions of execution are mutually exclusive or that you intend for a call to count more than once per message processed. |
|:heavy_multiplication_x:| PO024 | &nbsp; | Cache Error Responses | By default the ResponseCache policy will cache non 200 responses. Either create a condition or use policy configuration options to exclude non 200 responses. |
|:white_check_mark:| PO500 | Major | Avoid Python language | Python scripts can introduce performance bottlenecks for simple executions, as it is interpreted at runtime. |

### FaultRules level
| Status | Rule&nbsp;ID | Severity | Name | Description |
|:------:| ---- | -------- | ---- | ----------- |
|:white_check_mark:| FR001 | Major | No Condition on FaultRule | It's not a best practice to have a FaultRule without an outer condition, which automatically makes the FaultRule true. |

### Conditional level
| Status | Rule&nbsp;ID | Severity | Name | Description |
|:------:| ---- | -------- | ---- | ----------- |
|:heavy_multiplication_x:| CC001 | &nbsp; | Literals in Conditionals | Warn on literals in any conditional statement. |
|:heavy_multiplication_x:| CC002 | &nbsp; | Null Blank Checks | Blank checks should also check for null conditions. (to be reviewed) |
|:white_check_mark:| CC003 | Minor | Long condition statement | Conditions should not be long. (i.e. less than 256 characters) |
|:heavy_multiplication_x:| CC004 | &nbsp; | Overly complex condition | Condition complexity should be limited to fix number of variables and conjunctions. |
|:heavy_multiplication_x:| CC006 | &nbsp; | Detect logical absurdities | Conditions should not have internal logic conflicts - warn when these are detected. |


