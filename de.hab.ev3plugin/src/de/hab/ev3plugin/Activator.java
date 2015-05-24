package de.hab.ev3plugin;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.ISelectionService;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "de.hab.ev3plugin"; //$NON-NLS-1$

	// The shared instance
	private static Activator plugin;

	private String location;

	/**
	 * The constructor
	 */
	public Activator() {
		// location = Platform.getStateLocation(Platform.getBundle(PLUGIN_ID));
		// URL url = FileLocator.find(context.getBundle(), new ("/"), null);
		// location FileLocator.toFileURL(url);

		try {
			URL url = FileLocator.find(Platform.getBundle(PLUGIN_ID), new Path(
					"/resources/"), null);
			if (url != null)
				location = new File(FileLocator.resolve(url).toURI())
						.getCanonicalPath();
		} catch (Exception e) {
			location = "C:\\EV3";
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext
	 * )
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext
	 * )
	 */
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static Activator getDefault() {
		return plugin;
	}

	/**
	 * Returns an image descriptor for the image file at the given plug-in
	 * relative path
	 *
	 * @param path
	 *            the path
	 * @return the image descriptor
	 */
	public static ImageDescriptor getImageDescriptor(String path) {
		return imageDescriptorFromPlugin(PLUGIN_ID, path);
	}

	public String getResources() {
		return location;
	}

	public IProject getActiveProject() {
		ISelectionService ss = plugin.getWorkbench().getActiveWorkbenchWindow()
				.getSelectionService();
		String projExpID = "org.eclipse.ui.navigator.ProjectExplorer";
		ISelection sel = ss.getSelection(projExpID);
		Object selectedObject = sel;
		if (sel instanceof IStructuredSelection) {
			selectedObject = ((IStructuredSelection) sel).getFirstElement();
		}
		if (selectedObject instanceof IAdaptable) {
			IResource res = (IResource) ((IAdaptable) selectedObject)
					.getAdapter(IResource.class);
			IProject project = res.getProject();
			return project;
		}
		return null;
	}

	public InputStream getFileInputStream(String path) {
		if (location.equals("C:\\EV3")) {
			try {
				return new FileInputStream("C:\\EV3" + path);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else
			try {
				return FileLocator.openStream(Platform.getBundle(PLUGIN_ID),
						new Path("/resources/" + path), false);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		return null;
	}

	public static void log(String msg) {
		log(msg, null);
	}

	public static void log(String msg, Exception e) {
	//	plugin.getLog().log(
		//		new Status(Status.INFO, PLUGIN_ID, Status.OK, msg, e));
	}
}
