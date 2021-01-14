const EMPTY_FIELD_MESSAGE = 'This empty field!';
const ERROR_CLASS = '.error';
const INPUT_GROUP_CLASS = '.form-group';
const INPUT_TAG = 'input';
const INVALID_NAME_MESSAGE = 'Invalid name format!';
const INVALID_PASSWORD_MESSAGE = 'Invalid password format!';
const INVALID_SURNAME_MESSAGE = 'Invalid surname format!';
const INVALID_EMAIL_MESSAGE = 'Invalid email format!'
const USER_NAME_ID = '#user-name';
const USER_SURNAME_ID = '#user-surname';
const USER_PASSWORD_ID = '#user-password';
const USER_EMAIL_ID = '#user-email';
var registrationForm = document.querySelector('#registration');
registrationForm.addEventListener('submit', validate);
var nameRegEx = new RegExp('^[a-zA-zА-Яа-яЕЇі]{3,}$');
var passwordRegEx = new RegExp("^[a-zA-z]{6,}$");
var emailRegExp = new RegExp("^[0-9a-z-\.]+\@[0-9a-z-]{2,}\.[a-z]{2,}$");
var thereError = false;


function createErrorElement(message){
    var error = document.createElement('div');
    error.className = 'error';
    error.innerHTML = message;
    return error;
}

function deleteAllErrors(){
    registrationForm.querySelectorAll(ERROR_CLASS).forEach(error => error.parentNode.removeChild(error));
}


function spellValidating(inputBlock,RegExp,errorMessage){

    var entity = inputBlock.value;
     if (!RegExp.test(entity)){
         thereError = true;
        inputBlock.parentNode.appendChild(createErrorElement(errorMessage));
     }
}
function spellingIsCorrect(){
    let nameInputElement = registrationForm.querySelector(USER_NAME_ID);
    let surnameInputElement = registrationForm.querySelector(USER_SURNAME_ID);
    let passwordInputElement = registrationForm.querySelector(USER_PASSWORD_ID);
    let emailInputElement = registrationForm.querySelector(USER_EMAIL_ID);

    spellValidating(nameInputElement,nameRegEx,INVALID_NAME_MESSAGE);
    spellValidating(surnameInputElement,nameRegEx,INVALID_SURNAME_MESSAGE);
    spellValidating(passwordInputElement,passwordRegEx,INVALID_PASSWORD_MESSAGE);
    spellValidating(emailInputElement,emailRegExp,INVALID_EMAIL_MESSAGE);
 
}

function validate(){
    deleteAllErrors();
    spellingIsCorrect();
        if (thereError){
            event.preventDefault();
            thereError = false;
        }
}







