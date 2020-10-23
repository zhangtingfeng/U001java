<template>
    <div>
        <div :class="[curReadOnly?'box collapsed-box boxbackgroundcolor':'box boxbackgroundcolor']">
            <div class="box-header width-border  bg-lightgreen">
                <h3 class="box-title">售后服务问题</h3>
                <div class="box-tools pull-right">
                    <button type="button" class="btn btn-box-tool" data-widget="collapse">
                        <i :class='[curReadOnly?"fa fa-plus":"fa fa-minus"]'></i>
                    </button>
                </div>
            </div>

            <div v-if="!curReadOnly " class="box-body boxbody">
                <div class="row">

                    <div class="col-md-12">
                        <label>售后类型</label>

                        <select class="form-control" v-model="curaftermarket.buyeraftermarkettype">
                            <option :value="coupon.id" v-for="coupon in ASTypeDicList">{{coupon.text}}</option>
                            <!--  <option value="漏发">漏发</option>
                              <option value="补偿">补偿</option>
                              <option value="退款">退款</option>
                              <option value="换货">换货</option>
                              <option value="补发">补发</option>
                              <option value="退货退款">退货退款</option>-->
                        </select>


                    </div>
                </div>
                <div class="row">
                    <div class="col-md-6">
                        <label>售后描述</label>
                    </div>
                </div>
                <div class="row">
                    <div class="col-md-12">
                    <textarea type="text" class="form-control" rows="6"
                              name="curaftermarket.desc"
                              v-model="curaftermarket.buyeraftermarketdesc"
                              placeholder="请输入售后描述内容"/>
                    </div>
                </div>
                <div class="row">
                    <div class="col-md-6">
                        <img style="width: 100px;height: 100px;cursor:pointer;" v-for="img of images1serverList" @click="lookimg(img)"
                             :src="img"/>
                    </div>
                </div>
                <div class="row">

                    <div class="col-md-6">
                        <label>上传图片</label>

                        <span> <upload :id="id1" v-model="images2ClientList" :title="title1"></upload></span>
                    </div>
                </div>
                <div class="row" style="padding-left: 15px;padding-top: 20px;">
                    <button type="button" class="btn btn-primary" style="width:132px;"
                            @click="save()">保存
                    </button>
                </div>
            </div>
            <div v-if="curReadOnly && curaftermarket" class="box-body boxbody">
                <div class="row">
                    <div class="col-md-3">
                        <label>售后类型:</label>

                    </div>
                </div>
                <div class="row">
                    <div class="col-md-12">
                        <span>{{curaftermarket.buyeraftermarkettype}}</span>
                    </div>
                </div>

                <div class="row">
                    <div class="col-md-3">
                        <label>售后描述:</label>
                    </div>
                </div>
                <div class="row">
                    <div class="col-md-6">
                        <span>{{curaftermarket.buyeraftermarketdesc}}</span>
                    </div>
                </div>

                <div class="row">
                    <div class="col-md-6">
                        <img style="width: 100px;height: 100px;cursor:pointer;" v-for="img of images1serverList" @click="lookimg(img)"
                             :src="img"/>
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

            date: editors.get("date"), Upload
        },
        data() {
            return {
                id1: "input-img1",
                title1: "",
                images1serverList: [],
                images2ClientList: [],
                total: 0,
                curaftermarket: null,
                curReadOnly: this.readonly,
                ASTypeDicList: [],
                couponSelected: '',
            }
        },
        props: ["view", "id", "readonly", "args", "base"],
        async beforeMount() {
            // debugger;
            // if (this.orderFulldetail.Order_evaluate) {
            //   this.curgetevaluate = this.orderFulldetail.Order_evaluate;

            // }
            this.inidata();
            await this.getASTypeDicList();


        },

        methods: {
            async getASTypeDicList() {
                let letthis = this;
                //let processgetASTypeDicList = await ajax.get("api/basedata/dict?name=ASType");
                await ajax.get({
                    url: 'api/basedata/dict?name=aftermarketAsk'
                }).then(data => {
                    //debugger;
                    letthis.ASTypeDicList = data.aftermarketAsk;
                    //如果没有这句代码，select中初始化会是空白的，默认选中就无法实现
                    if (letthis.curaftermarket.buyeraftermarkettype == "" || letthis.curaftermarket.buyeraftermarkettype == null) {
                        letthis.curaftermarket.buyeraftermarkettype = letthis.ASTypeDicList[0].id;
                    }
                    if (letthis.curReadOnly) {
                       // alert(letthis.curaftermarket.buyeraftermarkettype);
                        letthis.curaftermarket.buyeraftermarkettype = pubFuncton.getShowTextaboutDicValue(letthis.curaftermarket.buyeraftermarkettype, letthis.ASTypeDicList);
                       // alert(letthis.curaftermarket.buyeraftermarkettype);
                    }
                }).catch(d => {

                });
            },

            getModifyValue(msgModifyvalue) {
                //  this.curgetevaluate.evacontentscore = msgModifyvalue;
            },
            async inidata() {
                this.curaftermarket = {};
                this.curaftermarket.processid = this.id;
                //   this.curaftermarket.askoranswer = "ask";
                //debugger;
                let _This = this;
                await ajax.post({
                    url: 'api/order/aftermarketinfo/getServiceDetail',
                    data: this.curaftermarket

                }).then(data => {
                    //debugger;
                    console.log(data);
                    // debugger;
                    _This.curaftermarket = data.TAftermarket_Detail;
                    if (data && data.TAftermarketImgList) {
                        var imgs1 = [];
                        for (var i of data.TAftermarketImgList) {
                            i.path = "api/order/upload/" + i.imgPath;
                            imgs1.push(i.path);
                        }
                        this.images1serverList = imgs1;
                    }


                    // msg.info("操作成功");
                    // this.searchQuery();
                }).catch(d => {
                    debugger;
                    console.log(d);
                    msg.info(d.msg, "error");
                });

            },
            lookimg(str) {
                var newwin = window.open();
                newwin.document.write("<imgs src=" + str + " />");
            },

            save(argTypeInfo) {
                debugger;

                let letthis=this;
                //   let ScoreValue = this.ScoreValue;
                this.curaftermarket.processid = this.id;

                if (this.images1serverList.length == 0 && (this.images2ClientList.length == 0)) {
                    msg.info("请点击上传照片", "error");
                    return;
                }

                let resetimages1Path = [];
                for (var img of this.images2ClientList) {
                    resetimages1Path.push(img.id);
                }

                this.curaftermarket.images1 = resetimages1Path;

                //  this.curaftermarket.askoranswer = "ask";
                progress.show();
                ajax.post({
                    url: 'api/order/aftermarketinfo/saveaftermarketdetail',
                    data: this.curaftermarket

                }).then(data => {
                    debugger;
                  //  console.log(d);
                 //   msg.info("操作成功");
                    if (data.TAftermarket_Detail){
                        letthis.curaftermarket = data.TAftermarket_Detail;
                    }
                   // letthis.curaftermarket = data.TAftermarket_Detail;
                    progress.hide();

                   // letthis.inidata();
                    // this.searchQuery();
                }).catch(d => {
                    debugger;
                    progress.hide();
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
