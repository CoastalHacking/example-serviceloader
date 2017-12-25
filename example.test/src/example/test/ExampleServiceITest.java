package example.test;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;

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
	public void shouldLoadService() {
		ExampleConsumer consumer = new ExampleConsumer();
		Assert.assertTrue(consumer.canConsume());
	}
}