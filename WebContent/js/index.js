/**
 * Created by Pkp on 04/23/17.
 */

var baseUrl = "/E-commerce/";
filterObj = {};
filterObj.filter = {};
productArr = {};
brand = [], category = [], availability = [];


$(document).ready(function() {
    $("#overlay").show();
    $("#search").on('submit', onSubmitHandler);
    getCartItems();
    getBrands();
    $("#filterCheckBox").on('change', $('input[type="checkbox"]'),checkBoxHandler);
    $('#products').on('click', $('button[type="button"]'), buttonClickHandler);
    $('#cartButton').on('click', cartBtnHandler);
});

function getCartItems(){

    if(localStorage.getItem("cart")){
        var cartJson = JSON.parse(localStorage.getItem("cart"));
        var cartBtn =  '<button type=\"button\" class=\"button-cart\" id=\"cartButton\"><i class=\"fa fa-shopping-cart\"></i> &nbsp;CART &nbsp;('+Object.keys(cartJson).length+')</button>';
        $("span#cartButton").html(cartBtn);
    }else{
        var cartBtn =  '<button type=\"button\" class=\"button-cart\" id=\"cartButton\"><i class=\"fa fa-shopping-cart\"></i> &nbsp;CART &nbsp;(0)</button>';
        $("span#cartButton").html(cartBtn);
    }

}

var getBrands = function(){

    $.ajax( {

        url: baseUrl+'GetBrands',
        type: "GET",

        success: function(response) {
            if(response.status == 200){
                populateBrands(response);
                getCategories();
            }else{
                alert("Something not right! reload the page");
            }

         //   $("#overlay").hide();

        },
        error: function(response) {
            $("#overlay").hide();
            alert("Something not right! reload the page");


        }
    });
}


var getCategories = function () {

    $.ajax( {

        url: baseUrl+'GetCategories',
        type: "GET",

        success: function(response) {
            if(response.status == 200){
            	
                populateCategories(response);
                getProducts(null);
            }else{
                alert("Something not right! reload the page");
            }



        },
        error: function(response) {
            $("#overlay").hide();
            alert("Something not right! reload the page");


        }
    });

};
function populateCategories(response) {

    var categoryFilter = "";
    for(var i=0; i< response.categories.length; i++ ){
        categoryFilter += '<p> <input type=\"checkbox\" id=\"category_'+response.categories[i]+'\" value=\"category_'+response.categories[i]+'\" /> <span>'+response.categories[i]+'</span></p>';
    }
    $("#categoryCheckBox").html(categoryFilter);

}

function populateBrands(response) {

    var brandFilter = "";
    for(var i=0; i< response.data.length; i++ ){

        brandFilter += '<p> <input type=\"checkbox\" id=\"brand_'+response.data[i]+'\" value=\"brand_'+response.data[i]+'\" /> <span>'+response.data[i]+'</span></p>';
    }
    $("#brandCheckBox").html(brandFilter);

}

function handleProductResponse(response) {

    productArr = response.data;

    if(productArr == "[]"){
        $("#resultCount").text("0");
        $("#products").html("");
    }else{
        $("#resultCount").text(productArr.length);
        var productList = "", stockIndicator ="", stockText = "", buttonStatus = "";
        for( var i =0; i <response.data.length; i++){
            if(productArr[i].stock < 0){
                stockIndicator = 'stock-indicator-blue';
                stockText = "Coming Soon"
                buttonStatus = "disabled";
            }else if(productArr[i].stock == 0){
                stockIndicator = 'stock-indicator-red';
                stockText = "More on the way";
                buttonStatus = "disabled";
            }else{
                stockIndicator = 'stock-indicator-green';
                stockText = "In stock";
                buttonStatus = "";
            }
            productList += '<div class=\" col-3 float-left\" >' +
            '<div class=\"card\">'+
            '<div class=\"product-details\">'+
            '<img src=\"uploads/'+productArr[i].image+'\"  class=\"img-product\" />'+
            '<p class=\"card-title\">'+productArr[i].vendorId+'</p>'+
            '<div class=\"card-body\">'+
            '<p class=\"'+stockIndicator+' float-left\">'+stockText+'</p>'+
            '<p class=\"cost float-right\">$'+productArr[i].retail+'</p>'+
            '</div>'+
            '<div class=\"card-body\">'+
            '<button type=\"button\" id=\"add_'+productArr[i].sku+'\"  class=\"btn-orange '+buttonStatus+'\" '+buttonStatus+'><i class=\"fa fa-shopping-cart\"></i>&nbsp ADD TO CART</button>'+
            '<button type=\"button\" id=\"info_'+productArr[i].sku+'\"  class=\"btn-blue\"><i class=\"fa fa-info-circle\"></i>&nbsp MORE INFO</button>'+
            '</div> </div></div></div>';


        }
        $("#products").html(productList);
    }

    $("#overlay").hide();
}
function getProducts(){

   $.ajax( {

        url: baseUrl+'GetProducts',
        type: "POST",

        success: function(response) {
            console.log(response);
            if(response.status == 200){

                handleProductResponse(response);

            }else{
                alert("Something not right! reload the page");
                $("#overlay").hide();
            }



        },
        error: function(response) {
            $("#overlay").hide();
            alert("Something not right! reload the page");


        }
    });
}

function getProductsFilter(filterArr){

    $.ajax( {

        url: baseUrl+'GetProducts',
        type: "POST",
        data : { filter : JSON.stringify(filterArr)},
        success: function(response) {
            console.log(response);
            if(response.status == 200){

                handleProductResponse(response);

            }else{
                alert("Something not right! reload the page");
                $("#overlay").hide();
            }



        },
        error: function(response) {
            $("#overlay").hide();
            alert("Something not right! reload the page");


        }
    });
}




function checkBoxHandler(e){
	 $("#overlay").show();
    var arr = $(e.target).val();
    var type = arr.split("_");
    
    if($(e.target).is(':checked')){
        if(type[0] == 'brand'){
            brand.push(type[1]);

        }else if(type[0] == 'category'){

            category.push(type[1]);
        }else{
           availability.push(type[1]);
        }
        filterObj.filter.brand = brand;
        filterObj.filter.category = category;
        filterObj.filter.availability = availability;

    }else{
        if(type[0] == 'brand'){
            var index = brand.indexOf(type[1]);
            if(index != -1){
                brand.splice( index, 1 );
            }

        }else if(type[0] == 'category'){
            var index = category.indexOf(type[1]);
            if(index != -1){
                category.splice( index, 1 );
            }

        }else{
            var index = availability.indexOf(type[1]);
            if(index != -1){
                availability.splice( index, 1 );
            }
        }
        filterObj.filter.brand = brand;
        filterObj.filter.category = category;
        filterObj.filter.availability = availability;
       
    }
    getProductsFilter(filterObj.filter);


}

function buttonClickHandler(e) {

    var btnId = $(e.target).attr('id');
    var action = [];
    if(btnId !== undefined){
        action = btnId.split("_");

        if(action[0] == "add"){
            //add to cart
            if(localStorage.getItem("cart")){
                var sku = action[1], eQuantity = 0;
                var existingCartItems = JSON.parse(localStorage.getItem("cart"));

               if( existingCartItems.hasOwnProperty(sku)){
                   for( var i =0; i < productArr.length; i++){
                       if(productArr[i].sku == sku){
                           eQuantity = productArr[i].stock;
                           break;
                       }
                   }
                   var exisQuantity = existingCartItems[sku].quantity;
                   exisQuantity++;
                   if(exisQuantity <= eQuantity){
                       existingCartItems[sku].quantity = exisQuantity;

                       localStorage.setItem("cart", JSON.stringify(existingCartItems));
                       getCartItems();
                       displaySnackBar("Quantity updated in cart.");
                   }else{
                       displaySnackBar("Error! Quantity in cart exceeds available stock.");
                   }

               }else{
                   var cartItem = {};
                   for( var i =0; i < productArr.length; i++){
                       if(productArr[i].sku == action[1]){
                           cartItem = productArr[i];
                           break;
                       }
                   }
                   existingCartItems[cartItem.sku] = { "quantity" : 1,
                       "item" :cartItem};

                   localStorage.setItem("cart", JSON.stringify(existingCartItems));
                   getCartItems();
                   displaySnackBar("1 Item ( "+cartItem.vendorId+" ) added to cart");

               }

            }else{
                var cartItem = {};
                var storageItem = {};
                for( var i =0; i < productArr.length; i++){
                    if(productArr[i].sku == action[1]){
                        cartItem = productArr[i];
                        break;
                    }
                }
                storageItem[cartItem.sku] = { "quantity" : 1,
                                              "item" :cartItem};

                localStorage.setItem("cart", JSON.stringify(storageItem));
                getCartItems();
                displaySnackBar("1 Item ( "+cartItem.vendorId+" ) added to cart");

            }

        }else{
            //Info

            localStorage.setItem("info", action[1]);

            window.location.href = "product-detail.html";

        }
    }
}

function displaySnackBar(message){

       // Get the snackbar DIV
        var x = document.getElementById("snackbar");
         $("#snackText").text(message);
        // Add the "show" class to DIV
        x.className = "show";


        setTimeout(function(){ x.className = x.className.replace("show", ""); }, 2000);

}

function cartBtnHandler() {
    window.location.href = "cart.html";
}


function onSubmitHandler(e) {
    $("#overlay").show();
    e.preventDefault();
    var searchQuery = $("#searchText").val();
    localStorage.setItem("search",searchQuery);
    window.location.href = 'search.html';
    $("#overlay").hide();
    
    

}