package fr.coppernic.framework.art;

import android.support.annotation.Nullable;

/**
 * Implemented by {@link AsyncExecutor} to be notified when a task has finished
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
