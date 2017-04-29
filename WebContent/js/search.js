var baseUrl = "/E-commerce/";

$(document).ready(function () {
    $("#search").on('submit', onSubmitHandler);
    var searchQuery = localStorage.getItem("search");
    getProducts(searchQuery);
    $("#search-results").on('click', $('button[type="button"]'), moreInfoHandler);
});

function getProducts(searchQuery) {
    $("#overlay").show();
    var url = baseUrl+'SearchProducts?search='+searchQuery;
    $.ajax( {

        url:url,
        type: "GET",

        success: function(response) {
            if(response.status == 200){

                searchResponseHandler(response.data);

            }else{
                alert("Error!"+response.message);
            }



        },
        error: function(response) {
            $("#overlay").hide();
            alert("Error! "+response.message);


        }
    });

}


function searchResponseHandler(data) {
  if(data != '[]'){

      var productString = "",  stockIndicator ="", stockText = "";
      for(var i=0; i< data.length; i++){

          if(data[i].stock < 0){
              stockIndicator = 'stock-indicator-blue';
              stockText = "Coming Soon"

          }else if(data[i].stock == 0){
              stockIndicator = 'stock-indicator-red';
              stockText = "More on the way";

          }else{
              stockIndicator = 'stock-indicator-green';
              stockText = "In stock";

          }

          productString += '<div class=\"cart-product-details\">'+
          '<div class=\"col-filter float-left\">'+
          '<img src=\"uploads/'+data[i].image+'\" class=\"cart-img-product\" />'+
          '</div>'+

          '<div class=\"col-products float-right\">'+
          '<p class=\"cart-font-title\">'+data[i].vendorId+'</p>'+
          '<p class=\"bold-black\">Price : $'+data[i].retail+'</p>'+
          '<span class=\"'+stockIndicator+'\">'+stockText+'</span>'+
          '<button type=\"button\" class=\"btn-remove\" id=\"'+data[i].sku+'\">More Info</button>'+
          '</div>'+
          '</div>'+
          '<hr class=\"hr-light\"/>';
      }

      $("#search-results").html(productString);

  }
    $("#overlay").hide();
}


function onSubmitHandler(e) {

    e.preventDefault();

    var search = $("#searchText").val();

    getProducts(search);

}

function moreInfoHandler(e){
    var btnId = $(e.target).attr('id')

    localStorage.setItem("info", btnId);

    window.location.href = "product-detail.html";
}