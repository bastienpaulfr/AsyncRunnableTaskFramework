package fr.coppernic.framework.art;

/**
 * Interface for an asynchronous task
 */
public interface AsyncRunnable<V> extends Runnable {

	/**
	 * Set task listener
	 *
	 * @param listener Listener
	 */
	void setListener(AsyncRunnableListener<V> listener);

	/**
	 * Get task name
	 *
	 * @return Task name
	 */
	String getName();

	/**
	 * Get task state
	 *
	 * @return State
	 */
	State getState();

	/**
	 * @return true if task can be executed, false otherwise
	 */
	boolean canRun();

	/**
	 * This method is called by run()
	 * <p>
	 * //FIXME move in AsyncRunnableTask
	 */
	void execute();

	/**
	 * Cancel the task
	 */
	void cancel();

	/**
	 * Call this to tell that the task has ended
	 *
	 * @param param Result of the task
	 */
	void onDone(V param);

	/**
	 * Call this to tell that the task suite has to be cancelled
	 *
	 * @param param Result of the task
	 */
	void onCancel(V param);

	/**
	 * Called when watchdog is fired
	 *
	 * @param timeout ITimeout implementation containing some info
	 */
	void onTimeout(ITimeout<V> timeout);

	//Fixme Make more General

	/**
	 * Set watchdog. Clear the previous watchdog.
	 *
	 * @param timeout timeout in ms
	 * @param param   Param to transmit if the watchdog is fired
	 * @param resId   Object to transmit if the watchdog is fired
	 */
	void setWatchdog(long timeout, V param, int resId);

	/**
	 * Clear the current watchdog
	 */
	void clearWatchdog();

	/**
	 *
	 * @return true if task has been cancelled
	 */
	boolean isCancelled();

	/**
	 * State of the task
	 */
	enum State {
		/**
		 * Task has not been started yet
		 */
		IDLE,
		/**
		 * Task is running
		 */
		RUNNING,
		/**
		 * Between running and done state
		 */
		PENDING,
		/**
		 * Task has been done
		 */
		DONE,
		/**
		 * Task has been cancelled
		 */
		CANCELLED,
		PAUSING,
	}

	/**
	 * Timeout object containing some data such as param to send when watchdog is fired
	 * and a custom object.
	 *
	 * @param <V>
	 */
	interface ITimeout<V> {

		V getParam();

		int getResId();
	}
}
