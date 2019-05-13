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
   <body style="height:1000px;width:1000px; margin-left: 200px">
       <div id="container" style="height: 100%;width:100%"></div>
       <script type="text/javascript">
var dom = document.getElementById("container");
var myChart = echarts.init(dom);
var app = {};
option = null;
var nodes = new Array();
var links = new Array();
var count = 1;
var startTime = (new Date("2018-11-16 08:10:00")).getTime();
//一秒一刷新
/* window.setInterval(function(){ 
	if(count<=24){
		count++;
		var endTime = new Date(startTime+600000*count).format("yyyy-MM-dd hh:mm:ss");
		console.log(endTime);
	    httpGetData(endTime);	
	}
}, 1000); */
httpGetData("2018-11-16 12:00:00");
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
	         		nodesTemp.forEach(function(node){
	         			var temp = {};
	         			temp["id"] = node["name"];
	         			temp["name"] = node["value"];
	         			temp["draggable"] = true;
	         			temp["symbolSize"] = node["value"]*5;
	         			temp["category"] = 0;
	         			nodes.push(temp);
	         		});
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
	         		drawChart();
	         },
	         error:function(response){
	         		console.log("获取点赞和得分数据错误");
	         }
	});
}
     
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
	        text: "测试人员Fork关系图",
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
	        type: 'graph',
	        layout: 'force',
	
	        force: {
	            repulsion: 300,
	            edgeLength: 120
	        },
	        data: nodes,
	        links:links,
	        edgeSymbol: ['arrow'],
	        focusNodeAdjacency: true,
	        categories : [ {name: '测试人员'}],
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
	    console.log(option);
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