package de.hab.ev3plugin.commands;

import java.util.regex.Pattern;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

import de.hab.ev3plugin.EV3Duder;
import de.hab.ev3plugin.gui.ChooseWLanEv3;
import de.hab.ev3plugin.gui.ChooseWLanEv3.Handler;
import ilg.gnuarmeclipse.managedbuild.cross.SharedStorage;

public class CommandEV3Wlan extends AbstractHandler implements IHandler {
	public static final Pattern SERIAL_PAT = Pattern.compile("^\\d{10}\\w{2}$");

	private boolean wlanMode = false;
	private EV3Duder ev3;
	static String device;
	static String oldUsb;
	static String oldSerial;

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		final IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();

		toggleEv3DuderConnectionParams();
		wlanMode = !wlanMode;
		/*
		 * Ideally the checkmark should only be set in case a valid Ev3 could be
		 * detected. and the wlanMode toggle here be removed and the other 2 wlanMode
		 * lines be uncommented. (Ideally)
		 */
		if (!wlanMode) {
			// wlanMode = false;
			return null;
		}

		String m_selectedToolchainName = SharedStorage.getToolchainName();
		String crossCommandUploader = SharedStorage.getToolchainUploader(m_selectedToolchainName);

		String uploader_path = crossCommandUploader;
		if (System.getProperty("os.name").startsWith("Windows")) {
			if (crossCommandUploader.isEmpty())
				uploader_path = "C:\\ev3\\uploader";
			uploader_path += "\\ev3duder.exe";
		} else
			uploader_path += "/ev3duder"; // let's hope it's in path

		ev3 = new EV3Duder(uploader_path, null);
		ev3.setSilent(true);

		ChooseWLanEv3 dialog = new ChooseWLanEv3(window.getShell());
		dialog.setBlockOnOpen(true);
		dialog.setHandler(new Handler() {
			/**
			 * /null/i validates and connects to the first ev3 it finds /[0-9]+/ validates
			 * and checks for existence of that serial /\./ validates and attempts a direct
			 * connection to the IP address
			 */
			@Override
			public boolean isValid(String id) {
				if (id.isEmpty())
					return false;

				if (SERIAL_PAT.matcher(id).matches()) {
					if (ev3.command("--tcp=" + id, "info", "ip")) {
						device = ev3.getStdout().trim();
						return true;
					}
				} else if (id.equalsIgnoreCase("null")) {
					if (ev3.command("--tcp", "info", "ip")) {
						device = ev3.getStdout().trim();
						return true;
					}
				} else if (id.indexOf(".") != -1) // no need for elaborate pattern matching as the uploader itself does
													// that
				{
					if (ev3.command("--tcp=" + id, "nop")) {
						device = id;
						return true;
					}
				} else {
					MessageDialog.openError(window.getShell(), "Pairing error",
							"Supplied string neither a valid IP address or serial.");
					return false;
				}

				MessageDialog.openError(window.getShell(), "Pairing error", "No Ev3 with specified ID could be found.");
				return false;
			}

			@Override
			public String fetchSerial() {
				if (!ev3.command("--usb", "info", "serial")) {
					MessageDialog.openError(window.getShell(), "Connection error", "No connected Ev3 were found.");
					return null;
				}
				return ev3.getStdout().trim();
			}

			@Override
			public void atCancel() {
				// toggleEv3DuderConnectionParams();
			}
		});
		dialog.open();
		if (device != null && !device.isEmpty()) {
			EV3Duder.tcp = "--tcp=" + device;
			// wlanMode = true;
		}

		return null;
	}

	static String usb = null, serial = null, tcp = null;

	static private void toggleEv3DuderConnectionParams() {
		if (usb == null) {
			usb = EV3Duder.usb;
			serial = EV3Duder.serial;
			tcp = EV3Duder.tcp;

			EV3Duder.usb = EV3Duder.serial = EV3Duder.tcp = "--nop";
		} else {
			EV3Duder.usb = usb;
			EV3Duder.serial = serial;
			EV3Duder.tcp = tcp;

			usb = serial = tcp = null;
		}
	}

}
