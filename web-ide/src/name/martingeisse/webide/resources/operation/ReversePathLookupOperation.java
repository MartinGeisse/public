/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.webide.resources.operation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import name.martingeisse.common.database.EntityConnectionManager;
import name.martingeisse.webide.entity.QWorkspaceResources;
import name.martingeisse.webide.entity.WorkspaceResources;
import name.martingeisse.webide.resources.ResourcePath;
import name.martingeisse.webide.resources.ResourceType;

import com.mysema.query.sql.SQLQuery;

/**
 * This is an internal operation that takes one or more resources
 * (using their IDs) and finds their paths in the workspace. This is
 * a "reverse" lookup in the sense that all normal code should start
 * with the path, and is used for the special case that user code
 * starts with one or more markers (which store the resource IDs,
 * not paths).
 * 
 * This operation produces a map (resource ID -> path) for the
 * resources specified at construction, as well as all ancestor
 * resources.
 */
class ReversePathLookupOperation extends WorkspaceOperation {

	/**
	 * the originIds
	 */
	private Collection<Long> originIds;
	
	/**
	 * the resourcesById
	 */
	private Map<Long, WorkspaceResources> resourcesById;
	
	/**
	 * the result
	 */
	private Map<Long, ResourcePath> result;

	/**
	 * Constructor.
	 */
	public ReversePathLookupOperation() {
	}

	/**
	 * Constructor.
	 * @param originIds the IDs of the resources to find the paths for
	 */
	public ReversePathLookupOperation(final Collection<Long> originIds) {
		this.originIds = originIds;
	}

	/**
	 * Constructor.
	 * @param originIds the IDs of the resources to find the paths for
	 */
	public ReversePathLookupOperation(final long... originIds) {
		setOriginIds(originIds);
	}

	/**
	 * Getter method for the originIds.
	 * @return the originIds
	 */
	public Collection<Long> getOriginIds() {
		return originIds;
	}

	/**
	 * Setter method for the originIds.
	 * @param originIds the originIds to set
	 */
	public void setOriginIds(final Collection<Long> originIds) {
		this.originIds = originIds;
	}

	/**
	 * Setter method for the originIds.
	 * @param originIds the originIds to set
	 */
	public void setOriginIds(final long... originIds) {
		this.originIds = new ArrayList<Long>();
		for (final long originId : originIds) {
			this.originIds.add(originId);
		}
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.webide.resources.operation.WorkspaceOperation#perform(name.martingeisse.webide.resources.operation.IWorkspaceOperationContext)
	 */
	@Override
	protected void perform(final IWorkspaceOperationContext context) {
		collectResources();
		buildPaths();
	}
	
	/**
	 * Fetches the origin resources as well as all their ancestor
	 * resources and stores them in the resourcesById map.
	 */
	private void collectResources() {
		this.resourcesById = new HashMap<Long, WorkspaceResources>();
		Collection<Long> missingResourceIds = new ArrayList<Long>(originIds);
		while (!missingResourceIds.isEmpty()) {
			List<WorkspaceResources> resources = fetchResources(missingResourceIds);
			for (WorkspaceResources resource : resources) {
				resourcesById.put(resource.getId(), resource);
			}
			missingResourceIds.clear();
			for (WorkspaceResources resource : resources) {
				if (!resource.getType().equals(ResourceType.WORKSPACE_ROOT.name())) {
					long parentId = resource.getParentId();
					if (resourcesById.get(parentId) == null) {
						missingResourceIds.add(parentId);
					}
				}
			}
		}
	}
	
	/**
	 * Fetches multiple resources by ID.
	 */
	private static List<WorkspaceResources> fetchResources(Collection<Long> ids) {
		final SQLQuery query = EntityConnectionManager.getConnection().createQuery();
		query.from(QWorkspaceResources.workspaceResources);
		query.where(QWorkspaceResources.workspaceResources.id.in(ids));
		return query.list(QWorkspaceResources.workspaceResources);
	}
	
	/**
	 * Takes the resource information from resourcesById, generates the paths,
	 * and stores them in the result map.
	 */
	private void buildPaths() {
		result = new HashMap<Long, ResourcePath>();
		for (long originId : originIds) {
			buildPath(originId);
		}
	}
	
	/**
	 * Builds the path for the specified id, stores it in the result map,
	 * and returns it. Ancestor paths are also built. Any paths that
	 * are already in the result map are just returned.
	 */
	private ResourcePath buildPath(long id) {
		ResourcePath path = result.get(id);
		if (path == null) {
			WorkspaceResources resource = resourcesById.get(id);
			if (resource == null) {
				throw new RuntimeException("internal error -- previously fetched resource not found");
			}
			if (resource.getType().equals(ResourceType.WORKSPACE_ROOT.name())) {
				path = ResourcePath.ROOT;
			} else {
				ResourcePath parentPath = buildPath(resource.getParentId());
				path = parentPath.appendSegment(resource.getName(), false);
			}
			result.put(resource.getId(), path);
		}
		return path;
	}

	/**
	 * Getter method for the result.
	 * @return the result
	 */
	public Map<Long, ResourcePath> getResult() {
		return result;
	}
	
}
