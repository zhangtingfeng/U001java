<template>
  <div>
    <div class="lr-padding">
      <h4>收货地址</h4>
    </div>
    <hr />
    <div class="lr-padding">
      <div class="row">
        <div class="col-md-3">
          <div class="panel panel-default">
            <div class="panel-body">
              <label>收件人：</label>
              {{rec.name}}
              <p>联系方式：{{rec.tel}}</p>
              <p>地址：{{rec.address}}</p>
              <p>
                <a class="detail-edit">修改</a>
              </p>
            </div>
          </div>
        </div>
        <div class="col-md-3">
          <div class="panel panel-default">
            <div class="panel-body">
              <div class="add-address">
                <img src="imgs/add.png" />
                <p>添加新地址</p>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
    <hr />
    <div class="lr-padding">
      <p class="my-h1">配送方式</p>
    </div>
    <hr />
    <div class="lr-padding">
      <div class="row">
        <div class="col-md-2">
          <a class="btn btn-primary my-btn1">自提</a>
        </div>
        <div class="col-md-2 col-md-offset-1">
          <a class="btn btn-primary my-btn2">物流</a>
        </div>
      </div>
    </div>
    <hr />
    <div class="lr-padding">
      <p class="my-h1" style="margin:15px 0;">商品信息</p>
    </div>
    <table :key="idx" class="table page-table my-table" v-if="order">
      <thead>
        <tr>
          <th width="20px"></th>
          <th width="70%">商品名称</th>
          <th class="center middle">单价</th>
          <th class="center middle">数量</th>
          <th class="center middle">金额</th>
        </tr>
      </thead>
      <tbody>
        <template v-for="(ogoods,i) in order.orderGoods">
          <tr :key="i">
            <td width="20px"></td>
            <td width="70%">
              <img class="pull-left" style="margin-right:20px;" weight="80" height="80" :src="ogoods.goods.img" />
              <span class="my-goods">{{ogoods.goods.title}}</span>
              <template v-for="(p,j) in ogoods.props">
                <div :key="j" v-if="j%3==0">
                  <span class="my-prop" v-if="j<ogoods.props.length">{{ogoods.props[j].propName}} :{{ogoods.props[j].propValue}}</span>
                  <span class="my-prop" v-if="(j+1)<ogoods.props.length">{{ogoods.props[j+1].propName}} :{{ogoods.props[j+1].propValue}}</span>
                  <span class="my-prop" v-if="(j+2)<ogoods.props.length">{{ogoods.props[j+2].propName}} :{{ogoods.props[j+2].propValue}}</span>
                </div>
              </template>
            </td>
            <td class="center middle" rowspan="3">{{ogoods.goodsPrice}}</td>
            <td class="center middle" rowspan="3">{{ogoods.num}}</td>
            <td class="center middle" rowspan="3">{{ogoods.total}}</td>
          </tr>
        </template>
      </tbody>
    </table>
    <hr />
    <div class="lr-padding">
      <p class="my-h1" style="margin:15px 0;">支付方式</p>
    </div>
    <hr />
    <div class="lr-padding pay-box">
      <img class="my-pay" src="imgs/wx_pay.png" />
      <img class="my-pay" src="imgs/zfb_pay.png" />
      <img class="my-pay" src="imgs/yl_pay.png" />
    </div>
    <hr />
  </div>
</template>

<script>
export default {
  data() {
    return {
      rec: {
        name: "张国强",
        tel: "18612176666",
        address: "山东省青州市驼山中路2206号",
      },
      order: this.value,
    };
  },
  props: ["value"],
  methods: {
    status(val) {
      switch (val) {
        case "1":
          return "待确认";
        default:
          return "";
      }
    },
  },
};
</script>

<style lang="less" scoped>
@import url("@/apps/template/common.less");
.lr-padding {
  margin: auto 20px;
}
hr {
  border-color: @bg1;
}
.pay-box {
  margin-bottom: 20px;
}
.my-table {
  thead {
    background-color: @bg1;
    th {
      padding-top: 10px;
      padding-bottom: 10px;
    }
  }
}
</style>
<style scoped>
.add-address {
  text-align: center;
  margin: 24px 0;
}
.my-h1 {
  font-size: 16px;
}
.my-btn1 {
  color: #6abf4b;
  background-color: #ffffff;
  border-color: #6abf4b;
  width: 100px;
}
.my-btn2 {
  color: #333333;
  background-color: #ffffff;
  border-color: #e7e7e7;
  width: 100px;
}
.my-btn1 :focus {
  background-color: #6abf4b;
  border-color: #6abf4b;
}
.my-btn1 :hover {
  background-color: #6abf4b;
  border-color: #6abf4b;
}
.my-pay {
  margin: 0 40px;
}
.my-goods {
  margin: 0 5px;
  padding: 5px 0;
}
.my-prop {
  margin: 0 5px;
}

.middle {
  vertical-align: middle;
}

.center {
  text-align: center;
}
.detail-edit {
  color: #6abf4b;
}
</style>