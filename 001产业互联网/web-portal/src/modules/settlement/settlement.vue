<template>
  <div>
    <div class="lr-padding">
      <h4>收货地址</h4>
    </div>
    <hr />
    <div class="lr-padding">
      <div class="row">
        <div class="col-md-3" v-for="(addr,idx) of rcvAddrs" :key="addr.id">
          <div class="panel panel-default">
            <div class="panel-body" @click="chooseAddr(idx)" :class="addr.active?'active':''">
              <label>收件人：{{addr.name}}</label><span v-if="addr.type==1" class="pull-right" style="color:grey;font-style:italic;">默认地址</span>
              <p>联系方式：{{addr.tel}}</p>
              <p>地址：
                <district :value="addr.area" />
                {{addr.address}}
              </p>
              <p>
                <a class="a-edit" @click="goToEdit">修改</a>
              </p>
            </div>
          </div>
        </div>
        <div class="col-md-3">
          <div class="panel panel-default">
            <div class="panel-body">
              <div class="add-address" @click="addRcvAddr">
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
      <p class="my-h1" style="margin:15px 0;">商品信息</p>
    </div>
    <table class="table page-table my-table" v-if="carts">
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
        <template v-for="(cart,i) in carts">
          <tr :key="i">
            <td width="20px"></td>
            <td width="70%">
              <img class="pull-left cart_pic" style="margin-right:20px;" :src="cart.img" />
              <span class="my-goods">({{cart.goods.goodsName}}){{cart.goods.title}}</span>
              <template v-for="(p,j) in cart.props">
                <div :key="j" v-if="j%3==0">
                  <span class="my-prop" v-if="j<cart.props.length">{{cart.props[j].propName}} :{{cart.props[j].propValue}}</span>
                  <span class="my-prop" v-if="(j+1)<cart.props.length">{{cart.props[j+1].propName}} :{{cart.props[j+1].propValue}}</span>
                  <span class="my-prop" v-if="(j+2)<cart.props.length">{{cart.props[j+2].propName}} :{{cart.props[j+2].propValue}}</span>
                </div>
              </template>
            </td>
            <td class="center middle">{{cart.goods.unitPrice}} 元</td>
            <td class="center middle">{{cart.num}} {{cart.goods.unit2Name}}</td>
            <td class="center middle">￥{{cart.goods.total}} 元</td>
          </tr>
        </template>
      </tbody>
    </table>
    <hr />
    <div class="lr-padding" style="margin:50px 0 50px 20px">
      <div class="row">
        <div class="col-md-2">
          <a @click="goToOrder" class="btn btn-primary my-btn1">提交订单</a>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { views } from "@f/framework";
import { progress, msg, ajax } from "@f/vendor";

export default {
  components: { District: views.get("district") },
  data() {
    return {
      rcvAddrs: [],
      addr: {},
      carts: [],
    };
  },
  methods: {
    async init() {
      try {
        let data = await ajax.post("api/portal/cart/getSettlementList");
        for (var c of data.carts) {
          var props = [];
          for (var p of data.props) {
            if (c.goodsId == p.salesId) {
              props.push(p);
            }
          }
          c.props = props;
          for (var i of data.imgs) {
            if (c.goodsId == i.salesId) {
              c.img = "api/trade/upload/" + i.imgPath;
            }
          }
          for (var g of data.goods) {
            if (c.goodsId == g.id) {
              var total = g.unitPrice * c.num;
              g.total = total;
              c.goods = g;
            }
          }
        }
        for (var a of data.addrs) {
          if (a.type == 1) {
            a.active = true;
            this.addr = a;
            break;
          }
        }
        this.carts = data.carts;
        this.rcvAddrs = data.addrs;
      } catch (error) {
        msg.info("获取数据出错！" + error.msg);

        window.location = "cart.html";
      }
    },
    status(val) {
      switch (val) {
        case "1":
          return "待确认";
        default:
          return "";
      }
    },
    async goToOrder() {
      try {
        progress.show();
        if (this.addr && this.addr.id) {
          let letdata = { carts: this.carts, addrId: this.addr.id };
          let data = await ajax
            .post({
              url: "api/order/order/createOrders",
              data: letdata,
            });

          window.location.href = "/platform/index.html#!menu/3001";
        }

      } catch (error) {
        msg.info("生成订单失败！");
      }
      finally {
        progress.hide();
      }
    }
    ,
    addRcvAddr() {
      window.location.href = "/platform/index.html#!menu/5002";
    }
    ,
    goToEdit() {
      window.location.href = "/platform/index.html#!menu/5002";
    }
    ,
    chooseAddr(idx) {
      var rcvAddrs = this.rcvAddrs;
      for (let i in rcvAddrs) {
        if (i == idx) {
          rcvAddrs[i].active = true;
          this.addr = rcvAddrs[i];
        } else {
          rcvAddrs[i].active = false;
        }
      }
    }
  },
  mounted() {
    this.init();
  }
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

.active {
  color: #ffffff;
  background-color: #6abf4b;
}

.cart_pic {
  width: 80px;
  height: 80px;
  margin-left: 20px;
}

.a-edit{
  color: #0033CC;
}
</style>
