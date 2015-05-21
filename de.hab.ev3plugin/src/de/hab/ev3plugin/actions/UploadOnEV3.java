package de.hab.ev3plugin.actions;

import ilg.gnuarmeclipse.managedbuild.cross.Option;
import ilg.gnuarmeclipse.managedbuild.cross.ProjectStorage;
import ilg.gnuarmeclipse.managedbuild.cross.SetCrossCommandWizardPage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Hashtable;

import org.eclipse.cdt.managedbuilder.core.IConfiguration;
import org.eclipse.cdt.managedbuilder.core.IManagedBuildInfo;
import org.eclipse.cdt.managedbuilder.core.ManagedBuildManager;
import org.eclipse.cdt.managedbuilder.ui.wizards.MBSCustomPageManager;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.ISelectionService;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;

import de.hab.ev3plugin.Activator;
import de.hab.ev3plugin.Assembler;
import de.hab.ev3plugin.EV3Duder;
import de.hab.ev3plugin.Preprocessor;
import de.hab.ev3plugin.progress.Progress;
import de.hab.ev3plugin.util.Gui;
import de.hab.ev3plugin.util.IO;

public class UploadOnEV3 implements IWorkbenchWindowActionDelegate {

	private IWorkbenchWindow window;

	@Override
	public void run(IAction action) {

		try {
			IProject proj = Gui.getActiveProject(); // adapter.getProject()
				
			if (proj == null) {
				MessageDialog.openInformation(window.getShell(), "Lego EV3",
						"Select the project you want to upload first.");
				return;
			}
			
			String projectRoot = proj.getLocation().toString();
			String projectName = proj.getName();
			
			String workspaceRoot = ResourcesPlugin.getWorkspace().getRoot()
					.getLocation().toString(); // get location of workspace

			String localBinary = projectRoot + File.separator + "debug"
					+ File.separator + projectName;
			String remoteBinary = "../prjs/SD_Card/" + projectName;
			String localLauncher = projectRoot + "/myapps/" + projectName + ".rbf";
			String remoteLauncher = "../prjs/SD_Card/myapps/" + projectName
					+ "Starter.rbf";

			Shell shell = ilg.gnuarmeclipse.managedbuild.cross.Activator.getDefault()
					.getWorkbench().getActiveWorkbenchWindow().getShell();
			Progress dialog = new Progress(shell);
			// Display display = Display.getDefault();
			// Progress dialog = new Progress(new Shell(display, SWT.MODELESS));
			dialog.setBlockOnOpen(false);
			dialog.open();
	
			if (!proj.exists())
				return; //FIXME: needed?!

			IManagedBuildInfo buildInfo = ManagedBuildManager.getBuildInfo(proj);
			if (buildInfo == null)
				return;
			
			IConfiguration[] configs = buildInfo.getManagedProject()
					.getConfigurations();
	
        /*	MessageDialog.openInformation(window.getShell(), "uploader=",
					ProjectStorage.getValue(configs[0], "uploader"));
		*/
			String path = ProjectStorage.getValue(configs[0], "uploader");
			File uploader = null;
			File assembler = null;
			EV3Duder ev3duder = null;	
			String relPathToElf = "../" + projectName + ".elf";

			if (path != null) {
				uploader = new File(uploader, "ev3duder"); // externalise this!
				assembler = new File(uploader, "assembler.jar"); // check for its dependencies!
			}
			if (uploader == null || !uploader.exists() || uploader.isDirectory())
			{

			}
			else
			{
				ev3duder = new EV3Duder(path, shell);
			}
			do {	
				dialog.setProgress(20, "Assembling starter..");
				dialog.setProgress(20, "Assembling starter..");

				if (assembler == null || !assembler.exists() || assembler.isDirectory()) {
				//	MessageDialog.openWarning(shell, "Assembling failed","Either the <assembler.jar> file couldn't be found in "+ path +
				//		" or the project has no <start.lms>. Default values will be used."); // change warnings to write to log!

					if (ev3duder == null)
						break; // no assembler + no uploader = nothing to do
					
					if (ev3duder.command("mkrbf", relPathToElf, localLauncher))
						break;	
					remoteLauncher = "/media/card/myapps/" + relPathToElf;
				}else
				{
                        Hashtable<String, String> map = new Hashtable<String, String>();
                        map.put("${projectName}", projectName);
                        Preprocessor pp = new Preprocessor(localLauncher);
                        String temp_starter = IO.removeExtension(pp.run(map).toString());
                        //Assembler asm = new Assembler(temp_starter, uploader);
                        (new Assembler(temp_starter, uploader)).run();
                        //asm.run();

                        IO.copy(new File(temp_starter + ".rbf"), new File(projectRoot + "myapps/start.rbf"));
                        // Now let's get upload params
                        Hashtable<String, String> defines = pp.defines();
                        remoteBinary = defines.get("destdir") + "/" + defines.get("elfexec");
                        remoteLauncher = defines.get("destdir") + "/" + defines.get("starter");

				}	
				// attempt creation of a /myapps/ directory, if it's already there, no harm done.

				dialog.setProgress(40, "Attempting to create directory");
				dialog.setProgress(40, "Attempting to create directory");
				ev3duder.command("mkdir", IO.getParent(remoteLauncher));

				dialog.setProgress(60, "Uploading ELF executable..");
				dialog.setProgress(60, "Uploading ELF executable..");

				if (!ev3duder.transferFile(localBinary, remoteBinary)) {
					// do stuff
					break;
				}
				Thread.sleep(300);

				dialog.setProgress(80, "Uploading starter file..");
				dialog.setProgress(80, "Uploading starter file..");
				if (ev3duder.transferFile(localLauncher, remoteLauncher)) {
					Thread.sleep(300);

					dialog.setProgress(90, "finalizing..");
					dialog.setProgress(90, "finalizing..");
					// dialog.setProgress(99);
				}
				
}while(false); // a legitimate use for goto IMHO
if (ev3duder == null)
{
	
		MessageDialog.openError(shell, "Uploader not found", "The project's uploader path <"
								+ path +
								"> is invalid. Try correcting it");
}
			//Thread.sleep(1000);
			dialog.close();
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
