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

import java.util.Collection;

import fr.coppernic.framework.art.AsyncRunnable.State;
import fr.coppernic.framework.io.Disposable;

/**
 * Interface for the executor of async tasks
 *
 * @author Bastien Paul
 */
public interface AsyncExecutor<V, T extends AsyncRunnable<V>>
	extends AsyncRunnableListener<V>, Disposable {

	/**
	 * Add a task to be executed
	 *
	 * @param command task to be executed
	 */
	void add(T command);

	/**
	 * Add a collection of task to be executed.
	 *
	 * @param c Collection of tasks to be executed
	 */
	void addAll(Collection<T> c);

	/**
	 * Execute the list of tasks added previously by add() or addAll()
	 * <p>
	 * At the end of the suite {@link AsyncExecutorListener#onDone()} is called.
	 * <p>
	 * Even if there is no tasks added to the suite,
	 * {@link AsyncExecutorListener#onDone()} will be called. As if there were tasks
	 * added to the suite.
	 *
	 * @return true if execution is started, false if there is no tasks to execute.
	 */
	RetCode execute();

	/**
	 * Execute the list of tasks added previously by add() or addAll() and pause just after
	 * <p>
	 * At the end of the suite {@link AsyncExecutorListener#onDone()} is called.
	 * <p>
	 * Even if there is no tasks added to the suite,
	 * {@link AsyncExecutorListener#onDone()} will be called. As if there were tasks
	 * added to the suite.
	 *
	 * @return true if execution is started, false if there is no tasks to execute.
	 */
	RetCode executeAndPause();

	/**
	 * Execute again a currently executing task.
	 * <p/>
	 * A task may wait for external information before calling onDone() method
	 * internally. Calling this method can tell the executing task that external
	 * information is ready to handle.
	 * <p/>
	 * The internal task must be ready to execute before calling this method.
	 *
	 * @return true if the task will be executed or false if the task wasn't ready to
	 * be executed or if there is no task to execute.
	 */
	RetCode executeCurrent();

	/**
	 * @return true if the executor is currently executing tasks, false otherwise
	 */
	boolean isExecuting();

	/**
	 * Set the executor listener
	 *
	 * @param listener Listener
	 */
	void setListener(AsyncExecutorListener<V, T> listener);

	/**
	 * Execute an action for all pending tasks
	 *
	 * @param action Action
	 */
	void doActionForAllPendingTasks(TaskAction<V> action);

	/**
	 * Pause execution
	 */
	RetCode pause();

	/**
	 * resume previously paused execution
	 */
	RetCode resume();

	RetCode executeOneTask();

	State getState();

	enum RetCode {
		OK,
		ERROR,
		WRONG_STATE,
		NO_TASKS,
	}
}
