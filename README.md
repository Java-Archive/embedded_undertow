# Infos for the repo.


This is a basic kernel for MicroServices. It was created as POC for a customer project.
Target was the basic implementation for a few technologies and the developer environment.

## Servlet Container
As Servlet Container I am using Undertow here. This we could config and start via the Main class.

## Servlets
For this project we needed Servlets. The Servlets itself are CDI managed. No EE Stack, CDI only.
But this will be changed to a D-DI version soon. Until now, WELD is activated per default.

## REST Resources
The REST resources are activated manually inside the class JaxRsActivator. I will change it in a way, that this class will
be generated via AnnotationProcessing.

## Kotlin
As additional language Kotlin is aktivated. With this you could add immuntable model elements for example.
I started with this, because it is a nice add on, but near enough to Java.

## TDD
To test this micro kernel you could start the container inside the ``@Before`` method mit ``Main.deploy()```.

### jUnit
Per maven, junit is activated

### JMH
JMH is included and could be run via the exec target from the maven plugin.
