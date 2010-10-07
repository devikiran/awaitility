/*
 * Copyright 2010 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.jayway.awaitility.core;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.*;

import org.junit.Test;

public class HamcrestToStringFilterTest {

	@Test
	public void removesIsFromToString() throws Exception {
		assertEquals("<4>", HamcrestToStringFilter.filter(is(equalTo(4))));
	}

	@Test
	public void removesNotNotFromToString() throws Exception {
		assertEquals("<4>", HamcrestToStringFilter.filter(not(not((equalTo(4))))));
	}

	@Test
	public void removesAllNotNotsFromToString() throws Exception {
		assertEquals("<4>", HamcrestToStringFilter.filter(not(not(not(not((equalTo(4))))))));
	}

	@Test
	public void removesNotNotButKeepsRemainingNotFromToString() throws Exception {
		assertEquals("not <4>", HamcrestToStringFilter.filter(not(not(not((equalTo(4)))))));
	}
}