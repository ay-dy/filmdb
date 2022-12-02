<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Insert</title>
<link rel="stylesheet" type="text/css" href="./css/styles.css">
</head>
<body>
	<div class="main-container">
		<div class="form-container">
			<div class="form-wrapper">
				<form action="./insert" method="POST" class="insert-form">
					<h2>Insert Film</h2>
					
					<label>Title *</label>
					<input type="text" name="insert-data" maxlength="100" autocomplete="off" required>
					
					<label>Year *</label>
					<input type="number" name="insert-data" min="0" max="99999999" required>
					
					<label>Director *</label>
					<input type="text" name="insert-data" maxlength="100" autocomplete="off" required>
					
					<label>Stars *</label>
					<input type="text" name="insert-data" maxlength="100" autocomplete="off" required>
					
					<label>Review *</label>
					<textarea name="insert-data" class="review-field" rows="14" maxlength="500" required></textarea>
					
					<div class="insert-button-wrapper">
						<input type="submit" value="Insert">
					</div>
				</form>
			</div>
		</div>
	</div>
</body>
</html>