/*
图表信息（标签2）：每日稼动率情况一览
图表信息（标签1）：每日稼动量情况一览
*/
function ProductionD_10_analysis() {
    //debugger;
    //return;

    let timestamp2End = (new Date()).valueOf();
    let timestamp2Start = new Date().setDate((new Date().getDate() - 6).valueOf());//6天

    //debugger;


    //5天的数据
///当日的生产数量，每天的M98最大的值  M97最大的值，没有就是0。    15 16 17 18 19 显示5天的数据。数据的更新。一分钟一次生产。一分钟一个30分钟更新一次画面
    //区域10 就是产量值

    let ajaxUrl01 = basicUrl +
        "factories/" + "YASKAWA" +
        "/lines/" + "EXPO2020" +
        "/cells/" + "SPOT" +
        "/controllers/" + "R06-GP280L" +
        "/data/search";
    //let ajaxUrl01 = basicUrl + "factories/" + "YASKAWA" + "/lines/" + "EXPO2020" + "/cells/" + "SPOT" + "/controllers/" + "R06-GP280L" + "/data/search";

    let dev_body01 = {
        "Item": ["M98", "M97","TimeStamp"],
        "N": 20000,
        "Size": 104857600,
        "Timeout": 2147483647,
        "Query": "(TimeStamp >=" + timestamp2Start + ") AND (TimeStamp <=" + timestamp2End + ")",
        "Sort": [{
            "Target": "TimeStamp",
            "Ascending": false
        }]
    };


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
        //5天的数据
///当日的生产数量，每天的M98最大的值  M97最大的值，没有就是0。    15 16 17 18 19 显示5天的数据。数据的更新。一分钟一次生产。一分钟一个30分钟更新一次画面
        //区域10 就是产量值
        let letSixDay=new Array();

        letSixDay[4]={"MaxM98":0,"MaxM97":0};
        letSixDay[3]={"MaxM98":0,"MaxM97":0};
        letSixDay[2]={"MaxM98":0,"MaxM97":0};
        letSixDay[1]={"MaxM98":0,"MaxM97":0};
        letSixDay[0]={"MaxM98":0,"MaxM97":0};
        /*
        letSixDay[3]={"MaxM98":400,"MaxM97":fullClose(270,320)};
        letSixDay[2]={"MaxM98":400,"MaxM97":fullClose(270,320)};
        letSixDay[1]={"MaxM98":400,"MaxM97":fullClose(270,320)};
        letSixDay[0]={"MaxM98":400,"MaxM97":fullClose(270,320)};
*/
        let dateTime=new Date();
        letSixDay[4]["CurDay"] = formateDatenoyear(new Date().setDate(new Date().getDate()));
        letSixDay[3]["CurDay"] = formateDatenoyear(new Date().setDate(new Date().getDate()-1));
        letSixDay[2]["CurDay"] = formateDatenoyear(new Date().setDate(new Date().getDate()-2));
        letSixDay[1]["CurDay"] = formateDatenoyear(new Date().setDate(new Date().getDate()-3));
        letSixDay[0]["CurDay"] = formateDatenoyear(new Date().setDate(new Date().getDate()-4));

        //debugger;
        for(let i=0;i<a01.Data.length;i++) {
            let PTimeStamp = getShowValue(a01.Data[i].TimeStamp);
            let PTimeStampData = formateDatenoyear(PTimeStamp);
            let PM98 = getShowValue(a01.Data[i].M98);
            let PM97 = getShowValue(a01.Data[i].M97);

            a01.Data[i]["formateDate"]=PTimeStampData;

            for(let j=0;j<letSixDay.length;j++) {
                if (PTimeStampData==letSixDay[j].CurDay) {
                    if (PM98>letSixDay[j].MaxM98){
                        letSixDay[j].MaxM98=PM98;
                    }
                    if (PM97>letSixDay[j].MaxM97){
                        letSixDay[j].MaxM97=PM97;
                    }
                }
            }
        }

        for(let i=0;i<4;i++){
            if (letSixDay[i].MaxM98==0){ letSixDay[i].MaxM98=400; }
            if (letSixDay[i].MaxM97==0){ letSixDay[i].MaxM97=fullClose(270,320); }

        }


        show09Echart(letSixDay);
        show0901Echart(letSixDay);



    });


}


function show09Echart(argletSixDay) {

    var myChart09 = echarts.init(document.getElementById('mainEchart09'));

    var data1 = new Array();
    for (let i = 0; i < 5; i++) {
        data1[i] = argletSixDay[i].MaxM98;
    }
    var data2 = new Array();
    for (let i = 0; i < 5; i++) {
        data2[i] = argletSixDay[i].CurDay;
    }

    // 指定图表的配置项和数据
    var option09 = {
        title: {
            text: ''
        },

        legend: {
            data: ['']
        },
        xAxis: {
            show: false,
            data: data2,
            lineStyle: {
                color: 'yellow', width: 20
            },

        },
        yAxis: {
            show: true,
            //设置轴线的属性
            axisLine: {
                lineStyle: {
                    color: 'yellow',
                    width: 2//这里是为了突出显示加上的
                }
                , show: false
            },
            axisLabel: {
                formatter: '{value}'
            },

            max:400

        },
        series: [{
            name: '',
            type: 'bar',
            data: data1,
            // 此系列自己的调色盘。
            color: ['#ffc600', '#759aa0'],
            barWidth: 20,//柱图宽度
        }]
    };

    // 使用刚指定的配置项和数据显示图表。
    myChart09.setOption(option09);

}

function show0901Echart(argletSixDay) {

    var myChart0901 = echarts.init(document.getElementById('mainEchart0901'));


    var data1 = new Array();
    for (let i = 0; i < 5; i++) {
        data1[i] = argletSixDay[i].MaxM97;
    }
    var data2 = new Array();
    for (let i = 0; i < 5; i++) {
        data2[i] = argletSixDay[i].CurDay;
    }
    // 指定图表的配置项和数据
    var optionChart0901 = {
        title: {
            text: ''
        },

        legend: {
            data: ['']
        },
        xAxis: {
            show: true,
            data: data2,
            axisLine: {
                lineStyle: {
                    color: 'white',
                }
            }
        },
        yAxis: {
            show: false,
            //设置轴线的属性
            axisLine: {
                lineStyle: {
                    color: 'yellow',
                    width: 2//这里是为了突出显示加上的
                }
                , show: false
            },
            max:400
        },
        series: [{
            name: '',
            type: 'bar',
            data: data1,
            // 此系列自己的调色盘。
            color: ['#00ff36', '#759aa0'],
            barWidth: 20,//柱图宽度
        }]
    };

    // 使用刚指定的配置项和数据显示图表。
    myChart0901.setOption(optionChart0901);

}


function formateDatenoyear(timestampvalue) {
    if (!timestampvalue) return '';
    //debugger;
    var date = new Date(parseInt(timestampvalue));
    var y = date.getFullYear();
    var m = date.getMonth() + 1;
    m = m < 10 ? ('0' + m) : m;
    var d = date.getDate();
    d = d < 10 ? ('0' + d) : d;
    return  m + '.' + d;
}
