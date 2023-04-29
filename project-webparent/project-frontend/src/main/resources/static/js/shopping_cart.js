$(document).ready(function(){
    $(".linkMinus").on("click", function(e){
        e.preventDefault();
        decreaseQuantity($(this));
    });

    $(".linkPlus").on("click", function(e){
        e.preventDefault();
        increaseQuantity($(this));
    });

    $(".linkRemove").on("click", function(e){
        e.preventDefault();
        removeProduct($(this));
    });

    $(".size").on("change", function(e){
        e.preventDefault();
        updateSize($(this));
    })
});

function updateSize(option) {
    size =   parseInt($("#input-option6303").val()) ;
    productId = option.attr("pid");
    quantity = $("#quantity" + productId).val();
    updateQuantity(productId, quantity, size);
}

function decreaseQuantity(link) {
    productId = link.attr("pid");
    quantityInput = $("#quantity" + productId);
    size =   parseInt($("#input-option6303").val()) ;
    newQuantity = parseInt(quantityInput.val())-1;

    if(newQuantity > 0){
        quantityInput.val(newQuantity);
        updateQuantity(productId, newQuantity, size);
    } else {
        showWarningModal('Minimum quantity is 1');
    }
}

function increaseQuantity(link) {
    productId = link.attr("pid");
    quantityInput = $("#quantity" + productId);
    size =   parseInt($("#input-option6303").val()) ;
    newQuantity = parseInt(quantityInput.val())+1;

    if(newQuantity <= 5){
        quantityInput.val(newQuantity);
        updateQuantity(productId, newQuantity, size);
    } else {
        showWarningModal('Maximum quantity is 5');
    }
}

function updateQuantity(productId, newQuantity, size) {
    url = contextPath + "cart/update/" + productId + "/" + newQuantity + "/" + size;

    $.ajax({
        type : "POST",
        url : url,
        beforeSend: function(xhr) {
			xhr.setRequestHeader(csrfHeaderName, csrfValue);
		}
    }).done(function(updatedSubtotal){
        updateSubtotal(updatedSubtotal, productId);
        updateTotal();
    }).fail(function(){
        showErrorModal("Error while updating product quantity.");
    });
}

function updateSubtotal(updatedSubtotal, productId){
    $("#subtotal" + productId).text(formatNumber(updatedSubtotal));
}

function updateTotal() {
    total = 0.0;
    productCount = 0;

    $(".subtotal").each(function(index, element){
        productCount++;
        total += parseFloat(clearFormatNumber(element.innerHTML)); //lấy ra giá trị tiền của từng subTotal
    });                                                     // làm tròn giá trị thập phân

    if (productCount <1) {
        showEmptyShoppingCart();
    } else {
        $("#total").text(formatNumber(total)); 
    }
}

function showEmptyShoppingCart(){
    $("#sectionTotal").hide();
    $("#sectionEmptyCartMessage").removeClass("d-none");
}


function removeProduct(link){
    url = link.attr("href");
    
    $.ajax({
        type: "DELETE",
        url: url,
        beforeSend: function(xhr) {
			xhr.setRequestHeader(csrfHeaderName, csrfValue);
		}
    }).done(function(response){
        rowNumber = link.attr("rowNumber");
        removeProductHTML(rowNumber);
        updateTotal();
        updateCountNumbers();

        showModalDialog("Shopping Cart", response);


    }).fail(function(){
        showErrorModal("Error while removing product.");
    });
}

function removeProductHTML(rowNumber) {
    $("#row" + rowNumber).remove();
	$("#blankLine" + rowNumber).remove();
}

function updateCountNumbers() {
	$(".divCount").each(function(index, element) {
		element.innerHTML = "" + (index + 1);
	}); 
}

function formatNumber(number){
    return  $.number(number, 2, ".", ",");
}

function clearFormatNumber(number){
    return number.replaceAll(",","");
}