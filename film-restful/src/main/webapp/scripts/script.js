getAllFilms("search_form_format", "table_wrap");
// Pre-populate the value of insert form's ID field.
// It takes longer to pre-populate the value than to display the form, so the 
// delay is visible to the user if it was done upon clicking NEW FILM button.
let nextAutoIncrement = "";
updateInsertFormId();

// ==================== Request initialisers ====================
function getAllFilms(format, filmTable) {
	format = getEncodedValue(format);

	const address = "filmapi?opt=af&format=" + format;
	const responseHandler = getSuitableHandler(format);

	ajaxGet(address, filmTable, function(request) {
		responseHandler(request, filmTable);
	});
}

function getSearchResults(searchQuery, format, filmTable) {
	title = getEncodedValue(searchQuery);
	format = getEncodedValue(format);

	const address = "filmapi?opt=sr&q=" + title + "&format=" + format;
	const responseHandler = getSuitableHandler(format);

	ajaxGet(address, filmTable, function(request) {
		responseHandler(request, filmTable);
	});
}

function getNextAutoIncrement(insertFormIdField, format) {
	format = getEncodedValue(format);
	const address = "filmapi?opt=nai&format=" + format;

	ajaxGet(address, insertFormIdField, function(request) {
		nextAutoIncrementHandler(request, insertFormIdField);
	});
}

function insertFilm(insertForm, format) {
	const filmData = getFieldValues(getInputFields(insertForm));
	format = getEncodedValue(format);

	const address = "filmapi";
	const data = formatFilmData(filmData, format);
	const acceptType = getAcceptType(format);
	console.log(formatFilmData(filmData, format));
	/*
	const data = "title=" + encodeURIComponent(filmData[1])
		+ "&year=" + encodeURIComponent(filmData[2])
		+ "&director=" + encodeURIComponent(filmData[3])
		+ "&stars=" + encodeURIComponent(filmData[4])
		+ "&review=" + encodeURIComponent(filmData[5]);
	*/
	ajaxPost(address, data, acceptType, function(request) {
		insertHandler(request, insertForm, filmData);
	})
}

function updateFilm(insertFormId, format) {
	const filmData = getFieldValues(getInputFields(insertFormId));
	format = getEncodedValue(format);
	//console.log(format);

	//console.log(filmData);
	const address = "filmapi";
	const data = formatFilmData(filmData, format);
	const acceptType = getAcceptType(format);
	console.log(formatFilmData(filmData, format));

	/*
	const data = {
		"id": filmData[0],
		"title": filmData[1],
		"year": filmData[2],
		"director": filmData[3],
		"stars": filmData[4],
		"review": filmData[5]
	}
	*/

	ajaxPut(address, data, acceptType, function(request) {
		updateHandler(request, insertFormId, filmData);
	});

}

function deleteFilm(filmId, resultRegion) {
	const address = "filmapi?id=" + filmId;

	ajaxDelete(address, resultRegion, function(request) {
		deleteHandler(request, filmId);
	});
}

// ==================== Request functions ====================
function ajaxGet(address, filmTableDivId, responseHandler) {
	const request = new XMLHttpRequest();
	request.onreadystatechange = function() {
		responseHandler(request, filmTableDivId);
	}
	request.open("GET", address, true);
	request.send(null);
}

function ajaxPost(address, data, acceptType, responseHandler) {
	const request = new XMLHttpRequest();
	request.onreadystatechange = function() {
		responseHandler(request);
	}
	request.open("POST", address, true);
	request.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
	request.setRequestHeader("Accept", acceptType)
	request.send(data);
}

function ajaxPut(address, data, acceptType, responseHandler) {
	const request = new XMLHttpRequest();
	request.onreadystatechange = function() {
		responseHandler(request);
	}
	request.open("PUT", address, true);
	request.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
	request.setRequestHeader("Accept", acceptType)
	request.send(data);
}

function ajaxDelete(address, resultRegion, responseHandler) {
	const request = new XMLHttpRequest();
	request.onreadystatechange = function() {
		responseHandler(request, resultRegion)
	}
	request.open("DELETE", address, true);
	request.send(null);
}

// ==================== Response handlers ====================
function displayJsonFilmTable(request, filmTableDivId) {
	if (request.readyState == 4 && request.status == 200) {
		closeAll();
		const films = JSON.parse(request.responseText);

		//showResultCount(films.length);

		if (films.length > 0) {
			const preparedHeadings = [];
			const preparedFilms = [];

			// Loop through the first film object and populate the "preparedHeadings" array with its keys.
			for (let key in films[0]) {
				// Capitalise the first character and concatenate it to the remaining ones.
				preparedHeadings.push(key.charAt(0).toUpperCase() + key.slice(1));
			}
			//console.log("JSON headings: ", preparedHeadings);

			// Loop through all film objects and populate the "preparedFilms" array.
			films.forEach(film => {
				const preparedFilm = Object.values(film);
				preparedFilms.push(preparedFilm);
			});
			//console.log("JSON films: ", preparedFilms);

			htmlInsert(filmTableDivId, generateTable(preparedHeadings, preparedFilms));

			showElement(filmTableDivId);
			scrollToTop(filmTableDivId);
		} else {
			hideElement(filmTableDivId);
			document.getElementById("search_message_content").innerHTML = "No results found for <b>" + document.getElementById("search_form_input").value + "</b>.";
			showElement("search_message_wrap");
		}
	}
}

function displayXmlFilmTable(request, filmTableDivId) {
	if (request.readyState == 4 && request.status == 200) {
		closeAll();

		const films = request.responseXML.getElementsByTagName("film");
		//showResultCount(films.length);

		if (films.length > 0) {
			const headings = films[0].children;
			const preparedHeadings = [];
			const preparedFilms = [];

			// Loop through the first film object and populate the "preparedHeadings" array with its children's nodeNames.
			for (let i = 0; i < headings.length; i++) {
				let heading = headings[i].nodeName;
				// Capitalise the first character and concatenate it to the remaining ones.
				preparedHeadings.push(heading.charAt(0).toUpperCase() + heading.slice(1));
			}
			//preparedHeadings.push("Actions");
			//console.log("XML headings: ", preparedHeadings);

			// Loop through all film objects and populate the "preparedFilms" array with its non-id versions.
			for (let i = 0; i < films.length; i++) {
				let rawFilmData = films[i].children;
				let preparedFilmData = [];
				for (let j = 0; j < rawFilmData.length; j++) {
					preparedFilmData.push(rawFilmData[j].innerHTML);
				}
				preparedFilms.push(preparedFilmData);
			}
			//console.log("XML films: ", preparedFilms);

			htmlInsert(filmTableDivId, generateTable(preparedHeadings, preparedFilms));

			showElement(filmTableDivId);
			scrollToTop(filmTableDivId);
		} else {
			hideElement(filmTableDivId);
			document.getElementById("search_message_content").innerHTML = "No results found for <b>" + document.getElementById("search_form_input").value + "</b>.";
			showElement("search_message_wrap");
		}
	}
}

function displayStringFilmTable(request, filmTableDivId) {
	if (request.readyState == 4 && request.status == 200) {
		closeAll();
		// Split the string with each new line.
		const films = request.responseText.split(/\n+/);
		// We don't need to account for the headings.
		//showResultCount(films.length - 1);

		// If there's more than just headings inside the "films" array...
		if (films.length > 1) {
			const preparedHeadings = films[0].split("|");

			//preparedHeadings.push("Actions");
			// Now that we got the headings in a separate array, we can drop them from the films array'
			films.shift();
			//console.log("String headings: ", preparedHeadings);

			const preparedFilms = [];
			for (let i = 0; i < films.length; i++) {
				let preparedFilmData = films[i].split("|");
				preparedFilms.push(preparedFilmData);
			}
			//console.log("String films: ", preparedFilms);

			htmlInsert(filmTableDivId, generateTable(preparedHeadings, preparedFilms));

			showElement(filmTableDivId);
			scrollToTop(filmTableDivId);
		} else {
			hideElement(filmTableDivId);
			document.getElementById("search_message_content").innerHTML = "No results found for <b>" + document.getElementById("search_form_input").value + "</b>.";
			showElement("search_message_wrap");
		}
	}
}

function deleteHandler(request, filmId) {
	closeForm("film_data_wrap");
	if (request.readyState == 4 && request.status == 200) {
		removeElement(filmId);
		const rowCount = document.getElementById("table").rows.length;
		if (rowCount == 1) {
			hideElement("table_wrap");
		}
		console.log(rowCount);
		showCrudMessage(request.responseText, "message_wrap", "message_content");
	}
}

function insertHandler(request, insertFormId, filmData) {
	closeForm(insertFormId);
	if (request.readyState == 4 && request.status == 200) {
		appendNewFilmRow(filmData);
		scrollToBottom("table_wrap");
		showCrudMessage(request.responseText, "message_wrap", "message_content");
		// The auto_increment value will increase with each insertion, so it needs updating
		// on the front-end.
		updateInsertFormId();
	}
}

function updateHandler(request, insertFormId, filmData) {
	closeForm(insertFormId);
	if (request.readyState == 4 && request.status == 200) {
		updateFilmRow(filmData);
		showCrudMessage(request.responseText, "message_wrap", "message_content");
	}
}

function nextAutoIncrementHandler(request, insertFormIdField) {
	if (request.readyState == 4 && request.status == 200) {
		nextAutoIncrement = request.responseText;
		setNewFilmId(insertFormIdField);
	}
}

// All of this will be used to create a function "updateFilmCount" but that will be added later.
//const filmCountDivContent = document.getElementById("film-count").innerHTML;
// Remove the "Results: " part of the String.
//let filmCount = filmCountDivContent.split(" ").slice(1);
//filmCount = Number(filmCount) - 1;
//htmlInsert("film-count", "Results: " + filmCount);

// ==================== Utility functions ====================

function generateTable(headings, films) {
	return ("<table id=\x22table\x22>" + generateTableHead(headings) + generateTableBody(films) + "</table>");
}

function generateTableHead(headings) {
	headings.push("<button class=\x22new-film-button\x22 onclick=\x22prepareInsertForm()\x22><i class=\x22fa-solid fa-plus\x22></i></button>");

	let thead = "<thead><tr>";
	headings.forEach((heading, index) => {
		if (index === 6) {
			// The "actions" heading must span across 2 columns, as there are 2 buttons.
			thead += "<th colspan='2'>" + heading + "</th>";
		} else {
			thead += "<th>" + heading + "</th>";
		}
	})
	thead += "</tr></thead>";

	return thead;
}

function generateTableBody(films) {
	let tbody = "<tbody>";

	films.forEach(film => {
		tbody += generateTableRow(film);
	});

	tbody += "</tbody>";

	return tbody;
}

function generateTableRow(film) {
	let filmId = film[0];
	let tr = "<tr id=\x22" + filmId + "\x22>";

	for (index in film) {
		tr += "<td>" + film[index] + "</td>";
	}

	generateActionButtons(filmId).forEach(button => {
		tr += button;
	})

	tr += "</tr>"

	return tr;
}

function generateActionButtons(filmId) {
	let buttons = [];

	buttons.push(
		/*
		   * The double quotes that embody onclick content had to be 
		   * encoded (\x22) to prevent formatting errors. The result region id
		   * "crud-message" is hard-coded because this is where the function is
		   * called and can't reference it otherwise.
		*/
		"<td class=\x22table-field-delete\x22><button class=\x22delete-button\x22 onclick=\x22deleteFilm('" + filmId + "', 'message')\x22>"
		+ "<i class='fa-regular fa-trash-can'></i>"
		+ "</button></td>"
	);

	buttons.push(
		"<td class=\x22table-field-update\x22><button class=\x22update-button\x22 onclick=\x22prepareUpdateForm('" + filmId + "', 'message')\x22>"
		+ "<i class='fa-solid fa-pencil'></i>"
		+ "</button></td>"
	);

	return buttons;
}

function appendNewFilmRow(filmData) {
	const tableBody = document.getElementsByTagName("tbody")[0]
	tableBody.innerHTML += generateTableRow(filmData);
}

function updateFilmRow(filmData) {
	const elementToUpdate = document.getElementById(filmData[0]);
	const tableRow = Array.from(elementToUpdate.children);
	tableRow.pop();
	tableRow.pop();
	filmData.forEach((detail, index) => {
		tableRow[index].innerHTML = detail;
	})
	elementToUpdate.scrollIntoView(true);
	console.log(tableRow);
}

function scrollToTop(elementId) {
	let element = document.getElementById(elementId);
	element.scrollTop = 0;
}

function scrollToBottom(elementId) {
	let element = document.getElementById(elementId);
	element.scrollTop = element.scrollHeight - element.clientHeight;
}

function getSuitableHandler(format) {
	switch (format) {
		case "json":
			return displayJsonFilmTable;
		case "xml":
			return displayXmlFilmTable;
		default:
			return displayStringFilmTable;
	}
}

function htmlInsert(elementId, data) {
	document.getElementById(elementId).innerHTML = data;
}

function getEncodedValue(elementId) {
	return encodeURIComponent(document.getElementById(elementId).value);
}

function showCrudMessage(text, messageWrapId, messageContentId) {
	htmlInsert(messageContentId, text);
	showElement(messageWrapId);
}

function hideElement(elementId) {
	document.getElementById(elementId).style.display = "none";
}

function showElement(elementId) {
	document.getElementById(elementId).style.display = "block";
}

function removeElement(elementId) {
	document.getElementById(elementId).remove();
}

// ======================================

function updateInsertFormId() {
	// Request initialiser
	getNextAutoIncrement("film_data_form_id", "search_form_format");
}

function prepareInsertForm() {
	clearForm(getInputFields("film_data_form")); // requires film data element references
	clearEventListeners("film_data_form_submit"); // requires a reference to submit button element

	setClassValue("film_data_form_id", "insert"); // requires a reference to ID element
	setClassValue("film_data_form_submit", "inactive insert"); // requires a reference to submit button element

	setNewFilmId("film_data_form_id"); // requires a reference to ID lement
	showElement("film_data_wrap"); // requires a reference to film data form wrapper element
}

function prepareUpdateForm(rowId, resultRegion) {
	const filmRow = Array.from(document.getElementById(rowId).children);
	const formFields = getInputFields("film_data_form");

	for (let i = 0; i < filmRow.length - 2; i++) {
		formFields[i].value = filmRow[i].innerHTML;
	}

	setClassValue("film_data_form_id", "update");
	setClassValue("film_data_form_submit", "active update");
	setEventListener("film_data_form_submit", "click", updateCallback);
	showElement("film_data_wrap");
}

function setClassValue(elementId, value) {
	document.getElementById(elementId).className = value;
}

function setEventListener(elementId, eventType, listenerToAdd) {
	clearEventListeners(elementId);
	document.getElementById(elementId).addEventListener(eventType, listenerToAdd);
}

/*
	Cloning a node does NOT copy the event listeners, so replacing an element
	with its own clone is essentially like having a removeEventListener() function that 
	wipes the lot instead of a specific one. That way, it saves us having to do 
	multiple checks if multiple event listeners are involved in the same button.
*/
function clearEventListeners(elementId) {
	const element = document.getElementById(elementId);
	element.replaceWith(element.cloneNode(true));
}

function clearClasses(elementId) {
	document.getElementById(elementId).className = "inactive";
}

function setNewFilmId(insertFormIdField) {
	document.getElementById(insertFormIdField).value = nextAutoIncrement;
}

function clearForm(inputFields) {
	// We want to keep the ID's value.
	inputFields.shift();
	inputFields.forEach(field => {
		field.value = "";
	});
}

function closeForm() {
	clearForm(getInputFields("film_data_form"));
	clearEventListeners("film_data_form_submit");
	hideElement("film_data_wrap");
}

function insertCallback() {
	//alert("Insert button clicked!")
	insertFilm("film_data_form", "search_form_format");
}

function updateCallback() {
	//alert("Update button clicked!");
	updateFilm("film_data_form", "search_form_format");
}

function checkFormInputFields(insertFormId) {
	const submitButton = document.getElementById("film_data_form_submit");
	const emptyFieldCheck = isEmpty(getFieldValues(getInputFields(insertFormId)));
	manageSubmitButtonAttributes(submitButton, emptyFieldCheck);
}

function isEmpty(inputFields) {
	for (let i = 0; i < inputFields.length; i++) {
		if (inputFields[i] == "") {
			return true;
		}
	}
	return false;
}

function manageSubmitButtonAttributes(submitButton, emptyFieldCheck) {
	// We either have "insert" or "update" as the second class name.
	let option = submitButton.className.split(" ").slice(1);
	console.log("option: " + option);

	if (emptyFieldCheck) {
		submitButton.className = "inactive " + option;
		clearEventListeners(submitButton.id);
	} else {
		submitButton.className = "active " + option;
		if (option == "insert") {
			setEventListener(submitButton.id, "click", insertCallback);
		} else {
			setEventListener(submitButton.id, "click", updateCallback);
		}
	}

}

function getInputFields(insertFormId) {
	const inputFields = Array.from(document.getElementById(insertFormId).children);
	inputFields.pop();
	//inputFields.shift();

	return inputFields;
}

function getFieldValues(inputFields) {
	inputFields.forEach((field, index) => {
		inputFields[index] = field.value;
	});
	return inputFields;
}

function blurActiveElement() {
	document.activeElement.blur();
}

function closeAll() {
	hideElement("search_message_wrap")
	hideElement("message_wrap");
}

// Won't be used on the search string. It will be used on INSERT and UPDATE parts.
// Still not utilised.
function formatSearchString(searchString, format) {
	switch (format) {
		case "json":
			return JSON.stringify({
				"q": searchString,
				"format": format
			});
		case "xml":
			return new DOMParser().parseFromString(
				"<search>" +
				"<text>" + searchString + "</text>" +
				"</search>", "text/xml"
			);
		default:
			return "";
	}
}

function formatFilmData(filmData, format) {
	switch (format) {
		case "json":
			return JSON.stringify({
				"id": filmData[0],
				"title": filmData[1],
				"year": filmData[2],
				"director": filmData[3],
				"stars": filmData[4],
				"review": filmData[5]
			});
		case "xml":
			return new DOMParser().parseFromString(
				"<film>" +
				"<id>" + filmData[0] + "</id>" +
				"<title>" + filmData[1] + "</title>" +
				"<year>" + filmData[2] + "</year>" +
				"<director>" + filmData[3] + "</director>" +
				"<stars>" + filmData[4] + "</stars>" +
				"<review>" + filmData[5] + "</review>" +
				"</film>", "text/xml"
			);
		default:
			return filmData[0] + "|" +
				filmData[1] + "|" +
				filmData[2] + "|" +
				filmData[3] + "|" +
				filmData[4] + "|" +
				filmData[5];
	}
}

function getAcceptType(format) {
	switch (format) {
		case "json":
			return "application/json";
		case "xml":
			return "text/xml";
		default:
			return "text/plain";
	}
}