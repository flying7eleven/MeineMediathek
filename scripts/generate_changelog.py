import re
import markdown2
from argparse import ArgumentParser, ArgumentTypeError, FileType

htmlPrefix = '<html><head><title>Changelog</title></head><body>'
htmlPostfix = '</body></html>'
d = re.compile( '\#\#\ Changelog' )

if __name__ == '__main__':
	# setup the argument parser and do the parsing
	argumentParser = ArgumentParser( description = 'Tool for extracting a changelog out of the README.md file.', epilog = 'This tool was written for Energize. Copyright (c) 2012 by Tim Huetz. Licenced under the terms of the GPLv3.' )
	argumentParser.add_argument( 'inputFile', type = FileType( 'r', 0 ), help = 'the readme file from which the changelog should be extracted' )
	argumentParser.add_argument( 'outputFile', type = FileType( 'w', 0 ), help = 'the file to write the HTML changelog to' )
	parsedArguments = argumentParser.parse_args()

	# check if all required arguments were passed to the application
	if parsedArguments.inputFile == None or parsedArguments.outputFile == None:
		argumentParser.print_help()
		exit( -1 )

	# read the whole readme file into the memory space
	text = parsedArguments.inputFile.read()

	# extract just the changelog information
	try:
		startIter = d.finditer( text ).next()	
		changelogText = text[startIter.start():]
	except StopIteration:
		print "Could not find the changelog information in the supplied file. Skipping!"
		exit( -2 )

	# convert the changelog into html and write it into the output file
	buf = markdown2.markdown( changelogText )
	parsedArguments.outputFile.write( htmlPrefix )
	parsedArguments.outputFile.write( buf )
	parsedArguments.outputFile.write( htmlPostfix )
