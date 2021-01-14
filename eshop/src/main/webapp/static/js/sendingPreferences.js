const PRODUCTS_SERVLET_URL='http://localhost:8080/' + ctx + '/products';
const SUBMIT_PREFERENCES_CLASS = '.submit-preferences';
const PREFERENCE_ELEMENTS_CLASS = '.user-preference';
const CLICK_EVENT_NAME = 'click';
const CHECKBOX_ELEMENT_NAME = 'checkbox';
const RADIO_ELEMENT_NAME = 'radio';
const RESET_FILTER_LINK='.resetFilter';
const RESET_FILTER_ELEMENT=document.querySelector(RESET_FILTER_LINK);
const SUBMIT_PREFERENCES_ELEMENTS = collectSubmitPreferences();
const PREFERENCE_ELEMENTS = collectPreferenceElements();
const PREFERENCE_LINK = document.querySelector('.preference-link');

var PAGE_ELEMENTS;
var CURRENT_PAGE;
var PRESSED_PAGE;

hangEventOnPgeButtonsAndDetermPressedPageNumber();
function hangEventOnPgeButtonsAndDetermPressedPageNumber(){
	PAGE_ELEMENTS=document.querySelectorAll('.productPage');
	PAGE_ELEMENTS.forEach(element=>{
		element.addEventListener('click',function (){
		PRESSED_PAGE=element;
	});
});
}

defineCurrentPage();
function defineCurrentPage(){
	PAGE_ELEMENTS.forEach(element=>{
		if(element.disabled){
			CURRENT_PAGE=element;
		}
	});
}
hangClickEvent(SUBMIT_PREFERENCES_ELEMENTS);

function collectSubmitPreferences(){
	return document.querySelectorAll(SUBMIT_PREFERENCES_CLASS);
}
function collectPreferenceElements(){
	return document.querySelectorAll(PREFERENCE_ELEMENTS_CLASS);
}

//This method hangs handler on click event of every element of submitElements list.
function hangClickEvent(submitElements){
	submitElements.forEach(element => {
		element.addEventListener(CLICK_EVENT_NAME,proceedSubmitPreferencesEvent);
	});
}
//This function will create url from indicated user-preferences
function proceedSubmitPreferencesEvent(event){
let parameterUrlPart = createParameterUrlPart(PREFERENCE_ELEMENTS);
sendPreferences(PRODUCTS_SERVLET_URL+parameterUrlPart);
}

function createParameterUrlPart(inputElements){
	let parameterUrl = '?';
	inputElements.forEach( element => {
	if(element.name!='productPageNumber'){
            if(element.type == RADIO_ELEMENT_NAME){
                if(element.checked == true){
                    parameterUrl = parameterUrl + element.name + '=' + element.value + '&';
                }
            }else{
            parameterUrl = parameterUrl + element.name + '=' + element.value + '&';
        }
	}
	});
    if(typeof CURRENT_PAGE != 'undefined' && typeof PRESSED_PAGE == 'undefined'){
		parameterUrl = parameterUrl + CURRENT_PAGE.name + '=' + CURRENT_PAGE.value;
	}if(typeof PRESSED_PAGE != 'undefined'){
        parameterUrl = parameterUrl + PRESSED_PAGE.name + '=' + PRESSED_PAGE.value;
	}
	return parameterUrl;
}
hangEventHandlerOnResetFilter();
function hangEventHandlerOnResetFilter(){
RESET_FILTER_ELEMENT.addEventListener('click',resetFilter);
}
function resetFilter(){
PREFERENCE_ELEMENTS.forEach(element=>{
if(element.type==RADIO_ELEMENT_NAME){
element.checked="";
}
if(element.name=="productName"){
element.value="";
}
if(element.name=="priceMax"){
element.value="10000";
}
if(element.name=="priceMin"){
element.value="0";
}
});
}

function sendPreferences(url){
	PREFERENCE_LINK.href=url;
	PREFERENCE_LINK.click();
}