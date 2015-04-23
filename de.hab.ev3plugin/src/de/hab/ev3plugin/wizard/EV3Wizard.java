package de.hab.ev3plugin.wizard;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.FileChannel;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.util.Arrays;

import org.eclipse.cdt.managedbuilder.core.BuildException;
import org.eclipse.cdt.managedbuilder.core.IConfiguration;
import org.eclipse.cdt.managedbuilder.core.IManagedBuildInfo;
import org.eclipse.cdt.managedbuilder.core.IManagedProject;
import org.eclipse.cdt.managedbuilder.core.IOption;
import org.eclipse.cdt.managedbuilder.core.ITool;
import org.eclipse.cdt.managedbuilder.core.IToolChain;
import org.eclipse.cdt.managedbuilder.core.ManagedBuildManager;
import org.eclipse.cdt.ui.wizards.CDTMainWizardPage;
import org.eclipse.cdt.ui.wizards.CDTProjectWizard;

import de.hab.ev3plugin.Activator;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;

import de.hab.ev3plugin.properties.NewStarter;
import static java.nio.file.StandardCopyOption.*;

import java.nio.file.*;

import static java.nio.file.StandardCopyOption.*;

import java.nio.file.attribute.*;

import static java.nio.file.FileVisitResult.*;

import java.io.IOException;
import java.util.*;

public class EV3Wizard extends CDTProjectWizard {

	/**
	 * This function is called at the end of the creation process Strangely,
	 * couldn't find any documentation for it. I will leave a stub as it might
	 * prove useful later on
	 * 
	 * @param prj
	 *            an IProject that contains state at end of the creation process
	 * @return an IProject
	 * @see createIProject for more information. Source is here
	 *      (http://eclipse-cdt.sourcearchive.com/documentation/6.0.2-1/
	 *      CDTCommonProjectWizard_8java-source.html)
	 */
	@Override
	protected IProject continueCreation(IProject proj) {

		// The things you specifically asked for:
		// prj.getName()
		// prj.getType()
		// toolchainManagedBuildManager.getBuildInfo(prj).getManagedProject().getConfigurations()[0].getToolChain()
		
		
		// toolchain.getOptionBySuperClassId("cdt.managedbuild.option.gnu.cross.prefix");
				// final IOption PATHoption =
				// toolchain.getOptionBySuperClassId("cdt.managedbuild.option.gnu.cross.path");
		
		
		NewStarter starter = new NewStarter(proj.getName(), Activator
				.getDefault().getResources() + File.separator + "lmsasm");
		starter.make();

		String api = "C:\\EV3\\API";

		// Link API Folder to the Project
		IFolder link = proj.getFolder("ev3");
		if (!link.exists()) {
			try {
				link.createLink(new Path(api), IResource.NONE, null);
			} catch (CoreException e) {

				e.printStackTrace();
			}
		} else {
			// No valid Path selected
		}

		// de.hab.ev3plugin.properties.EV3PropertyPage.configOptions(prj,
		// "C:\\EV3\\API");
		/*
		 * File sourceFile = new File(Activator.getDefault().getResources() +
		 * File.separator + "template.c"); File destFile = new File
		 * (prj.getFolder("").toString() + "main.c"); if(!destFile.exists()) {
		 * try { destFile.createNewFile(); } catch (IOException e) { // TODO
		 * Auto-generated catch block e.printStackTrace(); } }
		 * 
		 * FileChannel source = null; FileChannel destination = null;
		 * 
		 * try { source = new FileInputStream(sourceFile).getChannel();
		 * destination = new FileOutputStream(destFile).getChannel();
		 * destination.transferFrom(source, 0, source.size()); } catch
		 * (FileNotFoundException e) { // TODO Auto-generated catch block
		 * e.printStackTrace(); } catch (IOException e) { // TODO Auto-generated
		 * catch block e.printStackTrace(); } finally { if(source != null) { try
		 * { source.close(); } catch (IOException e) { // TODO Auto-generated
		 * catch block e.printStackTrace(); } } if(destination != null) { try {
		 * destination.close(); } catch (IOException e) { // TODO Auto-generated
		 * catch block e.printStackTrace(); } } } //
		 */        
		IFile file = proj.getFile("main.c");
		try {

			file.create(Activator.getDefault().getFileInputStream("template.c"), false, null);
			
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

		
		
		return super.continueCreation(proj);
	}

	@Override
	public void addPages() {
		// Change main page title
		// Change Main page description
		fMainPage = new CDTMainWizardPage("MainPage");
		fMainPage.setTitle("EV3 Wizard");
		fMainPage
				.setDescription("Creates a new LEGO® Mindstorms® EV3 C-Project");
		addPage(fMainPage);

	}

}
