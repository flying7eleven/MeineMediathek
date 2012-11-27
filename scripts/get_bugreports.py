import getpass
import imaplib
import email
import re

emailAddressRegEx = re.compile( '[A-Za-z]+[A-Za-z0-9\\.\\_\\-]*\\@[A-Za-z0-9\\.\\_\\-]*\\.[a-zA-Z]{2,4}' )
bugReportFieldId = re.compile( '([A-Z0-9\\_]{2,})\\=(.*)' )
includedExceptionsRegEx = re.compile( '([\\.a-z]{0,}[A-Za-z]{1,}Exception)' )

class BugReport(object):
	def __init__( self, content, reporter ):
		self._reportDict = {}
		self._reporterAddress = reporter
		self._reportLines = content.splitlines()
		self._includedExceptions = []

		#
		for currentException in includedExceptionsRegEx.findall( content ):
			self._includedExceptions.append( currentException )
		self._includedExceptions = set( sorted( self._includedExceptions ) )

		#
		tmpKeyId = None
		tmpKeyContent = []
		for currentReportLine in self._reportLines:
			currentResult = bugReportFieldId.findall( currentReportLine )
			if len( currentResult ) > 0:
				#
				if tmpKeyId:
					self._reportDict[ tmpKeyId ] = tmpKeyContent
					tmpKeyContent = []

				#
				tmpKeyId = currentResult[ 0 ][ 0 ].lower()
				tmpKeyContent.append( currentResult[ 0 ][ 1 ] )
			else:
				tmpKeyContent.append( currentReportLine )

	def __str__( self ):
		return ( "BugReport for v%s (Android %s)" % ( self._reportDict[ 'app_version_name' ][ 0 ], self._reportDict[ 'android_version' ][ 0 ] ) )

	def getXML( self ):
		# start the document
		htmlCode = '<?xml version="1.0" encoding="UTF-8" standalone="yes"?><?xml-stylesheet href="bugReport.xsl" type="text/xsl" ?>'
		htmlCode += '<BugReport appVersion="%s" androidVersion="%s" bugReporter="%s">' % ( self._reportDict[ 'app_version_name' ][ 0 ], self._reportDict[ 'android_version' ][ 0 ], self._reporterAddress )	

		#
		htmlCode += '<ExceptionList>'
		for currentException in self._includedExceptions:
			htmlCode += '<Exception>%s</Exception>' % currentException
		htmlCode += '</ExceptionList>'

		# loop through all fields in the dictionary
		htmlCode += '<AcraReport>'
		for currentKey in self._reportDict.keys():
			htmlCode += '<Entry id="%s" lines="%d">' % ( currentKey, len( self._reportDict[ currentKey ] ) )
			if len( self._reportDict[ currentKey ] ) > 1:
				for currentLine in self._reportDict[ currentKey ]:
					htmlCode += '<Line>%s</Line>' % currentLine
			else:
				htmlCode += '%s' % self._reportDict[ currentKey ][ 0 ]
			htmlCode += '</Entry>'
		htmlCode += '</AcraReport>'

		# close the HTML tags and return the generated code
		htmlCode += '</BugReport>'
		return htmlCode

	def writeXML( self, filename = 'bugreport.xml' ):
		htmlCode = self.getXML()
		try:
			outputFile = open( filename, 'w' )
			outputFile.write( htmlCode )
		finally:
			outputFile.close()

def get_first_text_block( email_message_instance ):
	maintype = email_message_instance.get_content_maintype()
	if maintype == 'multipart':
		for part in email_message_instance.get_payload():
			if part.get_content_maintype() == 'text':
				return part.get_payload()
	elif maintype == 'text':
		return email_message_instance.get_payload()
	else:
		return ''

if __name__ == '__main__':
	# ask the user for the login information
	loginUsername = raw_input( 'Username: ' )
	loginPassword = getpass.getpass()

	# try to establish a connection to the email server
	mailClient = imaplib.IMAP4_SSL( 'imap.gmail.com' )

	# login wit the provided information
	mailClient.login( loginUsername, loginPassword )
	mailClient.select( 'INBOX' )

	#
	result, data = mailClient.uid( 'search', None, '(HEADER Subject "com.halcyonwaves.apps.meinemediathek Crash Report")' )
	foundReportIds = data[ 0 ].split()

	#
	i = 0
	for currentEmailId in foundReportIds:
		#
		result, data = mailClient.uid( 'fetch', currentEmailId, '(RFC822)' )
		rawEmail = data[ 0 ][ 1 ]
		parsedEmailMessage = email.message_from_string( rawEmail )
		
		#
		bugReportSender = emailAddressRegEx.findall( parsedEmailMessage[ 'From' ] )[ 0 ]
		bugReportContent = get_first_text_block( parsedEmailMessage )

		#
		bug = BugReport( bugReportContent, bugReportSender )
		bug.writeXML( 'bugreport_%04d.xml' % i )

		i += 1

	# logout again
	mailClient.logout()
