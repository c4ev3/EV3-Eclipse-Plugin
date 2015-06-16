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

public class Ev3Duder { // wrapper for the ev3dude CLI program
	private String stdout;
	private String stderr;

	private String path;
	private Shell shell;
	private boolean silent = false;
	public Ev3Duder(String path, Shell shell) { 
		this.path = path;
		this.shell = shell;
	}

	

	public static void spawn(String path, Shell shell) {
		ProcessBuilder pb = new ProcessBuilder(path, "test"); // TODO: not portable!

		stdoutHandler thread = new stdoutHandler(pb, null);
		thread.run();

		MessageDialog.openInformation(shell, "Lego EV3",
				"Process exited with: " + thread.getReturn());
	}
	public void setSilent(boolean state)
	{
		this.silent = state;
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
		if (!silent)
		{
			for (String line : lines) {
				System.out.println(line);
			}
			if (ev3duder.getStatus() != 0)
				MessageDialog.openError(shell,
						"Error uploading (code=" + ev3duder.getStatus() + ")",
						ev3duder.getInfos() != null ?
							ev3duder.getInfos() :
							"An unknown error occured... Sorry about that."
						);
		}
		return ev3duder.getStatus() == 0;
		// BufferedReader reader = new BufferedReader(
		// new InputStreamReader(p.getInputStream()));
		// String line = null;
		// while ((line = reader.readLine()) != null) {
		// Activator.log(line);
		// }
	}
	public String getStderr()
	{
		return stderr;
	}
	public String getStdout()
	{
		return stdout;
	}
	public boolean command(String command, String...args)
	{
		String cmdline[] = new String[args.length + 2];
		cmdline[0] = path;
		cmdline[1] = command;
		System.arraycopy(args, 0, cmdline, 2, args.length);
		ProcessBuilder pb = new ProcessBuilder(cmdline); // TODO: not portable!


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
		if (!silent)
		{
			for (String line : lines) {
				Activator.log(line);
				//FIXME: use the log
			}
			if (ev3duder.getStatus() != 0)
				MessageDialog.openError(shell,
						"Error uploading (code=" + ev3duder.getStatus() + ")",
						ev3duder.getInfos() != null ?
							ev3duder.getInfos() :
							"An unknown error occured... Sorry about that."
						);
		}
		stdout = ev3duder.getInfos();
		stderr = ev3duder.getErrors();
		return ev3duder.getStatus() == 0;
	}
	public boolean startFile(String remote) {
		ProcessBuilder pb = new ProcessBuilder(path, "run", remote); // TODO: not portable!

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
		if (!silent)
		{
			if (ev3duder.getStatus() != 0)
				MessageDialog.openError(shell,
						"Error uploading (code=" + ev3duder.getStatus() + ")",
						ev3duder.getInfos() != null ?
							ev3duder.getInfos() :
							"An unknown error occured... Sorry about that."
						);
		}
		return ev3duder.getStatus() == 0;
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

}
