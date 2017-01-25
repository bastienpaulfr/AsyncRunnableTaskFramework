package fr.coppernic.framework.art;

import org.junit.Test;

import fr.coppernic.framework.robolectric.RobolectricTest;
import fr.coppernic.framework.utils.core.CpcResult.RESULT;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.isA;

/**
 * Created by bastien on 16/06/16.
 */
public class AsyncExecutorFactoryTest extends RobolectricTest {

	@Test
	public void makeAsyncExecutorService() {
		AsyncExecutor<RESULT,AsyncRunnableTask<RESULT>>executor =
			AsyncExecutorFactory.get(null);
		assertThat(executor, isA(AsyncExecutor.class));
	}
}
