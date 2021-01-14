<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ attribute name="notificationContainerName" required = "true" rtexprvalue="true"%>
<%@ attribute name="msgKey" required = "true" rtexprvalue="true"%>

<c:set var = "notificationContainer" value="${notificationContext.get(notificationContainerName)}"/>
<c:if test = "${notificationContainer != null}">
     <c:set var = "notificationMap" value="${notificationContainer.notificationContainer}"/>
     <c:if test = "${notificationMap!=null}">
        <c:set var = "msg" value="${notificationMap.get(msgKey)}"/>
        <c:if test="${msg!=null}">
        <div class="error">
            <c:out value="${msg}"/>
        </div>
        </c:if>
     </c:if>
</c:if>

