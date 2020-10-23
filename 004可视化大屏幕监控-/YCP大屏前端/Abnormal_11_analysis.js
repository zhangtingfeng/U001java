function Abnormal_11_analysis() {
    //debugger;
    //return;

    let timestamp2End = (new Date()).valueOf();


    let timestamp2Start = new Date().setMonth((new Date().getMonth() - 100)).valueOf();//
    /*
        FactoryName	LineName	CellName	ControllerName	程序名
        YASKAWA	EXPO2022	SPOT	R03-AR1440	JobName
        YASKAWA	EXPO2023	SPOT	R04-SP210-1	JbNm
    */


///http://localhost:8080/YCPApp/api/v2/factories/" + "A" + "/lines/" + "B" + "/cells/" + "C" + "/controllers/" + "D" + "/data/search
    let ajaxUrl01 = basicUrl +
        "factories/" + "YASKAWA" +
        "/lines/" + "EXPO2020" +
        "/cells/" + "SPOT" +
        "/controllers/" + "R03-AR1440" +
        "/apps/ArcWeldMonitorDA/analysis/search";
    let dev_body01 = {
        "Item": ["TimeStamp", "JobName", "ErrorLevel", "BeadNo"],
        "N": 20000,
        "Size": 104857600,
        "Timeout": 2147483647,
        "Query": "(TimeStamp >=" + timestamp2Start + ") AND (TimeStamp <=" + timestamp2End + ")",
        "Sort": [{
            "Target": "TimeStamp",
            "Ascending": false
        }]
    };

    ///YASKAWA	EXPO2023	SPOT	R04-SP210-1	JbNm
///Post, "http://localhost:8080/YCPApp/api/v2/factories/" + "A" + "/lines/" + "B" + "/cells/" + "C" + "/controllers/" + "D" + "/apps/" + appKey + "/analysis/search"
    let ajaxUrl02 = basicUrl +
        "factories/" + "YASKAWA" +
        "/lines/" + "EXPO2020" +
        "/cells/" + "SPOT" +
        "/controllers/" + "R04-SP210-1" +
        "/apps/SpotWeld/analysis/search";
    let dev_body02 = {
        "Item": ["TimeStamp", "JbNm", "WldList", "Sptr", "WldId"],
        "N": 20000,
        "Size": 104857600,
        "Timeout": 2147483647,
        "Query": "(TimeStamp >=" + timestamp2Start + ") AND (TimeStamp <=" + timestamp2End + ")",
        "Sort": [{
            "Target": "TimeStamp",
            "Ascending": false
        }]
    };

    /*
    YASKAWA	EXPO2020	SPOT	R06-GP280L	apps/RC.OperateRateD/analysis/search
    JbNm	TimeStamp	WldList中的Sptr=1 发生飞溅	WldId

  */
    let languageArray=[['Current Low','电流过低'],['Current Over','电流过大'],['Arc','弧焊'],['Spot','点焊'],['Sputter','发生飞溅']];
    let myfilllanguage = $("#languagebtn").attr("myIndex");
    myfilllanguage=1-parseInt(myfilllanguage);

    //debugger;
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
    }), $.ajax({
        type: "post",
        url: ajaxUrl02,
        async: true,
        cache: false,
        data: JSON.stringify(dev_body02),
        contentType: "application/json",
        dataType: "json",
        headers: {
            "YCP-API-TOKEN": servertoken
        }
    })).done(function (a01, a02) {
        let let11Array = new Array();


        //let a01Length=a01[0].Data.length;
        for (var i = 0; i < a01[0].Data.length; i++) {
            let letErrorLevelShow = "";
            //debugger;
            if (a01[0].Data[i]["ErrorLevel"] == 2) {
                letErrorLevelShow = languageArray[0][myfilllanguage];//"Current Low"
            } else if (a01[0].Data[i]["ErrorLevel"] == 3) {
                letErrorLevelShow = languageArray[1][myfilllanguage];//"Current Low"
            }
            else{
                continue;
            }
            let11Array.push({
                "JobName": a01[0].Data[i]["JobName"],
                "TimeStamp": a01[0].Data[i]["TimeStamp"],
                "ErrorLevel": letErrorLevelShow,
                "purpose":languageArray[2][myfilllanguage],
                "BeadNo": a01[0].Data[i]["BeadNo"]
            })
        }


        //debugger;
        for (let i = 0; i < a02[0].Data.length; i++) {
            let letErrorLevelShow = "";
            if (a02[0].Data[i] && a02[0].Data[i]["WldList"] && a02[0].Data[i]["WldList"][0] && a02[0].Data[i]["WldList"][0]["Sptr"] == 1) {
                letErrorLevelShow = languageArray[4][myfilllanguage];
            }
            else{
                continue;
            }
            let11Array.push({
                "JobName": a02[0].Data[i]["JbNm"],
                "TimeStamp": a02[0].Data[i]["TimeStamp"],
                "ErrorLevel": letErrorLevelShow,
                "purpose":languageArray[3][myfilllanguage],
                "BeadNo": a02[0].Data[i]["WldId"]
            })
        }

        var sortvarGlobalGetAlarmHistory = let11Array.sort(sortListByItem("TimeStamp"));
        let letArray=new Array();
       // debugger;
        letArray.push(sortvarGlobalGetAlarmHistory[0]);///要求排除重复数据
        for (let i = 1; i < sortvarGlobalGetAlarmHistory.length; i++) {
            let letTime01=letArray[letArray.length-1];
            let AlarmHistory02=sortvarGlobalGetAlarmHistory[i];
///TimeStamp  purpose JobName BeadNo
            if (letTime01.TimeStamp==AlarmHistory02.TimeStamp &&
                letTime01.purpose==AlarmHistory02.purpose &&
                letTime01.JobName==AlarmHistory02.JobName &&
                letTime01.BeadNo==AlarmHistory02.BeadNo){
                continue;
            }
            else
            {
                letArray.push(sortvarGlobalGetAlarmHistory[i]);
            }
        }

        show11Line(letArray);

    });

    //show10Line();
    //show0901Echart();
}



function show11Line(let11Array) {
    temp = document.getElementById('Ulbak10backimg');
    lis = temp.getElementsByTagName('li');

    var minvalue = let11Array.length > 30 ? 30 : let11Array.length;

   // if (lis.length > 8) {
     $("#Ulbak10backimg").find("li").remove();
   // }
    for(let i=0;i<let11Array.length;i++){
        let PTimeStamp = getShowValue(let11Array[i].TimeStamp);
        let Ppurpose = getShowValue(let11Array[i].purpose);
        let PJobName = getShowValue(let11Array[i].JobName);
        let PBeadNo= (let11Array[i].BeadNo);
        let PErrorLevel=getShowValue(let11Array[i].ErrorLevel);
        let PTimeStampData = formateDate(PTimeStamp);
        let PTimeStampTime = formateTime(PTimeStamp);
        var elem_li = document.createElement('li'); // 生成一bai个 li元素

//TimeStamp  purpose JobName BeadNo
        elem_li.innerHTML = "<span>"+PTimeStampData+"</span><span>"+PTimeStampTime+"</span><span>"+Ppurpose+"</span><span>"+PJobName+"</span><span>"+PBeadNo+"</span><span>"+PErrorLevel+"</span>";
        document.getElementById('Ulbak10backimg').appendChild(elem_li);
    }

}
