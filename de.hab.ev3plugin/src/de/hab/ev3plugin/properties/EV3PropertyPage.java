package de.hab.ev3plugin.properties;

import java.io.File;
import java.net.MalformedURLException;
import java.util.Arrays;

import org.eclipse.cdt.ui.newui.CDTPropertyManager;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.QualifiedName;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.dialogs.PropertyPage;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.cdt.managedbuilder.core.BuildException;
import org.eclipse.cdt.managedbuilder.core.IConfiguration;
import org.eclipse.cdt.managedbuilder.core.IManagedBuildInfo;
import org.eclipse.cdt.managedbuilder.core.IManagedProject;
import org.eclipse.cdt.managedbuilder.core.IOption;
import org.eclipse.cdt.managedbuilder.core.ITool;
import org.eclipse.cdt.managedbuilder.core.IToolChain;
import org.eclipse.cdt.managedbuilder.core.ManagedBuildManager;

import de.hab.ev3plugin.Activator;

public class EV3PropertyPage extends PropertyPage {

	private static final String API_PROPERTY = "API";
	private static final String LMSASM_PROPERTY = "LMSASM";
	private static final String DEFAULT_API = "C:\\EV3\\API";
	private static final String DEFAULT_LMSASM = "";

	private Text APItext;
	private Composite composite_1;
	private Text LMSASMtext;
	private String projectname;

	/**
	 * Constructor for EV3PropertyPage.
	 */
	public EV3PropertyPage() {
		super();
	}

	private void addSeparator(Composite parent) {
		Label separator = new Label(parent, SWT.SEPARATOR | SWT.HORIZONTAL);
		GridData gridData = new GridData();
		gridData.horizontalAlignment = GridData.FILL;
		gridData.grabExcessHorizontalSpace = true;
		separator.setLayoutData(gridData);
	}

	private void addFirstSection(Composite parent) {
		Composite composite = createDefaultComposite(parent);

		// Label for API field
		Label apiLabel = new Label(composite, SWT.NONE);
		apiLabel.setText("API:");

		// API text field
		APItext = new Text(composite, SWT.SINGLE | SWT.BORDER);
		GridData gd_APItext = new GridData();
		gd_APItext.widthHint = 260;
		APItext.setLayoutData(gd_APItext);

		// Add text to API text field
		try {
			String api = ((IResource) getElement())
					.getPersistentProperty(new QualifiedName("", API_PROPERTY));
			APItext.setText((api != null) ? api : DEFAULT_API);

			Button btnBrowseAPI = new Button(composite_1, SWT.NONE);
			btnBrowseAPI.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					try {
						DirectoryDialog dd = new DirectoryDialog(getShell(),
								SWT.OPEN);
						dd.setText("Open");
						dd.setFilterPath("C:/");
						String selected = dd.open();
						APItext.setText((selected != null) ? selected
								: DEFAULT_API);

					} catch (Exception e1) {

						e1.printStackTrace();
					}

				}
			});
			btnBrowseAPI.setText("Browse");
		} catch (CoreException e) {
			APItext.setText(DEFAULT_API);
		}
	}

	private void addThirdSection(Composite composite) {
		Composite composite_2 = new Composite(composite, SWT.NONE);
		GridLayout gl_composite_2 = new GridLayout();
		gl_composite_2.numColumns = 2;
		composite_2.setLayout(gl_composite_2);
		{
			Button btnNewStarter = new Button(composite_2, SWT.NONE);
			btnNewStarter.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {

					// Create a new projectstarter if starter has been deleted
					// or project has been copied
					if (LMSASMtext.getText() != DEFAULT_LMSASM) {
						NewStarter projectStarter = new NewStarter(projectname,
								LMSASMtext.getText());
						projectStarter.make();
					}

				}
			});
			btnNewStarter.setText("Starter neu erstellen");
		}
	}
	private void addRadioOptions(Composite composite)
	{
		//final Display display = new Display();
	    //Shell shell = new Shell(composite);
		 Group group1 = new Group(composite, SWT.SHADOW_IN);
	    group1.setText("Installation path");
	    group1.setLayout(new RowLayout(SWT.HORIZONTAL));
	    
	    Button[] radios = new Button[3];
	    
	    radios[0] = new Button(group1, SWT.RADIO);
	    radios[0].setText("SD_Card/myapps/");
	    
	    radios[1] = new Button(group1, SWT.RADIO);
	    radios[1].setText("BrkProg_SAVE/");
	    
	    radios[2] = new Button(group1, SWT.RADIO);
	    radios[2].setText("apps/");
	    
	    
	    radios[0].setSelection(true);
	    radios[2].setEnabled(false);
	}
	private void addSecondSection(Composite composite) {
		Composite composite_2 = new Composite(composite, SWT.NONE);
		GridData gd_composite_2 = new GridData(SWT.LEFT, SWT.CENTER, false,
				false, 1, 1);
		gd_composite_2.widthHint = 365;
		composite_2.setLayoutData(gd_composite_2);
		GridLayout gl_composite_2 = new GridLayout();
		gl_composite_2.numColumns = 3;
		composite_2.setLayout(gl_composite_2);
		{
			Label lmsasmlabel = new Label(composite_2, SWT.NONE);
			lmsasmlabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER,
					false, false, 1, 1));
			lmsasmlabel.setText("Lmsasm:");
		}
		{
			LMSASMtext = new Text(composite_2, SWT.BORDER);
			GridData gd_LMSASMtext = new GridData(SWT.FILL, SWT.CENTER, true,
					false, 1, 1);
			gd_LMSASMtext.widthHint = 260;
			LMSASMtext.setLayoutData(gd_LMSASMtext);
		}
		{
			Button btnBrowseLmsasm = new Button(composite_2, SWT.NONE);

			// Add text to the LMSASM text field
			try {
				String lmsasm = ((IResource) getElement())
						.getPersistentProperty(new QualifiedName("",
								LMSASM_PROPERTY));
				LMSASMtext.setText((lmsasm != null) ? lmsasm : DEFAULT_LMSASM);

				btnBrowseLmsasm.addSelectionListener(new SelectionAdapter() {
					@Override
					public void widgetSelected(SelectionEvent e) {

						try {
							DirectoryDialog dd = new DirectoryDialog(
									getShell(), SWT.OPEN);
							dd.setText("Open");
							dd.setFilterPath("C:/");
							String selected = dd.open();
							LMSASMtext.setText((selected != null) ? selected
									: DEFAULT_LMSASM);

						} catch (Exception e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}

					}
				});

				btnBrowseLmsasm.setText("Browse");
			} catch (CoreException e) {
				LMSASMtext.setText(DEFAULT_LMSASM);
			}
		}
	}

	/**
	 * @see PreferencePage#createContents(Composite)
	 */
	protected Control createContents(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		composite.setLayout(layout);
		GridData data = new GridData(GridData.FILL);
		data.grabExcessHorizontalSpace = true;
		composite.setLayoutData(data);

		addFirstSection(composite);
		addSecondSection(composite);
		addRadioOptions(composite);
		addSeparator(composite);
		addThirdSection(composite);

		// Get projectname
		projectname = ((IResource) getElement()).getFullPath().toString(); // returns:
																			// /projectname
		projectname = projectname.substring(1); // cuts of first char
		System.out.println("Projectname:" + projectname);

		return composite;
	}

	private Composite createDefaultComposite(Composite parent) {
		composite_1 = new Composite(parent, SWT.NULL);
		GridLayout gl_composite_1 = new GridLayout();
		gl_composite_1.numColumns = 3;
		composite_1.setLayout(gl_composite_1);

		GridData gd_composite_1 = new GridData();
		gd_composite_1.verticalAlignment = GridData.FILL;
		gd_composite_1.horizontalAlignment = GridData.FILL;
		composite_1.setLayoutData(gd_composite_1);

		return composite_1;
	}

	protected void performDefaults() {
		super.performDefaults();
		if (DEFAULT_API != null)
			APItext.setText(DEFAULT_API);
		if (!DEFAULT_LMSASM.isEmpty()) {
			LMSASMtext.setText(DEFAULT_LMSASM);
		} else {
			LMSASMtext.setText(Activator.getDefault().getResources() + File.separator + "lmsasm");	
		}
	}

	public boolean performOk() {

		// Create projectstarter
		if (LMSASMtext.getText() != DEFAULT_LMSASM) {
			NewStarter projectStarter = new NewStarter(projectname,
					LMSASMtext.getText());
			projectStarter.make();
		}

		// Link the API
		linkApiResource();

		// Change the compileroptions
		// Get Project
				IProject project = ResourcesPlugin.getWorkspace().getRoot()
						.getProject(projectname);
		configOptions(project, APItext.getText());

		CDTPropertyManager.getProjectDescription(this, project);
		CDTPropertyManager.performOkForced(this);

		// store the values in the text fields
		try {

			((IResource) getElement()).setPersistentProperty(new QualifiedName(
					"", API_PROPERTY), APItext.getText());
			((IResource) getElement()).setPersistentProperty(new QualifiedName(
					"", LMSASM_PROPERTY), LMSASMtext.getText());
		} catch (CoreException e) {
			return false;
		}

		return true;
	}

	private void linkApiResource() {
		// Get Project
		IProject project = ResourcesPlugin.getWorkspace().getRoot()
				.getProject(projectname);

		// Link API Folder to the Project
		IFolder link = project.getFolder("ev3");
		if (!link.exists()) {
			if (APItext.getText() != DEFAULT_API) {
				IPath location = new Path(APItext.getText());

				try {
					link.createLink(location, IResource.NONE, null);
				} catch (CoreException e) {

					e.printStackTrace();
				}
			} else {
				// No valid Path selected
			}
		}

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