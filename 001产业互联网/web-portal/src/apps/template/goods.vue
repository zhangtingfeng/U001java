<template>
  <div class="goods">
    <a :href="`index.html#!AppInfo?id=${goods.id}`">
      <img :src="goods.img" class="img-responsive" />
      <h3 v-if="goods.title.length>11" :title="goods.title">{{goods.title.substring(0,10)}}...</h3>
      <h3 v-else>{{goods.title}}</h3>
    </a>
    <p style="font-weight:bold;">{{goods.org.name.substring(0,9)}} </p>
    <template v-for="(prop,j) in goods.props">
      <p :key="prop.name" v-if="j<2">{{prop.propName}}:{{prop.propValue}}</p>
    </template>
    <template v-for="k in 2">
      <p :key="k" v-if="(goods.props.length+k)<=2">&nbsp;</p>
    </template>
    <p>单价：<span class="price">￥{{goods.unitPrice}} 元</span> </p>
    <a href="#" @click.prevent="addToCart" class="shoping">
      <!-- <i class="fa fa-plus-circle"></i> -->
      <img class="icon-add" src="imgs/add2.png" />
    </a>
  </div>
</template>

<script>
import { progress, msg, ajax } from "@f/vendor";
import user from "@/apps/user";
import cart from "@/apps/cart";

export default {
  props: ["goods"],
  methods: {
    async addToCart() {
      try {
        if (!user.user.org) {
          msg.info("请点击“我的中心”进行企业认证");
          return;
        }
        if (user.user.org == this.goods.orgId) {
          msg.info("用户不可以购买本机构商品！")
          return;
        }
        progress.show();

        var postData = { "goodsId": this.goods.id, "sellerId": this.goods.createUser, "num": 1 };
        let data = await ajax.post({
          url: "api/portal/cart/addToCart",
          data: postData
        })

        cart.init();
      } catch (error) {
        console.error(error);
        msg.info("添加购物车失败！");
      } finally {
        progress.hide();

      }
    },
  }
};
</script>

<style lang="less" scoped>
.goods {
  position: relative;
  border: 1px solid #e6e6e6;

  h3 {
    font-size: 16px;
    color: #6abf4b;
    margin: 5px 10px;
  }
  p {
    margin: 3px 10px;
  }
  a.shoping {
    position: absolute;
    right: 10px;
    bottom: 10px;
    font-size: 24px;
    color: #6abf4b;
  }
  img {
    width: 206px;
    height: 206px;
  }
  .price {
    color: #6abf4b;
  }
  .icon-add {
    width: 33px;
    height: 33px;
  }
}
</style>
