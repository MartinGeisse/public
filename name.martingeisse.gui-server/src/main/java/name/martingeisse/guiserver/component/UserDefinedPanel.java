/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.guiserver.component;

import name.martingeisse.guiserver.configuration.ConfigurationHolder;
import name.martingeisse.guiserver.template.basic.PanelReferenceConfiguration;

import org.apache.wicket.MarkupContainer;
import org.apache.wicket.markup.IMarkupCacheKeyProvider;
import org.apache.wicket.markup.IMarkupResourceStreamProvider;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.util.resource.IResourceStream;
import org.apache.wicket.util.resource.StringResourceStream;

/**
 * This component gets used for a gui:panel reference to a
 * gui:panel definition in a panel template.
 * 
 * TODO rename to ConfigurationDefinedPanel
 */
public class UserDefinedPanel extends Panel implements IMarkupCacheKeyProvider, IMarkupResourceStreamProvider {

	/**
	 * the panelReferenceConfigurationHandle
	 */
	private final int panelReferenceConfigurationHandle;

	/**
	 * the cachedPanelReferenceConfiguration
	 */
	private transient PanelReferenceConfiguration cachedPanelReferenceConfiguration;

	/**
	 * Constructor.
	 * @param id the wicket id
	 * @param panelReferenceConfiguration the panel reference configuration
	 */
	public UserDefinedPanel(String id, PanelReferenceConfiguration panelReferenceConfiguration) {
		super(id);
		this.panelReferenceConfigurationHandle = panelReferenceConfiguration.getSnippetHandle();
		this.cachedPanelReferenceConfiguration = panelReferenceConfiguration;
	}

	/**
	 * Getter method for the panel configuration.
	 * @return the panel configuration.
	 */
	public final PanelReferenceConfiguration getPanelReferenceConfiguration() {
		if (cachedPanelReferenceConfiguration == null) {
			cachedPanelReferenceConfiguration = (PanelReferenceConfiguration)ConfigurationHolder.needRequestUniverseConfiguration().getSnippet(panelReferenceConfigurationHandle);
		}
		return cachedPanelReferenceConfiguration;
	}

	/* (non-Javadoc)
	 * @see org.apache.wicket.markup.IMarkupCacheKeyProvider#getCacheKey(org.apache.wicket.MarkupContainer, java.lang.Class)
	 */
	@Override
	public String getCacheKey(MarkupContainer container, Class<?> containerClass) {
		if (container != this) {
			throw new IllegalArgumentException("a UserDefinedPanel cannot be used to provide a markup cache key for other components than itself");
		}
		return ConfigurationHolder.needRequestUniverseConfiguration().getSerialNumber() + ':' + getClass().getName() + ':' + getPanelReferenceConfiguration().getPanelConfiguration();
	}

	/* (non-Javadoc)
	 * @see org.apache.wicket.markup.IMarkupResourceStreamProvider#getMarkupResourceStream(org.apache.wicket.MarkupContainer, java.lang.Class)
	 */
	@Override
	public IResourceStream getMarkupResourceStream(MarkupContainer container, Class<?> containerClass) {
		if (container != this) {
			throw new IllegalArgumentException("a UserDefinedPanel cannot be used to provide a markup resource stream for other components than itself");
		}
		return new StringResourceStream(getPanelReferenceConfiguration().getPanelConfiguration().getTemplate().getWicketMarkup());
	}

}
