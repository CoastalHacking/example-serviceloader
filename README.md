
# Example OSGi ServiceLoader Bndtools Workspace

## example.api

Simple API created using bndtools API template

## example.provider

[ServiceLoader](https://docs.oracle.com/javase/8/docs/api/java/util/ServiceLoader.html) provider

See:

* [example.api.ExampleService](/example.provider/src/META-INF/services/example.api.ExampleService)
for the provider configuration.

* [ExampleServiceImplTest.shouldLoadService()](/example.provider/test/example/provider/ExampleServiceImplTest.java) for a simple test

Main configuration via its [bnd.bnd](/example.provider/bnd.bnd):

	# SPI
	Require-Capability: osgi.extender; \
		filter:="(osgi.extender=osgi.serviceloader.registrar)"

	Provide-Capability: osgi.serviceloader; \
		osgi.serviceloader=example.api.ExampleService

	Include-Resource: META-INF=src/META-INF


## example.consumer

ServiceLoader consumer and OSGi-ignorant bundle being installed into an OSGi
environment.

Main configuration via its [bnd.bnd](/example.consumer/bnd.bnd):

	Require-Capability: osgi.extender; \
		  filter:="(osgi.extender=osgi.serviceloader.processor)", \
		osgi.serviceloader; \
		  filter:="(osgi.serviceloader=example.api.ExampleService)"; \
		  cardinality:=multiple


## example.test

OSGi-runtime JUnit integration tests

See:

* [bnd.bnd](/example.test/bnd.bnd): ensure the `org.apache.aries.spifly.dynamic.bundle`
bundle is first, since this implicitly defines the start level of the bundle as the first
to be loaded. From [SPI Fly](http://aries.apache.org/modules/spi-fly.html):

> Note that, as with any OSGi Bundle that uses the OSGi 4.3 WeavingHooks, the weaver bundle (org.apache.aries.spifly.dynamic.bundle in the SPI Fly case) needs to be active before any bundles that need to be dynamically woven. OSGi Start Levels can provide a mechanism to control this.

* [ExampleServiceITest](/example.test/src/example/test/ExampleServiceITest.java):
  * `shouldHaveService`: access SPI via OSGi service registry
  * `shouldHaveConsumer`: access SPI via `ServiceLoader` within OSGi
