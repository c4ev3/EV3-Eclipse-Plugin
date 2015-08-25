package de.hab.ev3plugin.choose;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

public class ChooseEv3 extends TitleAreaDialog {
	
	public interface Handler {
		boolean isValid(String id);
		String fetchSerial();
	}
	private static final int CONNECT_ID = IDialogConstants.CLIENT_ID + 1;
			
	private Text txtNull;
	private Handler handler = null;
	private Composite parent;

	/**
	 * Create the dialog.
	 * @param parentShell
	 */
	public ChooseEv3(Shell parentShell) {
		super(parentShell);
	}
	public void setHandler(Handler handler) { this.handler = handler; }

	/**
	 * Create contents of the dialog.
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		this.parent = parent.getParent().getParent(); // طلع سلسفيل أهله ;-)
		setTitle("Choose Ev3");
		setMessage("Please enter either the serial number of the device or its IP address\nThe pairing process might take up to 6s, be patient.");
		Composite area = (Composite) super.createDialogArea(parent);
		Composite container = new Composite(area, SWT.NONE);
		container.setLayoutData(new GridData(GridData.FILL_BOTH));

		txtNull = new Text(container, SWT.BORDER);
		txtNull.setFont(SWTResourceManager.getFont(".Helvetica Neue DeskInterface", 20, SWT.NORMAL));
		txtNull.setBounds(10, 10, 272, 29);
		
		Button btnNewButton = new Button(container, SWT.NONE);
		btnNewButton.setFont(SWTResourceManager.getFont(".Helvetica Neue DeskInterface", 13, SWT.NORMAL));
		btnNewButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String serial = handler.fetchSerial();
				if (serial != null)
					txtNull.setText(serial);;
			}
		});
		btnNewButton.setBounds(288, 8, 162, 34);
		btnNewButton.setText("Fetch serial over USB");

		return area;
	}

	/**
	 * Create contents of the button bar.
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite dialog) {
		Button btnAttemptPairing = createButton(dialog, CONNECT_ID, IDialogConstants.OK_LABEL,
				true);
		btnAttemptPairing.setText("Pair EV3");
		btnAttemptPairing.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (handler.isValid(txtNull.getText()))
				{
					parent.setVisible(false);
					parent.dispose();
				}
			}
		});
		createButton(dialog, IDialogConstants.CANCEL_ID,
				IDialogConstants.CANCEL_LABEL, false);
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(460, 190);
	}
}
