package com.galactanet.gametable;

import co.tkjn.gametable.GametableVersion;

import java.io.*;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.JarInputStream;
import java.util.jar.Manifest;

class PluginLoader
{
    private static final String PLUGIN_DIR = "/Users/derek.kaye/src/tkjnGameTable/plugins/";
    private static final String GAMETABLE_PLUGIN_ATTRIBUTE = "Gametable-Plugin";
    private static final String GAMETABLE_MIN_VERSION_ATTRIBUTE = "Gametable-min-version";

    void loadPlugins()
    {
        List<File> jarFiles = getJarFiles(getPluginDir());
        List<Manifest> manifests = getManifestsForFiles(jarFiles);
        logError("Loaded " + manifests.size() + " manifests");
        validateManifests(manifests);
        checkPluginsAreCompatible(manifests);
        logError("Have " + manifests.size() + " manifests after validation");
    }

    private String getPluginDir()
    {
        return PLUGIN_DIR;
    }

    private List<File> getJarFiles(String dirName)
    {
        File dir = new File(dirName);

        return Arrays.asList(dir.listFiles(new FilenameFilter() {
            public boolean accept(File dir1, String filename)
            { return filename.endsWith(".jar"); }
        }));
    }

    private List<Manifest> getManifestsForFiles(List<File> files)
    {
        List<Manifest> manifests = new ArrayList<>();
        for (File file : files) {
            try {
                manifests.add(getManifestForFile(file));
            } catch (IOException e) {
                files.remove(file);
                logManifestFetchError(e);
            }
        }
        return manifests;
    }

    private void logManifestFetchError(Exception e)
    {
        logError(e.getMessage());
    }

    private void logError(String message)
    {
        Log.log(Log.SYS, message);
    }

    private Manifest getManifestForFile(File file) throws IOException
    {
        InputStream stream = new FileInputStream(file);
        JarInputStream jarStream = new JarInputStream(stream);
        return jarStream.getManifest();
    }

    private void validateManifests(List<Manifest> manifests)
    {
        for (Manifest manifest : manifests) {
            if (! validateManifest(manifest)) {
                manifests.remove(manifest);
                logError("Manifest does not have required attributes");
            }
        }
    }

    private boolean validateManifest(Manifest manifest)
    {
        Attributes attributes = manifest.getMainAttributes();
        return (attributes.getValue(GAMETABLE_PLUGIN_ATTRIBUTE) != null
                && attributes.getValue(GAMETABLE_MIN_VERSION_ATTRIBUTE) != null);
    }

    private void checkPluginsAreCompatible(List<Manifest> manifests)
    {
        for (Manifest manifest : manifests) {
            if (! versionIsAcceptable(manifest)) {
                manifests.remove(manifest);
                logError("Plugin is not compatible with this version of gametable");
            }
        }
    }

    private boolean versionIsAcceptable(Manifest manifest)
    {
        String requiredVersion = manifest.getMainAttributes().getValue(GAMETABLE_MIN_VERSION_ATTRIBUTE);
        String gametableVersion = GametableVersion.API_VERSION;
        return versionCompare(requiredVersion, gametableVersion) <= 0;
    }

    /**
     * Compares two version strings.
     *
     * Use this instead of String.compareTo() for a non-lexicographical
     * comparison that works for version strings. e.g. "1.10".compareTo("1.6").
     *
     * From https://stackoverflow.com/a/6702029/3761174
     *
     * @note It does not work if "1.10" is supposed to be equal to "1.10.0".
     *
     * @param str1 a string of ordinal numbers separated by decimal points.
     * @param str2 a string of ordinal numbers separated by decimal points.
     * @return The result is a negative integer if str1 is _numerically_ less than str2.
     *         The result is a positive integer if str1 is _numerically_ greater than str2.
     *         The result is zero if the strings are _numerically_ equal.
     */
    private int versionCompare(String str1, String str2) {
        String[] vals1 = str1.split("\\.");
        String[] vals2 = str2.split("\\.");
        int i = 0;
        // set index to first non-equal ordinal or length of shortest version string
        while (i < vals1.length && i < vals2.length && vals1[i].equals(vals2[i])) {
            i++;
        }
        // compare first non-equal ordinal number
        if (i < vals1.length && i < vals2.length) {
            int diff = Integer.valueOf(vals1[i]).compareTo(Integer.valueOf(vals2[i]));
            return Integer.signum(diff);
        }
        // the strings are equal or one string is a substring of the other
        // e.g. "1.2.3" = "1.2.3" or "1.2.3" < "1.2.3.4"
        return Integer.signum(vals1.length - vals2.length);
    }

    private void registerPlugin(File jarFile)
    {
        try {
            IGametablePlugin plugin = loadPluginJar(jarFile);
        } catch (Exception e) {
            logError(e.getMessage());
        }
    }

    private IGametablePlugin loadPluginJar(File file) throws ClassNotFoundException, IOException {
        JarFile jarFile = new JarFile(file, true);
        Manifest manifest = jarFile.getManifest();
        String classname = manifest.getMainAttributes().getValue(GAMETABLE_PLUGIN_ATTRIBUTE);
        URLClassLoader cl = new URLClassLoader(jarFile.toURL());
        Class myClass = cl.loadClass(classname);
        Object myClassObj = myClass.newInstance();
    }
}
