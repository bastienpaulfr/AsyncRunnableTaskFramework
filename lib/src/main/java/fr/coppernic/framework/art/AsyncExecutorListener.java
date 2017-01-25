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

/**
 * Executor listener
 *
 * @author Bastien Paul
 */
public interface AsyncExecutorListener<V, T extends AsyncRunnable<V>> {
	/**
	 * This method is called before a task is executed
	 *
	 * @param task Task that will be executed
	 */
	void beforeTask(T task);

	/**
	 * This method is called after a task has been executed.
	 *
	 * @param task Task that has been executed
	 * @param res  Result of the task
	 */
	void afterTask(T task, V res);

	/**
	 * This method is called when the whole task set has been executed
	 */
	void onDone();

	/**
	 * The method is called when the task wants to cancel the whole suite (In case of
	 * error for instance)
	 */
	void onCancelled();

	/**
	 * This method is called when the executor is paused. Call resume or dispose at that time.
	 */
	void onPaused();
}
