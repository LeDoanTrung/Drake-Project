$(document).ready(function() {
	$(".linkMinus").on("click", function(evt) {//bắt sự kiện khi nhấn button -
		evt.preventDefault();
		productId = $(this).attr("pid");//lấy ra giá trị của thuộc tính pid
		quantityInput = $("#quantity" + productId);//lấy ra thẻ có id là quantity0 hoặc quantity1,...tùy vào productId 
		newQuantity = parseInt(quantityInput.val()) - 1;//vì nhấn button - nên giảm số lượng đi 1 
		
		if (newQuantity > 0) {
			quantityInput.val(newQuantity);//set giá trị mới vào thẻ có id là quantity0 hoặc quantity1,...tùy vào productId 
		} else {
			showWarningModal('Minimum quantity is 1');//nếu số lượng <= 0 thì hiện modal báo số lượng tối thiểu là 1
		}
	});
	
	$(".linkPlus").on("click", function(evt) {//bắt sự kiện khi nhấn button +
		evt.preventDefault();
		productId = $(this).attr("pid");
		quantityInput = $("#quantity" + productId);
		newQuantity = parseInt(quantityInput.val()) + 1;//vì nhấn button + nên tăng số lượng lên 1
		
		if (newQuantity <= 5) {
			quantityInput.val(newQuantity);
		} else {
			showWarningModal('Maximum quantity is 5');//nếu số lượng > 5 thì hiện modal báo số lượng tối đa là 5
		}
	});	
});