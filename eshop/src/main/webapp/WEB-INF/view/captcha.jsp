<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
 <%@ taglib prefix = "ex" uri = "/WEB-INF/custom.tld"%>
    <c:set var="sessionId" value="${pageContext.session.getId()}"/>
<main>
    <div class="container-fluid captcha-main">
        <div class="row">
            <div class="col-xs-2 col-sm-3 col-lg-4"></div>
                <div class="col-xs-8 col-sm-6 col-lg-4">
                    <div class="card captcha-card">
                        <form action="${pageContext.request.contextPath}/captcha" method="POST">
                            <div class="card-body center padding-0" style="padding: 10px 0px 10px 0px">
                                <div class="info-header">${bundle.getString('captcha.header')}</div>
                                <div class="margin-t-b-5">
                                     <ex:captcha captchaId="${sessionId}"/>
                                </div>
                                 <div class="form-group margin-0 padding-left-right-30 margin-t-b-5">
                                    <input type="text" class="form-control" id="formGroupExampleInput" placeholder="${bundle.getString('captcha.input')}" name="captchaCode">
                                  </div>
                                <div class="center margin-t-b-5">
                                    <button type="submit" class="btn btn-info">${bundle.getString('captcha.finishRegistration')}</button>
                                </div>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        <div class="col-xs-2 col-sm-3 col-lg-4"></div>
        </div>
    </div>

</main>