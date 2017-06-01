/*
 * PogFilenameFilter.java: GameTable is in the Public Domain.
 */


package co.tkjn.gametable.poglibrary;

import java.io.File;
import java.io.FilenameFilter;

/**
 * @author tkjn
 */
public class PogFilenameFilter implements FilenameFilter
{

    /*
     * @see java.io.FileFilter#accept(java.io.File)
     */
    public boolean accept(File dir, String name)
    {
        return (name.endsWith(".pog"));
    }

}
