/**
 * \file SetupCOMPort.java
 * 
 * Shows Ev3Duder's static data
 */
package de.hab.ev3plugin.actions;


import ilg.gnuarmeclipse.managedbuild.cross.SharedStorage;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;

import de.hab.ev3plugin.Ev3Duder;
import de.hab.ev3plugin.gui.SetBluetoothCOM.Handler;


public class ConnectionInfo implements IWorkbenchWindowActionDelegate {
	private IWorkbenchWindow window;

	@Override
	public void run(IAction action) {
		String m_selectedToolchainName = SharedStorage.getToolchainName();
		String crossCommandUploader = SharedStorage
				.getToolchainUploader(m_selectedToolchainName);

		String uploader_path = crossCommandUploader;
		if (System.getProperty("os.name").startsWith("Windows"))
		{
			if (crossCommandUploader.isEmpty()) uploader_path = "C:\\ev3\\uploader";
			uploader_path += "\\ev3duder.exe";
		} else uploader_path += "/ev3duder"; // let's hope it's in path

		MessageDialog.openInformation(window.getShell(), "Ev3Duder static fields", 
				"command line: " + uploader_path + " \\ \n" +
				Ev3Duder.usb + " " + Ev3Duder.serial + " " + Ev3Duder.tcp + " " + Ev3Duder.timeout
				);
//		Ev3Duder ev3 = new Ev3Duder(uploader_path, null), ev3.command("info");

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
