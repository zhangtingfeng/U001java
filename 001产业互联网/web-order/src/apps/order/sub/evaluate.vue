<template>
    <div>
        <div :class="[curReadOnly?'box collapsed-box boxbackgroundcolor':'box boxbackgroundcolor']">
            <div class="box-header width-border  bg-lightgreen">
                <h3 class="box-title">服务评价</h3>
                <div class="box-tools pull-right">
                    <button class="btn btn-box-tool" data-widget="collapse" type="button">
                        <i :class='[curReadOnly?"fa fa-plus":"fa fa-minus"]'></i>
                    </button>
                </div>
            </div>

            <div class="box-body boxbody" v-if="!curReadOnly ">
                <div class="row">
                    <div class="form-group">
                        <label class="col-sm-2 control-label" for="inputEmail3">服务打分</label>
                        <div class="col-sm-4" style="margin-top: 10px;">
                            <template>
                                <div>
                                    <star :curReadOnly="curReadOnly" :numAllarg="5"
                                          :storScorearg="curgetevaluate.evacontentscore"
                                          @transfer="getModifyValue"></star>
                                </div>
                            </template>
                        </div>
                    </div>
                    <div class="form-group">
                        <div class="col-sm-6" style="margin-left: 1px;margin-top: 10px;">
                           <textarea class="form-control" name="curgetevaluate.evacontent" placeholder="请输入评论内容"
                                     rows="3"
                                     type="text"
                                     v-model="curgetevaluate.evacontent"/>
                        </div>
                    </div>
                </div>
                <div class="row" style="padding-left: 15px;padding-top: 20px;">
                    <button type="button" class="btn btn-primary" style="width:132px;"
                            @click="save()">保存
                    </button>
                </div>


            </div>
            <div class="box-body boxbody" v-if="curReadOnly && curgetevaluate">
                <div class="form-group">
                    <label class="col-sm-2 control-label" for="inputEmail3">服务打分</label>
                    <div class="col-sm-4">
                        <template>
                            <div>
                                <star :curReadOnly="curReadOnly" :numAllarg="5"
                                      :storScorearg="curgetevaluate.evacontentscore"
                                      @transfer="getModifyValue"></star>
                            </div>
                        </template>
                    </div>

                </div>
                <div class="form-group">
                    <div class="col-sm-6" style="margin-left: 1px;">
                        <span>{{curgetevaluate.evacontent}}</span>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
    import {ajax, msg, progress} from "@f/vendor";
    import {comps, dict, editors} from "@f/framework";

    import star from '../components/star'
    //import List from "../../fee/list";

    export default {

        components: {

            date: editors.get("date"), star
        },
        data() {
            return {

                total: 0,
                order: this.orderFulldetail.order,
                curgetevaluate: {},
                curReadOnly: this.readonly,
            }
        },
        props: ["view", "id", "readonly", "args", "base", "orderFulldetail"],
        beforeMount() {
            // debugger;
            if (this.orderFulldetail.Order_evaluate) {
                this.curgetevaluate = this.orderFulldetail.Order_evaluate;

            }

        },

        methods: {
            getModifyValue(msgModifyvalue) {
                this.curgetevaluate.evacontentscore = msgModifyvalue;
            },
            save(argTypeInfo) {
                //  debugger;
                progress.show();
                let ScoreValue = this.ScoreValue;
                this.curgetevaluate.orderId = this.id;

                ajax.post({
                    url: 'api/order/orderedit/saveevaluate',
                    data: this.curgetevaluate

                }).then(d => {
                    //debugger;
                    progress.hide();
                    console.log(d);
                    msg.info("操作成功");
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
