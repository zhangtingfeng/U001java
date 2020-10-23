<template>
    <div>


        <div class="box">
            <div class="box-header width-border bg-gray">
                <h3 class="box-title">签章信息</h3>
                <div class="box-tools pull-right">
                    <button type="button" class="btn btn-box-tool" data-widget="collapse">
                        <i :class='[readonly?"fa fa-plus":"fa fa-minus"]'></i>
                    </button>
                </div>
            </div>

            <div v-if="!readonly" class="box-body">
                <div style="display: none;">
                    <iframe frameborder="0" id="iframeMapViewComponent"
                            name="iframeMap" ref="iframeDom"
                            scrolling="no" v-bind:src="getPageUrl"
                    ></iframe>
                </div>
                <button @click="signhtml('')" class="btn btn-primary"
                        style="margin-left:2%;width:6%;min-width: 100px;"
                        type="button">签章
                </button>

                <button @click="signpreview('')"
                        v-if="contractSignInfo.return_Sign==null || contractSignInfo.return_Sign==''"
                        class="btn btn-primary"
                        style="margin-left:2%;width:6%;min-width: 100px;"
                        type="button">预览原件
                </button>

                <button @click="signwatch('')" v-if="ifThisArgsHavedChecked"
                        class="btn btn-primary"
                        style="margin-left:2%;width:6%;min-width: 100px;"
                        type="button">查看合同
                </button>
            </div>


            <div v-else-if="readonly" class="box-body">


                <button @click="signpreview('')" v-if="!ifThisArgsHavedChecked"
                        class="btn btn-primary"
                        style="margin-left:2%;width:6%;min-width: 100px;"
                        type="button">预览原件
                </button>

                <button @click="signwatch('')" v-if="ifThisArgsHavedChecked"
                        class="btn btn-primary"
                        style="margin-left:2%;width:6%;min-width: 100px;"
                        type="button">查看合同
                </button>
            </div>
        </div>


    </div>
</template>

<script>
    import {comps, dict, editors} from "@f/framework";
    import {msg, ajax, progress} from "@f/vendor";

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
                getPageUrl: 'signZhang.bak',

                curargs: 0,
                ifThisArgsHavedChecked: false
            }
        },
        props: ["view", "id", "readonly", "args", "base", "orderFulldetail"],
        async beforeMount() {
            //  debugger;
            this.serverValueorderFulldetail = this.orderFulldetail;
            this.setServerValue();
            if (this.args > -1) {
                this.curargs = this.args;
            } else {
                this.curargs = 2;
            }


            this.load();
            await this.getCheckServerSignFile();
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
                window.frames['iframeMap'].lodaTable(dataList[0], dataList[1], "sign", this.id, this.contractSignInfo);
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

                //href的{}不能少
                //let {href} = this.$router.resolve({path:'/api/order/pdfSourceShow/file/S209C00031_Source.pdf'});
                //window.open(href, '_blank');
///http://localhost:92/api/order/pdfContact/returnfile/S209C00031_Sign.pdf   http://localhost:92/api/order/pdfSourceShow/file/S209C00031_Source.pdf

                ///http://localhost:92/api/order/pdfSourceShow/pdffile/S209C00031_Source.pdf
                //let dddd = this.contract;api/order/pdfSourceShow/file/S209C00031_Source.pdf
                //alert("签章流程将调用。（待开发sign1！）");
                // let dataList = await this.getPDFShow("previewPdf");
                //debugger;
                // window.frames['iframeMap'].lodaTable(dataList[0], dataList[1], "previewPdf", this.Id, this.contractSignInfo);
                //  wid
                // this.invokeHtmlMethod()
            },
            async signwatch() {
                window.open("api/order/pdfSourceShow/file/" + this.contract.orderId + "_Sign" + this.args, "_blank");
                ///http://localhost:92/api/order/pdfContact/returnfile/S209C00031_Sign.pdf
                //debugger;
                //let ddddcontract = this.contract;
                //alert("签章流程将调用。（待开发sign1！）");
                //let dataList = await this.getPDFShow("viewPdf");
                //window.frames['iframeMap'].lodaTable(dataList[0], dataList[1], "viewPdf", this.Id, this.contractSignInfo);

                // this.invokeHtmlMethod()
            },

            async getCheckServerSignFile(argType) {
                // debugger;
                let curargs = this.curargs;
                let strJson = ({checkFile: curargs, OrderID: this.id});

                let letStringreturnBody = await ajax.post({
                    url: "api/order/pdfShow/ChecksignData",
                    data: strJson
                });
                this.ifThisArgsHavedChecked = letStringreturnBody.ThisArgsHavedChecked;
                // debugger;
            },

            async getPDFShow(argType) {
                let thiscontract = this.contract;


                //debugger;
                //let letStringPDFFile=JSON.stringify(PDFFile);
                //let letStringreturnBody=JSON.stringify(returnBodypdfShowsignData);
                debugger;
                let RetrunletStringPDFFile = await this.makepdfForView();
                let letStringreturnBody = null;

                let returnBodypdfShowsignData = {
                    "filePath": "",
                    "pdfOutPath": "",
                    "signUser": "test",
                    "contractNumber": thiscontract.orderId,
                    "requestId": "0000000001",
                    "sign_data": "",
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
                    "returnUrl": this.contractSignInfo.signRetrunServer + "/api/order/pdfShow/signReturnData?args=" + this.args,
                    "businessParam": thiscontract.orderId
                };
                debugger;
///http://localhost:92/api/order/pdfContact/returnfile/S209C00031_Sign.pdf
                switch (argType) {
                    case "sign":
                        letStringreturnBody = await ajax.post({
                            url: "api/order/pdfShow/signData",
                            data: returnBodypdfShowsignData
                        });
                        break;
                    default:
                        returnBodypdfShowsignData.letStringreturnBody = await ajax.post({
                            url: "api/order/pdfShow/getSignData",
                            data: returnBodypdfShowsignData
                        });
                        break;
                }


                let letReturnDataList = [RetrunletStringPDFFile.data, letStringreturnBody.data];
                // debugger;
                return letReturnDataList;
                // debugger;
            },

            async makepdfForView() {
                let _this = this;

                await this.initFullDetailOrder();


                let thiscontract = this.contract;
                let thiscontractgoods = this.orderFulldetail.orderGoods;
                //  let thiscontractprops = this.orderFulldetail.props;


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
                debugger;

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
                        "f.3.c": letarrayChanPinMingCheng.includes("f.3.c") ? "1" : "0",
                        "f.4.c": letarrayChanPinMingCheng.includes("f.4.c") ? "1" : "0",
                        "f.5.c": letarrayChanPinMingCheng.includes("f.5.c") ? "1" : "0",
                        "f.6.c": letarrayChanPinMingCheng.includes("f.6.c") ? "1" : "0",
                        "f.7.c": letarrayChanPinMingCheng.includes("f.7.c") ? "1" : "0",
                        "f.8.c": letarrayChanPinMingCheng.includes("f.8.c") ? "1" : "0",
                        "f.9.c": letarrayChanPinMingCheng.includes("f.9.c") ? "1" : "0",
                        "f.10.c": letarrayChanPinMingCheng.includes("f.10.c") ? "1" : "0",
                        "f.11":letf11,
                        "f.12.c": letarrayChanDI.includes("f.12.c") ? "1" : "0",
                        "f.13.c": letarrayChanDI.includes("f.13.c") ? "1" : "0",
                        "f.14.c": letarrayChanDI.includes("f.14.c") ? "1" : "0",
                        "f.15.c": letarrayChanDI.includes("f.15.c") ? "1" : "0",
                        "f.16.c": letarrayChanDI.includes("f.16.c") ? "1" : "0",
                        "f.17.c": letarrayChanDI.includes("f.17.c") ? "1" : "0",
                        "f.18.c": letarrayChanDI.includes("f.18.c") ? "1" : "0",
                        "f.19.c": letarrayChanDI.includes("f.19.c") ? "1" : "0",
                        "f.20.c": letarrayChanDI.includes("f.20.c") ? "1" : "0",
                        "f.21.c": letarrayChanDI.includes("f.21.c") ? "1" : "0",
                        "f.22.c": letarrayChanDI.includes("f.22.c") ? "1" : "0",
                        "f.23.c": letarrayChanDI.includes("f.23.c") ? "1" : "0",
                        "f.24": letf24,
                        "f.25.c": thiscontract.needpack == "需要加工包装" ? "1" : "0",
                        "f.26.c": thiscontract.needpack == "不需要加工包装" ? "1" : "0",
                        "f.27.c": thiscontract.pack == "乙方提供加工包装服务" ? "1" : "0",
                        "f.28.c": thiscontract.pack == "甲方委托汇农平台提供加工包装" ? "1" : "0",
                        "f.29": thiscontract.pack_remark,
                        "f.30": letspecificationsListDanWei,
                        "f.31": AllCount,
                        "f.32.c": thiscontract.specificationsList && thiscontract.specificationsList[0].ifcheck ? "1" : "0",
                        "f.33.c": thiscontract.specificationsList && thiscontract.specificationsList[1].ifcheck ? "1" : "0",
                        "f.34.c": thiscontract.specificationsList && thiscontract.specificationsList[2].ifcheck ? "1" : "0",
                        "f.35.c": thiscontract.specificationsList && thiscontract.specificationsList[3].ifcheck ? "1" : "0",
                        "f.36.c": thiscontract.specificationsList && thiscontract.specificationsList[4].ifcheck ? "1" : "0",
                        "f.37.c": thiscontract.specificationsList && thiscontract.specificationsList[5].ifcheck ? "1" : "0",
                        "f.38.c": thiscontract.specificationsList && thiscontract.specificationsList[6].ifcheck ? "1" : "0",
                        "f.39.c": thiscontract.specificationsList && thiscontract.specificationsList[7].ifcheck ? "1" : "0",
                        "f.40.c": thiscontract.specificationsList && thiscontract.specificationsList[8].ifcheck ? "1" : "0",
                        "f.41.c": thiscontract.specificationsList && thiscontract.specificationsList[9].ifcheck ? "1" : "0",
                        "f.42.c": thiscontract.specificationsList && thiscontract.specificationsList[10].ifcheck ? "1" : "0",
                        "f.43.c": thiscontract.specificationsList && thiscontract.specificationsList[11].ifcheck ? "1" : "0",
                        "f.44.c": thiscontract.specificationsList && thiscontract.specificationsList[12].ifcheck ? "1" : "0",
                        "f.45.c": thiscontract.specificationsList && thiscontract.specificationsList[13].ifcheck ? "1" : "0",
                        "f.46.c": thiscontract.specificationsList && thiscontract.specificationsList[14].ifcheck ? "1" : "0",
                        "f.47.c": thiscontract.specificationsList && thiscontract.specificationsList[15].ifcheck ? "1" : "0",
                        "f.48.c": thiscontract.specificationsList && thiscontract.specificationsList[16].ifcheck ? "1" : "0",
                        "f.49.c": thiscontract.specificationsList && thiscontract.specificationsList[17].ifcheck ? "1" : "0",
                        "f.50.c": thiscontract.specificationsList && thiscontract.specificationsList[18].ifcheck ? "1" : "0",
                        "f.51.c": thiscontract.specificationsList && thiscontract.specificationsList[19].ifcheck ? "1" : "0",
                        "f.52.c": thiscontract.specificationsList && thiscontract.specificationsList[20].ifcheck ? "1" : "0",
                        "f.53.c": thiscontract.specificationsList && thiscontract.specificationsList[21].ifcheck ? "1" : "0",
                        "f.54.c": thiscontract.specificationsList && thiscontract.specificationsList[22].ifcheck ? "1" : "0",
                        "f.55.c": thiscontract.specificationsList && thiscontract.specificationsList[23].ifcheck ? "1" : "0",
                        "f.56.c": thiscontract.specificationsList && thiscontract.specificationsList[24].ifcheck ? "1" : "0",
                        "f.57.c": thiscontract.specificationsList && thiscontract.specificationsList[25].ifcheck ? "1" : "0",
                        "f.58.c": thiscontract.specificationsList && thiscontract.specificationsList[26].ifcheck ? "1" : "0",
                        "f.59.c": thiscontract.specificationsList && thiscontract.specificationsList[27].ifcheck ? "1" : "0",
                        "f.60.c": thiscontract.specificationsList && thiscontract.specificationsList[28].ifcheck ? "1" : "0",
                        "f.61.c": thiscontract.specificationsList && thiscontract.specificationsList[29].ifcheck ? "1" : "0",
                        "f.62.c": thiscontract.specificationsList && thiscontract.specificationsList[30].ifcheck ? "1" : "0",
                        "f.63.c": thiscontract.specificationsList && thiscontract.specificationsList[31].ifcheck ? "1" : "0",
                        "f.64.c": thiscontract.specificationsList && thiscontract.specificationsList[32].ifcheck ? "1" : "0",
                        "f.65.c": thiscontract.specificationsList && thiscontract.specificationsList[33].ifcheck ? "1" : "0",
                        "f.66.c": thiscontract.specificationsList && thiscontract.specificationsList[34].ifcheck ? "1" : "0",
                        "f.67.c": thiscontract.specificationsList && thiscontract.specificationsList[35].ifcheck ? "1" : "0",
                        "f.68.c": thiscontract.specificationsList && thiscontract.specificationsList[36].ifcheck ? "1" : "0",
                        "f.69.c": thiscontract.specificationsList && thiscontract.specificationsList[37].ifcheck ? "1" : "0",
                        "f.70.c": thiscontract.specificationsList && thiscontract.specificationsList[38].ifcheck ? "1" : "0",
                        "f.72.c": thiscontract.specificationsList && thiscontract.specificationsList[39].ifcheck ? "1" : "0",
                        "f.73.c": thiscontract.specificationsList && thiscontract.specificationsList[40].ifcheck ? "1" : "0",
                        "f.74.c": thiscontract.specificationsList && thiscontract.specificationsList[41].ifcheck ? "1" : "0",
                        "f.75.c": thiscontract.specificationsList && thiscontract.specificationsList[42].ifcheck ? "1" : "0",
                        "f.76.c": thiscontract.specificationsList && thiscontract.specificationsList[43].ifcheck ? "1" : "0",
                        "f.77.c": thiscontract.specificationsList && thiscontract.specificationsList[44].ifcheck ? "1" : "0",
                        "f.78.c": thiscontract.specificationsList && thiscontract.specificationsList[45].ifcheck ? "1" : "0",
                        "f.79": this.getTotal(),
                        "f.80": this.getTotal(),
                        "f.81": this.convertCurrency(this.getTotal()),
                        "f.82":thiscontract.payDay,
                        "f.83": thiscontract.pay_type,
                        "f.84": thiscontract.delivery_Start_dt,
                        "f.85":thiscontract.delivery_End_dt,
                    }
                };
                //debugger;
                let letStringPDFFile = null;
                try {
                    let letargs = _this.args;
                    if (letargs == null || letargs == "") {
                        letargs = 0;
                    }
                    letStringPDFFile = await ajax.post({
                        url: "api/order/pdfShow/apiPdfUpload/" + letargs,
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
            convertCurrency(currencyDigits) {
                let MAXIMUM_NUMBER = 99999999999.99;
                let CN_ZERO = "零";
                let CN_ONE = "壹";
                let CN_TWO = "贰";
                let CN_THREE = "叁";
                let CN_FOUR = "肆";
                let CN_FIVE = "伍";
                let CN_SIX = "陆";
                let CN_SEVEN = "柒";
                let CN_EIGHT = "捌";
                let CN_NINE = "玖";
                let CN_TEN = "拾";
                let CN_HUNDRED = "佰";
                let CN_THOUSAND = "仟";
                let CN_TEN_THOUSAND = "万";
                let CN_HUNDRED_MILLION = "亿";
                let CN_SYMBOL = "";
                let CN_DOLLAR = "元";
                let CN_TEN_CENT = "角";
                let CN_CENT = "分";
                let CN_INTEGER = "整";


                let integral;
                let decimal;
                let outputCharacters;
                let parts;
                let digits, radices, bigRadices, decimals;
                let zeroCount;
                let i, p, d;
                let quotient, modulus;


                currencyDigits = currencyDigits.toString();
                if (currencyDigits == "") {
                    return "";
                } else {
                    let strtemp = currencyDigits.split("-");
                    if (strtemp.length > 1) {
                        currencyDigits = strtemp[1]
                    }
                }
                if (currencyDigits.match(/[^,.\d]/) != null) {
                    return "";
                }
                if ((currencyDigits).match(/^((\d{1,3}(,\d{3})*(.((\d{3},)*\d{1,3}))?)|(\d+(.\d+)?))$/) == null) {
                    return "";
                }


                currencyDigits = currencyDigits.replace(/,/g, "");
                currencyDigits = currencyDigits.replace(/^0+/, "");

                if (Number(currencyDigits) > MAXIMUM_NUMBER) {
                    return "";
                }
                parts = currencyDigits.split(".");
                if (parts.length > 1) {
                    integral = parts[0];
                    decimal = parts[1];
                    decimal = decimal.substr(0, 2);
                } else {
                    integral = parts[0];
                    decimal = "";
                }
                digits = new Array(CN_ZERO, CN_ONE, CN_TWO, CN_THREE, CN_FOUR, CN_FIVE, CN_SIX, CN_SEVEN, CN_EIGHT, CN_NINE);
                radices = new Array("", CN_TEN, CN_HUNDRED, CN_THOUSAND);
                bigRadices = new Array("", CN_TEN_THOUSAND, CN_HUNDRED_MILLION);
                decimals = new Array(CN_TEN_CENT, CN_CENT);
                outputCharacters = "";
                if (Number(integral) > 0) {
                    zeroCount = 0;
                    for (i = 0; i < integral.length; i++) {
                        p = integral.length - i - 1;
                        d = integral.substr(i, 1);
                        quotient = p / 4;
                        modulus = p % 4;
                        if (d == "0") {
                            zeroCount++;
                        } else {
                            if (zeroCount > 0) {
                                outputCharacters += digits[0];
                            }
                            zeroCount = 0;
                            outputCharacters += digits[Number(d)] + radices[modulus];
                        }
                        if (modulus == 0 && zeroCount < 4) {
                            outputCharacters += bigRadices[quotient];
                        }
                    }
                    outputCharacters += CN_DOLLAR;
                }

                if (decimal != "") {
                    for (i = 0; i < decimal.length; i++) {
                        d = decimal.substr(i, 1);
                        if (d != "0") {
                            outputCharacters += digits[Number(d)] + decimals[i];
                        }
                    }
                }
                if (outputCharacters == "") {
                    outputCharacters = CN_ZERO + CN_DOLLAR;
                }
                if (decimal == "" || decimal == 0) {
                    outputCharacters += CN_INTEGER;
                }
                outputCharacters = CN_SYMBOL + outputCharacters;
                return outputCharacters;
            }
            ,
        }
    }
</script>

<style lang="less" scoped>
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
