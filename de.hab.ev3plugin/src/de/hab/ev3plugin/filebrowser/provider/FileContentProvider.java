package de.hab.ev3plugin.filebrowser.provider;

import java.io.File;


import org.eclipse.core.runtime.Status;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

public class FileContentProvider implements ITreeContentProvider {

	@Override
	public Object[] getChildren(Object parent) {
		//Activator.log("getChildren: " + parent.toString());
		
		File file = (File) parent;
		return file.listFiles();
	}

	public Object[] getElements(Object inputElement) {
		//Activator.log("getElements: " + inputElement.toString());

		return (Object[]) inputElement;
	}

	@Override
	public Object getParent(Object element) {
		//Activator.log("getParent: " + element.toString());

		File file = (File) element;
		return file.getParentFile();
	}

	@Override
	public boolean hasChildren(Object parent) {
		//Activator.log("hasChildren: " + parent.toString());

		File file = (File) parent;
		return file.isDirectory();
	}

	@Override
	public void dispose() {

	}

	@Override
	public void inputChanged(Viewer arg0, Object arg1, Object arg2) {
		//Activator.log("inputChanged: ");

	}

}