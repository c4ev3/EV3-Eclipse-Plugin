package de.hab.ev3plugin.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class IO {
	static public void copy(File src, File dst) throws IOException {
	    InputStream in = new FileInputStream(src);
	    OutputStream out = new FileOutputStream(dst);

	    // Transfer bytes from in to out
	    byte[] buf = new byte[1024];
	    int len;
	    while ((len = in.read(buf)) > 0) {
	        out.write(buf, 0, len);
	    }
	    in.close();
	    out.close();
	}
	public static String getNameWithoutExtension(String s) {

	    String separator = System.getProperty("file.separator");
	    String filename;

	    // Remove the path upto the filename.
	    int lastSeparatorIndex = s.lastIndexOf(separator);
	    if (lastSeparatorIndex == -1) {
	        filename = s;
	    } else {
	        filename = s.substring(lastSeparatorIndex + 1);
	    }

	    // Remove the extension.
	    int extensionIndex = filename.lastIndexOf(".");
	    if (extensionIndex == -1)
	        return filename;

	    return filename.substring(0, extensionIndex);
	}
	public static String removeExtension(String s) {


	    // Remove the extension.
	    int extensionIndex = s.lastIndexOf(".");
	    if (extensionIndex == -1)
	        return s;

	    return s.substring(0, extensionIndex);
	}
    public static String getParent(String dir) 
    { 
    	return dir.substring(0, dir.lastIndexOf("/"));
    }
    public static String getName(String dir) 
    { 
    	if (dir.equals("/"))
    		return dir;
    	
    	return dir.substring(dir.lastIndexOf("/") + 1);
    }
}
