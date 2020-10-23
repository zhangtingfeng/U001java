function startConfig() {

    ///以下项目，可以注释掉某些项目，就不会启动调试   1000表示是1秒


    autoLightRedBlueYellow();///先启动一下    //区域1数据
    let intervalautoLight = setInterval("autoLightRedBlueYellow()", 1000 * 5);//区域1数据  自动刷新灯泡的颜色           测试通过 生产现场的设备稼动情况由各处指示灯显示（绿色工作、黄色待机、红色警报），相关数据可直接从数据库中获取。单击机器铭牌可于右侧显示该机器的当前工作情况。机器模型需要与现场机器一起动。


    autoLightAutoClick();///设置点击  调试4 5 6   可以临时打开后续函数show040506(vartextShow)
    LatestDataOperateRateH02();//区域2  先启动一下    区域2  要求一秒 1秒  当日当前生产量和计划生产量，以进度条展示完成百分比，颜色随百分比变化而变化
    let intervalLatestDataOperateRateH02 = setInterval("LatestDataOperateRateH02()", 1000 * 60);//区域2  要求一秒 1秒

    LatestDataOperateRateH03();///区域3    当前生产总稼动率，可直接从数据库中获取。颜色随百分比变化而变化
    let intervalLatestDataOperateRateH03 = setInterval("LatestDataOperateRateH03()", 1000 * 60 * 20);//区域3  要求一秒 0.5小时

    //4 5 6 是点击灯的状态，在show040506中  可以临时打开后续函数show040506(vartextShow)
    varScreenTaskurl = "http://192.168.255.2:27070/ScreenTask.jpg";


    GetProdunctionHistory08();///区域8 获取生产历史纪录  从数据库直接获取该表中展示的每日生产数据（数据由客户造）。生产率(rate)需要自己计算（实际生产量除以计划生产量即可）。
    let interval1GetProdunctionHistory08 = setInterval("GetProdunctionHistory08()", 1000 * 60);//思路：从原始数据中监控M97的变化，此时为新产品制造开始时间/上一个产品制造结束时间，目前设备动作周期为60秒

    GetAlarmHistory09();///区域9    从数据库直接获取该表中展示的报警履历。
    let interval1GetAlarmHistory09 = setInterval("GetAlarmHistory09()", 1000 * 60 * 20);//思路：从原始数据中监控I97的变化，此时为新产品制造开始时间/上一个产品制造结束时间，目前设备动作周期为60秒

    ProductionD_10_analysis();//区域10  先启动一下  从数据库直接获取该图中的每日生产数据（数据由客户造）。生产率(rate)需要自己计算。展示包括柱状图和折线图。柱状图展示真实数据，折线图展示真实数据以及预测数据（预测数据由我方自己造）。 图表可放大/缩小以更改显示的时间窗口
    let interProductionD_10_analysis = setInterval("ProductionD_10_analysis()", 1000 * 60);//区域10数据 要求一秒 0.5小时


    Abnormal_11_analysis();//区域11 先启动一下  从数据库直接获取该表中展示的每日AI异常设备分析结果数据（数据由客户自己造）。

    // debugger;
    videoSource = ["rtsp://admin:yaskawa123@192.168.255.3:554/Streaming/Channels/102",
        // "rtsp://admin:yaskawa123@192.168.255.4:554/Streaming/Channels/102",
        "rtsp://admin:yaskawa123@192.168.255.5:554/Streaming/Channels/102",
        "rtsp://admin:yaskawa123@192.168.255.6:554/Streaming/Channels/102"];
    //debugger;
    videoServerIP = "localhost";
    setTimeout('newshowVideoV1()', 3000);

    //videoServerIP="192.168.1.212";
    //setTimeout('newshowVideoV2()', 3000);
    //();////区域7   现场监控直播（监控源由4个摄像头切换提供）

}


function show040506(vartextShow) {

    addUl05backimgData04(vartextShow);////  区域 4  从数据库直接获取图中展示的设备信息
    LifeSpanmainEchart05(vartextShow);//showEchart05();   区域5  柱状图  从数据库直接获取设备总寿命与剩余寿命，以进度条展示剩余寿命百分比，颜色随百分比变化而变化
    LifeSpanmainEchartStatus06(vartextShow);//showEchart05();  区域6 折线图    从数据库直接获取单台设备的稼动率（绿色工作、黄色待机、红色警报），以小时为单位通过折线图展示历史数据。图表可放大/缩小以更改显示的时间窗口。

}
