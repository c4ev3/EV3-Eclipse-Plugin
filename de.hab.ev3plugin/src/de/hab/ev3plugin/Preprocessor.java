package de.hab.ev3plugin;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Hashtable;
import java.util.List;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Preprocessor {

	private static final Pattern params = Pattern.compile("^(?:#)?\\s*define\\s+(.*?)\\s+'(.*)'\\s*$");
	private static final Pattern vars = 
			Pattern.compile("(\\$\\{.*?\\})"); 
	//BUG: substitution is done in comments, but I can't think of a scenario where this is a bad thing
	private String fileName;
	private File tmp;
	private Hashtable<String, String>defs = new Hashtable<String, String>();
	
	public Preprocessor(String fileName)
	{
		this.fileName = fileName;
	}
	public File run(Hashtable<String, String>map)
	{
		File pp = null;
		FileWriter fw = null;
		try {
			pp = File.createTempFile("ev3starter", ".lms");
			fw = new FileWriter(pp, true); //the true will append the new data
			List<String> lines = Files.readAllLines(Paths.get(fileName),
                    Charset.defaultCharset());
			
			Matcher m;
            for (String line : lines) {
                m = vars.matcher(line);
                while (m.find()) {
                	//System.out.println(m.group(1) + '\n');
         
                		String val = map.get(m.group(1)); // get replacement string from map
                		if (val == null) val = defs.get(m.group(1)); // else get it from previous defines
                		if (val == null) val = ""; // else leave it empty
                		//System.out.println("result[" + m.group(1) + "]=" + val + "\n"); //TODO: think about debug output
                		line = m.replaceFirst(val);
                		m = vars.matcher(line);
                }
                	m = params.matcher(line);
                	if (m.find()) defs.put(m.group(1), m.group(2));
                	
                	fw.write(line);
                	fw.write('\n');
            }
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally
		{
			if (fw != null)
				try {
					fw.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
		for(Entry<String, String> entry : defs.entrySet()) {
    		System.out.println(entry.getKey() + " is defined as " + entry.getValue());
    	}
		return pp;
		
	}
	public Hashtable<String, String> defines()
	{	
		return defs;
    }
}
