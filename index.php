<?php

session_start();


?>
<!DOCTYPE html>
<head>
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>AutomifyChat</title>
<style>
html,body{
	margin: 0;
	padding: 0;
}
#main{
	padding: 30px;
	background-image: url(images/2.jpg);
	background-attachment: fixed;
    background-repeat: round;
}
#info{
	 box-shadow: 0 4px 8px 0 rgba(0, 0, 0, 1), 0 6px 20px 0 rgba(0, 0, 0, 1);
	padding: 10px;
	max-height: 100%;
	width: 600px;
	border-radius: 20px;
	font-family: verdana;
	font-weight: 0.8em;
	color:white;
	display: block;
	height: 1000px;
	 background-color: rgb(0,0,0); /* Fallback color */
background-color: rgba(0,0,0, 0.4); /* Black w/opacity/see-through */
 

}
input[type=text],input[type=password],input[type=email]{
	width: 500px;
	padding:10px;
	color: white;
	font-family: verdana;
	font-weight: 0.8em;
	 background-color: rgb(0,0,0); /* Fallback color */
background-color: rgba(0,0,0, 0.4); /* Black w/opacity/see-through */
    display:inline;
    box-sizing: border-box;
    border:transparent;
    box-shadow: 3px 3px 3px grey;
}
input[type=text]:focus,input[type=password]:focus,input[type=email]:focus{
border:4px solid lightblue;
box-shadow: 3px 3px 3px grey;
	}
	#bt{
		background:transparent;
		padding:8px;
		width: 300px; 
		border-radius: 5px;
		font-family: verdana;
		font-weight: 0.8em;
		color:white;
		cursor: pointer;
	}
footer{
	position:relative;
	top:80px;
	font-weight: 0.7em;
	font-family: bookman;
    font-style: italic;
    font-size: 20px;
}
#reg{
	overflow:<?php foreach ($username as $key => $password): ?>
		
	<?php endforeach ?>;
	
	text-align: left;
	overflow:auto;
}
#de{
	
	position: fixed;
	bottom: 0px;
	right:0px;

}
#te,a{
	text-decoration: none;
	box-shadow: 3px 3px 3px grey;
    border-radius: 10px;
	color: white;
	font-weight: 0.6em;
	font-family: bookman;
	font-size: 16px;


padding: 20px;
margin:  0px;
}

</style>
</head>
<body>
	<div id="de">
		<h2 id="te"><a href="about.php">.....</a></h2>
	</div>
	<div id="main">
		<center>
    <div id="info">
    	<fieldset>
    <legend>Login Here</legend>
    <form action="login.php" method="post">
    
    <input type="text" name="username" placeholder="Username Here.."required maxlength="17" minlength="8"><br><br>
    
    <input type="password" name="password" placeholder="Password Here.."required maxlength="17" minlength="8"><br><br>
    <button type="submit" style="background-color:transparent;color:white;width:260px;padding: 13px;cursor: pointer;"><b>Login AutomifyChat</b></button>
</fieldset>
    

</form>
<form action="signup.php" method="post">
	
	<br>
	<fieldset>
	<legend>Create Account</legend>
	
    <input type="text" name="username" placeholder="Username Here.."required maxlength="17" minlength="8"><br><br>
    
    <input type="email" name="email" placeholder="E-mail Here.."required><br><br>
    
    <input type="password" name="password" placeholder="Password Here.."required maxlength="17" minlength="8"><br><br>
     <button id="bt" type="submit"><b>Sign-Up AutomifyChat</b></button>
</form>
</fieldset>
<br>
<fieldset id="reg">
	<legend>Registered Programmers</legend>
<?php
$conn=mysqli_connect("localhost","root","","s1database");
if(!$conn){

	die("connection failed".mysqli_connect_error());

	

}
$query="SELECT * FROM signup";


if($result=mysqli_query($conn, $query)){

while($row=mysqli_fetch_row($result))
{
echo $row['0'].'.'.$row['1'].'<div id="zero"></div>'.'</br>';

}
mysqli_free_result($result);
}

mysqli_close($conn);
?>


</fieldset>
 </div>
</center>
</div>
</body>
</html>