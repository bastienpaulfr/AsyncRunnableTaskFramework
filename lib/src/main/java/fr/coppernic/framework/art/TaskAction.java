package fr.coppernic.framework.art;

/**
 * Created on 25/11/16
 *
 * @author bastien
 */

public interface TaskAction<V> {
	void action(AsyncRunnable<V> task);
}
