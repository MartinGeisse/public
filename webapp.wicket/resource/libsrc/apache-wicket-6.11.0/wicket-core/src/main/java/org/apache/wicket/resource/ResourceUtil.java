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
package org.apache.wicket.resource;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

import org.apache.wicket.WicketRuntimeException;
import org.apache.wicket.util.io.IOUtils;
import org.apache.wicket.util.resource.IResourceStream;
import org.apache.wicket.util.resource.ResourceStreamNotFoundException;

/**
 * Utilities for resources.
 * 
 * @author Jeremy Thomerson
 */
public class ResourceUtil
{

	private ResourceUtil()
	{
		// no-op
	}

	/**
	 * read string with platform default encoding from resource stream
	 * 
	 * @param resourceStream
	 * @return string read from resource stream
	 * 
	 * @see #readString(org.apache.wicket.util.resource.IResourceStream, java.nio.charset.Charset)
	 */
	public static String readString(IResourceStream resourceStream)
	{
		return readString(resourceStream, null);
	}

	/**
	 * read string with specified encoding from resource stream
	 * 
	 * @param resourceStream
	 *            string source
	 * @param charset
	 *            charset for the string encoding (use <code>null</code> for platform default)
	 * @return string read from resource stream
	 */
	public static String readString(IResourceStream resourceStream, Charset charset)
	{
		try
		{
			InputStream stream = resourceStream.getInputStream();

			try
			{
				byte[] bytes = IOUtils.toByteArray(stream);

				if (charset == null)
                                {
					charset = Charset.defaultCharset();
                                }

				return new String(bytes, charset.name());
			}
			finally
			{
				resourceStream.close();
			}
		}
		catch (IOException e)
		{
			throw new WicketRuntimeException("failed to read string from " + resourceStream, e);
		}
		catch (ResourceStreamNotFoundException e)
		{
			throw new WicketRuntimeException("failed to locate stream from " + resourceStream, e);
		}
	}
}
