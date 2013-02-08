# vim: set fileencoding=utf-8 :
import re
import markdown2
from bs4 import BeautifulSoup as soup
from argparse import ArgumentParser, ArgumentTypeError, FileType

htmlPrefix = '<?xml version="1.0" encoding="utf-8"?>\n<changelog>\n'
htmlPostfix = '</changelog>'

# define some required regular expressions
changelogMarkdownIdRegEx = re.compile( '\#\#\ Changelog' )
versionNumberRegEx = re.compile( 'Version (\d\.\d\.?\d?)' )
versionCodeRegEx = re.compile( 'Code: \<em\>(\d*)' )
releaseDateRegEx = re.compile( 'Released on\: \<strong\>\<em\>(\d\d\d\d\-(\d\d|XX)-(\d\d|XX))' )

def escape( string ):
	return string.replace( u'ö', 'oe' ).replace( u'ü', 'ue' ).replace( u'ä', 'ae' ).replace( u'Ö', 'Oe' ).replace( u'Ü', 'Ue' ).replace( u'Ä', 'Ae' )
#	return string.replace( u'ö', '&ouml;' ).replace( u'ü', '&uuml;' ).replace( u'ä', '&auml;' ).replace( u'Ö', '&Ouml;' ).replace( u'Ü', '&Uuml;' ).replace( u'Ä', '&Auml;' )

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
		startIter = changelogMarkdownIdRegEx.finditer( text ).next()	
		changelogText = text[startIter.end():]
	except StopIteration:
		print "Could not find the changelog information in the supplied file. Skipping!"
		exit( -2 )

	# convert the changelog into html to get the links set correctly
	htmlChangelog = markdown2.markdown( changelogText )
	parsedHtml = soup( htmlChangelog )

	# find the facts about the version numbers denoted in the file 
	foundVersionNumbers = versionNumberRegEx.findall( htmlChangelog )
	foundVersionCodes = versionCodeRegEx.findall( htmlChangelog )
	foundReleaseDates = releaseDateRegEx.findall( htmlChangelog )
	foundChangeLists = parsedHtml.find_all( 'ul' )

	# generate the XML representation for the changelog
	xmlChangelog = ''
	for currentRelease in range( 0, len( foundVersionNumbers ) ):
		xmlChangelog += '<release version="%s" versioncode="%s" releasedate="%s">\n' % ( foundVersionNumbers[ currentRelease ], foundVersionCodes[ currentRelease ], foundReleaseDates[ currentRelease ][ 0 ] )
		for currentEntry in foundChangeLists[ currentRelease ].find_all( 'li' ):
			xmlChangelog += '<change>%s</change>\n' % ( escape( ''.join( currentEntry.findAll( text = True ) ) ) )
		xmlChangelog += '</release>\n'

	# write the XML file
	parsedArguments.outputFile.write( htmlPrefix )
	parsedArguments.outputFile.write( xmlChangelog )
	parsedArguments.outputFile.write( htmlPostfix )
