<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="UTF-8" />
    <meta http-equiv="X-UA-Compatible" content="IE=edge" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Pennant HRMS</title>
    
    <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
 
   <style type="text/css">
   
   
   
   @import url("https://fonts.googleapis.com/css2?family=Poppins:wght@200;300;400;500;600;700&display=swap");
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
  font-family: "Poppins", sans-serif;
}
.sidebar {
  position: fixed;
  height: 100%;
  width: 260px;
  background: #11101d;
  padding: 15px;
  z-index: 99;
}
.logo {
  font-size: 25px;
  padding: 0 15px;
}
.sidebar a {
  color: #fff;
  text-decoration: none;
}
.menu-content {
  position: relative;
  height: 100%;
  width: 100%;
  margin-top: 40px;
  overflow-y: scroll;
}
.menu-content::-webkit-scrollbar {
  display: none;
}
.menu-items {
  height: 100%;
  width: 100%;
  list-style: none;
  transition: all 0.4s ease;
}
.submenu-active .menu-items {
  transform: translateX(-56%);
}
.menu-title {
  color: #fff;
  font-size: 14px;
  padding: 15px 20px;
}
.item a,
.submenu-item {
  padding: 16px;
  display: inline-block;
  width: 100%;
  border-radius: 12px;
}
.item i {
  font-size: 12px;
}
.item a:hover,
.submenu-item:hover,
.submenu .menu-title:hover {
  background: rgba(255, 255, 255, 0.1);
}
.submenu-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  color: #fff;
  cursor: pointer;
}
.submenu {
  position: absolute;
  height: 100%;
  width: 100%;
  top: 0;
  right: calc(-100% - 26px);
  height: calc(100% + 100vh);
  background: #11101d;
  display: none;
}
.show-submenu ~ .submenu {
  display: block;
}
.submenu .menu-title {
  border-radius: 12px;
  cursor: pointer;
}
.submenu .menu-title i {
  margin-right: 10px;
}
.navbar,
.main {
  left: 260px;
  width: calc(100% - 260px);
  transition: all 0.5s ease;
  z-index: 1000;
}
.sidebar.close ~ .navbar,
.sidebar.close ~ .main {
  left: 0;
  width: 100%;
}
.navbar {
  position: relative;
  color: #fff;
  padding: 15px 20px;
  font-size: 25px;
  background: #4070f4;
  cursor: pointer;
}
.navbar #sidebar-close {
  cursor: pointer;
}
.main {
  position: relative;
  
  align-items: center;
 
  height: 100vh;
  z-index: 100;
  background: #e7f2fd;
  max-width: 100%;
  max-height: 100%;
  overflow: auto;
}
.main h1 {
  color: #11101d;
  font-size: 40px;
  text-align: center;
}
   
<style>



.row {
  display: flex;
  flex-wrap: wrap;
  margin-bottom: 10px;
}

.graph-container-wrapper {
  flex-basis: 50%;
  padding: 10px;
  box-sizing: border-box;
  background-color: #e0e0e0;
  border-radius: 10px;
  box-shadow: 4px 4px 8px rgba(0, 0, 0, 0.2), -4px -4px 8px rgba(255, 255, 255, 0.5);
}

.graph-container {
  position: relative;
  background-color: #f5f5f5;
  border-radius: 10px;
  box-shadow: inset 4px 4px 8px rgba(0, 0, 0, 0.2), inset -4px -4px 8px rgba(255, 255, 255, 0.5);
  padding: 10px;
}

canvas {
  width: 100%;
  height: 200px;
}

.graph-space {
  flex-basis: 20px;
}

.col {
  flex-basis: 50%;
  padding: 10px;
  box-sizing: border-box;
}

.field {
  background-color: #e0e0e0;
  border-radius: 10px;
  box-shadow: 4px 4px 8px rgba(0, 0, 0, 0.2), -4px -4px 8px rgba(255, 255, 255, 0.5);
  padding: 10px;
}

.field-space {
  flex-basis: 20px;
}



</style>

   
   </style>
 
 
 
 <script src="https://code.jquery.com/jquery-3.7.0.min.js" integrity="sha256-2Pmvv0kuTBOenSvLm6bvfBSSHrUJ+3A7x6P5Ebd07/g=" crossorigin="anonymous"></script>

    <!-- Fontawesome CDN Link -->
    <script src="https://cdn.jsdelivr.net/npm/chart.js@3.9.1/dist/chart.min.js"></script>
    <link href="https://cdn.jsdelivr.net/npm/remixicon@2.5.0/fonts/remixicon.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.3/css/all.min.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.2.1/css/all.min.css" />
    
  </head>
  <body>
    <nav class="sidebar">
      <a href="#" class="logo">EMPLOYEE</a>

      <div class="menu-content">
        <ul class="menu-items">
          
			 <li class="item">
            <a onclick="loadStatistics()"><i class="ri-booklet-line"></i> Dashboard </a>
          </li>
          <li class="item">
            <a onclick="viewProfile()"><i class="ri-bar-chart-line"></i> Profile</a>
          </li>

          <li class="item">
            <div class="submenu-item">
              <span><i class="ri-handbag-line"></i> Master Data</span>
              <i class="fa-solid fa-chevron-right"></i>
            </div>

            <ul class="menu-items submenu">
              <div class="menu-title">
                <i class="fa-solid fa-chevron-left"></i>
                Back to Master Data
              </div>
              <li class="item">
                <a onclick="getHolidays()"><i class="ri-calendar-line"></i> Holidays</a>
              </li>
              <li class="item">
                <a onclick="getLeaves()"><i class="ri-file-list-line"></i> Leaves</a>
              </li>
            </ul>
          </li>
          <li class="item">
            <div class="submenu-item">
              <span><i class="ri-user-line"></i> ALMS</span>
              <i class="fa-solid fa-chevron-right"></i>
            </div>

            <ul class="menu-items submenu">
              <div class="menu-title">
                <i class="fa-solid fa-chevron-left"></i>
                Back to ALMS
              </div>
              <li class="item">
                <a onclick="applyLeaves()"><i class="ri-file-list-line"></i> Apply Leaves</a>
              </li>
              <li class="item">
                <a onclick="getPermissions()"><i class="ri-check-double-line"></i> Permissions</a>
              </li>
              <li class="item">
                <a onclick="viewAttendance()"><i class="ri-time-line"></i> Attendance</a>
              </li>             
            </ul>
          </li>

          <li class="item">
            <a onclick="getRefdoc()"><span class="icon"><i class="ri-booklet-line"></i></span> Ref Documents</a>
          </li>
          <li class="item">
            <a onclick="getPayslip()"><span class="icon"><i class="ri-file-line"></i></span> Payslips</a>
          </li>
           <li class="item">
            <a onclick="logout()"><span class="icon"><i class="ri-logout-box-r-line"></i></span> Logout</a>
          </li>
        </ul>
      </div>
    </nav>

    <nav class="navbar">
      <i class="fa-solid fa-bars" id="sidebar-close"></i>
    </nav>

    <main class="main">
      <h1>Welcome to Pennant Technologies....</h1>
    </main>

  
  

  
  
   <script>
 
 
 const sidebar = document.querySelector(".sidebar");
 const sidebarClose = document.querySelector("#sidebar-close");
 const menu = document.querySelector(".menu-content");
 const menuItems = document.querySelectorAll(".submenu-item");
 const subMenuTitles = document.querySelectorAll(".submenu .menu-title");

 sidebarClose.addEventListener("click", () => sidebar.classList.toggle("close"));

 menuItems.forEach((item, index) => {
   item.addEventListener("click", () => {
     menu.classList.add("submenu-active");
     item.classList.add("show-submenu");
     menuItems.forEach((item2, index2) => {
       if (index !== index2) {
         item2.classList.remove("show-submenu");
       }
     });
   });
 });

 subMenuTitles.forEach((title) => {
   title.addEventListener("click", () => {
     menu.classList.remove("submenu-active");
   });
 });
 
 
 function loadStatistics() {
	 var main = $('.main');
	 main.empty();

	 var row = $('<div class="row"></div>');

	 var graphContainer1 = $('<div class="graph-container-wrapper"><div class="graph-container"></div></div>');
	 var graphCanvas1 = $('<canvas id="punch"></canvas>');
	 graphContainer1.find('.graph-container').append(graphCanvas1);
	 var graphMessage1 = $('<div class="graph-message">Punch in/Punch out for yesterday</div>');
	 graphContainer1.append(graphMessage1);
	 row.append(graphContainer1);

	 // Add space between the graphs
	 row.append('<div class="graph-space"></div>');

	 var graphContainer2 = $('<div class="graph-container-wrapper"><div class="graph-container"></div></div>');
	 var graphCanvas2 = $('<canvas id="pie"></canvas>');
	 graphContainer2.find('.graph-container').append(graphCanvas2);
	 var graphMessage2 = $('<div class="graph-message">Leaves & Permissions</div>');
	 graphContainer2.append(graphMessage2);
	 row.append(graphContainer2);

	 main.append(row);

	 var dataFields = $('<div class="row right"></div>');
	 
	 
	 // field 1
	 
	 var field = $('<div class="col right"><div class="field" id="field1">0</div></div>');
var fieldMessage = $('<div class="field-message" id="msg1">No of leaves taken</div>');
	 field.append(fieldMessage);
	 dataFields.append(field);
	 

		 
		// field 3
		  field = $('<div class="col right"><div class="field" id="field3">8</div></div>');
		  fieldMessage = $('<div class="field-message" id="msg3">No of permissions taken</div>');
		  field.append(fieldMessage);
			 dataFields.append(field);
			 

				 field = $('<div class="col right"><div class="field" id="field5"></div></div>');
				  fieldMessage = $('<div class="field-message" id="msg5">Average Punch in</div>');
				  field.append(fieldMessage);
					 dataFields.append(field);
			   
				
					// field 6
					 field = $('<div class="col right"><div class="field" id="field6"></div></div>');
					  fieldMessage = $('<div class="field-message" id="msg6">Average punch out</div>');
					  field.append(fieldMessage);
						 dataFields.append(field);
	  
						 
					
						 
// 	 for (var i = 1; i <= 6; i++) {
		 
// 	   var field = $('<div class="col right"><div class="field">Field ' + i + '</div></div>');
// 	   var fieldMessage = $('<div class="field-message">Message for Field ' + i + '</div>');
// 	   field.append(fieldMessage);
// 	   dataFields.append(field);
// 	 }

	 // Add space between the fields
	 dataFields.append('<div class="field-space"></div>');

	 main.append(dataFields);

	 

	 $.ajax({
		 url:"getAvgPunchInAndOut",
		 type:"GET",
		 success:function(response){
			 var punchData = JSON.parse(response);
			 console.log(punchData[0]);
			 var punchhrs = Math.floor(punchData[0]/60);
			 var remainingMinutes = punchData[0] % 60; 
			 
			 $('#field5').text(punchhrs + " hrs " + remainingMinutes + " mins");
			 
			 punchhrs = Math.floor(punchData[1]/60);
			 remainingMinutes = punchData[1] % 60; 
			 
			$('#field6').text(punchhrs + " hrs " + remainingMinutes + " mins");
		 },
		 error:function(error){
			 console.log(error);
		 }
		 
	 });
	 
	 $.ajax({
		 url:"permissionStatistics",
		 type:"GET",
		 success:function(response){
			 console.log(response);
			 
			 $('#field3').text(response);
			 
		 },
		 error:function(error){
			 console.log(error);
		 }
		 
	 });
	 
	 createPunchGraph();
	 
	 
	 var piegraphdata;
	 
	 $.ajax({
		 url:"getLeaveStatistics",
		 type:"GET",
		 success:function(response){
			 console.log(JSON.parse(response));
			 var piegraphdata = JSON.parse(response);
			 $('#field1').text(piegraphdata.takenTotalLeaves);
			 createPieGraph(piegraphdata.allowedTotalLeaves,piegraphdata.takenTotalLeaves,24, $('#field3').val());
		 },
		 error:function(error){
			 console.log(error);
		 }
		 
	 });
	 
	 
	  
	}

 
 function createPieGraph(totalLeaves,leavesUsed,totalPermissions,permissionsUsed){

	    var data = {
	      labels: ["Leaves Available (per Year)", "Leaves Used", "Permissions Available (per Year)", "Permissions Used"],
	      datasets: [
	        {
	          data: [totalLeaves - leavesUsed, leavesUsed, totalPermissions - permissionsUsed, permissionsUsed],
	          backgroundColor: ["#c2e0a3", "#ff7373", "#98c1d9", "#ffb347"],
	        }
	      ]
	    };

	    var options = {
	      responsive: true,
	      maintainAspectRatio: false
	    };

	    var ctx = document.getElementById("pie").getContext("2d");
	    var pieChart = new Chart(ctx, {
	      type: "pie",
	      data: data,
	      options: options
	    });
 }
 

 
 
 function createPunchGraph() {
	  $.ajax({
	    url: "punchData",
	    type: "GET",
	    success: function(response) {
	      console.log(response);
	      const punchData = JSON.parse(response);

	      const times = punchData.map(data => data.time);
	      const events = punchData.map(data =>
	        data.event === "Punch In"
	          ? { value: 1, label: "Punch In" }
	          : { value: -1, label: "Punch Out" }
	      );

	      const ctx = document.getElementById("punch").getContext("2d");
	      new Chart(ctx, {
	        type: "bar",
	        data: {
	          labels: times,
	          datasets: [
	            {
	              label: "Punch In/Punch Out",
	              data: events.map(event => event.value),
	              backgroundColor: "rgba(54, 162, 235, 0.5)",
	              borderColor: "rgba(54, 162, 235, 1)",
	              borderWidth: 1
	            }
	          ]
	        },
	        options: {
	          responsive: true,
	          maintainAspectRatio: false,
	          scales: {
	            x: {
	              grid: {
	                display: false
	              }
	            },
	            y: {
	              ticks: {
	                stepSize: 1
	              }
	            }
	          },
	          plugins: {
	            tooltip: {
	              callbacks: {
	                label: function(context) {
	                  const event = events[context.dataIndex];
	                  return event.label;
	                }
	              }
	            }
	          }
	        }
	      });
	    },
	    error: function(error) {
	      console.log(error);
	    }
	  });
	}



 function getHolidays() {
	 console.log("hello");
	 $(".main").empty();
    	  $.ajax({
    	    type: "POST",
    	    url: "holidays",
    	    data: {},
    	    success: function(response) {
    	      var containerDiv = $(".main");
    	      containerDiv.html(response);
    	     
    	    },
    	    error: function() {
    	      alert("Error occurred. Please try again later.");
    	    }
    	  });
    	}

    
    
    function getyourtHolidays() {
  	  $.ajax({
  	    type: "POST",
  	    url: "yourholidays",
  	    data: {},
  	    success: function(response) {
  	      var containerDiv = $(".main");
  	      containerDiv.html(response);
  	     
  	    },
  	    error: function() {
  	      alert("Error occurred. Please try again later.");
  	    }
  	  });
  	}

    
    function getPermissions() {
  	  $.ajax({
  	    type: "POST",
  	    url: "getpermissions",
  	    data: {},
  	    success: function(response) {
  	      var containerDiv = $(".main");
  	      containerDiv.html(response);
  	     
  	    },
  	    error: function() {
  	      alert("Error occurred. Please try again later.");
  	    }
  	  });
  	}

   
    function applyLeaves() {
  	  $.ajax({
  	    type: "GET",
  	    url: "leaveform",
  	    success: function(response) {
  	      var containerDiv = $(".main");
  	      containerDiv.html(response);
  	     
  	    },
  	    error: function() {
  	      alert("Error occurred. Please try again later.");
  	    }
  	  });
  	}
    
    function viewAttendance() {
    	  $.ajax({
    	    type: "GET",
    	    url: "employeeAttendance",
    	    success: function(response) {
    	      var containerDiv = $(".main");
    	      containerDiv.html(response);
    	     
    	    },
    	    error: function() {
    	      alert("Error occurred. Please try again later.");
    	    }
    	  });
    	}
    
    function getLeaves() {
  	  $.ajax({
  	    type: "GET",
  	    url: "geEmployeeLeaves",
  	    success: function(response) {
  	      var containerDiv = $(".main");
  	      containerDiv.html(response);
  	     
  	    },
  	    error: function() {
  	      alert("Error occurred. Please try again later.");
  	    }
  	  });
  	}
    
    function viewProfile() {
    	  $.ajax({
    	    type: "GET",
    	    url: "toprofile",
    	    success: function(response) {
    	      var containerDiv = $(".main");
    	      containerDiv.html(response);
    	     
    	    },
    	    error: function() {
    	      alert("Error occurred. Please try again later.");
    	    }
    	  });
    	}
    
    function getPayslip() {
  	  $.ajax({
  	    type: "GET",
  	    url: "getemppayslip",
  	    success: function(response) {
  	      var containerDiv = $(".main");
  	      containerDiv.html(response);
  	     
  	    },
  	    error: function() {
  	      alert("Error occurred. Please try again later.");
  	    }
  	  });
  	}
    
    
    function getRefdoc() {
  	  $.ajax({
  	    type: "GET",
  	    url: "emprefDocuments",
  	    success: function(response) {
  	      var containerDiv = $(".main");
  	      containerDiv.html(response);
  	     
  	    },
  	    error: function() {
  	      alert("Error occurred. Please try again later.");
  	    }
  	  });
  	}
    
    
    function logout(){
    	$.ajax({
    		url:"logout",
    		type:"GET",
    		success:function(response){
    			window.location.href='./';
    		},
    		error:function(error){
    			console.log(error);
    		}
    		
    	});
    }
    
    
    
 </script>
 
  
  </body>
</html>

