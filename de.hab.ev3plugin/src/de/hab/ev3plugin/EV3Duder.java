/**
 * Wrapper for the ev3duder command line utility
 * \see http://github.com/a3f/ev3duder
 * \author Ahmad Fatoum
 * \copyright (c) 2015 Ahmad Fatoum. Code available under terms of the EPL
 */
package de.hab.ev3plugin;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;

public class Ev3Duder { // wrapper for the ev3dude CLI program
	private String stdout;
	private String stderr;

	private String path;
	static public String usb = "--usb=";
	static public String serial = "--serial=";
	static public String tcp = "--nop";
	static final public String timeout = "-t=0"; // let the ev3 decide (currently, 6s for UDP recv and 1s for tcp connect)

	private Shell shell;
	private boolean silent = false;
	/**
	 * Constructor
	 * @param path full path of ev3duder utility
	 * @param shell optional reference to a Shell object to use for GUI messages
	 * @bug   I guess Shell might not be the best way to do that?
	 */
	public Ev3Duder(String path, Shell shell) { 
		this.path = path;
		this.shell = shell;
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
		ProcessBuilder pb = new ProcessBuilder(path, usb, serial, tcp, timeout, "up", local, remote); // TODO: not portable!


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
		String cmdline[] = new String[args.length + 6];
		cmdline[0] = path;
		cmdline[1] = usb;
		cmdline[2] = serial;
		cmdline[3] = tcp;
		cmdline[4] = timeout;
		cmdline[5] = command;
		System.arraycopy(args, 0, cmdline, 6, args.length);
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
		ProcessBuilder pb = new ProcessBuilder(path, usb, serial, tcp, timeout, "run", remote); // TODO: not portable!

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

}
