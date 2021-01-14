var ORDER_FORM = document.querySelector('#orderForm');
var PAYMENT_SELECTOR = ORDER_FORM.querySelector('.paymentSelector');
var PAYMENT_BLOCKS = ORDER_FORM.querySelector('#paymentBlocks')
var ALL_PAYMENT_BLOCKS = PAYMENT_BLOCKS.querySelectorAll('.paymentBlock');
var PAYMENT_TYPE = PAYMENT_BLOCKS.querySelector('#paymentType');
var SUBMIT_ORDER_BUTTON = document.querySelector('#submitOrderButton')
var APPLE_PAY_BLOCK = document.querySelector('#applePayBlock');
var APPLE_PAYMENT_IMAGE = APPLE_PAY_BLOCK.querySelector('#applePayImage');

hangClickEventHandlerOnPaymentSelector();
hangClickEventOnApplePayImage();

function hangClickEventHandlerOnPaymentSelector(){
	PAYMENT_SELECTOR.addEventListener('click', paymentClickHandler)
}

function hangClickEventOnApplePayImage(){
	APPLE_PAYMENT_IMAGE.addEventListener('click',appleImageClickHandler);
}

function paymentClickHandler(){
	toHideElements(ALL_PAYMENT_BLOCKS);
	if(PAYMENT_SELECTOR.value == 'Card'){
		PAYMENT_BLOCKS.querySelector('#cardPayBlock').hidden = false;
		PAYMENT_TYPE.value = "card";
	}
	if(PAYMENT_SELECTOR.value == 'ApplePay'){
		PAYMENT_BLOCKS.querySelector('#applePayBlock').hidden = false;
		PAYMENT_TYPE.value = "applePay";
	}
	SUBMIT_ORDER_BUTTON.hidden = false;
}

function appleImageClickHandler(){
	element = APPLE_PAY_BLOCK.querySelector('.sign');
	element.innerHTML = '(finger data accepted)';
	inputElement = APPLE_PAY_BLOCK.querySelector('.fingerDataClass');
	inputElement.value = 'fingerData';
}
function toHideElements(elements){
	elements.forEach((element)=>{
		element.hidden = true;
	});
}

