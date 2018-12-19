<?php
session_start();
?>
<!DOCTYPE html>
<html>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<head>
	<title>Login Error</title>
	<style>
	html,body{

  height: 100%;
  margin: 0;
  font-family: Arial, Helvetica, sans-serif;
	}
.bg{
	/* The image used */
  background-image: url("images/5.png");
  
  /* Add the blur effect */
  filter: blur(8px);
  -webkit-filter: blur(8px);
  
  /* Full height */
  height: 100%; 
  
  /* Center and scale the image nicely */
  background-position: center;
  background-repeat: no-repeat;
  background-size: cover;
}
.text{
	 background-color: rgb(0,0,0); /* Fallback color */
  background-color: rgba(0,0,0, 0.6); /* Black w/opacity/see-through */
  color: white;
  font-weight: bold;
  border: 3px solid #f1f1f1;
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  z-index: 2;
  width: 80%;
  padding: 20px;
  text-align: center;
}
#myProgress {
  position: relative;
  width: 100%;
  height: 30px;
    background-color: rgb(0,0,0); /* Fallback color */
background-color: rgba(0,0,0, 0.5); /* Black w/opacity/see-through */
 
}

#myBar {
  position: absolute;
  width: 10%;
  height: 100%;
  background-color:skyblue;
}

#label {
  text-align: center;
  line-height: 30px;
  color: white;
}
.loader {
    border: 16px solid rgba(0,0,0, 0.5); /* Light grey */
    border-top: 16px  rgb(0,0,0); /* Blue */
    border-radius: 100%;
    width: 120px;
    height: 120px;
    animation: spin 1s linear infinite;
     background-color: rgb(0,0,0); /* Fallback color */
background-color: rgba(0,0,0, 0.5); /* Black w/opacity/see-through */
 
}

@keyframes spin {
    0% { transform: rotate(0deg); }
    100% { transform: rotate(360deg); }
   
     100%   {background-color:rgb(0,0,0);}
    25%  {background-color: #ff0066;}
    50%  {background-color: #ff6600;}
    0% {background-color: green;}

}
.craz{
    padding:5px;
	margin: 2px;
	background-color: lightgreen;
	display: inline-block;
	border-radius: 100%;
	animation: example 3s infinite;
    
    
    position: relative;
    -webkit-animation-name: example; /* Chrome, Safari, Opera */
    -webkit-animation-duration: 4s; /* Chrome, Safari, Opera */
    -webkit-animation-iteration-count: 4; /* Chrome, Safari, Opera */
    -webkit-animation-direction: reverse; /* Chrome, Safari, Opera */
    animation-name: example;
    animation-duration: 1s;
    animation-iteration-count: 3;
    animation-direction: all;    
}

/* Chrome, Safari, Opera */
@-webkit-keyframes example {
    0%   {background-color:red; left:0px; top:0px;}
    25%  {background-color:yellow; left:200px; top:0px;}
    50%  {background-color:blue; left:200px; top:200px;}
    75%  {background-color:green; left:0px; top:200px;}
    100% {background-color:red; left:0px; top:0px;}
}

/* Standard syntax */
@keyframes example {
    0%   {background-color:red; left:0px; top:0px;}
    25%  {background-color:yellow; left:10px; top:0px;}
    50%  {background-color:blue; left:10px; top:0px;}
    75%  {background-color:green; left:0px; top:0px;}
    100% {background-color:red; left:0px; top:0px;}
     
    
    

}
.craze{
	 padding:5px;
	margin: 2px;
	background-color: lightgreen;
	display: inline-block;
	border-radius: 100%;
		animation: example 3s infinite;
    
    
    position: relative;
    -webkit-animation-name: example; /* Chrome, Safari, Opera */
    -webkit-animation-duration: 4s; /* Chrome, Safari, Opera */
    -webkit-animation-iteration-count: 4; /* Chrome, Safari, Opera */
    -webkit-animation-direction: reverse; /* Chrome, Safari, Opera */
    animation-name: example;
    animation-duration: 1.5s;
    animation-iteration-count: 3;
    animation-direction:all;    
}

/* Chrome, Safari, Opera */
@-webkit-keyframes example {
    0%   {background-color:red; left:0px; top:0px;}
    25%  {background-color:yellow; left:200px; top:0px;}
    50%  {background-color:blue; left:200px; top:200px;}
    75%  {background-color:green; left:0px; top:200px;}
    100% {background-color:red; left:0px; top:0px;}
}

/* Standard syntax */
@keyframes example {
    0%   {background-color:red; left:0px; top:0px;}
    25%  {background-color:yellow; left:200px; top:0px;}
    50%  {background-color:blue; left:200px; top:200px;}
    75%  {background-color:green; left:0px; top:200px;}
    100% {background-color:red; left:0px; top:0px;}
}


.crazer{
	 padding:5px;
	margin: 2px;
	background-color: lightgreen;
	display: inline-block;
	border-radius: 100%;
		animation: example 3s infinite;
    
    
    position: relative;
    -webkit-animation-name: example; /* Chrome, Safari, Opera */
    -webkit-animation-duration: 4s; /* Chrome, Safari, Opera */
    -webkit-animation-iteration-count: 4; /* Chrome, Safari, Opera */
    -webkit-animation-direction: reverse; /* Chrome, Safari, Opera */
    animation-name: example;
    animation-duration: 2s;
    animation-iteration-count: 3;
    animation-direction:all;    
}

/* Chrome, Safari, Opera */
@-webkit-keyframes example {
    0%   {background-color:red; left:0px; top:0px;}
    25%  {background-color:yellow; left:200px; top:0px;}
    50%  {background-color:blue; left:200px; top:200px;}
    75%  {background-color:green; left:0px; top:200px;}
    100% {background-color:red; left:0px; top:0px;}
}

/* Standard syntax */
@keyframes example {
    0%   {background-color:red; left:0px; top:0px;}
    25%  {background-color:yellow; left:200px; top:0px;}
    50%  {background-color:blue; left:200px; top:200px;}
    75%  {background-color:green; left:0px; top:200px;}
    100% {background-color:red; left:0px; top:0px;}
}


.crazer1{
	 padding:5px;
	margin: 2px;
	background-color: lightgreen;
	display: inline-block;
	border-radius: 100%;
		animation: example 3s infinite;
    
    
    position: relative;
    -webkit-animation-name: example; /* Chrome, Safari, Opera */
    -webkit-animation-duration: 4s; /* Chrome, Safari, Opera */
    -webkit-animation-iteration-count: 4; /* Chrome, Safari, Opera */
    -webkit-animation-direction: reverse; /* Chrome, Safari, Opera */
    animation-name: example;
    animation-duration: 2.5s;
    animation-iteration-count: 3;
    animation-direction:all;    
}

/* Chrome, Safari, Opera */
@-webkit-keyframes example {
    0%   {background-color:red; left:0px; top:0px;}
    25%  {background-color:yellow; left:200px; top:0px;}
    50%  {background-color:blue; left:200px; top:200px;}
    75%  {background-color:green; left:0px; top:200px;}
    100% {background-color:red; left:0px; top:0px;}
}

/* Standard syntax */
@keyframes example {
    0%   {background-color:red; left:0px; top:0px;}
    25%  {background-color:yellow; left:200px; top:0px;}
    50%  {background-color:blue; left:200px; top:200px;}
    75%  {background-color:green; left:0px; top:200px;}
    100% {background-color:red; left:0px; top:0px;}
}

}

.crazer2{
	 padding:5px;
	margin: 2px;
	background-color: lightgreen;
	display: inline-block;
	border-radius: 100%;
		animation: example 3s infinite;
    
    
    position: relative;
    -webkit-animation-name: example; /* Chrome, Safari, Opera */
    -webkit-animation-duration: 4s; /* Chrome, Safari, Opera */
    -webkit-animation-iteration-count: 4; /* Chrome, Safari, Opera */
    -webkit-animation-direction: reverse; /* Chrome, Safari, Opera */
    animation-name: example;
    animation-duration: 3s;
    animation-iteration-count: 3;
    animation-direction:all;    
}

/* Chrome, Safari, Opera */
@-webkit-keyframes example {
    0%   {background-color:red; left:0px; top:0px;}
    25%  {background-color:yellow; left:200px; top:0px;}
    50%  {background-color:blue; left:200px; top:200px;}
    75%  {background-color:green; left:0px; top:200px;}
    100% {background-color:red; left:0px; top:0px;}
}

/* Standard syntax */
@keyframes example {
    0%   {background-color:red; left:0px; top:0px;}
    25%  {background-color:yellow; left:200px; top:0px;}
    50%  {background-color:blue; left:200px; top:200px;}
    75%  {background-color:green; left:0px; top:200px;}
    100% {background-color:red; left:0px; top:0px;}
}

}

.crazer3{
	 padding:5px;
	margin: 2px;
	background-color: lightgreen;
	display: inline-block;
	border-radius: 100%;
		animation: example 3s infinite;
    
    
    position: relative;
    -webkit-animation-name: example; /* Chrome, Safari, Opera */
    -webkit-animation-duration: 4s; /* Chrome, Safari, Opera */
    -webkit-animation-iteration-count: 4; /* Chrome, Safari, Opera */
    -webkit-animation-direction: reverse; /* Chrome, Safari, Opera */
    animation-name: example;
    animation-duration: 3.5s;
    animation-iteration-count: 3;
    animation-direction:all;    
}

/* Chrome, Safari, Opera */
@-webkit-keyframes example {
    0%   {background-color:red; left:0px; top:0px;}
    25%  {background-color:yellow; left:200px; top:0px;}
    50%  {background-color:blue; left:200px; top:200px;}
    75%  {background-color:green; left:0px; top:200px;}
    100% {background-color:red; left:0px; top:0px;}
}

/* Standard syntax */
@keyframes example {
    0%   {background-color:red; left:0px; top:0px;}
    25%  {background-color:yellow; left:200px; top:0px;}
    50%  {background-color:blue; left:200px; top:200px;}
    75%  {background-color:green; left:0px; top:200px;}
    100% {background-color:red; left:0px; top:0px;}
}

}
</style>
</head>
<body>
<div class="bg"></div>
<div class="text">
<h1 style="font-size: 30px">Oops! Communication Broken.</h1>
<p style="font-size: 25">Repairing Connection...</p>
<center>
 <div class="loader"></div>
</center>
<!-- Progress Bar
<div id="myProgress">
  <div id="myBar">
    <div id="label">20%</div>
  </div>
</div>

<br>
<button onclick="move()">Click Me</button> 

<script>
function move() {
  var elem = document.getElementById("myBar");   
  var width = 20;
  var id = setInterval(frame, 20);
  function frame() {
    if (width >= 100) {
      clearInterval(id);
    } else {
      width++; 
      elem.style.width = width + '%'; 
      document.getElementById("label").innerHTML = width * 1  + '%';
    }
  }
}
</script>
-->

</body>
</html>