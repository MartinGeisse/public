/**
 * Copyright (c) 2014 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package ${packageName};

/**
 * TODO angles measured in degrees or radians?
 */
public ${classExtensibility}class ${className}${extendsClause} {

<@field name="horizontalAngle" type="double" />
<@field name="verticalAngle" type="double" />
<@field name="rollAngle" type="double" />

<#if hasInitializingConstructor>
	/**
	 * Constructor.
	 * @param horizontalAngle the horizontalAngle coordinate
	 * @param verticalAngle the verticalAngle coordinate
	 * @param rollAngle the rollAngle coordinate
	 */
	public ${className}(double horizontalAngle, double verticalAngle, double rollAngle) {
		this.horizontalAngle = horizontalAngle;
		this.verticalAngle = verticalAngle;
		this.rollAngle = rollAngle;
	}
</#if>
<#if hasDelegatingConstructor>
	/**
	 * @param horizontalAngle the horizontalAngle coordinate
	 * @param verticalAngle the verticalAngle coordinate
	 * @param rollAngle the rollAngle coordinate
	 */
	public ${className}(double horizontalAngle, double verticalAngle, double rollAngle) {
		super(horizontalAngle, verticalAngle, rollAngle);
	}
</#if>
<@accessors name="horizontalAngle" type="double" />	
<@accessors name="verticalAngle" type="double" />
<@accessors name="rollAngle" type="double" />

}
