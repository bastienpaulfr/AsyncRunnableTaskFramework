/*
 * Copyright (c) 2017.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NON INFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package fr.coppernic.framework.art;

import org.junit.Before;
import org.junit.Test;

import fr.coppernic.framework.robolectric.RobolectricTest;
import fr.coppernic.framework.utils.core.CpcResult.RESULT;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Created on 16/06/16
 *
 * @author Bastien Paul
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
		TaskMulti task = new TaskMulti(2, 10);
		task.setListener(new Listener());
		assertThat(task.getState(), is(AsyncRunnable.State.IDLE));
		assertThat(task.canRun(), is(true));
		task.run();
		assertThat(task.getState(), is(AsyncRunnable.State.PENDING));
		assertThat(task.canRun(), is(true));
		task.execute();
		assertThat(task.canRun(), is(false));
		assertThat(task.getState(), is(AsyncRunnable.State.DONE));
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

		private final int max;
		private final long timeToSleep;
		private int i = 1;

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
