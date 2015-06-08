package de.hab.ev3plugin.filebrowser;

import java.io.File;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.jface.dialogs.MessageDialog;

import de.hab.ev3plugin.Activator;
import de.hab.ev3plugin.Ev3Duder;
import de.hab.ev3plugin.util.IO;
import de.hab.ev3plugin.util.Pair;

public class Ev3File {
	private static final Pattern dirpat = Pattern.compile("^(.*)/$");
	private static final Pattern filepat = Pattern.compile("^([A-F0-9]{32}) ([A-F0-9]{8}) (.*)$");

	private String path = null;
	private Ev3Duder ev3;
	private String hash = null;
	private int size = -1;
	private boolean restricted;

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
		this.restricted = !path.isEmpty() && (path.charAt(0) == '-' || Arrays.asList(restricted_paths).contains(path));
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
		Ev3File file = new Ev3File(instance, "");
		return file.listFiles();
	}

	final static String[] restricted_paths = {"/proc"};
	public boolean isRestricted()
	{
		return restricted;
	}
	public Ev3File[] listFiles() {
		if (this.path.isEmpty())
		{
			Ev3File[] contents = new Ev3File[2];
			contents[0] = new Ev3File(ev3, "- click to refresh", null, 0);
			contents[1] = new Ev3File(ev3, "/", null, 0);
			return contents;
		}
		boolean ret = ev3.command("ls", this.path);
		MessageDialog.openInformation(null, "Ev3File", 
				"Listing files for <" + this.path + ">"
					);
		if (ev3.getStdout() == null)
		{
				System.out.println("no stdout;");
				//return null;
		}
		String[] lines = ev3.getStdout().split(
				System.getProperty("line.separator"));
		int contents_sz = lines.length;
        if (lines.length >= 2 && lines[contents_sz-2].equals("../"))
                        contents_sz-=2;
        if (lines.length >= 1 && lines[contents_sz-1].equals("../"))
                        contents_sz--;
		Ev3File[] contents = new Ev3File[contents_sz];
		Matcher m;
		int i = 0;
		for (String line : lines)
		{
			String dadPath = path + (path.equals("/") ? "" : "/");
			m = dirpat.matcher(line);
			if (m.find())
			{
				// directory!
					if(line.charAt(0) == '.')
						continue;
				contents[i] = new Ev3File(ev3, dadPath + m.group(1), null, 0);
			}
			else
			{
				//file!
				m = filepat.matcher(lines[i]);
				if (m.find())
				{
				contents[i] = 
						new Ev3File(ev3,
								dadPath + m.group(3),
								m.group(1),
								Integer.parseInt(m.group(2), 16)
								);
				}else{
					//crash
						contents[i] = new Ev3File(ev3,
							"- Size Limit Exceeded",
							null,
							0);
				}
			}
			i++;
		}
		return contents;
	}
	public String getName()
	{
		if (path != null)
			if (path.isEmpty())
				return "/";
			else
                return IO.getName(path);
		return "Some error occured";
	}
	public String getParent()
	{
			if (path != null && !path.equals("/"))
					return IO.getParent(path);
			return null;
	}
	public Ev3File getParentFile()
	{
		return new Ev3File(ev3, getParent(), null, 0);
	}
	public boolean isDirectory()
	{
		return hash == null && !restricted;
	}
}
