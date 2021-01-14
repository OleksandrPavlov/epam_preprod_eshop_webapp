<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<form action="/upload" method="post" enctype="multipart/form-data">
<input type="text" name="name" />
<input type="text" name="description" />
<input type="file" name="file"/>
<input type="submit"/>
</form>