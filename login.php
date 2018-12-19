<?php

session_start();
include 'dbh.php';
$username=$_POST['username'];
$password=$_POST['password'];

$sql="SELECT * FROM signup WHERE username='$username' AND password='$password'";
$result=$conn->query($sql);
if(!$row=$result->fetch_assoc()){
	header("Location:error.php");
}else{

$_SESSION['name']=$_POST['username'];

	header("Location:home.php");

}





?>