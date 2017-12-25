package example.provider;

import org.junit.Assert;

import java.util.Iterator;
import java.util.ServiceLoader;

import org.junit.Test;

import example.api.ExampleService;
import example.api.ExampleUnimplementedService;

public class ExampleServiceImplTest {

	@Test
	public void shouldLoadService() {
		ServiceLoader<ExampleService> loader = ServiceLoader.load(ExampleService.class);
		Iterator<ExampleService> it = loader.iterator();
		ExampleService service = null;
		if (it.hasNext()) {
			service = it.next();
		}
		Assert.assertNotNull(service);
	}

	public void shouldNotLoadService() {
		ServiceLoader<ExampleUnimplementedService> loader = ServiceLoader.load(ExampleUnimplementedService.class);
		Iterator<ExampleUnimplementedService> it = loader.iterator();
		ExampleUnimplementedService service = null;
		if (it.hasNext()) {
			service = it.next();
		}
		Assert.assertNull(service);
	}	
}
