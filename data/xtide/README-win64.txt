2016-01-25

This archive contains Win64 native xtide command line client executables
built from the original XTide 2.15 source code.  Note that this is *ONLY* the
command-line client (tide).  The interactive GUI (xtide) and web server
interface (xttpd) are not supported for a native Win64 build but can easily
be built in a Cygwin environment.

This build was performed under Windows 10 64-bit using Visual Studio
Community 2015 with Update 1 following the process described at
http://www.flaterco.com/xtide/installation.html.  To run the binaries without
Visual Studio you need to install the Visual C++ Redistributable Package for
Visual Studio 2015, available free from Microsoft.

tide.exe is the standard build with a workaround for missing time zone
support that suffices for nearby years and U.S. locations.  The beginning of
time is in 1970.

tideutc.exe is built with the bigger time workaround that is normally enabled
by using the --enable-time-workaround configure switch (but in this case by
editing autoconf-defines.h to define TIME_WORKAROUND).  It has no time zone
support at all (all times are in UTC) but you can go back to the year 1700.

These are 64-bit binaries and they probably won't run on anything older than
Win 10 regardless.  On 32-bit Windows you can use the DJGPP-built DOS binary.

For all of the details, documentation and harmonic files, please refer to
http://www.flaterco.com/xtide/.  For using the command line interface
specifically, please refer to http://www.flaterco.com/xtide/tty.html.

The following behaviors will differ from the default Unix behaviors:

- File names in the environment variable HFILE_PATH or the configuration file
xtide.conf should be separated by semicolons instead of colons.

- The file xtide.conf (and any other configuration files that you use) should
go in the current working directory.  Not in PATH folders, not in the
directory where tide.exe executable is.

- Because the Microsoft Visual Studio runtime doesn't support the zoneinfo
database natively, you will get the message "XTide Warning:  Using obsolete
time zone database.  Summer Time (Daylight Savings Time) adjustments will not
be done for some locations."

- Although the program successfully changes the codeset from CP437 to
ISO-8859-1 when it runs, the DOS box reverts to CP437 as soon as the program
exits, so text output that is saved to a file and then TYPEd at the command
prompt will display incorrectly.  The saved output is in the character set
ISO-8859-1 and will display correctly in a web browser or editor.

- PNG format output cannot be redirected from standard output because it gets
corrupted by an ASCII conversion.  You must instead use the -o command line
option to specify the output file.

Thanks to Leonid Tochinski for getting the Visual C++ portability working in
2008.  This build is simply an attempt to maintain that.  I am not equipped
to provide routine help for the Windows platform and cannot respond to "How
do I..." type questions.  However, if there are actual demonstrable bugs or
other problems with this build, please report them to dave@flaterco.com.

DWF
