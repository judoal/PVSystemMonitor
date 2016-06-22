# PVSystemMonitor
<<<<<<< HEAD

This system is designed to read the data stream from a photovoltaic system.   Initially, this code works with an Outback system 
with a MATE (not MATE3) communications devices. This sends data over a serial (DB9) stream.   DTR must be high.   It will work with
a serial to ethernet converter (from SparkFun).   However currently I am using the WattPlot software to read the serial stream.  This
software also acts as a 2 client tcp server.   The data stream can then be read remotely.   This data stream is identical to that of 
of the mate output.

This heave lifting is done by the java classes and consists of methods to open tcp stream read it and interpret it according to the 
communications protocols published by Outback.  

MateSimulator simulate the MATE2 reading data previously recorded as matetest.obm.  This runs the MateDataStream class which can be configured (via cmd line) to read a file, the stream from the mate or a stream from the matesimulator.  Currently analyzed data is output to console.  TODO--create static web pages (with java) to be sent to a server.

=======
Monitor serial data stream
>>>>>>> origin/master
