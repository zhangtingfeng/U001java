<template>
  <div class="cart">
    <div class="container">
      <template v-if="carts && carts.length">
        <h4>我的购物车</h4>
        <table class="table table-striped table-hover table1">
          <thead>
            <tr class="bg-color">
              <td width="30px">&nbsp;</td>
              <td width="12%">
                <checkbox v-model="all" /><span style="margin-left:20px;">全选</span>
              </td>
              <td width="53%">商品名称</td>
              <!-- <td width="15%">件重（公斤/件）</td> -->
              <td class="center middle" width="10%">单价</td>
              <td class="center middle" width="10%">数量</td>
              <!-- <td width="5%">重量</td> -->

              <td class="center middle" width="10%">金额</td>
              <td class="center middle" width="5%">操作</td>
            </tr>
          </thead>
        </table>
        <template v-for="(cart,i) of carts">
          <table :key="i" class="table page-table">
            <tbody>
              <tr class="bg-color" v-if="cart.sellers">
                <td width="30px">&nbsp;</td>
                <td colspan="6" width="100%">
                  卖家：{{cart.sellers.orgName}}
                  <span style="margin-left:100px;"> 联系人：{{cart.sellers.seller}}</span>
                  <span style="margin-left:100px;"> 联系方式：{{cart.sellers.mobile}}</span>
                </td>
              </tr>
              <tr>
                <td @click="select(i)" width="30px">&nbsp;</td>
                <td @click="select(i)" class=" middle" width="12%">
                  <checkbox v-model="checks[i]" />
                  <img class="cart_pic" :src="cart.goods.img" />
                </td>
                <td @click="select(i)" width="53%">
                  <p class="goods-title">({{cart.goods.goodsName}}){{cart.goods.title}}</p>
                  <span v-for="p in cart.props" :key="cart.id+p.id" style="margin-right:10px;">{{p.propName}} :{{p.propValue}}</span>
                </td>
                <td class="center middle" width="10%">{{cart.goods.unitPrice}} 元</td>
                <td class="center middle" width="10%">
                  <input type="number" min="1" style="width:70%;" @change.stop="change(cart)" @keyup="change(cart)" v-model="cart.num">
                  <span> {{cart.goods.unit2Name}}</span>
                </td>
                <td class="center middle" width="10%">￥{{cart.goods.total}} 元</td>
                <td class="center middle" width="5%">
                  <a @click="deleteCart(cart,i)"><img src="imgs/icon_del_2.png"></a>
                </td>
              </tr>
            </tbody>
          </table>
        </template>
<div class="seprator" />
<table class="table table-hover table-3">
  <thead>
    <tr class="bg-color">
      <td width="30px">&nbsp;</td>
      <td width="10%">已选商品（<span class="cart-num">{{checkedNum}}</span>）</td>
      <td width="5%">|</td>
      <td width="10%" @click="clearCart">清空购物车</td>
      <td width="5%">|</td>
      <td width="30%" @click="goToList">继续购物</td>
      <td widht="18%">合计（不含运费）</td>
      <td widht="10%"><span class="cart-money">￥{{total}}</span></td>
      <td widht="2%">元</td>
      <td width="10%">
        <button @click="goBuy()" class="btn my-btn">结算</button>
      </td>
    </tr>
  </thead>
</table>
</template>
<template v-else>
  <div class="empty">
    <img src="imgs/cart_empty.png" />
    <p></p>
    <p>您的购物车还是空的</p>
    <button @click="goToList" class="btn my-btn">去逛逛</button>
  </div>
</template>
</div>
</div>
</template>

<script>
import Vue from "vue";
import { comps, dict } from "@f/framework";
import { progress, msg, ajax } from "@f/vendor";
import cart from "@/apps/cart";

export default {
  components: {
    Checkbox: comps.Checkbox
  },
  data() {
    return {
      checkedNum: 0,
      all: false,
      total: 0,
      checks: [],
      chkCarts: [],
      carts: [],
      cart
    };
  },
  watch: {
    checks() {
      this.total = 0;
      this.checkedNum = 0;
      var checks = this.checks;
      for (var i = 0; i < checks.length; i++) {
        if (checks[i]) {
          this.checkedNum++;
          this.total += this.carts[i].goods.total;
        }
      }
    },
    all() {
      for (var i = 0; i < this.checks.length; i++) {
        this.checks[i] = this.all;
      }
    },
  },
  methods: {
    async init() {
      try {
        let data = await ajax.get("api/portal/cart/getCartList");
        var checks = [];
        var sellers = [];
        if (data.carts) {
          for (var cart of data.carts) {
            checks.push(false);
            // 商品
            for (var g of data.goods) {
              // 商品照片
              for (var img of data.imgs) {
                if (g.id == img.salesId && img.type == "1" && img.idx == 1) {
                  g.img = "api/trade/upload/" + img.imgPath;
                }
              }
              if (cart.goodsId == g.id) {
                g.total = this.accMul(g.unitPrice, cart.num);
                g.volume = this.accMul(g.minQty, cart.num);
                cart.goods = g;
                break;
              }
            }
            // 商品属性
            var props = [];
            for (var p of data.props) {
              if (cart.goodsId == p.salesId) {
                props.push(p);
              }
            }
            cart.props = props;
            // 卖家
            for (var s of data.sellers) {
              if (cart.sellerId == s.id && sellers != s) {
                cart.sellers = s;
                sellers = s;
                break;
              }
            }
          }
          this.carts = data.carts;
        } else {
          this.carts = [];
        }

        this.checks = checks;
      } catch (error) {
        msg.info("获取购物车数据出错：" + error);
      }

    },
    accMul(arg1, arg2) {
      var m = 0, s1 = arg1.toString(), s2 = arg2.toString();
      try { m += s1.split(".")[1].length } catch (e) { }
      try { m += s2.split(".")[1].length } catch (e) { }
      return Number(s1.replace(".", "")) * Number(s2.replace(".", "")) / Math.pow(10, m)
    },
    deleteCart(cart, i) {
      var checks = [];
      for (var j = 0; j < this.checks.length; j++) {
        if (j != i) {
          checks.push(this.checks[j]);
        }
      }
      progress.show();
      let promise = ajax.post({
        url: "api/portal/cart/delFromCart",
        data: { id: cart.id }
      })

      promise.then(data => {
        progress.hide();
        if (this.carts.length == 1) {
          this.carts = [];
        }
        this.init();
        this.total = 0;
        this.checkedNum = 0;
        for (var i = 0; i < checks.length; i++) {
          if (checks[i]) {
            this.checkedNum++;
            this.total += this.carts[i].goods.total;
          }
        }
        this.checks = checks;
      }).catch(() => {
        msg.info('servererror', 'error')
      }).always(() => {
        progress.hide()
        cart.init();
      })

    },
    async goBuy() {
      this.chkCarts = [];
      var chkCarts = [];
      var checks = this.checks;
      for (var i = 0; i < checks.length; i++) {
        if (checks[i]) {
          var cart = this.carts[i];
          chkCarts.push({ "id": cart.id, "sellerId": cart.sellerId, "buyerId": cart.buyerId, "goodsId": cart.goodsId, "num": cart.num });
        }
      }
      // 一件商品时结算无须选中
      if (this.carts.length == 1 && chkCarts.length == 0) {
        var cart = this.carts[0];
        chkCarts.push({ "id": cart.id, "sellerId": cart.sellerId, "buyerId": cart.buyerId, "goodsId": cart.goodsId, "num": cart.num });
      }
      if (chkCarts.length == 0) {
        msg.info("请选择商品！");
        return;
      }
      var sellerId = chkCarts[0].sellerId;
      for (var cart of chkCarts) {
        if (cart.sellerId != sellerId) {
          msg.info("结算仅限同一卖家商品！");
          return;
        }
      }
      this.chkCarts = chkCarts;

      let data = await ajax.post({ url: "api/portal/cart/goBuy", data: chkCarts });
      if ("success" == data.msg) {
        window.location = "settlement.html";
      } else {
        msg.info("提交异常");
      }
    },
    select(i) {
      Vue.set(this.checks, i, !this.checks[i]);
    },
    change(cart) {
      cart.goods.total = this.accMul(cart.goods.unitPrice, cart.num);
      this.total = 0;
      this.checkedNum = 0;
      var checks = this.checks;
      for (var i = 0; i < checks.length; i++) {
        if (checks[i]) {
          this.checkedNum++;
          this.total += this.carts[i].goods.total;
        }
      }
    },
    async clearCart() {
      try {
        progress.show();
        let data = await ajax.post({
          url: "api/portal/cart/clearCarts"
        })

        this.init();
      } catch (error) {
        msg.info("清空购物车出错：" + error);
      } finally {
        progress.hide();
        cart.init();
      }
    },
    goToList() {
      window.location = "index.html#!AppList?t=0";
    }
  },
  mounted() {
    this.init();
  },
};
</script>

<style lang="less" scoped>
@import url("@/apps/template/common.less");

.cart {
  background-color: @bg1;
  padding-top: 10px;
  padding-bottom: 10px;

  .container {
    background-color: @bg2;
    padding: 0;

    .row {
      margin: 0;
      padding-left: 20px;
      padding-right: 20px;
    }
  }
}

h4 {
  padding-left: 30px;
}
.table1 {
  thead {
    background-color: @bg1;
    td {
      font-size: 16px;
      padding-top: 10px;
      padding-bottom: 10px;
    }
  }
}
div.seprator {
  height: 20px;
  background-color: @bg1;
}
.table-3 {
  .my-btn {
    background-color: @color1;
    border-color: @color1;
    display: block;
    width: 100%;
    color: @text2;
    font-size: 16px;
  }

  td {
    font-size: 16px;
    vertical-align: middle;
  }
}

.empty {
  padding: 100px auto;
  text-align: center;

  img {
    margin: 50px 0 20px 0;
  }

  .my-btn {
    background-color: @color1;
    border-color: @color1;
    display: block;
    width: 100px;
    color: @text2;
    font-size: 16px;
    margin: 50px auto 100px auto;
  }
}

.middle {
  vertical-align: middle;
}

.center {
  text-align: center;
}

.cart_pic {
  width: 80px;
  height: 80px;
  margin-left: 20px;
}

.bg-color {
  background-color: rgb(246, 250, 244);
}
.cart-num {
  color: #6abf4b;
  font-size: 16px;
}
.cart-money {
  color: #6abf4b;
  font-size: 25px;
}
.goods-title {
  font-size: 16px;
}
</style>