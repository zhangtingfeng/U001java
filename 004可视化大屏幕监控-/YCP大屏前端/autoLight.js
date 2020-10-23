function autoLightRedBlueYellow() {
    var dataautoLightURL = new Array();
    var dataautoLightBody = new Array();
    var dataautoLightResponse = new Array();

    var timestamp2End = (new Date()).valueOf();
    var timestamp2Start = new Date().setMonth((new Date().getMonth() - 1)).valueOf();//一个月

    for (var i = 0; i < globalButtonList.length; i++) {
        dataautoLightURL[i] = basicUrl +
            "factories/" + "YASKAWA" +
            "/lines/" + globalButtonList[i][0] +
            "/cells/" + globalButtonList[i][1] +
            "/controllers/" + globalButtonList[i][2] +
            "/apps/RC.OperateStatus/analysis/search";

        dataautoLightBody[i] = {
            "Item": ["Run", "EmgStop", "Alm","Idle"],
            "N": 20000,
            "Size": 104857600,
            "Timeout": 2147483647,
            "Query": "(TimeStamp >=" + timestamp2Start + ") AND (TimeStamp <=" + timestamp2End + ")",
            "Sort": [{
                "Target": "TimeStamp",
                "Ascending": false
            }]
        };

    }

    $.when($.ajax({
        type: "POST",
        url: dataautoLightURL[0],
        async: true,
        cache: false,
        data: JSON.stringify(dataautoLightBody[0]),
        contentType: "application/json",
        dataType: "json",
        headers: {
            "YCP-API-TOKEN": servertoken
        }
    }), $.ajax({
        type: "POST",
        url: dataautoLightURL[1],
        async: true,
        cache: false,
        data: JSON.stringify(dataautoLightBody[1]),
        contentType: "application/json",
        dataType: "json",
        headers: {
            "YCP-API-TOKEN": servertoken
        }
    }), $.ajax({
        type: "POST",
        url: dataautoLightURL[2],
        async: true,
        cache: false,
        data: JSON.stringify(dataautoLightBody[2]),
        contentType: "application/json",
        dataType: "json",
        headers: {
            "YCP-API-TOKEN": servertoken
        }
    }), $.ajax({
        type: "POST",
        url: dataautoLightURL[3],
        async: true,
        cache: false,
        data: JSON.stringify(dataautoLightBody[3]),
        contentType: "application/json",
        dataType: "json",
        headers: {
            "YCP-API-TOKEN": servertoken
        }
    }), $.ajax({
        type: "POST",
        url: dataautoLightURL[4],
        async: true,
        cache: false,
        data: JSON.stringify(dataautoLightBody[4]),
        contentType: "application/json",
        dataType: "json",
        headers: {
            "YCP-API-TOKEN": servertoken
        }
    }), $.ajax({
        type: "POST",
        url: dataautoLightURL[5],
        async: true,
        cache: false,
        data: JSON.stringify(dataautoLightBody[5]),
        contentType: "application/json",
        dataType: "json",
        headers: {
            "YCP-API-TOKEN": servertoken
        }
    }), $.ajax({
        type: "POST",
        url: dataautoLightURL[6],
        async: true,
        cache: false,
        data: JSON.stringify(dataautoLightBody[6]),
        contentType: "application/json",
        dataType: "json",
        headers: {
            "YCP-API-TOKEN": servertoken
        }
    }), $.ajax({
        type: "POST",
        url: dataautoLightURL[7],
        async: true,
        cache: false,
        data: JSON.stringify(dataautoLightBody[7]),
        contentType: "application/json",
        dataType: "json",
        headers: {
            "YCP-API-TOKEN": servertoken
        }
    }), $.ajax({
        type: "POST",
        url: dataautoLightURL[8],
        async: true,
        cache: false,
        data: JSON.stringify(dataautoLightBody[8]),
        contentType: "application/json",
        dataType: "json",
        headers: {
            "YCP-API-TOKEN": servertoken
        }
    }), $.ajax({
        type: "POST",
        url: dataautoLightURL[9],
        async: true,
        cache: false,
        data: JSON.stringify(dataautoLightBody[9]),
        contentType: "application/json",
        dataType: "json",
        headers: {
            "YCP-API-TOKEN": servertoken
        }
    }), $.ajax({
        type: "POST",
        url: dataautoLightURL[10],
        async: true,
        cache: false,
        data: JSON.stringify(dataautoLightBody[10]),
        contentType: "application/json",
        dataType: "json",
        headers: {
            "YCP-API-TOKEN": servertoken
        }
    })).done(function (a00, a01, a02, a03, a04, a05, a06, a07, a08, a09, a10) {
        dataautoLightResponse[0] = a00[0].Data[0];
        dataautoLightResponse[1] = a01[0].Data[0];
        dataautoLightResponse[2] = a02[0].Data[0];
        dataautoLightResponse[3] = a03[0].Data[0];
        dataautoLightResponse[4] = a04[0].Data[0];
        dataautoLightResponse[5] = a05[0].Data[0];
        dataautoLightResponse[6] = a06[0].Data[0];
        dataautoLightResponse[7] = a07[0].Data[0];
        dataautoLightResponse[8] = a08[0].Data[0];
        dataautoLightResponse[9] = a09[0].Data[0];
        dataautoLightResponse[10] = a10[0].Data[0];
        //debugger;
        setDisplayLightButton(dataautoLightResponse);

        /*
        Alm: 0
EmgStop: 0
Run: 0

         */

// a1 and a2 are arguments resolved for the page1 and page2 ajax requests, respectively.
// Each argument is an array with the following structure: [ data, statusText, jqXHR ]
        //debugger;

    });


    /*data0[4]=a01[0].Data[0]["StrctrR1"];
            data0[6]=a02[0].Data[0]["AppR1"];
            data0[7]=a03[0].Data[0]["SysVer"];
            data0[9]=a04[0].Data[0]["CntrlPwrTm"];
            data0[10]=a05[0].Data[0]["EngyTm"];
            data0[11]=a06[0].Data[0]["OprtngTm"];
            data0[12]=a07[0].Data[0]["Cycl"]==1?'自动循环':'单次执行';

            var varUlbak05backimg = document.getElementById('Ulbak05backimg');
            varUlbak05backimg.innerHTML = '';
            for (var i = 0; i < data0.length; i++) {
                var elem_li = document.createElement('li'); // 生成一bai个 li元素
                elem_li.innerHTML = data0[i];
                varUlbak05backimg.appendChild(elem_li); // 添加到UL中去

            }*/
}


function setDisplayLightButton(argdataautoLightResponse) {
    //return;

    globalButtonListStatus=[];
   // debugger;
    for (var i = 1; i < 12; i++) {



        var ddaddNumString = "0" + i.toString();
        if (i > 9) {
            ddaddNumString = i.toString();
        }
        var varbuttonimg = "buttonimg" + ddaddNumString;

        var pvarbuttonimg = document.getElementById(varbuttonimg).parentNode;
        var varpos = globalButtonList[i - 1][4];
        pvarbuttonimg.style.left = varpos[0] + "px";
        pvarbuttonimg.style.top = varpos[1] + "px";
        //pvarbuttonimg.style.opacity=1;
        /* Run	Run为1时是运行中
         Run	Run为0时且EmgStop和Alm为0时是待机中
         EmgStop和Alm	EmgStop或Alm为1时是报警中    Alm: 0
         EmgStop: 0
         Run: 0*/
        let varLightStatus = "";
        let varLightStatusText = "";

        if (!argdataautoLightResponse[i - 1]){
            //debugger;
            break;
            return;
        }
        if (argdataautoLightResponse[i - 1]['Run'] == 1) {
            varLightStatus = "lightblue.png";
            varLightStatusText="Running";
        } else if (argdataautoLightResponse[i - 1]['EmgStop'] == 0 &&argdataautoLightResponse[i - 1]['Alm'] == 0) {
            varLightStatus = "lightyellow.png";
            varLightStatusText="Waiting";
        } else if (argdataautoLightResponse[i - 1]['EmgStop'] == 1 || argdataautoLightResponse[i - 1]['Alm'] == 1) {
            varLightStatus = "lightred.png";
            varLightStatusText="Warning";
        }
        else{
            varLightStatus = "lightyellow.png";
            varLightStatusText="待机中";
        }

        globalButtonListStatus.push(varLightStatusText);

        $(('#'+varbuttonimg)).attr("src", "/YCPApp/resources/images/" + addon_name +"/"+ varLightStatus);
    }

}
