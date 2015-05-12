/*******************************************************************************
 * Copyright (c) 2013 Liviu Ionescu.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Liviu Ionescu - initial version
 *    Ahmad Fatoum (Hochschule Aschaffenburg) - Adding Uploader field
 *******************************************************************************/

package ilg.gnuarmeclipse.managedbuild.cross;

import org.eclipse.cdt.core.templateengine.SharedDefaults;

import de.hab.ev3plugin.util.Gui;

public class SharedStorage {

	// Note: The shared defaults keys don't have "cross" in them because we want
	// to keep
	// compatibility with defaults that were saved when it used to be a template
	static final String SHARED_CROSS_TOOLCHAIN_NAME = Activator.getIdPrefix()
			+ "." + SetCrossCommandWizardPage.CROSS_TOOLCHAIN_NAME;
	static final String SHARED_CROSS_TOOLCHAIN_PATH = Activator.getIdPrefix()
			+ "." + SetCrossCommandWizardPage.CROSS_TOOLCHAIN_PATH;
	static final String SHARED_CROSS_TOOLCHAIN_UPLOADER = Activator.getIdPrefix()
			+ "." + SetCrossCommandWizardPage.CROSS_TOOLCHAIN_UPLOADER;

	public static String getToolchainName() {

		String toolchainName = SharedDefaults.getInstance()
				.getSharedDefaultsMap().get(SHARED_CROSS_TOOLCHAIN_NAME);

		if (toolchainName == null)
			toolchainName = "";

		return toolchainName.trim();
	}
	public static void putToolchainValue(String toolchainName, String sharedValue, String path) {

		String pathKey = sharedValue + "."
				+ toolchainName.trim().hashCode();
		SharedDefaults.getInstance().getSharedDefaultsMap()
				.put(pathKey, path.trim());
	}
	

	public static void putToolchainPath(String toolchainName, String path) {
		putToolchainValue(toolchainName, SHARED_CROSS_TOOLCHAIN_PATH, path);
	}
	public static void putToolchainUploader(String toolchainName, String path) {
		putToolchainValue(toolchainName, SHARED_CROSS_TOOLCHAIN_UPLOADER, path);
	}
	
	public static void putToolchainName(String toolchainName) {

		SharedDefaults.getInstance().getSharedDefaultsMap()
				.put(SHARED_CROSS_TOOLCHAIN_NAME, toolchainName);
	}
	public static String getToolchainValue(String toolchainName, String sharedValue)
	{
		String pathKey = sharedValue + "."
				+ toolchainName.trim().hashCode();
		String sPath = SharedDefaults.getInstance().getSharedDefaultsMap()
				.get(pathKey);

		if (sPath == null) {
			sPath = "";
		}

		return sPath.trim();
	}
	public static String getToolchainPath(String toolchainName)
	{
		return getToolchainValue(toolchainName, SHARED_CROSS_TOOLCHAIN_PATH);
	}
	public static String getToolchainUploader(String toolchainName)
	{
		return getToolchainValue(toolchainName, SHARED_CROSS_TOOLCHAIN_UPLOADER);
	}
	public static void update() {

		SharedDefaults.getInstance().updateShareDefaultsMap(
				SharedDefaults.getInstance().getSharedDefaultsMap());
	}
}
