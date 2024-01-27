
console.log(" hello moto ");


const toggleSidebar=()=>{

        if($(".sidebar").is(":visible")){

          $(".sidebar").css("display","none"); // side bar band kiya
          $(".content").css("margin-left","0%");//margin 0 taki shift ho content left 

        }else{

             $(".sidebar").css("display","block"); // side bar chalu karo
             $(".content").css("margin-left","20%"); // margin 20 taki shift ho content right
        }
}
