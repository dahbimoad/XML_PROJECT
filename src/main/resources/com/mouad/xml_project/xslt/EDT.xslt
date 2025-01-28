<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:emp="http://GINF2Emploi.org"
    exclude-result-prefixes="emp">

    <xsl:output method="html" indent="yes"/>

    <!-- Key for efficient lookup -->
    <xsl:key name="dayTimeKey" match="emp:matiere" use="concat(emp:jour, '|', emp:startTime)" />

    <xsl:template match="/">
        <html>
            <head>
                <style>
                    table {
                        border-collapse: collapse;
                        width: 70%;
                        margin: 20px auto;
                    }
                    th, td {
                        border: 1px solid #ddd;
                        padding: 8px;
                        text-align: center;
                    }
                    th {
                        background-color: #f2f2f2;
                    }
                    td {
                        vertical-align: top;
                    }
                    .session-block {
                        padding: 6px;
                        margin-bottom: 4px;
                        border-radius: 4px;
                        color: white;
                    }
                    .type-CM {
                        background-color: #3498db;
                    }
                    .type-TP {
                        background-color: #27ae60;
                    }
                    .type-TD {
                        background-color: #e74c3c;
                    }
                    .footer {
                        margin: 20px 0;
                        text-align: center;
                    }
                    .legend {
                        display: inline-flex;
                        align-items: center;
                        gap: 20px;
                    }
                    .legend-item {
                        display: flex;
                        align-items: center;
                        gap: 8px;
                    }
                    .legend-color {
                        width: 20px;
                        height: 20px;
                        border-radius: 4px;
                    }
                </style>
            </head>
            <body>
                <h2 style="text-align: center">EDT GINF2</h2>
                <table>
                    <tr>
                        <th>Time</th>
                        <xsl:for-each select="/emp:emploi/emp:Days/emp:day">
                            <th><xsl:value-of select="@name"/></th>
                        </xsl:for-each>
                    </tr>
                    <xsl:apply-templates select="/emp:emploi/emp:times/emp:time"/>
                </table>

                <div class="footer">
                    <div class="legend">
                        <div class="legend-item">
                            <div class="legend-color type-CM"></div>
                            <span>CM</span>
                        </div>
                        <div class="legend-item">
                            <div class="legend-color type-TP"></div>
                            <span>TP</span>
                        </div>
                        <div class="legend-item">
                            <div class="legend-color type-TD"></div>
                            <span>TD</span>
                        </div>
                    </div>
                </div>
            </body>
        </html>
    </xsl:template>

    <xsl:template match="emp:time">
        <tr>
            <td style="background-color: #f2f2f2">
                <xsl:value-of select="@t"/>
            </td>
            <xsl:for-each select="/emp:emploi/emp:Days/emp:day">
                <xsl:variable name="currentDay" select="@name"/>
                <xsl:variable name="currentTime" select="current()/@t"/>
                <td>
                    <xsl:for-each select="/emp:emploi/emp:matieres/emp:matiere[emp:jour=$currentDay and emp:startTime=$currentTime]">
                        <div class="session-block type-{translate(substring-before(emp:type, ' '), 'abcdefghijklmnopqrstuvwxyz', 'ABCDEFGHIJKLMNOPQRSTUVWXYZ')}">
                            <xsl:value-of select="concat(emp:nom, ' - ', emp:nomProf, ' - ', emp:salle)"/>
                        </div>
                    </xsl:for-each>
                </td>
            </xsl:for-each>
        </tr>
    </xsl:template>

</xsl:stylesheet>