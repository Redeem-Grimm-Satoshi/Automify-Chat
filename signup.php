<?php
include 'dbh.php';
$username = $email = $password ="";
$username=$_POST['username'];
$email=$_POST['email'];
$password=$_POST['password'];

$sql="INSERT INTO signup(username,email,password)
values('$username','$email','$password')";
$result=$conn->query($sql);

header("Location:index.php");











?>