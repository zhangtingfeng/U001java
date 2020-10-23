<template>
    <div class="order-view">
        <template v-if="state === 0"> 加载售后...</template>
        <template v-if="state === -1"> 加载售后数据出错！</template>
        <template v-if="state === 1">
            <div class="box">
                <div class="box-header width-border  bg-lightgreen" data-widget="collapse">
                    <h3 class="box-title">售后编号: {{ id }}</h3>
                    <div class="box-tools pull-right">
                        <button type="button" class="btn btn-box-tool">
                            <i class="fa fa-minus"></i>
                        </button>
                    </div>
                </div>
            </div>

            <component
                    v-for="v of subs"
                    :key="v.name"
                    :is="v.view"
                    :id="id"
                    :view="v.name"
                    :readonly="v.readonly"
                    :args="v.args"
                    :asFulldetail="asFulldetail"
            />

            <div class="box">
                <div class="box-footer flow-links" style="  text-align: right;">
                    <a
                            style="width:132px;;margin-right: 0px;"
                            class="btn btn-primary"
                            href="#"
                            v-for="btn of btns"
                            :key="btn.id"
                            @click.prevent="flowSubmit(btn.id)"
                    >{{ btn.name || btn.id }}</a
                    >
                    <a class="btn btn-default" style="width:132px;margin-right: 0px;"  href="#" @click.prevent="back">返回</a>
                </div>
            </div>
        </template>
    </div>
</template>

<script>
    import {ajax, msg, progress} from "@f/vendor";
    import {user} from "@f/framework";
    import FlowMng from "../flow";
    import SubViews from "./subViews";

    export default {
        props: ["id"],
        data() {
            return {
                // 界面加载状态
                state: 0,
                process: {},
                // 用户类型
                userType: "",
                // 流程信息
                flow: {},
                // 订单计算出来需要加载的子界面
                subs: [],
                // 订单计算出来的流转按钮
                btns: [],
                asFulldetail: {},
            };
        },
        mounted() {
            this.initAS();
            this.load();
        },
        methods: {
            async initAS() {

                //   this.curaftermarket.askoranswer = "ask";

                let _This = this;
                _This.asFulldetail.processid = _This.id;
               await ajax.post({
                    url: 'api/order/aftermarketinfo/getServiceDetail',
                    data: _This.asFulldetail

                }).then(data => {
                    //debugger;
                    console.log(data);
                    // debugger;
                    _This.asFulldetail = data.TAftermarket_Detail;
                    if (data && data.TAftermarketImgList) {
                        var imgs1 = [];
                        for (var i of data.TAftermarketImgList) {
                            i.path = "api/order/upload/" + i.imgPath;
                            imgs1.push(i.path);
                        }
                        _This.asFulldetail.images1serverList = imgs1;
                    }



                    // msg.info("操作成功");
                    // this.searchQuery();
                }).catch(d => {
                    debugger;
                    console.log(d);
                    msg.info(d.msg, "error");
                });


            },
            back() {
                history.back();
            },
            async load() {
                //return ;
                this.state = 0;
                try {
                    // 1. 加载流程实例信息
                    this.process = await ajax.get(
                        `api/order/aftermarketinfo/process/${this.id}`
                    );

                    // 2. 获取订单当前流程和节点
                    let processId = this.process.process.id;
                    let flowId = this.process.process.flowId;
                    let nodeId = this.process.process.nodeId;
                    this.userType = this.process.userType;

                    // 3. 获取当前流程节点
                    let node = await FlowMng.getNode(flowId, nodeId);
                    this.flow = {processId, flowId, node};
                    //debugger;
                    // 4. 买家或者卖家进入，可以加载流程子界面以及流转按钮
                    if (this.userType === "1" || this.userType === "2") {
                        this.loadSubView();
                        this.loadBtns();
                    }

                    // 激活Box
                    setTimeout(() => {
                        $(this.$el).find(".box").boxWidget();
                    }, 1000);

                    this.state = 1;
                } catch (err) {
                    console.error(err);
                    this.state = -1;
                }
            },
            loadBtns() {
                if (!(this.flow.node && this.flow.node.links)) return;

                let btns = [];
                for (let l of this.flow.node.links) {
                    // 只有和当前买家或者卖家身份相同的才可以流转
                    if (l.type === this.userType)
                        btns.push({id: l.nodeId2, name: l.name});
                }
                this.btns = btns;
            },
            loadSubView() {
                if (!(this.flow.node && this.flow.node.props)) return;

                let type = this.userType === "1" ? 0 : 1;
                let subs = [];

                // 循环加载子界面
                for (let n in SubViews) {
                    // 获取该节点对应的当前子界面的配置
                    // [买家:1 只读 2: 编辑 其他:不可见][卖家:1 只读 2: 编辑 其他:不可见].[界面的参数]
                    //debugger;
                    let setting = this.flow.node.props[`view_${n}`];
                    let m = /^(\d{0,2})(\.(.*))?$/.exec(setting);

                    // 没有配置不加载这个子界面
                    if (!m) continue;
                    let t = m[1];
                    if (!t) continue;

                    if (t[type] === "1" || t[type] === "2") {
                        subs.push({
                            name: n,
                            view: SubViews[n],
                            readonly: t[type] === "1",
                            args: m[3],
                        });
                    }
                }

                this.subs = subs;
            },
            async flowSubmit(nodeId) {
                try {
                    progress.show();
                    let rs = await ajax.post({
                        url: `api/order/flow/${this.flow.processId}`,
                        data: {next: nodeId},
                    });
                    if (rs.rs != "0") throw "流程提交出错";

                    this.load();
                } catch (err) {
                    console.error(err);
                    msg.info(
                        typeof err === "string" ? err : err.msg || "系统错误",
                        "error"
                    );
                } finally {
                    progress.hide();
                }
            },
        },
    };
</script>

<style lang="less" scoped>
    @import url("@/common/common.less");
    .flow-links {
        a {
            padding-left: 30px;
            padding-right: 30px;
            margin-right: 30px;
        }
    }

    .box {
        margin-bottom: 10px;
    }

    label {
        padding-top: 4px;
    }
</style>
