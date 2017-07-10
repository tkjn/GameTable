package com.galactanet.gametable;

import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;

class PluginLoader
{
    private ServiceLoader<IGametablePlugin> foundPlugins = ServiceLoader.load(IGametablePlugin.class);
    private List<IGametablePlugin> plugins = new ArrayList<>();

    void loadPlugins()
    {
        for (IGametablePlugin plugin : foundPlugins) {
            plugins.add(plugin);
        }
    }

    List<IGametablePlugin> getPlugins()
    {
        return plugins;
    }
}
