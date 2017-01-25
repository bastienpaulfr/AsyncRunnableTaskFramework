package fr.coppernic.framework.art;

import android.os.HandlerThread;

/**
 * Created by bastien on 16/06/16.
 */
public class AsyncExecutorFactory {

	public static <V, T extends AsyncRunnable<V>> AsyncExecutor<V,T> get
		(AsyncExecutorListener<V, T> listener) {
		AsyncExecutor<V,T> executor = new AsyncExecutorService<>(
			new HandlerThread("AsyncExecutorService"));
		executor.setListener(listener);
		return executor;
	}
}
