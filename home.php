<?php

session_start();


?>
<!Doctype html>
<html>
<head>
<link rel="stylesheet" href="innocss.css">
	
<title>AutomifyChat|--> Home</title>

</head>
<body>

<div class="wrapper">

<img class="round" src="images/8.jpg" width="70px" height="70px" />
<h1 id="time">About Redeem Grim</h1>
<form action="logout.php" method="post">
<input class="desk" type="submit" name="log" value="Logout">

</form>

</div>



	    
<div class="sidenav">

	<!-- list items -->
<ul>


<li><a href="#">Online Members<br><hr>Online<br><img class="round" src="images/1.jpg" width="70px" height="70px" />
</a></li>
<li><a href="#">Online<br><img class="round" src="images/2.jpg" width="70px" height="70px" />
</a></li>
<li><a href="#">Offline<br><img class="round" src="images/3.jpg" width="70px" height="70px" />
</a></li>
<li><a href="#">Online<br><img class="round" src="images/4.jpg" width="70px" height="70px" />
</a></li>
<li><a href="#">Offline<br><img class="round" src="images/5.png" width="70px" height="70px" />
</a></li>





</ul>
<center>
<div class="display">
	<?php
$conn=mysqli_connect("localhost","root","","s1database");
if(!$conn){

	die("connection failed".mysqli_connect_error());

	

}
$query="SELECT * FROM posts";


if($result=mysqli_query($conn, $query)){

while($row=mysqli_fetch_row($result))
{
echo '<div class="rim">'.'<h3 class="spec">'.'<span>'.$row['3'].'</span>'.'</h3>'.'<br/>'.$row['1'].'</div>'.'</br>';

}
mysqli_free_result($result);
}

mysqli_close($conn);
?>
</div>
</center>
<!--message div -->
<div class="msg">
<center>
<form action="send.php" method="post">
<textarea cols="110" name="msg" class="span" placeholder="Type Message.." maxlength="200" minlength="5"required>	
</textarea><input type="submit" class="button" value="Send"style="vertical-align:middle">
</form>
</center>
</div>

 </div>
  
	

<!--<footer><h2>Chat Kit</h2></footer>-->

<!-- Time Function Here -->
<script>
var tick=setInterval(timer,1000);
function timer() {
var local=new Date();
document.getElementById('time').innerHTML=local.toLocaleTimeString();
/*
function playSound(){
	var talk=document.getElementById("isound");
	talk.autoplay=true;
	talk.load();

}
*/
}
</script>
</body>
</html>