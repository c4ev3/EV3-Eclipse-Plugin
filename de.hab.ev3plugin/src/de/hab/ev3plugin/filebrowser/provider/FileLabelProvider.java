/**
 * Provides the icons for the different file system entries
 * 
 */
package de.hab.ev3plugin.filebrowser.provider;

import ilg.gnuarmeclipse.managedbuild.cross.Activator;

import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import de.hab.ev3plugin.filebrowser.Ev3File;

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

private static final Image refreshImage = AbstractUIPlugin
.imageDescriptorFromPlugin(Activator.PLUGIN_ID,
"icons/refresh.gif").createImage();
private static final Image shutImage = AbstractUIPlugin
.imageDescriptorFromPlugin(Activator.PLUGIN_ID,
"icons/fileshut_nav.ico").createImage();

@Override
public Image getImage(Object element) {
		Ev3File.Kind kind = Ev3File.getKind((Ev3File) element);
        switch(kind)
        {
        case REFRESH: 	   return refreshImage;
        case RESTRICTED:   return shutImage;
        case DRIVE:		   return driveImage;
        case DIRECTORY:	   return folderImage;
        case FILE:default: return fileImage;
        }
}

@Override
public String getText(Object element) {
	return ((Ev3File) element).getName();

}
}