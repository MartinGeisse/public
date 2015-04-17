/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.guiserver.template.bootstrap.pagination;

import javax.xml.stream.XMLStreamException;

import name.martingeisse.guiserver.component.model.ModelProvider;
import name.martingeisse.guiserver.component.pagination.BootstrapPaginationBorder;
import name.martingeisse.guiserver.component.pagination.PaginationResponseData;
import name.martingeisse.guiserver.configuration.ConfigurationHolder;
import name.martingeisse.guiserver.template.AbstractSingleContainerConfiguration;
import name.martingeisse.guiserver.template.ComponentGroupConfiguration;
import name.martingeisse.guiserver.template.ConfigurationAssembler;
import name.martingeisse.guiserver.template.IConfigurationSnippet;
import name.martingeisse.guiserver.xml.builder.BindAttribute;
import name.martingeisse.guiserver.xml.builder.RegisterComponentElement;
import name.martingeisse.guiserver.xml.builder.StructuredElement;

import org.apache.wicket.Component;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;

/**
 * A complete pagination component, including page selection controls, backend requests
 * and a list view to display the items for the current page.
 */
@StructuredElement
@RegisterComponentElement(localName = "bsPagination")
public class BootstrapPaginationConfiguration extends AbstractSingleContainerConfiguration implements IConfigurationSnippet {

	/*
	 * Implementation note: a DataView isn't really useful for us here. It provides versioning support
	 * (which we don't need as we keep the whole view stateless), and uses an IDataProvider to obtain
	 * data, which again is useless for us since the whole data is obtained separately in a single
	 * request to the backend (IDataProvider is meant to simplify using separate count / items requests).
	 * 
	 * So basically, a ListView is much more useful for us here.
	 */

	/**
	 * the backendUrl
	 */
	private String backendUrl;

	/**
	 * the itemsPerPage
	 */
	private int itemsPerPage;

	/**
	 * the itemModelName
	 */
	private String itemModelName;

	/**
	 * the snippetHandle
	 */
	private int snippetHandle;

	/**
	 * Getter method for the backendUrl.
	 * @return the backendUrl
	 */
	public String getBackendUrl() {
		return backendUrl;
	}

	/**
	 * Setter method for the backendUrl.
	 * @param backendUrl the backendUrl to set
	 */
	@BindAttribute(name = "backendUrl")
	public void setBackendUrl(String backendUrl) {
		this.backendUrl = backendUrl;
	}

	/**
	 * Getter method for the itemsPerPage.
	 * @return the itemsPerPage
	 */
	public int getItemsPerPage() {
		return itemsPerPage;
	}

	/**
	 * Setter method for the itemsPerPage.
	 * @param itemsPerPage the itemsPerPage to set
	 */
	@BindAttribute(name = "itemsPerPage")
	public void setItemsPerPage(int itemsPerPage) {
		this.itemsPerPage = itemsPerPage;
	}

	/**
	 * Getter method for the itemModelName.
	 * @return the itemModelName
	 */
	public String getItemModelName() {
		return itemModelName;
	}

	/**
	 * Setter method for the itemModelName.
	 * @param itemModelName the itemModelName to set
	 */
	@BindAttribute(name = "itemModel")
	public void setItemModelName(String itemModelName) {
		this.itemModelName = itemModelName;
	}

	/**
	 * Getter method for the snippetHandle.
	 * @return the snippetHandle
	 */
	public int getSnippetHandle() {
		return snippetHandle;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.guiserver.template.IConfigurationSnippet#setSnippetHandle(int)
	 */
	@Override
	public void setSnippetHandle(int snippetHandle) {
		this.snippetHandle = snippetHandle;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.guiserver.template.AbstractComponentGroupConfiguration#getBaseIdPrefix()
	 */
	@Override
	protected String getBaseIdPrefix() {
		return "pagination";
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.guiserver.template.AbstractSingleContainerConfiguration#assembleContainerIntro(name.martingeisse.guiserver.template.ConfigurationAssembler)
	 */
	@Override
	protected void assembleContainerIntro(ConfigurationAssembler<ComponentGroupConfiguration> assembler) throws XMLStreamException {
		assembler.getMarkupWriter().writeStartElement("wicket:container");
		assembler.getMarkupWriter().writeAttribute("wicket:id", getComponentId());
		assembler.getMarkupWriter().writeStartElement("wicket:container");
		assembler.getMarkupWriter().writeAttribute("wicket:id", "pageItems");
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.guiserver.template.AbstractSingleContainerConfiguration#assembleContainerOutro(name.martingeisse.guiserver.template.ConfigurationAssembler)
	 */
	@Override
	protected void assembleContainerOutro(ConfigurationAssembler<ComponentGroupConfiguration> assembler) throws XMLStreamException {
		assembler.getMarkupWriter().writeEndElement();
		assembler.getMarkupWriter().writeEndElement();
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.guiserver.template.AbstractSingleContainerConfiguration#buildComponent()
	 */
	@Override
	public Component buildComponent() {
		return buildContainer();
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.guiserver.template.AbstractSingleContainerConfiguration#buildContainer()
	 */
	@Override
	protected MarkupContainer buildContainer() {
		LoadableDetachableModel<PaginationResponseData> model = new LoadableDetachableModel<PaginationResponseData>() {
			@Override
			protected PaginationResponseData load() {

				// TODO send a request to the backend
				return null;

			}
		};
		BootstrapPaginationBorder border = new BootstrapPaginationBorder(getComponentId(), model);
		border.add(new MyListView<Object>("pageItems", this));
		return border;
	}

	/**
	 * Specialized list view implementation.
	 */
	private static class MyListView<T> extends ListView<T> {

		/**
		 * the configurationHandle
		 */
		private final int configurationHandle;

		/**
		 * the cachedConfiguration
		 */
		private transient BootstrapPaginationConfiguration cachedConfiguration;

		/**
		 * Constructor.
		 * @param id the wicket id
		 */
		public MyListView(String id, BootstrapPaginationConfiguration configuration) {
			super(id);
			this.configurationHandle = configuration.getSnippetHandle();
			this.cachedConfiguration = configuration;
		}

		/**
		 * @return the configuration
		 */
		public BootstrapPaginationConfiguration getConfiguration() {
			if (cachedConfiguration == null) {
				cachedConfiguration = (BootstrapPaginationConfiguration)ConfigurationHolder.needRequestUniverseConfiguration().getSnippet(configurationHandle);
			}
			return cachedConfiguration;

		}

		/* (non-Javadoc)
		 * @see org.apache.wicket.markup.html.list.ListView#newItem(int, org.apache.wicket.model.IModel)
		 */
		@Override
		protected ListItem<T> newItem(int index, IModel<T> itemModel) {
			return new MyListItem<T>(index, itemModel);
		}

		/* (non-Javadoc)
		 * @see org.apache.wicket.markup.html.list.ListView#populateItem(org.apache.wicket.markup.html.list.ListItem)
		 */
		@Override
		protected void populateItem(ListItem<T> item) {
			getConfiguration().getChildren().buildAndAddComponents(item);
		}

	}

	/**
	 * Specialized list item implementation that acts as a {@link ModelProvider}.
	 */
	private static class MyListItem<T> extends ListItem<T> implements ModelProvider {

		/**
		 * Constructor.
		 * @param index the item index
		 * @param model the item model
		 */
		public MyListItem(int index, IModel<T> model) {
			super(index, model);
		}

		/* (non-Javadoc)
		 * @see name.martingeisse.guiserver.component.model.ModelProvider#getProvidedModelName()
		 */
		@Override
		public String getProvidedModelName() {
			MyListView<?> listView = (MyListView<?>)getParent();
			return listView.getConfiguration().getItemModelName();
		}

		/* (non-Javadoc)
		 * @see name.martingeisse.guiserver.component.model.ModelProvider#getProvidedModel()
		 */
		@Override
		public IModel<?> getProvidedModel() {
			return getModel();
		}

	}

}
