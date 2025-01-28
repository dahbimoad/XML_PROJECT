<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:emp="http://GINF2Emploi.org">

  <xsl:output method="html" indent="yes"/>
  <xsl:key name="dayTimeKey" match="emp:matiere" use="concat(emp:jour, emp:startTime)" />

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

  .session-td {
    border-width: 2px;
    margin-bottom: 4px;
  }

  .td-cm {
    background-color: #3498db;
    border-color: #7f8c8d;
    border-radius: 2%;
  }

  .td-tp {
    background-color: #27ae60;
    border-color: #27ae60;
    border-radius: 2%;
  }

  .td-td {
    background-color: #e74c3c;
    border-color: #e74c3c;
    border-radius: 2%;
  }

  .footer {
    margin-top: 20px;
    margin-bottom: 20px;
    font-size: 14px;
    display: flex;
    justify-content: center;
  }

  .legend {
    display: flex;
    font-weight: bold;
  }

  .legend div {
    display: flex;
    align-items: center;
    margin-right: 10px;
  }

  .legend span {
    margin-right : 20px;
  }
  h2 {
    text-align: center;
  }
</style>

      </head>
      <body>
        <h2>EDT GINF2</h2>
        <table>
          <tr>
            <th>Time</th>
            <xsl:for-each select="//emp:Days/emp:day">
              <th>
                <xsl:value-of select="@name"/>
              </th>
            </xsl:for-each>
          </tr>
          <xsl:apply-templates select="//emp:times/emp:time"/>
        </table>
        <div class="footer">
          <div class="legend">
            <div class="td-cm" style="width: 20px; height: 20px;"></div>
            <span>CM</span>
            <div class="td-tp" style="width: 20px; height: 20px;"></div>
            <span>TP</span>
            <div class="td-td" style="width: 20px; height: 20px;"></div>
            <span>TD</span>
          </div>
        </div>
      </body>
    </html>
  </xsl:template>

  <xsl:template match="emp:time">
    <tr>
      <td style="background-color: #f2f2f2; border-right: 2px solid #ddd;"><xsl:value-of select="@t"/></td>
      <xsl:apply-templates select="//emp:Days/emp:day">
        <xsl:with-param name="currentTime" select="@t"/>
      </xsl:apply-templates>
    </tr>
  </xsl:template>

  <xsl:template match="emp:day">
    <xsl:param name="currentTime"/>
    <td>
      <xsl:apply-templates select="key('dayTimeKey', concat(@name, $currentTime))"/>
    </td>
  </xsl:template>

  <xsl:template match="emp:matiere">
    <xsl:param name="currentTime"/>
    <xsl:if test="contains(concat(emp:startTime, emp:endTime), $currentTime)">
      <div style="padding: 6px; color: white;" class="session-td td-{emp:type}">
        <xsl:value-of select="concat(emp:nom, ' - ', emp:nomProf, ' - ', emp:salle)"/>
      </div>
    </xsl:if>
  </xsl:template>

</xsl:stylesheet>
