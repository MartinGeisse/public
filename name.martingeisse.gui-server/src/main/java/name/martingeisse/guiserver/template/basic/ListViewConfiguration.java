/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.guiserver.template.basic;

import javax.xml.stream.XMLStreamException;

import name.martingeisse.guiserver.component.model.ModelProvider;
import name.martingeisse.guiserver.configuration.ConfigurationHolder;
import name.martingeisse.guiserver.template.AbstractSingleContainerConfiguration;
import name.martingeisse.guiserver.template.ComponentGroupConfiguration;
import name.martingeisse.guiserver.template.ConfigurationAssembler;
import name.martingeisse.guiserver.template.IConfigurationSnippet;
import name.martingeisse.guiserver.template.model.NamedModelReferenceBehavior;
import name.martingeisse.guiserver.xml.builder.BindAttribute;
import name.martingeisse.guiserver.xml.builder.RegisterComponentElement;
import name.martingeisse.guiserver.xml.builder.StructuredElement;

import org.apache.wicket.Component;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;

/**
 * Creates a {@link ListView} based on a list in a model.
 */
@StructuredElement
@RegisterComponentElement(localName = "list")
public class ListViewConfiguration extends AbstractSingleContainerConfiguration implements IConfigurationSnippet {

	/**
	 * the modelReferenceSpecification
	 */
	private String modelReferenceSpecification;

	/**
	 * the itemModelName
	 */
	private String itemModelName;

	/**
	 * the snippetHandle
	 */
	private int snippetHandle;

	/**
	 * Getter method for the modelReferenceSpecification.
	 * @return the modelReferenceSpecification
	 */
	public String getModelReferenceSpecification() {
		return modelReferenceSpecification;
	}
	
	/**
	 * Setter method for the modelReferenceSpecification.
	 * @param modelReferenceSpecification the modelReferenceSpecification to set
	 */
	@BindAttribute(name = "model")
	public void setModelReferenceSpecification(String modelReferenceSpecification) {
		this.modelReferenceSpecification = modelReferenceSpecification;
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

	/* (non-Javadoc)
	 * @see name.martingeisse.guiserver.configuration.content.IConfigurationSnippet#setSnippetHandle(int)
	 */
	@Override
	public void setSnippetHandle(int handle) {
		this.snippetHandle = handle;
	}

	/**
	 * Getter method for the snippetHandle.
	 * @return the snippetHandle
	 */
	public int getSnippetHandle() {
		return snippetHandle;
	}
	
	/* (non-Javadoc)
	 * @see name.martingeisse.guiserver.template.AbstractComponentGroupConfiguration#getBaseIdPrefix()
	 */
	@Override
	protected String getBaseIdPrefix() {
		return "listview";
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.guiserver.template.AbstractSingleContainerConfiguration#assembleContainerIntro(name.martingeisse.guiserver.template.ConfigurationAssembler)
	 */
	@Override
	protected void assembleContainerIntro(ConfigurationAssembler<ComponentGroupConfiguration> assembler) throws XMLStreamException {
		writeOpeningComponentTag(assembler, "wicket:container");
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
		MyListView<Object> listView = new MyListView<Object>(getComponentId(), this);
		listView.add(new NamedModelReferenceBehavior(modelReferenceSpecification));
		return listView;
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
		private transient ListViewConfiguration cachedConfiguration;

		/**
		 * Constructor.
		 * @param id the wicket id
		 */
		public MyListView(String id, ListViewConfiguration configuration) {
			super(id);
			this.configurationHandle = configuration.getSnippetHandle();
			this.cachedConfiguration = configuration;
		}
		
		/**
		 * @return the configuration
		 */
		public ListViewConfiguration getConfiguration() {
			if (cachedConfiguration == null) {
				cachedConfiguration = (ListViewConfiguration)ConfigurationHolder.needRequestUniverseConfiguration().getSnippet(configurationHandle);
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
