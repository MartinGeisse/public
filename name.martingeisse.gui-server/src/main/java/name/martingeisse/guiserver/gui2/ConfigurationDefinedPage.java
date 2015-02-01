/**
 * Copyright (c) 2015 Martin Geisse
 */

package name.martingeisse.guiserver.gui2;

import name.martingeisse.guiserver.application.page.AbstractApplicationPage;
import name.martingeisse.guiserver.configurationNew.Configuration;
import name.martingeisse.guiserver.configurationNew.PageConfiguration;

import org.apache.wicket.MarkupContainer;
import org.apache.wicket.markup.IMarkupCacheKeyProvider;
import org.apache.wicket.markup.IMarkupResourceStreamProvider;
import org.apache.wicket.markup.Markup;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.resource.IResourceStream;
import org.apache.wicket.util.resource.StringResourceStream;

/**
 * The most common kind of page. The configuration defines this page using content
 * elements.
 */
public class ConfigurationDefinedPage extends AbstractApplicationPage implements IMarkupCacheKeyProvider, IMarkupResourceStreamProvider {

	/**
	 * the cachedPageConfiguration
	 */
	private transient PageConfiguration cachedPageConfiguration = null;

	/**
	 * Constructor.
	 * 
	 * @param pageParameters the page parameters
	 */
	public ConfigurationDefinedPage(PageParameters pageParameters) {
		super(pageParameters);
	}

	/**
	 * Getter method for the page configuration.
	 * @return the page configuration.
	 */
	public final PageConfiguration getPageConfiguration() {
		if (cachedPageConfiguration == null) {
			cachedPageConfiguration = resolvePageConfiguration();
		}
		return cachedPageConfiguration;
	}

	/**
	 * Getter method for the page configuration path.
	 * @return the page configuration path
	 */
	protected PageConfiguration resolvePageConfiguration() {
		String pageConfigurationPath = getPageParameters().get(PageConfiguration.CONFIGURATION_ELEMENT_PATH_PAGE_PARAMETER_NAME).toString();
		if (pageConfigurationPath == null) {
			throw new RuntimeException("page configuration path not specified in page parameters");
		}
		return Configuration.getInstance().getElement(PageConfiguration.class, pageConfigurationPath);
	}

	/* (non-Javadoc)
	 * @see org.apache.wicket.markup.IMarkupCacheKeyProvider#getCacheKey(org.apache.wicket.MarkupContainer, java.lang.Class)
	 */
	@Override
	public String getCacheKey(MarkupContainer container, Class<?> containerClass) {
		if (container != this) {
			throw new IllegalArgumentException("a ConfigurationDefinedPage cannot be used to provide a markup cache key for other components than itself");
		}
		return getClass().getName() + ':' + getPageConfiguration().getPath();
	}

	/* (non-Javadoc)
	 * @see org.apache.wicket.markup.IMarkupResourceStreamProvider#getMarkupResourceStream(org.apache.wicket.MarkupContainer, java.lang.Class)
	 */
	@Override
	public IResourceStream getMarkupResourceStream(MarkupContainer container, Class<?> containerClass) {
		if (container != this) {
			throw new IllegalArgumentException("a ConfigurationDefinedPage cannot be used to provide a markup resource stream for other components than itself");
		}
		return new StringResourceStream(getPageConfiguration().getContent().getWicketMarkup());
	}

	/* (non-Javadoc)
	 * @see org.apache.wicket.Page#onInitialize()
	 */
	@Override
	protected void onInitialize() {
		super.onInitialize();
		PageConfiguration pageConfiguration = getPageConfiguration();
		pageConfiguration.getContent().getComponents().buildAndAddComponents(this);
		// TODO add(new ContentElementRepeater("elements", pageConfiguration.getContentElements()));
	}

}
