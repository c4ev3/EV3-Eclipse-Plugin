package de.hab.ev3plugin.commands;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

import de.hab.ev3plugin.EV3Duder;
import ilg.gnuarmeclipse.managedbuild.cross.SharedStorage;

public class ConnectionInfo extends AbstractHandler implements IHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		final IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		
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
				EV3Duder.usb + " " + EV3Duder.serial + " " + EV3Duder.tcp + " " + EV3Duder.timeout
				);
//		Ev3Duder ev3 = new Ev3Duder(uploader_path, null), ev3.command("info");
		
		return null;
	}

}
