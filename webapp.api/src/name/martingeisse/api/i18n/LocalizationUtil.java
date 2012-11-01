/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.api.i18n;

import java.util.Locale;

import name.martingeisse.common.util.ParameterUtil;

/**
 * This class provides methods to localize texts. It stores a
 * context stack per thread and resolves localization keys with
 * respect to that stack. Each context is associated with a set
 * of .properties file, one per locale.
 */
public final class LocalizationUtil {

	/**
	 * the NO_ARGS
	 */
	private static final Object[] NO_ARGS = new Object[0];
	
	/**
	 * the contextStackContainer
	 */
	private static ThreadLocal<LocalizationContextStack> contextStackContainer = new ThreadLocal<LocalizationContextStack>();

	/**
	 * Prevent instantiation.
	 */
	private LocalizationUtil() {
	}

	/**
	 * Creates a fresh context stack for the current thread.
	 * @param locale the current thread's locale
	 */
	public static void createContextStack(final Locale locale) {
		ParameterUtil.ensureNotNull(locale, "locale");
		contextStackContainer.set(new LocalizationContextStack(locale));
	}

	/**
	 * Removes the context stack for the current thread.
	 */
	public static void removeContextStack() {
		contextStackContainer.remove();
	}

	/**
	 * Pushes the specified context on the context stack for the
	 * current thread.
	 * @param context the context to push
	 */
	public static void pushContext(final ILocalizationContext context) {
		contextStackContainer.get().push(context);
	}

	/**
	 * Pushes the specified context on the context stack for the
	 * current thread using a {@link ClassHierarchyLocalizationContext}.
	 * @param context the context to push
	 */
	public static void pushContext(final Class<?> context) {
		pushContext(new ClassHierarchyLocalizationContext(context));
	}

	/**
	 * Pops the top-of-stack context for the current thread.
	 */
	public static void popContext() {
		contextStackContainer.get().pop();

	}

	/**
	 * Localizes the specified text using the current thread's context stack.
	 * @param key the key for the text
	 * @return the localized text
	 */
	public static String localize(final String key) {
		return format(contextStackContainer.get().getLocalizationProperty(key), (Object[])null);
	}

	/**
	 * Localizes the specified text using the current thread's context stack,
	 * including the additional context.
	 * @param additionalContext a context to treat as an implicit top-of-stack
	 * @param key the key for the text
	 * @return the localized text
	 */
	public static String localize(ILocalizationContext additionalContext, final String key) {
		return format(contextStackContainer.get().getLocalizationProperty(additionalContext, key), (Object[])null);
	}

	/**
	 * Localizes the specified text using the current thread's context stack,
	 * including the additional context.
	 * @param additionalContext a context to treat as an implicit top-of-stack
	 * @param key the key for the text
	 * @return the localized text
	 */
	public static String localize(Class<?> additionalContext, final String key) {
		return format(contextStackContainer.get().getLocalizationProperty(additionalContext, key), (Object[])null);
	}

	/**
	 * Localizes the specified text using the current thread's context stack.
	 * @param key the key for the text
	 * @param arguments formatting arguments
	 * @return the localized text
	 */
	public static String localize(final String key, Object... arguments) {
		return format(contextStackContainer.get().getLocalizationProperty(key), arguments);
	}

	/**
	 * Localizes the specified text using the current thread's context stack,
	 * including the additional context.
	 * @param additionalContext a context to treat as an implicit top-of-stack
	 * @param key the key for the text
	 * @param arguments formatting arguments
	 * @return the localized text
	 */
	public static String localize(ILocalizationContext additionalContext, final String key, Object... arguments) {
		return format(contextStackContainer.get().getLocalizationProperty(additionalContext, key), arguments);
	}

	/**
	 * Localizes the specified text using the current thread's context stack,
	 * including the additional context.
	 * @param additionalContext a context to treat as an implicit top-of-stack
	 * @param key the key for the text
	 * @param arguments formatting arguments
	 * @return the localized text
	 */
	public static String localize(Class<?> additionalContext, final String key, Object... arguments) {
		return format(contextStackContainer.get().getLocalizationProperty(additionalContext, key), arguments);
	}
	
	/**
	 * Applies formatting to the specified property.
	 * 
	 * @param property the localization property
	 * @param arguments the arguments (may be null to represent an empty array)
	 * @return the localized text
	 */
	private static String format(String property, Object... arguments) {
		return String.format(contextStackContainer.get().getLocale(), property, arguments == null ? NO_ARGS : arguments);
	}
	
}
