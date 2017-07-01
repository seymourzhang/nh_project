<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<div class="page-sidebar nav-collapse collapse " >
		<!-- BEGIN SIDEBAR MENU -->        
		<ul class="page-sidebar-menu">
			<li>
				<!-- BEGIN SIDEBAR TOGGLER BUTTON -->

				<div class="sidebar-toggler hidden-phone" id="sidebar_toggler"></div>

				<!-- BEGIN SIDEBAR TOGGLER BUTTON -->

			</li>
			<li>
				<!-- BEGIN RESPONSIVE QUICK SEARCH FORM -->
				
				<!-- END RESPONSIVE QUICK SEARCH FORM -->
			</li>
			
			
			
			<%-- <li class="start active " id="fhindex">
				<a href="<%=path%>/user/index">
				<i class="icon-home"></i> 
				<span class="title">统计</span>
				<span class="selected"></span>
				</a>
			</li> --%>
			<c:forEach items="${menuList}" var="menu">
				<li id="lm${menu.MENU_ID }">
					<a href="javascript:;">
						<i class="${menu.MENU_ICON == null ? '' : menu.MENU_ICON}"></i>
						<span class="title">${menu.MENU_NAME }</span>
						<span class="arrow " id="se${menu.MENU_ID }"></span>
						<span id="op${menu.MENU_ID }"></span>
					</a>
					<ul class="sub-menu">
						<c:forEach items="${menu.subMenu}" var="sub">
								<li id="z${sub.MENU_ID }" onclick="siMenu('z${sub.MENU_ID }','lm${menu.MENU_ID }','se${menu.MENU_ID }','op${menu.MENU_ID }','${sub.MENU_NAME }','${sub.MENU_URL }','${sub.write_read }')">
									<a href="javascript:void(0);">
									<i class="${sub.MENU_ICON == null ? '' : sub.MENU_ICON}"></i>
										${sub.MENU_NAME }
									</a>
								</li>
						</c:forEach>
					</ul>
				</li>
			</c:forEach>
		</ul>
		<!-- END SIDEBAR MENU -->
	</div>