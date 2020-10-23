<template>
    <div>


        <div class="box boxbackgroundcolor">
            <div class="box-header width-border  bg-lightgreen">
                <h3 class="box-title">签章信息</h3>
                <div class="box-tools pull-right">
                    <button type="button" class="btn btn-box-tool" data-widget="collapse">
                        <i :class='[readonly?"fa fa-plus":"fa fa-minus"]'></i>
                    </button>
                </div>
            </div>

            <div v-if="!readonly" class="box-body boxbody" style="padding-left: 15px;padding-top: 10px !important;">
                <!-- <div style="display: none;">
                     <iframe frameborder="0" id="iframeMapViewComponent"
                             name="iframeMap" ref="iframeDom"
                             scrolling="no" v-bind:src="getPageUrl"
                     ></iframe>
                       <div class="row" style="padding-left: 15px;padding-top: 20px;">
                    <button type="button" class="btn btn-primary" style="padding-left: 50px;padding-right: 50px;"
                            @click="save()">保存
                    </button>
                </div>
                 </div>-->
                <button @click="sign('')" class="btn btn-primary"
                        v-if="!ifThisArgsHavedSigned"
                        style="width:132px;"
                        type="button">签章
                </button>

                <button @click="signpreview('')"
                        v-if="!contractSignInfo.return_Sign"
                        class="btn btn-primary"
                        style="width:132px;"
                        type="button">预览原件
                </button>

                <button @click="signwatch('')" v-if="contractSignInfo.return_Sign"
                        class="btn btn-primary"
                        style="width:132px;"
                        type="button">查看合同
                </button>
            </div>


            <div v-else-if="readonly" class="box-body boxbody" style="padding-left: 15px;padding-top: 10px !important;">


                <button @click="signpreview('')" v-if="!contractSignInfo.return_Sign"
                        class="btn btn-primary"
                        style="width:132px;"
                        type="button">预览原件
                </button>

                <button @click="signwatch('')" v-if="contractSignInfo.return_Sign"
                        class="btn btn-primary"
                        style="width:132px;"
                        type="button">查看合同
                </button>
            </div>
        </div>


    </div>
</template>

<script>
    import {comps, dict, editors} from "@f/framework";
    import {msg, ajax, progress} from "@f/vendor";
    import pubFuncton from "../../pubcommon";

    export default {
        components: {
            date: editors.get("date")
        },
        data() {
            return {

                serverValueorderFulldetail: {},
                contractSignInfo: {},
                seller: {},
                buyer: {},
                contract: {},
                specificationsList: [],
                total: 0,

                orderGoods: {},


                curargs: 0,
                ifThisArgsHavedSigned: false
            }
        },
        props: ["view", "id", "readonly", "args", "base", "orderFulldetail"],
        beforeMount() {
            //debugger;
            this.serverValueorderFulldetail = this.orderFulldetail;
            this.setServerValue();
            if (this.args > -1 && this.args) {
                this.curargs = this.args;
                // alert(1);
                // alert( 'this.curargs');
            } else {
                this.curargs = 1;
            }


            this.load();
            this.getCheckServerSignFile();
            //  debugger;
            //this.getTotal();
        },


        methods: {
            setServerValue() {
                this.contractSignInfo = JSON.parse(this.serverValueorderFulldetail.contractSignInfo);
                this.contract = this.serverValueorderFulldetail.contract;
                this.specificationsList = this.serverValueorderFulldetail.specificationsList;

                this.orderGoods = this.serverValueorderFulldetail.orderGoods;

            },
            load() {
                for (let t of this.serverValueorderFulldetail.dealers) {
                    if (t.type == "1") {
                        this.buyer = t;
                    } else if (t.type == "2") {
                        this.seller = t;
                    }
                }
                this.goods = this.serverValueorderFulldetail.goods;
            },

            async sign() {
                progress.show();

                let dataList = await this.getPDFShow("sign");
                // window.frames['iframeMap'].lodaTable(dataList[0], dataList[1], "sign", this.id, this.contractSignInfo);

                let dlform = document.createElement('form');
                dlform.style = "display:none;";
                dlform.method = 'post';
                dlform.action = this.contractSignInfo.signServer + "/pdfShow/apiPdfUpload/";
                dlform.target = '_blank';
                dlform.name = 'alipayment';
                dlform.id = 'alipayment';

                for (let item in dataList[2]) {
                    let letvalue = dataList[2][item];
                    // debugger;
                    let letthisinput = document.createElement('input');
                    // letthisinput.type = 'hidden';
                    letthisinput.name = item;
                    letthisinput.id = item;
                    letthisinput.value = letvalue;
                    dlform.appendChild(letthisinput);

                }


                //业务流程编号
                // debugger;
                let sign_data = document.createElement('input');
                // sign_data.type = 'hidden';
                sign_data.name = 'sign_data';
                sign_data.id = sign_data.name;
                sign_data.value = dataList[1];//所有页 骑缝章盖章页面类型
                dlform.appendChild(sign_data);
                //业务参数
                let file_base = document.createElement('input');
                //  base64File.type = 'hidden';
                file_base.name = 'file_base';
                file_base.id = "base64File";
                file_base.value = dataList[0];//所有页 骑缝章盖章页面类型
                dlform.appendChild(file_base);


                document.body.appendChild(dlform);
                //var params = $("#alipayment").serialize();
                //debugger;
                dlform.submit();
                document.body.removeChild(dlform);


                progress.hide();
                // this.invokeHtmlMethod()
            },
            async signpreview() {

                //debugger;
                let letHost = window.location.protocol + "//" + window.location.host;
                //alert(letHost);
                //return;
//url 为pdf路径
                await this.makepdfForView();
                window.open("api/order/pdfSourceShow/file/" + this.contract.orderId + "_Source", "_blank");


            },
            async signwatch() {
                window.open("api/order/pdfSourceShow/file/" + this.contract.orderId + "_Sign", "_blank");

            },

            getCheckServerSignFile() {

                let curargs = this.curargs;
                //    let strJson = ({checkFile: curargs, OrderID: this.id});

                //  let letStringreturnBody = await ajax.post({
                //    url: "api/order/pdfShow/ChecksignData",
                //     data: strJson
                //  })
                let curThisArgsHavedCheckedargsSignedfile = false;
              //  debugger;
                for (let i = 0; i < this.contractSignInfo.signRetrunList.length; i++) {
                 //   debugger;
                    if (this.contractSignInfo.signRetrunList[i]["args"] == curargs) {
                        curThisArgsHavedCheckedargsSignedfile = true;
                    }

                }


                this.ifThisArgsHavedSigned = curThisArgsHavedCheckedargsSignedfile;
                // debugger;
            },

            async getPDFShow() {
                let thiscontract = this.contract;


                //debugger;
                //let letStringPDFFile=JSON.stringify(PDFFile);
                //let letStringreturnBody=JSON.stringify(returnBodypdfShowsignData);
                //debugger;
                let RetrunletStringPDFFile = await this.makepdfForView();
                let letStringreturnBody = null;

                let returnBodypdfShowsignData = {
                    "filePath": "",
                    "pdfOutPath": "",
                    "signUser": "",
                    "contractNumber": thiscontract.orderId,
                    "requestId": "0000000002",
                    "certSource_1": "1",
                    "certSource": "1",
                    "certType_1": "1",
                    "certType": "1",
                    "sealCode": "00000001014006",
                    "datFile": "",
                    "account": "测试印章",
                    "userName": "北京安证通信息科技股份有限公司",
                    "userCertNum": "000000000001",
                    "sealType_1": "1",
                    "sealType": "1",
                    "sealSource_1": "1",
                    "sealSource": "1",
                    "imageBaseStr": "",
                    "centerName": "单位公章",
                    "isAddBar_1": "0",
                    "isAddBar": "0",
                    "barcodeContent": "",
                    "barcodeWidth": "",
                    "barcodeHeight": "",
                    "barPositionType_1": "2",
                    "barPositionType": "2",
                    "barXPosition": "",
                    "barYPosition": "",
                    "barPageNum": "",
                    "barKeyWord": "",
                    "isAddMuSeal_1": "1",
                    "isAddMuSeal": "1",
                    "sealWay_1": "2",
                    "sealWay": "2",
                    "leftOrRight_1": "right",
                    "leftOrRight": "right",
                    "cutCount": "2",
                    "yPosition": "100",
                    "isAddWater_1": "0",
                    "isAddWater": "0",
                    "waterPositionType_1": "1",
                    "waterPositionType": "1",
                    "waterXPosition": "",
                    "waterYPosition": "",
                    "waterPageNum": "",
                    "waterKeyWord": "",
                    "purOrgSeal": "",
                    "purPerSeal": "",
                    "supOrgSeal": "",
                    "supPerSeal": "",
                    "dragSeal_1": "1",
                    "dragSeal": "1",
                    "sealSignType_1": "1",
                    "sealSignType": "1",
                    "sealPosition": "",
                    "sealKeyword": "",
                    "operType_1": "1",
                    "operType": "1",
                    "returnUrl": this.contractSignInfo.signRetrunServer + "/api/order/pdfShow/signReturnData?args=" + this.curargs,
                    "businessParam": thiscontract.orderId,
                    "return_url": this.contractSignInfo.signRetrunServer + "/api/order/pdfShow/signReturnData?args=" + this.curargs,

                };


                // returnBodypdfShowsignData=returnBodypdfShowsignDatabak;
                //debugger;
///http://localhost:92/api/order/pdfContact/returnfile/S209C00031_Sign.pdf

                letStringreturnBody = await ajax.post({
                    url: "api/order/pdfShow/signData",
                    data: returnBodypdfShowsignData
                });


                let letReturnDataList = [RetrunletStringPDFFile.data, letStringreturnBody.data, returnBodypdfShowsignData];
                return letReturnDataList;
            },


            async makepdfForView() {
                let _this = this;
                await this.initFullDetailOrder();
                let thiscontract = this.contract;
                let thiscontractgoods = this.orderFulldetail.orderGoods;

                progress.show();
                //  debugger;
                //处理产品名称
                let letarrayChanPinMingCheng = new Array();
                let letf11 = "";
                //处理产品产地
                let letf24 = "";
                let letarrayChanDI = new Array();

                if (thiscontractgoods && thiscontractgoods.length > 0 && thiscontractgoods[0].hasOwnProperty("goods")) {
                    let letarrayChanDI = new Array();
                    ///检查苹果产地的其他

                    //  debugger;
                    for (let i = 0; i < thiscontractgoods.length; i++) {
                        let letgoodsName = thiscontractgoods[i].goods.goodsName;
                        let lettitle = thiscontractgoods[i].goods.title;

                        if (lettitle.indexOf("富士") != -1) {
                            letarrayChanPinMingCheng.push("f.3.c");

                            if (letgoodsName.indexOf("条红") != -1) {
                                letarrayChanPinMingCheng.push("f.4.c");
                            } else if (letgoodsName.indexOf("片红") != -1) {
                                letarrayChanPinMingCheng.push("f.5.c");
                            } else if (letgoodsName.indexOf("条拉片") != -1) {
                                letarrayChanPinMingCheng.push("f.6.c");
                            } else if (letgoodsName.indexOf("条片混") != -1) {
                                letarrayChanPinMingCheng.push("f.7.c");
                            }

                        } else if (lettitle.indexOf("维纳斯黄金") != -1) {
                            letarrayChanPinMingCheng.push("f.8.c");
                        } else if (lettitle.indexOf("王林") != -1) {
                            letarrayChanPinMingCheng.push("f.9.c");
                        }
                    }
                    if (letarrayChanPinMingCheng.length == 0) {
                        letarrayChanPinMingCheng.push("f.10.c");
                        letf11 = thiscontractgoods[0].goods.title;
                    }
                }

                if (thiscontractgoods && thiscontractgoods.length > 0 && thiscontractgoods[0].hasOwnProperty("goods") && thiscontractgoods[0].goods.hasOwnProperty("props") && thiscontractgoods[0].goods.props.length > 0) {
                    for (let i = 0; i < thiscontractgoods.length; i++) {
                        for (let j = 0; j < thiscontractgoods[i].goods.props.length; j++) {
                            let letpropName = thiscontractgoods[i].goods.props[j].propName;
                            if (letpropName.indexOf("苹果产地") != -1) {
                                let letpropValue = thiscontractgoods[i].goods.props[j].propValue;
                                if (letpropValue.indexOf("山东") != -1) {
                                    letarrayChanDI.push("f.12.c");
                                } else if (letpropValue.indexOf("陕西") != -1) {
                                    letarrayChanDI.push("f.13.c");
                                } else if (letpropValue.indexOf("山西") != -1) {
                                    letarrayChanDI.push("f.14.c");
                                } else if (letpropValue.indexOf("甘肃") != -1) {
                                    letarrayChanDI.push("f.15.c");
                                } else if (letpropValue.indexOf("新疆") != -1) {
                                    letarrayChanDI.push("f.16.c");
                                } else if (letpropValue.indexOf("河北") != -1) {
                                    letarrayChanDI.push("f.17.c");
                                } else if (letpropValue.indexOf("河南") != -1) {
                                    letarrayChanDI.push("f.18.c");
                                } else if (letpropValue.indexOf("云南") != -1) {
                                    letarrayChanDI.push("f.19.c");
                                } else if (letpropValue.indexOf("四川") != -1) {
                                    letarrayChanDI.push("f.20.c");
                                } else if (letpropValue.indexOf("辽宁") != -1) {
                                    letarrayChanDI.push("f.21.c");
                                } else if (letpropValue.indexOf("江苏") != -1) {
                                    letarrayChanDI.push("f.22.c");
                                } else {
                                    letarrayChanDI.push("f.23.c");
                                    letf24 = letpropValue;
                                }
                            }
                        }
                    }

                }


                ///计算产品数量
                let AllCount = 0;
                for (let i = 0; i < this.orderGoods.length; i++) {
                    let curAllCount = this.orderGoods[i].num;
                    AllCount += curAllCount;
                }
                //debugger;
                ////this.contract["specificationsList"] 单位：□若采购原果则为“斤”/□若采购成品则为“箱”
                let letspecificationsListDanWei = "箱";
                if (thiscontract.hasOwnProperty("specificationsList") && thiscontract.specificationsList.length > 0) {
                    for (let i = 0; i < thiscontract.specificationsList.length; i++) {
                        if (thiscontract.specificationsList[i].ifcheck) {
                            letspecificationsListDanWei = "斤";
                            break;
                        }
                    }
                }
                // debugger;
                thiscontract.needpack = pubFuncton.getShowTextaboutDicValue(thiscontract.needpack, await dict.get("contractneedpack"));
                thiscontract.pack = pubFuncton.getShowTextaboutDicValue(thiscontract.pack, await dict.get("contractpacksevices"));
                thiscontract.pay_type = pubFuncton.getShowTextaboutDicValue(thiscontract.pay_type, await dict.get("contractpaytype"));


                let PDFFile = {
                    "content": {
                        "filepath": thiscontract.orderId,
                        "p.0.0": thiscontractgoods && thiscontractgoods.length > 0 ? thiscontractgoods[0].goods.title : "",
                        "p.0.1": this.buyer.title,
                        "p.0.2": this.seller.title,
                        "1.1.0": thiscontractgoods && thiscontractgoods.length > 0 ? thiscontractgoods[0].goods.title : "",
                        "2.2.3.0": thiscontract.overduefine,
                        "4.4.1.0": thiscontract.pay_type,
                        "4.4.1.1": thiscontract.pay1Days,
                        "4.4.1.2": thiscontract.pay1Amount,
                        "4.4.1.3": thiscontract.pay2Amount,
                        "4.4.2.0": this.seller.bank,
                        "4.4.2.1": this.seller.bankTitle,
                        "4.4.2.2": this.seller.bankAccount,
                        "4.4.2.3": this.buyer.bank,
                        "4.4.2.4": this.buyer.bankTitle,
                        "4.4.2.5": this.buyer.bankAccount,
                        "7.7.1.0": thiscontract.validityperiod,
                        "8.8.1.0": this.buyer.contactor,
                        "8.8.1.1": this.buyer.contactPhone,
                        "8.8.1.2": this.buyer.contactAddress,
                        "8.8.2.0": this.seller.contactor,
                        "8.8.2.1": this.seller.contactPhone,
                        "8.8.2.2": this.seller.contactAddress,
                        "8.8.3.0": thiscontract.changesneedadvance,
                        "m.0": this.buyer.title,
                        "m.1": this.buyer.signer,
                        "m.2": this.buyer.phone,
                        "m.3": this.buyer.address,
                        "m.4": this.seller.title,
                        "m.5": this.seller.signer,
                        "m.6": this.seller.phone,
                        "m.7": this.seller.address,
                    },
                    "tabl": {
                        "f.0": this.buyer.title,
                        "f.1": this.seller.title,
                        "f.2": thiscontractgoods && thiscontractgoods.length > 0 ? thiscontractgoods[0].goods.title : "",
                        "f.11": letf11,
                        "f.24": letf24,
                        "f.25.c": thiscontract.needpack == "需要加工包装" ? "1" : "0",
                        "f.26.c": thiscontract.needpack == "不需要加工包装" ? "1" : "0",
                        "f.27.c": thiscontract.pack == "乙方提供加工包装服务" ? "1" : "0",
                        "f.28.c": thiscontract.pack == "甲方委托汇农平台提供加工包装" ? "1" : "0",
                        "f.29": thiscontract.pack_remark,
                        "f.30": letspecificationsListDanWei,
                        "f.31": AllCount,
                        "f.79": this.getTotal(),
                        "f.80": this.getTotal(),
                        "f.81": pubFuncton.convertCurrency(this.getTotal()),
                        "f.82": thiscontract.payDay,
                        "f.83": thiscontract.pay_type,
                        "f.84": thiscontract.delivery_Start_dt,
                        "f.85": thiscontract.delivery_End_dt,
                    }
                };


                /*
                                        "f.3.c": letarrayChanPinMingCheng.includes("f.3.c") ? "1" : "0",
                        "f.4.c": letarrayChanPinMingCheng.includes("f.4.c") ? "1" : "0",
                        "f.5.c": letarrayChanPinMingCheng.includes("f.5.c") ? "1" : "0",
                        "f.6.c": letarrayChanPinMingCheng.includes("f.6.c") ? "1" : "0",
                        "f.7.c": letarrayChanPinMingCheng.includes("f.7.c") ? "1" : "0",
                        "f.8.c": letarrayChanPinMingCheng.includes("f.8.c") ? "1" : "0",
                        "f.9.c": letarrayChanPinMingCheng.includes("f.9.c") ? "1" : "0",
                        "f.10.c": letarrayChanPinMingCheng.includes("f.10.c") ? "1" : "0", */
                let PDFFile_tabl_f_3_c_List = ["f.3.c", "f.4.c", "f.5.c", "f.6.c", "f.7.c", "f.8.c", "f.9.c", "f.10.c"];
                for (let item in PDFFile_tabl_f_3_c_List) {
                    let letThis = PDFFile_tabl_f_3_c_List[item];
                    PDFFile.tabl[letThis] = letarrayChanPinMingCheng.includes(letThis) ? "1" : "0";
                }
                let PDFFile_tabl_f_12_c_List = ["f.12.c", "f.13.c", "f.14.c", "f.15.c", "f.16.c", "f.17.c", "f.18.c", "f.19.c", "f.20.c", "f.21.c", "f.22.c", "f.23.c"];
                for (let item in PDFFile_tabl_f_12_c_List) {
                    let letThis = PDFFile_tabl_f_12_c_List[item];
                    PDFFile.tabl[letThis] = letarrayChanDI.includes(letThis) ? "1" : "0";
                }
                for (let i = 32; i < 78; i++) {
                    let letitem = "f." + i + ".c";
                    PDFFile.tabl[letitem] = thiscontract.specificationsList && thiscontract.specificationsList[i - 32].ifcheck ? "1" : "0";
                }


              //  debugger;
                let letStringPDFFile = null;
                try {

                    letStringPDFFile = await ajax.post({
                        url: "api/order/pdfShow/apiPdfUpload/" + this.curargs,
                        data: PDFFile
                    });
                } catch (e) {

                }
                //debugger;
                progress.hide();
                return letStringPDFFile;
            },


            async initFullDetailOrder() {
                try {
                    let letorderIdID = this.id;
                    // debugger;
                    let data = await ajax.post({
                        url: "api/order/info/getOrderDetail",
                        data: {orderId: letorderIdID},
                    })

                    let i = 1
                    for (var og of data.orderGoods) {
                        for (var g of data.goods) {
                            var props = []
                            for (var p of data.props) {
                                if (g.id == p.salesId) {
                                    props.push(p)
                                }
                            }
                            g.props = props
                            for (var m in data.images) {
                                if (g.id == m && data.images[m].length > 0) {
                                    g.images = data.images[m]
                                    g.img = "api/trade/upload/" + g.images[0].path
                                }
                            }
                            if (!g.images) {
                                g.img = "imgs/apples/pic_" + i + ".png"
                                if (i == 10) {
                                    i = 1
                                } else {
                                    i++
                                }
                            }
                            if (og.goodsId == g.id) {
                                og.goods = g
                                og.total = og.num * g.unitPrice
                            }
                        }
                    }
                    // debugger;
                    let letorderFulldetail = {}
                    letorderFulldetail.order = JSON.parse(data.order)
                    letorderFulldetail.orderGoods = data.orderGoods
                    letorderFulldetail.goods = data.goods
                    letorderFulldetail.dealers = data.dealers
                    letorderFulldetail.contract = data.contract
                    letorderFulldetail.contract["specificationsList"] = data.specificationsList
                    letorderFulldetail.payment_requestList = data.gettpayment_requestList;
                    letorderFulldetail.order_rdList = data.order_rdList;
                    letorderFulldetail.contractSignInfo = data.contractSignInfo;

                    this.serverValueorderFulldetail = letorderFulldetail;
                    this.setServerValue();
                    // debugger;
                    this.load();
                    await this.getCheckServerSignFile();
                    //debugger;
                } catch (err) {
                    //debugger
                    msg.info("重新获取订单详情失败,请检查登陆权限")
                }
            }
            ,

            getTotal() {
                //debugger;
                var total = 0;
                for (var og of this.serverValueorderFulldetail.orderGoods) {
                    total += parseFloat(og.amountPrice);
                }
                this.total = total;
                return total;

            }
            ,

        }
    }
</script>

<style lang="less" scoped>
    @import url("@/common/common.less");
    .panel-body {
        .row {
            margin: 0 0 10px 0;
        }

        p {
            font-weight: bold;
        }
    }

    .CSSBox {
        display: flex;
        flex-direction: row;
        -webkit-justify-content: space-between;
        justify-content: space-between;
    }

    .ButtonSave {
        align-items: flex-end;
    }


    .fade-enter-active,
    .fade-leave-active {
        transition: opacity .5s;
    }

    .fade-enter,
    .fade-leave-to
        /* .fade-leave-active below version 2.1.8 */ {
        opacity: 0;
    }
</style>
