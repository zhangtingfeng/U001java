<template>
    <div>
        <div class="order-list">
            <div class="lr-padding">
                <h4>我的订单</h4>
            </div>
            <div class="tabs">
                <a v-for="(t,index) of status" :key="t.id" href="#" :class="t.clz" @click="changeStatus(t)">
                    <span style="font-weight: bold">  {{ t.text }}</span>({{t.recordcount}})
                    <span class="lineRight"></span>
                </a>
            </div>
            <div class="lr-padding">
                <form class="search-form">
                    <div class="row">
                        <div class="col-md-4">
                            <label class="control-label">下单时间</label>
                            <div class="multi-controls">
                                <comp-date v-model="orderSearchTime.starttime"/>
                                <span style="margin:5px 15px 0;">至</span>
                                <comp-date v-model="orderSearchTime.endtime"/>
                            </div>
                        </div>
                        <div class="col-md-4">
                            <label class="control-label">&nbsp;</label>
                            <div class="multi-controls">
                                <button class="btn btn-search" @click.prevent="doQuery"><i class="fa fa-search"></i>搜索
                                </button>
                            </div>
                        </div>
                    </div>
                </form>
            </div>

            <module-loader style="display: none;" :app="['im', 'btn']" :args="{ userid:'U208F00001'}">
            </module-loader>

            <div class="lr-padding">

                <template v-for="(order, i) in orders">
                    <div class="order-item" :key="order.id" v-if="order.state1 == state || state == '0'">
                        <div class="oneListline">
                            <div class="headerline">
                                <span>序号：{{ orders.length- i }}</span>
                                <!-- <span>交易状态: {{statusShow(order.status)}}</span> -->
                                <span>买家状态：{{ buyerStatus(order.state1) }}</span>
                                <span>卖家状态：{{ sellerStatus(order.state2) }}</span>
                            </div>
                            <div class="dateailline">
                                <span>下单时间：{{ order.createTime }}</span>
                                <span>订单编号：{{ order.id }}</span>
                                <span>卖家机构：{{ order.sellers ? order.sellers.orgName : "" }}</span>
                                <span>卖家电话：{{ order.sellers ? order.sellers.mobile : "" }}</span>
                            </div>
                            <div class="buttonline">
                                <a class="btn whitebutton" :href="order.href">查看订单
                                </a>
                                <a class="btn bluebutton" href="#" @click.prevent="openAs(order)">售后服务
                                </a>
                            </div>
                        </div>
                    </div>
                </template>
                <template v-if="!orders">
                    <p style="text-align:center;font-size:16px;">暂时没有订单</p>
                </template>
            </div>
        </div>
    </div>
</template>

<script>
    import {msg, ajax} from "@f/vendor"
    import {comps, dict} from "@f/framework"
    import framework from "@f/framework"

    export default {
        components: {
            ModuleLoader: framework.ModuleLoader,
            CompDate: comps.Date,
            lineRight: ''
        },
        data() {
            return {
                orders: [],
                status: [],
                state: "0",
                data1: [],
                data2: [], orderSearchTime: {}
            }
        },
        //离开当前页面后执行  不要缓存
        destroyed: function () {
            location.reload();
        },
        methods: {
            async init() {
                //	this.orderSearchTime.starttime=new Date();
                //	this.orderSearchTime.endtime=new Date();
                //   debugger;
                let data1 = await dict.get("orderState1")
                let data2 = await dict.get("orderState2")
                this.data1 = data1
                this.data2 = data2
                var status = []
                status.push({id: "0", text: "全部", clz: "active", recordcount: 0})
                for (var d of data1) {
                    d.recordcount = 0;
                    status.push(d)
                }
                this.status = status;


                await this.iniQuery(this.orderSearchTime);

            },
            async iniQuery(orderSearchTime) {
                try {
                    let data = await ajax.post({
                        url: "api/order/buyer/getOrderList",
                        data: orderSearchTime,
                    })
                    //debugger;
                    if (data.orders) {
                        if (data.orders.length == 0) {
                            msg.info("暂无买家订单");
                        } else {
                            for (var order of data.orders) {
                                order.href = "#!m/order/order?id=" + order.id
                                // order.ashref = "#!m/order/aftermarket/index1?id=" + order.id
                                for (var s of data.sellers) {
                                    if (order.sellerId == s.orgId) {
                                        order.sellers = s
                                        break
                                    }
                                }
                                let ddddata = this.data1;
                                this.status[0].recordcount++;
                                for (let i = 0; i < ddddata.length; i++) {
                                    if (ddddata[i].id == order.state1) {
                                        this.status[i + 1].recordcount++;
                                    }
                                }
                            }
                            this.orders = data.orders.reverse();
                        }
                    }
                } catch {
                    msg.info("获取订单列表失败")
                }
            },
            doQuery() {
                this.iniQuery(this.orderSearchTime);
            },
            buyerStatus(val) {
                for (var d of this.data1) {
                    if (d.id == val) {
                        return d.text
                    }
                }
                return "完成"
            },
            sellerStatus(val) {
                for (var d of this.data2) {
                    if (d.id == val) {
                        return d.text
                    }
                }
                return "完成"
            },
            statusShow(val) {
                switch (val) {
                    case "1":
                        return "订单生成"
                    case "2":
                        return "订单交易"
                    case "3":
                        return "订单取消"
                    case "4":
                        return "订单完成"
                }
            },
            changeStatus(t) {
                for (var m of this.status) {
                    m.clz = ""
                }
                t.clz = "active"
                this.state = t.id
            },
            async openAs(order) {
                let rs = await ajax.get(`api/order/order/as/${order.id}`);
                debugger;
                window.location = `#!m/order/as?id=${rs.order.process3}`
            }
        },
        beforeMount() {

            this.init()
        },
    }
</script>
<style lang="less" scoped>
    @import url("@/common/common.less");

    .order-list {
        background-color: #ffffff;
        padding-bottom: 5px;
    }

    .lr-padding {
        margin: auto 20px;
        padding-top: 10px;
    }

    .order-info {
        position: relative;

        .order-amount {
            position: absolute;
            right: 30px;
            bottom: -10px;
        }

        span {
            margin: 0 10px;
        }
    }

    hr {
        border-color: @bg1;
        margin-left: 0;
        margin-right: 0;
    }

    .multi-controls {
        display: flex;
        flex-flow: row nowrap;

        input {
            flex: 1;
        }

        span {
            flex: 0;
        }
    }

    .search-form {
        .btn-search {
            background-color: @color1;
            border-color: @color1;
            color: @text2;
            padding-left: 30px;
            padding-right: 30px;
        }
    }

    .order-item {
        border: 1px solid @bg1;
        margin-bottom: 10px;

        .fee {
            font-size: 25px;
            color: @color1;
        }

        .detail {
            padding-bottom: 20px;

            .goods {
                flex: 1;
                display: flex;
                flex-flow: row nowrap;
                align-items: center;

                img {
                    width: 100px;
                    height: 100px;
                }

                .props {
                    display: flex;
                    flex-flow: row wrap;

                    p {
                        width: 200px;
                    }
                }

                .my-btn {
                    color: #6abf4b;
                    background-color: #ffffff;
                    border-color: #6abf4b;
                    width: 100px;
                }
            }
        }
    }
</style>
