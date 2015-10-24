<?php session_start();
include ("mysql.php");
?>
<!DOCTYPE html>
<html>
<head>
<title>Журнал контрагентов</title>
<meta name="keywords" content="" />
<meta name="description" content="" />
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<!-- Bootstrap -->
<link href="css/bootstrap.min.css" rel="stylesheet">
<link href="css/style.css" rel="stylesheet">
<link rel="stylesheet" href="css/misc.css">
<link rel="stylesheet" href="css/nivo-slider.css">
<link href="http://fonts.googleapis.com/css?family=Raleway:400,100,600" rel="stylesheet" type="text/css">
<script type="text/javascript" src="js/jquery.js"></script>
<script type="text/javascript" src="js/script.js"></script>
</head>
<body>
<header> 
  <!-- Меню -->
  <div id="home">
     <div class="top">
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
  <!-- меню -->
</div>
</header>
<div class="clear"></div>
<div id="myModal" class="modal hide fade" role="dialog" aria-labelledby="myModalLabel"  aria-hidden="true">
  <div class="modal-body" style="width:80%; height:80%" >
  </div>
  </div>
<div class='work_area'> 
  
<div role="tabpanel" style="width:100%; height:80%; vertical-align:middle central;">
  <ul class="nav nav-tabs" role="tablist">
  <li class="dropdown active"><a href="#" id="tabDropOne1" class="dropdown-toggle" data-toggle="dropdown" role="tab">Контрагенты<b class="caret"></b></a>
      <ul class="dropdown-menu" role="menu" aria-labelledby="tabDropOne1">
        <li><a href="#sales" tabindex="-1" data-toggle="tab">Покупатели</a></li>
        <li><a href="#buy" tabindex="-1" data-toggle="tab">Поставщики</a></li>
        <li><a href="#contact" tabindex="-1" data-toggle="tab"> Контактные лица</a></li>
        <li><a href="#in_work" tabindex="-1" data-toggle="tab">Этапи работы</a></li>
      </ul>
    </li>
    
    <li><a href="#dell" data-toggle="tab" role="tab">События</a></li>
    
  </ul>
  <nav style="margin:20px 20px 20px 20px;">
  <a href="#" class="glyphicon glyphicon-plus-sign" data-toggle="modal" data-target="#add" style="padding-left:20px;"> Добавить</a>
  <a href="#" class="glyphicon glyphicon-pencil" data-toggle="modal" data-target="#edit" style="padding-left:20px;"> Изменить</a>
  <a href="#" class="glyphicon glyphicon-remove" data-toggle="modal" data-target="#delet" style="padding-left:20px;"> Удалить</a>
</nav>
<div align="center" id="tabContent1" class="tab-content" style="width:85%;">
       
     <!-- задачи-->
    <div class="tab-pane fade" id="dell">
      <?
	  $table_title=array(); 
	  ?>
    </div>
    <!-- конец задачи-->
    <div class="tab-pane fade" id="sales">
    
    <!-- Таблица покупатели-->
    <? $table_title=array("№","Название организации","КОД ЕГРПОУ","Сегмент","WWW","Телефон","Адрес","Контакт.лицо","Куратор:"); 
	$table_result="`id`,`company_name`,`company_id`,`segment`,`www`,`contact_phone`,`company_adress`,`contact_personal`,`responsible`";
	$table_parametrs='company';
	$search_parametrs="`company_typ` =  'Потребитель'";
	?>

    </div>
<!-- Конец Таблица покупатели-->
  <!-- Таблица поставщики-->
    </div>
    <div class="tab-pane fade" id="buy">
      <p>Таблица поставщиков </p>
    </div>
    <!-- Конец Таблица поставщики -->
    <!-- Контакты-->
    <div class="tab-pane fade" id="contact">
      <p>Общий список контактов </p>
    </div>
    <!-- Конец контакты-->
  </div>
</div>

<!-- Сама форма таблицы-->
 <div class="containerDiv" id="#request" style="color:#ABF4EC">
   <div id="tablewrapper">
		<div id="tableheader">
        	<div class="search">
                <select id="columns" onchange="sorter.search('query')"></select>
                <input type="text" id="query" onkeyup="sorter.search('query')" />
            </div>
            <span class="details" >
				<div><span id="startrecord"></span> -<span id="endrecord"></span> с <span id="totalrecords"></span></div>
        		<div><a href="javascript:sorter.reset()">Обновить</a></div>
        	</span>
        </div>
        <table cellpadding="0" cellspacing="0" border="0" id="table" class="tinytable">
            <thead>
                <tr>
                   <? foreach($table_title as $title){echo "<th><h3>".$title."</h3></th>";}
				   ?>
                </tr>
            </thead>
            <tbody>
                <?
				$query=mysql_query("SELECT ".$table_result." FROM `".$table_parametrs."` WHERE ".$search_parametrs."");                $result=mysql_fetch_assoc ($query);
										
				do
				{
					echo "<tr>";
					
					foreach($result as $items)
					{

						echo "<td>".$items."</td>";
						
					}
					echo "</tr>";
			
				}while($result=mysql_fetch_assoc ($query));
				?>
                </tbody>
        </table>
   </div>
        <div id="tablefooter">
           <div id="tablenav">
            	<div>
                    <img src="/images/first.gif" width="5px" height="5px" alt="First Page" onclick="sorter.move(-1,true)" />
                    <img src="images/previous.gif" width="5px" height="5px" alt="First Page" onclick="sorter.move(-1)" />
                    <img src="images/next.gif" width="5px" height="5px" alt="First Page" onclick="sorter.move(1)" />
                    <img src="images/last.gif" width="5px" height="5px" alt="Last Page" onclick="sorter.move(1,true)" />
                </div>
                <div>
                	Страница :<select id="pagedropdown"></select>
				</div>
             <div>
                	<a href="javascript:sorter.showall()">Показать все</a>
                </div>
            </div>
			<div id="tablelocation" align="left">
            	<div><div class="page">Страница <span id="currentpage"></span>    с<span id="totalpages"></span></div>
                <span>Отображать:</span>
                    <select onchange="sorter.size(this.value)">
                    <option value="5">5</option>
                        <option value="10" selected="selected">10</option>
                        <option value="20">20</option>
                        <option value="50">50</option>
                        <option value="100">100</option>
                    </select>
                    
                </div>
                
            </div>
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
<!--Footer End--> 
<!-- Bottom Start -->
<div class="crm_bottom">
  <div class="container">
    <div class="row">
      <div class="left"> <span>Все права защищены © 2015 <a href="#">Силенко С.В. СПД</a></span> </div>
    </div>
  </div>
</div>
<!-- Bottom End --> 

<!-- jQuery (necessary for Bootstrap's JavaScript plugins) --> 
<!-- <script src="https://code.jquery.com/jquery.js"></script> --> 
<script src="js/jquery-1.10.2.min.js"></script> 
<script src="js/bootstrap.min.js"></script> 
<script src="js/jquery.cycle2.min.js"></script> 
<script src="js/jquery.cycle2.carousel.min.js"></script> 
<script src="js/jquery.nivo.slider.pack.js"></script> 
<script>$.fn.cycle.defaults.autoSelector = '.slideshow';</script> 
<script type="text/javascript">
	var sorter = new TINY.table.sorter('sorter','table',{
		headclass:'head',
		ascclass:'asc',
		descclass:'desc',
		evenclass:'evenrow',
		oddclass:'oddrow',
		evenselclass:'evenselected',
		oddselclass:'oddselected',
		paginate:true,
		size:10,
		colddid:'columns',
		currentid:'currentpage',
		totalid:'totalpages',
		startingrecid:'startrecord',
		endingrecid:'endrecord',
		totalrecid:'totalrecords',
		hoverid:'selectedrow',
		pageddid:'pagedropdown',
		navid:'tablenav',
		sortcolumn:1,
		sortdir:1,
		columns:[{index:7, format:'%', decimals:1},{index:8, format:'$', decimals:0}],
		init:true
	});
  </script>
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