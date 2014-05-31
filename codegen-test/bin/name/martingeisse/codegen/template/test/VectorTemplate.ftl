/**
 * Copyright (c) 2014 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package ${packageName};

import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 *
 */
public ${classExtensibility}class ${className}${extendsClause} {

<@field name="x" type="${type}" />
<@field name="y" type="${type}" />
<#if three>
<@field name="z" type="${type}" />
</#if>

<#if hasInitializingConstructor>
	/**
	 * Constructor.
	 * @param x the x coordinate
	 * @param y the y coordinate
<#if three>
	 * @param z the z coordinate
</#if>
	 */
	public ${className}(${type} x, ${type} y<#if three>, ${type} z</#if>) {
		this.x = x;
		this.y = y;
<#if three>
		this.z = z;
</#if>
	}
</#if>
<#if hasDelegatingConstructor>
	/**
	 * Constructor.
	 * @param x the x coordinate
	 * @param y the y coordinate
<#if three>
	 * @param z the z coordinate
</#if>
	 */
	public ${className}(${type} x, ${type} y<#if three>, ${type} z</#if>) {
		super(x, y<#if three>, z</#if>);
	}
</#if>
<@accessors name="x" type="${type}" />	
<@accessors name="y" type="${type}" />
<#if three>
<@accessors name="z" type="${type}" />
</#if>

<@equals>
		return (x == other.x && y == other.y<#if three> && z == other.z</#if>);
</@equals>
<@hashCode>
		return new HashCodeBuilder().append(x).append(y)<#if three>.append(z)</#if>.toHashCode();
</@hashCode>
<@toString>
		builder.append("x = ").append(x);
		builder.append(", y = ").append(y);
<#if three>
		builder.append(", z = ").append(z);
</#if> 
</@toString>
<@copyFrom>
		x = other.getX();
		y = other.getY();
<#if three>
		z = other.getZ();
</#if> 
</@copyFrom>

}
