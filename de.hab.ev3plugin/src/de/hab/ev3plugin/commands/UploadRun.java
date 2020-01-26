package de.hab.ev3plugin.commands;

public class UploadRun extends Upload {

	@Override
	protected void postUpload() {
		dialog.setProgress(90, "Starting ELF executable..");
		dialog.setProgress(90, "Starting ELF executable...");
		
		ev3duder.startFile(remoteLauncher);
		// dialog.setProgress(99);
	}
	
}
