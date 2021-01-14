<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<div class="cartBlock">
    <div><a href="${pageContext.request.contextPath}/products" class="modern">${bundle.getString('cart.backToMarket')}</a></div>
    <div id="cart">
            <div class="header">
                <img src = "${pageContext.request.contextPath}/static/img/order/delivery.png">
            </div>
                    <div class="body">
                      <form action="${pageContext.request.contextPath}/makeOrder" method="POST" id="orderForm">
                    <div class="form-group" style="text-align: center">
                      <label for="inputState">${bundle.getString('order.payment.header')}</label>
                          <select id="inputState" class="form-control paymentSelector">
                            <option >Card</option>
                            <option>ApplePay</option>
                          </select>
                    </div>
                    <div id="paymentBlocks">
                      <div id="applePayBlock" class = "paymentBlock" hidden = true>
                        <img src="${pageContext.request.contextPath}/static/img/order/otp.png" id = "applePayImage"/><br/>
                        <span class="sign">(${bundle.getString('order.finger')})</span>
                        <input type = "text" name = "fingerData" class = "fingerDataClass" hidden=true/>
                      </div>

                      <div id="cardPayBlock" class="row paymentBlock" hidden = true>
                      <div class="col-sm-6">
                  <label for="cardNumber">CARD</label>
                  <input type="text" class="form-control" id="cardNumber" name="cardNumber">
              </div>
              <div class="col-sm-2" >
                  <label for="cardCvv">CVV</label>
                  <input type="text" class="form-control" id="cardCvv" name="cardCvv">
              </div>
                      </div>
                      <input type="text" name="paymentType" id="paymentType" hidden = true/>
                </div>
                <button id="submitOrderButton" type="submit" class="btn btn-success" style="display: block; margin-right: auto; margin-left: auto" hidden = true>${bundle.getString('order.confirm')}</button>
                </form>
    </div>     
    <div style= "text-align: center">
      </div>  
</div>



 
