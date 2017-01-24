package fr.coppernic.framework.art;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.Log;

import java.io.InvalidObjectException;
import java.util.Collection;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicReference;

import fr.coppernic.framework.art.AsyncRunnable.State;


/**
 * Executor service
 */
public class AsyncExecutorService<V, T extends AsyncRunnable<V>>
	implements AsyncExecutor<V, T>, Handler.Callback {

	private static final String TAG = "AsyncExecutorService";
	private static final boolean DEBUG = true;

	private final Handler handler;
	private final ConcurrentLinkedQueue<T> taskQueue =
		new ConcurrentLinkedQueue<>();
	//private final AtomicBoolean disposed = new AtomicBoolean(false);
	private final AtomicReference<State> mState = new AtomicReference<>(State.IDLE);
	private T current = null;
	private AsyncExecutorListener<V, T> listener = new DummyListener();

	public AsyncExecutorService(HandlerThread handlerThread) {
		if (!handlerThread.isAlive()) {
			handlerThread.start();
		}
		handler = new Handler(handlerThread.getLooper(), this);
	}

	/* ------------------- AsyncExecutor ------------------- */

	@Override
	public void add(T command) {
		taskQueue.add(command);
	}

	@Override
	public void addAll(Collection<T> c) {
		taskQueue.addAll(c);
	}

	@Override
	public synchronized boolean isExecuting() {
		return current != null;
	}

	@Override
	public void setListener(AsyncExecutorListener<V, T> listener) {
		this.listener = listener;
	}

	/**
	 * Execute a list of tasks
	 * <p/>
	 * <p> We don't know the thread from which this method is called
	 * <p>
	 * <p> At the end of the suite {@link AsyncExecutorListener#onDone()} is called.
	 * <p>
	 * <p> Even if there is no tasks added to the suite,
	 * {@link AsyncExecutorListener#onDone()} will be called. As if there were tasks
	 * added to the suite.
	 *
	 * @return true if execution is started, false if there is no tasks to execute.
	 */
	@Override
	public synchronized RetCode execute() {
		if (mState.get() != State.IDLE) {
			return RetCode.WRONG_STATE;
		}
		mState.set(State.RUNNING);
		return launchExecution();
	}

	@Override
	public synchronized RetCode executeAndPause() {
		if (mState.get() != State.IDLE) {
			return RetCode.WRONG_STATE;
		}
		mState.set(State.PAUSING);
		return launchExecution();
	}

	@Override
	public synchronized RetCode executeCurrent() {
		if (mState.get() != State.RUNNING) {
			return RetCode.WRONG_STATE;
		} else if (current == null) {
			return RetCode.NO_TASKS;
		} else if (!current.canRun()) {
			return RetCode.WRONG_STATE;
		} else {
			handler.post(current);
			return RetCode.OK;
		}
	}

	/**
	 * Called by a task when this one has done.
	 * <p> We don't know the thread from which this method is called
	 *
	 * @param param result of the task
	 */
	@Override
	public synchronized void onDone(AsyncRunnable<V> task, V param) {
		logd("onDone");
		if (current == null) {
			//Task was cancelled, nothing to do
			logd("Current is null - task was cancelled");
		} else if (task != current) {
			throw new RuntimeException(new InvalidObjectException(task + " should be " + current));
		} else {
			// hook
			logd("State is " + mState.get());
			//noinspection unchecked
			listener.afterTask((T) task, param);
			switch (mState.get()) {
				case IDLE:
					//no op
					break;
				case RUNNING:
					continueTask();
					break;
				case PAUSING:
					mState.set(State.PENDING);
					listener.onPaused();
					break;
				case PENDING:
					//no op
					listener.onPaused();
					break;
				case DONE:
					//no op
					break;
				case CANCELLED:
					onCancel(task, param);
					break;
			}
		}
	}

	@Override
	public synchronized void onCancel(AsyncRunnable<V> task, V param) {
		//noinspection StatementWithEmptyBody
		if (current == null) {
			//Task was cancelled, nothing to do
		} else if (task != current) {
			throw new RuntimeException(
				new InvalidObjectException(task + " should be " + current));
		} else {
			//noinspection unchecked
			listener.afterTask((T) task, param);
			tearDown();
			listener.onCancelled();
		}
	}

	@Override
	public synchronized void doActionForAllPendingTasks(TaskAction<V> action) {
		// Execute action only if current task is not running
		if (current != null && !current.getState().equals(State.RUNNING)) {
			action.action(current);
		}
		// Execute action for all pending tasks
		for (AsyncRunnable<V> task : taskQueue) {
			action.action(task);
		}
	}

	@Override
	public synchronized RetCode pause() {
		if (mState.get() != State.RUNNING) {
			return RetCode.WRONG_STATE;
		} else {
			mState.set(State.PAUSING);
			return RetCode.OK;
		}
	}

	@Override
	public synchronized RetCode resume() {
		if (mState.get() == State.PAUSING) {
			mState.set(State.RUNNING);
			return RetCode.OK;
		} else if (mState.get() == State.PENDING) {
			mState.set(State.RUNNING);
			continueTask();
			return RetCode.OK;
		} else {
			return RetCode.WRONG_STATE;
		}
	}

	@Override
	public RetCode executeOneTask() {
		if (mState.get() != State.PENDING) {
			return RetCode.WRONG_STATE;
		} else {
			mState.set(State.PAUSING);
			continueTask();
			return RetCode.OK;
		}
	}

	@Override
	public State getState() {
		return mState.get();
	}

	/* ------------------- Handler.Callback ------------------- */

	@Override
	public boolean handleMessage(Message msg) {
		return false;
	}

	/* ------------------- Disposable ------------------- */

	/**
	 * Stops the handler thread given to the Executor !
	 * FIXME why is this synchronized ?
	 */
	@Override
	public synchronized void dispose() {
		if(mState.get() == State.CANCELLED){
			//Already cancelled;
			return;
		}

		mState.set(State.CANCELLED);
		if (isExecuting()) {
			cancel();
		}
	}

	protected synchronized T getCurrent() {
		return current;
	}

	private void initRunnableAsync(T command) {
		command.setListener(this);
	}

	private RetCode launchExecution() {
		for (T command : taskQueue) {
			initRunnableAsync(command);
		}

		current = taskQueue.poll();
		if (current != null) {
			executeTask(current);
			return RetCode.OK;
		} else {
			listener.onDone();
			return RetCode.NO_TASKS;
		}
	}

	private void executeTask(T task) {
		current = task;
		// Hook
		listener.beforeTask(current);
		// Execute task
		handler.post(current);
	}

	//FIXME why is this synchronized ?
	private synchronized void cancel() {
		taskQueue.clear();
		State previous = current.getState();
		//Cancel the current task
		current.cancel();

		//noinspection StatementWithEmptyBody
		if (previous.equals(State.RUNNING)) {
			// RUNNING : the current task is running. It will call onDone or onCancel soon !
			// We have to wait for the task to finish.
			Log.i(TAG,"Cancel : waiting for task to finish...");
		} else {
			// IDLE : task is not executed yet, but we are killing the looper before.
			// DONE : task is done, nothing to do
			// PENDING : task is waiting to be executed ! -> onDone or onCancel will be called
			// some days
			// do not process any more messages (even is there are some in pipe)
			handler.getLooper().quit();
			onCancel(current, null);
		}
	}

	private void continueTask() {
		current = taskQueue.poll();
		if (current != null) {
			executeTask(current);
		} else {
			//No more task
			tearDown();
			listener.onDone();
		}
	}

	private void tearDown() {
		taskQueue.clear();
		current = null;
		mState.set(State.DONE);
	}

	private void logd(String msg) {
		if (DEBUG) {
			Log.d(TAG, msg);
		}
	}

	private class DummyListener implements AsyncExecutorListener<V, T> {

		@Override
		public void beforeTask(T task) {
			Log.e(TAG, "No listener set");
		}

		@Override
		public void afterTask(T task, V param) {
			Log.e(TAG, "No listener set");
		}

		@Override
		public void onDone() {
			Log.e(TAG, "No listener set");
		}

		@Override
		public void onCancelled() {
			Log.e(TAG, "No listener set");
		}

		@Override
		public void onPaused() {
			Log.e(TAG, "No listener set");
		}
	}
}
