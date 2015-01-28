/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.stackd.client.sound;

import org.newdawn.slick.openal.Audio;

/**
 * Helper class to play a sound regularly, possibly under
 * specific conditions. Time is measured in milliseconds.
 * 
 * If a single step of time skips more than one interval,
 * the sound is still played only once since playing it
 * multiple times would only make it louder.
 */
public final class RegularSound {

	/**
	 * the audio
	 */
	private final Audio audio;
	
	/**
	 * the interval
	 */
	private final long interval;
	
	/**
	 * the lastPlayed
	 */
	private long lastPlayed;
	
	/**
	 * Constructor.
	 * @param audio the sound to play
	 * @param interval the time (in milliseconds) between two
	 * occurences of the sound
	 */
	public RegularSound(Audio audio, long interval) {
		this.audio = audio;
		this.interval = interval;
		this.lastPlayed = System.currentTimeMillis();
	}
	
	/**
	 * Adds the time since the last call to the internal time
	 * counters, possibly playing the sound.
	 */
	public void handleActiveTime() {
		long now = System.currentTimeMillis();
		if (now >= lastPlayed + interval) {
			lastPlayed = now;
			audio.playAsSoundEffect(1.0f, 1.0f, false);
		}
	}
	
	/**
	 * Resets the internal time counter.
	 */
	public void reset() {
		lastPlayed = System.currentTimeMillis();
	}
	
}
