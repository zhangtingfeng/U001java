<template>
    <div>
        <div :class='[readonly?"box  boxbackgroundcolor":"box boxbackgroundcolor"]'>
            <div class="box-header width-border bg-lightgreen">
                <h3 class="box-title ">商品信息</h3>
                <div class="box-tools pull-right">
                    <button type="button" class="btn btn-box-tool" data-widget="collapse">
                        <i :class='[readonly?"fa fa-plus":"fa fa-minus"]'></i>
                    </button>
                </div>
            </div>

            <div v-if="!readonly" class="box-body">

                <Contract-Goods :curReadOnly="readonly"  :props="props"
                                        :orderGoods="orderGoods"></Contract-Goods>

                <div class="row" style="padding-left: 15px;padding-top: 20px;">
                    <button type="button" class="btn btn-primary" style="width:132px;"
                            @click="save()">保存
                    </button>
                </div>

            </div>
            <div v-else-if="readonly" class="box-body">
                <Contract-Goods :curReadOnly="readonly"  :props="props"
                                :orderGoods="orderGoods"></Contract-Goods>

               </div>
        </div>

    </div>
</template>

<script>
    import {msg, ajax, progress} from "@f/vendor";
    import ContractGoods from '../components/contractgoods'

    export default {
        components: {
            ContractGoods
        },
        data() {
            return {

                orderGoods: this.orderFulldetail.orderGoods,
                props: this.orderFulldetail.props
            }
        },
        props: ["view", "id", "readonly", "args", "base", "orderFulldetail"],
        beforeMount() {

            this.load();

        },
        methods: {
            load() {
                //debugger;
                // history.back()
            },
            save() {
                progress.show();
                ajax.post({
                    url: 'api/order/orderedit/saveOrderGoodsinfo',
                    data: this.orderGoods

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

</style>
