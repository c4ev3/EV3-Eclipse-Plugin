package de.hab.ev3plugin.properties;

import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;


public class NewStarter {
	
	private String _workspaceDirectory;
	private String _filePath;
	private String _projectname;
	private String _javaPath;
	private String _assemblerPath;
	
	NewStarter()
	{
		_workspaceDirectory = "";
		_filePath = "";
		_projectname = "";
	}
	
	public NewStarter(String projectName , String lmsasmPath)
	{
		_workspaceDirectory = "";
		_filePath = "";
		_projectname = projectName;
		_javaPath = "java.exe";
		_assemblerPath = lmsasmPath;
	}
	public void make()
	{
		this.createStarter();
		this.assembleStarter();
	}
	
	private void createStarter()
	{

		//Get Path and Create Startername
		IWorkspace workspace = ResourcesPlugin.getWorkspace(); // get object which represents the workspace
		this._workspaceDirectory = workspace.getRoot().getLocation().toString(); //get location of workspace
		
	
		//Creating File Path 
		this._filePath = this._workspaceDirectory + "/"+"myapps"+"/"+this._projectname+"Starter.lms"; 
		
		//Creates file **Starter.lms and Folder myapps 
		try 
		{			   
			   File starter = new File(this._filePath);
			   starter.getParentFile().mkdirs(); //Verzeichnisse erstellen
			   if ( starter.createNewFile() ) 
			   {  
				  //Starter war noch nicht vorhanden
			      System.out.println("Starter erfolgreich erstellt");
			   } 
			   else 
			   {  
				  //Bereits vorhandenen Starter überschrieben oder Fehler
			      System.out.println("Starter nicht (neu) erstellt");
			   }
		} 
		catch ( IOException ioe ) 
			{ 
				ioe.printStackTrace(); 
			}
		
		//Creating Starter Code for Lego VM
		String starterString = "vmthread MAIN \r\n"+								
							   "{ \r\n"+
								"DATA32 Timer \r\n"+
								"DATA32 Status \r\n"+
								//TESTAUSGABE
								//"//Write a message \r\n"+
								//"UI_DRAW(FILLWINDOW,BG_COLOR,0,0) \r\n"+
								//"UI_DRAW(SELECT_FONT,LARGE_FONT) \r\n"+
								//"UI_DRAW(TEXT,FG_COLOR,5,50,'RUNNING!') \r\n"+
								//"UI_DRAW(UPDATE) \r\n"+
								"//This is the command that executes the program \r\n"+
								//"SYSTEM('/media/$USER/LMS2012_EXT/home/root/lms2012/prjs/BrkProg_SAVE/" + this._projectname + "',Status) \r\n"+ //specific projectname to start the right project
								"SYSTEM('/media/card/" + this._projectname + "',Status) \r\n"+
								"//Wait before exiting \r\n"+
								"TIMER_WAIT(1000,Timer) \r\n"+
								"TIMER_READY(Timer) \r\n"+
								"} \r\n";
		
		//Writing Startercode to Starterfile
		Writer writer = null;

		try 
		{
			writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(this._filePath), "ISO-8859-1"));
			writer.write(starterString);
		} 
		catch (IOException ex) {
		  
			} 
			finally 
			{
			   try 
			   {
				   writer.close();
			   }
			   catch (Exception ex) {}
		}
		
		
	}
	private void assembleStarter()
	{
		//assembler.jar ausführen und aus .lms eine .rbf Datei machen		
		//.lms und .rbf datei in workspace unter ./workspace/myapps ablegen/belassen
		
		
		ProcessBuilder pb = new ProcessBuilder("\"" +this._javaPath+ "\"" + " -jar assembler.jar \"" +this._workspaceDirectory+"/myapps/" + this._projectname + "Starter\"");
		pb.directory(new File(this._assemblerPath));
		
		//GEHT!!!
		//ProcessBuilder pb = new ProcessBuilder("\"C:/Program Files/Java/jre7/bin/java.exe\" -jar assembler.jar  \""+_workspaceDirectory+"/myapps/" + _projectname + "Starter\"");
		//pb.directory(new File("C:\\Users\\Lindner\\Desktop\\lmsasm"));
		
		try {
			@SuppressWarnings("unused")
			Process p = pb.start();
		} catch (IOException e) 
			{
				e.printStackTrace();
			}
	}
}
