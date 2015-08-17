/**
 * \file KillChildrenOfVM.java
 * \bug Only stub doesn't really do anything
 * The main problem is that the lms2012 server isn't responding
 * when the VM is running. To kill children one would either need:
 * - an ev3-side application that kills *.elf programs that's adressable somehow
 * - make the ev3 initialization function register a recurring check for some sort
 * 	 of kill me switch. Button press maybe
 */
package de.hab.ev3plugin.actions;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;


public class KillChildrenOfVM implements IWorkbenchWindowActionDelegate {
	private IWorkbenchWindow window;
	
	@Override
	public void run(IAction action) {
		   //Message for the future
				MessageDialog.openInformation(
						window.getShell(),
						"Lego EV3",
						"Check for lms2012's children and TERM INT QUIT with pause, then KILL");
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
