<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
    <%@ taglib prefix = "n" tagdir = "/WEB-INF/tags"%>
<div class="cartBlock">
    <div><a href="${pageContext.request.contextPath}/products" class="modern">${bundle.getString('cart.backToMarket')}</a></div>
    <div id="cart">
            <div class="header">
                <img src = "${pageContext.request.contextPath}/static/img/order/cart.png">
            </div>
            <c:choose> 
                <c:when test="${not empty productCart.productMap.entrySet()}">
                    <div class="body">
                        <div class="orderItems">
                            <table class="table orderItem">
                                 <thead>
                                    <tr>
                                        <th>${bundle.getString('cart.photo')}</th>
                                        <th>${bundle.getString('cart.count')}</th>
                                        <th>${bundle.getString('cart.price')}</th>
                                        <th>${bundle.getString('cart.total')}</th>
                                        <th>${bundle.getString('cart.action')}</th>
                                    </tr>
                                </thead>
                                <c:forEach var="item" items="${productCart.productMap.entrySet()}">
                                    <tr id="orderItem_${item.getKey().productId}">
                                        <td class="text-center pic">
                                            <img src="${pageContext.request.contextPath}/static/img/product/download.jpg"><br/>
                                            ${item.getKey().productName}
                                        </td>
                                        <td class="count text-center">
                                            <input type="number" value="${item.getValue()}" class="productCounter" min="1" max="1000"
                                                   id="count_${item.getKey().productId}"/>
                                        </td>
                                        <td class="price text-center">$ <span id="price_${item.getKey().productId}">${item.getKey().price}</span></td>
                                        <td class="total text-center ">$ <span id="totalItemCost_${item.getKey().productId}">${item.getKey().price * item.getValue()}</span></td>
                                        <td class="action text-center">
                                            <button type="button" class="btn btn-danger removeProduct" id="remove_${item.getKey().productId}">${bundle.getString('cart.remove')}
                                            </button>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </table>
                        </div>
                    </div>
                    <div class="footer">
                         <button type="button" class="btn btn-danger" id="removeAll">${bundle.getString('cart.clean')}</button>
                    </div>
                </c:when>
                <c:otherwise>
                    <div class="body">
                        <p>${bundle.getString('cart.empty')}</p>
                    </div>
                    <div></div>
                </c:otherwise>
            </c:choose>
    </div>     

    <div style= "text-align: center">
        <c:if test="${not empty productCart.productMap.entrySet()}"><a href="${pageContext.request.contextPath}/makeOrder" id="doOrderButton" class="modern">${bundle.getString('cart.doOrder')} <p id='totalCost'></p></a></c:if></div>
</div>
<script src = "${pageContext.request.contextPath}/static/js/productCart.js"></script>



