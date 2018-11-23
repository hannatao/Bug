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
           url : "http://47.99.140.117:8080/Bug/api/graph/11",
           type : "GET",
           dataType : "json", 
           data:{'case_take_id':"1489-2613"},
           success: function(response, status, xhr){
           		var dataArray = [];
           		var maxUsecaseCount = 0; 
      			response.forEach(function(people){
      				if(people["use_case"] > maxUsecaseCount){
      					maxUsecaseCount = people["use_case"] ;
      				}
      			})
      			console.log("maxUsecaseCount:"+maxUsecaseCount);
      			var seriesDataTotal = [];
      			var seriesDataValid = [];
      			for(var i=1;i<=maxUsecaseCount;i++){
      				var cate1 = [];
      				var cate2 = [];
	      			response.forEach(function(people){
	      				if(people["use_case"] == i){
	      					cate1.push(people["total_bugs"]);
	      					cate2.push(people["valid_bugs"]);
	      				}
	      			})
	      			seriesDataTotal.push(cate1);
	      			seriesDataValid.push(cate2);
      			}
      			console.log(seriesDataTotal);
      			console.log(seriesDataValid);
      			dataArray.push(echarts.dataTool.prepareBoxplotData(seriesDataTotal));
      			dataArray.push(echarts.dataTool.prepareBoxplotData(seriesDataValid));
      			console.log(dataArray);
           		drawChart(dataArray);
           },
           error:function(response){
           		console.log("获取11数据错误");
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
	            text: '用例与Bug数目盒状图',
	            left: 'center',
	        },
	        legend: {
	            y: '5%',
	            data: ['total_bugs', 'valid_bugs']
	        },
	        tooltip: {
	            trigger: 'item',
	            axisPointer: {
	                type: 'shadow'
	            }
	        },
	        grid: {
	            left: '10%',
	            top: '10%',
	            right: '10%',
	            bottom: '15%'
	        },
	        xAxis: {
			    name:"用例数",
		    	nameTextStyle:{
		    		fontSize:15,
		    	},
	            type: 'category',
	            data: $.map(data[0].axisData,function(n){
       					return parseInt(n)+1;
   					}),
	            boundaryGap: true,
	            nameGap: 30,
	            splitArea: {
	                show: true
	            },
	            axisLabel: {
	                formatter: '{value}'
	            },
	            splitLine: {
	                show: false
	            }
	        },
	        yAxis: {
	            type: 'value',
			    name:"Bug数",
		    	nameTextStyle:{
		    		fontSize:15,
		    	},
	            min: 0,
	            max: 15,
	            splitArea: {
	                show: false
	            }
	        },
	        dataZoom: [
	            {
	                type: 'inside',
	                start: 0,
	                end: 20
	            },
	            {
	                show: true,
	                height: 20,
	                type: 'slider',
	                top: '90%',
	                xAxisIndex: [0],
	                start: 0,
	                end: 20
	            }
	        ],
	        series: [
	            {
	                name: 'total_bugs',
	                type: 'boxplot',
	                data: data[0].boxData,
	                tooltip: {formatter: formatter}
	            },
	            {
	                name: 'valid_bugs',
	                type: 'boxplot',
	                data: data[1].boxData,
	                tooltip: {formatter: formatter}
	            }
	        ]
	    };
		if (option && typeof option === "object") {
	        myChart.setOption(option, true);
	    }
    }

    function formatter(param) {
        return [
            '' + param.name + ': ',
            'upper: ' + param.data[0],
            'Q1: ' + param.data[1],
            'median: ' + param.data[2],
            'Q3: ' + param.data[3],
            'lower: ' + param.data[4]
        ].join('<br/>')
    };
    </script>
   </body>
</html>