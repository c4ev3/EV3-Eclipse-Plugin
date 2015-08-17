/**
 * Wrapper for the LEGO bytecode (.rbf) assembler (assembler.jar)
 * \author Ahmad Fatoum
 * \copright (c) 2015 Ahmad Fatoum. Code available under terms of the EPL
 */
package de.hab.ev3plugin;

import java.io.File;
import java.io.IOException;


public class Assembler {
	private String file_name;
	private File asm_path;
	private final String java = "java"; // if java is not in path, bad luck
	/**
	 * Constructor
	 * @param file_name name of file to assembler
	 * @param asm_path  path to assembler.jar
	 */
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
