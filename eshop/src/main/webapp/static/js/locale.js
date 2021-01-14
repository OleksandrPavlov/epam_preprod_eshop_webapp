var LANGUAGE_ELEMENTS = document.querySelectorAll('.dropdown-menu .language');

addingLangParameter();

function addingLangParameter(){
    LANGUAGE_ELEMENTS.forEach((element) => {
    let param = extractParameters(element.href);
    let delimiter = '?';
    let hyperRef = removeEldestLangParameterFromUrl(window.location.href);
    console.log(hyperRef);
   if(hyperRef.indexOf('?') != -1){
      delimiter = '&';
    }
    removeEldestLangParameterFromUrl(hyperRef);
    element.href = hyperRef + delimiter + param;
    });
}

function removeEldestLangParameterFromUrl(url){
    let ch = url.search('lang=.+');
    console.log("INDEX OF LANG PARAMETER: " + ch);
    if(ch != -1){
        url = url.substring(0, ch - 1);
        return url;
    }
    return url;
}

function extractParameters(element){
let lastSlashIndex = element.lastIndexOf('/');
let changedString = element.substring(lastSlashIndex + 1, element.length);
return changedString;
}
