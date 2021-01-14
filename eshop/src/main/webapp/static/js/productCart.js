var UPDATE_ORDER_ITEM_SERVLET = ctx + '/ajax/cart/update-cart';
const PRODUCT_COUNTER_CLASS='.productCounter';
const PRODUCT_REMOVE_CLASS = '.removeProduct';
const REMOVE_ALL_BUTTON_ID = '#removeAll';
const PRODUCT_CART_BODY_CLASS='#cart .body';
const TOTAL_COST_ELEMENT = '#totalCost';
const CART_BLOCK_FOOTER = '.cartBlock .footer';
const DO_ORDER_BUTTON = '#doOrderButton';

//All element with id that starts with totalItemCost
const TOATAL_ITEMS_COST_CLASS = "[id^='totalItemCost']";

hangOnCounterChangeEvent(document.querySelectorAll(PRODUCT_COUNTER_CLASS));
hangOnRemoveOrderButtonClickEvent(document.querySelectorAll(PRODUCT_REMOVE_CLASS));
hangOnRemoveAllOrderButtonClickEvent(document.querySelector(REMOVE_ALL_BUTTON_ID));
hangOnOrderButton();

function hangOnCounterChangeEvent(elements){
	console.log('method # hangOnCounterChangeEvent');
	elements.forEach(element => {
		element.addEventListener('change', (event) => {
			let clickedOrderItem = event.target;
			parameterItems = prepareUpdatePackage(extractIdPart (clickedOrderItem.id), clickedOrderItem.value, 'update');
			sendRequest(UPDATE_ORDER_ITEM_SERVLET, 'POST', parameterItems, recalculateTotalItemById);
		});
	});
}

function windowLoadAction(){
	window.addEventListener('load', (event) => {
			recalculateTotalCostOfAll();
		});
}
function hangOnOrderButton(){
	console.log('method # hangOnOrderButton');
	let orderButtonElement = document.querySelector(TOTAL_COST_ELEMENT);
	window
		recalculateTotalCostOfAll();
	
}

function prepareParameterUrl(parameterItems){
	console.log('method # hangOnCounterChangeEvent');
	let resultParameterPart='';
	parameterItems.forEach((element) => {
		resultParameterPart += element.name + '=' + element.value + '&';
	});
	return  resultParameterPart;
}

function prepareUpdatePackage(productId, productCount, operation){
	console.log('method # hangOnCounterChangeEvent');
	let parameterItems = new Array();
            let productIdParameter = {name: 'productId', value: productId };
            let parameterOperation = {name: 'operation', value: operation};
            productCountParameter = {name: 'productCount', value: productCount };
            parameterItems.push(productIdParameter);
            parameterItems.push(parameterOperation);
            parameterItems.push(productCountParameter);
            return parameterItems;
}

function hangOnRemoveOrderButtonClickEvent(elements){
	console.log('method # hangOnRemoveOrderButtonClickEvent');
	elements.forEach(element => {
		element.addEventListener('click', (event) => {
            let parameterItems = prepareUpdatePackage(extractIdPart(event.target.id),'1','remove');
            sendRequest(UPDATE_ORDER_ITEM_SERVLET, 'POST', parameterItems, removeOrderItem);
		});
	});
}

function hangOnRemoveAllOrderButtonClickEvent(element){
	console.log('method # hangOnRemoveAllOrderButtonClickEvent');
		element.addEventListener('click', (event) => {
            let parameterItems =  prepareUpdatePackage('1','1','removeAll');
            sendRequest(UPDATE_ORDER_ITEM_SERVLET, 'POST', parameterItems, removeAllOrders);
		});
}

function removeOrderItem(jsonUpdatePack){
	console.log('method # removeOrderItem');
	let orderItemPrefix = 'orderItem_';
	let ordderItemQuantity = document.querySelectorAll("[id^= 'orderItem_']").length;
	let orderItemElement = document.querySelector('#' + orderItemPrefix + jsonUpdatePack.productId);
	orderItemElement.remove();
	
	if(ordderItemQuantity == 1){
		drawNewCartBody();
		hideOrderButton();
	}

}

function removeAllOrders(jsonUpdatePack){
	console.log('method # removeAllOrders');
	let bodyElement = document.querySelector(PRODUCT_CART_BODY_CLASS);
	let orderItems = bodyElement.querySelector(".orderItems");
	orderItems.innerHTML =  createEmptyNotificationElement('Empty cart yet:)').outerHTML;
	drawNewCartBody();
	hideOrderButton();
}

function drawNewCartBody(){
	console.log('method # drawNewCartBody');
	let cartBody = document.querySelector(PRODUCT_CART_BODY_CLASS);
	cartBody.innerHTML = "";
	let newCartBodyContent = document.createElement("p");
	newCartBodyContent.innerHTML = "Your cart is empty...";
	cartBody.appendChild(newCartBodyContent);
	let footer = document.querySelector(CART_BLOCK_FOOTER);
	footer.innerHTML="";
}

function hideOrderButton(){
console.log('method # hideOrderButton');
let orderButton = document.querySelector(DO_ORDER_BUTTON);
orderButton.remove();
}


function recalculateTotalItemById(jsonUpdatePack){
	console.log('method # recalculateTotalItemById');
	let totalItemPriceElementPrefix = "totalItemCost_";
	let counterElementPrefix = 'count_';
	let priceElementPrefix = 'price_';
	let totalElement = document.querySelector('#' + totalItemPriceElementPrefix + jsonUpdatePack.productId);
	let counterElement = document.querySelector('#' + counterElementPrefix + jsonUpdatePack.productId);
	let priceElement = document.querySelector('#' + priceElementPrefix + jsonUpdatePack.productId);
	totalElement.innerHTML = (parseFloat(counterElement.value) * parseFloat(priceElement.innerHTML)).toFixed(2);
	recalculateTotalCostOfAll();
}
function recalculateTotalCostOfAll(){
	console.log('method # recalculateTotaCostOfAll');
	let totalCostElements = document.querySelectorAll(TOATAL_ITEMS_COST_CLASS);
	console.log(totalCostElements);
	let totalCostValue = 0;
	totalCostElements.forEach((element) => {
		totalCostValue = (parseFloat(totalCostValue) + parseFloat(element.innerHTML)).toFixed(2);
	});
	let totalCostButton = document.querySelector(TOTAL_COST_ELEMENT);
	totalCostButton.innerHTML = totalCostValue + ' $';
}

function createEmptyNotificationElement(message){
	console.log('method # createEmptyNotificationElement');
	let messageContainer = document.createElement('div');
	messageContainer.className = 'emptyCartMessageContainer';
	messageContainer.innerHTML = message;
	return messageContainer;
}

function prepareUpdatePackage(productId, productCount, operation){
	console.log('method # prepareUpdatePackage');
	let parameterItems = new Array();
            let productIdParameter = {name: 'productId', value: productId };
            let parameterOperation = {name: 'operation', value: operation};
            productCountParameter = {name: 'productCount', value: productCount };
            parameterItems.push(productIdParameter);
            parameterItems.push(parameterOperation);
            parameterItems.push(productCountParameter);
            return parameterItems;
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

function onAction(){
	let jsonUpdatePack = JSON.parse(this.responseText);
	if(jsonUpdatePack.status != '1'){
	    alert(jsonUpdatePack.errorMessage);
	}else{
		this.innerFunction.call(this, jsonUpdatePack);
	}
}

function extractIdPart(str){
	let lastIndexOfBottomLine = str.lastIndexOf('_');
	return str.substring(lastIndexOfBottomLine + 1, str.lenght);
}