<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>个性化地图</title>
    <meta name="viewport" content="initial-scale=1.0, user-scalable=no" />
    <style type="text/css">
        html{height:100%}
        body{height:100%;margin:0px;padding:0px}
        #container{height:100%}
    </style>
    <script type="text/javascript" src="http://api.map.baidu.com/api?v=3.0&ak=uGYjIWKAhMUtqW4GoOTCXrmHbDU4L9dr"></script>
</head>
<body>
<div id="container"></div>
<script type="text/javascript">
    // 创建地图实例
    var map = new BMap.Map("container");
    // 创建点坐标
    var point = new BMap.Point(116.404, 39.915);
    // 初始化地图，设置中心点坐标和地图级别
    map.centerAndZoom(point, 15);
    //开启鼠标滚轮缩放
    map.enableScrollWheelZoom(true);
    //平移和缩放
    //map.addControl(new BMap.NavigationControl());
    //显示小型的平移缩放控件
    var opts = {type: BMAP_NAVIGATION_CONTROL_SMALL}
    map.addControl(new BMap.NavigationControl(opts));
    //比例尺
    //map.addControl(new BMap.ScaleControl());
    //使用偏移量，避免两个控件发生重叠
    var opts = {offset: new BMap.Size(150, 20)}
    map.addControl(new BMap.ScaleControl(opts));
    //可折叠的缩略地图
    map.addControl(new BMap.OverviewMapControl());
    //地图类型
    map.addControl(new BMap.MapTypeControl());
    map.setCurrentCity("北京"); // 仅当设置城市信息时，MapTypeControl的切换功能才能可用

    map.setMapStyleV2({
        styleId: 'd66e94f08581d169dd91c8b6dc7e08f0'
    });
</script>
</body>
</html>
