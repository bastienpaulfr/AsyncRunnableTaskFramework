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

package fr.coppernic.framework.art;

import org.junit.Test;

import fr.coppernic.framework.robolectric.RobolectricTest;
import fr.coppernic.framework.utils.core.CpcResult.RESULT;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.isA;

/**
 * Created on 16/06/16
 *
 * @author Bastien Paul
 */
public class AsyncExecutorFactoryTest extends RobolectricTest {

	@Test
	public void makeAsyncExecutorService() {
		AsyncExecutor<RESULT, AsyncRunnableTask<RESULT>> executor =
			AsyncExecutorFactory.get(null);
		assertThat(executor, isA(AsyncExecutor.class));
	}
}
