<?php session_start();
include ("mysql.php");
?>
<!DOCTYPE html>
<html>
  <head>
    <title>Онлайн Прайс</title>
    <meta name="keywords" content="" />
	<meta name="description" content="" />
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <!-- Bootstrap -->
    <link href="css/bootstrap.min.css" rel="stylesheet">
    <link href="css/responsive-calendar.css" rel="stylesheet">
    <link href="css/style.css" rel="stylesheet">
    <link href="css/circle.css" rel="stylesheet">
    <link rel="stylesheet" href="css/lightbox.css" type="text/css" media="screen" />
    <link href='http://fonts.googleapis.com/css?family=Raleway:400,100,600' rel='stylesheet' type='text/css'>	
    <script type="text/javascript" src="js/lightbox-plus-jquery.min.js"></script>
</head>
  <body>
  <div id="home"></div>
  <div class="top" style="position:fixed; top:0;">
      <div class="container crm_container">
        <div class="row">
          <div class="col-sm-3 col-md-3">
            <div class="avatar"> <img  src="images/client/1.jpg" alt="Photo_personal" width="200px" height="200px" style="width:70px; height:70px; float:left;" ><? echo "<h5>Пользователь: ",$_SESSION['login'], "</h5>";?> </div>
          </div>
          <div class="col-sm-9 col-md-9 crm_col9">
            <div id="top-menu">
              <nav class="mainMenu">
                <ul class="nav">
                  <li><a class="menu" href="index.html">Главная</a></li>
                  <li><a class="menu" href="crm_work_page.php">Рабочее место</a></li>
                  <li><a class="menu" href="salesexpert.php">Контрагенты</a></li>
                  <li><a class="menu" href="request_page.php">Заявки</a></li>
                </ul>
              </nav>
            </div>
          </div>
        </div>
      </div>
   </div>    
  <div class="clear"></div>
 <div class="work_area">                     
                          
 <h2 align="center">Онлайн Прайс</h2>
 <div class="panel-group" id="pricelist">
   <?
$type=mysql_query("SELECT DISTINCT `product_type` FROM `pricelist` ");
$list_type=mysql_fetch_assoc($type);

do
{   
    $test=array();
	$p=$list_type['product_type'];
    if(!isset($test[$p])){
    array_push ($test,$list_type['product_type']);}else{break;}
	foreach($test as $key=>$type_items)
	{   
	
		$t=mysql_query("SELECT * FROM `pricelist` WHERE `product_type`='$type_items'");
        $list=mysql_fetch_array ($t);
		?><div class="panel panel-default"><div class="panel-heading">
      <h4 class="panel-title"><a data-toggle="collapse" data-parent="#price1ist" href="#<?=$type_items;?>"><? echo $list['product_type_title'];?></a></h4>
    </div> 
	<div id="<?=$type_items;?>" class="panel-collapse collapse "><?
		do
		{
			
$list['product_about'] = mb_substr($list['product_about'], 0, 165, 'UTF-8') . '...';

		printf('
      <div class="panel-body"><img style="height:100px; width:100px; float:left; margin-right:20px;" src="%s" alt="%s"/><div align="center" style="color:#000;"><u><h5><strong>%s</strong></h5></u></div>
<p align="justify">%s</p>
<div align="right"><strong style="color:#FF0004;right;" ><u>Стоимость :%s грн.</u></strong></div></div>
   
  ',$list['producte_img'],$list['product_title'],$list['product_title'],$list['product_about'],$list['product_price_out']);
		}while($list=mysql_fetch_array ($t));
		?> </div><?
	}
	
}while($list_type=mysql_fetch_array ($type));
?>

</div>
</div>
  </div>
 <div class="crm_footer" style=" ">
  <div class="container">
    <div class="clear"></div>
    <div class="col-xs-6 col-sm-6 col-md-3 crm_col12">
      <h2>Контакты</h2>
      <span class="left col-xs-1 fa fa-map-marker"></span> <span class="right col-xs-11">м.Киев ул.Анны Ахматовой 13 оф.222</span>
      <div class="clear height10"></div>
      <span class="left col-xs-1 fa fa-phone"></span> <span class="right col-xs-11">044-220-18-73</span>
      <div class="clear height10"></div>
      <span class="left col-xs-1 fa fa-envelope"></span> <span class="right col-xs-11">info@crm.com</span>
      <div class="clear height10"></div>
      <span class="left col-xs-1 fa fa-globe"></span> <span class="right col-xs-11">www.crm.com</span>
      <div class="clear"></div>
    </div>
  </div>
</div>
<!--Footer End--> 
<!-- Bottom Start -->
<div class="crm_bottom">
  <div class="container">
    <div class="row">
      <div class="left"> <span>Все права защищены © 2015 <a href="#">Силенко С.В. СПД</a></span> </div>
    </div>
  </div>
</div>

<!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
    <!-- <script src="https://code.jquery.com/jquery.js"></script> -->

    <script src="js/bootstrap.min.js"></script>
    <script src="js/jquery-1.11.2.min.js" type="text/javascript"></script>
    <script src="js/jquery.nivo.slider.pack.js"></script> <script>$.fn.cycle.defaults.autoSelector = '.slideshow';</script>
    <script type="text/javascript">
      $(function(){
          var default_view = 'grid';
          if($.cookie('view') !== 'undefined'){
              $.cookie('view', default_view, { expires: 7, path: '/' });
          } 
          function get_grid(){
              $('.list').removeClass('list-active');
              $('.grid').addClass('grid-active');
              $('.prod-cnt').animate({opacity:0},function(){
                  $('.prod-cnt').removeClass('dbox-list');
                  $('.prod-cnt').addClass('dbox');
                  $('.prod-cnt').stop().animate({opacity:1});
              });
          }
          function get_list(){
              $('.grid').removeClass('grid-active');
              $('.list').addClass('list-active');
              $('.prod-cnt').animate({opacity:0},function(){
                  $('.prod-cnt').removeClass('dbox');
                  $('.prod-cnt').addClass('dbox-list');
                  $('.prod-cnt').stop().animate({opacity:1});
              });
          }
          if($.cookie('view') == 'list'){ 
              $('.grid').removeClass('grid-active');
              $('.list').addClass('list-active');
              $('.prod-cnt').animate({opacity:0});
              $('.prod-cnt').removeClass('dbox');
              $('.prod-cnt').addClass('dbox-list');
              $('.prod-cnt').stop().animate({opacity:1}); 
          } 

          if($.cookie('view') == 'grid'){ 
              $('.list').removeClass('list-active');
              $('.grid').addClass('grid-active');
              $('.prod-cnt').animate({opacity:0});
                  $('.prod-cnt').removeClass('dboxlist');
                  $('.prod-cnt').addClass('dbox');
                  $('.prod-cnt').stop().animate({opacity:1});
          }

          $('#list').click(function(){   
              $.cookie('view', 'list'); 
              get_list()
          });

          $('#grid').click(function(){ 
              $.cookie('view', 'grid'); 
              get_grid();
          });

          /* filter */
          $('.menuSwitch ul li').click(function(){
              var CategoryID = $(this).attr('category');
              $('.menuSwitch ul li').removeClass('cat-active');
              $(this).addClass('cat-active');
              
              $('.prod-cnt').each(function(){
                  if(($(this).hasClass(CategoryID)) == false){
                     $(this).css({'display':'none'});
                  };
              });
              $('.'+CategoryID).fadeIn(); 
              
          });
      });
    </script>
	<script src="js/jquery.singlePageNav.js"></script>
	
    <script type="text/javascript">
    $(window).load(function() {
        $('#slider').nivoSlider({
          prevText: '',
          nextText: '',
          controlNav: false,
        });
    });
    </script>
    <script>
      $(document).ready(function(){

        // hide #back-top first
        $("#back-top").hide();
        
        // fade in #back-top
        $(function () {
          $(window).scroll(function () {
            if ($(this).scrollTop() > 100) {
              $('#back-top').fadeIn();
            } else {
              $('#back-top').fadeOut();
            }
          });

          // scroll body to 0px on click
          $('#back-top a').click(function () {
            $('body,html').animate({
              scrollTop: 0
            }, 800);
            return false;
          });
        });

      });
      </script>
      <script type="text/javascript">
      <!--
          function toggle_visibility(id) {
             var e = document.getElementById(id);
             if(e.style.display == 'block'){
                e.style.display = 'none';
                $('#togg').text('show footer');
            }
             else {
                e.style.display = 'block';
                $('#togg').text('hide footer');
            }
          }
      //-->
      </script>
      <script type="text/javascript" src="js/lib/jquery.mousewheel-3.0.6.pack.js"></script>

      <script type="text/javascript">
      $(function() {
        $('a[href*=#]:not([href=#])').click(function() {
          if (location.pathname.replace(/^\//,'') == this.pathname.replace(/^\//,'') && location.hostname == this.hostname) {
            var target = $(this.hash);
            target = target.length ? target : $('[name=' + this.hash.slice(1) +']');
            if (target.length) {
              $('html,body').animate({
                scrollTop: target.offset().top
              }, 1000);
              return false;
            }
          }
        });
      });
      </script>
      <script src="js/stickUp.min.js" type="text/javascript"></script>
      <script type="text/javascript">
        //initiating jQuery
        jQuery(function($) {
          $(document).ready( function() {
            //enabling stickUp on the '.navbar-wrapper' class
            $('.mWrapper').stickUp();
          });
        });
      </script>
      <script>
     $('a.menu').click(function(){
    $('a.menu').removeClass("active");
    $(this).addClass("active");
	});
      </script>
      
      <script> <!-- scroll to specific id when click on menu -->
      	 // Cache selectors
var lastId,
    topMenu = $("#top-menu"),
    topMenuHeight = topMenu.outerHeight()+15,
    // All list items
    menuItems = topMenu.find("a"),
    // Anchors corresponding to menu items
    scrollItems = menuItems.map(function(){
      var item = $($(this).attr("href"));
      if (item.length) { return item; }
    });

// Bind click handler to menu items
// so we can get a fancy scroll animation
menuItems.click(function(e){
  var href = $(this).attr("href"),
      offsetTop = href === "#" ? 0 : $(href).offset().top-topMenuHeight+1;
  $('html, body').stop().animate({ 
      scrollTop: offsetTop
  }, 300);
  e.preventDefault();
});

// Bind to scroll
$(window).scroll(function(){
   // Get container scroll position
   var fromTop = $(this).scrollTop()+topMenuHeight;
   
   // Get id of current scroll item
   var cur = scrollItems.map(function(){
     if ($(this).offset().top < fromTop)
       return this;
   });
   // Get the id of the current element
   cur = cur[cur.length-1];
   var id = cur && cur.length ? cur[0].id : "";
   
   if (lastId !== id) {
       lastId = id;
       // Set/remove active class
       menuItems
         .parent().removeClass("active")
         .end().filter("[href=#"+id+"]").parent().addClass("active");
   }                   
});
      </script>
      </body>
</html>