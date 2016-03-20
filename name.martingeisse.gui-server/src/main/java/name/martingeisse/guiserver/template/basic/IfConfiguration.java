/**
 * Copyright (c) 2013 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.guiserver.template.basic;

import javax.xml.stream.XMLStreamException;

import name.martingeisse.common.terms.IConsumer;
import name.martingeisse.guiserver.template.AbstractComponentGroupConfiguration;
import name.martingeisse.guiserver.template.AbstractSingleContainerConfiguration;
import name.martingeisse.guiserver.template.ComponentGroupConfiguration;
import name.martingeisse.guiserver.template.ConfigurationAssembler;
import name.martingeisse.guiserver.template.MarkupContent;
import name.martingeisse.guiserver.template.model.NamedModelReferenceBehavior;
import name.martingeisse.guiserver.xml.builder.BindAttribute;
import name.martingeisse.guiserver.xml.builder.BindContent;
import name.martingeisse.guiserver.xml.builder.RegisterComponentElement;
import name.martingeisse.guiserver.xml.builder.StructuredElement;

import org.apache.wicket.Component;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.markup.html.WebMarkupContainer;

/**
 * Represents an "if" element in a template.
 * 
 * TODO support "else" branch.
 */
@StructuredElement
@RegisterComponentElement(localName = "if")
public class IfConfiguration extends AbstractComponentGroupConfiguration {

	/**
	 * the thenBranch
	 */
	private final Branch thenBranch = new Branch(this);

	/**
	 * the conditionModelReferenceSpecification
	 */
	private String conditionModelReferenceSpecification;

	/**
	 * Setter method for the markupContent.
	 * @param markupContent the markupContent to set
	 */
	@BindContent
	public void setMarkupContent(MarkupContent<ComponentGroupConfiguration> markupContent) {
		thenBranch.setMarkupContent(markupContent);
	}

	/**
	 * Getter method for the conditionModelReferenceSpecification.
	 * @return the conditionModelReferenceSpecification
	 */
	public String getConditionModelReferenceSpecification() {
		return conditionModelReferenceSpecification;
	}

	/**
	 * Setter method for the conditionModelReferenceSpecification.
	 * @param conditionModelReferenceSpecification the conditionModelReferenceSpecification to set
	 */
	@BindAttribute(name = "condition")
	public void setConditionModelReferenceSpecification(String conditionModelReferenceSpecification) {
		this.conditionModelReferenceSpecification = conditionModelReferenceSpecification;
	}
	
	/* (non-Javadoc)
	 * @see name.martingeisse.guiserver.template.AbstractComponentGroupConfiguration#assemble(name.martingeisse.guiserver.template.ConfigurationAssembler)
	 */
	@Override
	public void assemble(ConfigurationAssembler<ComponentGroupConfiguration> assembler) throws XMLStreamException {
		super.assemble(assembler);
		thenBranch.assemble(assembler);
	}
	
	/* (non-Javadoc)
	 * @see name.martingeisse.guiserver.template.ComponentGroupConfiguration#buildComponents(name.martingeisse.common.terms.IConsumer)
	 */
	@Override
	public void buildComponents(IConsumer<Component> consumer) {
		// since the "then" branch has been added as a configuration during the assembling phase, we need not call its
		// buildComponents() here -- the parent container will already do that
	}

	/**
	 * A branch in the "if" container.
	 */
	public static class Branch extends AbstractSingleContainerConfiguration {

		/**
		 * the ifConfiguration
		 */
		private final IfConfiguration ifConfiguration;

		/**
		 * Constructor.
		 * @param ifConfiguration the parent "if" configuration
		 */
		public Branch(IfConfiguration ifConfiguration) {
			this.ifConfiguration = ifConfiguration;
		}

		/* (non-Javadoc)
		 * @see name.martingeisse.guiserver.template.AbstractSingleContainerConfiguration#buildContainer()
		 */
		@Override
		protected MarkupContainer buildContainer() {
			WebMarkupContainer container = new WebMarkupContainer(getComponentId());
			container.add(new NamedModelReferenceBehavior(ifConfiguration.getConditionModelReferenceSpecification()) {
				
				/* (non-Javadoc)
				 * @see name.martingeisse.guiserver.template.model.NamedModelReferenceBehavior#onConfigure(org.apache.wicket.Component)
				 */
				@Override
				public void onConfigure(Component component) {
					super.onConfigure(component);
					component.setVisible(evaluateCondition(component));
				}
				
				/**
				 * 
				 */
				private boolean evaluateCondition(Component component) {
					Object modelObject = component.getDefaultModelObject();
					if (modelObject == null) {
						return false;
					}
					if (modelObject instanceof Boolean) {
						return ((Boolean)modelObject).booleanValue();
					}
					if (modelObject instanceof String) {
						return !((String)modelObject).isEmpty();
					}
					if (modelObject instanceof Integer) {
						return (((Integer)modelObject).intValue() == 0);
					}
					if (modelObject instanceof Long) {
						return (((Long)modelObject).longValue() == 0);
					}
					if (modelObject instanceof Number) {
						Number number = (Number)modelObject;
						double value = number.doubleValue();
						return (value == 0.0 || Double.isNaN(value));
					}
					return true;
				}
				
			});
			return container;
		}

	}

}
