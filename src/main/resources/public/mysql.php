<?php
/*Настройки подключения*/
$db_host  = "localhost"; //Имя хоста
$db_user  = "root";      //Пользователь БД
$db_pass  = "";          //Пароль для пользователя БД
$db_name  = "CRM";      //Имя базы данных
$connect_db = mysql_connect($db_host,$db_user,$db_pass) or die("Ошибка подключения к серверу:".mysql_error());

mysql_select_db("CRM",$connect_db)  or die("Ошибка подключения к  базе данных:".mysql_error());
?>