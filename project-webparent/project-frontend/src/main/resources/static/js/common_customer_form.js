var dropDownCountry;
var dataListState;
var fieldState;

$(document).ready(function() {
	dropDownCountry = $("#country");//dropdown country
	dataListState = $("#listStates");//list States
	fieldState = $("#state");//state
	
	dropDownCountry.on("change", function() {
		loadStatesForCountry();//khi chọn country thì load toàn bộ states của country đó
		fieldState.val("").focus();
	});
});

function loadStatesForCountry() {
	selectedCountry = $("#country option:selected");//lấy ra country đang được chọn
	countryId = selectedCountry.val();
	url = contextPath + "settings/list_states_by_country/" + countryId;
	
	$.get(url, function(responseJSON) {
		dataListState.empty();
		
		$.each(responseJSON, function(index, state) {
			$("<option>").val(state.name).text(state.name).appendTo(dataListState);
		});
		
	}).fail(function() {
		alert('failed to connect to the server!');
	});
}

function checkPasswordMatch(confirmPassword) {
	if (confirmPassword.value != $("#password").val()) {
		confirmPassword.setCustomValidity("Passwords do not match!");
	} else {
		confirmPassword.setCustomValidity("");
	}
}