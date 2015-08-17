package de.hab.ev3plugin.filebrowser;

import ilg.gnuarmeclipse.managedbuild.cross.SharedStorage;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IOpenListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.OpenEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

import de.hab.ev3plugin.Ev3Duder;
import de.hab.ev3plugin.filebrowser.provider.FileContentProvider;
import de.hab.ev3plugin.filebrowser.provider.FileLabelProvider;
import de.hab.ev3plugin.filebrowser.Ev3File;
import de.hab.ev3plugin.util.IO;

/* This is responsible for the root directory and
 * the action happending when clicking
 */

public class View extends ViewPart {
	public static final String ID = "de.hab.ev3plugin.filebrowser.view";
	private TreeViewer viewer;

	public void createPartControl(Composite parent) {
		final Ev3Duder ev3;
		String uploader_path;
		final Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();

		String m_selectedToolchainName = SharedStorage.getToolchainName();
		String crossCommandUploader = SharedStorage
				.getToolchainUploader(m_selectedToolchainName);

		uploader_path = crossCommandUploader;
		if (System.getProperty("os.name").startsWith("Windows"))
		{
			if (crossCommandUploader.isEmpty())
				uploader_path = "C:\\ev3\\uploader";
			uploader_path += "\\ev3duder.exe";
		}
		else
			uploader_path += "/ev3duder"; // let's hope it's in path

		final String details = uploader_path;
		ev3 = new Ev3Duder(uploader_path, null);

		viewer = new TreeViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
		viewer.setContentProvider(new FileContentProvider());
		viewer.setLabelProvider(new FileLabelProvider());
		viewer.setInput(Ev3File.listRoots(ev3));
		viewer.addOpenListener(new IOpenListener() {
			@Override
			public void open(OpenEvent event) {
				IStructuredSelection selection = (IStructuredSelection) event
						.getSelection();
				// if (((Pair<Boolean, Object>)selection.getFirstElement()).left
				// == false)
				Ev3File file = (Ev3File) selection.getFirstElement();
				if (file.getName().equals("- click to refresh")) {
					// viewer.setInput(Ev3File.listRoots(ev3));
					viewer.refresh();
				} else
					// MessageDialog.openInformation(null, "Ev3File",
					// "info here later")
					;
			}
		});
		MenuManager menuMgr = new MenuManager();
        Menu menu = menuMgr.createContextMenu(viewer.getControl());

		menuMgr.addMenuListener(new IMenuListener() {
			@Override
			public void menuAboutToShow(IMenuManager manager) {
				if (viewer.getSelection().isEmpty()) {
					return;
				}

				if (viewer.getSelection() instanceof IStructuredSelection) {
					IStructuredSelection selection = (IStructuredSelection) viewer
							.getSelection();
					final Ev3File file = (Ev3File) selection.getFirstElement();
					switch(Ev3File.getKind(file))
					{
					case RESTRICTED:
					case REFRESH:
					case DRIVE:
						break;
					case DIRECTORY:
					manager.add(new Action("Upload") {
						public void run() {
							FileDialog fileDialog = new FileDialog(
									shell, SWT.APPLICATION_MODAL);
							String browsedFile = fileDialog.open();
							if (browsedFile.isEmpty())
								return;
							ev3.command("up", browsedFile, file.getFullPath() + "/" + IO.getName(browsedFile));
						} });
					break;
					case FILE:
					manager.add(new Action("Download") {
						public void run() {
							DirectoryDialog dirDialog = new DirectoryDialog(
									shell, SWT.APPLICATION_MODAL);
							String browsedDirectory = dirDialog.open();
							if (browsedDirectory.isEmpty())
								return;
							ev3.command("dl", file.getFullPath(), browsedDirectory + "/" + file.getName());
						} });

					if (IO.getExtension(file.getFullPath()).equals("rbf"))
						manager.add(new Action("Run") {
							public void run() {
								ev3.command("run", file.getFullPath());
								
						} });
					else if (IO.getExtension(file.getFullPath()).equals("elf"))
						ev3.command("exec", file.getFullPath());
					else
						/* normal files? ignore them */;
						break;
					}
					manager.add(new Action("Details") {
						public void run() {
							String path = file.getFullPath() != null ? file.getFullPath() : "n/a";
							String size = file.getSize() != -1 ? new Integer(file.getSize()).toString() : "n/a";
							String hash = file.getHash() != null ? file.getHash() : "n/a";

							MessageDialog.openInformation(null, "Details",
									"File Name:\t" + file.getFullPath() + "\n" +
									"File Size:\t" + size + "\n" +
									"File Kind:\t" + Ev3File.getKind(file).name() + "\n" +
									"File Hash:\t" + hash + "\n\n" +
									"Uploader:\t" + details
							);
						}});
				}
			}
		});
		/*MenuItem menuDetails = new MenuItem(menu, 0);
		menuDetails.setEnabled(true);
		menuDetails.setText("HEHE");
		*/
		menuMgr.setRemoveAllWhenShown(true);
		viewer.getControl().setMenu(menu);

	}

	public void setFocus() {
		viewer.getControl().setFocus();
	}
}