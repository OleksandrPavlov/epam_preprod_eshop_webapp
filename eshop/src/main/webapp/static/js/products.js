const ADD_PRODUCT_TO_CART_BUTTON_CLASS='.addProductToCart';
const ADD_NEW_PRODUCT_TO_CART_SERVLET = ctx + '/ajax/cart/update-cart';
const GET_PRODUCT_COUNT_IN_CART = ctx + '/ajax/cart/product-count';
const PRODUCT_CART_ITEM_COUNTER = '#productCartItemCount';

var CURRENT_CLICKED_BUY_BUTTON;
executingFunctionsInCatchBlock();

/**
* Makes action on window full loading.
*/
 function onWindowLoaded() {
	console.log("hang on window")
		window.addEventListener("load" , (event) =>{
			retrievingCountProuctItemInCart(updateCounterOnCartButton);
		});
	}

/**
*This function sends ajax request to the server and parses response. The response should contain number of products in the cart.  
*
*/
var retrievingCountProuctItemInCart = function(onRetreivingAction){
	let requestObject;
	if(typeof XMLHttpRequest != 'undefined'){
		requestObject = new XMLHttpRequest();
	}
		requestObject.open('GET', window.origin + GET_PRODUCT_COUNT_IN_CART , true);
		requestObject.onload = onRetreivingAction;
		requestObject.send();
}

/**
*This function retriving current number of products in cart and updates digit value in #productCartItemCount element
*
*/
var updateCartCounterOnCookieChange = function() {
    var lastCookie = document.cookie; 
    return function() {
        var currentCookie = document.cookie;
        if (currentCookie != lastCookie) {
			retrievingCountProuctItemInCart(updateCounterOnCartButton);
            lastCookie = currentCookie; 
        }
    };
}();

/**
*Checks the cookie on updates after adjusted time interval and calls updateCartCounterOnCookieChange function if cookie has been updated
*
*/
window.setInterval(updateCartCounterOnCookieChange, 100);

 function hangOnBuyButtonClickEvent(elements){
	elements.forEach(element => {
		element.addEventListener('click',   (event) => {
			let clickedProductItem = event.target;
			CURRENT_CLICKED_BUY_BUTTON = clickedProductItem;
			parameterItems = prepareUpdatePackage(extractIdPart (clickedProductItem.id), '1', 'add');
			sendRequest(ADD_NEW_PRODUCT_TO_CART_SERVLET, 'POST', parameterItems, buyButtonHandler);
			
		});
	});

}

 async function sendRequest(address, method, parameters, onLoadHandler){
	let requestObject;
	if(typeof XMLHttpRequest != 'undefined'){
		requestObject = new XMLHttpRequest();
	}
	requestObject.open(method, address , true);
	if(method == 'POST'){
		requestObject.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
	}
	let parameterUrl = prepareParameterUrl(parameters);
	requestObject.innerFunction = onLoadHandler;
    requestObject.onload = onAction;
	await requestObject.send(parameterUrl);
}

function extractIdPart(str){
	let lastIndexOfBottomLine = str.lastIndexOf('_');
	return str.substring(lastIndexOfBottomLine + 1, str.lenght);
}

function onAction(){
	let jsonUpdatePack = JSON.parse(this.responseText);
	if(jsonUpdatePack.status != '1'){
	    alert(jsonUpdatePack.errorMessage);
	}else{
		this.innerFunction.call(this, jsonUpdatePack);
	}
}

function buyButtonHandler(){
CURRENT_CLICKED_BUY_BUTTON.disabled = true;
}

function updateCounterOnCartButton(){
	let jsonUpdatePack = JSON.parse(this.responseText);
		element = document.querySelector(PRODUCT_CART_ITEM_COUNTER);
		element.innerHTML = jsonUpdatePack.productCartItemCount;
	}

function prepareParameterUrl( parameterItems){
	let resultParameterPart='';
	parameterItems.forEach((element) => {
		resultParameterPart += element.name + '=' + element.value + '&';
	});
	return  resultParameterPart;
}

function recalculateTotalItemById(jsonUpdatePack){
	let totalItemPriceElementPrefix = "totalItemCost_";
	let counterElementPrefix = 'count_';
	let priceElementPrefix = 'price_';
	let totalElement = document.querySelector('#' + totalItemPriceElementPrefix + jsonUpdatePack.productId);
	let counterElement = document.querySelector('#' + counterElementPrefix + jsonUpdatePack.productId);
	let priceElement = document.querySelector('#' + priceElementPrefix + jsonUpdatePack.productId);
	totalElement.innerHTML = (counterElement.value * priceElement.innerHTML).toFixed(2);
}

function prepareUpdatePackage(productId, productCount, operation){
	let parameterItems = new Array();
            let productIdParameter = {name: 'productId', value: productId };
            let parameterOperation = {name: 'operation', value: operation};
            productCountParameter = {name: 'productCount', value: productCount };
            parameterItems.push(productIdParameter);
            parameterItems.push(parameterOperation);
            parameterItems.push(productCountParameter);
            return parameterItems;
}

function executingFunctionsInCatchBlock(){

	try{

		hangOnBuyButtonClickEvent(document.querySelectorAll(ADD_PRODUCT_TO_CART_BUTTON_CLASS));
			}catch(err){
				console.log(err.message);
	}
	try{
		onWindowLoaded.call();
			}catch(err){
				console.log(err.message);
	}
}



