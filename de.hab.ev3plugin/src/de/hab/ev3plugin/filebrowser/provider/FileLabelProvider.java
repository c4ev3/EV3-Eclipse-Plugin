package de.hab.ev3plugin.filebrowser.provider;

import ilg.gnuarmeclipse.managedbuild.cross.Activator;

import java.io.File;

import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import de.hab.ev3plugin.filebrowser.Ev3File;
import de.hab.ev3plugin.util.Pair;

public class FileLabelProvider extends LabelProvider {
private static final Image folderImage = AbstractUIPlugin
.imageDescriptorFromPlugin(Activator.PLUGIN_ID,
"icons/folder.gif").createImage();
private static final Image driveImage = AbstractUIPlugin
.imageDescriptorFromPlugin(Activator.PLUGIN_ID,
"icons/filenav_nav.gif").createImage();
private static final Image fileImage = AbstractUIPlugin
.imageDescriptorFromPlugin(Activator.PLUGIN_ID,
"icons/file_obj.gif").createImage();

@Override
public Image getImage(Object element) {
        @SuppressWarnings("unchecked")
        Pair<File, Ev3File> pair = (Pair<File, Ev3File>) element;
        if (pair.left != null)
        {	
        	File file = pair.left;
        	if (file.isDirectory())
        		return file.getParent() != null ? folderImage : driveImage;
        	return fileImage;
        }else{
        	Ev3File file = pair.right;
        	return file.isDirectory() ? folderImage : driveImage;
        }
}

@Override
public String getText(Object element) {
      @SuppressWarnings("unchecked")
       Pair<File, Ev3File> pair = (Pair<File, Ev3File>) element;
       if (pair.left != null)
       {	
    	   File file = pair.left;
    	   String fileName = ((File) file).getName();
if (fileName.length() > 0) {
return fileName;
}
return ((File) file).getPath();
} else return pair.right.getName(); 

}
}