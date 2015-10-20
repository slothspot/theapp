<?php session_start();
include ("mysql.php");
?>
<!DOCTYPE html>
<html>
<head>
<title>Журнал заявок <? echo  $request_type_add; ?></title>
<meta name="keywords" content="" />
<meta name="description" content="" />
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<!-- Bootstrap -->
<link href="css/bootstrap.min.css" rel="stylesheet">
<link href="css/style.css" rel="stylesheet">
<link rel="stylesheet" href="css/misc.css">
<link href="css/circle.css" rel="stylesheet">
<link href="css/jquery.bxslider.css" rel="stylesheet" />
<link rel="stylesheet" href="css/nivo-slider.css">
<link href="http://fonts.googleapis.com/css?family=Raleway:400,100,600" rel="stylesheet" type="text/css">
<script type="text/javascript" src="js/jquery.min.js"></script>
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
                  <li><a class="menu" href="#client">Контрагенты</a></li>
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
 <?
 if(isset($_POST['request_edit_id']))
     {
		 $request_edit_id=$_POST['request_edit_id'];
		 $request_title_edit=$_POST['request_title_edit'];
		 $request_need_edit=$_POST['request_edit_need'];
		 $request_type_edit=$_POST['request_type_edit'];
		 $request_about_edit=$_POST['request_about_edit'];
		 $request_img_edit="images/tovar/".$_POST['request_img_edit'];
		 $request_status_edit=$_POST['request_status_edit'];
		 $request_responsible_edit=$_POST['request_responsible_edit'];
		 $request_datetake=date("Y-m-d H:i:s");
		 $date_finish_edit=$_POST['date_finish_edit'];
		 $request_result=$_POST['request_result'];
		 $edit_request=mysql_query("UPDATE `request` SET `request_title` = '".$request_title_edit."',`request_type` = '".$request_type_edit."', `request_need` = '".$request_need_edit."', `request_about` = '".$request_about_edit."', `request_img` = '".$request_img_edit."', `date_take_work` = '".$request_datetake."',`date_finish` = '".$date_finish_edit."', `request_status` = '".$request_status_edit."', `request_responsible` = '".$request_responsible_edit."', `request_result`='".$request_result."' WHERE `id` = '".$request_edit_id."'");
		 
	 }
 if(isset($_POST['request_need_add']))
     {
		 $request_title_add=$_POST['request_title_add'];
		 $request_type_add=$_POST['request_type_add'];
		 $request_need_add=$_POST['request_need_add'];
		 $request_about_add=$_POST['request_about_add'];
		 $request_img_add="images/tovar/".$_POST['request_img_add'];
		 $request_datecreat=date("Y-m-d H:i:s");
		 $request_id_company_add=$_SESSION['id_company'];
		 $request_company_personal_add=$_SESSION['login'];
		 $sql = "INSERT INTO `request` (`company_id`, `company_personal`,  `request_title`, `request_type`, `request_need`, `request_about`, `request_img`, `request_priority`, `date_creat`, `request_status`) VALUES ('$request_id_company_add', '$request_company_personal_add','$request_title_add', '$request_type_add', '$request_need_add', '$request_about_add','$request_img_add', '1','$request_datecreat','Ждет подтверждения');";
if(!mysql_query($sql))
 {echo '<center><p><b>Ошибка при добавлении данных!</b></p></center>';}
 else
 {echo '<center><p><b>Данные успешно добавлены!</b></p></center>';}

	}
 
 ?>
    <h4 align="center">Журнал заявок</h4>
   <div class="rowDivHeaderMenu">
   <a href="#" class="glyphicon glyphicon-plus-sign" data-toggle="modal" data-target="#request_add"> Добавить запрос</a> </div>
   
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
                    <th class="nosort"><h3>Заявка</h3></th>
                    <th><h3>От</h3></th>
                    <th><h3>Тип заявки</h3></th>
                    <th><h3>Причина</h3></th>
                    <th><h3>Описание</h3></th>
                    <th><h3>Дата создания</h3></th>
                    <th><h3>Принято в работу</h3></th>
                    <th><h3>Изображение</h3></th>
                    <th><h3>Статус</h3></th>
                    <th><h3>Дата выполнение</h3></th>
                    <th><h3>Ответсветый</h3></th>
                    <th><h3>Результат</h3></th>
                </tr>
            </thead>
            <tbody>
<?  
$request=mysql_query("SELECT * FROM `request`");
$result=mysql_fetch_array ($request);
do {
if($result['request_status'] =='Выполнено'){$bg_state='#01BC05';}
if($result['request_status'] =='В работе'){$bg_state='#DAEF08';}
if($result['request_status'] =='Ждет подтверждения'){$bg_state='#CC0306';}	
if($result['request_type'] =='Просчет'){$bg_row='#ABF4EC';}else{$bg_row='#cee6fe'; }
if($result['date_finish']=='0000-00-00'){$result['date_finish']='-';}	
printf ('<tr style="background-color:%s" >
                    <td><a href="?id_request_to_edit=%s" data-toggle="modal" >%s</a></td>
                    <td>%s</td>
					<td>%s</td>
                    <td>%s</td>
                    <td>%s</td>
                    <td>%s</td>
                    <td>%s</td>
                    <td><img style="width:40px; height="40px" src="%s" /></td>
                    <td id="status" style="background-color:%s;"><strong>%s</strong></td>
                    <td>%s</td>
                    <td>%s</td>
					<td>%s</td>
                </tr>',$bg_row,$result['id'],$result['id'],$result['company_personal'],$result['request_type'],$result['request_need'],$result['request_title'],$result['date_creat'],$result['date_take_work'],$result['request_img'],$bg_state,$result['request_status'],$result['date_finish'],$result['request_responsible'],$result['request_result']);}
while ($result=mysql_fetch_array ($request)); ?></div>

</tbody>
        </table>
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
    </div>




<?php     //Редакт.запроса.
if(isset($_GET['id_request_to_edit'])){
$id_request_to_edit=$_GET['id_request_to_edit'];
$request_to_edit=mysql_query("SELECT * FROM `request` WHERE id='$id_request_to_edit'");
$result_to_edit=mysql_fetch_array ($request_to_edit);
if($result_to_edit['request_status'] =='Выполнено'){$bg_state='#01BC05';}
if($result_to_edit['request_status'] =='В работе'){$bg_state='#DAEF08';}
if($result_to_edit['request_status'] =='Ждет подтверждения'){$bg_state='#CC0306';}	
printf('
<form action="request_page.php" method="post" enctype="multipart/form-data">
<div class="modal show" id="request_edit" role="dialog" aria-labelledby="modal-title" aria-hidden="true">
  <div class="modal-dialog">
    <div class="modal-content">
      <div class="modal-header-blue" style="background-color:%s">
        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
        <h4 class="modal-title">
          <p style="color:#000"><strong>Заявка №: %s    Автор: %s    От:%s</strong></p>
		  <input name="request_edit_id" type="hidden" value="%s">
        </h4>
      </div>
        <div class="modal-body" align="left">
    <label>Запрос на:</label><br>
    <input type="text" name="request_title_edit" value="%s">
<img  style="width:auto; height:200px; float:right;" src="%s" alt="%s"><br>
    <label>Причина запроса:</label><br>
	<input type="text" name="request_edit_need" value="%s" /><br>
    <label>Тип запроса:</label><br>
    <select name="request_type_edit" ><option value="%s">%s</option>
             <option>Просчет</option>
             <option>Закупка</option>
             </select>
   <br> <label>Описание :</label><br>
    <textarea name="request_about_edit" rows="5" cols="70" >%s</textarea>
<br>
    <label>Изображение товара:<input name="request_img_edit" type="file" name="fupload" src="%s">
</label>
</div>
        <div class="modal-footer-blue" align="justify" style="background-color:%s">
		<div align="left" style="color:#000;"><label style="margin-right:5px;"> Статус : </label><select name="request_status_edit" style="margin-right:5px;"><option>%s</option>
		<option>Ждет подтверждения</option><option>В работе</option><option>Выполнено</option></select>
		<label style="margin-right:5px;" >Ответсвеный за выполнения: </label > <select name="request_responsible_edit" style="margin-right:5px;">
													  <option>%s</option>
													  <option>Вася</option>
													  <option>Петя</option>
													  <option>Коля</option> 
													  </select>
        <label style="margin-right:5px;">Дата выполнения :    </label><input type="date" name="date_finish_edit" style="margin-right:5px;">
		<label>Результат:</label>
	<input type="text" name="request_result" value="%s" /><br>
		</div>
         <input type="submit"   name="submit" class="btn btn-primary" value="Сохранить изменения"> <input align="right"  type="submit" name="submit"  class="btn btn-primary" value="Закрыть запрос">
        </div></form>
    </div>
  </div>
</div> ',$bg_state,$result_to_edit['id'],$result_to_edit['company_personal'],$result_to_edit['date_creat'],$result_to_edit['id'],$result_to_edit['request_title'],$result_to_edit['request_img'],$result_to_edit['request_title'],$result_to_edit['request_need'],$result_to_edit['request_type'],$result_to_edit['request_type'],$result_to_edit['request_about'],$result_to_edit['request_img'],$bg_state,$result_to_edit['request_status'],$result_to_edit['request_responsible'],$result_to_edit['request_result']);}
?>
<div class="modal fade" id="request_add"  role="dialog" aria-labelledby="modal-title" aria-hidden="true">
  <div class="modal-dialog" style="width:60%; "><form id="request_form_add" action="request_page.php" method="post">
    <div class="modal-content">
      <div class="modal-header-blue">
        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
        <h4 class="modal-title">
          <p style="color:#fff"><strong>Оформление заявки: </strong></p>
        </h4>
      </div> 
        <div class="modal-body" align="center" id="login">
    <label>Запрос на:</label>
    <input type="text" name="request_title_add"> 
<br>
    <label>Тип запроса:<br></label>
    <select name="request_type_add" >
             <option>Просчет стоимости</option>
             <option>Закупка</option>
             </select>
<br>
<label>Причина запроса:</label><br>
	<input type="text" name="request_need_add"/><br>

    <label>Описание запроса:<br></label>
    <textarea name="request_about_add" rows="10" cols="70"></textarea>
<br>
    <label>Изображение товара:<input name="request_img_add" type="file" name="fupload">
<br></label>
</div>
        <div class="modal-footer-blue">
         <input type="submit"   name="submit" class="btn btn-primary" value="Сохранить">
        </div>
    </div></form>
  </div>
</div>   
 <div>
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