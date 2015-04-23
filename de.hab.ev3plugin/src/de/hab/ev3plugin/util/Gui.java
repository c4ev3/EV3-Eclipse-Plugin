package de.hab.ev3plugin.util;

import ilg.gnuarmeclipse.managedbuild.cross.Activator;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.ISelectionService;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

public class Gui {
	public static void MsgBox(String title, String value) {
		   IWorkbench wb = PlatformUI.getWorkbench();
		   IWorkbenchWindow win = wb.getActiveWorkbenchWindow();
		   MessageDialog.openInformation(win.getShell(), title, value);
}
	public static void MsgBox(String value) {
		MsgBox("", value);
	}
	public static IProject getActiveProject() {
		ISelectionService ss = Activator.getDefault().getWorkbench().getActiveWorkbenchWindow()
				.getSelectionService();
		String projExpID = "org.eclipse.ui.navigator.ProjectExplorer";
		ISelection sel = ss.getSelection(projExpID);
		Object selectedObject = sel;
		if (sel instanceof IStructuredSelection) {
			selectedObject = ((IStructuredSelection) sel).getFirstElement();
		}
		if (selectedObject instanceof IAdaptable) {
			IResource res = (IResource) ((IAdaptable) selectedObject)
					.getAdapter(IResource.class);
			if (res!= null)
				return res.getProject();
		}
		return null;
	}
	
}
