package de.hab.ev3plugin.progress;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

public class Progress extends TitleAreaDialog {
	private Text text;
	private ProgressBar progressBar;

	/**
	 * Create the dialog.
	 * 
	 * @param parentShell
	 */
	public Progress(Shell parentShell) {
		super(parentShell);
	}

	/**
	 * Create contents of the dialog.
	 * 
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		setMessage("Please hold.");
		setTitleImage(SWTResourceManager.getImage("icons/EV3_Icon.ico"));
		setTitle("Uploading to EV3...");
		setDialogHelpAvailable(false);
		setHelpAvailable(false);
		parent.setToolTipText("");
		Composite area = (Composite) super.createDialogArea(parent);
		Composite container = new Composite(area, SWT.NONE);
		container.setLayoutData(new GridData(GridData.FILL_BOTH));

		progressBar = new ProgressBar(container, SWT.HORIZONTAL);
		// progressBar.setState(SWT.ERROR);
		progressBar.setBounds(21, 10, 413, 34);
		progressBar.setMinimum(0);
		progressBar.setMaximum(100);
		RowData rowData = new RowData();
		progressBar.setLayoutData(rowData);

		text = new Text(container, SWT.BORDER | SWT.READ_ONLY | SWT.V_SCROLL
				| SWT.MULTI);
		text.setEditable(false);
		text.setBounds(21, 50, 413, 95);
		return area;
	}

	/**
	 * Create contents of the button bar.
	 * 
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		Button button = createButton(parent, IDialogConstants.OK_ID, "Abort",
				true);
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
			}
		});
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(450, 300);
	}

	public void setProgress(int percentage, String status) {
		progressBar.setSelection(percentage);
		setMessage(status, IMessageProvider.INFORMATION);
	}

	public void setProgress(int percentage) {
		progressBar.setSelection(percentage);
	}

	public void log(String line) {
		text.append(line);
	}

}
