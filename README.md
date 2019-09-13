# SPI-Annotation-Processor

[![Maven Central](https://maven-badges.herokuapp.com/maven-central/io.toolisticon.spiap/spiap-processor/badge.svg)](https://maven-badges.herokuapp.com/maven-central/io.toolisticon.spiap/spiap-processor)
[![Build Status](https://travis-ci.org/toolisticon/SPI-Annotation-Processor.svg?branch=master)](https://travis-ci.org/toolisticon/SPI-Annotation-Processor)
[![codecov](https://codecov.io/gh/toolisticon/SPI-Annotation-Processor/branch/master/graph/badge.svg)](https://codecov.io/gh/toolisticon/SPI-Annotation-Processor)

# Why you should use this project?

If you want to use a service provider interface (_SPI_) you need to register your service implementation in the _/META-INF/services/&lt;Full qualified spi interface name&gt;_ file.
Additionally you usually need to write a service locator to be able to use the service implementation.

The annotation processor offered by this project provides exactly this. It allows you to create the service locator file just by adding an annotation to you spi implementation.
Additionally it will generate a service locator for you.  

# Features
Annotation processor that
- provides support for generating service locator file in _/META-INF/services/&lt;Full qualified spi interface name&gt;_
- provides support for generating service locator class for accessing the SPI implementations

# How does it work?

First you need to add the SPI annotation processor dependencies to your project.
Since the annotation processor has to be there just at compile time, it's ok to use provided scope.

	<dependencies>
	    <!-- api is just needed in compile scope if you want to use the ServiceLocator features -->
	    <dependency>
	        <groupId>io.toolisticon.spiap</groupId>
	        <artifactId>spiap-api</artifactId>
	    </dependency>
	    <!-- must be on provided scope since it is just needed at compile time -->
	    <dependency>
	        <groupId>io.toolisticon.spiap</groupId>
	        <artifactId>spiap-processor</artifactId>
	        <scope>provided</scope>
	    </dependency>
	</dependencies>

The annotation processor then will create the configuration into the StandardLocation.SOURCE_OUTPUT folder.
You need to use the following build-helper-maven-plugin configuration to include the generated resource files into your jar:

    <plugin>
        <groupId>org.codehaus.mojo</groupId>
        <artifactId>build-helper-maven-plugin</artifactId>
        <version>1.9.1</version>
        <executions>
            <execution>
                <id>add-resource</id>
                <phase>generate-resources</phase>
                <goals>
                    <goal>add-resource</goal>
                </goals>
                <configuration>
                    <resources>
                        <resource>
                            <directory>target/generated-sources/annotations</directory>
                            <targetPath />
                        </resource>
                    </resources>
                </configuration>
            </execution>
        </executions>
    </plugin>

## How to generate the service locator class
### By annotating the service provider interface
Just add the Spi annotation to your SPI:

	@Spi
	public interface ExampleSpiInterface {

	    String doSomething();

	}

The locator is named like the SPI suffixed with ServiceLocator (ExampleSpiInterfaceServiceLocator in this example)

### By annotating a package
Create a package-info.java file in the package were you want to create the service locator in.
Then add the SpiServiceLocator annotation in it:

    @SpiServiceLocator(ExampleSpiInterface.class)
    package your.target.package;

    import io.toolisticon.spiap.api.SpiServiceLocator;
    import your.spi.package.ExampleSpiInterface;

The locator will be created in the annotated package. It is named like the SPI suffixed with ServiceLocator (ExampleSpiInterfaceServiceLocator in this example)


## How to register a service implementation
Just add a Service annotation to your service implementation:

	@Service(value = ExampleSpiInterface.class, id = "YOUR_OPTIONAL_SERVICE_ID", description = "OPTIONAL DESCRIPTION", priority = 0)
	public class ExampleSpiService implements ExampleSpiInterface {
	    @Override
            public String doSomething() {
                return "IT WORKS !";
            }
	}

Service annotations mandatory value must declare the SPI you want the service class to be registered to.
All other annotation attributes are optional. 

- id defines a custom id which can be used to locate a specific servics implementation via the generated service locator class. Defaults to fully qualified service class name in generated service locator.
- description declares a short description about the implementation
- priority is used to define a specific order in which the services are located

It's also possible to implement more than one SPI in a class by using the Services annotation:
        
	@Services({
	    @Service(value = ExampleSpiInterface1.class, id = "YOUR_OPTIONAL_SERVICE_ID", description = "OPTIONAL DESCRIPTION", priority = 0),
	    @Service(value = ExampleSpiInterface2.class)
	})
	public class ExampleSpiService implements ExampleSpiInterface1, ExampeSpiInterface2 {
	    @Override
            public String doSomething1() {
                return "IT WORKS !";
            }
	    @Override
            public String doSomething2() {
                return "IT WORKS TOO!";
            }
	}

## How to use the service locator

	System.out.println(ExampleSpiInterfaceServiceLocator.locate().doSomething());


It's not required to have the spiap-api dependency at runtime anymore since settings of annotations will be stored in a generated property file (stored in /META-INF/spiap/&lt;service interface fqn&gt;/&lt;service implementation fqn&gt;.properties).
See our examples subprojects for further information.

## Disable service implementations in the service locator
Just add the _OutOfService_ annotation next to the Services or Service annotation to disable the service implementation in the service locator.

# Contributing

We welcome any kind of suggestions and pull requests.

## Building and developing the SPI-Annotation-Processor

The SPI-Annotation-Processor is built using Maven (at least version 3.0.0).
A simple import of the pom in your IDE should get you up and running. To build the annotation-processor-toolkit on the commandline, just run `mvn` or `mvn clean install`

## Requirements

The likelihood of a pull request being used rises with the following properties:

- You have used a feature branch.
- You have included a test that demonstrates the functionality added or fixed.
- You adhered to the [code conventions](http://www.oracle.com/technetwork/java/javase/documentation/codeconvtoc-136057.html).

## Contributions

- (2017) Tobias Stamann (Holisticon AG)

# License

This project is released under the revised [MIT License](LICENSE).

This project includes and repackages the [Annotation-Processor-Toolkit](https://github.com/holisticon/annotation-processor-toolkit) released under the  [MIT License](/3rdPartyLicenses/annotation-processor-toolkit/LICENSE.txt).
