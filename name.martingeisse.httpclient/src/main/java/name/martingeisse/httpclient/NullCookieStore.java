/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.httpclient;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.client.CookieStore;
import org.apache.http.cookie.Cookie;

/**
 * A {@link CookieStore} implementation that does not store any cookies.
 */
public final class NullCookieStore implements CookieStore {

	/* (non-Javadoc)
	 * @see org.apache.http.client.CookieStore#addCookie(org.apache.http.cookie.Cookie)
	 */
	@Override
	public void addCookie(Cookie cookie) {
	}

	/* (non-Javadoc)
	 * @see org.apache.http.client.CookieStore#clear()
	 */
	@Override
	public void clear() {
	}

	/* (non-Javadoc)
	 * @see org.apache.http.client.CookieStore#clearExpired(java.util.Date)
	 */
	@Override
	public boolean clearExpired(Date date) {
		return false;
	}

	/* (non-Javadoc)
	 * @see org.apache.http.client.CookieStore#getCookies()
	 */
	@Override
	public List<Cookie> getCookies() {
		return new ArrayList<>();
	}
	
}
