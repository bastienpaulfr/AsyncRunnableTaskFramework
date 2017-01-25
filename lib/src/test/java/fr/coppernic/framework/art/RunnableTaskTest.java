package fr.coppernic.framework.art;

import org.junit.Before;
import org.junit.Test;

import fr.coppernic.framework.robolectric.RobolectricTest;
import fr.coppernic.framework.utils.core.CpcResult.RESULT;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Created by bastien on 16/06/16.
 */
public class RunnableTaskTest extends RobolectricTest {

	private AsyncRunnableTaskTest task = null;

	@Before
	public void before() {
		task = new AsyncRunnableTaskTest();
		task.setTimeToSleep(100);
	}

	@Test(expected = NullPointerException.class)
	public void executeWithoutSettingUp() {
		task.run();
	}

	@Test
	public void basicExecution() {
		task.setListener(new Listener());
		task.run();
	}

	@Test(expected = RuntimeException.class)
	public void cannotExecuteTwice() {
		task.setListener(new Listener());
		assertThat(task.canRun(), is(true));
		task.run();
		assertThat(task.canRun(), is(false));
		task.run();
	}

	@Test
	public void executeTwice() {
		TaskMulti task = new TaskMulti(2,10);
		task.setListener(new Listener());
		assertThat(task.getState(),is(AsyncRunnable.State.IDLE));
		assertThat(task.canRun(), is(true));
		task.run();
		assertThat(task.getState(),is(AsyncRunnable.State.PENDING));
		assertThat(task.canRun(), is(true));
		task.execute();
		assertThat(task.canRun(), is(false));
		assertThat(task.getState(),is(AsyncRunnable.State.DONE));
	}

	private class Listener implements AsyncRunnableListener<RESULT> {
		@Override
		public void onDone(AsyncRunnable<RESULT> task, RESULT param) {
			assertThat(param, is(RESULT.OK));
		}

		@Override
		public void onCancel(AsyncRunnable<RESULT> task, RESULT param) {
			assertThat(param, is(RESULT.CANCELLED));
		}
	}

	private class TaskMulti extends AsyncRunnableTask<RESULT> {

		private int i = 1;
		private final int max;
		private final long timeToSleep;

		public TaskMulti(int max, long timeToSleep) {
			this.max = max;
			this.timeToSleep = timeToSleep;
		}

		@Override
		public String getName() {
			return null;
		}

		@Override
		public void execute() {
			if (i++ == max) {
				onDone(RESULT.OK);
			} else {
				try {
					Thread.sleep(timeToSleep);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
