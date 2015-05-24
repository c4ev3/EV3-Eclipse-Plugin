package de.hab.ev3plugin.filebrowser;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.hab.ev3plugin.Activator;
import de.hab.ev3plugin.Ev3Duder;
import de.hab.ev3plugin.util.IO;
import de.hab.ev3plugin.util.Pair;

public class Ev3File {
	private static final Pattern dirpat = Pattern.compile("^(.*)/$");
	private static final Pattern filepat = Pattern.compile("^([A-F0-9]{32}) ([A-F0-9]{8}) (.*)$");
	public enum Fs {
		EV3,
		local
	}
	private String path;
	private Ev3Duder ev3;
	private String hash;
	private int size;


	public Ev3File(Ev3Duder instance, String path) {
		// TODO Auto-generated constructor stub
		this.path = path;
		ev3 = instance; 
		ev3.setSilent(true);
	}
	private Ev3File(Ev3Duder instance, String path, String hash, int size)
	{
		this.ev3 = instance;
		this.path = path;
		this.hash = hash;
		this.size = size;
	}
	private Ev3File(Ev3File clone)
	{
		this.ev3 = clone.ev3;
		this.path = clone.path;
		this.hash = clone.hash;
		this.size = clone.size;
	}
	public static Ev3File[] listRoots(Ev3Duder instance) {
		// TODO Auto-generated method stub
		Ev3File file = new Ev3File(instance, "/");
		return file.listFiles();
	}

	public Ev3File[] listFiles() {
		boolean ret = ev3.command("ls", this.path);
		if (ev3.getStdout() == null)
		{
				System.out.println("no stdout;");
				return null;
		}
		String[] lines = ev3.getStdout().split(
				System.getProperty("line.separator"));
		Ev3File[] contents = new Ev3File[lines.length];
		Matcher m;
		for (int i =0; i < lines.length; i++)
		{
			m = dirpat.matcher(lines[i]);
			if (m.find())
			{
				// directory!
				contents[i] = new Ev3File(ev3, m.group(1), null, 0);
			}
			else
			{
				//file!
				m = filepat.matcher(lines[i]);
				contents[i] = m.find() ?
						new Ev3File(ev3,
								path + m.group(3),
								m.group(1),
								Integer.parseInt(m.group(2), 16)
								) :
									null;
			}

		}
		return contents;
	}
	public String getName()
	{
		return IO.getName(path);
	}
	public String getParent()
	{
		return IO.getParent(path);
	}
	public Ev3File getParentFile()
	{
		return new Ev3File(ev3, getParent(), null, 0);
	}
	public boolean isDirectory()
	{
		return hash == null;
	}
}
