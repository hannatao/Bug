<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%> 
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	  <title>众包协作概况</title>
	  <script src="/Bug/js/jquery-3.2.0.min.js"></script>
       <script type="text/javascript" src="http://echarts.baidu.com/gallery/vendors/echarts/echarts.min.js"></script>
       <script type="text/javascript" src="http://echarts.baidu.com/gallery/vendors/echarts-gl/echarts-gl.min.js"></script>
       <script type="text/javascript" src="http://echarts.baidu.com/gallery/vendors/echarts-stat/ecStat.min.js"></script>
       <script type="text/javascript" src="http://echarts.baidu.com/gallery/vendors/echarts/extension/dataTool.min.js"></script>
       <script type="text/javascript" src="http://echarts.baidu.com/gallery/vendors/echarts/map/js/china.js"></script>
       <script type="text/javascript" src="http://echarts.baidu.com/gallery/vendors/echarts/map/js/world.js"></script>
       <script type="text/javascript" src="http://echarts.baidu.com/gallery/vendors/echarts/extension/bmap.min.js"></script>
       <script type="text/javascript" src="http://echarts.baidu.com/gallery/vendors/simplex.js"></script>
</head>
<body>
	<%@include file="navi.html"%>
	<div class="row" style="height:650px;">
		<div class="col-md-3 col-sm-3 col-xs-3"></div>
		<div class="col-md-6 col-sm-6 col-xs-6" id="container" style="height:100%;"></div>
		<div class="col-md-3 col-sm-3 col-xs-3"></div>
	</div>
	<script type="text/javascript">
	var dom = document.getElementById("container");
	var myChart = echarts.init(dom);
	var app = {};
	option = null;
	$.get('/Bug/data/score.json', function (data) {
		console.log(data);
		var arr1 = new Array();
		var arr2 = new Array();
		var arr3 = new Array();
		var arr4 = new Array();
		data.forEach(function(people){
			var temp1 = [people["case"],people["report"]]; 
			var temp2 = [people["check"],people["report"]];
			var temp3 = [people["case"],people["check"]];  
			var temp4 = [people["report"],people["sum"]];
			arr1.push(temp1);
			arr2.push(temp2);
			arr3.push(temp3);
			arr4.push(temp4);
		})
		var dataAll = [arr1,arr2,arr3,arr4];
		drawGraph(dataAll);
	});
	
	function drawGraph(dataAll){
	option = {
		backgroundColor: new echarts.graphic.RadialGradient(0.3, 0.3, 0.8, [{
	        offset: 0,
	        color: '#f7f8fa'
	    }, {
	        offset: 1,
	        color: '#cdd0d5'
	    }]),
	    title: {
	        text: '得分关系散点图',
	        x: 'center',
	        y: 0
	    },
	    grid: [
	        {x: '7%', y: '10%', width: '38%', height: '38%'},
	        {x2: '7%', y: '10%', width: '38%', height: '38%'},
	        {x: '7%', y2: '7%', width: '38%', height: '38%'},
	        /* {x2: '7%', y2: '7%', width: '38%', height: '38%'} */
	    ],
	    tooltip: {
	        formatter: 'Group {a}: ({c})'
	    },
	    xAxis: [
	        {gridIndex: 0, min: 0, max: 20,
		    name:"用例分",
	    	nameTextStyle:{
	    		fontSize:12,
	    	}},
	        {gridIndex: 1, min: 0, max: 20,name:"审查分",
	    	nameTextStyle:{
	    		fontSize:12,
	    	}},
	        {gridIndex: 2, min: 0, max: 20,name:"用例分",
	    	nameTextStyle:{
	    		fontSize:12,
	    	}},
/* 	        {gridIndex: 3, min: 0, max: 60,name:"Bug分",
	    	nameTextStyle:{
	    		fontSize:12,
	    	}} */
	    ],
	    yAxis: [
	        {gridIndex: 0, min: 0, max: 60,  name:"Bug分",
	    	nameTextStyle:{
	    		fontSize:12,
	    	}},
	        {gridIndex: 1, min: 0, max: 60,  name:"Bug分",
	    	nameTextStyle:{
	    		fontSize:12,
	    	}},
	        {gridIndex: 2, min: 0, max: 20,  name:"审查分",
	    	nameTextStyle:{
	    		fontSize:12,
	    	}},
/* 	        {gridIndex: 3, min: 0, max: 100,  name:"总分",
	    	nameTextStyle:{
	    		fontSize:12,
	    	}} */
	    ],
	    series: [
	        {
	            name: 'I',
	            type: 'scatter',
	            xAxisIndex: 0,
	            yAxisIndex: 0,
	            data: dataAll[0],
	        },
	        {
	            name: 'II',
	            type: 'scatter',
	            xAxisIndex: 1,
	            yAxisIndex: 1,
	            data: dataAll[1],
	        },
	        {
	            name: 'III',
	            type: 'scatter',
	            xAxisIndex: 2,
	            yAxisIndex: 2,
	            data: dataAll[2],
	        },
/* 	        {
	            name: 'IV',
	            type: 'scatter',
	            xAxisIndex: 3,
	            yAxisIndex: 3,
	            data: dataAll[3],
	        } */
	    ]
	};
	if (option && typeof option === "object") {
	    myChart.setOption(option, true);
	}	
	}
       </script>
   </body>
       </script>
   </body>
</html>