<template>
    <div>
        <div :class='[curReadOnly?"box collapsed-box boxbackgroundcolor":"box boxbackgroundcolor"]'>
            <div class="box-header width-border  bg-lightgreen">
                <h3 class="box-title">收货信息</h3>
                <div class="box-tools pull-right">
                    <button type="button" class="btn btn-box-tool" data-widget="collapse">
                        <i :class='[curReadOnly?"fa fa-plus":"fa fa-minus"]'></i>
                    </button>
                </div>
            </div>

            <div v-if="!curReadOnly " class="box-body boxbody">
                <div class="row">

                    <div class="col-md-3">
                        <label>收货时间</label>
                        <span>
                            <date name="receive_dt" v-model="curgetRD.receive_dt"></date>
                        </span>
                    </div>
                    <div class="col-md-3">
                        <label>收货人</label><span><input type="text" class="form-control"
                                               name="curgetRD.receiver"
                                               v-model="curgetRD.receiver"
                                               placeholder="请输入收货人"/></span>
                    </div>
             <!--      <div class="col-md-3">
                        <label>运单号</label><span><input type="text" class="form-control"
                                               name="curgetRD.trans_no"
                                               v-model="curgetRD.trans_no"
                                               placeholder="请输入运单号"/></span>
                    </div>
-->
                </div>


                <div class="row" style="padding-left: 15px;padding-top: 20px;">
                    <button type="button" class="btn btn-primary" style="width:132px;"
                            @click="save()">保存
                    </button>
                </div>
            </div>
            <div v-if="curReadOnly && curgetRD" class="box-body boxbody">
                <div class="row">

                    <div class="col-md-3">
                        <label>收货时间</label>

                        <span class="form-control"> {{curgetRD.receive_dt}}</span>
                    </div>
                    <div class="col-md-3">
                        <label>收货人</label><span class="form-control">{{curgetRD.receiver}}</span>
                    </div>


                </div>
            </div>

        </div>


    </div>
</template>

<script>
    import {ajax, msg, progress} from "@f/vendor";
    import {comps, dict, editors} from "@f/framework";

    export default {
        components: {
            date: editors.get("date")
        },
        data() {
            return {
                curReadOnly: this.readonly,
                total: 0,
                order: this.orderFulldetail.order,
                curgetRD: {},
                order_ADDR:this.orderFulldetail.order_ADDR
            }
        },
        props: ["view", "id", "readonly", "args", "base", "orderFulldetail"],
        beforeMount() {
            //debugger;
            //this.curgetRD.receive_dt = new Date();////初始化时间

            if (this.order_ADDR && this.order_ADDR.name) {

                this.curgetRD.receiver =this.order_ADDR.name;
            }

            let curorderFulldetailorder_rdList = this.orderFulldetail.order_rdList;
            if (curorderFulldetailorder_rdList) {
                for (let i = 0; i < curorderFulldetailorder_rdList.length; i++) {
                    if ("收货" == curorderFulldetailorder_rdList[i].status) {
                        this.curgetRD = curorderFulldetailorder_rdList[i];


                        break;
                    }
                }
            }
           // debugger;
            if (!this.curgetRD.receive_dt && this.curReadOnly){
                this.curgetRD.receive_dt="";///防止不保存 会有乱码
            } else if(!this.curgetRD.receive_dt) {
                this.curgetRD.receive_dt = new Date();////初始化时间
            }

        },

        methods: {
            save(argTypeInfo) {
                //debugger;
                progress.show();
                this.curgetRD.orderId = this.id;
                this.curgetRD.status = "收货";
                ajax.post({
                    url: 'api/order/orderedit/savesRQ',
                    data: this.curgetRD

                }).then(d => {
                    //debugger;
                    progress.hide();
                    console.log(d);
                    msg.info("操作成功");
                    // this.searchQuery();
                }).catch(d => {
                    debugger;
                    console.log(d);
                    msg.info(d.msg, "error");
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
