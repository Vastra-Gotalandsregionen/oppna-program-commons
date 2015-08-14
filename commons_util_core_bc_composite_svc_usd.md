# Introduction #

The USD webservice client can add incidents to the UnicenterServicePlus ServiceDesk application.


# Details #

## logging: ##

org.apache.commons is used


## Third party dependencies ##

JUnit,org.apache.axis,

## configuration: ##

The file usd.properties (should be placed in the classpath root)

It should contain e.g.
```
endpoint=http://someserver.org:8080/axis/services/USD_R11_WebService?WSDL
user=someuser
password=somepwd
repositoryHandle=doc_rep:400001
```
The file usdAppToGroup.properties (should be placed in the classpath root)

It should contain e.g.
```
Tyck_till_test_portlet=01 Gbg.SC.Service Desk IT
Antonio_portal=01 Gbg.SC.Service Desk IT
... 
(appname)=(groupname in USD)
```
## Usage ##

pom dependecies:
```
<dependency>
    <groupId>se.vgr.commons-util</groupId>
    <artifactId>commons-util-core-bc-composite-svc-usd</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
```
Spring context setup example:
```
<bean id="usdService" class="se.vgr.usdservice.USDServiceImpl" >
	<constructor-arg ref="usdProperties" />
    <property name="usdAppToGroupMappings"  ref="usdAppToGroupMappings" />
</bean>
	
<bean id="usdProperties"
		class="org.springframework.beans.factory.config.PropertiesFactoryBean">
		<property name="location" value="classpath:usd.properties" />
</bean>
  
<bean id="usdAppToGroupMappings"
        class="org.springframework.beans.factory.config.PropertiesFactoryBean">
        <property name="location" value="classpath:usdAppToGroup.properties" />
</bean>
```