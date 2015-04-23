package de.hab.ev3plugin;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.InvalidParameterException;
import java.util.ArrayList;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;

public class EV3Duder { // wrapper for the ev3dude CLI program
	private String path;
	private Shell shell;
	public EV3Duder(String path, Shell shell) { 
		this.path = path;
		this.shell = shell;
	}

	static class stdoutHandler extends Thread {
		public int exitValue = -1337; // unset magic
		private Process proc;
		private ProcessBuilder pb;
		private Shell shell;
		stdoutHandler(ProcessBuilder pb, Shell shell) {
			this.pb = pb;
			this.shell = shell;
		}

		@Override
		public void run() {
			try {
				ArrayList<String> lines = new ArrayList<String>();

				proc = pb.start();
				// read from the merged stream
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(proc.getInputStream()));
				String line = "<nothing read from stdout>";
				// read until the stream is exhausted, meaning the process
				// has terminated
				while ((line = reader.readLine()) != null) {
					lines.add(line);
					// System.out.println(line); // use the output here
				}

				// get the exit code if required
				exitValue = proc.waitFor();
				if (proc.exitValue() != 0) {
					MessageDialog.openError(shell, "Error uploading (code=" + exitValue
							+ ")", lines.get(lines.size() - 1));

				} else
					MessageDialog.openError(shell, "Following error_code:", ""
							+ exitValue);
			} catch (Exception e) {
				MessageDialog.openError(shell, "Exception reading Input", "stack trace in console");
				e.printStackTrace();
			}

		}

		public int getReturn() {
			return exitValue;
		}
	}

	public static void spawn(String path, Shell shell) {
		ProcessBuilder pb = new ProcessBuilder(path, "test"); // TODO: not portable!

		stdoutHandler thread = new stdoutHandler(pb, null);
		thread.run();

		MessageDialog.openInformation(shell, "Lego EV3",
				"Process exited with: " + thread.getReturn());
	}

	public boolean transferFile(String local, String remote) { // TODO:
																		// pass
																		// a
																		// callback
																		// where
																		// stderr
																		// is
																		// written
		Activator
				.log("0) Invoking " + path);
		ProcessBuilder pb = new ProcessBuilder(path, "up", local, remote); // TODO: not portable!

		Activator.log("1) attempting getting " + local + " to " + remote);

		/*
		 * stdoutHandler thread = new stdoutHandler(pb); thread.run(); return
		 * thread.getReturn() == 0;
		 */
		ProcessBuilderWrapper ev3duder;
		try {
			ev3duder = new ProcessBuilderWrapper(pb);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}

		String[] lines = ev3duder.getErrors().split(
				System.getProperty("line.separator"));
		for (String line : lines) {
			Activator.log(line);
		}
		if (ev3duder.getStatus() != 0)
			MessageDialog.openError(shell,
					"Error uploading (code=" + ev3duder.getStatus() + ")",
					ev3duder.getInfos());

		return ev3duder.getStatus() == 0;
		// BufferedReader reader = new BufferedReader(
		// new InputStreamReader(p.getInputStream()));
		// String line = null;
		// while ((line = reader.readLine()) != null) {
		// Activator.log(line);
		// }
	}

	public boolean startFile(String remote) {
		ProcessBuilder pb = new ProcessBuilder(path, "exec", remote); // TODO: not portable!

		ProcessBuilderWrapper ev3duder;
		try {
			ev3duder = new ProcessBuilderWrapper(pb);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		if (ev3duder.getStatus() != 0)
			MessageDialog.openError(shell,
					"Error uploading (code=" + ev3duder.getStatus() + ")",
					ev3duder.getInfos());

		return ev3duder.getStatus() == 0;
	}
}
