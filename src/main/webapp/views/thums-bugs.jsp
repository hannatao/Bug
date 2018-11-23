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
	$.ajax({
           url : "http://47.99.140.117:8080/Bug/api/graph/21",
           type : "GET",
           dataType : "json", 
           data:{'case_take_id':"1489-2613"},
           success: function(response, status, xhr){
           		var dataArray = new Array();
           		for(var p in response){
           			var temp = new Array();
           			temp.push(response[p]["thums"]);
           			temp.push(response[p]["score"]);
					dataArray.push(temp);
           		}
           		console.log(dataArray);
				option = {
					title: [{
				        text: 'Bug得分随点赞数变化散点图',
				        x: '50%',
				        textAlign: 'center',
				        subtextStyle:{
				        	color:'#000000',
				        	fontSize:25
				        },
				        textStyle:{
				        	verticalAlign:'middle',
				        	color:"#000000",
				        	fontSize:25
				        }      
				    }],
				    xAxis: {
				    	name:"点赞数",
				    	nameTextStyle:{
				    		fontSize:20,
				    	},
                        // x轴的字体样式
                        axisLabel: {        
                                show: true,
                                textStyle: {
                                    color: '#000000',
                                    fontSize:'15'
                                }
                            },
                        // 控制网格线是否显示
                        splitLine: {
                                show: false, 
                                //  改变轴线颜色
                                lineStyle: {
                                    // 使用深浅的间隔色
                                    color: ['red']
                                }                            
                        },
                        // x轴的颜色和宽度
                        axisLine:{
                            lineStyle:{
                                color:'#000000',
                                  width:3,   //这里是坐标轴的宽度,可以去掉
                            }
                        }
				    },
				    yAxis: {
				    	name:"Bug得分",
				    	nameTextStyle:{
				    		fontSize:20,
				    	},
				    	    axisLabel: {        
                                show: true,
                                textStyle: {
                                    color: '#000000',
                                    fontSize:'20'
                                }
                            },
                                                    // x轴的颜色和宽度
                        axisLine:{
                            lineStyle:{
                                color:'#000000',
                                  width:3,   //这里是坐标轴的宽度,可以去掉
                            }
                        }
				    },
				    series: [{
				        symbolSize: 15,
				        data: dataArray,
				        type: 'scatter'
				    }]
				};
				;
				if (option && typeof option === "object") {
				    myChart.setOption(option, true);
				}
           },
           error:function(response){
           		console.log("获取点赞和得分数据错误");
           }
     });
       </script>
   </body>
</html>