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
       <script type="text/javascript" src="https://api.map.baidu.com/api?v=2.0&ak=ZUONbpqGBsYGXNIYHicvbAbM"></script>
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
app.title = '气泡图';
	$.ajax({
           url : "http://47.99.140.117:8080/Bug/api/graph/12",
           type : "GET",
           dataType : "json", 
           data:{'case_take_id':"1489-2613"},
           success: function(response, status, xhr){
          /*  {"valid_bugs":9,"report_id":"10010000034646","total_bugs":12,"thumsUp":31} */
          	var dataArray1 = new Array();
          	var dataArray2 = new Array();
           	for(var i=0;i<response.length;i++){
           		var temp1 = new Array();
           		var temp2 = new Array();
           		temp1.push(response[i]["thumsUp"]);
           		temp1.push(response[i]["valid_bugs"]);
           		temp1.push("valid_bugs");
           		temp2.push(response[i]["thumsUp"]);
           		temp2.push(response[i]["total_bugs"]);
           		temp2.push("total_bugs");
           		dataArray1.push(temp1);
           		dataArray2.push(temp2);
           	}
            console.log(dataArray1);
           	drawChart(dataArray1,dataArray2);
           },
           error:function(response){
           		console.log("获取点赞和得分数据错误");
           }
     });
function drawChart(dataArray1,dataArray2){
/* var data = [
    [[28604,77,17096869,'Australia',1990],[31163,77.4,27662440,'Canada',1990],[1516,68,1154605773,'China',1990],[13670,74.7,10582082,'Cuba',1990],[28599,75,4986705,'Finland',1990],[29476,77.1,56943299,'France',1990],[31476,75.4,78958237,'Germany',1990],[28666,78.1,254830,'Iceland',1990],[1777,57.7,870601776,'India',1990],[29550,79.1,122249285,'Japan',1990],[2076,67.9,20194354,'North Korea',1990],[12087,72,42972254,'South Korea',1990],[24021,75.4,3397534,'New Zealand',1990],[43296,76.8,4240375,'Norway',1990],[10088,70.8,38195258,'Poland',1990],[19349,69.6,147568552,'Russia',1990],[10670,67.3,53994605,'Turkey',1990],[26424,75.7,57110117,'United Kingdom',1990],[37062,75.4,252847810,'United States',1990]],
    [[44056,81.8,23968973,'Australia',2015],[43294,81.7,35939927,'Canada',2015],[13334,76.9,1376048943,'China',2015],[21291,78.5,11389562,'Cuba',2015],[38923,80.8,5503457,'Finland',2015],[37599,81.9,64395345,'France',2015],[44053,81.1,80688545,'Germany',2015],[42182,82.8,329425,'Iceland',2015],[5903,66.8,1311050527,'India',2015],[36162,83.5,126573481,'Japan',2015],[1390,71.4,25155317,'North Korea',2015],[34644,80.7,50293439,'South Korea',2015],[34186,80.6,4528526,'New Zealand',2015],[64304,81.6,5210967,'Norway',2015],[24787,77.3,38611794,'Poland',2015],[23038,73.13,143456918,'Russia',2015],[19360,76.5,78665830,'Turkey',2015],[38225,81.4,64715810,'United Kingdom',2015],[53354,79.1,321773631,'United States',2015]]
]; */
	option = {
    backgroundColor: new echarts.graphic.RadialGradient(0.3, 0.3, 0.8, [{
        offset: 0,
        color: '#f7f8fa'
    }, {
        offset: 1,
        color: '#cdd0d5'
    }]),
   	title: {
        text: '被点赞数与Bug数目',
        x: 'center',
        y: 0
    },
    legend: {
        right: 10,
        data: ['valid_bugs', 'total_bugs']
    },
    xAxis: {
		name:"被点赞数",
    	nameTextStyle:{
    		fontSize:15,
    	},
        splitLine: {
            lineStyle: {
                type: 'dashed'
            }
        },
                                // x轴的字体样式
                        axisLabel: {        
                                show: true,
                                textStyle: {
                                    color: '#000000',
                                    fontSize:'15'
                                }
                            },
    },
    yAxis: {
    	name:"Bug数",
    	nameTextStyle:{
    		fontSize:18,
    	},
        splitLine: {
            lineStyle: {
                type: 'dashed'
            }
        },
                                        // x轴的字体样式
                        axisLabel: {        
                                show: true,
                                textStyle: {
                                    color: '#000000',
                                    fontSize:'18'
                                }
                            },
        scale: true
    },
    series: [{
        name: 'valid_bugs',
        data: dataArray1,
        type: 'scatter',
/*         symbolSize: function (data) {
            return Math.sqrt(data[2]) / 5e2;
        }, */
        symbolSize:20,
        label: {
            emphasis: {
                show: true,
                formatter: function (param) {
                    return param.data[1];
                },
                position: 'top'
            }
        },
        itemStyle: {
            normal: {
                shadowBlur: 10,
                shadowColor: 'rgba(120, 36, 50, 0.5)',
                shadowOffsetY: 5,
                color: new echarts.graphic.RadialGradient(0.4, 0.3, 1, [{
                    offset: 0,
                    color: 'rgb(251, 118, 123)'
                }, {
                    offset: 1,
                    color: 'rgb(204, 46, 72)'
                }])
            }
        }
    }, {
        name: 'total_bugs',
        data: dataArray2,
        type: 'scatter',
/*         symbolSize: function (data) {
            return Math.sqrt(data[2]) / 5e2;
        }, */
        symbolSize:20,
        label: {
            emphasis: {
                show: true,
                formatter: function (param) {
                    return param.data[1];
                },
                position: 'top'
            }
        },
        itemStyle: {
            normal: {
                shadowBlur: 10,
                shadowColor: 'rgba(25, 100, 150, 0.5)',
                shadowOffsetY: 5,
                color: new echarts.graphic.RadialGradient(0.4, 0.3, 1, [{
                    offset: 0,
                    color: 'rgb(129, 227, 238)'
                }, {
                    offset: 1,
                    color: 'rgb(25, 183, 207)'
                }])
            }
        }
    }]
};;
if (option && typeof option === "object") {
    myChart.setOption(option, true);
}
}
       </script>
   </body>
</html>