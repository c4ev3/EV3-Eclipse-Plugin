package de.hab.ev3plugin.filebrowser;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IOpenListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.OpenEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;

import de.hab.ev3plugin.Ev3Duder;
import de.hab.ev3plugin.filebrowser.provider.FileContentProvider;
import de.hab.ev3plugin.filebrowser.provider.FileLabelProvider;
import de.hab.ev3plugin.util.Pair;
import de.hab.ev3plugin.filebrowser.Ev3File;

/* This is responsible for the root directory and
 * the action happending when clicking
 */

public class View extends ViewPart {
	public static final String ID = "de.hab.ev3plugin.filebrowser.view";
	private TreeViewer viewer;
	public void createPartControl(Composite parent) {
		Ev3Duder ev3 = new Ev3Duder("/Users/a3f/prjs/ev3duder/ev3duder", null);
		viewer = new TreeViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
		viewer.setContentProvider(new FileContentProvider());
		viewer.setLabelProvider(new FileLabelProvider());
		viewer.setInput(Ev3File.listRoots(ev3));
		viewer.addOpenListener(new IOpenListener() {
			@Override
			public void open(OpenEvent event) {
				IStructuredSelection selection = (IStructuredSelection) event
						.getSelection();
				//if (((Pair<Boolean, Object>)selection.getFirstElement()).left == false)
				Ev3File file = (Ev3File)selection.getFirstElement();
				if (file.getName().equals("- click to refresh"))
					{
		Ev3Duder ev3 = new Ev3Duder("/Users/a3f/prjs/ev3duder/ev3duder", null);
				viewer.setInput(Ev3File.listRoots(ev3));
					viewer.refresh();}
				else
					MessageDialog.openInformation(null, "Ev3File", 
						"info here later"
							);
							}
		});
	}

	public void setFocus() {
		viewer.getControl().setFocus();
	}
}