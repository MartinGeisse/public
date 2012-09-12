/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.readonly;

import java.sql.Types;

import name.martingeisse.admin.application.IPlugin;
import name.martingeisse.admin.entity.schema.type.ISqlTypeInfo;
import name.martingeisse.admin.entity.schema.type.UnknownSqlTypeInfo;

/**
 * This contributor provides basic rendering of entity properties.
 * It is the most basic renderer short of the absolute fallback
 * (null-safe toString()).
 * 
 * This contributor has score 0.
 * 
 * This contributor also implements {@link IPlugin} to conveniently allow
 * adding it outside all other plugins.
 */
public class BaselineReadOnlyRendererContributor implements IPropertyReadOnlyRendererContributor, IPlugin {

	/* (non-Javadoc)
	 * @see name.martingeisse.admin.application.IPlugin#unbox()
	 */
	@Override
	public IPlugin[] unbox() {
		return null;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.admin.application.IPlugin#contribute(name.martingeisse.admin.application.capabilities.ApplicationCapabilities)
	 */
	@Override
	public void contribute() {
		ReadOnlyRenderingConfigurationUtil.addPropertyReadOnlyRendererContributor(this);
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.admin.readonly.IPropertyReadOnlyRendererContributor#getScore()
	 */
	@Override
	public int getScore() {
		return 0;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.admin.readonly.IPropertyReadOnlyRendererContributor#getRenderer(int)
	 */
	@Override
	public IPropertyReadOnlyRenderer getRenderer(final ISqlTypeInfo type) {
		if (type instanceof UnknownSqlTypeInfo) {
			final UnknownSqlTypeInfo unknownSqlType = (UnknownSqlTypeInfo)type;
			final int typeCode = unknownSqlType.getSqlTypeCode();
			if (typeCode == Types.BOOLEAN || typeCode == Types.BIT) {
				return BooleanRenderer.INSTANCE;
			}
		}
		return null;
	}

}
