/**
 * Copyright (c) 2015 Martin Geisse
 */

package name.martingeisse.guiserver.component;

import name.martingeisse.guiserver.application.page.AbstractApplicationPage;
import name.martingeisse.guiserver.configuration.ConfigurationHolder;
import name.martingeisse.guiserver.configuration.element.xml.PageConfiguration;

import org.apache.wicket.MarkupContainer;
import org.apache.wicket.markup.IMarkupCacheKeyProvider;
import org.apache.wicket.markup.IMarkupResourceStreamProvider;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.resource.IResourceStream;
import org.apache.wicket.util.resource.StringResourceStream;

/**
 * Common base class for {@link ConfigurationDefinedPage} and {@link ConfigurationDefinedHomePage}.
 */
public abstract class AbstractConfigurationDefinedPage extends AbstractApplicationPage implements IMarkupCacheKeyProvider, IMarkupResourceStreamProvider {

	/**
	 * the pageConfigurationPath
	 */
	private final String pageConfigurationPath;
	
	/**
	 * the cachedPageConfiguration
	 */
	private transient PageConfiguration cachedPageConfiguration = null;

	/**
	 * Constructor.
	 * 
	 * @param pageParameters the page parameters
	 */
	public AbstractConfigurationDefinedPage(PageParameters pageParameters, String pageConfigurationPath) {
		super(pageParameters);
		
		// the implicit page parameters will be lost when re-visiting a stateful page
		// through a component URL, so we save it here
		this.pageConfigurationPath = pageConfigurationPath;
	}

	/**
	 * Getter method for the page configuration.
	 * @return the page configuration.
	 */
	public final PageConfiguration getPageConfiguration() {
		if (cachedPageConfiguration == null) {
			cachedPageConfiguration = ConfigurationHolder.needRequestUniverseConfiguration().getElement(PageConfiguration.class, pageConfigurationPath);
		}
		return cachedPageConfiguration;
	}

	/* (non-Javadoc)
	 * @see org.apache.wicket.markup.IMarkupCacheKeyProvider#getCacheKey(org.apache.wicket.MarkupContainer, java.lang.Class)
	 */
	@Override
	public String getCacheKey(MarkupContainer container, Class<?> containerClass) {
		if (container != this) {
			throw new IllegalArgumentException("a ConfigurationDefinedPage cannot be used to provide a markup cache key for other components than itself");
		}
		return ConfigurationHolder.needRequestUniverseConfiguration().getSerialNumber() + ':' + getClass().getName() + ':' + getPageConfiguration().getPath();
	}

	/* (non-Javadoc)
	 * @see org.apache.wicket.markup.IMarkupResourceStreamProvider#getMarkupResourceStream(org.apache.wicket.MarkupContainer, java.lang.Class)
	 */
	@Override
	public IResourceStream getMarkupResourceStream(MarkupContainer container, Class<?> containerClass) {
		if (container != this) {
			throw new IllegalArgumentException("a ConfigurationDefinedPage cannot be used to provide a markup resource stream for other components than itself");
		}
		return new StringResourceStream(getPageConfiguration().getTemplate().getWicketMarkup());
	}

	/* (non-Javadoc)
	 * @see org.apache.wicket.Page#onInitialize()
	 */
	@Override
	protected void onInitialize() {
		super.onInitialize();
		PageConfiguration pageConfiguration = getPageConfiguration();
		pageConfiguration.getTemplate().getComponents().buildAndAddComponents(this);
	}

}
