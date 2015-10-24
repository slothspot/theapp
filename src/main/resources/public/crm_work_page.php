<?php session_start();
include ("mysql.php");
?>
<!DOCTYPE html>
<html>
  <head>
    <title>Робочое место CRM </title>
    <meta name="keywords" content="" />
	<meta name="description" content="" />
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <!-- Bootstrap -->
    <link href="css/bootstrap.min.css" rel="stylesheet">
<link href="css/responsive-calendar.css" rel="stylesheet">
    <link href="css/style.css" rel="stylesheet">
    <link href='http://fonts.googleapis.com/css?family=Raleway:400,100,600' rel='stylesheet' type='text/css'>	
   <?
if(!isset($_SESSION['login'])){
if (isset($_POST['id_company'])) { $id_company = $_POST['id_company']; if ($id_company == '') { unset($id_company);} } 
if (isset($_POST['login'])) { $login = $_POST['login']; if ($login == '') { unset($login);} } 
if (isset($_POST['password'])) { $password=$_POST['password']; if ($password =='') { unset($password);} }
if (empty($login) or empty($password)) 
    {
    exit ("Вы ввели не всю информацию, вернитесь назад и заполните все поля!");
    }
	$id_company = stripslashes($id_company);
    $id_company = htmlspecialchars($id_company);
    $login = stripslashes($login);
    $login = htmlspecialchars($login);
    $password = stripslashes($password);
    $password = htmlspecialchars($password);
    $id_company = trim ($id_company);
    $login = trim($login);
    $password = trim($password); 
    $result = mysql_query("SELECT * FROM db_company WHERE company_id='$id_company'"); 
    $client = mysql_fetch_array($result);
    if (empty($client['company_personal']))
    {
    exit ("Извините, введённый вами данные неверный.");
    }
    else {
    if ($client['company_personal']==$login AND $client['company_personal_password']) {
    $_SESSION['id_company']=$client['company_id']; 
	$_SESSION['login']=$client['company_personal']; 
	$_SESSION['type']=$client['company_personal_type'];
    $_SESSION['id']=$client['id'];
    
    }
 else {
    exit ("Извините, введённый вами login или пароль неверный.");
    }
    }}
    ?> 
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
                  <li><a class="menu" href="salesexpert.php">Контрагенты</a></li>
                  <li><a class="menu" href="request_page.php">Заявки</a></li>
                  <li><a class="menu" href="pricelist.php">Онлайн прайс</a></li>
                </ul>
              </nav>
            </div>
          </div>
        </div>
      </div>
   </div>    
  <div class="clear"></div>
 <div class="work_area">
<div class="container">
      <!-- Responsive calendar - START -->
    	<div class="responsive-calendar">
        <div class="controls">
            <a class="pull-left" data-go="prev"><div class="btn btn-primary"><</div></a>
            <h4><span data-head-year></span> <span data-head-month></span></h4>
            <a class="pull-right" data-go="next"><div class="btn btn-primary">></div></a>
        </div><hr/>
        <div class="day-headers">
          <div class="day header">Пн.</div>
          <div class="day header">Вт.</div>
          <div class="day header">Ср.</div>
          <div class="day header">Чт.</div>
          <div class="day header">Птн.</div>
          <div class="day header">Сб.</div>
          <div class="day header">Вс.</div>
        </div>
        <div class="days" data-group="days">
          
        </div>
      </div>
      <!-- Responsive calendar - END -->
    </div>
    <script src="js/jquery.js"></script>
    <script src="js/responsive-calendar.js"></script>
    <script type="text/javascript">
      $(document).ready(function () {
        $(".responsive-calendar").responsiveCalendar({
          time: '2015-09',
          events: {
            "2015-09-21": {"number": 5}}
        });
      });
    </script>
    
   </div> 

   <div class="crm_footer">
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

<iframe src="https://calendar.yandex.ru/week?embed&layer_ids=2342304&tz_id=Europe/Kiev" width="100%" height="450" frameborder="0" style="border: 1px solid #eee"></iframe>
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
    <script src="js/jquery.cycle2.min.js"></script>
    <script src="js/jquery.nivo.slider.pack.js"></script>
    <script>$.fn.cycle.defaults.autoSelector = '.slideshow';</script>
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
                  }
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
          controlNav: false
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
          $('#back-top').find('a').click(function () {
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