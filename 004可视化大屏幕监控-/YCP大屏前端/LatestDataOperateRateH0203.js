function LatestDataOperateRateH02() {  ////区域2 采集接口
    //debugger;
    //return;



    let timestamp2End = (new Date()).valueOf();


    var date = new Date(); //获取一个时间对象
    let y = date.getFullYear();
    let m=date.getMonth();
    let d = date.getDate();


    let timestamp2Start = (new Date(y,m,d)).valueOf();//今天0点开始的数据
    //debugger;

////"http://" + window.location.host + "/YCPApp/api/v2/"
    let ajaxUrl01 = basicUrl +
        "factories/" + "YASKAWA" +
        "/lines/" + "EXPO2020" +
        "/cells/" + "SPOT" +
        "/controllers/" + "R06-GP280L" +
        "/apps/LatestData/analysis/search";
    ajaxUrl01=basicUrl+"factories/" + "YASKAWA" + "/lines/" + "EXPO2020" + "/cells/" + "SPOT" + "/controllers/" + "R06-GP280L" + "/data/search";

    let dev_body01 = {
        "Item": ["M98","M97","Run","TimeStamp"],
        "N": 20000,
        "Size": 104857600,
        "Timeout": 2147483647,
        "Query": "(TimeStamp >=" + timestamp2Start + ") AND (TimeStamp <=" + timestamp2End + ")",
        "Sort": [{
            "Target": "TimeStamp",
            "Ascending": false
        }]
    };

    /*let ajaxUrl02 = basicUrl +
        "factories/" + "YASKAWA" +
        "/lines/" + "EXPO2020" +
        "/cells/" + "SPOT" +
        "/controllers/" + "R06-GP280L" +
        "/apps/LatestData/analysis/search";
    let dev_body02 = {
        "Item": ["M97"],
        "N": 20000,
        "Size": 104857600,
        "Timeout": 2147483647,
        "Query": "(TimeStamp >=" + timestamp2Start + ") AND (TimeStamp <=" + timestamp2End + ")",
        "Sort": [{
            "Target": "TimeStamp",
            "Ascending": true
        }]
    };
*/

    /*  YASKAWA	EXPO2020	SPOT	R06-GP280L	apps/LatestData/analysis/search	I98
      YASKAWA	EXPO2020	SPOT	R06-GP280L	apps/LatestData/analysis/search	I97
      YASKAWA	EXPO2020	SPOT	R06-GP280L	apps/RC.OperateRateH/analysis/search	Run
  */


    $.when($.ajax({
        type: "post",
        url: ajaxUrl01,
        async: true,
        cache: false,
        data: JSON.stringify(dev_body01),
        contentType: "application/json",
        dataType: "json",
        headers: {
            "YCP-API-TOKEN": servertoken
        }
    })).done(function (a01) {
        debugger;
        let destiona=0;let shijidestiona=0;
        //for(let i=0;i<a01.Data.length;i++)
        //{
            //shijidestiona+=a01.Data[i].M97;
            //destiona+=a01.tData[i].M98;let
        //}var
        //debugger;
        let varLetUse=0;
        if (a01.Data.length>0){
            shijidestiona=a01.Data[0].M97;
            destiona=a01.Data[0].M98;
            if (shijidestiona>destiona){
                destiona=shijidestiona;
            }
            varLetUse=shijidestiona*100.0/destiona;
        }
        else
        {
            //destiona=1400;
            //shijidestiona=500;
            //varLetUse=shijidestiona*100.0/destiona;
        }
        functionshow0401Echart(varLetUse);
        $("#mainEchart0401Text").text(shijidestiona);
        $("#mainEchart0401MinText").text(0);
        $("#mainEchart0401MaxText").text(destiona);

        //mainEchart0401MinText

    });
}


function LatestDataOperateRateH03() {
    //debugger;
    //return;


    let timestamp2End = (new Date()).valueOf();
    let timestamp2Start = new Date().setMonth((new Date().getMonth() - 1)).valueOf();//一个月



    let ajaxUrl03 = basicUrl +
        "factories/" + "YASKAWA" +
        "/lines/" + "EXPO2020" +
        "/cells/" + "SPOT" +
        "/controllers/" + "R06-GP280L" +
        "/apps/RC.OperateRateH/analysis/search";
    let dev_body03 = {
        "Item": ["Run","TimeStamp"],
        "N": 20000,
        "Size": 104857600,
        "Timeout": 2147483647,
        "Query": "(TimeStamp >=" + timestamp2Start + ") AND (TimeStamp <=" + timestamp2End + ")",
        "Sort": [{
            "Target": "TimeStamp",
            "Ascending": false
        }]
    };

    /*  YASKAWA	EXPO2020	SPOT	R06-GP280L	apps/LatestData/analysis/search	I98
      YASKAWA	EXPO2020	SPOT	R06-GP280L	apps/LatestData/analysis/search	I97
      YASKAWA	EXPO2020	SPOT	R06-GP280L	apps/RC.OperateRateH/analysis/search	Run
  */


    $.when($.ajax({
        type: "post",
        url: ajaxUrl03,
        async: true,
        cache: false,
        data: JSON.stringify(dev_body03),
        contentType: "application/json",
        dataType: "json",
        headers: {
            "YCP-API-TOKEN": servertoken
        }
    })).done(function (a03 ) {
        //debugger;
        let legtGetRun=(a03.Data[0]["Run"]+0).toFixed(0);
        if (legtGetRun<30){
            legtGetRun=30;
        }

        //var show0401Echart = fullClose(0, 100);
        functionshow0402Echart(legtGetRun);
        $("#mainEchart0402Text").text(legtGetRun + "%");
        //debugger;
    });
}



function functionshow0401Echart(currentvalue) {
    //return;
    //debugger;
    let current = currentvalue * 1.8;// 当前用量
    let chart0401background = echarts.init(document.getElementById('mainEchart0401background'));


    current = currentvalue * 1.8;// 当前用量
    //let all = 100;// 总量
    let option0401background = {


        polar: {
            radius: ['68%', '90%'],
            center: ['50%', '50%'],
        },

        angleAxis: {
            max: 360,
            show: false,
        },
        radiusAxis: {
            type: 'category',
            show: true,
            axisLabel: {
                show: false,
            },
        },
        series: [
            {
                name: '',
                type: 'bar',
                roundCap: true,
                barWidth: 60,
                showBackground: false,
                backgroundStyle: {
                    color: 'rgba(66, 66, 66, .3)',
                },
                data: [180],
                coordinateSystem: 'polar',

                itemStyle: {
                    normal: {
                        color: new echarts.graphic.LinearGradient(0, 1, 0, 0, [{
                            offset: 0,
                            color: '#ffffff'
                        }, {
                            offset: 1,
                            color: '#ffffff'
                        }]),
                    }
                }

            },


        ]
    };

    chart0401background.setOption(option0401background);

    let chart0401 = echarts.init(document.getElementById('mainEchart0401'));
    //let all = 100;// 总量
    let option0401 = {
        polar: {
            radius: ['70%', '90%'],
            center: ['50%', '50%'],
        },

        angleAxis: {
            max: 360,
            show: false,
        },
        radiusAxis: {
            type: 'category',
            show: true,
            axisLabel: {
                show: false,
            },
        },
        series: [
            {
                name: '',
                type: 'bar',
                roundCap: true,
                barWidth: 60,
                showBackground: false,
                backgroundStyle: {
                    color: 'rgba(66, 66, 66, .3)',
                },
                data: [current],
                coordinateSystem: 'polar',

                itemStyle: {
                    normal: {
                        color: new echarts.graphic.LinearGradient(0, 1, 0, 0, [{
                            offset: 0,
                            color: '#4cfffe'
                        }, {
                            offset: 1,
                            color: '#4cfffe'
                        }]),
                    }
                }

            },


        ]
    };
    chart0401.setOption(option0401);
}


function functionshow0402Echart(currentvalue) {

    let chart0402 = echarts.init(document.getElementById('mainEchart0402'));
    let current = currentvalue;// 当前用量
    let all = 100 - current;// 总量
    let option0402 = {
        series: [
            {
                type: 'pie',
                label: {
                    show: false
                },
                center: ['50%', '35%'],
                radius: ['50%', '80%'],
                startAngle: 50,
                data: [
                    {
                        name: '用量',
                        value: current,
                        itemStyle: {
                            color: '#a8d288'
                        }
                    },
                    {
                        name: 'bottom',
                        value: all,
                        itemStyle: {
                            color: 'transparent'
                        }
                    },
                ]
            }
        ]
    };
    chart0402.setOption(option0402);
}

