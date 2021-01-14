<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ attribute name="noteType" required = "true" rtexprvalue="true"%>
<%@ attribute name="notificationContainerName" required = "true" rtexprvalue="true"%>
<c:set var = "notificationContainer" value="${notificationContext.get(notificationContainerName)}"/>
<c:if test = "${notificationContainer != null}">
     <c:set var = "notificationMap" value="${notificationContainer.notificationContainer}"/>

      <c:forEach var="note" items="${notificationMap}">
          <div class="alert ${noteType}">
              ${note.value}
          </div>
      </c:forEach>
</c:if>










