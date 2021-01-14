const ALERT_ELEMENT_CLASS = '.alert';
const ALERT_ELEMENT = document.querySelectorAll(ALERT_ELEMENT_CLASS);

function hangClickListenerOnBody(someAction){
	let body = document.querySelector('body');
	body.addEventListener('click', someAction);
}

hangClickListenerOnBody((event) =>{
	ALERT_ELEMENT.forEach(element=>element.remove());
});