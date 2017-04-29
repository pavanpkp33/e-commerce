var baseUrl = "/E-commerce/";
var product = {};
$(document).ready(function() {
    $("#overlay").show();
    $("#continue").on('click', backButtonHandler);
    $("#search").on('submit', onSubmitHandler);

    var info;
        if(localStorage.getItem("info")){
            info = localStorage.getItem("info");
        }else{
            window.location.href ="proj3.html";
        }
    getCartItems();
    getProductDetails(info);
    $("#cart-button").on('click', addToCartHandler);
    $("#cartButton").on('click', cartBtnHandler);
   
});

function getCartItems(){

    if(localStorage.getItem("cart")){
        var cartJson = JSON.parse(localStorage.getItem("cart"));
        var cartBtn =  '<button type=\"button\" class=\"button-cart\"><i class=\"fa fa-shopping-cart\"></i> &nbsp;CART &nbsp;('+Object.keys(cartJson).length+')</button>';
        $("span#cartButton").html(cartBtn);
    }else{
        var cartBtn =  '<button type=\"button\" class=\"button-cart\"><i class=\"fa fa-shopping-cart\"></i> &nbsp;CART &nbsp;(0)</button>';
        $("span#cartButton").html(cartBtn);
    }

}

function cartBtnHandler(){

    window.location.href = 'cart.html';
}


function getProductDetails(sku) {

    $.ajax( {

        url: baseUrl+'GetProduct?sku='+sku,
        type: "GET",

        success: function(response) {
            if(response.status == 200){

                handleResponse(response);

            }

            $("#overlay").hide();

        },
        error: function(response) {
            $("#overlay").hide();
            alert("Something not right! reload the page");


        }
    });


}

function backButtonHandler(){
    localStorage.removeItem("info");
    window.location.href="proj3.html";
}


function handleResponse(response) {

    product = response.data[0];

    var  stockIndicator ="", stockText = "",  quantity = "";

    if(product.stock < 0){
        stockIndicator = 'stock-indicator-blue';
        stockText = "Coming Soon"

        $("#cart-button").html("<button class=\"btn-orange disabled\" disabled>ADD TO CART</button>");
        quantity = "";
    }else if(product.stock == 0){
        stockIndicator = 'stock-indicator-red';
        stockText = "More on the way";
        $("#cart-button").html("<button class=\"btn-orange disabled\" disabled>ADD TO CART</button>");
        quantity = "0 Available";

    }else{
        stockIndicator = 'stock-indicator-green';
        stockText = "In stock";
        $("#cart-button").html("<button class=\"btn-orange \" >ADD TO CART</button>");
        quantity = product.stock.toString()+" Available";
    }
    var imgsrc = '<img src="uploads/'+response.data[0].image+'"  class="img-product-display" />';
    //var product = response.data[0];
    $("#imgPlace").html(imgsrc);
    var divString = '<span class=\"card-title-details\">'+product.vendorId+'</span>' +
        '<hr class="hr-light"/>'+
        '<span class=\"card-title-details\">Description</span>'+
        '<span class=\"card-product-description\">'+product.description+' </span>'+
        '<hr class="hr-light"/>'+
        '<span class=\"card-price-details\">$'+product.retail+'</span>'+
        '<div class=\"card-body\" >'+
        '<p class=\"'+stockIndicator+' float-left\">'+stockText+'</p>'+
        '<p class=\"float-left\">'+quantity+'</p>'+
        '</div>'+
        '<span class=\"card-title-details\">Features</span>'+
        '<span class=\"card-product-description\" >'+product.features+'</span>';
    $("#productDetails").html(divString);
}

function displaySnackBar(message){

    // Get the snackbar DIV
    var x = document.getElementById("snackbar")
    $("#snackText").text(message);
    // Add the "show" class to DIV
    x.className = "show";


    setTimeout(function(){ x.className = x.className.replace("show", ""); }, 2000);

}

function addToCartHandler() {

    if(localStorage.getItem("cart")){
        var sku = product.sku;
        var existingCartItems = JSON.parse(localStorage.getItem("cart"));
        if( existingCartItems.hasOwnProperty(sku)){
            var exisQuantity = existingCartItems[sku].quantity;
            exisQuantity++;
            if(exisQuantity <= product.stock){
                existingCartItems[sku].quantity = exisQuantity;
                localStorage.setItem("cart", JSON.stringify(existingCartItems));
                getCartItems();
                displaySnackBar("Quantity updated in cart.");
            }else{
                displaySnackBar("Error! Quantity in cart exceeds available stock.");
            }

        }else{
            var cartItem = {};
            cartItem = product;

            existingCartItems[cartItem.sku] = { "quantity" : 1,"item" :cartItem};

            localStorage.setItem("cart", JSON.stringify(existingCartItems));
            getCartItems();
            displaySnackBar("1 Item ( "+cartItem.vendorId+" ) added to cart");

        }

    }else{
        var cartItem = {};
        var storageItem = {};
        cartItem = product;
        storageItem[cartItem.sku] = { "quantity" : 1,
            "item" :cartItem};

        localStorage.setItem("cart", JSON.stringify(storageItem));
        getCartItems();
        displaySnackBar("1 Item ( "+cartItem.vendorId+" ) added to cart");

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