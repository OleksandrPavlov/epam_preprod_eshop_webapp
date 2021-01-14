<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<div class="locale-container">
    <div class="languageLogo">
      <img src="${pageContext.request.contextPath}/static/img/locale/${bundle.getLocale().getLanguage()}.png"  class = "languagePic" />
    </div>
      <div class="dropdown" style="margin: 0px 0px 0px 10px;">
            <button class="btn btn-secondary dropdown-toggle" type="button" id="dropdownMenuButton" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false" >
                ${bundle.getString("frame.languageKey")}
            </button>
            <div class="dropdown-menu" aria-labelledby="dropdownMenuButton">
                <a class="dropdown-item language" id="ru" href="lang=ruRU">RU</a>
                <a class="dropdown-item language" id="en" href="lang=enEN">EN</a>
            </div>
      </div>
    </div>