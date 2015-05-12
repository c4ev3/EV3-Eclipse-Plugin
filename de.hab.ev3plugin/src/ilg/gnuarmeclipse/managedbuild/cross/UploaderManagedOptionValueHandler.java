package ilg.gnuarmeclipse.managedbuild.cross;

import org.eclipse.cdt.managedbuilder.core.BuildException;
import org.eclipse.cdt.managedbuilder.core.IBuildObject;
import org.eclipse.cdt.managedbuilder.core.IConfiguration;
import org.eclipse.cdt.managedbuilder.core.IHoldsOptions;
import org.eclipse.cdt.managedbuilder.core.IOption;
import org.eclipse.cdt.managedbuilder.core.ManagedOptionValueHandler;

import de.hab.ev3plugin.util.Gui;

public class UploaderManagedOptionValueHandler extends ManagedOptionValueHandler {

	@Override
	public boolean handleValue(IBuildObject configuration,
			IHoldsOptions holder, IOption option, String extraArgument,
			int event) {

		// ManagedOptionValueHandlerDebug.dump(configuration, holder, option,
		// extraArgument, event);

		if (event == EVENT_OPEN) {

			IConfiguration config = Utils.getConfiguration(configuration);
			String path = ProjectStorage.getValue(config, "uploader");
			if (path.length() > 0) {
				// overwrite the .cproject definition only if the
				// workspace definition is useful
				IOption optionToSet;
				try {
					optionToSet = holder.getOptionToSet(option, false);
					optionToSet.setValue(path);
				} catch (BuildException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		} else if (event == EVENT_APPLY) {
			
			// Clear discovered includes and macros, to make room for
			// new ones
			// does not work like this :-(
			// SpecsProvider.clear();

			// save (quite often to my taste) the value
			String path = (String) option.getValue();
			IConfiguration config = Utils.getConfiguration(configuration);
			ProjectStorage.putValue(config, "uploader", path);

			// the event was handled
			return true;
		}

		return false;
	}

}
