<!DOCTYPE html>
<html>
<head>
  	<#import "./common/common.macro.ftl" as netCommon>
	<@netCommon.commonStyle />
	<title>${I18n.admin_name}</title>
	 
	<script src="https://cdn.jsdelivr.net/npm/echarts@5/dist/echarts.min.js"></script>
</head>
<body class="hold-transition sidebar-mini">
<div class="wrapper">
    <!-- 导航栏 -->
    <nav class="main-header navbar navbar-expand navbar-white navbar-light">
    	<@netCommon.commonHeader />
    </nav>
    <!-- /.导航栏 -->

    <!-- 主侧边栏容器 -->
    <aside class="main-sidebar sidebar-dark-primary elevation-4">
	    <@netCommon.commonLeft "help" />
    </aside>
	<!-- Content Wrapper. Contains page content -->
	<div class="content-wrapper">
		<!-- Content Header (Page header) -->
		<section class="content-header">
			<h1>${I18n.job_help}</h1>
		</section>
        <!-- 为 ECharts 准备一个具备大小（宽高）的 DOM -->
        <div id="main" style="width: 100%; height:400px;"></div>
	</div>
	<!-- footer -->
	<@netCommon.commonFooter />
</div>
<@netCommon.commonScript />
    <script type="text/javascript">
    // 基于准备好的dom,初始化echarts实例
    var myChart = echarts.init(document.getElementById('main'));
    var graph={  //这是数据项目中一般都是获取到的
        nodes:[
            {"id":"0","name":"id0","x": 100,"y": 200, symbolSize: [42,42],sizeFlag: [42,42]},  
            {"id":"1","name":"id1","x": 100,"y": 200, symbolSize: [42,42],sizeFlag: [42,42]},
            {"id":"2","name":"id2","x": 100,"y": 200, symbolSize: [42,42],sizeFlag: [42,42]},
            {"id":"3","name":"id3","x": 100,"y": 200, symbolSize: [42,42],sizeFlag: [42,42]},  
        ],
        links:[
            {"id":"0", "name":"0-1",  "source":"0","target":"1"},
            {"id":"1", "name":"0-2", "source":"0","target":"2"},
            {"id":"2", "name":"0-3", "source":"0","target":"3"},
        ]
    } 
    var categories=[
        {
            id:0,
            name: '手机',
            itemStyle:{normal:{color:'#c23531'}},
            symbolSize:[42,42]
        },
        {
            id:1,
            name: 'QQ',
            itemStyle:{normal:{color:'#61a0a8'}},
            symbolSize:[42,42]
        },
        {
            id:2,
            name:'微信',
            itemStyle:{normal:{color:'#749f83'}},
            symbolSize:[42,42]
        },
        {
            id:3,
            name:'微博',
            itemStyle:{normal:{color:'#d48265'}},
            symbolSize:[42,42]
        },
        {
            id:4,
            name: '其它',
            itemStyle:{normal:{color:'#2E3F4C'}},
            symbolSize:[64,64]
        }
    ];
    var winWidth=document.body.clientWidth;
    var winHeight=document.body.clientHeight;
    graph.nodes.forEach(function (node) {
        node.x=parseInt(Math.random()*1000);  //这里是最重要的如果数据中有返回节点x,y位置这里就不用设置,如果没有这里一定要设置node.x和node.y,不然无法定位节点 也实现不了拖拽了；
        node.y=parseInt(Math.random()*1000); 
        node.label={
            normal:{
                show:true
            }
        }
    });
    graph.links.forEach(function (link) {       
        link.label={
            normal:{
                show:true
            }
        }
    });
    //这里是option配置
    var option = {
        //图例组件
        legend: [{   
            data: categories.map(function (a) {
                return a.name;
            }),
            top:0,
            itemGap:26,
            textStyle:{
                padding:[0,12]
            },
            backgroundColor:'#f5f5f5'
        }],
        animationDurationUpdate: 1500,
        animationEasingUpdate: 'quinticInOut',
        series : [{
            type: 'graph',
            layout: 'none',           //因为节点的位置已经有了就不用在这里使用布局了
            circular:{rotateLabel:true},
            animation: false,
            data: graph.nodes,
            links: graph.links,
            categories: categories, //节点分类的类目
            roam: true,             //添加缩放和移动
            //draggable: false,     //注意这里设置为false,不然拖拽鼠标和节点有偏移
            label: {
                normal: {
                    position: 'bottom',
                    rich:{
                        bg:{
                            backgroundColor: '#f5f5f5'
                        }
                    }
                }
            }
        }]
    };
    myChart.setOption(option);  
    myChart.on('click', function (item){
        console.log(item); 
    });

    initInvisibleGraphic();
    function initInvisibleGraphic() {
        // Add shadow circles (which is not visible) to enable drag.
        myChart.setOption({
            graphic: echarts.util.map(graph.nodes, function (item, dataIndex) {  
                var tmpPos=myChart.convertToPixel({'seriesIndex': 0},[item.x,item.y]);
        
                //使用图形元素组件在节点上划出一个隐形的图形覆盖住节点
                return {
                    type: 'circle',
                    id:dataIndex,
                    x: tmpPos[0],
                    y: tmpPos[1],
                    shape: {
                        cx: 0,
                        cy: 0,
                        r: 20
                    },
                    info: item,
                    name: item.name,
                    // silent:true,
                    invisible: true,
                    draggable: true,
                    ondrag: echarts.util.curry(onPointDragging, dataIndex),
                    z: 100              //使图层在最高层
                };
            })
        });
        window.addEventListener('resize', updatePosition);
        myChart.on('dataZoom', updatePosition);
    }
    myChart.on('graphRoam', updatePosition);
    function updatePosition() {    //更新节点定位的函数
        myChart.setOption({
            graphic: echarts.util.map(graph.nodes, function (item, dataIndex) {
                var tmpPos=myChart.convertToPixel({'seriesIndex': 0},[item.x,item.y]);
               
                return {
                    x: tmpPos[0],
                    y: tmpPos[1]
                };
            })
        });

    }
    function onPointDragging(dataIndex) {      //节点上图层拖拽执行的函数
        var tmpPos=myChart.convertFromPixel({'seriesIndex': 0},this.position);
        option.series[0].data[dataIndex].x = tmpPos[0];
        option.series[0].data[dataIndex].y = tmpPos[1];
        myChart.setOption(option);
        updatePosition();
    }

    </script>
    

</body>
</html>
