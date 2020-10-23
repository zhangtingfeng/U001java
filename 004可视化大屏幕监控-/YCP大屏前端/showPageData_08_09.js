function GetProdunctionHistory08() {///获取生产历史纪录
    //return;
    //debugger;///http://localhost:8080/YCPApp/api/v2/factories/A/lines/B/cells/C/controllers/D/apps/0022000000000000/analysis/search
    ///http://localhost:8084/?controllerName=D&lineName=B&cellName=C&factoryName=A
    //debugger;
    let timestamp2End = (new Date()).valueOf();
    let timestamp2Start = new Date().setMonth((new Date().getMonth() - 10)).valueOf();//一个月
///日期的开时间Data  产品名固定大写AProduct 日期写当天日期Time 计划值只能M98 Real和M97  Plan 比率自己计算  每次变化追加一行 一秒一次 刷新周期5分钟
   //日期   时间  产品名 计划 实际 比率
    ///YASKAWA	EXPO2025	SPOT	R06-GP280L	apps/LatestData/analysis/search	I97
    var ajaxUrl = basicUrl +
        "factories/" + "YASKAWA" +
        "/lines/" + "EXPO2020" +
        "/cells/" + "SPOT" +
        "/controllers/" + "R06-GP280L"+
        "/data/search";
    //ajaxUrl="http://localhost:8084/YCPApp/api/v2/factories/" + "YASKAWA" + "/lines/" + "EXPO2020" + "/cells/" + "SPOT" + "/controllers/" + "R06-GP280L" + "/data/search";

    var dev_body = {
        "Item": ["M97","M98", "TimeStamp"],
        "N": 20000,
        "Size": 104857600,
        "Timeout": 2147483647,
        "Query": "(TimeStamp >=" + timestamp2Start + ") AND (TimeStamp <=" + timestamp2End + ")",
        "Sort": [{
            "Target": "TimeStamp",
            "Ascending": false
        }]
    }


    //var ajaxUrl = basicUrl + "factories/A/lines/B/cells/C/controllers/D/apps/0022000000000000/analysis/search";
    //debugger;

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
        success: function (inkData) {
            //debugger;
            let letArray=new Array();
            letArray.push(inkData.Data[0]);
            for (let i = 1; i < inkData.Data.length; i++) {
                let M97 = getShowValue(inkData.Data[i].M97);
                if (letArray[letArray.length-1].M97==M97){
                    letArray[letArray.length-1]=inkData.Data[i];
                }
                else
                {
                    letArray.push(inkData.Data[i]);
                }
            }
            let minvalue = letArray.length > 20 ? 20 : letArray.length;


///<span>Date</span><span>Time</span><span>Product</span><span>Plan</span><span>Real</span><span>Rate</span>
            //return;
            //temp = document.getElementById('Ulbak07backimg');
            //lis = temp.getElementsByTagName('li');
            $("#Ulbak07backimg").find("li").remove();
            //if (lis.length > 8) {
            //    $("#Ulbak10backimg").find("li").remove();
            //}

            //console.log(ink.Data);  目前只要三条数据
            //debugger;
            for (let i = 0; i < minvalue; i++) {
                let kinTimeStamp = getShowValue(letArray[i].TimeStamp);
                let M97 = getShowValue(letArray[i].M97);
                let M98 = getShowValue(letArray[i].M98);

                let kinTimeStampDate = formateDate(kinTimeStamp);
                let kinTimeStampTime = formateTime(kinTimeStamp);
                letArray[i]["Rate"]=0;
                if (M97>0 && M98>0)
                {
                    letArray[i]["Rate"]=(M97*100.0/M98).toFixed(2)+'%';
                }
                var elem_li = document.createElement('li'); // 生成一bai个 li元素
                elem_li.innerHTML = '<span>' + kinTimeStampDate + '</span><span>' + kinTimeStampTime + '</span><span>A</span><span>' + M98 + '</span><span>' + M97 + '</span><span>' + letArray[i]["Rate"] + '</span>';
                document.getElementById('Ulbak07backimg').appendChild(elem_li); // 添加到UL中去

            }
            //console.log(statusArr);
            //statusJudge(statusArr);
        },
        error: function (e) {
            //debugger;
           ///console.log("Exception in getLineInfo()");
            //console.log(e);
            //console.log("=========");
        }
    })
    /*
        $.ajax({
            url: ajaxUrl,
            contentType: "text/plain",
            data:  JSON.stringify(dev_body),
            type: "POST",
            dataType: "json",
            timeout: 1000,
            cache: false,
            async: false,
            success: function (data) {
                servertoken = "" + data.Token;
                console.log(servertoken)
                return servertoken;
            },
            error: function (e) {
                console.log(e);
            }
        });*/
}


function GetAlarmHistory09() {///获取AlarmHistory
    //debugger;
    //return;

    var timestamp2End = (new Date()).valueOf();
    var timestamp2Start = new Date().setMonth((new Date().getMonth() - 10)).valueOf();//一个月


    //debugger;
    var varajaxUrlArray = new Array();
    var dev_bodyArray = new Array();
    var dev_bodyArrayDevice = new Array();

    for (var i = 0; i < 10; i++) {///最后一个NC不需要
        var varpos = globalButtonList[i];
        // debugger;
        var ajaxUrl = basicUrl +
            "factories/" + "YASKAWA" +
            "/lines/" + varpos[0] +
            "/cells/" + varpos[1] +
            "/controllers/" + varpos[2] +
            "/apps/RC.AlarmHistory/analysis/search";
        varajaxUrlArray[i] = ajaxUrl;


        var dev_body = {
            "Item": ["AlmCd", "AlmCtgry", "AlmNm", "RbtInf", "Tsk1JbNm", "TimeStamp"],
            "N": 20000,
            "Size": 104857600,
            "Timeout": 2147483647,
            "Query": "(TimeStamp >=" + timestamp2Start + ") AND (TimeStamp <=" + timestamp2End + ")",
            "Sort": [{
                "Target": "TimeStamp",
                "Ascending": false
            }]
        };
        dev_bodyArray[i] = dev_body;

        dev_bodyArrayDevice[i]=varpos[2];
    }

    $.when($.ajax({
        type: "post",
        url: varajaxUrlArray[0],
        async: true,
        cache: false,
        data: JSON.stringify(dev_bodyArray[0]),
        contentType: "application/json",
        dataType: "json",
        headers: {
            "YCP-API-TOKEN": servertoken
        }
    }), $.ajax({
        type: "post",
        url: varajaxUrlArray[1],
        async: true,
        cache: false,
        data: JSON.stringify(dev_bodyArray[1]),
        contentType: "application/json",
        dataType: "json",
        headers: {
            "YCP-API-TOKEN": servertoken
        }
    }), $.ajax({
        type: "post",
        url: varajaxUrlArray[2],
        async: true,
        cache: false,
        data: JSON.stringify(dev_bodyArray[2]),
        contentType: "application/json",
        dataType: "json",
        headers: {
            "YCP-API-TOKEN": servertoken
        }
    }), $.ajax({
        type: "post",
        url: varajaxUrlArray[3],
        async: true,
        cache: false,
        data: JSON.stringify(dev_bodyArray[3]),
        contentType: "application/json",
        dataType: "json",
        headers: {
            "YCP-API-TOKEN": servertoken
        }
    }), $.ajax({
        type: "post",
        url: varajaxUrlArray[4],
        async: true,
        cache: false,
        data: JSON.stringify(dev_bodyArray[4]),
        contentType: "application/json",
        dataType: "json",
        headers: {
            "YCP-API-TOKEN": servertoken
        }
    }), $.ajax({
        type: "post",
        url: varajaxUrlArray[5],
        async: true,
        cache: false,
        data: JSON.stringify(dev_bodyArray[5]),
        contentType: "application/json",
        dataType: "json",
        headers: {
            "YCP-API-TOKEN": servertoken
        }
    }), $.ajax({
        type: "post",
        url: varajaxUrlArray[6],
        async: true,
        cache: false,
        data: JSON.stringify(dev_bodyArray[6]),
        contentType: "application/json",
        dataType: "json",
        headers: {
            "YCP-API-TOKEN": servertoken
        }
    }), $.ajax({
        type: "post",
        url: varajaxUrlArray[7],
        async: true,
        cache: false,
        data: JSON.stringify(dev_bodyArray[7]),
        contentType: "application/json",
        dataType: "json",
        headers: {
            "YCP-API-TOKEN": servertoken
        }
    }), $.ajax({
        type: "post",
        url: varajaxUrlArray[8],
        async: true,
        cache: false,
        data: JSON.stringify(dev_bodyArray[8]),
        contentType: "application/json",
        dataType: "json",
        headers: {
            "YCP-API-TOKEN": servertoken
        }
    }), $.ajax({
        type: "post",
        url: varajaxUrlArray[9],
        async: true,
        cache: false,
        data: JSON.stringify(dev_bodyArray[9]),
        contentType: "application/json",
        dataType: "json",
        headers: {
            "YCP-API-TOKEN": servertoken
        }
    })).done(function (a00, a01, a02, a03, a04, a05, a06, a07, a08, a09) {
        //debugger;
        a00=GiveSetDevice(a00[0].Data,dev_bodyArrayDevice[0]);
        a01=GiveSetDevice(a01[0].Data,dev_bodyArrayDevice[1]);
        a02=GiveSetDevice(a02[0].Data,dev_bodyArrayDevice[2]);
        a03=GiveSetDevice(a03[0].Data,dev_bodyArrayDevice[3]);
        a04=GiveSetDevice(a04[0].Data,dev_bodyArrayDevice[4]);
        a05=GiveSetDevice(a05[0].Data,dev_bodyArrayDevice[5]);
        a06=GiveSetDevice(a06[0].Data,dev_bodyArrayDevice[6]);
        a07=GiveSetDevice(a07[0].Data,dev_bodyArrayDevice[7]);
        a08=GiveSetDevice(a08[0].Data,dev_bodyArrayDevice[8]);
        a09=GiveSetDevice(a09[0].Data,dev_bodyArrayDevice[9]);



        let c = a00.concat(a01); //c=[1,2,3,4,5,6];
        c = c.concat(a02); //c=[1,2,3,4,5,6];
        c = c.concat(a03); //c=[1,2,3,4,5,6];
        c = c.concat(a04); //c=[1,2,3,4,5,6];
        c = c.concat(a05); //c=[1,2,3,4,5,6];
        c = c.concat(a06); //c=[1,2,3,4,5,6];
        c = c.concat(a07); //c=[1,2,3,4,5,6];
        c = c.concat(a08); //c=[1,2,3,4,5,6];
        c = c.concat(a09); //c=[1,2,3,4,5,6];

        var sortvarGlobalGetAlarmHistory = c.sort(sortListByItem("TimeStamp"));
        var minvalue = sortvarGlobalGetAlarmHistory.length > 20 ? 20 : sortvarGlobalGetAlarmHistory.length;
        $("#Ulbak08backimg").find("li").remove();
        //debugger;
        for (var i = 0; i < minvalue; i++) {
            //debugger;

            var kinTimeStamp = getShowValue(sortvarGlobalGetAlarmHistory[i].TimeStamp);
            var kinTimeStamp_Result = getShowValue(sortvarGlobalGetAlarmHistory[i].TimeStamp_Result);
            var device = getShowValue(sortvarGlobalGetAlarmHistory[i].device);
            var AlmCtgry = getShowValue(sortvarGlobalGetAlarmHistory[i].AlmCtgry);


            var varTextAlmCtgry = "";
            if (AlmCtgry == 1) {
                varTextAlmCtgry = "重度报警";
            } else if (AlmCtgry == 2) {
                varTextAlmCtgry = "中度报警";
            } else if (AlmCtgry == 3) {
                varTextAlmCtgry = "轻报警";
            }

            kinTimeStamp = formateDate(kinTimeStamp);
            let endkinTimeStamp = formateTime(kinTimeStamp);
            let ararmName=sortvarGlobalGetAlarmHistory[i].AlmNm;
            let ararmAlmCd=sortvarGlobalGetAlarmHistory[i].AlmCd;

            var elem_li = document.createElement('li'); // 生成一bai个 li元素
            elem_li.innerHTML = '<span>' + kinTimeStamp + '</span><span>' + endkinTimeStamp + '</span><span>' + ararmName + '</span><span>' + ararmAlmCd + '</span><span>' + device + '</span>';
            document.getElementById('Ulbak08backimg').appendChild(elem_li); // 添加到UL中去

        }

    });

    /*
        //debugger;
        //GetAlarmHistoryPOSTajaxUrl(ajaxUrl, dev_body, varpos[2]);
        //debugger;
        if (varGlobalGetAlarmHistory && varGlobalGetAlarmHistory.length > 0) {

            //console.error(varGlobalGetAlarmHistory);
            var varStringvarGlobalGetAlarmHistory = JSON.stringify(varGlobalGetAlarmHistory);
            var sortvarGlobalGetAlarmHistory = varGlobalGetAlarmHistory.sort(sortListByItem("TimeStamp"));
            //debugger;
            var minvalue = sortvarGlobalGetAlarmHistory.length > 20 ? 20 : sortvarGlobalGetAlarmHistory.length;
            $("#Ulbak08backimg").find("li").remove();
            for (var i = 0; i < minvalue; i++) {
                //debugger;

                var kinTimeStamp = getShowValue(sortvarGlobalGetAlarmHistory[i].TimeStamp);
                var kinTimeStamp_Result = getShowValue(sortvarGlobalGetAlarmHistory[i].TimeStamp_Result);
                var device = getShowValue(sortvarGlobalGetAlarmHistory[i].device);
                var AlmCtgry = getShowValue(sortvarGlobalGetAlarmHistory[i].AlmCtgry);


                var varTextAlmCtgry = "";
                if (AlmCtgry == 1) {
                    varTextAlmCtgry = "重度报警";
                } else if (AlmCtgry == 2) {
                    varTextAlmCtgry = "中度报警";
                } else if (AlmCtgry == 3) {
                    varTextAlmCtgry = "轻报警";
                }

                kinTimeStamp = formateDate(kinTimeStamp);
                var endkinTimeStamp = formateTime(kinTimeStamp);


                var elem_li = document.createElement('li'); // 生成一bai个 li元素
                elem_li.innerHTML = '<span>' + kinTimeStamp + '</span><span>' + endkinTimeStamp + '</span><span>' + device + '</span><span>' + varTextAlmCtgry + '</span><span>' + AlmCtgry + '</span>';
                document.getElementById('Ulbak08backimg').appendChild(elem_li); // 添加到UL中去

            }
            varGlobalGetAlarmHistory = [];
        }
    }

    */
}


function GiveSetDevice(varData,argdevice){
    let  letArray=new Array();
    for (let k = 0; k < varData.length; k++) {
        //debugger;
        varData[k].device = argdevice;
        varData[k].meformateDate = formateDate(varData[k].TimeStamp);
        letArray.push(varData[k]);
    }
    return letArray;
}

/*
////  when
function GetAlarmHistoryPOSTajaxUrl(ajaxUrlarg, argdev_body, argdevice) {

    var varData = "";
    $.ajax({
        type: "POST",
        url: ajaxUrlarg,
        async: true,
        cache: false,
        data: JSON.stringify(argdev_body),
        contentType: "application/json",
        dataType: "json",
        headers: {
            "YCP-API-TOKEN": servertoken
        },
        success: function (ink) {
            varData = ink.Data;
            for (var k = 0; k < varData.length; k++) {
                //debugger;
                varData[k].device = argdevice;
                varData[k].meformateDate = formateDate(varData[k].TimeStamp);
                varGlobalGetAlarmHistory.push(varData[k]);
            }

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
    })
    return varData;
}

*/
