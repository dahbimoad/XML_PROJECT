<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:fo="http://www.w3.org/1999/XSL/Format">

    <xsl:output method="xml" indent="yes"/>

    <!-- Match the root element -->
    <xsl:template match="/">
        <fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">
            <fo:layout-master-set>
                <fo:simple-page-master master-name="simplePage" page-width="29cm" page-height="23.7cm"
                    margin-top="1cm" margin-bottom="1cm" margin-left="1cm" margin-right="1cm">
                    <fo:region-body margin-top="3cm" margin-bottom="2cm"/>
                    <fo:region-before extent="2cm"/>
                    <fo:region-after extent="1cm"/>
                </fo:simple-page-master>
            </fo:layout-master-set>
            <fo:page-sequence master-reference="simplePage">
                <fo:flow flow-name="xsl-region-body">
                    <fo:block>
                        <!-- Add your content here -->
                        <fo:table border="1pt solid #ddd" table-layout="fixed" width="100%">
                            <!-- Add table header -->
                            <fo:table-header>
                                <fo:table-row>
                                    <fo:table-cell padding="3pt" text-align="center" background-color="#f2f2f2">
                                        <fo:block>Time</fo:block>
                                    </fo:table-cell>
                                    <!-- Add day headers -->
                                    <xsl:for-each select="/emploi/Days/day">
                                        <fo:table-cell padding="3pt" text-align="center" background-color="#f2f2f2">
                                            <fo:block>
                                                <xsl:value-of select="@name"/>
                                            </fo:block>
                                        </fo:table-cell>
                                    </xsl:for-each>
                                </fo:table-row>
                            </fo:table-header>
                            <!-- Add table body -->
                            <fo:table-body>
                                <!-- Generate table rows for each time -->
                                <xsl:apply-templates select="/emploi/times/time"/>
                            </fo:table-body>
                        </fo:table>
                        <!-- Add your legend or other content -->
                        <fo:block margin-top="10pt">
                            <fo:inline font-weight="bold">Legend: </fo:inline>
                            <fo:inline font-weight="bold">CM</fo:inline>
                            <fo:inline>, </fo:inline>
                            <fo:inline font-weight="bold">TP</fo:inline>
                            <fo:inline>, </fo:inline>
                            <fo:inline font-weight="bold">TD</fo:inline>
                        </fo:block>
                    </fo:block>
                </fo:flow>
            </fo:page-sequence>
        </fo:root>
    </xsl:template>

    <xsl:template match="/emploi/times/time">
        <xsl:variable name="currentTime" select="@t"/>
        <fo:table-row>
            <fo:table-cell padding="3pt" text-align="center" background-color="#f2f2f2">
                <fo:block>
                    <xsl:value-of select="@t"/>
                </fo:block>
            </fo:table-cell>
            <!-- Generate matiere cells for each day -->
            <xsl:for-each select="/emploi/Days/day">
                <fo:table-cell padding="3pt" text-align="center" border="1pt solid #ddd">
                    <fo:block>
                        <xsl:apply-templates select="/emploi/matieres/matiere">
                            <xsl:with-param name="currentTime" select="$currentTime"/>
                            <xsl:with-param name="currentDay" select="@name"/>
                        </xsl:apply-templates>
                    </fo:block>
                </fo:table-cell>
            </xsl:for-each>
        </fo:table-row>
    </xsl:template>

    <xsl:template match="/emploi/matieres/matiere">
        <xsl:param name="currentTime"/>
        <xsl:param name="currentDay"/>

        <xsl:variable name="startTime">
            <xsl:choose>
                <xsl:when test="contains(startTime, '-')">
                    <xsl:value-of select="substring-before(startTime, '-')"/>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:value-of select="startTime"/>
                </xsl:otherwise>
            </xsl:choose>
        </xsl:variable>

        <xsl:variable name="currentStartTime">
            <xsl:choose>
                <xsl:when test="contains($currentTime, '-')">
                    <xsl:value-of select="substring-before($currentTime, '-')"/>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:value-of select="$currentTime"/>
                </xsl:otherwise>
            </xsl:choose>
        </xsl:variable>



        <xsl:if test="$currentDay = jour and  contains(startTime, $currentTime)">
            <fo:block>
                <fo:block-container border-width="1pt" margin-bottom="4px">
                    <fo:block padding="6px" background-color="#f2f2f2" border-color="#ddd" color="black">
                        <xsl:value-of select="concat(nom, ' - ', nomProf, ' - ', salle)"/>
                    </fo:block>
                </fo:block-container>
            </fo:block>
        </xsl:if>
    </xsl:template>



</xsl:stylesheet>