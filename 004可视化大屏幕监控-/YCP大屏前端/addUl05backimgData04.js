function addUl05backimgData04(vardddddText) {
    //debugger;

    document.getElementById('back05TextTitle').innerHTML = vardddddText;
    var data0 = new Array();
    globalButtonListStatusIIIII = 0;
    for (var i = 0; i < globalButtonList.length; i++) {
        if (globalButtonList[i][3] == vardddddText) {
            //debugger;
            data0[0] = 'YASKAWA';
            data0[1] = globalButtonList[i][0];
            data0[2] = globalButtonList[i][1];
            data0[3] = globalButtonList[i][2];
            data0[4] = "";///红绿灯状态
            data0[5] = globalButtonList[i][3];
            data0[6] = globalButtonList[i][6];


            globalButtonListStatusIIIII = i;////记录值  取红绿灯状态


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


    let ajaxUrl01 = basicUrl +
        "factories/" + "YASKAWA" +
        "/lines/" + data0[1] +
        "/cells/" + data0[2] +
        "/controllers/" + data0[3] +
        "/apps/RC.SystemStruct/analysis/search";

    let dev_body01 = {
        "Item": ["StrctrR1"],
        "N": 20000,
        "Size": 104857600,
        "Timeout": 2147483647,
        "Query": "(TimeStamp >=" + timestamp2Start + ") AND (TimeStamp <=" + timestamp2End + ")",
        "Sort": [{
            "Target": "TimeStamp",
            "Ascending": false
        }]
    };

    let ajaxUrl02 = basicUrl +
        "factories/" + "YASKAWA" +
        "/lines/" + data0[1] +
        "/cells/" + data0[2] +
        "/controllers/" + data0[3] +
        "/apps/RC.VersionInfo/analysis/search";

    let dev_body02 = {
        "Item": ["AppR1"],
        "N": 20000,
        "Size": 104857600,
        "Timeout": 2147483647,
        "Query": "(TimeStamp >=" + timestamp2Start + ") AND (TimeStamp <=" + timestamp2End + ")",
        "Sort": [{
            "Target": "TimeStamp",
            "Ascending": false
        }]
    };


    let ajaxUrl03 = basicUrl +
        "factories/" + "YASKAWA" +
        "/lines/" + data0[1] +
        "/cells/" + data0[2] +
        "/controllers/" + data0[3] +
        "/apps/RC.VersionInfo/analysis/search";

    let dev_body03 = {
        "Item": ["SysVer"],
        "N": 20000,
        "Size": 104857600,
        "Timeout": 2147483647,
        "Query": "(TimeStamp >=" + timestamp2Start + ") AND (TimeStamp <=" + timestamp2End + ")",
        "Sort": [{
            "Target": "TimeStamp",
            "Ascending": false
        }]
    };

    let ajaxUrl04 = basicUrl +
        "factories/" + "YASKAWA" +
        "/lines/" + data0[1] +
        "/cells/" + data0[2] +
        "/controllers/" + data0[3] +
        "/apps/RC.SystemTime/analysis/search";

    let dev_body04 = {
        "Item": ["CntrlPwrTm"],
        "N": 20000,
        "Size": 104857600,
        "Timeout": 2147483647,
        "Query": "(TimeStamp >=" + timestamp2Start + ") AND (TimeStamp <=" + timestamp2End + ")",
        "Sort": [{
            "Target": "TimeStamp",
            "Ascending": false
        }]
    };


    let ajaxUrl05 = basicUrl +
        "factories/" + "YASKAWA" +
        "/lines/" + data0[1] +
        "/cells/" + data0[2] +
        "/controllers/" + data0[3] +
        "/apps/RC.SystemTime/analysis/search";

    let dev_body05 = {
        "Item": ["EngyTm"],
        "N": 20000,
        "Size": 104857600,
        "Timeout": 2147483647,
        "Query": "(TimeStamp >=" + timestamp2Start + ") AND (TimeStamp <=" + timestamp2End + ")",
        "Sort": [{
            "Target": "TimeStamp",
            "Ascending": false
        }]
    };


    let ajaxUrl06 = basicUrl +
        "factories/" + "YASKAWA" +
        "/lines/" + data0[1] +
        "/cells/" + data0[2] +
        "/controllers/" + data0[3] +
        "/apps/RC.SystemTime/analysis/search";

    let dev_body06 = {
        "Item": ["OprtngTm"],
        "N": 20000,
        "Size": 104857600,
        "Timeout": 2147483647,
        "Query": "(TimeStamp >=" + timestamp2Start + ") AND (TimeStamp <=" + timestamp2End + ")",
        "Sort": [{
            "Target": "TimeStamp",
            "Ascending": false
        }]
    };


    let ajaxUrl07 = basicUrl +
        "factories/" + "YASKAWA" +
        "/lines/" + data0[1] +
        "/cells/" + data0[2] +
        "/controllers/" + data0[3] +
        "/apps/RC.OperateStatus/analysis/search";

    let dev_body07 = {
        "Item": ["Cycl"],
        "N": 20000,
        "Size": 104857600,
        "Timeout": 2147483647,
        "Query": "(TimeStamp >=" + timestamp2Start + ") AND (TimeStamp <=" + timestamp2End + ")",
        "Sort": [{
            "Target": "TimeStamp",
            "Ascending": false
        }]
    };
//debugger;
    $.when($.ajax({
        type: "POST",
        url: ajaxUrl01,
        async: true,
        cache: false,
        data: JSON.stringify(dev_body01),
        contentType: "application/json",
        dataType: "json",
        headers: {
            "YCP-API-TOKEN": servertoken
        }
    }), $.ajax({
        type: "POST",
        url: ajaxUrl02,
        async: true,
        cache: false,
        data: JSON.stringify(dev_body02),
        contentType: "application/json",
        dataType: "json",
        headers: {
            "YCP-API-TOKEN": servertoken
        }
    }), $.ajax({
        type: "POST",
        url: ajaxUrl03,
        async: true,
        cache: false,
        data: JSON.stringify(dev_body03),
        contentType: "application/json",
        dataType: "json",
        headers: {
            "YCP-API-TOKEN": servertoken
        }
    }), $.ajax({
        type: "POST",
        url: ajaxUrl04,
        async: true,
        cache: false,
        data: JSON.stringify(dev_body04),
        contentType: "application/json",
        dataType: "json",
        headers: {
            "YCP-API-TOKEN": servertoken
        }
    }), $.ajax({
        type: "POST",
        url: ajaxUrl05,
        async: true,
        cache: false,
        data: JSON.stringify(dev_body05),
        contentType: "application/json",
        dataType: "json",
        headers: {
            "YCP-API-TOKEN": servertoken
        }
    }), $.ajax({
        type: "POST",
        url: ajaxUrl06,
        async: true,
        cache: false,
        data: JSON.stringify(dev_body06),
        contentType: "application/json",
        dataType: "json",
        headers: {
            "YCP-API-TOKEN": servertoken
        }
    }), $.ajax({
        type: "POST",
        url: ajaxUrl07,
        async: true,
        cache: false,
        data: JSON.stringify(dev_body07),
        contentType: "application/json",
        dataType: "json",
        headers: {
            "YCP-API-TOKEN": servertoken
        }
    })).done(function (a01, a02, a03, a04, a05, a06, a07) {
// a1 and a2 are arguments resolved for the page1 and page2 ajax requests, respectively.
// Each argument is an array with the following structure: [ data, statusText, jqXHR ]
        //debugger;
        data0[4] = a01[0].Data && a01[0].Data[0] && a01[0].Data[0]["StrctrR1"] ? a01[0].Data[0]["StrctrR1"] : '';
        data0[5] = globalButtonList[globalButtonListStatusIIIII][5];
        //data0[6]=a02[0].Data[0]["AppR1"];
        data0[7] = a03[0].Data && a03[0].Data[0] && a03[0].Data[0]["SysVer"] ? a03[0].Data[0]["SysVer"] : '';
        data0[8] = globalButtonListStatus[globalButtonListStatusIIIII];
        data0[9] = a04[0].Data&&a04[0].Data[0]&&a04[0].Data[0]["CntrlPwrTm"]?a04[0].Data[0]["CntrlPwrTm"]:'';
        data0[10] = a05[0].Data[0]["EngyTm"];
        data0[11] = a06[0].Data[0]["OprtngTm"];
        data0[12] = a07[0].Data[0]["Cycl"] == 1 ? '自动循环' : '单次执行';


        if (globalButtonListStatusIIIII == globalButtonList.length - 1)///最后一个NC要改一下显示的名字
        {
            ///NC的设备信息CellName应该是Spot，ControllerName应该是NC-MNC1000，Model应该是没有的，就写NC，Version没有的，可以用---表示
            data0[2] = "Spot";///
            data0[3] = "NC-MNC1000";///
            data0[4] = "NC";///
            data0[7] = "---";///
        }
        let letUlbak05backimg = document.getElementById('Ulbak05backimg');
        letUlbak05backimg.innerHTML = '';
        for (let i = 0; i < data0.length; i++) {
            let elem_li = document.createElement('li'); // 生成一bai个 li元素
            elem_li.innerHTML = data0[i];
            letUlbak05backimg.appendChild(elem_li); // 添加到UL中去

        }
    });


}
