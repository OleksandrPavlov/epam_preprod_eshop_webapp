<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="pagination" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="n" tagdir="/WEB-INF/tags"%>
<n:notify notificationContainerName='loginErrorContainer' noteType='warning'/>
<n:notify notificationContainerName='productSearchNotificationContainer' noteType='errorAlert'/>
<n:notify notificationContainerName='captchaCheckNotificationSuccess' noteType='successAlert'/>
<div class="container-fluid">
    <div class="row">
        <!--Here will be some filters-->
        <aside class="col-xs-12 col-sm-4 col-md-3 col-lg-2">
            <!--Feature block -->
            <div class="filterBlock">
                <div class="nameFilter">
                    <input type="text" name="productName" class="user-preference" placeholder=" ${bundle.getString('products.search')}:" value="${currentProductPageRules.productName}"/>
                </div>
                <div class="producerFilter">
                    ${bundle.getString("products.producer")}:
                    <div class="form-group form-check">
                        <input type="radio" class="form-check-input user-preference" id="ProducerLGChbx" name="productProducer" value="LG" <c:if test="${currentProductPageRules.producer=='LG'}">checked</c:if>>
                        <label class="form-check-label" for="ProducerLGChbx">LG</label>
                    </div>
                    <div class="form-group form-check">
                        <input type="radio" class="form-check-input user-preference" id="ProducerSamsungChbx"
                               name="productProducer" value="Samsung" <c:if test="${currentProductPageRules.producer=='Samsung'}">checked</c:if>>
                        <label class="form-check-label" for="ProducerSamsungChbx">samsung</label>
                    </div>
                    <div class="form-group form-check">
                        <input type="radio" class="form-check-input user-preference" id="ProducerMeizuChbx"
                               name="productProducer" value="Meizu" <c:if test="${currentProductPageRules.producer=='Meizu'}">checked</c:if>>
                        <label class="form-check-label" for="ProducerMeizuChbx">meizu</label>
                    </div>
                </div>
                <div class="categoryFilter">
                    ${bundle.getString("products.category")}:
                    <div class="form-group form-check">
                        <input type="radio" class="form-check-input user-preference" id="categoryPhone"
                               name="productCategory" value="phone" <c:if test="${currentProductPageRules.category=='phone'}">checked</c:if>/>
                        <label class="form-check-label" for="categoryPhone"> ${bundle.getString("products.phones")}</label>
                    </div>
                    <div class="form-group form-check">
                        <input type="radio" class="form-check-input user-preference" id="categoryTV" name="productCategory" value="tv" <c:if test="${currentProductPageRules.category=='tv'}">checked</c:if>/>
                        <label class="form-check-label" for="categoryTV"> ${bundle.getString("products.tv")}</label>
                    </div>
                </div>
                <div class="priceFilter">
                    Min:
                    <input class="user-preference" type="text" name="priceMin" id="priceMinF" placeholder="min:" value="${currentProductPageRules.minPrice}"/>
                    Max:
                    <input class="user-preference" type="text" name="priceMax" id="priceMaxF" placeholder="max:" value="${currentProductPageRules.maxPrice}"/>
                </div>
                <input type="submit" class="btn btn-info submit-preferences" id="doFilter" value = "${bundle.getString('products.find')}"/>
                <input type="button" class="resetFilter" value=" ${bundle.getString('products.reset')}">
            </div>
        </aside>
        
        <!--Here will be product main part -->
        <main class="col-xs-12 col-sm-8 col-md-9 col-lg-10">
            <div class="preferenceBlock">
                <div class="sortBlock">
                    ${bundle.getString("products.sortOption")}
                    <select name="sortProducts" id="sort" class="user-preference">
                        <option value="sortByName" class="submit-preferences" <c:if test="${currentProductPageRules.sort=='sortByName'}">selected="selected"</c:if>> ${bundle.getString("products.sortByName")}</option>
                        <option value="sortByPrice" class="submit-preferences"<c:if test="${currentProductPageRules.sort=='sortByPrice'}">selected="selected"</c:if>>${bundle.getString("products.sortByPrice")}</option>
                    </select>
                </div>
                <div class="productCountBlock">
                   ${bundle.getString("products.displayedProducts")}
                    <select name="productCount" id="productCount" class="user-preference">
                        <option value="12" class="submit-preferences" <c:if test="${currentProductPageRules.productDisplayed==12}">selected="selected"</c:if>>12</option>
                        <option value="36" class="submit-preferences" <c:if test="${currentProductPageRules.productDisplayed==36}">selected="selected"</c:if>>36</option>
                    </select>
                </div>
            </div>
            <!--Product list-->
            <div class="product-list">
                <div class="row">
                    <c:forEach var="product" items="${currentProductList}">
                    <div class="col-sm-12 col-md-4 col-lg-3 mbt-5">
                        <!--Product card block-->
                        <div class="card product">
                            <div class="card-body">
                                <!--Image block-->
                                <div>
                                    <img class="product-pic" src="https://via.placeholder.com/250"/>
                                </div>
                                <!--Product name-->
                                <div class="center-text product-name">
                                    <h4>${product.productName}</h4>
                                </div>
                                <!--Code Buy-->
                                <div class="container">
                                    <div class="small-text">
                                        Code: ${product.productId}
                                    </div>

                                    <input type="button" class="btn btn-info addProductToCart" value="${bundle.getString('products.buy')}" id="buy_${product.productId}"/>

                                </div>
                                <ul class="list-group ">
                                    <li class="list-group-item">${bundle.getString('products.category')}: ${product.categoryName}</li>
                                    <li class="list-group-item">${bundle.getString('products.producer')}: ${product.producerName}</li>
                                </ul>
                                <div class="price">
                                    $ ${product.price}
                                </div>
                            </div>
                        </div>
                    </div>
                    </c:forEach>
                </div>
                 <c:if test="${empty currentProductList}">
                                <div class="alert alert-info" role="alert">
                                    <h3>No one items found by your request!</h3>
                                </div>
                                </c:if>
            </div>
             <pagination:pagination/>
        </main>
    </div>
</div>
<%--THIS IS IMPORTANT PART OF PAGE FOR INNER CALCULATIONS--%>
<a class="preference-link"></a>
<form id="preferenceForm" method="GET"></form>
<script src = "${pageContext.request.contextPath}/static/js/notification.js"></script>
<script src = "${pageContext.request.contextPath}/static/js/products.js"></script>