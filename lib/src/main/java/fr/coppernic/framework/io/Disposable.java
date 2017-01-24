package fr.coppernic.framework.io;

/**
 * Created by bastien on 16/06/16.
 */
public interface Disposable {
	/**
	 * Dispose the object. No future interactions with this object shall be done.
	 */
	void dispose();
}
