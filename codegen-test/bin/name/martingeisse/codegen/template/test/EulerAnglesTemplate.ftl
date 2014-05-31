/**
 * Copyright (c) 2014 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package ${packageName};

import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Euler angles (actually, nautical angles), expressed as a horizontal angle (yaw),
 * vertical angle (pitch) and roll angle. All angles are expressed in degrees, not radians.
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

<@equals>
		return (horizontalAngle == other.horizontalAngle && verticalAngle == other.verticalAngle && rollAngle == other.rollAngle);
</@equals>
<@hashCode>
		return new HashCodeBuilder().append(horizontalAngle).append(verticalAngle).append(rollAngle).toHashCode();
</@hashCode>
<@toString>
		builder.append("horizontalAngle = ").append(horizontalAngle);
		builder.append(", verticalAngle = ").append(verticalAngle);
		builder.append(", rollAngle = ").append(rollAngle); 
</@toString>
<@copyFrom>
		horizontalAngle = other.getHorizontalAngle();
		verticalAngle = other.getVerticalAngle();
		rollAngle = other.getRollAngle();
</@copyFrom>

}
