package de.hab.ev3plugin.filebrowser.provider;

import java.io.File;





import org.eclipse.core.runtime.Status;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

import de.hab.ev3plugin.filebrowser.Ev3File;
import de.hab.ev3plugin.util.Pair;

public class FileContentProvider implements ITreeContentProvider {

	@Override
	public Object[] getChildren(Object parent) {
		@SuppressWarnings("unchecked")
		Pair<File, Ev3File> file = (Pair<File, Ev3File>) parent;
		return file.left != null ? new >file.left.listFiles() : file.right.listFiles();
	}

	public Object[] getElements(Object inputElement) {
		return (Object[]) inputElement;
	}

	@Override
	public Object getParent(Object element) {
		@SuppressWarnings("unchecked")
		Pair<File, Ev3File> file = (Pair<File, Ev3File>) element;
		return file.left != null ? file.left.getParentFile() : file.right.getParentFile();
	}

	@Override
	public boolean hasChildren(Object parent) {
		@SuppressWarnings("unchecked")
		Pair<File, Ev3File> file = (Pair<File, Ev3File>) parent;
		return file.left != null ? file.left.isDirectory() : file.right.isDirectory();
	}

	@Override
	public void dispose() {

	}

	@Override
	public void inputChanged(Viewer arg0, Object arg1, Object arg2) {
		//Activator.log("inputChanged: ");

	}

}