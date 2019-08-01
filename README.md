# EV3 Eclipse Plugin [![Gitter Chat](https://badges.gitter.im/Join%20Chat.svg)](https://gitter.im/c4ev3/Lobby?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge)

(Refer to [http://c4ev3.github.io](http://c4ev3.github.io) for a general introduction to c4ev3.)

# Cross-compilation
The Codesourcery linux-none-gnueabi toolchain is required. Others might
work too but are not enabled by default.
# Templates
## Starter file
In order to make the ELF-Binaries available in the EV3's LCD Menu, a
specially crafted rbf file is in order.
.rbf files are the LEGO VM's native format and may be assembled
using the assembler.jar utility and related files.
Assembler.jar is a LOGO interpreter, the associated LOGO files
translate .lms source code into the VM's bytecode. Scripting
the VM in availability of C, is probably unnecessary but the launcher
file is still nonetheless provided and can be edited if one pleases.
More info on LMS's C-like syntax is available [here](http://analyticphysics.com/Diversions/Assembly%20Language%20Programming%20for%20LEGO%20Mindstorms%20EV3.htm)

### Special defines
Each start.lms file should contain the defines:
* `destdir` (default: `../prjs/BrkProg_SAVE`)
* `starter` (default: `myapps/${projectName}.rbf`)
* `elfexec` (default: `${projectName}`)

In absence, the default values are used. In absence of the file
altogether, ev3duder's mkrbf is called and a dependency on lms isn't
required then.
The assembler is intelligent enough to not allocate unreferenced
variables, which means that the special defines are most of time only
relevant to the java plugin which parses them at upload.
${projectName} is then expanded into the current project's name.
Curly braces have been chosen in order to differentiate from
$(projectName) which is a valid identifier for the internal "ProjTempl"
project templates.

# Compiling the plugin
## Preparing eclipse
To compile the plugin yourself you need a version of eclipse with the "Eclipse Plugin Development Tools" installed.
Since the plugin is targeted at Eclipse for C/C++ Developers, it would be best to just add the development tools to
your existing eclipse cpp installation.

To do this follow these steps:

1. In eclipse go to `Help->Install new Software...`
2. Select "The Eclipse Project Updates" from the dropdown menu
3. Select "Eclipse Plugin Development Tools" and press next
4. Follow the presented steps until eclipse restarts

## Testing / Compiling the plugin
Download / clone the git repository and import it into eclipse via `Import...->General->Existing Projects into Workspace`.

To run the plugin rightclick on the project and use `Run As->Eclipse Application`.

## Exporting the plugin as jar file
To export the plugin rightclick on the project and use `Export...->Plug-in Development->Deployable plug-ins and fragments`.

Select a the project and a destination directory. Eclipse will create a directory "plugins" and put the jar in there.

# Adding the plugin to eclipse
To add the plugin to eclipse, simply copy the exported jar file into the "dropins" directory in the eclipse installation folder.

# Acknowledgements
This plugin has been originally written for use in the teaching of
Informatics at the Hochschule Aschaffenburg.
In agreement with the university, it's hereby released under the
terms of the Eclipse Public License.

The cross compilation facility is adapted from the GNU ARM Plugin. My
effort was originally an extension of an existing plugin that lacked
communication. This is a complete rewrite but the idea behind
some things stayed same, so thanks to <fname> lindner, its original author.
The business (fun) part is done outside the plugin by the ev3duder
utility. ev3duder is written in GNU C99 and released under the terms of
the GNU General Public License. It's also hosted on Github and
available here.
