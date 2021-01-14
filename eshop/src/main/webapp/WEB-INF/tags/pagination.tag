<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:forEach var="i" begin="1" end="${currentProductPageRules.pageCount}">
    <input type="button" name="productPageNumber" class="user-preference submit-preferences productPage"  value="${i}" <c:if test="${i==currentProductPageRules.currentPage}">disabled='disabled'</c:if>/>
</c:forEach>
