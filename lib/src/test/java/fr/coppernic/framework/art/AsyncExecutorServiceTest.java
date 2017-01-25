package fr.coppernic.framework.art;

import android.os.HandlerThread;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.robolectric.Shadows;
import org.robolectric.shadows.ShadowLooper;

import fr.coppernic.framework.art.AsyncExecutor.RetCode;
import fr.coppernic.framework.robolectric.RobolectricTest;
import fr.coppernic.framework.utils.core.CpcResult.RESULT;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Created by bastien on 16/06/16.
 */
public class AsyncExecutorServiceTest extends RobolectricTest {

	private static final String TAG = "AsyncExecutorServiceTest";
	@Spy
	private final TaskTwice spyTwice = new TaskTwice();
	@Spy
	private final TaskCancel spyCancel = new TaskCancel();
	@Spy
	private final Listener listener = new Listener();
	@Spy
	private final AsyncRunnableTaskTest spy = new AsyncRunnableTaskTest();
	@Mock
	private final AsyncRunnableTaskTest mock = new AsyncRunnableTaskTest();
	@Rule
	public MockitoRule mockitoRule = MockitoJUnit.rule();
	@Rule
	public ExpectedException expectedException = ExpectedException.none();
	private TestExecutor service = null;
	private HandlerThread handlerThread;

	@Before
	public void before() {
		handlerThread = new HandlerThread(TAG);
		service = new TestExecutor(handlerThread);
	}

	@After
	public void after() {
		handlerThread.quit();
		handlerThread = null;
	}

	@Test
	public void testSimpleExecution() {

		service.add(spy);
		service.execute();
		runLooper();

		verify(spy, times(1)).onDone(RESULT.OK);
	}

	@Test
	public void executorIsDisposed() {
		service.dispose();
		service.add(mock);
		assertThat(service.execute(),is(RetCode.WRONG_STATE));
	}

	@Test
	public void testCallbackAfterExecution() {
		service.setListener(listener);
		service.add(spy);
		service.execute();
		runLooper();

		verify(listener, times(1)).onDone();
		verify(listener, times(1)).beforeTask(spy);
		verify(listener, times(1)).afterTask(spy, RESULT.OK);
	}

	@Test
	public void testRunTwice() {
		service.add(spyTwice);
		service.execute();
		runLooper();
		RetCode ret = service.executeCurrent();
		assertThat(ret,is(RetCode.OK));
		runLooper();
		verify(spyTwice, times(1)).onDone(RESULT.OK);
	}

	@Test
	public void testCancel() {
		service.setListener(listener);
		service.setExpected(RESULT.CANCELLED);
		service.add(spyCancel);
		service.execute();
		runLooper();

		verify(listener, times(1)).onCancelled();
		verify(listener, times(1)).beforeTask(spyCancel);
		verify(listener, times(1)).afterTask(spyCancel, RESULT.CANCELLED);
	}

	private void runLooper() {
		ShadowLooper l = Shadows.shadowOf(handlerThread.getLooper());
		l.idle();
	}

	private class TestExecutor extends AsyncExecutorService<RESULT,
		AsyncRunnableTask<RESULT>> {

		private RESULT expected = RESULT.OK;

		void setExpected(RESULT expected) {
			this.expected = expected;
		}

		TestExecutor(HandlerThread handlerThread) {
			super(handlerThread);
		}

		@Override
		public synchronized void onDone(AsyncRunnable<RESULT> task,
		                                RESULT param) {
			assertThat(param, is(expected));
			super.onDone(task, param);
		}

		@Override
		public synchronized void onCancel(AsyncRunnable<RESULT> task,
		                                  RESULT param) {
			assertThat(param, is(expected));
			super.onCancel(task, param);
		}
	}

	private class TaskTwice extends AsyncRunnableTask<RESULT> {

		private int i = 0;

		@Override
		public String getName() {
			return "TaskTwice";
		}

		@Override
		public void execute() {
			if (i++ == 1) {
				onDone(RESULT.OK);
			}
		}
	}

	private class TaskCancel extends AsyncRunnableTask<RESULT> {

		@Override
		public String getName() {
			return "TaskCancel";
		}

		@Override
		public void execute() {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			onCancel(RESULT.CANCELLED);
		}
	}

	private class Listener implements AsyncExecutorListener<RESULT, AsyncRunnableTask<RESULT>> {

		public Listener() {
		}

		@Override
		public void beforeTask(AsyncRunnableTask<RESULT> task) {

		}

		@Override
		public void afterTask(AsyncRunnableTask<RESULT> task, RESULT param) {

		}

		@Override
		public void onDone() {

		}

		@Override
		public void onCancelled() {

		}

		@Override
		public void onPaused() {

		}
	}

}
