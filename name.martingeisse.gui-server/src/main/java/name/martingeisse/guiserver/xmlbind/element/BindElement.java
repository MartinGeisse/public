/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.guiserver.xmlbind.element;

import name.martingeisse.common.terms.Multiplicity;
import name.martingeisse.guiserver.xmlbind.MarkupContent;
import name.martingeisse.guiserver.xmlbind.attribute.BindAttribute;

/**
 * This annotation can be used to bind an element to a class. Parsing
 * the element means creating an instance of that class.
 * 
 * An element bound using this annotation can either accept child objects
 * or markup content, but not both.
 * 
 * The constructor used to create the instance should take the following
 * parameters in this order:
 * - all attribute values in the order defined by the 'attributes' sub-annotations
 * - if childObjectMultiplicity in {ONE, ZERO_OR_ONE}, the child object
 * - if childObjectMultiplicity in {NONZERO, ANY}, a list of child objects
 * - if acceptsMarkupContent, a parameter of type {@link MarkupContent}
 * 
 */
public @interface BindElement {

	/**
	 * @return the attribute bindings
	 */
	public BindAttribute[] attributes() default {};

	/**
	 * @return the child object multiplicity
	 */
	public Multiplicity childObjectMultiplicity() default Multiplicity.ZERO;

	/**
	 * @return the local names of all elements that may denote child objects,
	 * or the empty array to treat all special elements as child objects
	 */
	public String[] childObjectElementNameFilter() default {};
	
	/**
	 * @return whether this element accepts markup content
	 */
	public boolean acceptsMarkupContent() default false;

}
