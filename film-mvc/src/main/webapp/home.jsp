<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Home</title>
<link rel="stylesheet" type="text/css" href="./css/styles.css">
<script src="https://kit.fontawesome.com/59a8e1717f.js"></script>
</head>
<body>
	<div class="search-container">
		<form action="./search" class="search-form">
			<input name="q" type="text" value="${searchString}" placeholder="Search for a film..." maxlength="100" autocomplete="off">
			<button type="submit">
				<i class="fa-solid fa-magnifying-glass"></i>
			</button>
		</form>
	</div>
	<div class="table-menu-container">
		<div class="table-menu-wrapper">
			<c:if test="${not empty searchString}">
				<form action="./home" method="POST" class="show-all-films-form">
					<button type="submit" name="show-all-films-button"
						class="show-all-films-button">
						<i class="fa-solid fa-house-chimney"></i>
					</button>
				</form>
			</c:if>
			<form action="./insert" class="new-film-form">
				<button type="submit" class="new-film-button">
					<i class="fa-solid fa-plus"></i>
				</button>
			</form>
		</div>
	</div>
	<c:choose>
		<c:when test="${resultCount > 0}">
			<div class="table-container">
				<div class="table-wrapper">
					<table>
						<thead>
							<tr>
								<th><i class="fa-solid fa-hashtag"></i></th>
								<th>Title</th>
								<th>Year</th>
								<th>Director</th>
								<th>Stars</th>
								<th>Review</th>
								<th colspan="2"><i class="fa-solid fa-gears"></i></th>
							</tr>
						</thead>
						<tbody>
							<c:forEach items="${films}" var="f">
								<tr>
									<td>${f.id}</td>
									<td>${f.title}</td>
									<td>${f.year}</td>
									<td>${f.director}</td>
									<td>${f.stars}</td>
									<td>${f.review}</td>
									<td>
										<form action="./delete" method="POST" class="delete-form">
											<button type="submit" name="did" value="${f.id}">
												<i class="fa-regular fa-trash-can"></i>
											</button>
										</form>
									</td>
									<td>
										<form action="./update" class="update-form-home">
											<button type="submit" name="uid"
												value="${f.id}">
												<i class="fa-regular fa-pen-to-square"></i>
											</button>
										</form>
									</td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
				</div>
			</div>
		</c:when>
		<c:otherwise>
			<div class="message-container">
				<div class="message">
					No results found for&nbsp;<b>${searchString}</b>.
				</div>
			</div>
		</c:otherwise>
	</c:choose>
</body>
</html>