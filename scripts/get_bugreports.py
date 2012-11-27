import getpass
import imaplib
import email
import re

emailAddressRegEx = re.compile( '[A-Za-z]+[A-Za-z0-9\\.\\_\\-]*\\@[A-Za-z0-9\\.\\_\\-]*\\.[a-zA-Z]{2,4}' )
bugReportFieldId = re.compile( '([A-Z0-9\\_]{2,})\\=(.*)' )

class BugReport(object):
	def __init__( self, content, reporter ):
		self._reportDict = {}
		self._reporterAddress = reporter
		self._reportLines = content.splitlines()

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

	def getHTML( self ):
		# start the document
		htmlCode = '<!DOCTYPE html><html><head><title>Bug Report for %s</title>' % self._reportDict[ 'app_version_name' ][ 0 ]
		htmlCode += '<link href="bugReportStyle.css" rel="stylesheet" type="text/css">'
		htmlCode += '</head><body>'

		#
		htmlCode += '<h1>BugReport for v%s (Android %s)</h1>' % ( self._reportDict[ 'app_version_name' ][ 0 ], self._reportDict[ 'android_version' ][ 0 ] )
		htmlCode += '<h2>Reported by: <a href="mailto:%s">%s</a></h2>' % ( self._reporterAddress, self._reporterAddress )

		# loop through all fields in the dictionary
		for currentKey in self._reportDict.keys():
			htmlCode += '<div class="bugReportEntry">'
			htmlCode += '<div class="bugReportField">%s</div>' % currentKey
			htmlCode += '<div class="bugReportContent">'
			for currentLine in self._reportDict[ currentKey ]:
				htmlCode += '<div class="bugReportContentLine">%s</div>' % currentLine	
			htmlCode += '</div></div>'

		# close the HTML tags and return the generated code
		htmlCode += '</body></html>'
		return htmlCode

	def writeHTML( self, filename = 'bugreport.html' ):
		htmlCode = self.getHTML()
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
		bug.writeHTML( 'bugreport_%04d.html' % i )

		i += 1

	# logout again
	mailClient.logout()
