package fr.coppernic.framework.art;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import java.lang.ref.WeakReference;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Base implementation of an Async task
 */
public abstract class AsyncRunnableTask<V> implements AsyncRunnable<V>, Handler.Callback {

	private static final String TAG = "AsyncRunnableTask";
	private static final boolean DEBUG = false;
	private final Object mutex = new Object();
	private Handler handler = null;
	private AsyncRunnableListener<V> listener = null;
	private boolean firstExecution = true;
	private Timer timer = null;
	private Timeout<V> timeoutTask = null;
	private final AtomicReference<State> mState = new AtomicReference<>(State.IDLE);

	/* ********** AsyncRunnable ********** */

	@Override
	public void setListener(AsyncRunnableListener<V> listener) {
		this.listener = listener;
	}

	@Override
	public synchronized void onDone(final V param) {
		if (mState.get() == State.CANCELLED) {
			onCancel(param);
		} else if (mState.get() != State.DONE) {
			after();
			checkState(new State[]{State.RUNNING, State.PENDING}, State.DONE);
			if(isHandlerAlive()) {
				handler.post(new Runnable() {
					@Override
					public void run() {
						listener.onDone(AsyncRunnableTask.this, param);
					}
				});
			}
		}
	}

	@Override
	public synchronized void onCancel(final V param) {
		if (mState.get() != State.DONE) {
			after();
			checkState(new State[]{State.RUNNING, State.PENDING, State.CANCELLED}, State.DONE);
			if(isHandlerAlive()) {
				handler.post(new Runnable() {
					@Override
					public void run() {
						listener.onCancel(AsyncRunnableTask.this, param);
					}
				});
			}
		}
	}

	@Override
	public void onTimeout(ITimeout<V> timeout) {
		onDone(timeout.getParam());
	}

	/**
	 * @return true if task can run, false otherwise
	 */
	@Override
	public synchronized boolean canRun() {
		State state = mState.get();
		return state == State.IDLE || state == State.PENDING;
	}

	@Override
	public State getState() {
		return mState.get();
	}

	@Override
	public synchronized void clearWatchdog() {
		if (DEBUG) {
			Log.d(TAG, "clearWatchdog");
		}
		if (timeoutTask != null) {
			timeoutTask.cancel();
			timeoutTask = null;
		}
		if (timer != null) {
			timer.cancel();
			timer.purge();
			timer = null;
		}
	}

	@Override
	public void setWatchdog(long timeout, V param, int resId) {
		clearWatchdog();
		Log.d(TAG, "setWatchdog : " + timeout + "ms");
		if (timer == null) {
			timer = new Timer();
		}
		timeoutTask = new Timeout<>(this, param, resId);
		timer.schedule(timeoutTask, timeout);
	}

	//Do not synchronize cancel because it has to be able to cancel during actual execution.
	@Override
	public void cancel() {
		// Don't set the state if it is done
		synchronized (mutex) {
			setState(new State[]{State.PENDING, State.RUNNING, State.IDLE}, State.CANCELLED);
		}
		//FIXME check that super.cancel() was called in case or overriding
	}

	@Override
	public boolean isCancelled() {
		return getState() == State.CANCELLED;
	}

	/* ********** Runnable ********** */

	@Override
	final synchronized public void run() {
		// We are synchronizing this code with cancel because we have several mState.get() calls
		// in a row and we do not want want mState become CANCELLED between them.
		synchronized (mutex) {
			if (mState.get() == State.CANCELLED) {
				//do not execute if it was cancelled
				return;
			}
			// Do this line before check state
			firstExecution = mState.get() != State.PENDING;
			checkState(new State[]{State.IDLE, State.PENDING}, State.RUNNING);
		}

		if (firstExecution) {
			before();
		}
		this.execute();

		if (mState.get() != State.DONE) {
			mState.set(State.PENDING);
		}
	}

	/* ********** Handler.Callback ********** */

	@Override
	public boolean handleMessage(Message msg) {
		return false;
	}

	/* ********** methods ********** */

	protected Handler getHandler() {
		return handler;
	}

	/**
	 * Tell if this is the first execution of the task
	 *
	 * @return true is this is the first execution
	 */
	protected boolean isFirstExecution() {
		return firstExecution;
	}

	/**
	 * Called by onDone() to release resources.
	 */
	protected void after() {
		clearWatchdog();
	}

	/**
	 * Executed bu run()
	 */
	private void before() {
		handler = new Handler(Looper.myLooper(), this);
	}

	/**
	 * Set a state according to previous allowed state.
	 * <p>
	 * Throw RuntimeException if previous state was not correct.
	 *
	 * @param currents Previous allowed states
	 * @param nev      New state
	 */
	//Do not synchronize this method
	private void checkState(State[] currents, State nev) {
		boolean ok = false;
		for (State current : currents) {
			if (current == mState.get()) {
				ok = true;
			}
		}

		if (!ok) {
			throw new RuntimeException("Wrong mState, " + mState);
		} else {
			mState.set(nev);
		}
	}

	/**
	 * Set the new state only if previous was in the list of current states
	 *
	 * @param currents Currents states
	 * @param nev      new state
	 */
	@SuppressWarnings("SameParameterValue")
	private void setState(State[] currents, State nev) {
		boolean ok = false;
		for (State current : currents) {
			if (current == mState.get()) {
				ok = true;
			}
		}
		if (ok) {
			mState.set(nev);
		}
	}

	private boolean isHandlerAlive(){
		return handler.getLooper().getThread().isAlive();
	}

	static class Timeout<V> extends TimerTask implements ITimeout<V> {

		private final WeakReference<AsyncRunnableTask<V>> ref;
		private final V param;
		private final int resId;

		Timeout(AsyncRunnableTask<V> t, V param, int resId) {
			ref = new WeakReference<>(t);
			this.param = param;
			this.resId = resId;
		}

		public V getParam() {
			return param;
		}

		public int getResId() {
			return resId;
		}

		@Override
		public void run() {
			AsyncRunnableTask<V> t = ref.get();
			if (t != null) {
				t.onTimeout(this);
			}
		}
	}
}
