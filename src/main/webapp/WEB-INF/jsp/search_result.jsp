
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<body>
<h3> List of movie results for the query : </h3>
 <h3> <i> ${query} </i></h3>


<% int i = 0;  %>

<c:forEach items="${titles}" var="t">

<ul>
      <% out.print(i+1); %>  <a href = "display?i=<% out.print(i); %>" >  ${t} </a>
     </ul>
     <% i = i + 1;
       %>
</c:forEach>

<h3> Performance metrics  </h3>
<h3> <i> Precision : ${precision} </i> </h3>
<h3> <i> Recall : ${recall} </i> </h3>
</body>
</html>