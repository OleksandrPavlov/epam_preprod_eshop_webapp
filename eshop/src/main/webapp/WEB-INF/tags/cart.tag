<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<div class="cart">
    <form action="${pageContext.request.contextPath}/product-cart" method="GET">
        <button type="submit" class="btn btn-primary "  style="background: transparent; border-color: #e8e8e8">
        <c:out value="${bundle.getString('products.cart')}"/> <span id="productCartItemCount" class="badge badge-light">0</span>
        </button>
    </form>
</div>
