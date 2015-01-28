/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.stackd.server.section.storage;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import name.martingeisse.stackd.common.geometry.ClusterSize;
import name.martingeisse.stackd.common.geometry.SectionId;
import org.apache.commons.io.IOUtils;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.cache.RemovalListener;
import com.google.common.cache.RemovalNotification;

/**
 * This class is responsible for actually storing sections in files.
 * Each file contains a "super-section" with as many sections as a
 * section has cubes.
 */
public final class FolderBasedSectionStorage extends AbstractSectionStorage {
	
	/**
	 * the storageFolder
	 */
	private final File storageFolder;
	
	/**
	 * the fileHandleCache
	 */
	private final LoadingCache<SectionId, RandomAccessFile> fileHandleCache;
	
	/**
	 * Constructor.
	 * @param clusterSize the cluster-size of sections
	 * @param storageFolder the folder that contains world storage files
	 */
	public FolderBasedSectionStorage(final ClusterSize clusterSize, final File storageFolder) {
		super(clusterSize);
		this.storageFolder = storageFolder;
		this.fileHandleCache = CacheBuilder.newBuilder().maximumSize(100).removalListener(new RemovalListener<SectionId, RandomAccessFile>() {
			@Override
			public void onRemoval(RemovalNotification<SectionId, RandomAccessFile> notification) {
				try {
					notification.getValue().close();
				} catch (IOException e) {
				}
			}
		}).build(new CacheLoader<SectionId, RandomAccessFile>() {
			@Override
			public RandomAccessFile load(SectionId superSectionId) throws Exception {
				File file = getSectionFile(superSectionId);
				
				// create an empty file if there is none yet
				if (!file.exists()) {
					final FileOutputStream fileOutputStream = new FileOutputStream(file);
					try {
						final byte[] emptyToc = new byte[16 * 16 * 16 * 3 * 4];
						fileOutputStream.write(emptyToc);
					} finally {
						fileOutputStream.close();
					}
				}
				
				return new RandomAccessFile(file, "rw");
			}
		});
	}

	/**
	 * Getter method for the storageFolder.
	 * @return the storageFolder
	 */
	public File getStorageFolder() {
		return storageFolder;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.stackd.server.section.AbstractSectionStorageBackend#loadSectionCubes0(name.martingeisse.stackd.common.geometry.SectionId[])
	 */
	@Override
	public byte[][] loadSectionCubes0(SectionId[] sectionIds) {
		byte[][] result = new byte[sectionIds.length][];
		for (int i=0; i<result.length; i++) {
			final SectionId sectionId = sectionIds[i];
			final SectionId superSectionId = getSuperSectionIdFromSectionId(sectionId);
			final int tocIndex = getSectionTocIndex(sectionId);
			final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
			try {
				loadSectionFromFile(byteArrayOutputStream, fileHandleCache.get(superSectionId), tocIndex);
			} catch (final Exception e) {
				throw new RuntimeException(e);
			}
			result[i] = byteArrayOutputStream.toByteArray();
		}
		return result;
	}
	
	/* (non-Javadoc)
	 * @see name.martingeisse.stackd.server.section.AbstractSectionStorageBackend#saveSection(java.io.InputStream, name.martingeisse.stackd.common.geometry.SectionId)
	 */
	@Override
	public void saveSectionCubes0(SectionId sectionId, InputStream in) {
		final SectionId superSectionId = getSuperSectionIdFromSectionId(sectionId);
		final int tocIndex = getSectionTocIndex(sectionId);
		try {
			saveSectionToFile(in, fileHandleCache.get(superSectionId), tocIndex);
		} catch (final Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 
	 */
	private boolean loadSectionFromFile(final OutputStream out, final RandomAccessFile access, final int tocIndex) throws IOException {

		// read the ToC entry
		access.seek(tocIndex * 12);
		final int dataStartAddress = access.readInt();
		final int dataSize = access.readInt();
		/* int dataFlags = */access.readInt();

		// handle missing sections
		if (dataStartAddress < 1) {
			return false;
		}

		// read the data
		final byte[] compressedCubeData = new byte[dataSize];
		access.seek(dataStartAddress);
		access.readFully(compressedCubeData);
		
		// write data to the stream
		out.write(compressedCubeData);
		
		return true;

	}

	/**
	 * 
	 */
	private void saveSectionToFile(final InputStream in, final RandomAccessFile access, final int tocIndex) throws IOException {

		// write the section to the end of the file
		final int dataAddress = (int)access.length();
		access.seek(dataAddress);
		final byte[] compressedCubeData = IOUtils.toByteArray(in);
		access.write(compressedCubeData);

		// update the ToC entry
		access.seek(tocIndex * 12);
		access.writeInt(dataAddress);
		access.writeInt(compressedCubeData.length);
		access.writeInt(0);

	}

	/**
	 * Obtains the super-section ID from the section ID.
	 * @param sectionId the section ID
	 * @return the super-section ID
	 */
	private SectionId getSuperSectionIdFromSectionId(final SectionId sectionId) {
		return new SectionId(sectionId.getX() >> 4, sectionId.getY() >> 4, sectionId.getZ() >> 4);
	}

	/**
	 * Returns the file for the specified super-section.
	 * 
	 * @param id the super-section ID
	 * @return the storage file
	 */
	private File getSectionFile(final SectionId superSectionId) {
		return new File(storageFolder, "sc_" + superSectionId.getX() + "_" + superSectionId.getY() + "_" + superSectionId.getZ());
	}

	/**
	 * Returns the index in the ToC of the section file that is used for
	 * the section with the specified ID.
	 * 
	 * @param sectionId the section ID
	 * @return the storage file
	 */
	private int getSectionTocIndex(final SectionId sectionId) {
		final int x = (sectionId.getX() & 15);
		final int y = (sectionId.getY() & 15);
		final int z = (sectionId.getZ() & 15);
		return (x << 8) + (y << 4) + z;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.stackd.server.section.AbstractSectionStorageBackend#saveSectionRenderModel0(name.martingeisse.stackd.common.geometry.SectionId, java.io.InputStream)
	 */
	@Override
	public void saveSectionRenderModel0(SectionId sectionId, InputStream in) {
		throw new RuntimeException("not yet implemented");
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.stackd.server.section.AbstractSectionStorageBackend#deleteSectionRenderModel0(name.martingeisse.stackd.common.geometry.SectionId)
	 */
	@Override
	public void deleteSectionRenderModel0(SectionId sectionId) {
		throw new RuntimeException("not yet implemented");
	}

}
