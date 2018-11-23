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
	<script src="/Bug/js/worldcloud.js"></script>
	<title>众包协作概况</title>
</head>
<body style="">
<%@include file="navi.html"%>
    <div class="modal fade" id="bugTreeCloud" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true" style="position:absolute;top:30px">
      <div class="modal-dialog">
          <div class="modal-content" style="height:580px;width:650px">
              <div class="modal-header" style="text-align: center;">
                  	<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
	                <h3 class="modal-title" id="myModalLabel">Bug树词汇云</h3>
              </div>
              <div class="modal-body">
				<div class="col-md-12 col-sm-12 col-xs-12" style="background-image:url(/assets/img/recommend-bug-image/back.jpg);background-size:cover">
				    <div  id="cloudComponent" style="height:500px;width:600px"></div>
				</div>
              </div>
          </div><!-- /.modal-content -->
      </div><!-- /.modal -->
    </div>
	<div class="row" style="height:600px;margin-top:50px">
		<div style="height:100%;width:5%;float:left;"></div>
		<div id= "relationDiv"  style="height:100%;width:90%;background:#7B7B7B;float:left;"></div>
		<div style="height:100%;width:5%;float:left;"></div>
		<div style="clear:both"></div>
    </div>
</body>

<script type="text/javascript">
	var refreshTime = 60000;
	var nodes = new Array();
	var links = new Array();
	var text_color = 'white';
   	var item_color = 'rgba(226,245,240,1)';
   	var border_color = 'rgba(0,0,0,0.9)';        
	var images = [
        'image://../image/people.png',
        'image://../image/tree.png'
  		];
  	var categories = [
          {"name":"测试者",'icon':images[0],"itemStyle":{"normal":{"color":item_color,'borderColor':border_color}}},
          {"name":"Bug树",'icon':images[1],"itemStyle":{"normal":{"color":item_color,'borderColor':border_color}}}];
          
   //用于使chart自适应高度和宽度,通过窗体高宽计算容器高宽
	var relation = document.getElementById('relationDiv');
	var relationGraph = echarts.init(relation);
	$("document").ready(function(){
	     httpGetData(drawGraph);
	});
	//refreshTime秒刷新一次
	window.setInterval(function(){
		console.log("refresh");  
	    httpGetData(drawGraph);
	}, refreshTime); 
	//页面跳转
   function navigatePage(toPage){
   	if(toPage=="relation"){
   		window.location.href = "/Bug/views/relationShow.jsp?case_take_id="+case_take_id;
   	}
   	else{
   		window.location.href = "/Bug/views/show.jsp?case_take_id="+case_take_id;
   	}
   }   
	
	//模态框动态加载
	relationGraph.on('click',function(params){
		var cloudData = new Array(); 
		if(params.category==0){
			return;
		}
		else{
		    //document.getElementById('myModalLabel').innerHTML = params.value +'词汇云';
	        $.ajax({
	               url : "http://47.99.140.117:8080/Bug/api/report/keyWords",
	               type : "GET",
	               dataType : "json", 
	               data:{'id':params.name},
	               success: function(response, status, xhr){
	               		for(var p in response){
	               			var temp = {};
	               			temp["name"] = p;
	               			temp["value"] = response[p];
	               			//temp["itemStyle"] = createRandomItemStyle();
	               			cloudData.push(temp);
	               		}
	               		drawModel(cloudData);
	               },
	               error:function(response){
	               	console.log("获取词汇云错误");
	               }
	         });	
		}
	})

	//发送请求获取数据
	function httpGetData(callbackFunction){    
            $.ajax({
                url : "http://47.99.140.117:8080/Bug/api/report/relations",
                type : "GET",
                dataType : "json",
                data:{'case_take_id':'1489-2613'},
                success: function(response, status, xhr){
                	personNode = response["PersonNode"];
                	treeNode = response["TreeNode"];
                	link = response["Link"];
                	packageRelationData(personNode,treeNode,link);
                	return callbackFunction();
                },
                error:function(response){
                	console.log("取关系数据错误");
                }
            });          
	}
	
	//打包relation数据
	function packageRelationData(personNode,treeNode,linksData){
	    nodes = new Array();
	    links = new Array();
	    var maxNum = 100;
	    //var width = 2000;
	    var height = 1000;
	    var level = parseInt(personNode.length/maxNum)+1;
		//var dx = width/(personNode.length+1);
		dx = 100;
		var dy = height/(level+1);
		for(var i=0;i<personNode.length;i++){
			var node = {};
			//node["value"] = "人"+(i+1);
			node["x"] = dx*i;
			node["y"] = 50+dy*(i%level);
			node["category"] = 0;
			node["symbol"] = images[0];
			node["name"] = personNode[i];
			nodes.push(node);
		}
		 
		var level = parseInt(treeNode.length/maxNum)+1;
		var dy = height/(level+1);
		//var dx = width/(treeNode.length+1);
		for(var i=0;i<treeNode.length;i++){
			var node = {};
			node["value"] = "节点:"+countBugs(treeNode[i],linksData);
			node["x"] = dx*i;
			node["y"] = height/2+50+dy*(i%level);
			node["category"] = 1;
			node["symbol"] = images[1];
			node["name"] = treeNode[i];
			nodes.push(node);
		}
		
		for(var i=0;i<linksData.length;i++){
			var temp = {source:linksData[i][0],target:linksData[i][1]};
			links.push(temp);
		}
	}
	
	function countBugs(name,linksData){
		var count = 0; 
		for(var i=0;i<linksData.length;i++){
			if(name==linksData[i][1]){
				count++;
			}
		}
		return count;
	}

	//颜色随机
	function createRandomItemStyle() {
	    return {
	        normal: {
	            color: 'rgb(' + [
	                Math.round(Math.random() * 160),
	                Math.round(Math.random() * 160),
	                Math.round(Math.random() * 160)
	            ].join(',') + ')'
	        }
	    };
	}
	
	//绘制模态框
	function drawModel(cloudData){
		var cloudOption = {
		    tooltip: {
		        show: true
		    },
		    series: [{
		        name: '词频',
		        type: 'wordCloud',
		        size: ['100%', '100%'],
		        textRotation : [0, 45, 90, -45],
		        textPadding: 0,
		        gridSize:50,
		        sizeRange: [20, 50],
				left: 'center',
		        top: 'center',
		        width: '100%',
		        height: '100%',
		        drawOutOfBound: false,
/* 		        autoSize: {
		            enable: true,
		            minSize: 10
		        }, 
                textStyle: {
                    normal: {
                        color: function() {
                            return 'rgb(' +
                                    Math.round(Math.random() * 255) +
                                    ', ' + Math.round(Math.random() * 255) +
                                    ', ' + Math.round(Math.random() * 255) + ')'
                        }
                }}, */
                textStyle: {
           		normal: {
	                fontFamily: 'sans-serif',
	                fontWeight: 'bold',
	                // Color can be a callback function or a color string
	                color: function () {
	                    // Random color
	                    return 'rgb(' + [
	                        Math.round(Math.random() * 160),
	                        Math.round(Math.random() * 160),
	                        Math.round(Math.random() * 160)
	                    ].join(',') + ')';
	                }
            	},
	            emphasis: {
	                shadowBlur: 10,
	                shadowColor: '#333'
	            }
        },
		        data:cloudData
		    }]
		};
		var cloudComponent = document.getElementById('cloudComponent');
		var cloudGraph = echarts.init(cloudComponent);
		cloudGraph.setOption(cloudOption);
		$('#bugTreeCloud').modal('show');
	}
	
	//绘制关系图
	function drawGraph(){
		var relationOption = {
			//背景设置
/* 			backgroundColor: {
			    type: 'pattern',
			    repeat: 'repeat'
			}, */
/* 			  title:{
			       text: 'Bug树与测试者关系图',
			       x: '50%',
			       textAlign: 'center',
				   textStyle:{
			       		color:"#FFFFFF",
			       		fontSize:25
			       } 
			   }, */
			  backgroundColor: "rgba(2,13,34,1)",
		      tooltip: {},
		      color:['red','green'],
		      legend: {
		         show: true,
		         itemWidth: 20,
		         itemHeight: 20,
		         orient: 'vertical',
		         x: 'right', 
		         data: categories,
		         textStyle:{color:text_color},
		         selectedMode:false
		     },
		     grid:{
		         top:'0',//距上边距
		         bottom:'0',
		         right:'0',
		         left:'0'
		     },
		      animationDurationUpdate: 1500,
		      animationEasingUpdate: 'quinticInOut',
		      series : [
		          {
		              type: 'graph',
		              layout: 'none',
		              symbolSize: 50,
		              roam: true,
		              label: {
		                  normal: {
		                      position: 'top',
		                      show: true,
		                      formatter: function(params){
		                      	if(params.data.category == 0){
		                      		return params.name;
		                      	}
		                      	else{
		                      		return params.value;
		                      	}
		                      },
		                      color: text_color,
		                      fontSize:15
		                  }
		              },
		              edgeSymbol: ['circle', 'arrow'],
		              edgeSymbolSize: [4, 10],
		              edgeLabel: {
		                  normal: {
		                      textStyle: {
		                          fontSize: 15
		                      }
		                  }
		              },
		              data:nodes,
		              links:links,
		              categories:categories,
                	  focusNodeAdjacency: true,
		              categories:categories,
		              lineStyle: {
		                  normal: {
		                      opacity: 0.9,
		                      width: 2,
		                      curveness: 0
		                  }
		              },
		              tooltip: {
	                    trigger: 'item',
	                    formatter: function(params){
		                      	if(params.data.category == 0){
		                      		return params.name;
		                      	}
		                      	else{
		                      		return params.value;
		                      	}
	                    }
	                }
		          }
		      ]
		    };
		relationGraph.setOption(relationOption);		 
	}
	
    </script>
</html>