<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%> 
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<script src="/Bug/js/jquery-3.2.0.min.js"></script>
       <script type="text/javascript" src="http://echarts.baidu.com/gallery/vendors/echarts/echarts.min.js"></script>
       <script type="text/javascript" src="http://echarts.baidu.com/gallery/vendors/echarts-gl/echarts-gl.min.js"></script>
       <script type="text/javascript" src="http://echarts.baidu.com/gallery/vendors/echarts-stat/ecStat.min.js"></script>
       <script type="text/javascript" src="http://echarts.baidu.com/gallery/vendors/echarts/extension/dataTool.min.js"></script>
       <script type="text/javascript" src="http://echarts.baidu.com/gallery/vendors/echarts/map/js/china.js"></script>
       <script type="text/javascript" src="http://echarts.baidu.com/gallery/vendors/echarts/map/js/world.js"></script>
       <script type="text/javascript" src="https://api.map.baidu.com/api?v=2.0&ak=ZUONbpqGBsYGXNIYHicvbAbM"></script>
       <script type="text/javascript" src="http://echarts.baidu.com/gallery/vendors/echarts/extension/bmap.min.js"></script>
       <script type="text/javascript" src="http://echarts.baidu.com/gallery/vendors/simplex.js"></script>
	<title>众包协作概况</title>
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
var nodes = new Array();
var links = new Array();

$.ajax({
         url : "http://47.99.140.117:8080/Bug/api/graph/13",
         type : "GET",
         dataType : "json", 
         data:{'case_take_id':"1489-2613"},
         success: function(response, status, xhr){
         		var nodesTemp = response["nodes"];
         		var linksTemp = response["links"];
         		nodesTemp.forEach(function(node){
         			var temp = {};
         			temp["id"] = node["name"];
         			temp["name"] = node["value"];
         			temp["symbolSize"] = node["value"]*2;
         			temp["value"] = node["value"]; 
         			temp["draggable"] = true;
         			//temp["category"] = "测试人员";
         			nodes.push(temp);
         		});
         		linksTemp.forEach(function(link){
         			var temp = {};
         			temp["source"] = link["source"];
         			temp["target"] = link["target"];
         			temp["lineStyle"] = {normal:{
         				color:'red',
         				width: link["value"],
         			}}
					links.push(temp);
         		})
         		drawChart();
         },
         error:function(response){
         		console.log("获取点赞和得分数据错误");
         }
});
     
function drawChart(){
	option = {
	    backgroundColor: new echarts.graphic.RadialGradient(0.3, 0.3, 0.8, [{
	        offset: 0,
	        color: '#f7f8fa'
	    }, {
	        offset: 1,
	        color: '#cdd0d5'
	    }]),
	    title: {
	        text: "测试人员点赞关系图",
	        top: "top",
	        left: "center"
	    },
	    color:"black",
	    tooltip: {},
	      legend: {
	        orient: 'vertical', // 'vertical'
	        x: 'left',  
	        data:["测试人员"]
	      },
	    toolbox: {
	        show: true,
	        feature: {
	            dataView: {
	                show: true,
	                readOnly: true
	            },
	            restore: {
	                show: true
	            },
	            saveAsImage: {
	                show: true
	            }
	        }
	    },
	    animationDuration: 1000,
	    animationEasingUpdate: 'quinticInOut',
	    series: [{
	        name: '招标倾向性分析',
	        type: 'graph',
	        layout: 'force',
	
	        force: {
	            repulsion: 500,
	            edgeLength: 120
	        },
	        data: nodes,
	        links:links,
	        edgeSymbol: ['arrow'],
	        focusNodeAdjacency: true,
	        roam: true,
	        label: {
	            normal: {
	                show: true,
	                position: 'top',
	            }
	        },
	        lineStyle: {
	            normal: {
	                color: 'source',
	                curveness: 0,
	                type: "solid"
	            }
	        }
	    }]
	};
	if (option && typeof option === "object") {
	    myChart.setOption(option, true);
	}
}
       </script>
   </body>
</html>