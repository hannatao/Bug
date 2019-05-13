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
	         url : "http://47.99.140.117:8080/Bug/api/graph/31",
	         type : "GET",
	         dataType : "json", 
	         data:{'case_take_id':"1632-2927"},
	         success: function(response, status, xhr){
	         /* {"thums_by_rec":26,"valid_by_rec":11,"valid_by_self":3,"fork_by_rec":26,"fork_by_self":6,"thums_by_self":5} */
	         	sum = response["fork_by_rec"]+response["fork_by_self"];
	         	count1 = Math.round(response["fork_by_rec"] /sum*100);
	         	count2 = 100-count1;
	         	thums1 = Math.round(response["thums_by_rec"]/response["fork_by_rec"]*100);
	         	thums2 = Math.round(response["thums_by_self"]/response["fork_by_self"]*100);
	         	valid1 = Math.round(response["valid_by_rec"]/response["fork_by_rec"]*100);
	         	valid2 = Math.round(response["valid_by_self"]/response["fork_by_self"]*100);
				var data = { 
				    id: 'multipleThree',
				    title: '推荐Fork与查找Fork对比',
				    legend: ['推荐Fork', '查找Fork'],
				    barWidth:30,
				    yAxis: ['有效Bug占比','被点赞Bug占比','个数占比'],
				    xAxis: [
				        [ -valid1,-thums1,-count1],
				        [ valid2, thums2,count2]
				    ],
				    color: ['#5e94dd', '#49b5bd'],
				} 
	         	drawChart(data);
	         },
	         error:function(response){
	         		console.log("获取点赞和得分数据错误");
	         }
	});
	
	function drawChart(data){
		option = {
			backgroundColor: new echarts.graphic.RadialGradient(0.3, 0.3, 0.8, [{
		        offset: 0,
		        color: '#f7f8fa'
		    }, {
		        offset: 1,
		        color: '#cdd0d5'
		    }]),
			title: {
		        text: data.title,
		        top: "top",
		        left: "center"
		    },
		    tooltip: {
		        trigger: 'axis',
		        axisPointer: { // 坐标轴指示器，坐标轴触发有效
		            type: false // 默认为直线，可选为：'line' | 'shadow'
		        },
		        formatter: function(params) {
		            var time = '';
		            var str = '';
		            for (var i of params) {
		                time = i.name.replace(/\n/g, '') + '<br/>';
		                if (i.data == 'null' || i.data == null) {
		                    str += i.seriesName + '：无数据' + '<br/>'
		                } else {
		                    str += i.seriesName + '：' + Math.abs(i.data) + '<br/>'
		                }
		            }
		            return time + str;
		        },
		    },
		    legend: {
		        top: 30,
		        itemGap: 10,
		        itemWidth: 10,
		        itemHeight: 10,
		        data: data.legend
		    },
		    color: data.color,
		    grid: {
		        x: 50,
		        x2: 30,
		        y2: 5,
		        containLabel: true
		    },
		    xAxis: {
		        show: false
		    },
		    yAxis: [{
		        type: 'category',
		        axisLine: {
		            show: false
		        },
		        axisTick: {
		            show: false
		        },
		        axisLabel: {
		            show: true,
		            interval: '0',
		            textStyle: {
		                fontSize: 15,
		                color: 'black',
		            },
		        },
		        data: data.yAxis
		    }],
		    series: [{
		            name: data.legend[0],
		            type: 'bar',
		            barWidth: data.barWidth||12,
		            stack: '总量',
		            label: {
		                normal: {
		                    show: true,
		                    position: 'left',
		                    color: 'black',
		                    fontSize: '15',
		                    formatter: function(params) {
		                        return params.data * -1+"%";
		                    }
		                },
		
		            },
		            data: data.xAxis[0]
		        },
		        {
		            name: data.legend[1],
		            type: 'bar',
		            barWidth: data.barWidth||12,
		            stack: '总量',
		            label: {
		                normal: {
		                    show: true,
		                    position: 'right',
			                fontSize: 15,
			                color: 'black',
			                 formatter: function(params) {
		                        return params.data+"%";
		                    }
		                }
		            },
		            data: data.xAxis[1]
		        },
		    ]
		}
	if (option && typeof option === "object") {
	    myChart.setOption(option, true);
	}	
	}
       </script>
   </body>
       </script>
   </body>
</html>