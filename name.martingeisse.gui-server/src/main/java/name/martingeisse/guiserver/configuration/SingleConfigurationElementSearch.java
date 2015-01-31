/**
 * Copyright (c) 2015 Martin Geisse
 */

package name.martingeisse.guiserver.configuration;

/**
 * Base class to implement a search query that returns the first matched configuration
 * element.
 *
 * @param <T> the element type to look for (other types won't be matched at all)
 */
public abstract class SingleConfigurationElementSearch<T extends ConfigurationElement> {

	/**
	 * the elementClass
	 */
	private final Class<T> elementClass;

	/**
	 * Constructor.
	 * @param elementClass the element class
	 */
	public SingleConfigurationElementSearch(Class<T> elementClass) {
		this.elementClass = elementClass;
	}

	/**
	 * Executes this search query.
	 * 
	 * @return the query result, or null if not found
	 */
	public final T execute() {
		return execute(Configuration.getInstance().getRootNamespace());
	}

	/**
	 * 
	 */
	final T execute(ConfigurationNamespace namespace) {
		for (ConfigurationElement element : namespace.getElements().values()) {
			if (elementClass.isInstance(element)) {
				T typedElement = elementClass.cast(element);
				if (checkMatch(typedElement)) {
					return typedElement;
				}
			} else if (element instanceof ConfigurationNamespace) {
				T result = execute((ConfigurationNamespace)element);
				if (result != null) {
					return result;
				}
			}
		}
		return null;
	}

	/**
	 * Checks whether the specified namespace must be searched at all. 
	 * @param namespace the namespace
	 * @return true to search that namespace, false to skip it
	 */
	public boolean mustSearchNamespace(ConfigurationNamespace namespace) {
		return true;
	}

	/**
	 * Checks if the specified element matches the search criteria.
	 * @param element the element
	 * @return true if the element matches, false to skip it
	 */
	public abstract boolean checkMatch(T element);

}
