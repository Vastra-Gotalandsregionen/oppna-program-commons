# Introduction #

This LDAP client can be used when searching for or for managing users (add,modify or delete) in the LDAP directory.


# Details #

## logging: ##

none! TODO


## Third party dependencies ##

JUnit

## configuration: ##

The file ldap.properties (should be placed in the classpath root)

It should contain e.g.
```
BIND_DN=cn=root
BIND_URL=ldap://someserver.acme.org:389
BIND_PW=somepwd
```
## Usage ##
```
pom dependecies:
<dependency>
    <groupId>se.vgr.commons-util</groupId>
    <artifactId>commons-util-core-bc-composite-svc-ldap</artifactId>
    <version>3.1</version>
</dependency>
```
Spring context setup example:
```
<bean id="ldapService" class="se.vgr.ldapservice.LdapServiceImpl" >
		<constructor-arg ref="ldapProperties" />
</bean>
    
<bean id="ldapProperties"
        class="org.springframework.beans.factory.config.PropertiesFactoryBean">
        <property name="location" value="classpath:ldap.properties" />
</bean>
```

