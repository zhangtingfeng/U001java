<template>
    <div>
        <div :class="[curReadOnly?'box collapsed-box boxbackgroundcolor':'box boxbackgroundcolor']">
            <div class="box-header width-border  bg-lightgreen">
                <h3 class="box-title">发货信息</h3>
                <div class="box-tools pull-right">
                    <button class="btn btn-box-tool" data-widget="collapse" type="button">
                        <i :class='[curReadOnly?"fa fa-plus":"fa fa-minus"]'></i>
                    </button>
                </div>
            </div>

            <div class="box-body boxbody" v-if="!curReadOnly ">
                <div class="row">

                    <div class="col-md-3">
                        <label>发货时间</label>
                        <span>
                            <date name="delivery_dt" v-model="curgetRD.delivery_dt"></date>
                        </span>
                    </div>
                    <div class="col-md-3">
                        <label>送货单号</label><span><input class="form-control" name="curgetRD.delivery_no"
                                                        placeholder="请输入送货单号"
                                                        type="text"
                                                        v-model="curgetRD.delivery_no"/></span>
                    </div>

                    <div class="col-md-3">
                        <label>发运方式 </label>
                        <span><input class="form-control" name="curgetRD.delivery_no"
                                     placeholder="请输入发运方式"
                                     type="text"
                                     v-model="curgetRD.delivery_type"/></span>
                    </div>
                    <div class="col-md-3">
                        <label>承运商</label><span><input class="form-control" name="curgetRD.id_carrier" placeholder="请输入承运商"
                                                       type="text" v-model="curgetRD.id_carrier"/></span>
                    </div>

                    <div class="col-md-3">
                        <label>发货仓库</label><span><input class="form-control" name="curgetRD.id_warehouse"
                                                        placeholder="请输入发货仓库"
                                                        type="text"
                                                        v-model="curgetRD.id_warehouse"/></span>
                    </div>

                </div>


                <div class="row" style="padding-left: 15px;padding-top: 20px;">
                    <button type="button" class="btn btn-primary" style="width:132px;"
                            @click="save()">保存
                    </button>
                </div>
            </div>
            <div class="box-body boxbody" v-if="curReadOnly && curgetRD">
                <div class="row">

                    <div class="col-md-3">
                        <label>发货时间</label>
                        <span class="form-control">   {{curgetRD.delivery_dt}}</span>
                    </div>
                    <div class="col-md-3">
                        <label>送货单号</label><span class="form-control">{{curgetRD.delivery_no}}</span>
                    </div>

                    <div class="col-md-3">
                        <label>发运方式 </label>
                        <span class="form-control">{{curgetRD.delivery_type}}</span>
                    </div>
                    <div class="col-md-3">
                        <label>承运商</label>
                        <span class="form-control">{{curgetRD.id_carrier}}</span>
                    </div>

                    <div class="col-md-3">
                        <label>发货仓库</label>
                        <span class="form-control"> {{curgetRD.id_warehouse}}</span>
                    </div>

                </div>


            </div>

        </div>


    </div>
</template>

<script>
    import {ajax, msg, progress} from "@f/vendor";
    import {comps, dict, editors} from "@f/framework";
    //import List from "../../fee/list";

    export default {
        components: {

            date: editors.get("date")
        },
        data() {
            return {
                total: 0,
                order: this.orderFulldetail.order,
                curgetRD: {},
                curReadOnly: this.readonly
            }
        },
        props: ["view", "id", "readonly", "args", "base", "orderFulldetail"],
        beforeMount() {
            //debugger;
            let letThis = this;
            let curorderFulldetailorder_rdList = this.orderFulldetail.order_rdList;

            let letcurgetRD = {};
            //letcurgetRD.delivery_dt = new Date();////初始化时间


            letcurgetRD.delivery_type = this.orderFulldetail.contract.delivery_vendor;//发运方式
            this.curgetRD = letcurgetRD;
            //   debugger;

            //return;
            let letIfHaveData = false;
            if (curorderFulldetailorder_rdList) {
             //   debugger;
                for (let i = 0; i < curorderFulldetailorder_rdList.length; i++) {
                    if ("发货" == curorderFulldetailorder_rdList[i].status) {
                        this.curgetRD = curorderFulldetailorder_rdList[i];
                        letIfHaveData = true;
                        break;
                    }
                }
            }
            // debugger;
            if (letIfHaveData == false) {
                this.readcangku();
            }
           // debugger;
            if (!this.curgetRD.delivery_dt && this.curReadOnly){
                this.curgetRD.delivery_dt="";///防止不保存 会有乱码
            } else if(!this.curgetRD.delivery_dt) {
                this.curgetRD.delivery_dt = new Date();////初始化时间
            }

        },

        methods: {

            readcangku() {
                let letthis = this;
                if (letthis.curReadOnly) return;
                if (letthis.curgetRD.id_warehouse == null || letthis.curgetRD.id_warehouse == "") {

                    ajax.post({
                        url: 'api/portal/sale/getSalesInfo',
                        data: {id: this.orderFulldetail.orderGoods[0].goodsId}

                    }).then(d => {
                      //  debugger;

                        letthis.curgetRD.id_warehouse = JSON.parse(d.wh).name;

                        console.log(d);
                        //   msg.info("操作成功");
                        // this.searchQuery();
                    }).catch(d => {
                        // debugger;
                        console.log(d);
                        //  msg.info(d.msg, "error");
                    });
                }
            }
            ,


            save(argTypeInfo) {
                //debugger;
                progress.show();
                this.curgetRD.orderId = this.id;
                this.curgetRD.status = "发货";
                ajax.post({
                    url: 'api/order/orderedit/savesRQ',
                    data: this.curgetRD

                }).then(d => {
                    progress.hide();
                    //debugger;
                    console.log(d);
                    msg.info("操作成功");
                    // this.searchQuery();
                }).catch(d => {
                    progress.hide();
                    debugger;
                    console.log(d);
                    msg.info(d.msg, "error");
                });


            }
            ,


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
