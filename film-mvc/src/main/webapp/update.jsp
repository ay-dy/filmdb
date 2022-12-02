<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Update</title>
<link rel="stylesheet" type="text/css" href="./css/styles.css">
</head>
<body>
	<div class="main-container">
		<div class="form-container">
			<div class="form-wrapper">
				<form action="./update" method="POST" class="update-form">
					<h2>Update Film</h2>
					
					<label>Title *</label>
					<input type="text" name="update-data" value="${film.title}" maxlength="100" autocomplete="off" required>
					
					<label>Year *</label>
					<input type="number" name="update-data" value="${film.year}" min="0" max="99999999" required>
					
					<label>Director *</label>
					<input type="text"name="update-data" value="${film.director}" maxlength="100" autocomplete="off" required>
					
					<label>Stars *</label>
					<input type="text" name="update-data" value="${film.stars}" maxlength="100" autocomplete="off" required>
					
					<label>Review *</label>
					<textarea name="update-data" class="review-field" rows="14" maxlength="500" required>${film.review}</textarea>
					
					<div class="update-button-wrapper">
						<button type="submit" name="update-button" value="${film.id}">Update</button>
					</div>
				</form>
			</div>
		</div>
	</div>
</body>
</html>