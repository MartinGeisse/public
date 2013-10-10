/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.wicket.protocol.http;

import org.junit.Assert;
import org.junit.Test;

/**
 * 
 * @author Johan
 */
public class RequestUtilsTest extends Assert
{
	/**
	 * 
	 */
	@Test
	public void doubleDotsMiddle()
	{
		assertEquals("/a/b", RequestUtils.removeDoubleDots("/a/b/../b"));
		assertEquals("a/b", RequestUtils.removeDoubleDots("a/b/../b"));
		assertEquals("a/b/", RequestUtils.removeDoubleDots("a/b/../b/"));
	}

	/**
	 * 
	 */
	@Test
	public void doubleDotsEnd()
	{
		assertEquals("/a/b", RequestUtils.removeDoubleDots("/a/b/c/.."));
		assertEquals("a/b", RequestUtils.removeDoubleDots("a/b/c/.."));
	}

	/**
	 * 
	 */
	@Test
	public void doubleDotsStart()
	{
		assertEquals("/../a/b", RequestUtils.removeDoubleDots("/../a/b"));
		assertEquals("../a/b", RequestUtils.removeDoubleDots("../a/b"));
	}

	/**
	 * 
	 */
	@Test
	public void emptyDoubleDots()
	{
		assertEquals("", RequestUtils.removeDoubleDots(""));
	}

	/**
	 * 
	 */
	@Test
	public void oneDoubleDots()
	{
		assertEquals("..", RequestUtils.removeDoubleDots(".."));
		assertEquals("../", RequestUtils.removeDoubleDots("../"));
		assertEquals("/..", RequestUtils.removeDoubleDots("/.."));
	}

	/**
	 * 
	 */
	@Test
	public void toAbsolutePath()
	{
		assertEquals(RequestUtils.toAbsolutePath("http://aif.ru/test/test", "../blah/zzz"),
			"http://aif.ru/blah/zzz");
		assertEquals(RequestUtils.toAbsolutePath("http://aif.ru/test", "blah/zzz"),
			"http://aif.ru/blah/zzz");
		assertEquals(RequestUtils.toAbsolutePath("http://aif.ru/test/", "../blah/zzz"),
			"http://aif.ru/blah/zzz");
		assertEquals(RequestUtils.toAbsolutePath("http://aif.ru/blah/test", "zzz"),
			"http://aif.ru/blah/zzz");
		assertEquals(RequestUtils.toAbsolutePath("http://aif.ru/blah/test", "./zzz"),
			"http://aif.ru/blah/zzz");
		assertEquals(RequestUtils.toAbsolutePath("http://aif.ru/blah/test", "./"),
			"http://aif.ru/blah/");
		assertEquals(RequestUtils.toAbsolutePath("http://aif.ru/", "./"), "http://aif.ru/");

	}
}
