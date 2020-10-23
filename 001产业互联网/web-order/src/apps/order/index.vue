<template>
    <div class="order-view">

        <div class="imbox" style="">
            <module-loader
                    :app="['im', 'btn']"
                    :args="{ userid: contactUserID }"
            >
            </module-loader>
        </div>

        <template v-if="state === 0"> 加载订单...</template>
        <template v-if="state === -1"> 加载订单数据出错！</template>
        <template v-if="state === 1">
            <div class="box boxbackgroundcolor">
                <div
                        class="box-header width-border bg-lightgreen"
                        data-widget="collapse"
                >
                    <h3 class="box-title titlehead">订单号: {{ id }}</h3>
                    <div class="box-tools pull-right">


                        <button type="button" class="btn btn-box-tool">
                            <i class="fa fa-minus"></i>
                        </button>
                    </div>
                </div>
                <div class="box-body">
                    <flow-view
                            :name="`orderState${order.base.type}`"
                            :value="order.base.state2"
                    ></flow-view>
                </div>
            </div>

            <template v-if="orderFulldetail">
                <component
                        v-for="v of subs"
                        :key="v.name"
                        :is="v.view"
                        :id="id"
                        :view="v.name"
                        :readonly="v.readonly"
                        :args="v.args"
                        :base="order.base"
                        :orderFulldetail="orderFulldetail"
                />
            </template>

            <div class="box boxbackgroundcolor">
                <div
                        class="box-footer flow-links"
                        style="text-align: right; margin-right: 0px"
                >
                    <a
                            class="btn btn-primary"
                            style="width: 132px; margin-right: 0px"
                            href="#"
                            v-for="btn of btns"
                            :key="btn.id"
                            @click.prevent="flowSubmit(btn.id)"
                    >{{ btn.name || btn.id }}</a
                    >
                    <a
                            class="btn btn-default"
                            style="width: 132px; margin-right: 0px"
                            href="#"
                            @click.prevent="back"
                    >返回</a
                    >
                </div>
            </div>
        </template>
    </div>
</template>

<script>
    import {ajax, msg, progress} from "@f/vendor";
    import FlowMng from "../flow";
    import SubViews from "./subViews";
    import FlowView from "./flowView";
    import framework from "@f/framework";

    export default {
        components: {FlowView, ModuleLoader: framework.ModuleLoader},
        props: ["id"],
        data() {
            return {
                // 界面加载状态
                state: 0,
                // 订单基本信息
                order: null,
                // 订单流程信息
                flow: {},
                // 订单计算出来需要加载的子界面
                subs: [],
                // 订单计算出来的流转按钮
                btns: [],
                orderFulldetail: null,
                contactUserID: null,
            };
        },
        mounted() {
            this.initFullDetailOrder();
            this.load();
        },
        methods: {
            back() {
                history.back();
            },
            async load() {
                //return ;
                this.state = 0;
                try {
                    // 1. 加载订单基本信息
                    this.order = await ajax.get(`api/order/info/${this.id}?sub=base`);
                    let base = this.order.base;

                    // 2. 获取订单当前流程和节点
                    let flowId = base.flow2 || base.flow1;
                    let processId = base.process2 || base.process1;
                    let nodeId = base.node2 || base.node1;

                    // 3. 获取当前流程节点
                    let node = await FlowMng.getNode(flowId, nodeId);
                    this.flow = {processId, flowId, node};
                    //debugger;
                    // 4. 买家或者卖家进入，可以加载流程子界面以及流转按钮
                    if (this.order.base.type === "1" || this.order.base.type === "2") {
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
                    if (l.type === this.order.base.type && this.order.base.links[l.nodeId2])
                        btns.push({id: l.nodeId2, name: l.name});
                }
                this.btns = btns;
            },
            loadSubView() {
                if (!(this.flow.node && this.flow.node.props)) return;

                let type = this.order.base.type === "1" ? 0 : 1;
                let subs = [];

                // 循环加载子界面
                for (let n in SubViews) {
                    // 获取该节点对应的当前子界面的配置
                    // [买家:1 只读 2: 编辑 其他:不可见][卖家:1 只读 2: 编辑 其他:不可见].[界面的参数]
                    //debugger;
                    let setting = this.flow.node.props[`view_${n}`];
                    let m = /^(\d{0,2})(\.(.*))?$/.exec(setting);
                    //alert(n);
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
            async initFullDetailOrder() {
                try {
                    let letID = this.id;
                    //debugger;
                    let data = await ajax.post({
                        url: "api/order/info/getOrderDetail",
                        data: {orderId: this.id},
                    });

                    let i = 1;
                    for (var og of data.orderGoods) {
                        for (var g of data.goods) {
                            var props = [];
                            for (var p of data.props) {
                                if (g.id == p.salesId) {
                                    props.push(p);
                                }
                            }
                            g.props = props;
                            for (var m in data.images) {
                                if (g.id == m && data.images[m].length > 0) {
                                    g.images = data.images[m];
                                    g.img = "api/trade/upload/" + g.images[0].path;
                                }
                            }
                            if (!g.images) {
                                g.img = "imgs/apples/pic_" + i + ".png";
                                if (i == 10) {
                                    i = 1;
                                } else {
                                    i++;
                                }
                            }
                            if (og.goodsId == g.id) {
                                og.goods = g;
                                og.total = og.num * g.unitPrice;
                            }
                        }
                    }
                    debugger;
                    this.orderFulldetail = {};
                    this.orderFulldetail.order = JSON.parse(data.order);
                    this.orderFulldetail.orderGoods = data.orderGoods;
                    this.orderFulldetail.goods = data.goods;
                    this.orderFulldetail.props = data.props;
                    this.orderFulldetail.dealers = data.dealers;
                    this.orderFulldetail.contract = data.contract;
                    this.orderFulldetail.contract.specificationsList =
                        data.specificationsList;
                    this.orderFulldetail.payment_requestList = data.gettpayment_requestList;
                    this.orderFulldetail.order_rdList = data.order_rdList;
                    this.orderFulldetail.contractSignInfo = data.contractSignInfo;
                    this.orderFulldetail.order_ADDR = data.order_ADDR;
                    this.orderFulldetail.Order_evaluate = data.Order_evaluate;
                    this.getContacttelUserID();
                    //debugger;
                } catch (err) {
                    //debugger
                    msg.info("获取订单详情失败,请检查登陆权限");
                }
            },
            getContacttelUserID() {
                // debugger;

                if (this.order.base.type === "1") {
                    let goodCreateuser = this.orderFulldetail.goods[0].createUser;
                    this.contactUserID = goodCreateuser;
                }
                if (this.order.base.type === "2") {
                    let OrderBuyeruser = this.orderFulldetail.order.createUserid;
                    this.contactUserID = OrderBuyeruser;
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
</style>
<style lang="less">
    .box {
        margin-bottom: 10px;
    }

    label {
        padding-top: 4px;
    }

    .imbox  {
        display: inline-block;
        z-index: 100000;
        float: right;
        position: absolute;
        top:10px;
        right:45px;
    }


    .imbox img {
        width: 16px;
    }

    .wnd-chat {
        z-index: 10000;
    }
</style>
