<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="n" tagdir="/WEB-INF/tags"%>
<n:notify notificationContainerName='loginErrorContainer' noteType='warning'/>
<n:notify notificationContainerName='notificationBlock' noteType='warning'/>

<div class="container-fluid">
    <div class="row">
        <div class="col-xs-0 col-sm-1  col-md-2 col-lg-4"></div>
	        <div class="col-xs-12 col-sm-10 col-md-8 col-lg-4" style="padding: 20px 40px 0px 40px">
                   <form id="login" action="${pageContext.request.contextPath}/login" method="POST">
                        <div class="card">
                              <div class="card-header">
${bundle.getString('login.header')}
                              </div>
                              <div class="card-body">
                                  <div class="center-text">
                                  </div>
                                  <div class="form-group">
                                      <span class="small-text">${bundle.getString('registration.login')}:</span>
                                      <input type="text" class="form-control" id="user-login" name="user-login" style="height: 28px"/>
                                  </div>
                                  <div class="form-group">
                                      <span class="small-text">${bundle.getString('registration.password')}:</span>
                                      <input type="password" class="form-control" id="user-password" name="user-password" style="height: 28px"/>
                                  </div>
                                  <div>
                                      <span class="small-text">${bundle.getString('login.remember')}</span>
                                      <input class="form-check-input" type="checkbox" id="user-remember" name="user-remember" style="margin-left: 10px; margin-top: 7px;"/>
                                  </div>
                                  <div class=" row margin-top-bottom-5">
                                      <div class="col-sm-12" style="text-align: center">
                                          <button type="submit" class="btn btn-success">${bundle.getString('login.enter')}</button>
                                      </div>
                                  </div>
                                  <div style="text-align: center">
                                      <a href="${pageContext.request.contextPath}/registration" style="text-align: center">${bundle.getString('registration.register')}</a>
                                  </div>
                              </div>
                        </div>
                        <input type="text" name="login-caller" value="login-page-caller" style="display: none"/>
                   </form>
	        </div>
        <div class="col-xs-0 col-sm-1  col-md-2 col-lg-4"></div>
    </div>
</div>
<script src="${pageContext.request.contextPath}/static/js/notification.js"/>