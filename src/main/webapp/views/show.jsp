<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<link rel="stylesheet" href="/Bug/css/bootstrap.css">
	<script src="/Bug/js/echarts.min.js"></script>
	<script src="/Bug/js/jquery-3.2.0.min.js"></script>
	<script src="/Bug/js/bootstrap.min.js"></script>
	<script src="/Bug/js/worldcloud.js"></script>
	<title>众包协作概况</title>
</head>
<body style="background-image:url(/Bug/image/mooctest.jpg);background-size:cover">
	<div class="row" style="margin:20px">
		<div class="col-md-4 col-sm-4 col-xs-4">
			   &nbsp;&nbsp;&nbsp;&nbsp;
			   <a  onclick="javascript:navigatePage('rank')"> 
			   		<img id="likeImg" alt="" style="vertical-align:middle;height:30px;width:30px" src="/Bug/image/rank.png">
			   </a>
			   &nbsp;&nbsp;&nbsp;&nbsp;
			   <a onclick="javascript:navigatePage('relation')"> 
			   		<img id="likeImg" alt="" style="vertical-align:middle;height:30px;width:30px" src="/Bug/image/relation.png">
			   </a>
		</div>
		<div class="col-md-4 col-sm-4 col-xs-4" style="text-align:center;">
			<span style="font-weight: bolder;font-size: 40px;color:#FFFFFF;"><!-- <img alt="" src="/Bug/image/mooc.png" style="height:40px;width:40px">慕测 -->群体智能协作</span>
		</div>
	</div>
  
	<div class="row" style="height:600px;margin-top:20px">
	<div style="height:100%;width:2%;float:left;"></div>
		<div id= "thumsDiv"  style="height:100%;width:47%;background:#7B7B7B;float:left;"></div>
		<div style="height:100%;width:2%;float:left;"></div>
		<div id= "forksDiv"  style="height:100%;width:47%;background:#7B7B7B;float:left;"></div>
		<div style="height:100%;width:2%;float:left;"></div>
		<div style="clear:both"></div>
		<!-- <div id= "forksDiv"  style="height:100%;background:#3C3C3C;" class="col-md-5 col-sm-5 col-xs-5"></div> -->
    </div>
</body>

<script type="text/javascript">
	var case_take_id;
	var thumJson;
	var forkJson;
	var thumsDiv = document.getElementById('thumsDiv');
	var thumsGraph = echarts.init(thumsDiv);
	var forksDiv = document.getElementById('forksDiv');
	var forksGraph = echarts.init(forksDiv);
	var maxCount = 10;
	var refreshTime = 60000;
	var img = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAD4AAAA/CAYAAAC4nXvhAAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAAAyZpVFh0WE1MOmNvbS5hZG9iZS54bXAAAAAAADw/eHBhY2tldCBiZWdpbj0i77u/IiBpZD0iVzVNME1wQ2VoaUh6cmVTek5UY3prYzlkIj8+IDx4OnhtcG1ldGEgeG1sbnM6eD0iYWRvYmU6bnM6bWV0YS8iIHg6eG1wdGs9IkFkb2JlIFhNUCBDb3JlIDUuNi1jMTQyIDc5LjE2MDkyNCwgMjAxNy8wNy8xMy0wMTowNjozOSAgICAgICAgIj4gPHJkZjpSREYgeG1sbnM6cmRmPSJodHRwOi8vd3d3LnczLm9yZy8xOTk5LzAyLzIyLXJkZi1zeW50YXgtbnMjIj4gPHJkZjpEZXNjcmlwdGlvbiByZGY6YWJvdXQ9IiIgeG1sbnM6eG1wPSJodHRwOi8vbnMuYWRvYmUuY29tL3hhcC8xLjAvIiB4bWxuczp4bXBNTT0iaHR0cDovL25zLmFkb2JlLmNvbS94YXAvMS4wL21tLyIgeG1sbnM6c3RSZWY9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC9zVHlwZS9SZXNvdXJjZVJlZiMiIHhtcDpDcmVhdG9yVG9vbD0iQWRvYmUgUGhvdG9zaG9wIENDIDIwMTggKFdpbmRvd3MpIiB4bXBNTTpJbnN0YW5jZUlEPSJ4bXAuaWlkOkE0M0E4N0M5QjBFRjExRTg4MTFCRTk4MzM1OEI3RTk2IiB4bXBNTTpEb2N1bWVudElEPSJ4bXAuZGlkOkE0M0E4N0NBQjBFRjExRTg4MTFCRTk4MzM1OEI3RTk2Ij4gPHhtcE1NOkRlcml2ZWRGcm9tIHN0UmVmOmluc3RhbmNlSUQ9InhtcC5paWQ6QTQzQTg3QzdCMEVGMTFFODgxMUJFOTgzMzU4QjdFOTYiIHN0UmVmOmRvY3VtZW50SUQ9InhtcC5kaWQ6QTQzQTg3QzhCMEVGMTFFODgxMUJFOTgzMzU4QjdFOTYiLz4gPC9yZGY6RGVzY3JpcHRpb24+IDwvcmRmOlJERj4gPC94OnhtcG1ldGE+IDw/eHBhY2tldCBlbmQ9InIiPz4uP4qxAAAMDElEQVR42uRb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";
    //var img =  'image://../image/people.png';
    //页面跳转
	function navigatePage(toPage){
		if(toPage=="relation"){
			window.location.href = "/Bug/views/relationShow.jsp?case_take_id="+case_take_id;
		}
		else{
			window.location.href = "/Bug/views/show.jsp?case_take_id="+case_take_id;
		}
	}
    //3秒刷新一次
	window.setInterval(function(){ 
	    httpGetData(drawGraph);
	}, refreshTime);
	      
    //用于使chart自适应高度和宽度,通过窗体高宽计算容器高宽
	$("document").ready(function(){
		if(getRequest());
	    	httpGetData(drawGraph);
	}); 
	
	//获取case_take_id
	function getRequest() {
        var url = decodeURI(location.search); //获取url中"?"符后的字串
        var theRequest = new Object();
        if (url.indexOf("?") != -1) {
            var str = url.substr(1);
            strs = str.split("&");
            for ( var i = 0; i < strs.length; i++) {
                theRequest[strs[i].split("=")[0]] = unescape(strs[i].split("=")[1]);
            }
            case_take_id = theRequest["case_take_id"];
            return true;
        }
        else{
        	alert("请输入case_take_id");
        	return false;
        }
    }

	//发送请求获取数据
	function httpGetData(callbackFunction){
           $.ajax({
                url : "http://47.99.140.117:8080/Bug/api/report/ThumsRank",
                type : "GET",
                dataType : "json", 
                data:{'case_take_id':case_take_id},
                success: function(response, status, xhr){
                	thumJson = sortJson(response);
                },
                error:function(response){
                	console.log("取点赞数据错误");
                }
            });
            
            $.ajax({
                url : "http://47.99.140.117:8080/Bug/api/report/ForkRank",
                type : "GET",
                dataType : "json", 
                data:{'case_take_id':case_take_id},
                success: function(response, status, xhr){
                	forkJson = sortJson(response);
                	return callbackFunction();
                },
                error:function(response){
                	console.log("取fork数据错误");
                }
            });        
	}
	
	//排序函数
	function sortJson(sourceJson){
		var array = new Array();
		var names = new Array();
		var counts = new Array();
		for(var  k in sourceJson){
			var temp = {};
			temp["name"] = k;
			temp["count"] = sourceJson[k];
			array.push(temp)
		}
		var target = array.sort(sortJ);
		for(var i=0;i<target.length&&i<maxCount;i++){
			names.push(target[i].name);
			counts.push(target[i].count);
		} 
		var result = {};
		result["names"] = names.reverse();
		result["counts"] = counts.reverse();
		return result;
	}	
	function sortJ(a,b){
		return b.count-a.count;
	}
	
function drawGraph(){
	option1 = {
	    backgroundColor: "rgba(2,13,34,1)",
	  	//标题设置
	    title: [{
	        text: '被点赞用户排行榜',
	        x: '50%',
	        textAlign: 'center',
	        subtextStyle:{
	        	color:'#FFFFFF',
	        	fontSize:25
	        },
	        textStyle:{
	        	verticalAlign:'middle',
	        	color:"#FFFFFF",
	        	fontSize:25
	        }      
	    }],
	    tooltip: {
	        show: true,
	        trigger: 'axis',
	        axisPointer: {
	            type: 'line'
	        }
	    },
	    grid: {
	        top: '30',
	        bottom: '10',
	        left: '100',
	        containLabel: true
	    },
	    xAxis: [{
	        type: 'value',
	        axisLabel: {
	            show: false,
	            interval: 1,
	            textStyle: {
	                color: '#fff',
	                fontSize: 12
	            }
	        },
	        axisLine: {
	            show: false
	        },
	        axisTick: {
	            show: false
	        },
	        splitLine: {
	            show: false
	        }
	
	    }],
	    yAxis: {
	        type: 'category',
	        data: thumJson.names,
	        axisLabel: {
	            color: "rgba(255,255,255,1)",
	            formatter: function(value) {
	                return '{' + 0 + '| }{value|' + value + '}';
	            },
	            rich: {
	                value: {
	                    align: 'center',
	                    padding: [0, 0, 0, -5],
	                    fontSize: 20
	                },
	                0: {
	                    height: 60,
	                    align: 'center',
	                    backgroundColor: {
	                        image: img
	                    }
	                }
	            }
	        },
	        axisTick: {
	            show: false
	        },
	        axisLine: {
	            show: false
	        },
	        zlevel: 99
	    },
	    series: [{
	        type: 'bar',
	        barWidth: '30%',
	        barCategoryGap: '20',
	        itemStyle: {
	            normal: {
	                barBorderRadius: [10, 10, 10, 10],
	                color: new echarts.graphic.LinearGradient(
	                    0, 0, 1, 0,
	                    [{
	                            offset: 0,
	                            color: '#048BFE'
	                        },
	                        {
	                            offset: 1,
	                            color: '#00EEF1'
	                        }
	                    ]
	                ),
	                label: {
	                    show: true,
	                    position: 'right',
	                    textStyle: {
	                        color: '#fff',
	                        fontSize: '14'
	                    }
	                }
	            }
	        },
	        data: thumJson.counts
	    }]
	};
	
	option2 = {
    backgroundColor: "rgba(2,13,34,1)",
  	//标题设置
    title: [{
        text: '被Fork用户排行榜',
        x: '50%',
        textAlign: 'center',
        subtextStyle:{
        	color:'#FFFFFF',
        	fontSize:25
        },
        textStyle:{
        	verticalAlign:'middle',
        	color:"#FFFFFF",
        	fontSize:25
        }      
    }],
    tooltip: {
        show: true,
        trigger: 'axis',
        axisPointer: {
            type: 'line'
        }
    },
    grid: {
        top: '30',
        bottom: '10',
        left: '100',
        containLabel: true
    },
    xAxis: [{
        type: 'value',
        axisLabel: {
            show: false,
            interval: 1,
            textStyle: {
                color: '#fff',
                fontSize: 12
            }
        },
        axisLine: {
            show: false
        },
        axisTick: {
            show: false
        },
        splitLine: {
            show: false
        }

    }],
    yAxis: {
        type: 'category',
        data: forkJson.names,
        axisLabel: {
            color: "rgba(255,255,255,1)",
            formatter: function(value) {
                return '{' + 0 + '| }{value|' + value + '}';
            },
            rich: {
                value: {
                    align: 'center',
                    padding: [0, 0, 0, -5],
                    fontSize: 20
                },
                0: {
                    height: 60,
                    align: 'center',
                    backgroundColor: {
                        image: img
                    }
                }
            }
        },
        axisTick: {
            show: false
        },
        axisLine: {
            show: false
        },
        zlevel: 99
    },
    series: [{
        type: 'bar',
        barWidth: '30%',
        barCategoryGap: '20',
        itemStyle: {
            normal: {
                barBorderRadius: [10, 10, 10, 10],
                color: new echarts.graphic.LinearGradient(
                    0, 0, 1, 0,
                    [{
                            offset: 0,
                            color: '#048BFE'
                        },
                        {
                            offset: 1,
                            color: '#00EEF1'
                        }
                    ]
                ),
                label: {
                    show: true,
                    position: 'right',
                    textStyle: {
                        color: '#fff',
                        fontSize: '14'
                    }
                }
            }
        },
        data: forkJson.counts
    }]
};
	    thumsGraph.setOption(option1);
	    forksGraph.setOption(option2);		 
	}
	
    </script>
</html>