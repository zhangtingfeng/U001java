<template>
    <div>
        <!--都改成放在一起 -->
        <div :class='[curReadOnly?"box collapsed-box boxbackgroundcolor":"box boxbackgroundcolor"]'>
            <div class="box-header width-border bg-lightgreen">
                <h3 class="box-title">合同信息</h3>
                <div class="box-tools pull-right">
                    <button class="btn btn-box-tool" data-widget="collapse" type="button">
                        <i :class='[curReadOnly?"fa fa-plus":"fa fa-minus"]'></i>
                    </button>
                </div>
            </div>

            <div class="box-body boxbody" v-if="!curReadOnly">
                <div class="row">
                    <div class="col-md-12">
                        <label>选择合同模板</label>
                        <select class="form-control" style="width:50%;margin:15px 0;">
                            <option value="1">交易合同模板</option>
                        </select>
                    </div>
                </div>


                <!--处理加工包装 -->
                <div class="row">
                    <div class="col-md-3" v-for="contractOBJ in contractJsonList">
                        <label>{{contractOBJ.name}}</label>
                        <date v-if="contractOBJ.editor=='date'" name="delivery_Start_dt"
                              v-model="contract[contractOBJ.Vmodel]"></date>
                        <input v-else-if="contractOBJ.editor=='input'" :type="contractOBJ.type"
                               :placeholder="'请输入'+contractOBJ.name" class="form-control"
                               v-model="contract[contractOBJ.Vmodel]"></input>
                        <select v-else-if="contractOBJ.editor=='select'" class="form-control" :name="contractOBJ.Vmodel"
                                v-model="contract[contractOBJ.Vmodel]">
                            <option :value="coupon.id" v-for="coupon in dicList[contractOBJ.dicList]">{{coupon.text}}
                            </option>
                        </select>

                    </div>

                    <Pay-Mount :curReadOnly="curReadOnly"
                               :contract="contract"></Pay-Mount>


                </div>


                <Contract-Specification :curReadOnly="curReadOnly"
                                        :specificationsList="specificationsList"></Contract-Specification>

                <div class="row" style="padding-left: 15px;padding-top: 20px;">
                    <button type="button" class="btn btn-primary" style="width:132px;"
                            @click="save()">保存
                    </button>
                </div>

            </div>

            <div class="box-body boxbody" v-else-if="curReadOnly">

                <!--处理加工包装 -->
                <div class="row">
                    <div class="col-md-3" v-for="contractOBJ in contractJsonList">
                        <label>{{contractOBJ.name}}</label>
                        <span v-if="contractOBJ.editor=='select'" class="form-control">{{getDicValueList(contractOBJ.Vmodel,contractOBJ.dicList)}}</span>
                        <span v-else class="form-control">{{contract[contractOBJ.Vmodel]}}</span>
                    </div>

                    <Pay-Mount :curReadOnly="curReadOnly"
                               :contract="contract"></Pay-Mount>

                </div>

                <Contract-Specification :curReadOnly="curReadOnly"
                                        :specificationsList="specificationsList"></Contract-Specification>

            </div>
        </div>
    </div>
</template>

<script>
    import {comps, dict, editors} from "@f/framework";
    import {msg, ajax, progress} from "@f/vendor";
    import pubFuncton from "../../pubcommon";
    import ContractSpecification from '../components/ContractSpecification'
    import PayMount from '../components/paymount'

    export default {
        components: {
            date: editors.get("date"), ContractSpecification, PayMount
        },
        data() {
            return {

                curReadOnly: this.readonly,
                seller: {},
                buyer: {},
                contract: {},
                specificationsList: [],
                total: 0,

                orderGoods: this.orderFulldetail.orderGoods,
                curorderFulldetail: this.orderFulldetail,
                contractJsonList: {},
                contractSpecificationList: [],
                dicList: {},

            }
        },
        props: ["view", "id", "readonly", "args", "base", "orderFulldetail"],
        beforeMount() {

            this.load();

        },
        async mounted() {


        },
        methods: {

///S209M00003
            async load() {
                for (let t of this.orderFulldetail.dealers) {
                    if (t.type == "1") {
                        this.buyer = t;
                    } else if (t.type == "2") {
                        this.seller = t;
                    }
                }
                this.goods = this.orderFulldetail.goods;


                this.contract = this.orderFulldetail.contract;

                let letChckList = [];
                for (let i = 0; i < this.orderFulldetail.contract.specificationsList.length; i++) {
                    letChckList.push(this.orderFulldetail.contract.specificationsList[i].ifcheck);
                }
                this.specificationsList = letChckList;
                // this.specificationsList=this.orderFulldetail.specificationsList;
                // debugger;

                let contractpacksevicesList = await dict.get("contractpacksevices");
                let contractneedpackList = await dict.get("contractneedpack");
                let contractpaytypeList = await dict.get("contractpaytype");

                this.dicList.packList = contractpacksevicesList;
                this.dicList.needpackList = contractneedpackList;
                this.dicList.contractpaytypeList = contractpaytypeList;

                // =lettempList;
                //  debugger;
                if (this.curReadOnly) {
                    // debugger;
                    // this.accountList = await pubFuncton.pubFuncton();
                    //this.contract.needpack = pubFuncton.getShowTextaboutDicValue(this.contract.needpack, this.dicList.needpackList);
                    // this.contract.pack = pubFuncton.getShowTextaboutDicValue(this.contract.pack, this.dicList.packList);
                }
                this.contractJsonList = require("../../../../static/contract0.json");
                this.contractSpecificationList = require("../../../../static/contractSpecification.json");
            },
            async save(argTypeInfo) {
                debugger;


                let letthis = this;
                let letAll = parseFloat(this.contract.pay1Amount) + parseFloat(this.contract.pay2Amount);


                if (!this.contract.pay_type) {
                    alert('费用结算方式必须选择');
                }

                if ((letAll) != 100) {
                    alert("第一次付款(" + (this.contract.pay1Amount ? this.contract.pay1Amount : 0) + ")和第二次付款(" + (this.contract.pay2Amount ? this.contract.pay2Amount : 0) + ")总和应该是100%");
                    return;
                }

                progress.show();

                // let letChckList=[];
                for (let i = 0; i < this.orderFulldetail.contract.specificationsList.length; i++) {
                    this.orderFulldetail.contract.specificationsList[i].ifcheck = this.specificationsList[i];
//                    letChckList.push();
                }
                /*
                // this.specificationsList=letChckList;
                ajax.post({
                    url: 'api/order/orderedit/savepackageinfo',
                    data: this.contract
                }), ajax.post({
                    url: 'api/order/orderedit/savepayinfo',
                    data: this.contract

                }),
                    */
                let letcontractData = await ajax.post({
                    url: 'api/order/orderedit/saveOrderContractinfo',
                    data: this.contract
                });
                this.contract = letcontractData.OrderContract;

                await ajax.post({
                    url: 'api/order/orderedit/saveOrderSpecificationsinfo',
                    data: this.orderFulldetail.contract.specificationsList
                });
                progress.hide();
                msg.info("操作成功");


            },
            getDicValueList(Vmodel, dicList) {
                //  pubFuncton.getShowTextaboutDicValue(contract[contractOBJ.Vmodel], dicList[contractOBJ.dicList])；
                //   debugger;
                // return Vmodel;
                return pubFuncton.getShowTextaboutDicValue(this.contract[Vmodel], this.dicList[dicList])
            }
        }
    }
</script>

<style lang="less" scoped>
    @import url("@/common/common.less");

    .panel-body {
        .row {
            margin: 0 0 10px 0;
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
