/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.osm.viewer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.util.glu.GLU.gluOrtho2D;

import org.lwjgl.input.Keyboard;

import name.martingeisse.osm.data.OsmNode;
import name.martingeisse.osm.data.OsmWay;
import name.martingeisse.osm.data.OsmWorld;
import name.martingeisse.stackd.client.frame.AbstractFrameHandler;
import name.martingeisse.stackd.client.frame.BreakFrameLoopException;
import name.martingeisse.stackd.client.glworker.GlWorkUnit;
import name.martingeisse.stackd.client.glworker.GlWorkerLoop;

/**
 *
 */
public class OsmViewer extends AbstractFrameHandler {

	/**
	 * the GEO_UNIT_SCALE_X
	 */
	private static final double GEO_UNIT_SCALE_X = 0.1;

	/**
	 * the GEO_ASPECT_RATIO
	 */
	private static final double GEO_ASPECT_RATIO = (8.6177 - 8.7228) / (50.4143 - 50.4487);

	/**
	 * the ZOOM_SPEED
	 */
	private static final double ZOOM_SPEED = 1.03;

	/**
	 * the MOVE_SPEED_X
	 */
	private static final double MOVE_SPEED_X = GEO_UNIT_SCALE_X / 20.0;

	/**
	 * the MOVE_SPEED_Y
	 */
	private static final double MOVE_SPEED_Y = MOVE_SPEED_X / GEO_ASPECT_RATIO;

	/**
	 * the x
	 */
	private double x = 8.67025;

	/**
	 * the y
	 */
	private double y = 50.4315;

	/**
	 * the scale
	 */
	private double scale = GEO_UNIT_SCALE_X;

	/**
	 * the world
	 */
	private final OsmWorld world;

	/**
	 * Constructor.
	 * @param world the world to view
	 */
	public OsmViewer(OsmWorld world) {
		this.world = world;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.stackd.client.frame.AbstractFrameHandler#draw(name.martingeisse.stackd.client.glworker.GlWorkerLoop)
	 */
	@Override
	public void draw(GlWorkerLoop glWorkerLoop) {
		glWorkerLoop.schedule(new GlWorkUnit() {
			@Override
			public void execute() {

				// clear the screen
				glDepthMask(false);
				glClearColor(0, 0, 0, 1);
				glClear(GL_COLOR_BUFFER_BIT);

				// set up transformation matrices
				glMatrixMode(GL_PROJECTION);
				glLoadIdentity();
				gluOrtho2D((float)(x - scale), (float)(x + scale), (float)(y - scale / GEO_ASPECT_RATIO), (float)(y + scale / GEO_ASPECT_RATIO));
				glMatrixMode(GL_MODELVIEW);
				glLoadIdentity();

				// draw ways
				glBegin(GL_LINES);
				glColor3f(0.0f, 1.0f, 0.0f);
				for (OsmWay way : world.getWays().values()) {
					OsmNode previousNode = null;
					for (OsmNode node : way.getNodes()) {
						if (previousNode != null) {
							glVertex2d(previousNode.getLongitude(), previousNode.getLatitude());
							glVertex2d(node.getLongitude(), node.getLatitude());
						}
						previousNode = node;
					}
				}
				glEnd();

				// draw nodes
				glPointSize(2.0f);
				glBegin(GL_POINTS);
				glColor3f(1.0f, 0.0f, 0.0f);
				for (OsmNode node : world.getNodes().values()) {
					glVertex2d(node.getLongitude(), node.getLatitude());
				}
				glEnd();

			}
		});
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.stackd.client.frame.AbstractFrameHandler#handleStep()
	 */
	@Override
	public void handleStep() throws BreakFrameLoopException {
		if (Keyboard.isKeyDown(Keyboard.KEY_UP)) {
			y += MOVE_SPEED_Y * scale / GEO_UNIT_SCALE_X;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_DOWN)) {
			y -= MOVE_SPEED_Y * scale / GEO_UNIT_SCALE_X;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_LEFT)) {
			x -= MOVE_SPEED_X * scale / GEO_UNIT_SCALE_X;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) {
			x += MOVE_SPEED_X * scale / GEO_UNIT_SCALE_X;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_Q)) {
			scale = scale / ZOOM_SPEED;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_A)) {
			scale = scale * ZOOM_SPEED;
		}
	}

}
