<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <meta http-equiv="X-UA-Compatible" content="ie=edge">
  <title>Vitogram</title>
  <link href="https://maxcdn.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css"
    rel="stylesheet">
    <link rel="shortcut icon" href="/images/favicon.ico">
    <link rel="stylesheet" href="/css/styles.css">
</head>
<body>
    <%@ include file = "../include/nav.jsp" %>
  
  <main id="feed">
  
  	<c:forEach var="image" items="${images}">
	    <div class="photo u-default-box">
	      <header class="photo__header">
			<img src="/upload/${image.user.profileImage}" onerror="this.onerror=null; this.src='/images/avatar.jpg'"/>
			<div class="photo_user_info">
		        <span class="photo__username">${image.user.userName}</span>
		        <span class="photo__location">${image.location}</span>
		    </div>
	      </header>
	      <img src="/upload/${image.postImage}" />
	      <div class="photo__info">
	          <div class="photo__actions">
	            <span class="photo__action">
	            	<!-- 좋아요 이미지 -->
	            <c:choose>
	            	<c:when test="${image.heart eq true}">
	            		<i onClick="onFeedLoad(${image.id})" id="${image.id}" class="fa fa-heart heart heart-clicked"></i>
	            	</c:when>
	            	<c:otherwise>
	            		<i onClick="onFeedLoad(${image.id})" id="${image.id}" class="fa fa-heart-o heart"></i>
	            	</c:otherwise>
	            </c:choose>
	              <i class="fa fa-heart-o heart"></i>
	            </span>
	            <span class="photo__action">
	              <i class="fa fa-comment-o"></i>
	            </span>
	          </div>
	          <span class="photo__likes">5,103 likes</span>
	          <p>${image.caption}</p>
	          <p>
	          	<c:forEach var="tag" items="${image.tags}">
	          		#${tag.name}
	          	</c:forEach>
	          </p>
	          <ul class="photo__comments">
	            <li class="photo__comment">
	              <span class="photo__comment-author">serranoarevalo</span> i love this!
	            </li>
	            <li class="photo__comment">
	              <span class="photo__comment-author">serranoarevalo</span> i don't love this!
	            </li>
	          </ul>
	          <span class="photo__date">${image.createDate}</span>
	          <div class="photo__add-comment-container">
	            <textarea placeholder="Add a comment..."></textarea>
	            <i class="fa fa-ellipsis-h"></i>
	          </div>
	      </div>
	    </div>
    </c:forEach>
  </main>
   <%@ include file = "../include/footer.jsp" %>
	<script src="/js/like.js"></script>

</body>
</html>
