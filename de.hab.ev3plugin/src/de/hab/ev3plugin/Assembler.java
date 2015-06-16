package de.hab.ev3plugin;

import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;


public class Assembler {
	private String file_name;
	private File asm_path;
	private final String java = "java"; // if java is not in path, bad luck
	public Assembler(String file_name, File asm_path)
	{
		this.asm_path = asm_path;
		this.file_name = file_name;
	}
		
	public int run() throws IOException
	{
		ProcessBuilder pb = new ProcessBuilder(java, "-jar", "assembler.jar", file_name); // TODO: note, without .lms!
		pb.directory(asm_path);
		
		Process p = pb.start();
		try {
			p.waitFor();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return p.exitValue();
	}
}	
