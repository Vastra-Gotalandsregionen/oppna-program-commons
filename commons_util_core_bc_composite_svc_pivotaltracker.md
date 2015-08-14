# Introduction #

The Pivotal Tracker client can add user stories to a PivotalTracker project.


# Details #

## logging: ##

org.slf4j is used


## Third party dependencies ##

JUnit,org.apache.http, org.w3c.dom

## configuration: ##

The file pivotaltracker.properties (should be placed in the classpath root)

It should contain e.g.
```
PT_USER=someuser
PT_PWD=somepwd
```

## Usage ##

pom dependecies:
```
<dependency>
    <groupId>se.vgr.commons-util</groupId>
    <artifactId>commons-util-core-bc-composite-svc-pivotaltracker</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
```

Spring context setup example:
```
<bean id="pivotalTrackerProperties"
        class="org.springframework.beans.factory.config.PropertiesFactoryBean">
        <property name="location" value="classpath:pivotalTracker.properties" />
</bean>

<bean id="pivotalTrackerService" class="se.vgr.incidentreport.pivotaltracker.impl.PivotalTrackerServiceImpl" >
        <constructor-arg ref="pivotalTrackerProperties" />
</bean>
```