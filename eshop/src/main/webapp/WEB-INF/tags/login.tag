<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:if test="${empty currentUser}">
   <div style="padding: 5px 50px 5px 70px; text-align:center;" >

       <form action="${pageContext.request.contextPath}/login" method="POST">
           <div class="login-frame-container">
               <div>
                   <input type="text" class="form-control login-input" placeholder="${bundle.getString('frame.login')}" name="user-login" style="font-size:10px; margin-top: 12px;">
               </div>
               <div>
                   <input type="password" class="form-control login-input" placeholder="${bundle.getString('frame.password')}" name="user-password" style="font-size:10px;  margin-top: 12px;">
               </div>
                <div>
                   <button type="submit" class="btn btn-primary login-input" style="font-size:10px">${bundle.getString('frame.enter')}</button>
                    <a href="${pageContext.request.contextPath}/registration" style="font-size:12px">${bundle.getString('frame.registration')}</a>
               </div>
            </div>
           <input type = "text" name ="login-caller" value="login-frame-caller" style = "display: none;"/>
       </form>

   </div>
</c:if>
<c:if test="${not empty currentUser}">
        <form action="${pageContext.request.contextPath}/home/logout" method="POST">
         <div class="logout">
            <div class="currentUserName">
              ${currentUser.name}
            </div>
            <div class="logoutButton">
               <button type="submit" class="btn btn-outline-light">Logout</button>
            </div>
            <img class="avatar" src="${pageContext.request.contextPath}/home/avatar"/>
         </div>
        </form>
   
</c:if>