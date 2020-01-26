package de.hab.ev3plugin.commands;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

import de.hab.ev3plugin.EV3Duder;
import de.hab.ev3plugin.gui.SetBluetoothCOM;
import de.hab.ev3plugin.gui.SetBluetoothCOM.Handler;

public class SetBluetoothComPort extends AbstractHandler implements IHandler {
	
	static private String device = "COM1";

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		final IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		
		SetBluetoothCOM dialog = new SetBluetoothCOM(window.getShell());
		dialog.setBlockOnOpen(true);
		dialog.setStandardValue(device);
		dialog.setHandler(new Handler() {
			@Override
			public void setValue(String _device) {
				EV3Duder.serial = "--serial=" + _device;
			}
		});
		dialog.open();
		
		return null;
	}

}
