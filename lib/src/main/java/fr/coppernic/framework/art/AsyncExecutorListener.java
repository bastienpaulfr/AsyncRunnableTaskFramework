package fr.coppernic.framework.art;

/**
 * Executor listener
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
