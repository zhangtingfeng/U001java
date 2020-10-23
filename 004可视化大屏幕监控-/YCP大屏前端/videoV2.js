//document.write("<script language=javascript src=’/js/import.js’></script>");


var vedioTime = 1000 * 30;
var verCurrentargnewSource = null;
var verCurrentargnewSourceIni = false;
var checkIfStopcurrentTime = 0;
var varaddTime = 8;


function newshowVideoV2() {
    /*debugger;

    if (verImdieCurrentargnewSourceIni==false){
        verImdieCurrentargnewSourceIni=true;
        setTimeout(newshowVideo(), 3000);
        return;
    }
    alert(1111);*/
    Source = this.videoSource;
    setPlayerSourceV2(Source, -1);
}


function setPlayerSourceV2(newSourceList, iiii) {
    iiii++;
    if (iiii >= newSourceList.length - 1) {
        iiii = 0;
    }
    let newSource = newSourceList[iiii];
    verCurrentargnewSource = newSource;
    replay();
    setTimeout(function () {
        setPlayerSourceV2(newSourceList, iiii);
    }, vedioTime);


}

var videoObject = {
    init: function (id, src) {
        var self = this;
        this.src = src;
        this.id = id;
        this.flvPlayer = flvjs.createPlayer({type: 'flv', url: src, isLive: true, enableStashBuffer: false}, {});
        this.flvPlayer.attachMediaElement(document.getElementById(id));
        this.flvPlayer.load();

        this.flvPlayer.play();
        return this;
    }
}


function replay() {

    if (verCurrentargnewSource != null) {
        if (window.v) {
            window.v.flvPlayer.unload();
            window.v.flvPlayer.destroy();
        }
        window.v = videoObject.init("test_video", "http://"+videoServerIP + ":8081/live/" + window.btoa(verCurrentargnewSource) + "/live.flv");


        setTimeout("ifCanStep()", 2000);



    }
}

function ifCanStep(){

    if (varaddTime > 0) {
        console.warn(varaddTime);

        checkIfStopcurrentTime = window.v.flvPlayer.currentTime;
        if (checkIfStopcurrentTime>0){
            setTimeout("checkIfStop()", 5000);
            window.v.flvPlayer.currentTime = window.v.flvPlayer.currentTime + varaddTime;
        }
    }
}

function checkIfStop() {
    if (checkIfStopcurrentTime == window.v.flvPlayer.currentTime) {
        varaddTime = varaddTime - 0.5;
        console.warn(varaddTime);
        alert(varaddTime);
        replay();
    }
}
