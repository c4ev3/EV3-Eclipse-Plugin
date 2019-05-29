package de.hab.ev3plugin.commands;

import java.io.File;
import java.util.Hashtable;
import java.util.regex.Pattern;

import org.eclipse.cdt.managedbuilder.core.IConfiguration;
import org.eclipse.cdt.managedbuilder.core.IManagedBuildInfo;
import org.eclipse.cdt.managedbuilder.core.ManagedBuildManager;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

import de.hab.ev3plugin.Assembler;
import de.hab.ev3plugin.EV3Duder;
import de.hab.ev3plugin.Preprocessor;
import de.hab.ev3plugin.gui.UploadProgress;
import de.hab.ev3plugin.util.Gui;
import de.hab.ev3plugin.util.IO;
import ilg.gnuarmeclipse.managedbuild.cross.ProjectStorage;

public class Upload extends AbstractHandler {
	
	protected String projectRoot;
	protected String projectName;
	protected IManagedBuildInfo buildInfo;
	protected String binaryDir;
	protected String workspaceRoot; // get location of workspace

	protected String localBinary;
	String remoteBinary;
	protected String localLauncher;
	protected String remoteLauncher;
	protected boolean windows;
	protected String ev3duder_binname;
	protected EV3Duder ev3duder;
	protected Shell shell;
	protected UploadProgress dialog;
	
	protected void postUpload() {
		
	}

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		
		try {
			
			IProject proj = Gui.getActiveProject();
			

			if (proj == null) {
				MessageDialog.openInformation(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), "Lego EV3",
						"Select the project you want to upload first.");
				return null;
			}
			
			if (!Pattern.matches("^[a-zA-Z0-9][a-zA-Z0-9_-]*$", proj.getName())) {
				if (!MessageDialog.openQuestion(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), "Lego EV3 WARNING!",
						"WARNING! The project name must only contain a-z, A-Z, 0-9, _ and must NOT contain spaces. If you use other characters, there might be unpredictable behavior and errors. Do you still want to continue?")) {
					return null;
				}
			}
			

			projectRoot = proj.getLocation().toString();
			projectName = proj.getName();
			buildInfo = ManagedBuildManager.getBuildInfo(proj);

			binaryDir = buildInfo.getDefaultConfiguration().getName();
			workspaceRoot = ResourcesPlugin.getWorkspace().getRoot().getLocation().toString(); // get location of
																								// workspace

			localBinary = projectRoot + "/" + binaryDir + "/" + projectName + ".elf";
			String remoteBinary = "/media/card/" + projectName + ".elf";
			localLauncher = projectRoot + "/myapps/" + projectName + ".rbf";
			remoteLauncher = "/media/card/myapps/" + projectName + ".rbf";
			windows = System.getProperty("os.name").startsWith("Windows");
			ev3duder_binname = windows ? "ev3duder.exe" : "ev3duder";
			shell = ilg.gnuarmeclipse.managedbuild.cross.Activator.getDefault().getWorkbench()
					.getActiveWorkbenchWindow().getShell();
			dialog = new UploadProgress(shell);
			boolean nowait = true; // TODO: add to .lms define nowait
			// Display display = Display.getDefault();
			// Progress dialog = new Progress(new Shell(display, SWT.MODELESS));
			dialog.setBlockOnOpen(false);
			dialog.open();

			if (!proj.exists())
				return null; // FIXME: needed?!

			IConfiguration[] configs = buildInfo.getManagedProject().getConfigurations();

			/*
			 * MessageDialog.openInformation(window.getShell(), "uploader=",
			 * ProjectStorage.getValue(configs[0], "uploader"));
			 */
			String path = ProjectStorage.getValue(configs[0], "uploader");
			File uploader = null;
			File assembler = null;
			ev3duder = null;

			if (path != null) {
				uploader = new File(path, ev3duder_binname); // externalise this!
				assembler = new File(path, "assembler.jar"); // check for its dependencies!
			}
			if (uploader == null || !uploader.exists() || uploader.isDirectory()) {

			} else {
				ev3duder = new EV3Duder(uploader.toString(), shell);
			}
			do {
				dialog.setProgress(20, "Assembling starter..");
				dialog.setProgress(20, "Assembling starter..");

				if (assembler == null || !assembler.exists() || assembler.isDirectory()) {
					// MessageDialog.openWarning(shell, "Assembling failed","Either the
					// <assembler.jar> file couldn't be found in "+ path +
					// " or the project has no <start.lms>. Default values will be used."); //
					// change warnings to write to log!

					if (ev3duder == null) {
						MessageDialog.openWarning(shell, "Uploader not found",
								"Ev3duder couldn't be found in " + path + "!");
						break; // no assembler + no uploader = nothing to do
					}
					if (!ev3duder.command("mkrbf", "/media/card/" + projectName + ".elf", localLauncher)) {
						MessageDialog.openWarning(shell, "Mkrbf failed", "Ev3duder couldn't mkrbf :( ");
						break;
					}

					remoteBinary = "/media/card/" + projectName + ".elf";
				} else {
					Hashtable<String, String> map = new Hashtable<String, String>();
					map.put("${projectName}", projectName);
					map.put("${card}", "/media/card");
					map.put("${usb}", "/media/usb");
					map.put("${brick}", "/home/root/lms2012/prjs/BrkProg_SAVE");
					dialog.log("Preprocessing start.lms");
					Preprocessor pp = new Preprocessor(projectRoot + "/start.lms");
					String temp_starter = IO.removeExtension(pp.run(map).getAbsolutePath());
					dialog.log(" [DONE]\n");

					// Assembler asm = new Assembler(temp_starter, uploader);
					dialog.log("Generating rbf: " + temp_starter);
					System.out.println("temp_starter=" + temp_starter);
					(new Assembler(temp_starter, uploader.getParentFile())).run();
					// asm.run();

					IO.copy(new File(temp_starter + ".rbf"), new File(localLauncher));
					dialog.log(" [DONE]\n");

					// Now let's get upload params
					Hashtable<String, String> defines = pp.defines();
					remoteBinary = defines.get("elfexec");
					dialog.log("remoteBinary='" + remoteBinary + "'\n");
					remoteLauncher = defines.get("starter");
					dialog.log("remoteLauncher='" + remoteLauncher + "'\n");

				}

				/*
				 * left for illustratory purposes. ev3duder up already creates directories //
				 * attempt creation of a /myapps/ directory, if it's already there, no harm
				 * done. ev3duder.setSilent(true); dialog.setProgress(40,
				 * "Attempting to create directory"); dialog.setProgress(40,
				 * "Attempting to create directory"); ev3duder.command("mkdir",
				 * IO.getParent(remoteLauncher)); ev3duder.setSilent(false);
				 */
				dialog.setProgress(60, "Uploading ELF executable..");
				dialog.setProgress(60, "Uploading ELF executable..");

				if (!ev3duder.transferFile(localBinary, remoteBinary))
					break;
				if (!nowait)
					Thread.sleep(300);

				dialog.setProgress(80, "Uploading starter file..");
				dialog.setProgress(80, "Uploading starter file..");
				if (!ev3duder.transferFile(localLauncher, remoteLauncher)) {
					MessageDialog.openWarning(shell, "Uploading launcher failed", " no idea. sorry ."); // change
																										// warnings to
																										// write to log!
					break;
				}

				postUpload(); // OO Baby steps
			} while (false); // a legitimate use for goto IMHO
			if (ev3duder == null) {
				// TODO: remove connect on mkrbf
				MessageDialog.openError(shell, "Uploader not found",
						"The project's uploader path <" + path + "> is invalid. Try correcting it");
			}
			// if (!nowait) Thread.sleep(1000);
			dialog.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	
}
