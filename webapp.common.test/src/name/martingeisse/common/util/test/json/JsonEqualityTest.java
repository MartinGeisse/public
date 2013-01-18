/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.common.util.test.json;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import name.martingeisse.test.json.IJsonEquivalence;
import name.martingeisse.test.json.JsonEquality;

import org.junit.Test;

/**
 * Test case for {@link JsonEquality}.
 */
public class JsonEqualityTest {

	/**
	 * the equality
	 */
	private IJsonEquivalence equality = new JsonEquality();
	
	/**
	 * 
	 */
	private void checkTrue(Object x, Object y) {
		assertTrue(equality.equivalent(x, y));
		assertTrue(equality.equivalent(y, x));
		equality.assertEquivalent(x, y, new LinkedList<String>());
		equality.assertEquivalent(y, x, new LinkedList<String>());
	}
	
	/**
	 * 
	 */
	private void checkFalse(Object x, Object y) {
		assertFalse(equality.equivalent(x, y));
		assertFalse(equality.equivalent(y, x));
		try {
			equality.assertEquivalent(x, y, new LinkedList<String>());
			throw new RuntimeException();
		} catch (AssertionError e) {
		}
		try {
			equality.assertEquivalent(y, x, new LinkedList<String>());
			throw new RuntimeException();
		} catch (AssertionError e) {
		}
	}
	
	/**
	 * @throws Exception ...
	 */
	@Test
	public void testPrimitiveEquivalence() throws Exception {
		List<?> list = new ArrayList<Object>();
		Map<?, ?> map = new HashMap<String, Object>();
		Object[] elements = new Object[] {null, false, true, 1, "foo", list, map};
		for (int i=0; i<elements.length; i++) {
			for (int j=0; j<elements.length; j++) {
				Object x = elements[i], y = elements[j];
				if (i == j) {
					checkTrue(x, y);
				} else {
					checkFalse(x, y);
				}
			}
		}
	}
	
	/**
	 * @throws Exception ...
	 */
	@Test
	public void testLists() throws Exception {
		List<?> list1 = Arrays.<Object>asList(1, null, "abc");
		List<?> list2 = Arrays.<Object>asList(1, null, "abc");
		List<?> list3 = Arrays.<Object>asList(1, null);
		checkTrue(list1, list2);
		checkFalse(list1, list3);
		checkFalse(list2, list3);
	}

	/**
	 * @throws Exception ...
	 */
	@Test
	public void testSublists() throws Exception {
		List<?> sublist1 = Arrays.<Object>asList(true, true);
		List<?> sublist2 = Arrays.<Object>asList(true, true);
		List<?> sublist3 = Arrays.<Object>asList(true, false);
		List<?> list1 = Arrays.<Object>asList(1, sublist1, "abc");
		List<?> list2 = Arrays.<Object>asList(1, sublist2, "abc");
		List<?> list3 = Arrays.<Object>asList(1, sublist3, "abc");
		checkTrue(list1, list2);
		checkFalse(list1, list3);
		checkFalse(list2, list3);
	}

	/**
	 * @throws Exception ...
	 */
	@Test
	public void testObjects() throws Exception {
		
		Map<String, Object> map1 = new HashMap<String, Object>();
		map1.put("aaa", 1);
		map1.put("bbb", 2);

		Map<String, Object> map1a = new HashMap<String, Object>();
		map1a.put("aaa", 1);
		map1a.put("bbb", 2);
		
		Map<String, Object> map2 = new HashMap<String, Object>();
		map2.put("aaa", 1);
		
		Map<String, Object> map3 = new HashMap<String, Object>();
		map3.put("bbb", 2);
		
		Map<String, Object> map4 = new HashMap<String, Object>();
		map4.put("aaa", 1);
		map4.put("bbb", 2);
		map4.put("ccc", 3);

		Map<String, Object> map5 = new HashMap<String, Object>();
		map5.put("aaa", 1);
		map5.put("bbb", 3);
		
		checkTrue(map1, map1);
		checkTrue(map1a, map1a);
		checkTrue(map2, map2);
		checkTrue(map3, map3);
		checkTrue(map4, map4);
		checkTrue(map5, map5);
		
		checkTrue(map1, map1a);
		checkFalse(map1, map2);
		checkFalse(map1, map3);
		checkFalse(map1, map4);
		checkFalse(map1, map5);
		checkFalse(map2, map3);
		checkFalse(map2, map4);
		checkFalse(map2, map5);
		checkFalse(map3, map4);
		checkFalse(map3, map5);
		checkFalse(map4, map5);
		
	}
	
	/**
	 * @throws Exception ...
	 */
	@Test
	public void testObjectsWithSublists() throws Exception {
		
		List<?> sublistA = Arrays.<Object>asList(true, true);
		List<?> sublistB = Arrays.<Object>asList(true, false);
		
		Map<String, Object> map1 = new HashMap<String, Object>();
		map1.put("aaa", 1);
		map1.put("bbb", 2);
		map1.put("ccc", sublistA);

		Map<String, Object> map2 = new HashMap<String, Object>();
		map2.put("aaa", 1);
		map2.put("bbb", 2);
		map2.put("ccc", sublistA);

		Map<String, Object> map3 = new HashMap<String, Object>();
		map3.put("aaa", 1);
		map3.put("bbb", 2);
		map3.put("ccc", sublistB);

		checkTrue(map1, map1);
		checkTrue(map1, map2);
		checkFalse(map1, map3);
		checkTrue(map2, map1);
		checkTrue(map2, map2);
		checkFalse(map2, map3);
		checkFalse(map3, map1);
		checkFalse(map3, map2);
		checkTrue(map3, map3);
		
	}
	
}
