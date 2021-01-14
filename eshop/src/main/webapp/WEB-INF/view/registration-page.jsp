<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix = "n" tagdir = "/WEB-INF/tags"%>
<n:notify notificationContainerName = 'registrationProcessErrorContainer' noteType='warning'/>
<n:notify notificationContainerName = 'captchaCheckNotification' noteType='errorAlert'/>
<div class="container-fluid">
    <div class="row">
        <div class="col-xs-0 col-sm-1  col-md-2 col-lg-4"></div>
	        <div class="col-xs-12 col-sm-10 col-md-8 col-lg-4" style="padding: 20px 40px 0px 40px">
	           <form id="registration" action="${pageContext.request.contextPath}/registration" method="POST" enctype="multipart/form-data">
					<div class="card " style="background-color: #5c5c5c">
						  <div class="card-header" >
						   ${bundle.getString('registration.registrationForm')}
						  </div>
						  <div class="card-body" >
						  	<div class="bottom-line">
							  	<n:fieldAlert notificationContainerName="registrationFormErrorContainer" msgKey="registration.notify.badLogin"/>
						   		<div class="form-group">
	                      			<input type="text" class="form-control" placeholder="${bundle.getString('registration.login')}" id="user-login" name="user-login"
						       				value="${registrationForm.login}">
	                    		</div>
						  		<n:fieldAlert notificationContainerName="registrationFormErrorContainer" msgKey="registration.notify.badName"/>
					   			<div class="form-group">
	                  				<input type="text" class="form-control" placeholder="${bundle.getString('registration.name')}" id="user-name" name="user-name"
					       				value="${registrationForm.name}">
	               		 		</div>	 								
								    <n:fieldAlert notificationContainerName="registrationFormErrorContainer" msgKey="registration.notify.badSurname"/>
	                    		<div class="form-group">
	                        		<input type="text" class="form-control"  placeholder="${bundle.getString('registration.surname')}" id="user-surname" name="user-surname"
					        			value="${registrationForm.surname}">
	                    		</div>								
								 <n:fieldAlert notificationContainerName="registrationFormErrorContainer" msgKey="registration.notify.badEmail"/>
	                        	<div class="form-group">
	                            	<input type="email" class="form-control" placeholder="${bundle.getString('registration.email')}" id="user-email" name="user-email" value="${registrationForm.email}">
	                            </div>	
                            </div>	
                            <div class="bottom-line">							
								 <n:fieldAlert notificationContainerName="registrationFormErrorContainer" msgKey="registration.notify.badPassword"/>
	                            <div class="form-group">
	                                <input type="password" class="form-control" placeholder="${bundle.getString('registration.password')}" id="user-password" name="user-password"  value="${registrationForm.password}">
	                            </div>
								<n:fieldAlert notificationContainerName="registrationFormErrorContainer" msgKey="registration.notify.badConfirmPassword"/>
	                			<div class="form-group">
	                    			<input type="password" class="form-control" placeholder="${bundle.getString('registration.password.confirm')}" id="user-password-confirm" name="user-password-confirm">
	                			</div>
                			</div>
                			<div class="bottom-line">	
	                    		<div class="form-group row">
									<div class=" col-sm-7 small-text">${bundle.getString('registration.notify.new.events')}</div>
									<div class=" col-sm-5 ">
							    		<div class="form-check">
							        		<input class="form-check-input" type="checkbox" id="user-notify" name="user-notify"
							        			<c:if test="${registrationForm.toNotify==true}">
							        			<c:out value="checked=\"checked\""/>
							        			</c:if>>
				      					</div>
				   					</div>
				  				</div>
				  			</div>
				  			<div class="bottom-line">	
				  				<n:fieldAlert notificationContainerName="registrationFormErrorContainer" msgKey="registration.notify.badAvatar"/>
				  				<div class="form-group">
				  					<input type="file" name="avatar-file" id="avatar_id"/>
				  				</div>
				  			</div>
				  		</div>
				  		<div class="row margin-top-bottom-5" ">
				   		 	<div class="col-sm-12" style="text-align: center">
				      		<button type="submit" class="btn btn-primary">${bundle.getString('registration.register')}</button>
				    		</div>
				  		</div>
	 			 	</div>
				</div>
			</form>
	    </div>
    <div class="col-xs-0 col-sm-1  col-md-2 col-lg-4"></div>
    </div>
</div>
<script src="${pageContext.request.contextPath}/static/js/notification.js"/>