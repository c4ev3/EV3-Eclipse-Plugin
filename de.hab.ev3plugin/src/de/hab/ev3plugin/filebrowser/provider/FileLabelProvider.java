package de.hab.ev3plugin.filebrowser.provider;

import ilg.gnuarmeclipse.managedbuild.cross.Activator;

import java.io.File;

import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.plugin.AbstractUIPlugin;

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
File file = (File) element;
if (file.isDirectory())
return file.getParent() != null ? folderImage : driveImage;
return fileImage;
}

@Override
public String getText(Object element) {
String fileName = ((File) element).getName();
if (fileName.length() > 0) {
return fileName;
}
return ((File) element).getPath();
}
}