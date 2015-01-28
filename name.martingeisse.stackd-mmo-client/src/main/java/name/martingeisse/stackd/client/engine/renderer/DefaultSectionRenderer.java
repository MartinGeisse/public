/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.stackd.client.engine.renderer;

import static org.lwjgl.opengl.GL11.GL_ALPHA_TEST;
import static org.lwjgl.opengl.GL11.GL_FRONT_AND_BACK;
import static org.lwjgl.opengl.GL11.GL_NOTEQUAL;
import static org.lwjgl.opengl.GL11.GL_OBJECT_LINEAR;
import static org.lwjgl.opengl.GL11.GL_OBJECT_PLANE;
import static org.lwjgl.opengl.GL11.GL_S;
import static org.lwjgl.opengl.GL11.GL_T;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_GEN_MODE;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_GEN_Q;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_GEN_R;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_GEN_S;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_GEN_T;
import static org.lwjgl.opengl.GL11.GL_VERTEX_ARRAY;
import static org.lwjgl.opengl.GL11.glAlphaFunc;
import static org.lwjgl.opengl.GL11.glColor3f;
import static org.lwjgl.opengl.GL11.glDepthMask;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glEnableClientState;
import static org.lwjgl.opengl.GL11.glPolygonMode;
import static org.lwjgl.opengl.GL11.glTexGeni;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import name.martingeisse.stackd.client.engine.FrameRenderParameters;
import name.martingeisse.stackd.client.engine.WorldWorkingSet;
import name.martingeisse.stackd.client.glworker.EnumWorkUnits;
import name.martingeisse.stackd.client.glworker.FixedSubjectsWorkUnits;
import name.martingeisse.stackd.client.glworker.GlWorkUnit;
import name.martingeisse.stackd.client.glworker.GlWorkerLoop;
import name.martingeisse.stackd.client.system.StackdTexture;
import name.martingeisse.stackd.common.geometry.AxisAlignedDirection;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.opengl.TextureImpl;

/**
 * The default implementation of {@link ISectionRenderer} that
 * supports all features, possibly at a performance cost.
 */
public class DefaultSectionRenderer implements ISectionRenderer {

	/**
	 * the glWorkerLoop
	 */
	private GlWorkerLoop glWorkerLoop;
	
	/**
	 * the wireframe
	 */
	private boolean wireframe = false;

	/**
	 * the textureCoordinateGeneration
	 */
	private boolean textureCoordinateGeneration = true;
	
	/**
	 * the texturing
	 */
	private boolean texturing = true;
	
	/**
	 * the sunlightDirectionX
	 */
	private float sunlightDirectionX;

	/**
	 * the sunlightDirectionY
	 */
	private float sunlightDirectionY;

	/**
	 * the sunlightDirectionZ
	 */
	private float sunlightDirectionZ;

	/**
	 * the sunlightAmbient
	 */
	private float sunlightAmbient;

	/**
	 * the sunlightDiffuse
	 */
	private float sunlightDiffuse;
	
	/**
	 * the auxFloatBuffer
	 */
	private final FloatBuffer auxFloatBuffer = ByteBuffer.allocateDirect(16).order(ByteOrder.nativeOrder()).asFloatBuffer();
	
	/**
	 * the preparationWorkUnit
	 */
	private final GlWorkUnit preparationWorkUnit = new GlWorkUnit() {
		@Override
		public void execute() {
			
			// enable backface culling since we don't do quick BFC for faces in the same section
			glEnable(GL11.GL_CULL_FACE);
			GL11.glCullFace(GL11.GL_BACK);
			GL11.glFrontFace(GL11.GL_CCW);
					
			// wireframe or solid polygons
			glPolygonMode(GL_FRONT_AND_BACK, wireframe ? GL11.GL_LINE : GL11.GL_FILL);
			
			// texturing or not
			if (texturing) {
				TextureImpl.unbind(); // TODO this nonsense alone is a good reason to get rid of Slick textures
				glEnable(GL_TEXTURE_2D);
			} else {
				glDisable(GL_TEXTURE_2D);
			}
			
			// texture coordinate generation
			if (textureCoordinateGeneration) {
				glEnable(GL_TEXTURE_GEN_S);
				glEnable(GL_TEXTURE_GEN_T);
				glDisable(GL_TEXTURE_GEN_Q);
				glDisable(GL_TEXTURE_GEN_R);
			}
			
			// key color support
			glEnable(GL_ALPHA_TEST);
			glAlphaFunc(GL_NOTEQUAL, 0);
			
			// various
			glDepthMask(true);
			glColor3f(1.0f, 1.0f, 1.0f);
			glEnableClientState(GL_VERTEX_ARRAY);
			
		}
	};
	
	/**
	 * the directionPreparationWorkUnits
	 */
	private final EnumWorkUnits<AxisAlignedDirection> directionPreparationWorkUnits = new EnumWorkUnits<AxisAlignedDirection>(AxisAlignedDirection.class) {
		@Override
		protected void handleSubject(AxisAlignedDirection direction) {
			
			// configure texture coordinate generation
			glTexGeni(GL_S, GL_TEXTURE_GEN_MODE, GL_OBJECT_LINEAR);
			glTexGeni(GL_T, GL_TEXTURE_GEN_MODE, GL_OBJECT_LINEAR);
			// note: T axis is reversed in OpenGL vs. image formats (+T means "up")
			if (direction.getSignY() == 0) {
				glTexGen(GL_S, direction.getSignZ() / 8f, 0, -direction.getSignX() / 8f);
				glTexGen(GL_T, 0, -1 / 8f, 0);
			} else {
				glTexGen(GL_S, 1 / 8f, 0, 0);
				glTexGen(GL_T, 0, 0, -1 / 8f);
			}
			
			// apply sunlight
			float brightness = direction.getOpposite().select(sunlightDirectionX, sunlightDirectionY, sunlightDirectionZ);
			if (brightness < 0) {
				brightness = 0;
			}
			brightness = (sunlightAmbient + sunlightDiffuse * brightness);
			glColor3f(brightness, brightness, brightness);
			
		}
	};

	/**
	 * the cleanupWorkUnit
	 */
	private final GlWorkUnit cleanupWorkUnit = new GlWorkUnit() {
		@Override
		public void execute() {
			glDisable(GL11.GL_CULL_FACE);
		}
	};

	/**
	 * the texturePreparationWorkUnits
	 */
	private FixedSubjectsWorkUnits<StackdTexture> texturePreparationWorkUnits;
	
	/**
	 * Constructor.
	 */
	public DefaultSectionRenderer() {
		this.sunlightAmbient = 0.6f;
		this.sunlightDiffuse = 0.4f;
		setSunlightDirection(2.0f, -1.0f, 0.5f);
	}
	
	/* (non-Javadoc)
	 * @see name.martingeisse.stackd.client.engine.renderer.ISectionRenderer#getGlWorkerLoop()
	 */
	@Override
	public GlWorkerLoop getGlWorkerLoop() {
		return glWorkerLoop;
	}
	
	/**
	 * Setter method for the glWorkerLoop.
	 * @param glWorkerLoop the glWorkerLoop to set
	 */
	public void setGlWorkerLoop(GlWorkerLoop glWorkerLoop) {
		this.glWorkerLoop = glWorkerLoop;
	}

	/**
	 * Getter method for the wireframe.
	 * @return the wireframe
	 */
	public boolean isWireframe() {
		return wireframe;
	}

	/**
	 * Setter method for the wireframe.
	 * @param wireframe the wireframe to set
	 */
	public void setWireframe(final boolean wireframe) {
		this.wireframe = wireframe;
	}

	/**
	 * Getter method for the textureCoordinateGeneration.
	 * @return the textureCoordinateGeneration
	 */
	public boolean isTextureCoordinateGeneration() {
		return textureCoordinateGeneration;
	}

	/**
	 * Setter method for the textureCoordinateGeneration.
	 * @param textureCoordinateGeneration the textureCoordinateGeneration to set
	 */
	public void setTextureCoordinateGeneration(final boolean textureCoordinateGeneration) {
		this.textureCoordinateGeneration = textureCoordinateGeneration;
	}

	/**
	 * Getter method for the texturing.
	 * @return the texturing
	 */
	public boolean isTexturing() {
		return texturing;
	}

	/**
	 * Setter method for the texturing.
	 * @param texturing the texturing to set
	 */
	public void setTexturing(final boolean texturing) {
		this.texturing = texturing;
	}

	/**
	 * Getter method for the sunlightDirectionX.
	 * @return the sunlightDirectionX
	 */
	public float getSunlightDirectionX() {
		return sunlightDirectionX;
	}

	/**
	 * Getter method for the sunlightDirectionY.
	 * @return the sunlightDirectionY
	 */
	public float getSunlightDirectionY() {
		return sunlightDirectionY;
	}

	/**
	 * Getter method for the sunlightDirectionZ.
	 * @return the sunlightDirectionZ
	 */
	public float getSunlightDirectionZ() {
		return sunlightDirectionZ;
	}

	/**
	 * Setter method for the sunlightDirectionZ.
	 * @param sunlightDirectionX the x component of the sunlight direction to set
	 * @param sunlightDirectionY the y component of the sunlight direction to set
	 * @param sunlightDirectionZ the z component of the sunlight direction to set
	 */
	public void setSunlightDirection(final float sunlightDirectionX, final float sunlightDirectionY, final float sunlightDirectionZ) {
		double norm = Math.sqrt(sunlightDirectionX * sunlightDirectionX + sunlightDirectionY * sunlightDirectionY + sunlightDirectionZ * sunlightDirectionZ);
		this.sunlightDirectionX = (float)(sunlightDirectionX / norm);
		this.sunlightDirectionY = (float)(sunlightDirectionY / norm);
		this.sunlightDirectionZ = (float)(sunlightDirectionZ / norm);
	}

	/**
	 * Getter method for the sunlightAmbient.
	 * @return the sunlightAmbient
	 */
	public float getSunlightAmbient() {
		return sunlightAmbient;
	}
	
	/**
	 * Setter method for the sunlightAmbient.
	 * @param sunlightAmbient the sunlightAmbient to set
	 */
	public void setSunlightAmbient(float sunlightAmbient) {
		this.sunlightAmbient = sunlightAmbient;
	}
	
	/**
	 * Getter method for the sunlightDiffuse.
	 * @return the sunlightDiffuse
	 */
	public float getSunlightDiffuse() {
		return sunlightDiffuse;
	}
	
	/**
	 * Setter method for the sunlightDiffuse.
	 * @param sunlightDiffuse the sunlightDiffuse to set
	 */
	public void setSunlightDiffuse(float sunlightDiffuse) {
		this.sunlightDiffuse = sunlightDiffuse;
	}
	
	/**
	 * Performs set-up work for the specified set of textures. The argument array must
	 * include all textures that might be passed to {@link #prepareForTexture(StackdTexture)}.
	 * @param textures the textures
	 */
	public void prepareForTextures(StackdTexture[] textures) {
		texturePreparationWorkUnits = new FixedSubjectsWorkUnits<StackdTexture>(textures) {
			@Override
			protected void handleSubject(StackdTexture texture) {
				texture.glBindTexture();
			}
		};
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.stackd.client.engine.renderer.ISectionRenderer#onBeforeRenderWorkingSet(name.martingeisse.stackd.client.engine.WorldWorkingSet, name.martingeisse.stackd.client.engine.FrameRenderParameters)
	 */
	@Override
	public void onBeforeRenderWorkingSet(final WorldWorkingSet workingSet, final FrameRenderParameters frameRenderParameters) {
		glWorkerLoop.schedule(preparationWorkUnit);
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.stackd.client.engine.renderer.ISectionRenderer#onAfterRenderWorkingSet(name.martingeisse.stackd.client.engine.WorldWorkingSet, name.martingeisse.stackd.client.engine.FrameRenderParameters)
	 */
	@Override
	public void onAfterRenderWorkingSet(final WorldWorkingSet workingSet, final FrameRenderParameters frameRenderParameters) {
		glWorkerLoop.schedule(cleanupWorkUnit);
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.stackd.client.engine.renderer.ISectionRenderer#prepareForTexture(name.martingeisse.stackd.client.system.StackdTexture)
	 */
	@Override
	public void prepareForTexture(final StackdTexture texture) {
		if (texturing) {
			if (texturePreparationWorkUnits == null) {
				throw new IllegalStateException("textures not prepared");
			}
			texturePreparationWorkUnits.schedule(glWorkerLoop, texture);
		}
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.stackd.client.engine.renderer.ISectionRenderer#prepareForDirection(name.martingeisse.stackd.common.geometry.AxisAlignedDirection)
	 */
	@Override
	public void prepareForDirection(AxisAlignedDirection direction) {
		directionPreparationWorkUnits.schedule(glWorkerLoop, direction);
	}
	
	/**
	 * 
	 */
	private void glTexGen(int axis, float x, float y, float z) {
		auxFloatBuffer.rewind();
		auxFloatBuffer.put(x);
		auxFloatBuffer.put(y);
		auxFloatBuffer.put(z);
		auxFloatBuffer.put(0);
		auxFloatBuffer.rewind();
		GL11.glTexGen(axis, GL_OBJECT_PLANE, auxFloatBuffer);
	}
	
}
