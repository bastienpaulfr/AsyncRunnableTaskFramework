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

package fr.coppernic.framework.robolectric;

import org.junit.runners.model.InitializationError;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.manifest.AndroidManifest;
import org.robolectric.res.FileFsFile;
import org.robolectric.res.FsFile;

/**
 * More dynamic path resolution.
 * <p>
 * This workaround is only for Mac Users necessary and only if they don't use the $MODULE_DIR$
 * workaround. Follow this issue at https://code.google.com/p/android/issues/detail?id=158015
 */
public class CustomRobolectricRunner extends RobolectricGradleTestRunner {

	public CustomRobolectricRunner(Class<?> klass) throws InitializationError {
		super(klass);
	}

	protected AndroidManifest getAppManifest(Config config) {
		AndroidManifest appManifest = super.getAppManifest(config);
		FsFile androidManifestFile = appManifest.getAndroidManifestFile();

		if (androidManifestFile.exists()) {
			return appManifest;
		} else {
			AndroidManifest m = getAppManifest1(config, appManifest);
			if(m.getAndroidManifestFile().exists()){
				return m;
			} else {
				return getAppManifest2(config, appManifest);
			}
		}
	}

	AndroidManifest getAppManifest1(Config config, AndroidManifest appManifest){
		String moduleRoot = getModuleRootPath(config);
		FsFile androidManifestFile = FileFsFile.from(moduleRoot,
		                                      appManifest.getAndroidManifestFile().getPath());
		FsFile resDirectory = FileFsFile.from(moduleRoot,
		                                      appManifest.getAndroidManifestFile().getPath()
			                                      .replace("AndroidManifest.xml", "res"));
		FsFile assetsDirectory = FileFsFile.from(moduleRoot,
		                                         appManifest.getAndroidManifestFile().getPath()
			                                         .replace("AndroidManifest.xml", "assets"));
		return new AndroidManifest(androidManifestFile, resDirectory, assetsDirectory);
	}

	AndroidManifest getAppManifest2(Config config, AndroidManifest appManifest){
		String moduleRoot = getModuleRootPath(config);
		FsFile androidManifestFile = FileFsFile.from(moduleRoot,
		                                             appManifest.getAndroidManifestFile().getPath()
			                                             .replace("full", "aapt"));
		System.out.println("androidManifestFile : " + androidManifestFile.getPath() + ", exist : " +
		                   androidManifestFile.exists());
		FsFile resDirectory = FileFsFile.from(moduleRoot,
		                                      appManifest.getAndroidManifestFile().getPath()
			                                      .replace("AndroidManifest.xml", "res"));
		FsFile assetsDirectory = FileFsFile.from(moduleRoot,
		                                         appManifest.getAndroidManifestFile().getPath()
			                                         .replace("AndroidManifest.xml", "assets"));
		return new AndroidManifest(androidManifestFile, resDirectory, assetsDirectory);
	}


	private String getModuleRootPath(Config config) {
		String moduleRoot =
			config.constants().getResource("").toString().replace("file:", "").replace("jar:", "");
		// last index of because travis has "build" in path
		return moduleRoot.substring(0, moduleRoot.lastIndexOf("/build"));
	}

}