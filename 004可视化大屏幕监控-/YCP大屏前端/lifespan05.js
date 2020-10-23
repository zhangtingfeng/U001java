function LifeSpanmainEchart05(letdddddText) {
    //debugger;
    //return;

    let data0 = new Array();

    for (let i = 0; i < globalButtonList.length; i++) {
        if (globalButtonList[i][3] == letdddddText) {
            data0[0] = 'YASKAWA';
            data0[1] = globalButtonList[i][0];
            data0[2] = globalButtonList[i][1];
            data0[3] = globalButtonList[i][2];
            data0[4] = "";///红绿灯状态
            data0[5] = globalButtonList[i][3];


            break;

            /*  设备信息：工厂名
              设备信息：产线名
              设备信息：单元名
              设备信息：控制柜名
              设备信息：机器人型号
              设备信息：序列号
              设备信息：用途
              设备信息：版本号
              设备信息：当前状态
              设备信息：Control power on time
              设备信息：Energy saving state accumulated time
              设备信息：Working time
              设备信息：当前模式（单循环/自动循环等）*/
        }
    }

    let timestamp2End = (new Date()).valueOf();
    let timestamp2Start = new Date().setMonth((new Date().getMonth() - 10)).valueOf();//一个月


////"http://" + window.location.host + "/YCPApp/api/v2/"
    let ajaxurl = "http://" + window.location.host + "/YCPApp/getTorqueGraphData?" +
        "factoryName=YASKAWA" +
        "&lineName=" + data0[1] +
        "&cellName=" + data0[2] +
        "&unitName=" + data0[3] +
        "&robot=R1" +
        "&graphName=reducer_life";


    $.when($.ajax({
        type: "get",
        url: ajaxurl,
        async: true,
        cache: false,
        data: null,
        contentType: "application/json",
        dataType: "json",

    })).done(function (a01) {

        //debugger;
        showEchart05(letdddddText, a01.graphData);
        // debugger;
// a1 and a2 are arguments resolved for the page1 and page2 ajax requests, respectively.
// Each argument is an array with the following structure: [ data, statusText, jqXHR ]

        /*
        {
	"currentMinMax": [0.0, 36000.0],
	"minMaxThreshold": [15000.0, 15000.0],
	"jsonKeys": [{
		"R": "CurrentR",
		"B": "CurrentB",
		"S": "CurrentS",
		"T": "CurrentT",
		"U": "CurrentU",
		"L": "CurrentL"
	}],
	"minMax": ["1000.0", "0.0"],
	"graphData": [
		["CurrentS", 0.0],
		["S", 15000.0],
		["CurrentL", 0.0],
		["L", 15000.0],
		["CurrentU", 0.0],
		["U", 15000.0],
		["CurrentR", 0.0],
		["R", 15000.0],
		["CurrentB", 0.0],
		["B", 15000.0],
		["CurrentT", 0.0],
		["T", 15000.0]
	]
}
         */

        //debugger;

    });
    /*
        ////值写死  后面调试
        a01Data=[
            ["CurrentS", 0.0],
            ["S", 36000.0],
            ["CurrentL", 0.0],
            ["L", 15000.0],
            ["CurrentU", 0.0],
            ["U", 15000.0],
            ["CurrentR", 0.0],
            ["R", 15000.0],
            ["CurrentB", 0.0],
            ["B", 15000.0],
            ["CurrentT", 0.0],
            ["T", 15000.0]
        ];
        showEchart05(a01Data);

    */
}


function showEchart05(letType, a01Data) {
    //return;
    debugger;
    var myChart = echarts.init(document.getElementById('mainEchart05'));
    //var myArray=new Array()
    ///区域5：R02和R10是七轴机器人，所以需要显示7个，SLURBT＋E，NC是3轴，显示SLU【优先级低】
    var varadddata = new Array();
    let letXList = [];
    if (letType == 'NC') {
        varadddata[0] = a01Data[1][1] - a01Data[0][1];
        varadddata[1] = a01Data[3][1] - a01Data[2][1];
        varadddata[2] = a01Data[5][1] - a01Data[4][1];
        letXList = ["S", "L", "U"];
    } else if (letType == 'R02') {
        varadddata[0] = a01Data[1][1] - a01Data[0][1];
        varadddata[1] = a01Data[3][1] - a01Data[2][1];
        varadddata[2] = a01Data[5][1] - a01Data[4][1];
        varadddata[3] = a01Data[7][1] - a01Data[6][1];
        varadddata[4] = a01Data[9][1] - a01Data[8][1];
        varadddata[5] = a01Data[11][1] - a01Data[10][1];
        varadddata[6] = a01Data[13][1] - a01Data[12][1];
        letXList = ["S", "L", "U", "R", "B", "T", "E"];
    } else if (letType == 'R08') {
        //debugger;
        //区域5的设备寿命柱状图，读不到数据的轴，显示为36000
       /// 比如：R08是3轴SLU，SL有数据，U没有数据，那么U的数据就按36000显示
       // R10是7轴SLURBTE，T轴没有数据，那么T的数据就按36000显示【需要你确认一下T轴是否有数据，客户说不确定有没有】

        if ( a01Data.length>6){
            varadddata[0] = a01Data[1][1] - a01Data[0][1];
            varadddata[1] = a01Data[3][1] - a01Data[2][1];
            varadddata[2] = a01Data[5][1] - a01Data[4][1];
        }
        else
        {
            varadddata[0] = 36000;
            varadddata[1] = 36000;
            varadddata[2] = 36000;
        }
       //debugger;
        letXList = ["S", "L", "U"];
    } else if (letType == 'R10') {
        varadddata[0] = a01Data[1][1] - a01Data[0][1];
        varadddata[1] = a01Data[3][1] - a01Data[2][1];
        varadddata[2] = a01Data[5][1] - a01Data[4][1];
        varadddata[3] = a01Data[7][1] - a01Data[6][1];
        varadddata[4] = a01Data[9][1] - a01Data[8][1];
        varadddata[5] = 36000;
        varadddata[6] = a01Data[11][1] - a01Data[10][1];
        letXList = ["S", "L", "U", "R", "B", "T", "E"];
    } else if (letType == 'R09' || letType == 'R03') {
        varadddata[0] = a01Data[1][1] - a01Data[0][1];
        varadddata[1] = a01Data[3][1] - a01Data[2][1];
        varadddata[2] = a01Data[5][1] - a01Data[4][1];
        varadddata[3] = a01Data[7][1] - a01Data[6][1];
        varadddata[4] = a01Data[9][1] - a01Data[8][1];
        varadddata[5] = 360000;
        letXList = ["S", "L", "U", "R", "B", "T"];
    } else {
        varadddata[0] = a01Data[1][1] - a01Data[0][1];
        varadddata[1] = a01Data[3][1] - a01Data[2][1];
        varadddata[2] = a01Data[5][1] - a01Data[4][1];
        varadddata[3] = a01Data[7][1] - a01Data[6][1];
        varadddata[4] = a01Data[9][1] - a01Data[8][1];
        varadddata[5] = a01Data[11][1] - a01Data[10][1];
        letXList = ["S", "L", "U", "R", "B", "T"];
    }

    //debugger;
    // 指定图表的配置项和数据
    var option = {
        title: {
            text: ''
        },
        tooltip: {},
        legend: {
            data: ['']
        },
        xAxis: {
            show: true,
            data: letXList,
            show: true,
            axisLine: {
                show: false,
                lineStyle: {
                    color: 'white', width: 0, fontSize: 10,

                }
            },
            axisLabel: {
                show: true,
                textStyle: {
                    fontSize: '15',
                    color: 'white'
                }
            }
        },
        yAxis: {
            show: false,
            max: 36000,
            axisLine: {
                show: false,
                lineStyle: {
                    color: '#ff0000', width: 0, fontSize: 10,

                }
            },
            axisLabel: {
                show: true,
                textStyle: {
                    fontSize: '15',
                    color: '#ffffff'
                }
            }
        },
        series: [{
            name: '',
            type: 'bar',
            barWidth: 20,
            barGap: '190%',/*多个并排柱子设置柱子之间的间距*/
            barCategoryGap: '190%',/*多个并排柱子设置柱子之间的间距*/
            data: varadddata,
            // 此系列自己的调色盘。
            color: ['#92d14f', '#759aa0']
        }]
    };

    // 使用刚指定的配置项和数据显示图表。
    myChart.setOption(option);

}
