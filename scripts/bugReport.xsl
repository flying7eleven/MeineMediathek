<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns="http://www.w3.org/1999/xhtml" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output method="xml" indent="yes" encoding="UTF-8"/>
	
	<!-- TODO -->
	<xsl:template match="/BugReport">
		<html>
			<head>
				<title>Bug Report</title>
			</head>
			<body>
				<h1>Bug Report for <xsl:value-of select="@appVersion" /> (used on Adnroid <xsl:value-of select="@androidVersion" />)</h1>
				<ul>
					<xsl:apply-templates select="ExceptionList" />
				</ul>
			</body>
		</html>
	</xsl:template>
	
	<!-- TODO -->
	<xsl:template match="/ExceptionList">
		<li>
			<xsl:apply-templates select="Exception" />
		</li>
	</xsl:template>
	
	<!-- TODO -->
	<xsl:template match="/Exception">
		<li>
			<xsl:text>1</xsl:text>
		</li>
	</xsl:template>
 
</xsl:stylesheet>