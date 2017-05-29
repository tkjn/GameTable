package com.galactanet.gametable;

/**
 * All plugins should implement this interface.
 * When the gametable is ready for the plugin to start, it will call the initialise method with the gametable instance.
 */
public interface IGametablePlugin {
    void initialise(GametableFrame gametable);
    String getName();
}
