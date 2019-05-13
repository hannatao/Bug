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
       <script type="text/javascript" src="http://echarts.baidu.com/gallery/vendors/echarts/extension/bmap.min.js"></script>
       <script type="text/javascript" src="http://echarts.baidu.com/gallery/vendors/simplex.js"></script>
	<title>众包协作概况</title>
</head>
<body>
	<%@include file="navi.html"%>
	<div class="row" style="height:800px;">
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
var count = 1;
var startTime = (new Date("2018-11-16 07:50:00")).getTime();
let position = [];
var addPoint = 0;
//一秒一刷新
window.setInterval(function(){ 
	if(count<=24){
		count++;
		var endTime = new Date(startTime+600000*count).format("yyyy-MM-dd hh:mm:ss");
	    httpGetData(endTime);	
	}
}, 1000);
//httpGetData("2018-11-16 10:00:00");
function httpGetData(endTime){
	$.ajax({
	         url : "http://47.99.140.117:8080/Bug/api/graph/14",
	         type : "GET",
	         dataType : "json", 
	         data:{'case_take_id':"1632-2927",'time':endTime},
	         success: function(response, status, xhr){
	         		nodes = [];
	         		links = [];
	         		var nodesTemp = response["nodes"];
	         		var linksTemp = response["links"];
	         		for(var i=0;i<nodesTemp.length;i++){
	         			var node = nodesTemp[i];
	         			var temp = {};
	         			let pos = [];
	         			let flag = false;
					    position.forEach(function(posTemp) {
							if(node["name"] == posTemp["name"]){
								flag = true;
								pos = [posTemp["x"],posTemp["y"]];
							}
						})
						if(!flag){
							pos = generatePos(node["name"]);
						}
	         			temp["id"] = node["name"];
	         			temp["name"] = "id:"+node["name"].substring(node["name"].length-3);
	         			temp["symbolSize"] = node["value"]*5;
	         			temp["category"] = 0;
	         			temp["x"] = Math.round(pos[0]);
	         			temp["y"] = Math.round(pos[1]);
	         			nodes.push(temp);
	         		}
	         		linksTemp.forEach(function(link){
	         			var temp = {};
	         			temp["source"] = link["source"];
	         			temp["target"] = link["target"];
	         			temp["lineStyle"] = {normal:{
	         				color:'red',
	         				//width:link["value"]*5
	         			}}
						links.push(temp);
	         		})
	         		drawChart(endTime);
	         },
	         error:function(response){
	         		console.log("获取点赞和得分数据错误");
	         }
	});
}

function generatePos(name){
	var result = []; 
	var nums = [6,12,24];
	var circle = [500,500];
	var r = 200;
	addPoint++;
	if(addPoint==1){
		position.push({name:name,x:circle[0],y:circle[1]});
		return circle;
	}
	var m = 1;
	var n = 1;
	var temp = addPoint-1;
	var hudu = 0;
	for(var i=0;i<nums.length;i++){
		var num = nums[i];
		if(temp<=num){
			n = temp;
			break;
		}
		n = temp-num;
		temp = n;
		m++;
	}
	var hudu =  2*Math.PI /360*(360/nums[m-1]*n);
	result[0] = circle[0] + Math.sin(hudu) * r*m; 
	result[1] = circle[1] - Math.cos(hudu) * r*m;
	position.push({name:name,x:result[0],y:result[1]});
	return result;
}
     
function drawChart(endTime){
	option = {
	    backgroundColor: new echarts.graphic.RadialGradient(0.3, 0.3, 0.8, [{
	        offset: 0,
	        color: '#f7f8fa'
	    }, {
	        offset: 1,
	        color: '#cdd0d5'
	    }]),
	    title: {
	        text: "测试人员Fork关系图",
	        subtext: endTime,
	        subtextStyle: {color: 'red', fontSize: 16},
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
	    //animationDuration: 1000,
	    //animationEasingUpdate: 'quinticInOut',
	    series: [{
	        type: 'graph',
	        layout: 'none',
			draggable:false,   //拖动，可拖放
/* 	        force: {
	            repulsion: 300,
	            edgeLength: 120,
	            edgeLength: [100,1000],
	        }, */
	        data: nodes,
	        links:links,
	        edgeSymbol: ['arrow'],
	        //focusNodeAdjacency: true,
	        categories : [ {name: '测试人员'}],
	        //roam: true,
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

Date.prototype.format = function(fmt) { 
     var o = { 
        "M+" : this.getMonth()+1,                 //月份 
        "d+" : this.getDate(),                    //日 
        "h+" : this.getHours(),                   //小时 
        "m+" : this.getMinutes(),                 //分 
        "s+" : this.getSeconds(),                 //秒 
        "q+" : Math.floor((this.getMonth()+3)/3), //季度 
        "S"  : this.getMilliseconds()             //毫秒 
    }; 
    if(/(y+)/.test(fmt)) {
            fmt=fmt.replace(RegExp.$1, (this.getFullYear()+"").substr(4 - RegExp.$1.length)); 
    }
     for(var k in o) {
        if(new RegExp("("+ k +")").test(fmt)){
             fmt = fmt.replace(RegExp.$1, (RegExp.$1.length==1) ? (o[k]) : (("00"+ o[k]).substr((""+ o[k]).length)));
         }
     }
    return fmt; 
}
       </script>
   </body>
</html>