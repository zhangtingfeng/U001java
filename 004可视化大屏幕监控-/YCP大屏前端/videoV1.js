//document.write("<script language=javascript src=’/js/import.js’></script>");


var vedioTime = 1000 * 30;


function newshowVideoV1() {

    //debugger;
    //alert("111");
    Source = this.videoSource;
    setPlayerSourceV1(Source, 0);
}



if (window.Streamedian) {
    let errHandler = function (err) {
        console.warn(err);
    };

    let infHandler = function (inf) {
        //let sourcesNode = document.getElementById("sourcesNode");
        // let clients = inf.clients;
        //sourcesNode.innerHTML = "";
    };


    var html5Player = document.getElementById("test_video");
    var playerOptions = {
        socket: "ws://localhost:8088/ws/", redirectNativeMediaErrors: true,
        bufferDuration: 30,
        errorHandler: errHandler
    };
    var player = Streamedian.player('test_video', playerOptions);

    function setPlayerSourceV1(newSourceList, iiii) {
        iiii++;
        if (iiii >= newSourceList.length - 1) {
            iiii = 0;
        }
        let newSource = newSourceList[iiii];
        setTimeout(function () {
            setPlayerSourceV1(newSourceList, iiii);
        }, vedioTime);
//debugger;




        player.destroy();
        player = null;
        html5Player.src = newSource;


        player = Streamedian.player("test_video", playerOptions);
        setTimeout("setAdjust()", 1000);
        //html5Player.play();
        //setTimeout(triggerClick, 40);
    }


}

function setAdjust(){
    let html5Player = document.getElementById('test_video');
    if ( html5Player.buffered!=0){
        html5Player.currentTime = html5Player.buffered&& html5Player.buffered.end?html5Player.buffered.end(0):0;
        html5Player.playbackRate = 1;
    }
}
