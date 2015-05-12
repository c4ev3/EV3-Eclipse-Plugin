# EV3 Eclipse Plugin
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
More info on LMS's C-like syntax is available *here*

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
