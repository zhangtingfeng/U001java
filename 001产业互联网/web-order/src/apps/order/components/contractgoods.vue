<template>
    <div>

        <table style="border-color: #F5F5F5;!important; border-width:1px !important; border-style:solid !important;;"
               class="table page-table my-table lr-padding">
            <thead>
            <tr id="textAlignCenter" style="background-color: #F5F5F5">
                <th width="5%">序号</th>
                <th width="70%">商品名称</th>
                <th>单价</th>
                <th>数量</th>
                <th>金额(元)</th>
            </tr>
            </thead>
            <tbody>
            <template v-for="(og,i) in orderGoods">
                <tr :key="i">
                    <td class="textAlignCenter" width="5%">{{i+1}}</td>
                    <td width="70%">
                        <img class="pull-left" style="margin-right:20px;" weight="80" height="80"
                             :src="og.goods.img"/>
                        <span class="my-goods">（{{og.goods.goodsName}}） {{og.goods.title}}</span>
                        <div>

                            <template v-for="(p,j) in props">
                                        <span v-if="p.salesId==og.goods.id" class="my-prop" :key="j"
                                              style="margin:0 5px;">{{p.propName}} :{{p.propValue}}</span>
                            </template>
                        </div>
                    </td>
                    <td  v-if="!curReadOnly" class="textAlignCenter"><input  type="number" @change="change(og,i)"
                                                       @click.stop="change(og,i)"
                                                       v-model="og.goodsPrice" style="width:100px;">

                    </td>

                    <td v-if="!curReadOnly" class="textAlignCenter"><input type="number" @change="change(og,i)"
                                                       @click.stop="change(og,i)"
                                                       v-model="og.num" style="width:100px;">
                    </td>
                    <td v-if="!curReadOnly" class="textAlignCenter"><input type="number" v-model="og.amountPrice"
                                                       style="width:100px;">
                    </td>
                    <td v-if="curReadOnly" class="textAlignCenter">{{og.goodsPrice}}</td>
                    <td v-if="curReadOnly" class="textAlignCenter">{{og.num}}</td>
                    <td v-if="curReadOnly" class="textAlignCenter">{{og.amountPrice}}</td>

                </tr>
            </template>
            </tbody>
            <tfoot>
            <tr>
                <td></td>
                <td></td>
                <th>金额：</th>
                <th colspan="2">￥{{total}}元</th>
            </tr>
            </tfoot>
        </table>
    </div>
</template>

<script>
    export default {
        name: "ContractGoods",
        props: {
            // v-model默认传入属性值
            curReadOnly: Boolean,
            orderGoods: Array,
            props: Array
        },
        mounted() {
            this.getTotal();
        },
        data() {
            return {
                total: 0,
            }
        },
        methods: {
            change(og, i) {
                og.goodsPrice = Number(og.goodsPrice);
                og.amountPrice = (og.goodsPrice * og.num).toFixed(2);
                this.getTotal();
            },


            getTotal() {
                //debugger;
                var total = 0;
                for (var og of this.orderGoods) {
                    total += parseFloat(og.amountPrice);
                }
                this.total = total.toFixed(2);

            },
        },


    }
</script>

<style lang="less" scoped>

    #textAlignCenter th{
        text-align: center;
    }
    #textAlignCenter1 th{
        text-align: center;

    }
    .textAlignCenter{
        text-align: center;

        border-color: #F5F5F5 !important;
        border-width:1px !important;
        border-style:solid !important;
    }
</style>
