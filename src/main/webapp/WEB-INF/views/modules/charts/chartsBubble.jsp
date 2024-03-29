<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="head.jsp"%>
<html>
<head>
    <title>气泡图</title>
    <script src="http://code.highcharts.com/highcharts-more.js"></script>
</head>
<body>
<div id="container" style="width: 550px; height: 400px; margin: 0 auto"></div>
<script language="JavaScript">
    $(document).ready(function() {
        var chart = {
            type: 'bubble',
            zoomType: 'xy'
        };
        var title = {
            text: 'Highcharts Bubbles'
        };
        var series= [{
            data: [[97, 36, 79], [94, 74, 60], [68, 76, 58], [64, 87, 56], [68, 27, 73], [74, 99, 42], [7, 93, 87], [51, 69, 40], [38, 23, 33], [57, 86, 31]]
        }, {
            data: [[25, 10, 87], [2, 75, 59], [11, 54, 8], [86, 55, 93], [5, 3, 58], [90, 63, 44], [91, 33, 17], [97, 3, 56], [15, 67, 48], [54, 25, 81]]
        }, {
            data: [[47, 47, 21], [20, 12, 4], [6, 76, 91], [38, 30, 60], [57, 98, 64], [61, 17, 80], [83, 60, 13], [67, 78, 75], [64, 12, 10], [30, 77, 82]]
        }
        ];

        var json = {};
        json.chart = chart;
        json.title = title;
        json.series = series;
        $('#container').highcharts(json);

    });
</script>
</body>
</html>
