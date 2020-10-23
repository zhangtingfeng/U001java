<template>
    <div>
        <div :class='[curReadOnly?"box collapsed-box boxbackgroundcolor":"box boxbackgroundcolor"]'>
            <div class="box-header width-border  bg-lightgreen">
                <h3 class="box-title">付款记录信息</h3>
                <div class="box-tools pull-right">
                    <button type="button" class="btn btn-box-tool" data-widget="collapse">
                        <i :class='[curReadOnly?"fa fa-plus":"fa fa-minus"]'></i>
                    </button>
                </div>
            </div>

            <div v-if="!curReadOnly && curgettpayment_request " class="box-body boxbody">
                <div class="row">
                    <div class="col-md-3">
                        <label>付款机构</label>
                        <select class="form-control" v-model="curgettpayment_request.paymentOrg">
                            <!--选择项的value值默认选择项文本 可动态绑定选择项的value值 更改v-model指令绑定数据-->
                            <option v-for="item in accountList" :value="item.id">{{item.rltAcc}}</option>
                        </select>





                    </div>
                    <div class="col-md-3">
                        <label>付款方式</label>
                        <select class="form-control" v-model="curgettpayment_request.type">
                            <option :value="coupon.id" v-for="coupon in ASTypeDicList">{{coupon.text}}</option>
                        </select>
                    </div>

                    <div class="col-md-3">
                        <label>付款金额(元)</label><span><input type="number" class="form-control"
                                                           name="curgettpayment_request.amount"
                                                           v-model="curgettpayment_request.amount"
                                                           placeholder="请输入付款金额"/></span>
                    </div>

                    <div class="col-md-3">
                        <label>付款时间</label>
                        <date name="curgettpayment_request.paymentDt"
                              v-model="curgettpayment_request.paymentDt"></date>
                    </div>


                </div>
                <div class="row">
                    <div class="col-md-3">
                        <label>交易流水号</label><span><input type="text" class="form-control"
                                                         name="curgettpayment_request.paymentNo"
                                                         v-model="curgettpayment_request.paymentNo"
                                                         placeholder="请输入交易流水号"/></span>
                    </div>
                </div>

                <div class="row" style="padding-left: 15px;padding-top: 20px;">
                    <button type="button" class="btn btn-primary" style="width:132px;"
                            @click="save()">保存
                    </button>
                </div>


            </div>

            <div v-if="curReadOnly && curgettpayment_request" class="box-body boxbody">
                <div class="row">
                    <div class="col-md-3">
                        <label>付款机构</label><span class="form-control">{{curgettpayment_request.paymentOrg}}</span>
                    </div>
                    <div class="col-md-3">
                        <label>付款方式</label>
                        <span class="form-control">{{curgettpayment_request.type}}</span>
                    </div>
                    <div class="col-md-3">
                        <label>付款金额(元)</label>
                        <span class="form-control">{{curgettpayment_request.amount}}</span>
                    </div>
                    <div class="col-md-3">
                        <label>付款时间</label>
                        <span class="form-control">{{curgettpayment_request.paymentDt}}</span>
                    </div>
                </div>
                <div class="row">
                    <div class="col-md-3">
                        <label>交易流水号</label>
                        <span class="form-control">{{curgettpayment_request.paymentNo}}</span>
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
   // import {pubFuncton} from "@f/pubcommon";

    export default {
        components: {
            date: editors.get("date")
        },
        data() {
            return {

                order: this.orderFulldetail.order,
                curgettpayment_request: {},
                curReadOnly:this.readonly,
                contract: this.orderFulldetail.contract,
                orderGoods: this.orderFulldetail.orderGoods,
                total: 0,
                dealer: {},
                accountList: [],
                ASTypeDicList: [],
            }
        },
        props: ["view", "id", "readonly", "args", "base", "orderFulldetail"],
        async beforeMount() {
            await this.getASTypeDicList();
            let _this = this;
            //this.curReadOnly=!this.curReadOnly;
            let letCheckGet = false;
            // debugger;
            let curargs = this.args;
///支付金额 开始
            // debugger;
            this.getTotal();
            //curargs=2; 调试
            if (curargs == 1 && this.contract && this.contract.pay1Amount && this.total > 0) {
                this.curgettpayment_request.amount = (this.contract.pay1Amount * this.total * 0.01).toFixed(2);
            }
            if (curargs == 2 && this.contract && this.contract.pay2Amount && this.total > 0) {
                this.curgettpayment_request.amount = (this.contract.pay2Amount * this.total * 0.01).toFixed(2);
            }
            ///支付金额 end

////付款机构 begin
            let letbuyerIdOrsellerId = "";
            letbuyerIdOrsellerId = this.order.buyerId;
            for (let i = 0; i < this.orderFulldetail.dealers.length; i++) {
                if (letbuyerIdOrsellerId == this.orderFulldetail.dealers[i].orgId) {
                    this.dealer = this.orderFulldetail.dealers[i];
                    break;
                }
            }
////付款机构 end


            this.curgettpayment_request.payPhase = curargs;////预先赋值，兼容没有申请支付的流程
            this.curgettpayment_request.orderId = this.id;////预先赋值，兼容没有流程申请支付的流程

            let curgettpayment_requestList = this.orderFulldetail.payment_requestList;

            if (curgettpayment_requestList && curgettpayment_requestList.length > 0) {
                for (let i = 0; i < curgettpayment_requestList.length; i++) {
                    if (curgettpayment_requestList[i].payPhase == curargs) {
                        this.curgettpayment_request = curgettpayment_requestList[i];
                        letCheckGet = true;
                        break;
                    }
                }
            }

            // debugger;
            if (!this.curgettpayment_request.paymentDt && this.curReadOnly) {
                this.curgettpayment_request.paymentDt = "";
            } else if (!this.curgettpayment_request.paymentDt) {
                this.curgettpayment_request.paymentDt = new Date();////初始化时间
            }////有await语句的要放在后面执行，否则影响页面变量的双向绑定的初始化
            //debugger;
            if (this.curReadOnly && letCheckGet) {
                this.curgettpayment_request.paymentOrg = await pubFuncton.getDescFunction(_this.curgettpayment_request.paymentOrg);
                debugger;
            } else {
                this.accountList = await pubFuncton.pubFuncton();
            }


            if (this.curgettpayment_request.type == "" || this.curgettpayment_request.type == null) {
                this.curgettpayment_request.type = this.ASTypeDicList[0].id;
            }

            if (this.curReadOnly) {
                this.curgettpayment_request.type = pubFuncton.getShowTextaboutDicValue(this.curgettpayment_request.type, this.ASTypeDicList);
                debugger;
            }

        },

        methods:
            {
                async getASTypeDicList() {

                    //let processgetASTypeDicList = await ajax.get("api/basedata/dict?name=ASType");
                    let letDic = await ajax.get({url: 'api/basedata/dict?name=paymenttype'});
                   // debugger;
                    this.ASTypeDicList = letDic.paymenttype;
                    //如果没有这句代码，select中初始化会是空白的，默认选中就无法实现



                },
                save(argTypeInfo) {
                    debugger;
                    let letthis = this;
                    progress.show();
                    ajax.post({
                        url: 'api/order/orderedit/savespayreq',
                        data: this.curgettpayment_request
                    }).then(d => {
                        //debugger;
                        if (d.payreq) {
                            letthis.curgettpayment_request = d.payreq;
                        }
                        progress.hide();
                        console.log(d);
                        msg.info("操作成功");
                        // this.searchQuery();
                    }).catch(d => {
                        debugger;
                        progress.hide();
                        console.log(d);
                        msg.info(d.msg, "error");
                    });
                },
                getTotal() {
                    //debugger;
                    var total = 0;
                    for (var og of this.orderGoods) {
                        total += parseFloat(og.amountPrice);
                    }
                    this.total = total.toFixed(2);
                }
                ,

            }
        ,

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
