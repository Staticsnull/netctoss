<%@page pageEncoding="utf-8" %>
<!--EL默认的取值范围
    pageContext,request,session,application
    若想从cookie中取值，语法如下：cookie.NAME.value 
    NAME是有所变化的  -->
<img src="images/logo.png" alt="logo" class="left"/>
<span style="font-weight: bold;font-size:16px;color:#666; ">${user }</span>
<a href="logout.do">[退出]</a> 