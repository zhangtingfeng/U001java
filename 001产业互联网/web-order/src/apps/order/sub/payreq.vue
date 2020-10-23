<template>
    <div>
        <div :class='[curReadOnly?"box collapsed-box boxbackgroundcolor":"box boxbackgroundcolor"]'>
            <div class="box-header width-border  bg-lightgreen">
                <h3 class="box-title">付款申请</h3>
                <div class="box-tools pull-right">
                    <button type="button" class="btn btn-box-tool" data-widget="collapse">
                        <i :class='[curReadOnly?"fa fa-plus":"fa fa-minus"]'></i>
                    </button>
                </div>
            </div>

            <div v-if="!curReadOnly " class="box-body boxbody">
                <div class="row">
                    <!-- <div v-if="curgettpayment_request.id!=null &&curgettpayment_request.id!=''" class="col-md-3">
                         <p>申请单编号</p><span> {{curgettpayment_request.id}}</span>
                     </div>-->
                    <div class="col-md-3">
                        <label>申请单标题</label><span> <input type="text" class="form-control"
                                                          name="curgettpayment_request.title"
                                                          v-model="curgettpayment_request.title"
                                                          placeholder="请输入申请单标题"/></span>
                    </div>

                   <!-- <div class="col-md-3">
                        <label>收款机构</label>
                        <select class="form-control" v-model="curgettpayment_request.receiptOrg">
                            选择项的value值默认选择项文本 可动态绑定选择项的value值 更改v-model指令绑定数据
                            <option v-for="item in accountList" :value="item.id">{{item.rltAcc}}</option>
                        </select>


                    </div>
                    <div class="col-md-3">
                        <label>收款账户</label><span><input type="text" class="form-control"
                                                        name="curgettpayment_request.receiptAccount"
                                                        v-model="curgettpayment_request.receiptAccount"
                                                        placeholder="请输入收款账户"/></span>
                    </div>
                    <div class="col-md-3">
                        <label>收款银行</label><span><input type="text" class="form-control"
                                                        name="curgettpayment_request.receiptBank"
                                                        v-model="curgettpayment_request.receiptBank"
                                                        placeholder="请输入收款银行"/></span>
                    </div>-->

                    <div class="col-md-3">
                        <label>付款金额</label><span><input type="number" class="form-control"
                                                        name="curgettpayment_request.amount"
                                                        v-model="curgettpayment_request.amount" placeholder="请输入付款金额"/></span>
                    </div>

                    <!-- <div class="col-md-3">
                         <p>申请单状态</p><span> {{curgettpayment_request.status}}</span>
                     </div>-->
                </div>

                <div class="row" style="padding-left: 15px;padding-top: 20px;">
                    <button type="button" class="btn btn-primary" style="width:132px;"
                            @click="save()">保存
                    </button>

                    <button @click="saveSunmit()" class="btn btn-primary"
                            style="width:132px;"
                            type="button">提交
                    </button>
                </div>


            </div>
            <div v-if="curReadOnly && curgettpayment_request" class="box-body boxbody">
                <div class="row">

                    <div class="col-md-3">
                        <label>申请单标题</label>
                        <span class="form-control">{{curgettpayment_request.title}}</span>
                    </div>

                    <div class="col-md-3">
                        <label>付款状态</label>
                        <span class="form-control">{{curgettpayment_request.status}}</span>
                    </div>

                    <div class="col-md-3">
                        <label>付款金额</label>
                        <span class="form-control">{{curgettpayment_request.amount}}</span>
                    </div>

                    <div class="col-md-3">
                        <label>付款方式</label>
                        <span class="form-control">{{curgettpayment_request.type}}</span>
                    </div>
                </div>
            </div>

        </div>


    </div>
</template>

<script>
    import {ajax, msg, progress} from "@f/vendor";
    import {comps, dict, editors} from "@f/framework";
    import pubFuncton from "../../pubcommon";

    export default {
        components: {
            date: editors.get("date")
        },
        data() {
            return {
                total: 0,
                order: this.orderFulldetail.order,
                curgettpayment_request: {},
                curReadOnly: this.readonly,
                selerdealer: {},
                accountList: [],
                orderGoods: this.orderFulldetail.orderGoods,
                contract: this.orderFulldetail.contract,
                ASTypeDicList: [], ASpayStatusTypeDicList: [],
            }
        },
        props: ["view", "id", "readonly", "args", "base", "orderFulldetail"],
        async beforeMount() {

            await this.getASTypeDicList();
            //debugger;
            let curgettpayment_requestList = this.orderFulldetail.payment_requestList;
//this.curReadOnly=!this.curReadOnly;
            let letCheckGet = false;
            let curargs = this.args;
        //    debugger;
            this.getTotal();
            debugger;
            // curargs=2;
            if (curargs == 1 && this.contract && this.contract.pay1Amount && this.total > 0) {
                this.curgettpayment_request.amount = (this.contract.pay1Amount * this.total * 0.01).toFixed(2);
            }
            if (curargs == 2 && this.contract && this.contract.pay2Amount && this.total > 0) {
                this.curgettpayment_request.amount = (this.contract.pay2Amount * this.total * 0.01).toFixed(2);
            }


            for (let i = 0; i < curgettpayment_requestList.length; i++) {
                if (curgettpayment_requestList[i].payPhase == curargs && curargs != null) {
                    this.curgettpayment_request = curgettpayment_requestList[i];
                    letCheckGet = true;
                    break;
                }
            }


            if (letCheckGet == false) {////都找不到，那就是新搞一个
                this.getselerdealer();
                this.curgettpayment_request.payPhase = curargs;
                if(curargs==1){
                    this.curgettpayment_request.paymentDt=this.contract.payDay;///计划付款时间
                }
                debugger;
                this.curgettpayment_request.paymentOrg = this.orderFulldetail.order.buyerId;///付款机构
                this.curgettpayment_request.receiptOrg = this.orderFulldetail.order.sellerId;///收款机构  请输入开户银行（买方）
                this.curgettpayment_request.receiptAccount = this.selerdealer.bankAccount;///收款账户
                this.curgettpayment_request.receiptBank = this.selerdealer.bank;///收款银行
            }
            /*申请单状态:1 申请 2 已审核 3 已支付 已支付*/
            if (this.curgettpayment_request.status) {
                this.curReadOnly = true;
            }



            //debugger;
            if (this.curReadOnly && letCheckGet) {

                this.curgettpayment_request.receiptOrg = await pubFuncton.getDescFunction(this.curgettpayment_request.receiptOrg);
               // debugger;
            } else {
                this.accountList = await pubFuncton.pubFuncton();
            }

           // if (this.curgettpayment_request.type == "" || this.curgettpayment_request.type == null) {
            //    this.curgettpayment_request.type = this.ASTypeDicList[0].id;
          //  }
           // debugger;
            if (this.curReadOnly) {
                this.curgettpayment_request.type = pubFuncton.getShowTextaboutDicValue(this.curgettpayment_request.type, this.ASTypeDicList);
                this.curgettpayment_request.status = pubFuncton.getShowTextaboutDicValue(this.curgettpayment_request.status, this.ASpayStatusTypeDicList);
            }
            this.$forceUpdate();
            //debugger;


        },

        methods: {
            async getASTypeDicList() {

                //let processgetASTypeDicList = await ajax.get("api/basedata/dict?name=ASType");
                let letDic = await ajax.get({url: 'api/basedata/dict?name=paymenttype'});
              //  debugger;
                this.ASTypeDicList = letDic.paymenttype;


                let letDic1 = await ajax.get({url: 'api/basedata/dict?name=payStatus'});
                this.ASpayStatusTypeDicList= letDic1.payStatus;
                //如果没有这句代码，select中初始化会是空白的，默认选中就无法实现


            },
            getTotal() {
                //debugger;
                var total = 0;
                for (var og of this.orderGoods) {
                    total += parseFloat(og.amountPrice);
                }
                this.total = total.toFixed(2);
            },

            getselerdealer() {
                //await this.initpayment_request();
                // debugger;

                let letbuyerIdOrsellerId = "";
                letbuyerIdOrsellerId = this.orderFulldetail.order.sellerId;

                for (let i = 0; i < this.orderFulldetail.dealers.length; i++) {
                    if (letbuyerIdOrsellerId == this.orderFulldetail.dealers[i].orgId) {
                        this.selerdealer = this.orderFulldetail.dealers[i];
                        break;
                    }
                }
            },

            saveSunmit() {
                this.curgettpayment_request.status = "1";
                this.save();
                //slocation.reload();
            },

            save(argTypeInfo) {
                progress.show();
                //debugger; status:0保存状态   申请单状态:1 申请 2 已审核 3 已支付
                if (this.curgettpayment_request.status != "1") {
                    this.curgettpayment_request.status = "";//:0保存状态
                }
                let _this = this;
                this.curgettpayment_request.orderId = this.id;

                //this.curgettpayment_request.payPhase = "0";
                ajax.post({
                    url: 'api/order/orderedit/savespayreq',
                    data: this.curgettpayment_request

                }).then(d => {
                    debugger;
                    console.log(d);
                    _this.curgettpayment_request = d.payreq;
                    msg.info("操作成功");

                    if (_this.curgettpayment_request.status=="1"){
                        progress.show();
                        location.reload();
                        progress.hide();
                    }
                    progress.hide();
                   // window.
                   // window.reload();
                    // this.searchQuery();
                }).catch(d => {
                    debugger;
                    console.log(d);
                    msg.info(d.msg, "error");
                    progress.hide();
                });


            },


        }
    }
</script>

<style lang="less" scoped>
    @import url("@/common/common.less");

    .panel-body {
        .row {
            margin: 0 0 100px 0 !important;
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
