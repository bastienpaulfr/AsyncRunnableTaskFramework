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

import android.support.annotation.Nullable;

/**
 * Implemented by {@link AsyncExecutor} to be notified when a task has finished
 *
 * @author Bastien Paul
 */
public interface AsyncRunnableListener<V> {
	/**
	 * Called when a task has finished
	 *
	 * @param task  Tasks that ends
	 * @param param Result of the task
	 */
	void onDone(AsyncRunnable<V> task, V param);

	/**
	 * Called when a task wants to cancel execution of all tasks
	 *
	 * @param task  Tasks that cancel
	 * @param param Result of task, can be null
	 */
	void onCancel(AsyncRunnable<V> task, @Nullable V param);
}
