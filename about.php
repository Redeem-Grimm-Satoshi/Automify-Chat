<!DOCTYPE html>
<html>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<head>
	<title>About Programmer</title>
	<style>
	html,body{

  height: 100%;
  margin: 0;
  font-family: Arial, Helvetica, sans-serif;
	}
.bg{
	/* The image used */
  background-image: url("images/2.jpg");
  
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
</style>
</head>
<body>
<div class="bg"></div>
<div class="text">
<h1 style="font-size: 50px">I am Redeem Grimm</h1>
<p>I am  a Java Programmer</p>

</body>
</html>