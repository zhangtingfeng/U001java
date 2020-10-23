<template>
    <div>
        <div :class="[curReadOnly?'box collapsed-box boxbackgroundcolor':'box boxbackgroundcolor']">
            <div class="box-header width-border  bg-lightgreen">
                <h3 class="box-title">售后服务问题解决</h3>
                <div class="box-tools pull-right">
                    <button type="button" class="btn btn-box-tool" data-widget="collapse">
                        <i :class='[curReadOnly?"fa fa-plus":"fa fa-minus"]'></i>
                    </button>
                </div>
            </div>

            <div v-if="!curReadOnly " class="box-body boxbody">
                <div class="row">
                    <div class="col-md-3">
                        <label>解决类型</label>
                    </div>
                    <div class="col-md-12">
                         <select class="form-control" v-model="curaftermarketmy.selleraftermarkettype">
                            <option :value="coupon.id" v-for="coupon in ASTypeDicList">{{coupon.text}}</option>
                            <!--     <option value="漏发">漏发</option>
                                <option value="补偿">补偿</option>
                                <option value="退款">退款</option>
                                <option value="换货">换货</option>
                                <option value="补发">补发</option>
                                <option value="退货退款">退货退款</option>-->
                        </select>
                    </div>
                </div>
                <div class="row">
                    <div class="col-md-3">
                                             <label>解决日期</label>

                    </div>
                </div>
                <div class="row">
                    <div class="col-md-12">
                         <date name="curaftermarketmy.sellerserviceDay"
                              v-model="curaftermarketmy.sellerserviceDay"></date>
                    </div>
                </div>


                <div class="row">
                    <div class="col-md-6">
                        <label>解决描述</label>
                    </div>
                </div>
                <div class="row">
                    <div class="col-md-12">
                    <textarea type="text" class="form-control" rows="6"
                              name="curaftermarketmy.desc"
                              v-model="curaftermarketmy.selleraftermarketdesc"
                              placeholder="请输入售后描述内容"/>
                    </div>
                </div>


                <div class="row" style="padding-left: 15px;padding-top: 20px;">
                    <button type="button" class="btn btn-primary" style="width:132px;"
                            @click="save()">保存
                    </button>
                </div>
            </div>
            <div v-if="curReadOnly && curaftermarketmy" class="box-body boxbody">
                <div class="row">

                    <div class="col-md-12">
                        <label>售后类型</label>
                        <span>{{curaftermarketmy.selleraftermarkettype}}</span>
                    </div>
                    <div class="col-md-12">
                        <label>解决日期</label>

                        <span>{{curaftermarketmy.sellerserviceDay}}</span>

                    </div>
                </div>
                <div class="row">
                    <div class="col-md-12">
                        <label>解决描述</label>
                    </div>
                </div>
                <div class="row">
                    <div class="col-md-12">
                        <span>{{curaftermarketmy.selleraftermarketdesc}}</span>
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

    import Upload from "../components/Upload"; // 照片上传
    //import List from "../../fee/list";

    export default {

        components: {

            date: editors.get("date")
        },
        data() {
            return {
                id1: "input-img1",
                title1: "图片描述",
                images1: [],
                total: 0,
                curaftermarketmy: {sellerserviceDay:new Date()},
                curReadOnly: this.readonly,
                ASTypeDicList: [],
            }
        },
        props: ["view", "id", "readonly", "args", "base", "asFulldetail"],
         async mounted() {
             // debugger;
             // if (this.orderFulldetail.Order_evaluate) {
             //   this.curgetevaluate = this.orderFulldetail.Order_evaluate;

             // }
             await this.getASTypeDicList();

             this.inidata();


         },

        methods: {
            async getASTypeDicList() {

                //let processgetASTypeDicList = await ajax.get("api/basedata/dict?name=ASType");
                let letDic = await ajax.get({url: 'api/basedata/dict?name=aftermarketAnswer'});
                this.ASTypeDicList = letDic.aftermarketAnswer;
                //如果没有这句代码，select中初始化会是空白的，默认选中就无法实现



            },
            getModifyValue(msgModifyvalue) {
                //  this.curgetevaluate.evacontentscore = msgModifyvalue;
            },
            inidata() {
                this.curaftermarketmy = this.asFulldetail;
               // debugger;
                if (!this.curaftermarketmy.sellerserviceDay && this.curReadOnly) {
                    this.curaftermarketmy.sellerserviceDay = "";///防止不保存 会有乱码
                } else if (!this.curaftermarketmy.sellerserviceDay) {
                    this.curaftermarketmy.sellerserviceDay = new Date();////初始化时间
                }

                if (this.curaftermarketmy.selleraftermarkettype == "" || this.curaftermarketmy.selleraftermarkettype == null) {
                    this.curaftermarketmy.selleraftermarkettype = this.ASTypeDicList[0].id;
                }

                if (this.curReadOnly) {
                    this.curaftermarketmy.selleraftermarkettype = pubFuncton.getShowTextaboutDicValue(this.curaftermarketmy.selleraftermarkettype, this.ASTypeDicList);
                }
            },


            save(argTypeInfo) {
                debugger;
                progress.show();
                //   let ScoreValue = this.ScoreValue;

                //this.curaftermarket.sellerserviceDay = this.sellerserviceDay;
                this.curaftermarketmy.processid = this.id;
                let letthis = this;

                ajax.post({
                    url: 'api/order/aftermarketinfo/saveaftermarketdetail',
                    data: this.curaftermarketmy

                }).then(data => {
                    debugger;
                    if (data.TAftermarket_Detail) {
                        letthis.curaftermarketmy = data.TAftermarket_Detail;
                    }
                    console.log(data);
                    msg.info("操作成功");
                    progress.hide();
                    // this.searchQuery();
                }).catch(d => {
                    progress.hide();
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

    #AppStartEva {
        background-color: #00a65a;
    }
</style>
