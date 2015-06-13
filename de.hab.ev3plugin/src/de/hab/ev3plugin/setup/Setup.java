package de.hab.ev3plugin.setup;

import java.util.Arrays;

import org.eclipse.cdt.managedbuilder.core.BuildException;
import org.eclipse.cdt.managedbuilder.core.IConfiguration;
import org.eclipse.cdt.managedbuilder.core.IManagedBuildInfo;
import org.eclipse.cdt.managedbuilder.core.IManagedProject;
import org.eclipse.cdt.managedbuilder.core.IOption;
import org.eclipse.cdt.managedbuilder.core.ITool;
import org.eclipse.cdt.managedbuilder.core.IToolChain;
import org.eclipse.cdt.managedbuilder.core.ManagedBuildManager;
import org.eclipse.core.resources.IProject;

import de.hab.ev3plugin.Activator;

public class Setup {
	public static void setDefaults(IProject proj) {
		configOptions(proj, "C:\\EV3\\API"); //TODO: FIXME
	}

	public static void configOptions(IProject proj, String api) {

		// Get storageModule ModuleId = "org.eclipse.cdt.core.settings"
		IManagedBuildInfo info = ManagedBuildManager.getBuildInfo(proj);

		// Get storageModule moduleId = "cdtBuildSystem"
		IManagedProject sub_info = info.getManagedProject();

		// Get configuration name
		IConfiguration config = sub_info.getConfigurations()[0];

		// Get toolChain
		IToolChain toolchain = config.getToolChain();

		// Get list of all tools
		ITool[] toolList = toolchain.getTools();

		// Get Cross Compiler toolname = "Cross GCC Compiler"
		ITool crossTool = toolList[0];
		for (ITool t : toolList) {
			if (t.getName().toString().matches("Cross GCC Compiler")) {
				crossTool = t;
				System.out.println("ID: " + t.getId());
				System.out.println("Name:" + t.getName());
				System.out.println("Cross GCC Compiler gefunden!");
			}
		}
		// Get Cross Linker toolname = "Cross GCC Linker"
		ITool linkTool = toolList[0];
		for (ITool t : toolList) {
			if (t.getName().toString().matches("Cross GCC Linker")) {
				linkTool = t;
				System.out.println("ID: " + t.getId());
				System.out.println("Name:" + t.getName());
				System.out.println("Cross GCC Linker gefunden!");
			}
		}

		// Find options to configure
		/*
		try {
			toolchain.getOptionBySuperClassId(
					"cdt.managedbuild.option.gnu.cross.prefix")
					.getDefaultValue();
		} catch (Exception e) { // evil laughter
			toolchain.createOption(null, "cdt.managedbuild.option.gnu.cross.prefix", "", false);
			
			ManagedBuildManager.setOption(config, crossTool, crossTool
			.getOptionBySuperClassId("cdt.managedbuild.option.gnu.cross.prefix"), "arm-none-linux-gnueabi-");
			
			ManagedBuildManager.setOption(config, crossTool, crossTool
			.getOptionBySuperClassId("cdt.managedbuild.option.gnu.cross.path"), "C:\\CSLite\\bin");
			/*crossTool.createOption(null,
					"cdt.managedbuild.option.gnu.cross.prefix",
					"arm-none-linux-gnueabi-", false);
			crossTool.createOption(null,
					"cdt.managedbuild.option.gnu.cross.path",
					"C:\\CSLite\\bin", false);
*//*
		}
		Activator.log("not here:"
			+ toolchain
						.getOptionBySuperClassId(
								"cdt.managedbuild.option.gnu.cross.prefix")
						.getDefaultValue().toString());
*/
		// Toolchain
		// Already set up in during the wizard process
		// final IOption PREFIXoption =
		// toolchain.getOptionBySuperClassId("cdt.managedbuild.option.gnu.cross.prefix");
		// final IOption PATHoption =
		// toolchain.getOptionBySuperClassId("cdt.managedbuild.option.gnu.cross.path");

		// Compiler
		final IOption INCPATHoption = crossTool
				.getOptionBySuperClassId("gnu.c.compiler.option.include.paths");
		final IOption OPTIIZATIONoption = crossTool
				.getOptionBySuperClassId("gnu.c.compiler.option.optimization.level");
		final IOption DEBUGoption = crossTool
				.getOptionBySuperClassId("gnu.c.compiler.option.debugging.level");
		// Linker
		final IOption LIBPATHoption = linkTool
				.getOptionBySuperClassId("gnu.c.link.option.paths");
		final IOption SHAREDoption = linkTool
				.getOptionBySuperClassId("gnu.c.link.option.shared");

		// Lets see Include Path
		System.out.println(INCPATHoption.toString());
		System.out.println(INCPATHoption.getValue());
		// Lets see Libary Path
		System.out.println(LIBPATHoption.toString());
		System.out.println(LIBPATHoption.getValue());

		// Lets change and optimize the options for the EV3 project

		// INCLUDES: Add Lego C API
		// already added before?
		if (!INCPATHoption.getValue().toString().contains(api)) {
			String[] optvalues = {};
			// Get list of old values
			try {
				optvalues = INCPATHoption.getBasicStringListValue();
			} catch (BuildException e) {

				e.printStackTrace();
			}
			// Add the new value to the list
			String[] new_optvalues = append(optvalues, api);
			// Store option
			ManagedBuildManager.setOption(config, crossTool, INCPATHoption,
					new_optvalues);
			ManagedBuildManager.saveBuildInfo(proj, true);
		}

		// LIBRARIES: Add Lego C API
		// already added before?
		if (!LIBPATHoption.getValue().toString().contains(api)) {
			String[] optvalues = {};
			// Get list of old values
			try {
				optvalues = LIBPATHoption.getBasicStringListValue();
			} catch (BuildException e) {

				e.printStackTrace();
			}
			// Add the new value to the list
			String[] new_optvalues = append(optvalues, api);
			// Store option
			ManagedBuildManager.setOption(config, linkTool, LIBPATHoption,
					new_optvalues);
			ManagedBuildManager.saveBuildInfo(proj, true);
		}

		// Compiler Optimization Level: Set Optimize for size (-Os)
		ManagedBuildManager.setOption(config, crossTool, OPTIIZATIONoption,
				"gnu.c.optimization.level.size");
		ManagedBuildManager.saveBuildInfo(proj, true);

		// Compiler Debug Level: Set None
		ManagedBuildManager.setOption(config, crossTool, DEBUGoption,
				"gnu.c.debugging.level.none");
		ManagedBuildManager.saveBuildInfo(proj, true);

		// Configuration: Set Artifacttype to Shared Library
		try {
			config.setBuildArtefactType("org.eclipse.cdt.build.core.buildArtefactType.sharedLib");
		} catch (BuildException e) {

			e.printStackTrace();
		} finally {
			config.setArtifactExtension("");
			// Linker Output Prefix: Set empty
			linkTool.setOutputPrefix("");

			// Linker Shared Libary settings: Set -share to false
			ManagedBuildManager
					.setOption(config, linkTool, SHAREDoption, false);
			ManagedBuildManager.saveBuildInfo(proj, true);
		}

	}

	// Add a new element to a list and returns the list
	private static <T> T[] append(T[] arr, T element) {
		final int N = arr.length;
		arr = Arrays.copyOf(arr, N + 1);
		arr[N] = element;
		return arr;
	}
}
