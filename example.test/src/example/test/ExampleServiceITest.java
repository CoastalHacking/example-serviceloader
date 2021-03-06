package example.test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTracker;
import org.osgi.util.tracker.ServiceTrackerCustomizer;

import example.api.ExampleService;
import example.consumer.ExampleConsumer;

@RunWith(MockitoJUnitRunner.class)
public class ExampleServiceITest {

	private final BundleContext context = FrameworkUtil.getBundle(ExampleServiceITest.class).getBundleContext();

	@Test
	public void shouldHaveService() {
		ServiceReference<ExampleService> ref = context.getServiceReference(ExampleService.class);
		Assert.assertNotNull(ref);
		ExampleService service = context.getService(ref);
		Assert.assertNotNull(service);
		context.ungetService(ref);
	}

	@Test
	public void shouldHaveConsumer() {
		ExampleConsumer consumer = new ExampleConsumer();
		Assert.assertTrue(consumer.canConsume());
	}

	private Bundle getProviderBundle() {
		Bundle result = null;
		for (Bundle bundle: context.getBundles()) {
			if (bundle.getLocation().endsWith("example.provider.jar")) {
				result = bundle;
			}
		}
		return result;
	}
	
	public static class TestServiceTrackerCustomizer implements ServiceTrackerCustomizer<ExampleService, ExampleService> {

		public TestServiceTrackerCustomizer(CountDownLatch latch, BundleContext context) {
			super();
			this.latch = latch;
			this.context = context;
		}
		private ExampleService service;
		private CountDownLatch latch;
		private BundleContext context;

		public ExampleService getService() {
			return service;
		}

		@Override
		public ExampleService addingService(ServiceReference<ExampleService> reference) {
			System.out.println("Service reference being returned: " + reference);
			service = context.getService(reference);
			return service;
		}

		@Override
		public void modifiedService(ServiceReference<ExampleService> reference, ExampleService service) {}

		@Override
		public void removedService(ServiceReference<ExampleService> reference, ExampleService service) {
			System.out.println("Service reference being removed: " + service);
			this.service = null;
			latch.countDown();
		}
	}

	@Test
	public void shouldCallServiceTracker() throws Exception {

		// Start the service tracker
		CountDownLatch latch = new CountDownLatch(1);
		TestServiceTrackerCustomizer customizer = new TestServiceTrackerCustomizer(latch, context);
		ServiceTracker<ExampleService, ExampleService> tracker = new ServiceTracker<ExampleService, ExampleService>(context, ExampleService.class, customizer);
		tracker.open();

		ExampleService service = customizer.getService();
		Assert.assertNotNull(service);

		// Stop the consumer and close tracker
		Bundle bundle = getProviderBundle();
		if (bundle != null) {
			System.out.println("Stopping bundle: " + bundle);
			bundle.stop();
		}
		tracker.close();

		service = customizer.getService();
		Assert.assertNull(service);

		// Assert the customizer was notified of the service going away
		Assert.assertTrue(latch.await(500, TimeUnit.MILLISECONDS));
	}

}