/**
 * \file UploadAndRunOnEV3.java
 * My sorry attempt at OOP
 */
package de.hab.ev3plugin.actions;

public class UploadAndRunOnEV3 extends UploadOnEV3 {

		
	@Override
	public void postUpload()
	{
		dialog.setProgress(90, "Starting ELF executable..");
		dialog.setProgress(90, "Starting ELF executable...");
		
		ev3duder.startFile(remoteLauncher);
		// dialog.setProgress(99);
	}
}
