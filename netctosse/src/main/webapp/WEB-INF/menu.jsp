<%@page pageEncoding="utf-8"%>
 <ul id="menu">
 	<!--此jsp是其他jsp中的一部分，是用来被复用的，所以在
 	浏览器中的任何功能中都可以看到，并点击到这些链接
 	因此这里要写绝对路径，而不是相对路径  -->
     <li><a href="/netctosse/toindex.do" class="index_off"></a></li>
     <li><a href="../role/role_list.html" class="role_off"></a></li>
     <li><a href="../admin/admin_list.html" class="admin_off"></a></li>
     <li><a href="/netctosse/findcost.do" class="fee_off"></a></li>
     <li><a href="../account/account_list.html" class="account_off"></a></li>
     <li><a href="../service/service_list.html" class="service_off"></a></li>
     <li><a href="../bill/bill_list.html" class="bill_off"></a></li>
     <li><a href="../report/report_list.html" class="report_off"></a></li>
     <li><a href="../user/user_info.html" class="information_off"></a></li>
     <li><a href="../user/user_modi_pwd.html" class="password_off"></a></li>
 </ul>