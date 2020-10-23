//------------- WebAPI用Key变量 ---------//
var varScreenTaskurl = "";///如何调试期间，您不需要加载在线监控，那么可以对这个地址赋值为空，空就不会加载。  调试时期不加载
  //4 5 6 是点击灯的状态，在show040506中  可以临时打开后续函数show040506(vartextShow)
    varScreenTaskurl = "http://192.168.255.2:27070/ScreenTask.jpg";

var fact_temp = null;
var line_temp = null;
var cell_temp = null;
var ctrl_temp = null;
var mecha_temp = null;
var allPage = document.querySelector('html');
var basicUrl = "http://" + window.location.host + "/YCPApp/api/v2/"


var start = document.getElementById('fromDate');
var end = document.getElementById('toDate');
var hajime = null;
var owari = null;
var servertoken;
var times = 0;
var errtimes = 0;
var devlist = [];
var charts = [];
//var p_time_list = [];
var n_time_list;
var userData = '{"UserName":"admin","Password":"admin"}'	//YCP用账号密码
var dataTmp = [
    "X1_Trq_Max_Daily", "Y1_Trq_Max_Daily", "Z1_Trq_Max_Daily",
    "X2_Trq_Max_Daily", "Y2_Trq_Max_Daily", "Z2_Trq_Max_Daily",
    "X3_Trq_Max_Daily", "Y3_Trq_Max_Daily", "Z3_Trq_Max_Daily",
    "X4_Trq_Max_Daily", "Y4_Trq_Max_Daily", "Z4_Trq_Max_Daily",
    "X5_Trq_Max_Daily", "Y5_Trq_Max_Daily", "Z5_Trq_Max_Daily",
    "X6_Trq_Max_Daily", "Y6_Trq_Max_Daily", "Z6_Trq_Max_Daily",
    "X7_Trq_Max_Daily", "Y7_Trq_Max_Daily", "Z7_Trq_Max_Daily",
    "X8_Trq_Max_Daily", "Y8_Trq_Max_Daily", "Z8_Trq_Max_Daily",
];
var dataTmp2 = [
    "CarTypeAll_Daily", "CarType1_Daily", "CarType2_Daily",
    "CarType3_Daily", "CarType4_Daily", "CarType5_Daily",
    "CarType6_Daily", "CarType7_Daily", "CarType8_Daily",
    "CarType9_Daily", "CarType10_Daily", "CarType11_Daily",
    "CarType12_Daily", "CarType13_Daily", "CarType14_Daily",
    "CarType15_Daily", "CarType16_Daily"
];

var languageTextList = [
    ["YASKAWAText", "YASKAWA", "YASKAWA"],
    ["ProductionManagementSystemText", "Production Management System", "生产管理系统"],
    ["bak06Text", "Operation Monitor", "现场监控"],
    ["bak07Text", "Production History", "生产历史"],
    ["bak08Text", "Alarm History", "报警历史记录"],
    ["bak09Text", "Daily Production Analysis", "日常生产分析"],
    ["bak10Text", "Abnormal Production Analysis", "异常生产分析"],
    ["buttonimg01Text", "R01", "R01"],
    ["buttonimg02Text", "R02", "R02"],
    ["buttonimg03Text", "R03", "R03"],
    ["buttonimg04Text", "R04", "R04"],
    ["buttonimg05Text", "R05", "R05"],
    ["buttonimg06Text", "R06", "R06"],
    ["buttonimg07Text", "R07", "R07"],
    ["buttonimg08Text", "R08", "R08"],
    ["buttonimg09Text", "R09", "R09"],
    ["buttonimg10Text", "R10", "R10"],
    ["buttonimg11Text", "NC", "NC"],
    ["mainTodaysProductionVolumeText", "Todays's Production Volume", "今日产量"],
    ["mainCurrentOperatingRateText", "Current Operating Rate", "当前 稼动率"],
    ["mainUlbak05backimgText", "<li>Factory Name</li><li>Line Name</li><li>Cell Name</li><li>Controller Name</li><li>Model</li><li>Serial Number</li><li>Application</li><li>Version</li><li>Current State</li><li>Power On Time</li><li>E-saving Time</li><li>Working Time</li><li>Current Mode</li>", "<li>工厂名称</li><li>生产线名称</li><li>装置名称</li><li>控制柜名称</li><li>机器人型号</li><li>序列号</li><li>用途</li><li>版本号</li><li>当前状态</li><li>控制开机时间</li><li>节能状态累计时间</li><li>工作时间</li><li>当前模式</li>"],
    ["mainDeviceLifespanText", "Device Lifespan(remaining hrs)", "设备寿命(剩余小时)"],
    ["mainDeviceOperatingRateText", "Device Operating Rate(every hrs)", "设备运行率(每小时)"],
    ["mainWorkingWaitingWarningText", "<span>Run</span><span>Idl</span><span>Alm</span><span>Dscnct</span><span>EngySv</span>", "<span>运行中</span><span>待机中</span><span>异常中</span><span>脱机中</span><span>节能中</span>"],
    ["mainbak07backimgText", "<span>Date</span><span>Time</span><span>CarType</span><span>Plan</span><span>Current</span><span>Rate</span>", "<span>日期</span><span>时间</span><span>产品</span><span>计划</span><span>实际</span><span>比率</span>"],
    ["mainbak08backimgText", "<span>Date</span><span>Time</span><span>AlarmName</span><span>AlarmCode</span><span>Device</span>", "<span>日期</span><span>时间</span>报警内容<span></span><span>报警代码</span><span>设备</span>"],
    ["mainbak0902MarkerText", "<span>Real</span><span>Plan</span>", "<span>实际</span><span>计划</span>"],
    ["mainbak10backimgText", "<span>Date</span><span>Time</span><span>Application</span><span>JobName</span><span>Number</span><span>Result</span>", "<span>日期</span><span>时间</span><span>用途</span><span>程序名</span><span>编号</span><span>结果</span>"],
];

/*日期   时间  产品名 计划 实际 比率
 ["mainbak09ButtonText", "Production Volume", "产量"],
    ["mainbak0902ButtonText", "Production Rate", "生产率"],
    ["mainBottombak09backimgText", "<span>Real</span><span>Plan</span><span>Real</span><span>Prediction</span>", "<span>实际</span><span>&nbsp计划</span><span>&nbsp实际</span><span>&nbsp预测</span>"],
 */

///R01-SP225H  --->   NC-MNC1000
var globalButtonList = [
    ["EXPO2020", "SPOT", "R01-SP225H", "R01", [547, 545], "S97A58-1-1", "Spot"],
    ["EXPO2020", "SPOT", "R02-SP100B", "R02", [547, -3], "S97A59-1-1", "Spot"],
    ["EXPO2020", "SPOT", "R03-AR1440", "R03", [701, 545], "1403353-1710-1", "Arc"],
    ["EXPO2020", "SPOT", "R04-SP210-1", "R04", [701, -3], "2068888-106-1", "Spot"],
    ["EXPO2020", "SPOT", "R05-SP210-2", "R05", [837, 545], "2273504-2-1", "Spot"],
    ["EXPO2020", "SPOT", "R06-GP280L", "R06", [834, -3], "2273505-2-1", "Handling"],
    ["EXPO2020", "PAINT", "R07-MPX3500", "R07", [138, -3], "S9K542-1-1", "Paint"],
    ["EXPO2020", "PAINT", "R08-MPO10", "R08", [273, -3], "S9K542-11-1", "DoorOpener"],
    ["EXPO2020", "PAINT", "R09-GP25SV", "R09", [138, 545], "S97A60-1-1", "Sealing"],
    ["EXPO2020", "PAINT", "R10-GP15HLB", "R10", [273, 545], "S97A72-1-1", "Sealing"],
    ["EXPO2020", "SPOT", "R06-GP280L", "NC", [981, 260], "1234567890", "Handling"]];
var globalButtonListStatus = new Array();

var varBooleanIFIniDevie = false;///是否初始化的识别
var varLightAutoClick = 0;///自动点击灯泡
var varClickButtonArray = new Array();
var varGlobalGetAlarmHistory = new Array();

var videoSource = [];
var videoServerIP = "";
//-------Execute here---------//
$(document).ready(function () {
setButtonRateScreenTask();
    if (varScreenTaskurl != "") {////调试时期不加载
        $("#loadImageScreenTaskID").attr("src", varScreenTaskurl);///设置服务端屏幕参数
    }
    //debugger;

    setDisplaysetlanguage(0);
    setDisplayBakground();
    getToken();
    ControllerLocation();
    setAddClickFuntion();
    

    startConfig();

    //alert("111");

})


function setDisplaysetlanguage(fill) {

    //debugger;
    for (var i = 0; i < languageTextList.length; i++) {
        var varid = "#" + languageTextList[i][0];

        if (languageTextList[i][0].indexOf('<li>' > -1) || languageTextList[i][0].indexOf('<span>' > -1)) {
            $(varid).html(languageTextList[i][fill + 1]);
        } else {
            $(varid).text(languageTextList[i][fill + 1]);
        }
    }
}

function setDisplayBakground() {
    $("#languageimg").attr("src", "/YCPApp/resources/images/" + addon_name + "/languageEN.png");
    $("#fullScreenbtnimg").attr("src", "/YCPApp/resources/images/" + addon_name + "/fullscrwt.png");
    //$("#buttonRateVolumeimg").attr("src", "/YCPApp/resources/images/" + addon_name + "/buttonrate.png");
    $("#addmp4_mp4").attr("src", "/YCPApp/resources/images/" + addon_name + "/addmp4.mp4.png");

    //addon_name
    //debugger;
    //$("#bak01").attr("background","url(/YCPApp/resources/images/"+addon_name+"/bak01.png)");
    //$("#bak02").attr("background","url(/YCPApp/resources/images/"+addon_name+"/bak02.png)");
    document.getElementById("container").style.background = "url(/YCPApp/resources/images/" + addon_name + "/back_ground1.jpg)";

    document.getElementById("bak04backimg").style.background = "url(/YCPApp/resources/images/" + addon_name + "/bak04.png)";
    document.getElementById("bak05backimg").style.background = "url(/YCPApp/resources/images/" + addon_name + "/bak05.png)";


    //document.getElementById("bak01backimg").style.background = "url(/YCPApp/resources/images/" + addon_name + "/bak01.png)";
    //document.getElementById("bak02backimg").style.background = "url(http://192.168.1.10:7070/ScreenTask.jpg?rand=0.1036390203308622)";
    //document.getElementById("bak03backimg").style.background = "url(/YCPApp/resources/images/"+addon_name+"/bak03.png)";
    //document.getElementById("bak04backimg").style.background = "url(bak04.png)";
    //document.getElementById("bak05backimg").style.background = "url(bak05.png)";
    //document.getElementById("bak06backimg").style.background = "url(/YCPApp/resources/images/" + addon_name + "/bak06.png)";/// MP4动画，不加载
    document.getElementById("bak07backimg").style.background = "url(/YCPApp/resources/images/" + addon_name + "/bak07.png)";
    document.getElementById("bak08backimg").style.background = "url(/YCPApp/resources/images/" + addon_name + "/bak08.png)";
    document.getElementById("bak09backimg").style.background = "url(/YCPApp/resources/images/" + addon_name + "/bak09.png)";
    //document.getElementById("bak09backimg_02").style.background = "url(/YCPApp/resources/images/" + addon_name + "/bak09.png)";

    document.getElementById("bak10backimg").style.background = "url(/YCPApp/resources/images/" + addon_name + "/bak10.png)"

    //show0405();


    //showEchart05();
    //showEchart0502();


}



function autoLightAutoClick() {
    // return;
    ///本循环定时调用
    varLightAutoClick++;
    if (varLightAutoClick > 11) {
        varLightAutoClick = 1;
    }
    var varStringNum = "0" + varLightAutoClick.toString();
    if (varLightAutoClick > 9) varStringNum = varLightAutoClick.toString()
    var clickButton = "#buttonimg" + varStringNum;

    $(clickButton).click();


}

///生成任意区间的整数
function fullClose(n, m) { //[n,m]
    var result = Math.random() * (m + 1 - n) + n;
    while (result > m) {
        result = Math.random() * (m + 1 - n) + n;
    }
    return Math.round(result);///javascript 四舍五入; js 四舍五入
}


function functiongFadin() {
    $("#bak04backimg").fadeIn();
    $("#bak05backimg").fadeIn();

    /*
    temp = document.getElementById('Ulbak10backimg');
    lis = temp.getElementsByTagName('li');
    if (lis.length > 8) {
        $("#Ulbak10backimg").find("li").remove();
    }

    var elem_li = document.createElement('li'); // 生成一bai个 li元素
    elem_li.innerHTML = '<span>2020.09.15</span><span>17:10:12</span><span>YSA193</span><span>MPX500</span><span>Voltage</span><span>10:00 AM</span>';
    document.getElementById('Ulbak10backimg').appendChild(elem_li); // 添加到UL中去*/
}

function sortListByItem(argSortItem) {
    return function (a, b) {
        var value1 = a[argSortItem];
        var value2 = b[argSortItem];
        return (value2 - value1);
    }

}


function setButtonRateScreenTask() {

    if (varScreenTaskurl != "") {
        var $img = $("#loadImageScreenTaskID");
        $img[0].onload = loadImageScreenTask;
        $img[0].onerror = loadImageScreenTask;
    }


}


function setAddClickFuntion() {


    for (var i = 1; i < 12; i++) {
        var ddaddNumString = "0" + i.toString();
        if (i > 9) {
            ddaddNumString = i.toString();
        }
        //debugger;
        var varbuttonimg = "#buttonimg" + ddaddNumString;
        var varbuttonimgText = "#buttonimg" + ddaddNumString + "Text";

        $(varbuttonimg).attr("adddnum", i);
        var varthisobj = copy($(varbuttonimgText));
        varClickButtonArray[i - 1] = i;

        var varbuttonimggetElementById = "buttonimg" + ddaddNumString;
        document.getElementById(varbuttonimggetElementById).onclick = function () {
            clickDevice(this)
        };


        var varbuttonimgTextgetElementById = "buttonimg" + ddaddNumString + "Text";

    }
}

function clickDevice(obj, obj1, ob2) {

    var varparentElement = obj.parentElement;
    var varTextOBJ = varparentElement.getElementsByClassName("lightButtonText")[0];
    var aText = varTextOBJ.textContent ? varTextOBJ.textContent : varTextOBJ.innerText;  //obj是dom对象，需要加$转成jquery对象

    if (varBooleanIFIniDevie == false) {
        functiongFadin();
        varBooleanIFIniDevie = true;
    } else {

    }

    show040506(aText);

    //console.log(argDeviceNum);
}

function fullScreen() {
    var fillFullscreen = $("#fullScreenbtn").attr("myIndex");
    if (fillFullscreen == 0) {
        allPage.webkitRequestFullscreen();
        $("#fullScreenbtn").attr("myIndex", 1);
        $("#fullScreenbtnimg").attr("src", "/YCPApp/resources/images/" + addon_name + "/extscrbk.png");
    } else if (fillFullscreen == 1) {
        $("#fullScreenbtn").attr("myIndex", 0);
        document.webkitExitFullscreen();
        $("#fullScreenbtnimg").attr("src", "/YCPApp/resources/images/" + addon_name + "/fullscrwt.png");

    }

    $("#languagebtn").trigger('click');// attr("myIndex", 0);
}

function setlanguageOnclick() {
    //debugger;
    var filllanguage = $("#languagebtn").attr("myIndex");

    if (filllanguage == 0) {
        $("#languagebtn").attr("myIndex", 1);
        $("#languageimg").attr("src", "/YCPApp/resources/images/" + addon_name + "/languageEN.png")
    } else if (filllanguage == 1) {
        $("#languagebtn").attr("myIndex", 0);
        $("#languageimg").attr("src", "/YCPApp/resources/images/" + addon_name + "/languageCN.png");
    }
    setDisplaysetlanguage(parseInt(filllanguage));
    //debugger;
    Abnormal_11_analysis();
}

/*
function setButtonRate(varautosetButtonRate) {
    //debugger;
    var fill = $("#buttonRateVolume").attr("myIndex");
    //fill=1-parseInt(fill);

    //if (parseInt(varautosetButtonRate)>0) {
    //  fill=parseInt(varautosetButtonRate)-1;

    //}
    //debugger;
    if (fill == 0) {
        $("#buttonRateVolume").attr("myIndex", 1);
        $("#buttonRateVolumeimg").attr("src", "/YCPApp/resources/images/" + addon_name + "/buttonrate.png")

        $("#bak09backimg").css("display", "block");
        $("#bak09backimg_02").css("display", "none");

        show09Echart();
        show0901Echart();


    } else if (fill == 1) {
        $("#buttonRateVolume").attr("myIndex", 0);
        $("#buttonRateVolumeimg").attr("src", "/YCPApp/resources/images/" + addon_name + "/buttonVolume.png");


        ///按钮的切换  以下是rate
        $("#bak09backimg").css("display", "none");
        $("#bak09backimg_02").css("display", "block");
        show09Echart_02();
        show0901Echart_02();
    }
}
*/

function loadImageScreenTask() {
    //debugger;
    var varSRC = varScreenTaskurl + "?rand=" + (Math.random().toString());
    $("#loadImageScreenTaskID").attr("src", varSRC)
    //console.log(1);  demo/screen/ScreenTask.jpg  http://192.168.1.136:7070/ScreenTask.jpg
    //alert("图片加载完成");
}

function copy(mainObj) {
    let objCopy = {}; // objCopy will store a copy of the mainObj
    let key;

    for (key in mainObj) {
        objCopy[key] = mainObj[key]; // copies each property to the objCopy object
    }
    return objCopy;
}

function formateDate(timestampvalue) {
    if (!timestampvalue) return '';
    //debugger;
    var date = new Date(parseInt(timestampvalue));
    var y = date.getFullYear();
    var m = date.getMonth() + 1;
    m = m < 10 ? ('0' + m) : m;
    var d = date.getDate();
    d = d < 10 ? ('0' + d) : d;
    return y + '.' + m + '.' + d;
}

function formateTime(timestampvalue) {
    if (!timestampvalue) return '';
    //debugger;
    var date = new Date(parseInt(timestampvalue));
    var h = date.getHours();
    h = h < 10 ? ('0' + h) : h;
    var m = date.getMinutes();
    m = m < 10 ? ('0' + m) : m;
    var s = date.getSeconds();
    s = s < 10 ? ('0' + s) : s;
    return h + ':' + m + ':' + s;
}

function getShowValue(argtruevalue) {
    if (!argtruevalue)
        return ''
    else
        return argtruevalue;
}


$('#searchbtn1').click(function () {
    getMax(dataTmp[0], dataTmp[1], dataTmp[2], servertoken)
});
$('#searchbtn2').click(function () {
    getMax(dataTmp[3], dataTmp[4], dataTmp[5], servertoken)
});
$('#searchbtn3').click(function () {
    getMax(dataTmp[6], dataTmp[7], dataTmp[8], servertoken)
});
$('#searchbtn4').click(function () {
    getMax(dataTmp[9], dataTmp[10], dataTmp[11], servertoken)
});
$('#searchbtn5').click(function () {
    getMax(dataTmp[12], dataTmp[13], dataTmp[14], servertoken)
});
$('#searchbtn6').click(function () {
    getMax(dataTmp[15], dataTmp[16], dataTmp[17], servertoken)
});
$('#searchbtn7').click(function () {
    getMax(dataTmp[18], dataTmp[19], dataTmp[20], servertoken)
});
$('#searchbtn8').click(function () {
    getMax(dataTmp[21], dataTmp[22], dataTmp[23], servertoken)
});

//var selecVal = $('#sel1 option:selected').val();
//console.log(selecVal);
//var graph = echarts.init(document.getElementById('myChart'))


//----Language-----//
window.onload = function () {
    languageSelect(getLanguage());
}
console.log(getLanguage());
//----语言全局保持----//
var languageNow = getLanguage();

var defaultLang = "en";

function languageSelect(defaultLang) {
    $("[i18n]").i18n({
        defaultLang: defaultLang,
        filePath: "/YCPApp/resources/json_messages/" + addon_name + "/",
        filePrefix: "i18n_",
        fileSuffix: "",
        forever: true,
        callback: function (res) {
        }
    });
}


function getLanguage() {
    var name = "clientLang"
    var arr, reg = new RegExp("(^| )" + name + "=([^;]*)(;|$)");
    if (arr = document.cookie.match(reg))
        return unescape(arr[2]);
    else
        return defaultLang;
}


//---雷达----//
function getI18nString(par) {
    var vale = $("#" + par).text();
    return vale;
}


//-------点击获取数据列表---------//
function letItWork() {
    languageSelect(getLanguage())
    createControllerInfo();
    //lineData();
    makeTable();
    proCount();
}


//-----日夜模式切换------//
$("#switchbtn").attr("src", "moon.png")

function sunCtrl() {
    var sun = $("#switchbtn").attr("myIndex");
    var fill = $("#fullScreenbtn").attr("myIndex")
    if (fill == 1 && sun == 1) {
        $("body").css("animation", "dtn 1s forwards");
        $("#switchbtn").attr("src", "sun.png")
        $("#fullScreenbtn").attr("src", "extscrwt.png")
        $("#switchbtn").attr("myIndex", 0);
    } else if (fill == 0 && sun == 0) {
        $("body").css("animation", "ntd 1s forwards");
        $("#switchbtn").attr("src", "moon.png")
        $("#fullScreenbtn").attr("src", "fullscrbk.png")
        $("#switchbtn").attr("myIndex", 1);
    } else if (fill == 0 && sun == 1) {
        $("body").css("animation", "dtn 1s forwards");
        $("#switchbtn").attr("src", "sun.png")
        $("#fullScreenbtn").attr("src", "fullscrwt.png")
        $("#switchbtn").attr("myIndex", 0);
    } else {
        $("body").css("animation", "ntd 1s forwards");
        $("#switchbtn").attr("src", "moon.png")
        $("#fullScreenbtn").attr("src", "extscrbk.png")
        $("#switchbtn").attr("myIndex", 1);
    }
}


//-------点击按钮全屏--------//
//$("#fullScreenbtn").attr("src", "fullscrbk.png")


/*
function fullScreen() {
    var fill = $("#fullScreenbtn").attr("myIndex");
    var sun = $("#switchbtn").attr("myIndex");
    if (fill == 0 && sun == 1) {
        allPage.webkitRequestFullscreen();
        $("#fullScreenbtn").attr("myIndex", 1);
        $("#fullScreenbtn").attr("src", "extscrbk.png")
        console.log(fill);
        console.log(sun);

    } else if (fill == 1 && sun == 0) {
        $("#fullScreenbtn").attr("myIndex", 0);
        document.webkitExitFullscreen();
        $("#fullScreenbtn").attr("src", "fullscrwt.png")
        console.log(fill);
        console.log(sun);
    } else if (fill == 1 && sun == 1) {
        $("#fullScreenbtn").attr("myIndex", 0);
        document.webkitExitFullscreen();
        $("#fullScreenbtn").attr("src", "fullscrbk.png")
        console.log(fill);
        console.log(sun);
    } else {
        $("#fullScreenbtn").attr("myIndex", 1);
        allPage.webkitRequestFullscreen();
        $("#fullScreenbtn").attr("src", "extscrwt.png")
        console.log(fill);
        console.log(sun);
    }
}
*/

//------Function Definition Begins-------//

//------Get Token-------//
function getToken() {
    var ajaxUrl = basicUrl + "users/auth";
    //debugger;
    $.ajax({
        url: ajaxUrl,
        contentType: "text/plain",
        data: userData,
        type: "POST",
        dataType: "json",
        timeout: 1000,
        cache: false,
        async: false,
        success: function (data) {
            servertoken = "" + data.Token;
            //debugger;
            console.log(servertoken)
            return servertoken;
        },
        error: function (e) {
            console.log(e);
        }
    });
};
console.log(servertoken);


//---------Init Date---------//
function init() {
    console.log("init start");
    //--------- From、To日期设定---------//
    var dates = jQuery('#fromDate, #toDate').datepicker({
        showAnim: 'drop',
        changeMonth: false,
        dateFormat: "yy/mm/dd",
        onSelect: function (selectedDate) {
            var option = this.id == 'fromDate' ? 'minDate' : 'maxDate',
                instance = $(this).data('datepicker'),
                date = $.datepicker.parseDate(
                    instance.settings.dateFormat ||
                    $.datepicker._defaults.dateFormat,
                    selectedDate, instance.settings);
            dates.not(this).datepicker('option', option, date);
        }
    });
    $("#fromDate").datepicker();
    $("#toDate").datepicker();
};


// 取得URL  http://localhost:8084/?controllerName=D&lineName=B&cellName=C&factoryName=A
function ControllerLocation() {
    //debugger;
    console.log("ControllerLocation start");
    console.log("  iframeURL:" + window.location.href);
    // BaseURL取得
    var href = parent.location.href;
    console.log("  href:" + href);
    // 工厂名取得
    this.factory = getHeaderString(href, "factoryName");
    console.log("  factoryName:" + this.factory);
    // 产线名取得
    this.line = getHeaderString(href, "lineName");
    console.log("  lineName:" + this.line);
    // 单元名取得
    this.cell = getHeaderString(href, "cellName");
    console.log("  cellName:" + this.cell);
    // 控制器名取得
    this.controller = getHeaderString(href, "controllerName");
    console.log("  controllerName:" + this.controller);

    console.log("ControllerLocation end");
};


// URL全局保持
var controllerLocation = new ControllerLocation();


//----------------获取URL文字段-------------//
function getHeaderString(str, startKey) {
    return getString(str, startKey + '=', '&', 0);
}


function getString(str, startKey, endKey, startIndex) {
    var startKeyIndex = str.indexOf(startKey, startIndex);
    if (startKeyIndex >= 0) {
        var valueIndex = startKeyIndex + startKey.length;
        var endKeyIndex = str.indexOf(endKey, valueIndex);
        if (endKeyIndex >= 0) {
            return str.substring(valueIndex, endKeyIndex);
        } else {
            return str.substring(valueIndex);
        }
    } else {
        return "";
    }
}


//----------daily efficiency chart--------------suspend//
function dayBarChart() {
    var labe = [30];
    var labeltwo = [20];
    var labelthr = [40];
    var daybarChart = c3.generate({
        bindto: '.workStats',
        data: {
            x: 'x',
            columns: [
                ['x', today],
                ['pro1', labe],
                ['pro2', labeltwo],
                ['pro3', labelthr]
            ],
            type: 'bar',
            groups: [
                ['pro1', 'pro2', 'pro3']
            ]
        },
        axis: {
            x: {
                type: "timeseries",
                tick: {
                    format: "%Y-%m-%d"
                }
            },
            y: {
                max: 100,
                padding: { top: 0, bottom: 0 },
                label: {
                    text: '(%)',
                    position: 'outer-middle'
                }
            }

        }
    });
}


//-----------weekly efficiency chart----------suspend//
function weekBarChart() {
    var weekBarChart = c3.generate({
        bindto: '.workStatsWeek',
        data: {
            columns: [
                ['data2', 0.6]
            ],
            axes: {
                data2: 'data2'
            },
            types: {
                data2: 'bar' // ADD
            }
        },
        axis: {
            y: {
                label: {
                    text: '百分比',
                    position: 'outer-middle'
                }
            }
        }
    });
}


//-----------------获取状态码------------------//
function getLineInfo() {

    return;
    hajime = moment(start.value).valueOf();
    owari = moment(end.value).valueOf() + 86399000;
    console.log(hajime);
    console.log(owari);
    var statusArr = [];

    var dev_body = {
        "Item": ["Case", "TimeStamp"],
        "N": 20000,
        "Size": 104857600,
        "Timeout": 2147483647,
        "Query": "(TimeStamp >=" + hajime + ") AND (TimeStamp <=" + owari + ")",
        "Sort": [{
            "Target": "TimeStamp",
            "Ascending": false
        }]
    }

    $.ajax({
        type: "POST",
        url: basicUrl +
            "factories/" + controllerLocation.factory +
            "/lines/" + controllerLocation.line +
            "/cells/" + controllerLocation.cell +
            "/controllers/" + controllerLocation.controller +
            "/apps/0022000000000000/analysis/search",
        async: false,
        cache: false,
        data: JSON.stringify(dev_body),
        contentType: "application/json",
        dataType: "json",
        headers: {
            "YCP-API-TOKEN": servertoken
        },
        success: function (ink) {
            //console.log(ink.Data);
            for (var i = 0; i < ink.Data.length; i++) {
                var kin = ink.Data[i].Result;
                statusArr.push(kin);
            }
            console.log(statusArr);
            //statusJudge(statusArr);
        },
        error: function (e) {
            console.log("Exception in getLineInfo()");
            console.log(e);
            console.log("=========");
        }
    })
}

//---------判断状态码输出分析结果-----------//
function statusJudge(statusArr) {
    //$("#textContent").text ("变频器力矩输出曲线异常，建议检查或更换必要部件。");
    for (i = 0; i < statusArr.length; i++) {
        if (statusArr[i] == 0 && statusArr[i + 1] == 0) {
            $("#textContent").text("\n" + "变频器力矩输出曲线异常，建议检查或更换必要部件。");
            break;
        } else {
            $("#textContent").text("\n" + "变频器力矩输出曲线正常。");
        }

    }
}


//---------获取折线图数据---------//
function lineData(start) {
    return;
    /*var x1Trqs = [];
    var y1Trqs = [];
    var z1Trqs = [];
    var x2Trqs = [];
    var y2Trqs = [];
    var z2Trqs = [];
    var x3Trqs = [];
    var y3Trqs = [];
    var z3Trqs = [];
    var x4Trqs = [];
    var y4Trqs = [];
    var z4Trqs = [];
    var x5Trqs = [];
    var y5Trqs = [];
    var z5Trqs = [];
    var x6Trqs = [];
    var y6Trqs = [];
    var z6Trqs = [];
    var x7Trqs = [];
    var y7Trqs = [];
    var z7Trqs = [];
    var x8Trqs = [];
    var y8Trqs = [];
    var z8Trqs = [];  ;
    var xAxs = [];
    var timeTitles = [];*/
    var yxw = [];
    //hajime = moment(start.value).valueOf();
    //owari  = moment(end.value).valueOf()+86399000;
    var dev_body = {
        "Item": ["list", "TimeStamp"],
        "N": 20000,
        "Size": 104857600,
        "Timeout": 2147483647,
        "Query": "(TimeStamp >=" + start + ") AND (TimeStamp <=" + start + 1 + ")",
        "Sort": [{
            "Target": "TimeStamp",
            "Ascending": false
        }]
    };
    $.ajax({
        type: "POST",
        url: basicUrl +
            "factories/" + controllerLocation.factory +
            "/lines/" + controllerLocation.line +
            "/cells/" + controllerLocation.cell +
            "/controllers/" + controllerLocation.controller +
            "/apps/0022000000000000/analysis/search",
        async: false,
        cache: false,
        data: JSON.stringify(dev_body),
        contentType: "application/json",
        dataType: "json",
        headers: {
            "YCP-API-TOKEN": servertoken
        },
        success: function (shit) {
            console.log(shit.Data);
            $("#textContent").text("");
            //for(var i=0;i< shit.Data.length;i++){
            //var gua = shit.Data[i]["list"];
            var R1 = getJsonData(shit.Data[0]["list"], "R1_SpotStatus");
            var R2 = getJsonData(shit.Data[0]["list"], "R2_SpotStatus");
            var R3 = getJsonData(shit.Data[0]["list"], "R3_SpotStatus");
            var R4 = getJsonData(shit.Data[0]["list"], "R4_SpotStatus");
            var R5 = getJsonData(shit.Data[0]["list"], "R5_SpotStatus");
            var R6 = getJsonData(shit.Data[0]["list"], "R6_SpotStatus");
            var zero = getJsonData(shit.Data[0]["list"], "X1_F");
            //console.log(zero);
            var one = getJsonData(shit.Data[0]["list"], "Y1_F");
            var two = getJsonData(shit.Data[0]["list"], "Z1_F");
            var three = getJsonData(shit.Data[0]["list"], "X2_F");
            var four = getJsonData(shit.Data[0]["list"], "Y2_F");
            var five = getJsonData(shit.Data[0]["list"], "Z2_F");
            var six = getJsonData(shit.Data[0]["list"], "X3_F");
            var seven = getJsonData(shit.Data[0]["list"], "Y3_F");
            var eight = getJsonData(shit.Data[0]["list"], "Z3_F");
            var nine = getJsonData(shit.Data[0]["list"], "X4_F");
            var ten = getJsonData(shit.Data[0]["list"], "Y4_F");
            var ten1 = getJsonData(shit.Data[0]["list"], "Z4_F");
            var ten2 = getJsonData(shit.Data[0]["list"], "X5_F");
            var ten3 = getJsonData(shit.Data[0]["list"], "Y5_F");
            var ten4 = getJsonData(shit.Data[0]["list"], "Z5_F");
            var ten5 = getJsonData(shit.Data[0]["list"], "X6_F");
            var ten6 = getJsonData(shit.Data[0]["list"], "Y6_F");
            var ten7 = getJsonData(shit.Data[0]["list"], "Z6_F");
            var ten8 = getJsonData(shit.Data[0]["list"], "X7_F");
            var ten9 = getJsonData(shit.Data[0]["list"], "Y7_F");
            var ten10 = getJsonData(shit.Data[0]["list"], "Z7_F");
            var ten11 = getJsonData(shit.Data[0]["list"], "X8_F");
            var ten12 = getJsonData(shit.Data[0]["list"], "Y8_F");
            var ten13 = getJsonData(shit.Data[0]["list"], "Z8_F");
            var xAx = getJsonData(shit.Data[0]["list"], "TimeStamp");
            var timeTitle = shit.Data[0]["TimeStamp"];
            for (var s = 0; s < xAx.length; s++) {
                var xjr = moment(parseInt(xAx[s])).format("HH:mm:ss");
                //console.log(parseInt(xAxs[s]))
                yxw.push(xjr);
            }
            var reichu = moment(timeTitle).format('YYYY-MM-DD HH:mm:ss');
            /*x1Trqs.push(x1Trq);
            y1Trqs.push(y1Trq);
            z1Trqs.push(z1Trq);
            x2Trqs.push(x2Trq);
            y2Trqs.push(y2Trq);
            z2Trqs.push(z2Trq);
            x3Trqs.push(x2Trq);
            y3Trqs.push(y2Trq);
            z3Trqs.push(z2Trq);
            x4Trqs.push(x2Trq);
            y4Trqs.push(y2Trq);
            z4Trqs.push(z2Trq);
            x5Trqs.push(x2Trq);
            y5Trqs.push(y2Trq);
            z5Trqs.push(z2Trq);
            x6Trqs.push(x2Trq);
            y6Trqs.push(y2Trq);
            z6Trqs.push(z2Trq);
            x7Trqs.push(x2Trq);
            y7Trqs.push(y2Trq);
            z7Trqs.push(z2Trq);
            x8Trqs.push(x2Trq);
            y8Trqs.push(y2Trq);
            z8Trqs.push(z2Trq);
            xAxs.push(xAx);
            timeTitles.push(timeTitle);*/
            //}
            //console.log(x1Trqs);
            if (zero.length == 0) {
                $("#textContent").text("未找到数据，请尝试重新设定时间区间");
                //alert("数据不存在,请尝试重新设定时间区间");
            } else {
                drawLineChart(R1, R2, R3, R4, R5, R6, zero, one, two, three, four, five, six, seven, eight, nine, ten, ten1, ten2, ten3, ten4, ten5, ten6, ten7, ten8, ten9, ten10, ten11, ten12, ten13, yxw, reichu)
                //makeTable(x1Trqs,y1Trqs,z1Trqs,x2Trqs,y2Trqs,z2Trqs,x3Trqs,y3Trqs,z3Trqs,x4Trqs,y4Trqs,z4Trqs,x5Trqs,y5Trqs,z5Trqs,x6Trqs,y6Trqs,z6Trqs,x7Trqs,y7Trqs,z7Trqs,x8Trqs,y8Trqs,z8Trqs,xAxs,timeTitles)
            }
        },
        error: function (e) {
            console.log("Exception in lineData()");
            console.log(e);
            console.log("=========");
        }

    })

}


//----------------------显示折线图-------------//
function drawLineChart(R1, R2, R3, R4, R5, R6, zero, one, two, three, four, five, six, seven, eight, nine, ten, ten1, ten2, ten3, ten4, ten5, ten6, ten7, ten8, ten9, ten10, ten11, ten12, ten13, yxw, reichu) {
    //console.log(zero);
    //console.log(yxw);
    var graph = echarts.init(document.getElementById('myChart'));
    var option = {
        title: { text: reichu + "" },
        toolbox: {
            show: true,
            feature: {
                dataZoom: {
                    yAxisIndex: 'none'
                },
                dataView: { readOnly: false },
                //magicType: {type: ['line', 'bar']},
                restore: {},
                saveAsImage: {},
                myTool: {
                    show: true,
                    title: 'X轴',
                    icon: "image://fill1.png",
                    onclick: function () {
                        option.legend.selected = {
                            'X1_F': true,
                            'X2_F': true,
                            'X3_F': true,
                            'X4_F': true,
                            'X5_F': true,
                            'X6_F': true,
                            'X7_F': true,
                            'X8_F': true,
                            'Y1_F': false,
                            'Y2_F': false,
                            'Y3_F': false,
                            'Y4_F': false,
                            'Y5_F': false,
                            'Y6_F': false,
                            'Y7_F': false,
                            'Y8_F': false,
                            'Z1_F': false,
                            'Z2_F': false,
                            'Z3_F': false,
                            'Z4_F': false,
                            'Z5_F': false,
                            'Z6_F': false,
                            'Z7_F': false,
                            'Z8_F': false
                        };
                        return graph.setOption(option);
                        console.log("更新成功")
                    }
                },
                myTool1: {
                    show: true,
                    title: 'Y轴',
                    icon: "image://fill2.png",
                    onclick: function () {
                        option.legend.selected = {
                            'Y1_F': true,
                            'Y2_F': true,
                            'Y3_F': true,
                            'Y4_F': true,
                            'Y5_F': true,
                            'Y6_F': true,
                            'Y7_F': true,
                            'Y8_F': true,
                            'X1_F': false,
                            'X2_F': false,
                            'X3_F': false,
                            'X4_F': false,
                            'X5_F': false,
                            'X6_F': false,
                            'X7_F': false,
                            'X8_F': false,
                            'Z1_F': false,
                            'Z2_F': false,
                            'Z3_F': false,
                            'Z4_F': false,
                            'Z5_F': false,
                            'Z6_F': false,
                            'Z7_F': false,
                            'Z8_F': false
                        };
                        return graph.setOption(option);
                        console.log("更新成功")
                    }
                },
                myTool2: {
                    show: true,
                    title: 'Z轴',
                    icon: "image://fill3.png",
                    onclick: function () {
                        option.legend.selected = {
                            'Z1_F': true,
                            'Z2_F': true,
                            'Z3_F': true,
                            'Z4_F': true,
                            'Z5_F': true,
                            'Z6_F': true,
                            'Z7_F': true,
                            'Z8_F': true,
                            'X1_F': false,
                            'X2_F': false,
                            'X3_F': false,
                            'X4_F': false,
                            'X5_F': false,
                            'X6_F': false,
                            'X7_F': false,
                            'X8_F': false,
                            'Y1_F': false,
                            'Y2_F': false,
                            'Y3_F': false,
                            'Y4_F': false,
                            'Y5_F': false,
                            'Y6_F': false,
                            'Y7_F': false,
                            'Y8_F': false
                        };
                        return graph.setOption(option);
                        console.log("更新成功")
                    }
                }
            }

        },
        legend: { y: 'bottom', type: 'scroll' },
        tooltip: {
            padding: [10, 20],
            trigger: 'axis',
            axisPointer: {
                type: 'shadow',
                shadowStyle: {
                    color: 'rgba(0, 0, 0, 0.1)'
                }
            },
        },
        xAxis: {
            data: yxw
        },
        yAxis: {},
        series: [
            { type: 'line', name: 'R1_Stats', data: R1 },
            { type: 'line', name: 'R2_Stats', data: R2 },
            { type: 'line', name: 'R3_Stats', data: R3 },
            { type: 'line', name: 'R4_Stats', data: R4 },
            { type: 'line', name: 'R5_Stats', data: R5 },
            { type: 'line', name: 'R6_Stats', data: R6 },
            { type: 'line', name: 'X1_F', data: zero },
            { type: 'line', name: 'Y1_F', data: one },
            { type: 'line', name: 'Z1_F', data: two },
            { type: 'line', name: 'X2_F', data: three },
            { type: 'line', name: 'Y2_F', data: four },
            { type: 'line', name: 'Z2_F', data: five },
            { type: 'line', name: 'X3_F', data: six },
            { type: 'line', name: 'Y3_F', data: seven },
            { type: 'line', name: 'Z3_F', data: eight },
            { type: 'line', name: 'X4_F', data: nine },
            { type: 'line', name: 'Y4_F', data: ten },
            { type: 'line', name: 'Z4_F', data: ten1 },
            { type: 'line', name: 'X5_F', data: ten2 },
            { type: 'line', name: 'Y5_F', data: ten3 },
            { type: 'line', name: 'Z5_F', data: ten4 },
            { type: 'line', name: 'X6_F', data: ten5 },
            { type: 'line', name: 'Y6_F', data: ten6 },
            { type: 'line', name: 'Z6_F', data: ten7 },
            { type: 'line', name: 'X7_F', data: ten8 },
            { type: 'line', name: 'Y7_F', data: ten9 },
            { type: 'line', name: 'Z7_F', data: ten10 },
            { type: 'line', name: 'X8_F', data: ten11 },
            { type: 'line', name: 'Y8_F', data: ten12 },
            { type: 'line', name: 'Z8_F', data: ten13 }
        ]

    };
    graph.clear();
    //option.legend.selected = {'整合OL0000':false,'整合OL0001':false,'整合OL0002':false,'整合OL0003':false,'OL0000':true,'OL0001':true,'OL0002':true,'OL0003':true};
    graph.setOption(option);
    console.log("Graph Finished!")
}


function makeTable(/*x1Trqs,y1Trqs,z1Trqs,x2Trqs,y2Trqs,z2Trqs,x3Trqs,y3Trqs,z3Trqs,x4Trqs,y4Trqs,z4Trqs,x5Trqs,y5Trqs,z5Trqs,x6Trqs,y6Trqs,z6Trqs,x7Trqs,y7Trqs,z7Trqs,x8Trqs,y8Trqs,z8Trqs,xAxs,timeTitles*/) {
    var rows = [];
    var p_time_list = []
    var table = document.getElementById("recordTable");
    var headache1 = ["Date", "CarType", "NCnum", "TactTime", "Graph"];
    var headache2 = ["日期", "车型", "NC数量", "节拍时间", "图表"];
    var headlist;
    languageNow === "zh_CN" ? headlist = headache2 : headlist = headache1;
    console.log(headlist);
    const ENV_MAX = 4;

    while (table.rows[0]) table.deleteRow(0);

    for (i = 0; i < devlist.length + 1; i++) {

        rows.push(table.insertRow(-1));
        if (i == 0) {
            for (j = 0; j < headlist.length; j++) {
                cell = rows[i].insertCell(-1);
                cell.whiteSpace = "inherit";
                cell.style.color = "white";
                //cell.setAttribute("i18n",headache[j]);
                cell.style.textAlign = 'center';
                cell.appendChild(document.createTextNode(headlist[j]));
                cell.style.backgroundColor = "#2F5597";
            }
        } else {

            for (j = 0; j < ENV_MAX; j++) {
                cell = rows[i].insertCell(-1);
                cell.whiteSpace = "inherit";
                cell.style.textAlign = 'center';
                cell.style.verticalAlign = 'middle';
                //cell.style.display = 'block';
                if (j == 0) {
                    var dt = moment(devlist[i - 1][j]).format('YYYY-MM-DD HH:mm:ss');
                    p_time_list.push(devlist[i - 1][j]);
                    //console.log(dt);
                    cell.appendChild(document.createTextNode(dt));
                } else {
                    cell.appendChild(document.createTextNode(devlist[i - 1][j]));
                }
            }
            //console.log(p_time_list);

            /* 有効检查 */
            /*var nowtime_obj = new Date();
            var now_time = nowtime_obj.getTime();
            var prev_24h = now_time - (24 * 60 * 60 *1000);

            /* X2最大Torque */
            /*cell=rows[i].insertCell(-1);
            cell.whiteSpace = "inherit";
            cell.display = "flex";
            cell.style.textAlign = 'center';
            cell.style.verticalAlign = 'middle';
            //var dt = moment(devlist[i-1][0]).format('YYYY-MM-DD HH:mm:ss');
            var tt = devlist[i-1][4];
            cell.appendChild(document.createTextNode(tt));
            /*if(Number.isNaN(dt.getFullYear()) != true) {
                var crd_date = dt.getFullYear() + "/" + toChgDigits(dt.getMonth()+1, 2) + "/" + toChgDigits(dt.getDate(), 2) + " " + toChgDigits(dt.getHours(), 2) + ":" + toChgDigits(dt.getMinutes(), 2) + ":" + toChgDigits(dt.getSeconds(), 2) + "." + toChgDigits(dt.getMilliseconds(), 3);
                cell.appendChild(document.createTextNode(crd_date));
            }
            else {
                var crd_date = "-";
                cell.appendChild(document.createTextNode(crd_date));
            }*/
            /* X3最大Torque */
            /*cell=rows[i].insertCell(-1);
            cell.whiteSpace = "inherit";
            cell.display = "flex";
            cell.style.textAlign = 'center';
            cell.style.verticalAlign = 'middle';
            var ctgr = devlist[i-1][5];
            cell.appendChild(document.createTextNode(ctgr));


           /* CrashRecordBTN */
            cell = rows[i].insertCell(-1);
            cell.whiteSpace = "inherit";
            cell.display = "flex";
            cell.style.textAlign = 'center';
            cell.style.verticalAlign = 'middle';
            var crdbtn = document.createElement("img");
            var style = "style";
            crdbtn.src = "recview_btn.png";
            crdbtn.id = i + "";
            //crdbtn.data-toggle = "modal";
            //crdbtn.data-target = "#myModal";
            cell.appendChild(crdbtn);
            $("#" + i + "").click(function () {
                //lineData()
                var val = $(this).attr("id");
                var kabuda = this;
                //var yxw = [];
                console.log(val);
                //console.log(parseInt(val),p_time_list.length-parseInt(val));
                //console.log(p_time_list[parseInt(val)-1],p_time_list[parseInt(val)]);
                lineData(p_time_list[parseInt(val) - 1]);

                /*var zero = x1Trqs[parseInt(val)-1];
                var one  = y1Trqs[parseInt(val)-1];
                var two  = z1Trqs[parseInt(val)-1];
                var three = x2Trqs[parseInt(val)-1];
                var four  = y2Trqs[parseInt(val)-1];
                var five  = z2Trqs[parseInt(val)-1];
                var six = x3Trqs[parseInt(val)-1];
                var seven  = y3Trqs[parseInt(val)-1];
                var eight  = z3Trqs[parseInt(val)-1];
                var nine = x4Trqs[parseInt(val)-1];
                var ten  = y4Trqs[parseInt(val)-1];
                var ten1  = z4Trqs[parseInt(val)-1];
                var ten2 = x5Trqs[parseInt(val)-1];
                var ten3  = y5Trqs[parseInt(val)-1];
                var ten4  = z5Trqs[parseInt(val)-1];
                var ten5 = x6Trqs[parseInt(val)-1];
                var ten6  = y6Trqs[parseInt(val)-1];
                var ten7  = z6Trqs[parseInt(val)-1];
                var ten8 = x7Trqs[parseInt(val)-1];
                var ten9  = y7Trqs[parseInt(val)-1];
                var ten10  = z7Trqs[parseInt(val)-1];
                var ten11 = x8Trqs[parseInt(val)-1];
                var ten12  = y8Trqs[parseInt(val)-1];
                var ten13  = z8Trqs[parseInt(val)-1];
                var zq = xAxs[parseInt(val)-1];
                for(s=0; s<zq.length; s++){
                  var xjr = moment(parseInt(zq[s])).format("HH:mm:ss");
                  //console.log(parseInt(xAxs[s]))
                  yxw.push(xjr);
              }*/
                //var reichu = moment(timeTitles[parseInt(val)-1]).format('YYYY-MM-DD HH:mm:ss');
                kabuda.setAttribute("data-toggle", "modal");
                kabuda.setAttribute("data-target", "#myModal");
                //drawLineChart(zero,one,two,three,four,five,six,seven,eight,nine,ten,ten1,ten2,ten3,ten4,ten5,ten6,ten7,ten8,ten9,ten10,ten11,ten12,ten13,yxw,reichu);
            }
            );
            //console.log(i);
        }
    }

}


function createControllerInfo() {
    hajime = moment(start.value).valueOf();
    owari = moment(end.value).valueOf() + 86399000;
    console.log(servertoken);
    getToken();
    return;
    $.ajax({
        type: "GET",
        url: basicUrl + "factories/",
        async: false,
        dataType: 'json',
        headers: {
            "YCP-API-TOKEN": servertoken
        },
        /* Factory情報収集 */
        success: function (fact_arg, textStatus, jqXHR) {
            console.log(fact_arg);
            for (i = 0; i < fact_arg.Factory.length; i++) {
                fact_temp = fact_arg.Factory[i];
                $.ajax({
                    type: "GET",
                    url: basicUrl + "factories/" + fact_temp + "/lines",
                    dataType: 'json',
                    async: false,
                    cache: false,
                    headers: {
                        "YCP-API-TOKEN": servertoken
                    },
                    /* Line情報収集 */
                    success: function (line_arg, textStatus, jqXHR) {
                        console.log(line_arg);
                        for (j = 0; j < line_arg.Line.length; j++) {
                            line_temp = line_arg.Line[j];
                            $.ajax({
                                type: "GET",
                                url: basicUrl + "factories/" + fact_temp + "/lines/" + line_temp + "/cells",
                                async: false,
                                cache: false,
                                dataType: 'json',
                                headers: {
                                    "YCP-API-TOKEN": servertoken
                                },
                                /* Cell情報収集 */
                                success: function (cell_arg, textStatus, jqXHR) {
                                    console.log(cell_arg);
                                    for (k = 0; k < cell_arg.Cell.length; k++) {
                                        cell_temp = cell_arg.Cell[k];
                                        $.ajax({
                                            type: "GET",
                                            url: basicUrl + "factories/" + fact_temp + "/lines/" + line_temp + "/cells/" + cell_temp + "/controllers",
                                            async: false,
                                            cache: false,
                                            dataType: 'json',
                                            headers: {
                                                "YCP-API-TOKEN": servertoken
                                            },
                                            /* Controller情報収集 */
                                            success: function (ctrl_arg, textStatus, jqXHR) {
                                                console.log(ctrl_arg);
                                                var req_body = {
                                                    "Item": ["TimeStamp", "TimeStamp_Result", "CarType", "ncNumber", "TactTime"],
                                                    "N": 100000,
                                                    "Size": 104857600,
                                                    "Timeout": 2147483647,
                                                    "Query": "(TimeStamp >= " + hajime + ") AND (TimeStamp <= " + owari + ")",
                                                    "Sort": [{
                                                        "Target": "TimeStamp",
                                                        "Ascending": false
                                                    }]
                                                };
                                                for (l = 0; l < ctrl_arg.Controller.length; l++) {
                                                    ctrl_temp = ctrl_arg.Controller[l].Key
                                                    $.ajax({
                                                        type: "POST",
                                                        url: basicUrl + "factories/" + fact_temp + "/lines/" + line_temp + "/cells/" + cell_temp + "/controllers/" + ctrl_temp + "/apps/" + "0022000000000000" + "/analysis/search",
                                                        async: false,
                                                        cache: false,
                                                        data: JSON.stringify(req_body),
                                                        contentType: 'application/json',
                                                        dataType: 'json',
                                                        headers: {
                                                            "YCP-API-TOKEN": servertoken
                                                        },
                                                        success: function (crash_data, textStatus, jqXHR) {
                                                            console.log(crash_data);
                                                            devlist = [];
                                                            if (crash_data.Header.Returned != 0) {
                                                                for (y = 0; y < crash_data.Data.length; y++) {
                                                                    var devdata = [crash_data.Data[y].TimeStamp_Result, crash_data.Data[y].CarType, crash_data.Data[y].ncNumber, crash_data.Data[y].TactTime]
                                                                    var selecVal = $('#sel1 option:selected').val();
                                                                    console.log(devdata);
                                                                    console.log(selecVal);
                                                                    if (selecVal == 0 && devdata[1] && devdata[0] != undefined) {
                                                                        console.log("Judge0");
                                                                        var alldata = devdata;
                                                                        devlist.push(alldata);
                                                                    } else if (selecVal != 0 && devdata[1] == selecVal && devdata[0] != undefined) {
                                                                        console.log("JudgeSuccess");
                                                                        var selectedData = devdata;
                                                                        devlist.push(selectedData);
                                                                    } else {
                                                                        console.log("JudgeFail");
                                                                        //devlist.push(devdata);
                                                                    }
                                                                }
                                                            } else {
                                                                var devdata = ["-", "-", "-", "-", "-"]
                                                            }
                                                            //devlist.push(devdata);
                                                            console.log(devlist);
                                                            //for (var p = 0; p<devlist.length; )
                                                            /*p_time_list = devlist.map(function(x){
                                                                return x[0];
                                                            })*/
                                                            //n_time_list = p_time_list.reverse();
                                                            //console.log(n_time_list);
                                                            //console.log(p_time_list);
                                                        }
                                                    });
                                                }
                                            }
                                        });
                                    }
                                }
                            });
                        }
                    }
                });
            }
            /* 环境List更新 */
            g_env_list = devlist;
            //g_time_list = g_time_list.reverse();
            /* 画面描画処理 */
            if (g_env_list.length == 0) {
                makeTable();
                alert("未找到匹配数据，请重新设定检索条件。");
            } else {
                makeTable();
            }
        }
    });
};

function getJsonData(array, key) {
    var key = key || "coldplay";
    var res = [];
    if (array) {
        array.forEach(function (t) {
            res.push(t[key]);
        });
    }
    return res;
}


//----画出产量图------//
function statChart(proRow, xRow) {
    var statpic = echarts.init(document.getElementById("workpic"));
    var optionEn = {
        title: {
            text: 'Production',
            backgroundColor: '#2F5597',
            borderRadius: 5,
            fontSize: 30,
            textStyle: {
                color: '#FFFFFF',
                fontSize: 25
            }
        },
        tooltip: { trigger: 'axis' },
        legend: {
            data: ['ProNum'],
            x: '450px',
            y: '10px'
        },
        xAxis: {
            //type: 'category',
            data: xRow
        },
        yAxis: {},
        series: [{
            name: 'ProNum',
            color: 'blue',
            //stack:'总量',
            type: 'bar',
            data: proRow
            /*label:{
                normal:{
                show: true,
                formatter: function(params) {
             if (params.value > 0) {
                return params.value;
             } else {
                return 0;
                     }
            }
            }
        }*/
        }]
    };
    var optionCn = {
        title: {
            text: '产量',
            backgroundColor: '#2F5597',
            borderRadius: 5,
            fontSize: 30,
            textStyle: {
                color: '#FFFFFF',
                fontSize: 25
            }
        },
        tooltip: { trigger: 'axis' },
        legend: {
            data: ['产量(件)'],
            x: '450px',
            y: '10px'
        },
        xAxis: {
            data: xRow
        },
        yAxis: {},
        series: [{
            name: 'ProNum',
            color: 'blue',
            //stack:'总量',
            type: 'bar',
            data: proRow
            /*label:{
                normal:{
                show: true,
                formatter: function(params) {
             if (params.value > 0) {
                return params.value;
             } else {
                return 0;
                     }
            }
            }
        }*/
        }]
    };
    if (languageNow === "zh_CN") {
        statpic.setOption(optionCn);
    } else {
        statpic.setOption(optionEn);
    }
}


//-----日产量获取并计算-----//
function proCount() {
    return;

    var hajimems = moment(start.value).valueOf();
    hajime = moment(start.value);
    console.log(hajime);
    owari = moment(end.value);
    console.log(owari);
    var days = 1 + (owari.diff(hajime, 'days'));  //算出选定时间的相隔天数
    var months = 1 + (owari.diff(hajime, 'months'))  //算出选定时间的相隔月数
    console.log(months);
    var oneday = 86400000;
    var onemonth = 2678400000;
    var proRow = [];
    var xRow = [];
    var selecVal = parseInt($('#sel1 option:selected').val());
    console.log(selecVal);
    var owariend;
    var now;
    var after;
    //if(months <= 1){
    for (var i = 1; i <= days; i++) {
        var car = [];
        var times = 0;
        now = hajimems + (i - 1) * oneday;
        console.log(now);
        after = hajimems + i * oneday - 1;
        console.log(after);
        var one_body = {
            "Item": ["TimeStamp_Daily", dataTmp2[selecVal]],
            "N": 20000,
            "Size": 104857600,
            "Timeout": 2147483647,
            "Query": "(TimeStamp >=" + now + ") AND (TimeStamp <=" + after + ")",
            "Sort": [{
                "Target": "TimeStamp",
                "Ascending": false
            }]
        };
        $.ajax({
            type: "POST",
            url: basicUrl +
                "factories/" + controllerLocation.factory +
                "/lines/" + controllerLocation.line +
                "/cells/" + controllerLocation.cell +
                "/controllers/" + controllerLocation.controller +
                "/apps/0022000000000000/analysis/search",
            async: false,
            cache: false,
            data: JSON.stringify(one_body),
            contentType: "application/json",
            dataType: "json",
            headers: {
                "YCP-API-TOKEN": servertoken
            },
            success: function (shit) {
                console.log(shit.Data)
                for (y = 0; y < shit.Data.length; y++) {
                    console.log(dataTmp2[selecVal]);
                    var kaikai = dataTmp2[selecVal];
                    var cardata = shit.Data[y][kaikai];
                    console.log(cardata);
                    if (shit.Data[y].TimeStamp_Daily != undefined) {
                        proRow.push(cardata);
                        xRow.push(moment(after).format('MM-DD'));
                    }
                }
                //console.log(car);
                /*var selecVal = $('#sel1 option:selected').val();
                console.log(selecVal);
                if(selecVal==0){
                    times = car.length;
                }
                else{
                    for(var z=0; z< car.length; z++){
                        if(car[z] == selecVal){
                        times ++;
                        }
                        }
                }
                console.log(times);
                   proRow.push(times);
                   console.log(proRow);*/
                //xRow.push(moment(after).format('MM-DD'));
                console.log(xRow);
            }
        });
    }
    statChart(proRow, xRow);
    //}
    /*else{
            for(var i = 1;i <= months;i++){
            var car = [];
            var times = 0;
            now = hajimems+(i-1)*onemonth;
            console.log(now);
            after = hajimems+i*onemonth;
            console.log(after);
            var one_body = {
            "Item":["TimeStamp","TimeStamp_Daily","CarType"],
            "N": 20000,
            "Size" : 104857600,
            "Timeout" : 2147483647,
            "Query": "(TimeStamp >=" + now + ") AND (TimeStamp <=" + after + ")",
            "Sort": [{
                "Target": "TimeStamp",
                "Ascending": true
            }]
        };
        $.ajax({
        type:"POST",
        url:basicUrl +
            "factories/" + controllerLocation.factory +
            "/lines/" + controllerLocation.line +
            "/cells/" + controllerLocation.cell +
            "/controllers/" + controllerLocation.controller +
            "/apps/0022000000000000/analysis/search",
        async:false,
        cache: false,
        data: JSON.stringify(one_body),
        contentType: "application/json",
        dataType: "json",
        headers: {
                "YCP-API-TOKEN": servertoken
            },
        success:function(shit){
            for (y=0 ; y < shit.Data.length ; y++){
                var cardata = shit.Data[y].CarType
          if(shit.Data[y].TimeStamp_Daily != undefined){
                car.push(cardata)
              }
            }
            console.log(car);
            var selecVal = $('#sel1 option:selected').val();
            console.log(selecVal);
            if(selecVal==0){
                times = car.length;
            }
            else{
                for(var z=0; z< car.length; z++){
                    if(car[z] == selecVal){
                    times ++;
                    }
                    }
            }
            //console.log(times);
               proRow.push(times);
               //console.log(proRow);
               console.log(moment(now).format('MM'));
               xRow.push(moment(now).format('MMM'));
        }
    });
        }
    statChart(proRow,xRow);
    }*/
}


function getRepeat(arr, value) {
    var count = 0;
    for (var i = 0; i < arr.length; i++) {
        if (arr[i] == value) {
            count++
        }
    }
    return count
}


function getMax(x, y, z, passport) {
    return;
    var trq_x = [];
    var trq_y = [];
    var trq_z = [];
    var xTime = [];
    hajime = moment(start.value).valueOf();
    owari = moment(end.value).valueOf() + 86399000;
    getToken();
    console.log(passport);
    var dev_body = {
        "Item": ["TimeStamp", "TimeStamp_Daily", x, y, z],
        "N": 20000,
        "Size": 104857600,
        "Timeout": 2147483647,
        "Query": "(TimeStamp >=" + hajime + ") AND (TimeStamp <=" + owari + ")",
        "Sort": [{
            "Target": "TimeStamp",
            "Ascending": true
        }]
    };
    console.log(dev_body);
    $.ajax({
        type: "POST",
        url: basicUrl +
            "factories/" + controllerLocation.factory +
            "/lines/" + controllerLocation.line +
            "/cells/" + controllerLocation.cell +
            "/controllers/" + controllerLocation.controller +
            "/apps/0022000000000000/analysis/search",
        async: false,
        cache: false,
        data: JSON.stringify(dev_body),
        contentType: "application/json",
        dataType: "json",
        headers: {
            "YCP-API-TOKEN": passport
        },
        success: function (hululu) {
            for (var i = 0; i < hululu.Data.length; i++) {
                if (hululu.Data[i].TimeStamp_Daily != undefined) {
                    //console.log(hululu.Data);
                    trq_x.push(hululu.Data[i][x]);
                    //console.log(x);
                    trq_y.push(hululu.Data[i][y]);
                    trq_z.push(hululu.Data[i][z]);
                    xTime.push(moment(hululu.Data[i].TimeStamp_Daily).format('MM-DD'));
                    console.log(hululu.Data[i].TimeStamp_Daily);
                }
            }
        }
    });
    console.log(trq_x);
    console.log(xTime);
    drawTrqGrph(trq_x, trq_y, trq_z, xTime);
}


function drawTrqGrph(a, b, c, d) {
    var chartTrq = echarts.init(document.getElementById("statsWindow"));
    var optionTrq = {
        title: {
            text: 'MaxTrqGraph',
            backgroundColor: '#2F5597',
            borderRadius: 5,
            fontSize: 30,
            textStyle: {
                color: '#FFFFFF',
                fontSize: 25
            }
        },
        tooltip: { trigger: 'axis' },
        legend: {
            data: ['X-TrqMax', 'Y-TrqMax', 'Z-TrqMax'],
            y: 'bottom'
        },
        xAxis: {
            data: d
        },
        yAxis: {},
        series: [{
            name: 'X-TrqMax',
            color: 'blue',
            type: 'line',
            data: a
        },
        {
            name: 'Y-TrqMax',
            color: 'red',
            type: 'line',
            data: b
        }, {
            name: 'Z-TrqMax',
            color: 'green',
            type: 'line',
            data: c
        }
        ]
    };
    chartTrq.setOption(optionTrq);
}

