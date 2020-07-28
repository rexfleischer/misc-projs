<!--
  To change this template, choose Tools | Templates
  and open the template in the editor.
-->

<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Current Projects</title>
  </head>
  <body>
    <table>
      <g:each in="${allprojects}" status="i" var="project">
        <tr>
          <td>${project.name}</td>
          <td>${project.description}</td>
          <td>${project.duedate}</td>
        </tr>
      </g:each>
    </table>
  </body>
</html>
