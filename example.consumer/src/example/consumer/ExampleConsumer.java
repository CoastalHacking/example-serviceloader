package example.consumer;

import java.util.Iterator;
import java.util.ServiceLoader;

import example.api.ExampleService;

public class ExampleConsumer {

	public boolean canConsume() {
		ServiceLoader<ExampleService> loader = ServiceLoader.load(ExampleService.class);
		Iterator<ExampleService> it = loader.iterator();
		ExampleService service = null;
		if (it.hasNext()) {
			service = it.next();
		}
		return service != null;
	}

}
