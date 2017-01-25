package fr.coppernic.framework.robolectric;

import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowLog;

import fr.coppernic.framework.art.BuildConfig;


/**
 * Base class extended by every Robolectric test in this project.
 * <p>
 *     Robolectric tests are done in a single thread !
 */
@RunWith(CustomRobolectricRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public abstract class RobolectricTest {

	@BeforeClass
	public static void beforeClass() {
		//Configure robolectric
		ShadowLog.stream = System.out;
	}

}