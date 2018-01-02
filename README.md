# SPI-Annotation-Processor

[![Maven Central](https://maven-badges.herokuapp.com/maven-central/io.toolisticon.spiap/spiap-parent/badge.svg)](https://maven-badges.herokuapp.com/maven-central/io.toolisticon.spiap/spiap-parent)
[![Build Status](https://travis-ci.org/toolisticon/SPI-Annotation-Processor.svg?branch=master)](https://travis-ci.org/toolisticon/SPI-Annotation-Processor)
[![codecov](https://codecov.io/gh/toolisticon/SPI-Annotation-Processor/branch/master/graph/badge.svg)](https://codecov.io/gh/toolisticon/SPI-Annotation-Processor)

# Why you should use this project?

If you want to use a service provider interface (SPI) you need to register your service implementation in the /META-INF/services/&lt;Full qualified spi interface name&gt; file.
Additionally you usually need to write a service locator to be able to use the service implementation.

The annotation processor offered by this project provides exactly this. It allows you to create the service locator file just by adding an annotation to you spi implementation.
Additionally it will generate a service locator for you.  

# Features
Annotation processor that
- provides support for generating service locator file in /META-INF/services/&lt;Full qualified spi interface name&gt;
- provides support for generating service locator class for accessing the SPI implementations

# How does it work?

First you need to add the SPI annotation processor dependency to your project.
Since it has to be there just at compile time, it's ok to use provided scope.

	<dependencies>
	    <dependency>
	        <groupId>de.holisticon.tools.spiap</groupId>
	        <artifactId>spi-ap-processor</artifactId>
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
Just add a SpiImpl annotation to your service implementation:

	@SpiImpl(spis = {"de.holisticon.example.spiapexample.api.ExampleSpiInterface"})
	public class ExampleSpiService implements ExampleSpiInterface {
	    @Override
            public String doSomething() {
                return "IT WORKS !";
            }
	}


## How to use the service locator

	System.out.println(ExampleSpiInterfaceServiceLocator.locate().doSomething());


See our examples subprojects about how to use the annotations.


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

## Sponsoring

This project is sponsored and supported by [holisticon AG](http://www.holisticon.de/)

![Holisticon AG](/holisticon-logo.png)

# License

This project is released under the revised [BSD License](LICENSE).

This project includes and repackages the [Annotation-Processor-Toolkit](https://github.com/holisticon/annotation-processor-toolkit) released under the  [BSD License](/3rdPartyLicenses/annotation-processor-toolkit/LICENSE.txt).
