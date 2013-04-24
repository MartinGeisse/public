/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.webide.document;

import java.util.concurrent.ConcurrentHashMap;

import name.martingeisse.common.util.ParameterUtil;
import name.martingeisse.webide.resources.ResourceHandle;
import name.martingeisse.webide.resources.ResourcePath;

import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * This class allows global access to shared documents. Each document
 * is identified by a workspace resource (workspace ID and resource
 * path) and a document type (identified by the class that implements
 * the document body, i.e. the concrete {@link IDocumentBody} subclass.
 * 
 * The latter implies that loading the same workspace resource using
 * different document types yields different documents that happen to
 * be backed by the same workspace resource, whereas loading the same
 * workspace resource twice using the same document type returns the
 * same resource twice, creating it in the first call.
 */
public class DocumentHub {

	/**
	 * the documents
	 */
	private static final ConcurrentHashMap<Key, Document> documents = new ConcurrentHashMap<Key, Document>();
	
	/**
	 * Returns the shared document for the specified key. The document
	 * is loaded if it does not exist in the document hub yet.
	 * 
	 * @param key the document key
	 * @return the document
	 */
	public static Document getDocument(Key key) {
		
		// if a document already exists, we want to skip the complex synchronization
		// procedure needed for newly built document objects
		final Document existingDocument = documents.get(key);
		if (existingDocument != null) {
			return existingDocument;
		}
		
		// in case a new document is built, we need to lock it until fully created
		final Document newDocument = new Document(key.getResourceHandle(), key.getDocumentType());
		synchronized(newDocument) {
			
			// insert the document, so the next client will see it, and check if another
			// client inserted a document in the meantime
			final Document previousDocument = documents.putIfAbsent(key, newDocument);
			if (previousDocument != null) {
				return previousDocument;
			}
			
			// the document is inserted and locked, so initialize and load it now
			newDocument.initializeBody(key);
			newDocument.load();
			
		}
		
		return newDocument;
	}
	
	/**
	 * Prevent instantiation.
	 */
	private DocumentHub() {
	}

	/**
	 * Used to identify documents in the document hub.
	 */
	public static final class Key {
		
		/**
		 * the resourceHandle
		 */
		private final ResourceHandle resourceHandle;
		
		/**
		 * the documentType
		 */
		private final Class<? extends IDocumentBody> documentType;

		/**
		 * Constructor.
		 * @param workspaceId the workspace ID
		 * @param resourcePath the resource path
		 * @param documentType the document type
		 */
		public Key(long workspaceId, ResourcePath resourcePath, Class<? extends IDocumentBody> documentType) {
			this(new ResourceHandle(ParameterUtil.ensureNotNull(workspaceId, "workspaceId"), ParameterUtil.ensureNotNull(resourcePath, "resourcePath")), documentType);
		}
		
		/**
		 * Constructor.
		 * @param resourceHandle the resource handle
		 * @param documentType the document type
		 */
		public Key(ResourceHandle resourceHandle, Class<? extends IDocumentBody> documentType) {
			this.resourceHandle = ParameterUtil.ensureNotNull(resourceHandle, "resourceHandle");
			this.documentType = ParameterUtil.ensureNotNull(documentType, "documentType");
		}
		
		/* (non-Javadoc)
		 * @see java.lang.Object#hashCode()
		 */
		@Override
		public int hashCode() {
			return new HashCodeBuilder().append(resourceHandle).append(documentType).toHashCode();
		}
		
		/* (non-Javadoc)
		 * @see java.lang.Object#equals(java.lang.Object)
		 */
		@Override
		public boolean equals(Object obj) {
			if (obj instanceof Key) {
				Key other = (Key)obj;
				return (resourceHandle.equals(other.resourceHandle) && documentType.equals(other.documentType));
			}
			return false;
		}
		
		/**
		 * Getter method for the resourceHandle.
		 * @return the resourceHandle
		 */
		public ResourceHandle getResourceHandle() {
			return resourceHandle;
		}
		
		/**
		 * Getter method for the documentType.
		 * @return the documentType
		 */
		public Class<? extends IDocumentBody> getDocumentType() {
			return documentType;
		}
		
	}
}
