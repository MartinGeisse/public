/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.stackd.common.network;

import name.martingeisse.stackd.common.geometry.AxisAlignedDirection;
import name.martingeisse.stackd.common.geometry.SectionId;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * The identifier for a section data object. This class consists of a {@link SectionId}
 * that selects the section, and a {@link SectionDataType} that selects the actual
 * object within the section.
 */
public final class SectionDataId {

	/**
	 * the sectionId
	 */
	private final SectionId sectionId;
	
	/**
	 * the type
	 */
	private final SectionDataType type;

	/**
	 * Constructor.
	 * @param sectionId the section ID
	 * @param type the data type selector
	 */
	public SectionDataId(SectionId sectionId, SectionDataType type) {
		this.sectionId = sectionId;
		this.type = type;
	}

	/**
	 * Constructor.
	 * @param identifierText the text returned by {@link #getIdentifierText()}.
	 * @throws IllegalArgumentException if the identifier text is malformed
	 */
	public SectionDataId(String identifierText) throws IllegalArgumentException {
		final String[] idTextSegments = StringUtils.split(identifierText, '_');
		if (idTextSegments.length != 4) {
			throw new IllegalArgumentException(identifierText);
		}
		try {
			int x = Integer.parseInt(idTextSegments[0]);
			int y = Integer.parseInt(idTextSegments[1]);
			int z = Integer.parseInt(idTextSegments[2]);
			type = SectionDataType.valueOf(idTextSegments[3]);
			sectionId = new SectionId(x, y, z);
		} catch (final NumberFormatException e) {
			throw new IllegalArgumentException("invalid SectionDataId: " + identifierText);
		} catch (final IllegalArgumentException e) {
			throw new IllegalArgumentException("invalid SectionDataId: " + identifierText);
		}
	}

	/**
	 * Getter method for the sectionId.
	 * @return the sectionId
	 */
	public SectionId getSectionId() {
		return sectionId;
	}
	
	/**
	 * Getter method for the type.
	 * @return the type
	 */
	public SectionDataType getType() {
		return type;
	}
	
	/**
	 * Converts this ID to a string that is unique among all IDs, i.e. that can be
	 * converted back to an ID equal to this one.
	 * @return the identifier text
	 */
	public String getIdentifierText() {
		StringBuilder builder = new StringBuilder();
		builder.append(sectionId.getX());
		builder.append('_');
		builder.append(sectionId.getY());
		builder.append('_');
		builder.append(sectionId.getZ());
		builder.append('_');
		builder.append(type.name());
		return builder.toString();
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		} else if (obj instanceof SectionDataId) {
			SectionDataId other = (SectionDataId)obj;
			return (other.type == this.type && other.sectionId.equals(this.sectionId));
		} else {
			return false;
		}
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(sectionId).append(type).toHashCode();
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "(" + sectionId.getX() + ", " + sectionId.getY() + ", " + sectionId.getZ() + ", " + type + ")";
	}

	/**
	 * Returns a {@link SectionDataId} for the same section as this one, but with
	 * the specified type.
	 * 
	 * @param otherType the type of the section data id to return
	 * @return the new section data id
	 */
	public SectionDataId getWithType(SectionDataType otherType) {
		return new SectionDataId(sectionId, otherType);
	}
	
	/**
	 * Returns the ID of the data object with the same type as this one, but in the
	 * neighboring section by stepping in the specified direction.
	 * 
	 * @param direction the direction
	 * @return the neighbor's ID
	 */
	public SectionDataId getNeighbor(AxisAlignedDirection direction) {
		return getNeighbor(direction, type);
	}
	
	/**
	 * Returns the ID of the data object with the specified type, but in the
	 * neighboring section by stepping in the specified direction.
	 * 
	 * @param direction the direction
	 * @param neighborType the type of the object to return
	 * @return the neighbor's ID
	 */
	public SectionDataId getNeighbor(AxisAlignedDirection direction, SectionDataType neighborType) {
		return new SectionDataId(sectionId.getNeighbor(direction), neighborType);
	}

}
