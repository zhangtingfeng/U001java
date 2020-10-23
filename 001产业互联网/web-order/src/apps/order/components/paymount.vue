<template>
    <div :onclick="curReadOnly?'return false':'return true'" class="boxbody">

        <div class="col-md-3">
            <label>{{contractDayTextList[0]}}</label>
            <span v-if="curReadOnly" class="form-control">{{contract.pay1Days}}</span>
            <input v-else-if="!curReadOnly" class="form-control" type="number" v-model="contract.pay1Days"/>
        </div>

        <div class="col-md-3">
            <label>{{contractDayTextList[1]}}</label>
            <span v-if="curReadOnly" class="form-control">{{contract.pay1Amount}}</span>
            <input v-else-if="!curReadOnly" @change="change('pay1Amount')" class="form-control" type="number"
                   v-model="contract.pay1Amount"/>
        </div>

        <div v-if="contract.pay_type == '1'">
            <div class="col-md-3">
                <label>{{contractDayTextList[2]}}</label>
                <span v-if="curReadOnly" class="form-control">{{contract.pay2Days}}</span>
                <input  v-else-if="!curReadOnly" class="form-control" type="number" v-model="contract.pay2Days"/>
            </div>
            <div class="col-md-3">
                <label>{{contractDayTextList[3]}}</label>
                <span v-if="curReadOnly" class="form-control">{{contract.pay2Amount}}</span>
                <input v-else-if="!curReadOnly" @change="change('pay2Amount')" class="form-control" type="number"
                       v-model="contract.pay2Amount"/>
            </div>
        </div>
        <div v-else-if="contract.pay_type == '2'">
            <div class="col-md-3">
                <label>{{contractDayTextList[4]}}</label>
                <span v-if="curReadOnly" class="form-control">{{contract.pay2Days}}</span>
                <input  v-else-if="!curReadOnly" class="form-control" type="number" v-model="contract.pay2Days"/>
            </div>
            <div class="col-md-3">
                <label>{{contractDayTextList[5]}}</label>
                <span v-if="curReadOnly" class="form-control">{{contract.pay2Amount}}</span>
                <input  v-else-if="!curReadOnly" @change="change('pay2Amount')" class="form-control" type="number"
                       v-model="contract.pay2Amount"/>
            </div>
        </div>
    </div>
</template>

<script>
    export default {
        name: "PayMount",
        props: {
            // v-model默认传入属性值
            curReadOnly: Boolean,
            contract: Object
        },
        data() {
            return {
                contractDayTextList: [
                    "合同签订后付款(合同签订后的工作日内)", "合同签订后的工作日内(支付全款(%))",
                    "货物到达交付地点后且验收合格后(多少工作日内)", "货物到达交付地点后且验收合格后(支付全款(%))",
                    "货物在始发地经甲方检验合格后发车前(工作日内)", "经甲方检验合格后发车前(支付全款(%))"
                ],

            }
        },
        methods: {
            change(typepay1Amount) {
                //  debugger;

                if (typepay1Amount == "pay1Amount") {
                    if (this.contract.pay1Amount >= 0 && this.contract.pay1Amount <= 100) {
                        this.contract.pay2Amount = 100 - this.contract.pay1Amount;
                    }
                } else if (typepay1Amount == "pay2Amount") {
                    if (this.contract.pay2Amount >= 0 && this.contract.pay2Amount <= 100) {
                        this.contract.pay1Amount = 100 - this.contract.pay2Amount;
                    }
                }
            },
        },


    }
</script>

<style lang="less" scoped>
    @import url("@/common/common.less");
    </style>
