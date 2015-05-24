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
		Ev3Duder ev3 = new Ev3Duder("ev3", null);
		viewer = new TreeViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
		viewer.setContentProvider(new FileContentProvider());
		viewer.setLabelProvider(new FileLabelProvider());
		viewer.setInput(new Pair<File[], Ev3File[]>(File.listRoots(), Ev3File.listRoots(ev3)));
		viewer.addOpenListener(new IOpenListener() {

			@Override
			public void open(OpenEvent event) {
				IStructuredSelection selection = (IStructuredSelection) event
						.getSelection();
				//if (((Pair<Boolean, Object>)selection.getFirstElement()).left == false)
				@SuppressWarnings("unchecked")
				Pair<File, Ev3File> pair = (Pair<File, Ev3File>)selection.getFirstElement();

				if (pair.left == null)
				{
					Ev3File file = pair.right;
					// thinks of a default action
					MessageDialog.openInformation(null, "Ev3File", 
						"info here later"
							);
				}
				else if (pair.right == null)
				{
					File file = pair.left;
                        if (Desktop.isDesktopSupported()) {
                                Desktop desktop = Desktop.getDesktop();
                                if (desktop.isSupported(Desktop.Action.OPEN)) {
                                        try {
                                                desktop.open(file);
                                        } catch (IOException e) {
                                                // DO NOTHING
                                                try {
                                                        desktop.open(new File(
                                                                        System.getProperty("user.home")));
                                                } catch (IOException e1) {
                                                        // TODO Auto-generated catch block
                                                        e1.printStackTrace();
                                                }
                                        }
                                }
                        }
				}
			}
		});
	}

	public void setFocus() {
		viewer.getControl().setFocus();
	}
}