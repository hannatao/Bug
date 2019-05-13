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
	$.ajax({
           url : "http://47.99.140.117:8080/Bug/api/graph/21",
           type : "GET",
           dataType : "json", 
           data:{'case_take_id':"1632-2927"},
           success: function(response, status, xhr){
           		var dataArray = new Array();
           		for(var p in response){
           			var temp = new Array();
           			temp.push(response[p]["thums"]);
           			temp.push(response[p]["score"]);
					dataArray.push(temp);
           		}
           		drawChart(dataArray);
           },
           error:function(response){
           		console.log("获取点赞和得分数据错误");
           }
     });
     
function drawChart(dataArray){
	option = {
	backgroundColor: new echarts.graphic.RadialGradient(0.3, 0.3, 0.8, [{
        offset: 0,
        color: '#f7f8fa'
    }, {
        offset: 1,
        color: '#cdd0d5'
    }]),
    title: {
        text: '点赞数与Bug得分',
        x: 'center',
        y: 0
    },
    xAxis: {
		name:"点赞数",
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
                                    fontSize:'20'
                                }
                            },
    },
    yAxis: {
    	name:"Bug得分",
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
                                    fontSize:'20'
                                }
                            },
        scale: true
    },
    series: [{
        name: 'valid_bugs',
        data: dataArray,
        type: 'scatter',
        symbolSize:15,
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
    }]
};
	if (option && typeof option === "object") {
	    myChart.setOption(option, true);
	}
}
       </script>
   </body>
</html>