$(document).ready(function(){
    $("#buttonAdd2Cart").on("click", function(e){
        addToCart();
    });

    $("#buttonAdd2Favor").on("click", function(e){
        addToFavor();
    })
});

function addToCart(){
    quantity = $("#quantity" + productId).val();
    size = $("#input-option6303").val();
    url = contextPath + "cart/add/" + productId + "/" + quantity+"/"+size;
    if(size == "") {
        showModalDialog("Shopping Cart", "Vui lòng chọn size");
    }
    else{
        $.ajax({
            type: "POST",
            url: url,
            beforeSend: function(xhr){
                xhr.setRequestHeader(csrfHeaderName,csrfValue);
            }
        }).done(function(response){
            showModalDialog("Shopping Cart", response);
        }).fail(function(){
            showErrorModal("Error while adding product to shopping cart.")
        });
    }
}

function addToFavor(){
    
    url = contextPath + "favor/add/" + productId;
    
        $.ajax({
            type: "POST",
            url: url,
            beforeSend: function(xhr){
                xhr.setRequestHeader(csrfHeaderName,csrfValue);
            }
        }).done(function(response){
            showModalDialog("Favorite List", response);
        }).fail(function(){
            showErrorModal("Error while adding product to favorite list.")
        });
    
}

