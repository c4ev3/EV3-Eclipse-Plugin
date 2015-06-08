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
private static final Image shutImage = AbstractUIPlugin
.imageDescriptorFromPlugin(Activator.PLUGIN_ID,
"icons/fileshut_nav.ico").createImage();

@Override
public Image getImage(Object element) {
		Ev3File file = (Ev3File) element;
        return file.isRestricted() ? shutImage : file.getParent() == null ? driveImage : file.isDirectory() ? folderImage : fileImage;
}

@Override
public String getText(Object element) {
	return ((Ev3File) element).getName();

}
}