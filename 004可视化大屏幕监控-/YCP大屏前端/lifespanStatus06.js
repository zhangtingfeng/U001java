function LifeSpanmainEchartStatus06(letdddddText) {
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
    //let timestamp2Start = new Date().setMonth((new Date().getMonth() - 100)).valueOf();//一个月

    let date = new Date(); //获取一个时间对象
    let y = date.getFullYear();
    let m = date.getMonth();
    let d = date.getDate();
    let timestamp2Start = (new Date(y, m, d)).valueOf();//今天0点开始的数据


///取10个小时的值，要画折线图，要放大缩小。最小的10个小时。放大是一个小时的折线。取整点值。找一个。接口通  无数据返回。
////"http://" + window.location.host + "/YCPApp/api/v2/"
    let ajaxUrl = basicUrl +
        "factories/" + "YASKAWA" +
        "/lines/" + data0[1] +
        "/cells/" + data0[2] +
        "/controllers/" + data0[3] +
        "/apps/RC.OperateRateH/analysis/search";
    let dev_body = {
        "Item": ["Alm", "Run", "Idl", "EngySv", "Dscnct", "TimeStamp"],
        "N": 20000,
        "Size": 104857600,
        "Timeout": 2147483647,
        "Query": "(TimeStamp >=" + timestamp2Start + ") AND (TimeStamp <=" + timestamp2End + ")",
        "Sort": [{
            "Target": "TimeStamp",
            "Ascending": true
        }]
    };
    let varZheXianArrayToday = new Array();
///8-9-10-11-12-13-14-15-16-17
    let thisNullList=new Array();
    for(let i=0;i<10;i++){
        thisNullList[i]=null;
    }
    //debugger;
//        [null,null,null,null,null,null,null,null,null,null];
    for(let i=0;i<5;i++)
    {
        varZheXianArrayToday[i]=((thisNullList.concat()));
    }
    varZheXianArrayToday.push([8,9,10,11,12,13,14,15,16,17]);
    //thisNullList[0]=7;
    //let varZheXianArray=new Array();


   // debugger;
    $.ajax({
        type: "POST",
        url: ajaxUrl,
        async: true,
        cache: false,
        data: JSON.stringify(dev_body),
        contentType: "application/json",
        dataType: "json",
        headers: {
            "YCP-API-TOKEN": servertoken
        },
        success: function (ink) {
            //let varZheXianArray = new Array();
            //varZheXianArray[0] = [];
            ///varZheXianArray[1] = [];
            ///varZheXianArray[2] = [];
            //varZheXianArray[3] = [];
            //varZheXianArray[4] = [];
            //varZheXianArray[5] = [];
            for (let i = 0; i < ink.Data.length; i++) {
                let dateHourme = new Date(parseInt(ink.Data[i]["TimeStamp"]));
                let letCurHour=dateHourme.getHours();
                let letPos=-1;
                for (let j = 0; j < varZheXianArrayToday[varZheXianArrayToday.length-1].length; j++) {
                    if (letCurHour==varZheXianArrayToday[varZheXianArrayToday.length-1][j]){
                        letPos=j;
                        break;
                    }
                }

                //debugger;
                ///varZheXianArray[5].push(dateHourme.getHours());



                varZheXianArrayToday[0][letPos]=(ink.Data[i]["Alm"]);
                varZheXianArrayToday[1][letPos]=(ink.Data[i]["Run"]);
                varZheXianArrayToday[2][letPos]=(ink.Data[i]["Idl"]);
                varZheXianArrayToday[3][letPos]=(ink.Data[i]["EngySv"]);
                varZheXianArrayToday[4][letPos]=(ink.Data[i]["Dscnct"]);


            }

            //varZheXianArray[0][0]=90;
///"Alm", "Run", "Idl", "EngySv", "Dscnct","TimeStamp"
            debugger;
            showEchart06(varZheXianArrayToday);


            /*
                        Alm: 0
                        Dscnct: 0
                        EngySv: 97.43589743589743
                        Idl: 2.564102564102564
                        Run: 0*/
            //if (callback) {
            //    callback(varData);
            //}
            //statusJudge(statusArr);
        },
        error: function (e) {
            //debugger;
            console.log("Exception in getLineInfo()");
            console.log(e);
            console.log("=========");
        }
    });


}


function showEchart06(varZheXianArray) {
    var myChart = echarts.init(document.getElementById('mainEchart0502'));
    //debugger;
    //var myArray=new Array()
    /* var data1 = new Array();
     for (var i = 0; i < 10; i++) {
         data1[i] = fullClose(80, 100);
     }
     var data2 = new Array();
     for (var i = 0; i < 10; i++) {
         data2[i] = fullClose(30, 70);
     }
     var data3 = new Array();
     for (var i = 0; i < 10; i++) {
         data3[i] = fullClose(0, 20);
     }*/
//"Alm", "Run", "Idl", "EngySv", "Dscnct"
    //varZheXianArray[2][0] = null;
    option = {
        title: {
            text: '折线图堆叠', show: false
        },
        tooltip: {
            trigger: 'axis', show: false
        },
        legend: {
            data: ['01', '02', '03', '04', '05']
            , show: false
        },
        grid: {
            left: '3%',
            right: '4%',
            bottom: '3%',
            containLabel: true,
            show: false
        },
        toolbox: {
            feature: {
                saveAsImage: {}
            },
            show: false
        },
        xAxis: {
            type: 'category',
            boundaryGap: false,
            data: varZheXianArray[5],
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
            },


        },
        yAxis: {
            show: false,
            type: 'value',
            max: 100
        },
        dataZoom: [{
            type: "inside"         //详细配置可见echarts官网  ["Alm", "Run", "Idl", "EngySv", "Dscnct", "TimeStamp"],
        }],
        series: [
            {
                name: 'Alm',
                type: 'line',
                stack: 'Alm',
                data: varZheXianArray[0],
                lineStyle: {
                    color: '#ff0000', width: 4
                }
            },
            {
                name: 'Run',
                type: 'line',
                stack: 'Run',
                data: varZheXianArray[1],
                lineStyle: {
                    color: '#70ad47', width: 4
                }
            },
            {
                name: 'Idl',
                type: 'line',
                stack: 'Idl',
                data: varZheXianArray[2],
                lineStyle: {
                    color: '#ffff00', width: 4
                }
            },
            {
                name: 'EngySv',
                type: 'line',
                stack: 'EngySv',
                data: varZheXianArray[3],
                lineStyle: {
                    color: '#de00ff', width: 4
                }
            },
            {
                name: 'Dscnct',
                type: 'line',
                stack: 'Dscnct',
                data: varZheXianArray[4],
                lineStyle: {
                    color: '#6c716d', width: 4
                }
            }
        ]
    };


    // 使用刚指定的配置项和数据显示图表。
    myChart.setOption(option);

}


/*
function showEchart05020000(varDataList) {
    var myChart = echarts.init(document.getElementById('mainEchart0502'));
    //var myArray=new Array()
    /*var data1 = new Array();
    for (var i = 0; i < 10; i++) {
        data1[i] = fullClose(80, 100);
    }
    var data2 = new Array();
    for (var i = 0; i < 10; i++) {
        data2[i] = fullClose(30, 70);
    }
    var data3 = new Array();
    for (var i = 0; i < 10; i++) {
        data3[i] = fullClose(0, 20);
    }
    option = {
        title: {
            text: '图表信息', show: false
        },
        tooltip: {
            trigger: 'axis', show: true
        },
        legend: {
            data: ['异常报警中', '运行中', '待机中', '节能中', '脱机中']
            , show: false
        },
        grid: {
            left: '3%',
            right: '4%',
            bottom: '3%',
            containLabel: false,
            show: false
        },
        toolbox: {
            feature: {
                saveAsImage: {}
            },
            show: false
        },
        xAxis: {
            type: 'category',
            boundaryGap: false,
            data: varDataList[5],
            show: false,
            axisLine: {
                show: false,
                lineStyle: {
                    color: '#ff0000', width: 0, fontSize: 10,

                }
            },
            axisLabel: {
                show: false,
                textStyle: {
                    fontSize: '15',
                    color: '#ff0000'
                }
            }
        },
        yAxis: {
            max:100,
            show: false,
            type: 'value',
            position:'left',
            axisLine: {
                formatter:'{value}% ',
                lineStyle: {
                    color: '#ff0000', width: 0, fontSize: 10,

                }
            },
        },
        dataZoom: [{
            type: "inside"         //详细配置可见echarts官网
        }],
        series: [
            {
                name: '异常报警中',
                type: 'line',
                stack: '异常报警中',
                data: varDataList[0],
                lineStyle: {
                    color: '#70ad47', width: 4
                }
            },
            {
                name: '运行中',
                type: 'line',
                stack: '运行中',
                data: varDataList[1],
                lineStyle: {
                    color: '#ffff00', width: 4
                }
            },
            {
                name: '待机中',
                type: 'line',
                stack: '待机中',
                data: varDataList[2],
                lineStyle: {
                    color: '#f40001', width: 4
                }
            },
            {
                name: '节能中',
                type: 'line',
                stack: '节能中',
                data: varDataList[3],
                lineStyle: {
                    color: '#f00001', width: 4
                }
            },
            {
                name: '脱机中',
                type: 'line',
                stack: '脱机中',
                data: varDataList[4],
                lineStyle: {
                    color: '#a40001', width: 4
                }
            }
        ]
    };


    // 使用刚指定的配置项和数据显示图表。
    myChart.setOption(option);

}

*/

