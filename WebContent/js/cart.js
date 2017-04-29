var baseUrl = "/E-commerce/";
var fCart, eCart;


$(document).ready(function() {

    $("#continueShopping").on('click', btnCtnHandler);
    $("#search").on('submit', onSubmitHandler);
    $("#placeOrder").on('click', btnOrderHandler);
    fCart = document.getElementById("fill-cart");
    eCart = document.getElementById("empty-cart");
    initCheck();
    $('#cartItems').on('input', $('input[type="number"]'), inputHandler);
    $('#cartItems').on('click', $('button[type="button"]'), removeHandler);

});

function btnCtnHandler(){
	
	window.location.href = 'proj3.html';
}

function initCheck(){
    if(localStorage.getItem("cart") && localStorage.getItem("cart") != "{}"){
        fCart.style.display = 'block';
        eCart.style.display = 'none';
        populateItems();
    }else{
        fCart.style.display = 'none';
        eCart.style.display = 'block';
        // $("#empty-cart").addClass("container-main-search");
    }
}

function btnOrderHandler(){
	
	window.location.href = 'checkout.html';
}
function populateItems(){
    var totalItemCost = 0, totalItems ;
    var existingCartItems = JSON.parse(localStorage.getItem("cart"));
    totalItems = Object.keys(existingCartItems).length;
    var productString = "";
    for(var key in existingCartItems){
        if(existingCartItems.hasOwnProperty(key)){
            var product = existingCartItems[key];
            totalItemCost += product.item.retail*product.quantity;
            productString += '<div class=\"cart-product-details\">'+
            '<div class=\"col-filter float-left\">'+
            '<img src=\"uploads/'+product.item.image+'\"  class=\"cart-img-product\" />'+
            '<span class=\"quantity\"> Qty : <input class=\"input-quantity\" type="number" min=\"0\" max=\"999\" id=\"'+product.item.sku+'\" value=\"'+product.quantity+'\" name=\"quantity\"></span>'+
            '</div>'+
            '<div class=\"col-products float-right\">'+
            '<p class=\"cart-font-title\">'+product.item.vendorId+'</p>'+
            '<h3>$'+product.item.retail*product.quantity+'</h3>'+
            '<p class=\"font-thin-black\">Quantity Available : '+product.item.stock+' </p>'+
            '<button type=\"button\" class=\"btn-remove\" id=\"btn_'+product.item.sku+'\">REMOVE</button>'+
            '</div>'+
            '</div>'+
            '<hr class=\"hr-light\"/>';

        }
    }
    //adding shipping charge
    var withShipping = totalItemCost + 5;
    var taxes = withShipping*0.08;
    var totalAmount = withShipping + taxes;
    var summaryString = '<div class=\"cart-card\">'+
                         '<p class=\"cart-title\">PRICE DETAILS</p>'+
                        '<hr class=\"hr-light\"/>'+
                        '<div class=\"display-inline\">'+
                        '<span class=\"cart-title font-thin-black\">Price('+totalItems+' item(s))</span>'+
                        '<span class=\"span-1\">$'+totalItemCost.toFixed(2)+'</span>'+
                        '</div>'+
                        '<div class=\"display-inline\">'+
                        '<span class=\"cart-title font-thin-black\">Delivery Charges</span>'+
                        '<span class=\"span-2\">$5</span>'+
                        '</div>'+
                        '<div class=\"display-inline\">'+
                        '<span class=\"cart-title font-thin-black\">Taxes</span>'+
                        '<span class=\"span-4\">$'+taxes.toFixed(2)+'</span>'+
                        '</div>'+
                        '<hr class=\"hr-dashed\"/>'+
                        '<div class=\"display-inline\">'+
                        '<span class=\"cart-title font-bold-black\">Amount payable</span>'+
                        '<span class=\"font-bold-black span-3\">$'+totalAmount.toFixed(2)+'</span>'+
                        '</div>'+
                        '</div>';

    $("#cartItems").html(productString);
    $("#summary").html(summaryString);
    $("#overlay").hide();
}

function inputHandler(e) {
    var textId = $(e.target).attr('id');
    var value = Number($(e.target).val());

    setTimeout(function(){
        $("#overlay").hide();
    },750);
    if(value == ""){
        value = 0;
    }

    if(localStorage.getItem("cart")) {

        var existingCartItems = JSON.parse(localStorage.getItem("cart"));

        if (existingCartItems.hasOwnProperty(textId)) {
            var curProduct = existingCartItems[textId];

            if (value <= curProduct.item.stock) {
                existingCartItems[textId].quantity = value;
                localStorage.setItem("cart", JSON.stringify(existingCartItems));
                initCheck();
                displaySnackBar("Cart updated!");

            } else {
                displaySnackBar("Entered quantity exceeds available quantity!");
            }

        }
    }
    
}

function displaySnackBar(message){

    // Get the snackbar DIV
    var x = document.getElementById("snackbar")
    $("#snackText").text(message);
    // Add the "show" class to DIV
    x.className = "show";


    setTimeout(function(){ x.className = x.className.replace("show", ""); }, 2000);

}

function removeHandler(e) {
    var btnId = $(e.target).attr('id');
    var btnArr = btnId.split("_");
    //$("#overlay").show();
    if(localStorage.getItem("cart")) {

        var existingCartItems = JSON.parse(localStorage.getItem("cart"));

        if (existingCartItems.hasOwnProperty(btnArr[1])) {
                delete existingCartItems[btnArr[1]];

                localStorage.setItem("cart", JSON.stringify(existingCartItems));
                initCheck();
                displaySnackBar("Item removed from cart!");

        }
    }


}


function onSubmitHandler(e) {
    $("#overlay").show();
    e.preventDefault();
    var searchQuery = $("#searchText").val();
    localStorage.setItem("search",searchQuery);
    window.location.href = 'search.html';
    $("#overlay").hide();



}