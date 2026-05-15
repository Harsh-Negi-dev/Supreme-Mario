package com.mario.replay;

/**
 * Interface for managing replay functionality
 */
public interface ReplayManager {
    /**
     * Start recording a new gameplay session
     */
    void startRecording();

    /**
     * Stop recording the current session
     */
    void stopRecording();

    /**
     * Save the current recording to a file
     * @param filename Name of the file (without extension)
     */
    void saveRecording(String filename);

    /**
     * Load a replay from file and prepare for playback
     * @param filename Name of the file (without extension)
     */
    void loadReplay(String filename);

    /**
     * Start playback of loaded replay
     */
    void startPlayback();

    /**
     * Stop playback
     */
    void stopPlayback();

    /**
     * Check if currently recording
     */
    boolean isRecording();

    /**
     * Check if currently playing back a replay
     */
    boolean isPlayingReplay();

    /**
     * Get the replay player instance
     */
    ReplayPlayer getReplayPlayer();

    /**
     * Get the replay recorder instance
     */
    ReplayRecorder getReplayRecorder();
}
