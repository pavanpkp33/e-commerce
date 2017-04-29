var baseUrl = "/E-commerce/";
$(document).ready(function () {
    if(!(localStorage.getItem("cart") && localStorage.getItem("cart") != "{}")){
        window.location.href = 'proj3.html';
    }
    $("#same-shipping").on('change', sameShippingHandler);
    $("#div-shippingform").show();
    $("#div-billingform").hide();
    $("#div-ccform").hide();
    $("#div-ordersummary").hide();
    $("#shipping-form").on('submit', shpFormHandler);
    $("#billing-form").on('submit', billFormHandler);
    $("#btnBillingBack").on('click', btnBillingHandler);
    $("#btnccBack").on('click', btnccHandler);
    $("#btnSummarBack").on('click', btnSummarBackHandler);
    $("#btnCancel").on('click', btnCancelHandler);
    $("#ccform").on('submit', ccFormHandler);
    $("#order-summary").on('submit', orderFormHandler);
    $("#success").hide();
});

function shpFormHandler(e){
    //Shipping details form onclick
    e.preventDefault();
    var phone =  $("#sf_phone").val();
    var zipcode = $("#sf_zipcode").val();
    if(phone.length < 10 || phone.length > 10){
        alert("Phone number should have 10 digits");
        return;
    }
    if(zipcode.length > 5 || zipcode.length < 5){
        alert("Invalid ZIP.");
        return;
    }
    $("#div-shippingform").hide();
    $("#div-billingform").show();
}


function btnBillingHandler() {
    //Billing button back clicked
    $("#div-shippingform").show();
    $("#div-billingform").hide();
    $("#div-ccform").hide();
    $("#div-ordersummary").hide();

}

function billFormHandler(e) {
//billing details form onclick
    e.preventDefault();

    $("#div-billingform").hide();
    $("#div-ccform").show();

}

function ccFormHandler(e) {

    e.preventDefault();

    var cnumber = $("#card_number").val();
    var card_exp = $("#card_exp").val();
    var card_cvv = $("#card_cvv").val();

    if(cnumber.length > 16 || cnumber.length < 15){
        alert("Invalid Card number");
        return;
    }
    if(card_exp.length != 6){
        alert("Invalid expiry date.");
        return;
    }
    if(card_cvv.length > 4 || card_cvv.length < 3){
        alert("Invalid CVV");
        return;
    }
    $("#div-ccform").hide();
    $("#div-ordersummary").show();
    //add code to fetch LocalStorage
    var items = JSON.parse(localStorage.getItem("cart"));
    var total = 5, taxes = 0, outstanding = 0;
    var summary = "";
    for(var key in items){
        total += Number(items[key].item.retail)*Number(items[key].quantity);
        summary += '<div class=\"cart-product-details\">'+
        '<div class=\"col-filter float-left\">'+
        '<img src=\"uploads/'+items[key].item.image+'\"  class=\"cart-img-product\" />'+
        '</div>'+
        '<div class=\"col-products float-right\">'+
        '<p class=\"cart-font-title\">'+items[key].item.vendorId+'</p>'+
        '<p class=\"font-thin-black\">Quantity selected : '+items[key].quantity+' </p>'+
        '<h3>$'+Number(items[key].item.retail)*Number(items[key].quantity)+'</h3>'+
        '</div>'+
        '</div>';
        
    }
    $("#displaySummary").html(summary);
    taxes = total*0.08;
    outstanding = total + taxes;
    $("#final-amount").text('$'+outstanding.toFixed(2));


}


function btnccHandler() {

    $("#div-billingform").show();
    $("#div-ccform").hide();
    $("#div-shippingform").hide();
    $("#div-ordersummary").hide();

}


function btnSummarBackHandler() {
    $("#div-ccform").show();
    $("#div-shippingform").hide();
    $("#div-billingform").hide();
    $("#div-ordersummary").hide();
}

function btnCancelHandler() {

    window.location.href = 'proj3.html';
}

function orderFormHandler(e) {
    $("#overlay").show();
    e.preventDefault();
    var dataArr = [], cartItems = {}, item = {};
    cartItems = JSON.parse(localStorage.getItem("cart"));
    for(var key in cartItems){
        if(cartItems.hasOwnProperty(key)){
            item["sku"] = key;
            item["quantity"] = cartItems[key].quantity;
        }
        dataArr.push(item);
        item = {};
    }

    
    $.ajax( {

        url: baseUrl+'UpdateHandsOn',
        type: "POST",
        data : { cart : JSON.stringify(dataArr)},
        success: function(response) {

            if(response.status == 200){

                $("#checkout-forms").hide();
                $("#success").show();
                localStorage.removeItem("cart");
                $("#overlay").hide();

            }else{

                $("#overlay").hide();
                alert("Unexpected Error!"+response.message);
            }



        },
        error: function(response) {
            $("#overlay").hide();
            alert("Unexpected Error!"+response.message);


        }
    });
}

function sameShippingHandler(e) {
    var firstName = $("#sf_firstname").val();
    var lastName = $("#sf_lastname").val();
    var email =     $("#sf_email").val();
    var phone =     $("#sf_phone").val();
    var address =  $("#sf_address").val();
    var city = $("#sf_city").val();
    var state = $("#sf_state").val();
    var country = $("#sf_country").val();
    var zipcode = $("#sf_zipcode").val();
    if($(e.target).is(':checked')){
        //fill
         $("#bf_firstname").val(firstName);
        $("#bf_lastname").val(lastName);
         $("#bf_email").val(email);
        $("#bf_phone").val(phone);
        $("#bf_address").val(address);
        $("#bf_city").val(city);
        $("#bf_state").val(state);
         $("#bf_country").val(country);
        $("#bf_zipcode").val(zipcode);

    }else{
        //delete
    	$("#billing-form").trigger('reset');
    }

}


