/**
 * \file SetupCOMPort.java
 * 
 * Set COM Port for use with bluetooth
 * Please note that Bluetooth is ONLY attempted when a USB connection couldn't be found
 */
package de.hab.ev3plugin.actions;


import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;

import de.hab.ev3plugin.Ev3Duder;
import de.hab.ev3plugin.gui.SetBluetoothCOM.Handler;
import de.hab.ev3plugin.gui.SetBluetoothCOM;


public class SetupCOMPort implements IWorkbenchWindowActionDelegate {
	private IWorkbenchWindow window;
	static private String device = "COM1";

	@Override
	public void run(IAction action) {
		SetBluetoothCOM dialog = new SetBluetoothCOM(window.getShell());
		dialog.setBlockOnOpen(true);
		dialog.setStandardValue(device);
		dialog.setHandler(new Handler() {
			@Override
			public void setValue(String _device) {
				Ev3Duder.serial = "--serial=" + _device;
				MessageDialog.openInformation(window.getShell(), "EYEAHEAUEHUAEH", Ev3Duder.serial);
			}
		});
		dialog.open();
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
