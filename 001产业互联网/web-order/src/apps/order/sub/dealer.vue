<template>
    <div>
        <div :class='[curReadOnly?"box collapsed-box boxbackgroundcolor":"box boxbackgroundcolor"]'>
            <div class="box-header width-border  bg-lightgreen">
                <h3 class="box-title">{{ view=='buyer'?"买家信息":"卖家信息"}}</h3>
                <div class="box-tools pull-right">
                    <button type="button" class="btn btn-box-tool" data-widget="collapse">
                        <i :class='[readonly?"fa fa-plus":"fa fa-minus"]'></i>
                    </button>
                </div>
            </div>

            <div v-if="!curReadOnly" class="box-body boxbody">

                <div class="row">
                    <div class="col-md-3">
                        <label>企业名称</label><span> <input type="text" class="form-control" name="buyer.title"
                                                         v-model="dealer.title" placeholder="请输入合同"/></span>
                    </div>
                    <div class="col-md-3">
                        <label>合同授权代表人</label><span><input type="text" class="form-control" name="buyer.signer"
                                                           v-model="dealer.signer" placeholder="请输入合同授权代表人"/></span>
                    </div>
                    <div class="col-md-3">
                        <label>合同电话</label><span><input type="text" class="form-control" name="buyer.phone"
                                                        v-model="dealer.phone" placeholder="请输入合同电话"/></span>
                    </div>
                    <div class="col-md-3">
                        <label>合同联系地址</label><span><input type="text" class="form-control" name="buyer.address"
                                                          v-model="dealer.address" placeholder="请输入合同联系地址"/></span>
                    </div>

                    <div class="col-md-3">
                        <label>开户银行</label><span><input type="text" class="form-control" name="buyer.bank"
                                                        v-model="dealer.bank" placeholder="请输入开户银行"/></span>
                    </div>
                    <div class="col-md-3">
                        <label>账户名称</label><span><input type="text" class="form-control" name="buyer.bankTitle"
                                                        v-model="dealer.bankTitle" placeholder="请输入账户名称"/></span>
                    </div>
                    <div class="col-md-3">
                        <label>银行账号</label><span><input type="text" class="form-control" name="buyer.bankAccount"
                                                        v-model="dealer.bankAccount" placeholder="请输入银行账号"/></span>
                    </div>

                    <div class="col-md-3">
                        <label>联系人</label><span><input type="text" class="form-control" name="buyer.contactor"
                                                       v-model="dealer.contactor" placeholder="请输入联系人"/></span>
                    </div>
                    <div class="col-md-3">
                        <label>联系人电话</label><span><input type="text" class="form-control" name="buyer.contactPhone"
                                                         v-model="dealer.contactPhone" placeholder="请输入联系人电话"/></span>
                    </div>
                    <div class="col-md-3">
                        <label>联系地址</label><span><input type="text" class="form-control" name="buyer.contactAddress"
                                                        v-model="dealer.contactAddress" placeholder="请输入联系地址"/></span>
                    </div>
                </div>
                <div class="row" style="padding-left: 15px;padding-top: 20px;">
                    <button type="button" class="btn btn-primary" style="width:132px;"
                            @click="save()">保存
                    </button>
                </div>
                <!--
                                <p>ID: {{ id }}</p>
                                <p>Readonly: {{ readonly }}</p>
                                <p>Args: {{ args }}</p>
                                <p>Base: {{ base }}</p>-->
            </div>
            <div v-else-if="curReadOnly" class="box-body boxbody">
                <div class="row">
                    <div class="col-md-3">
                        <label>合同</label><span class="form-control">{{dealer.title}}</span>
                    </div>
                    <div class="col-md-3">
                        <label>合同授权代表人</label><span class="form-control">{{dealer.signer}}</span>
                    </div>
                    <div class="col-md-3">
                        <label>合同电话</label><span class="form-control">{{dealer.phone}}</span>
                    </div>
                    <div class="col-md-3">
                        <label>合同联系地址</label><span class="form-control">{{dealer.address}}</span>
                    </div>

                    <div class="col-md-3">
                        <label>开户银行</label><span class="form-control">{{dealer.bank}}</span>
                    </div>
                    <div class="col-md-3">
                        <label>账户名称</label><span class="form-control">{{dealer.bankTitle}}</span>
                    </div>
                    <div class="col-md-3">
                        <label>银行账号</label><span class="form-control">{{dealer.bankAccount}}</span>
                    </div>

                    <div class="col-md-3">
                        <label>联系人</label><span class="form-control">{{dealer.contactor}}</span>
                    </div>
                    <div class="col-md-3">
                        <label>联系人电话</label><span class="form-control">{{dealer.contactPhone}}</span>
                    </div>
                    <div class="col-md-3">
                        <label>联系地址</label><span class="form-control">{{dealer.contactAddress}}</span>
                    </div>
                </div>
                <!--
                                <p>ID: {{ id }}</p>
                                <p>Readonly: {{ readonly }}</p>
                                <p>Args: {{ args }}</p>
                                <p>Base: {{ base }}</p>
                                <p>orderFulldetail: {{ orderFulldetail }}</p>-->
            </div>
        </div>

    </div>
</template>

<script>
    import {msg, ajax, progress} from "@f/vendor";

    export default {
        data() {
            return {
                curReadOnly: this.readonly,
                dealer: {},
                curorderFulldetail: this.orderFulldetail
            }
        },
        props: ["view", "id", "readonly", "args", "base", "orderFulldetail"],
        beforeMount() {
            this.load();
        },
        methods: {
            load() {
                //debugger;
                let letbuyerIdOrsellerId = "";
                if (this.view == 'buyer') {
                    letbuyerIdOrsellerId = this.curorderFulldetail.order.buyerId;
                } else {
                    letbuyerIdOrsellerId = this.curorderFulldetail.order.sellerId;
                }

                for (let i = 0; i < this.curorderFulldetail.dealers.length; i++) {
                    if (letbuyerIdOrsellerId == this.curorderFulldetail.dealers[i].orgId) {
                        this.dealer = this.curorderFulldetail.dealers[i];
                        break;
                    }
                }


                // history.back()
            },
            save(argTypeInfo) {
                //debugger;
                progress.show();
                ajax.post({
                    url: 'api/order/orderedit/savesellerinfo',
                    data: this.dealer

                }).then(d => {
                    //debugger;
                    console.log(d);
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
</style>
