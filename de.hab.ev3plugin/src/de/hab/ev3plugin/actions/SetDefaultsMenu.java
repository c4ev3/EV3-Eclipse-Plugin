package de.hab.ev3plugin.actions;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.ISelectionService;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;

import de.hab.ev3plugin.Activator;
import de.hab.ev3plugin.setup.Setup;
import de.hab.ev3plugin.util.Gui;

public class SetDefaultsMenu implements IWorkbenchWindowActionDelegate {

	private IWorkbenchWindow window;

	@Override
	public void run(IAction action) {

		try {
			// get project location
			// https://wiki.eclipse.org/FAQ_How_do_I_access_the_active_project%3F
			
			
			/*
			ISelectionService sel_serv = Activator.getDefault().getWorkbench()
					.getActiveWorkbenchWindow().getSelectionService();
			String projExpID = "org.eclipse.ui.navigator.ProjectExplorer";
			ISelection sel = sel_serv.getSelection(projExpID);
			IResource adapter = null;
			if (sel instanceof IStructuredSelection) {
				IStructuredSelection ss = (IStructuredSelection) sel;
				Object element = ss.getFirstElement();
				if (element instanceof IResource
						&& element instanceof IAdaptable) {

					IAdaptable adaptable = (IAdaptable) element;
					adapter = (IResource) adaptable.getAdapter(IResource.class);

				} // TODO: else clause needed?!
			}
			if (adapter == null) {
				MessageDialog.openInformation(window.getShell(), "Lego EV3",
						"plugin failed to identify active project :(");
				return;
			}
*/
			IProject proj = Gui.getActiveProject(); // adapter.getProject()
			Activator.log(proj.getName());
			Setup.setDefaults(proj);
			try {
				proj.refreshLocal(IResource.DEPTH_INFINITE,
						new NullProgressMonitor());
			} catch (CoreException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public void selectionChanged(IAction action, ISelection selection) {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

	@Override
	public void init(IWorkbenchWindow window) {

		this.window = window;

	}
}
