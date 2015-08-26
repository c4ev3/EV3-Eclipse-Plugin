/**
 * \file SetupEV3WLanConnection.java
 * 
 * Eventually entering WPA2 credentials should also be made possible!
 */
package de.hab.ev3plugin.actions;

import java.util.regex.Pattern;

import ilg.gnuarmeclipse.managedbuild.cross.SharedStorage;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;

import de.hab.ev3plugin.Ev3Duder;
import de.hab.ev3plugin.gui.ChooseWLanEv3;
import de.hab.ev3plugin.gui.ChooseWLanEv3.Handler;


public class SetupEV3WLanConnection implements IWorkbenchWindowActionDelegate {
    public static final Pattern SERIAL_PAT = Pattern.compile("^\\d{10}\\w{2}$");

	private IWorkbenchWindow window;
	private boolean toggle = false;
	private Ev3Duder ev3;
	static String device;
	static String oldUsb;
	static String oldSerial;

	@Override
	public void run(IAction action) {
		   //Message for the future
		toggleEv3DuderConnectionParams();
		toggle = !toggle;
		if (toggle == false)
			return;

		String m_selectedToolchainName = SharedStorage.getToolchainName();
		String crossCommandUploader = SharedStorage
				.getToolchainUploader(m_selectedToolchainName);

		String uploader_path = crossCommandUploader;
		if (System.getProperty("os.name").startsWith("Windows"))
		{
			if (crossCommandUploader.isEmpty()) uploader_path = "C:\\ev3\\uploader";
			uploader_path += "\\ev3duder.exe";
		} else uploader_path += "/ev3duder"; // let's hope it's in path

		ev3 = new Ev3Duder(uploader_path, null);
		ev3.setSilent(true);

		ChooseWLanEv3 dialog = new ChooseWLanEv3(window.getShell());
		dialog.setBlockOnOpen(true);
		dialog.setHandler(new Handler() {
			/**
			 * /null/i validates and connects to the first ev3 it finds
			 * /[0-9]+/ validates and checks for existence of that serial
			 * /\./ validates and attempts a direct connection to the IP address
			 */
			@Override
			public boolean isValid(String id) {
				if (id.isEmpty()) return false;

				if (SERIAL_PAT.matcher(id).matches())
				{ 
					if (ev3.command("--tcp="+ id, "info", "ip"))
					{
						device = ev3.getStdout().trim();
						return true;
					}
				}
				else if (id.equalsIgnoreCase("null"))
				{
					if (ev3.command("--tcp", "info", "ip"))
					{
						device = ev3.getStdout().trim();
						return true;
					}
				}
				else if (id.indexOf(".") != -1) // no need for elaborate pattern matching as the uploader itself does that
				{
					if (ev3.command("--tcp=" + id, "nop"))
					{
						device = id;
						return true;
					}
				}else
				{ 
					MessageDialog.openError(window.getShell(), "Pairing error", "Supplied string neither a valid IP address or serial.");
					return false;
				}
	
				MessageDialog.openError(window.getShell(), "Pairing error", "No Ev3 with specified ID could be found.");
				return false;
			}
			@Override
			public String fetchSerial() {
				if (!ev3.command("--usb", "info", "serial"))
				{
						MessageDialog.openError(window.getShell(), "Connection error", "No connected Ev3 were found.");
						return null;
				}
				return ev3.getStdout().trim();
			}
		});
		dialog.open();
		if (device != null && !device.isEmpty()) 
		{
			Ev3Duder.tcp = "--tcp=" + device;
		}
	}
	static String usb = null, serial = null, tcp = null;
	static private void toggleEv3DuderConnectionParams()
	{
		if (usb == null)
		{
			usb = Ev3Duder.usb;
			serial = Ev3Duder.serial;
			tcp = Ev3Duder.tcp;
		
			Ev3Duder.usb = Ev3Duder.serial = Ev3Duder.tcp = "--nop";
		}else
		{
			Ev3Duder.usb = usb;
			Ev3Duder.serial = serial;
			Ev3Duder.tcp = tcp;
			
			usb = serial = tcp = null;
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
