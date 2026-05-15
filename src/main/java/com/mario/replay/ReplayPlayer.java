package com.mario.replay;

import com.google.gson.Gson;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 * Plays back recorded gameplay
 */
public class ReplayPlayer {
    private List<ReplayFrame> frames;
    private int currentFrameIndex = 0;
    private boolean isPlaying = false;
    private boolean isPaused = false;
    private float playbackSpeed = 1.0f;

    public ReplayPlayer() {
    }

    public void loadReplay(String filename) {
        try {
            String content = new String(Files.readAllBytes(Paths.get("replays/" + filename + ".json")));
            Gson gson = new Gson();
            ReplayRecorder.ReplayData replayData = gson.fromJson(content, ReplayRecorder.ReplayData.class);
            
            if (replayData != null && replayData.frames != null) {
                this.frames = replayData.frames;
                this.currentFrameIndex = 0;
                System.out.println("Replay loaded: " + filename + " (" + frames.size() + " frames)");
            }
        } catch (IOException e) {
            System.err.println("Failed to load replay: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void startPlayback() {
        if (frames != null && !frames.isEmpty()) {
            this.isPlaying = true;
            this.isPaused = false;
            this.currentFrameIndex = 0;
        }
    }

    public void stopPlayback() {
        this.isPlaying = false;
        this.isPaused = false;
        this.currentFrameIndex = 0;
    }

    public void pausePlayback() {
        this.isPaused = true;
    }

    public void resumePlayback() {
        this.isPaused = false;
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    public boolean isPaused() {
        return isPaused;
    }

    public void setPlaybackSpeed(float speed) {
        this.playbackSpeed = Math.max(0.25f, Math.min(4.0f, speed));
    }

    public float getPlaybackSpeed() {
        return playbackSpeed;
    }

    public ReplayFrame getCurrentFrame() {
        if (frames != null && currentFrameIndex >= 0 && currentFrameIndex < frames.size()) {
            return frames.get(currentFrameIndex);
        }
        return null;
    }

    public void advanceFrame() {
        if (isPlaying && !isPaused && frames != null) {
            currentFrameIndex++;
            if (currentFrameIndex >= frames.size()) {
                stopPlayback();
            }
        }
    }

    public int getCurrentFrameIndex() {
        return currentFrameIndex;
    }

    public int getTotalFrames() {
        return frames != null ? frames.size() : 0;
    }

    public float getProgress() {
        if (frames == null || frames.isEmpty()) {
            return 0.0f;
        }
        return (float) currentFrameIndex / frames.size();
    }

    public void seekToFrame(int frameIndex) {
        if (frames != null && frameIndex >= 0 && frameIndex < frames.size()) {
            this.currentFrameIndex = frameIndex;
        }
    }

    public void seekToProgress(float progress) {
        if (frames != null) {
            int frameIndex = (int) (progress * frames.size());
            seekToFrame(Math.min(frameIndex, frames.size() - 1));
        }
    }

    public List<ReplayFrame> getFrames() {
        return frames;
    }

    public boolean hasFrames() {
        return frames != null && !frames.isEmpty();
    }
}
