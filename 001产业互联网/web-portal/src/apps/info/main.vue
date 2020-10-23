<template>
  <div class="info">
    <div class="container">
      <div class="row">
        <div class="col-md-2">
          <div id="goods-pics">
            <img class="preview" :src="picPath" />
            <div class="row">
              <div class="col-md-4" v-for="(img, idx) of goods.imgs1" :key="img.id">
                <img class="img-responsive" :src="img.path" @click="changePic(idx)" />
              </div>
            </div>
          </div>
        </div>
        <div class="col-md-10">
          <h3 class="title">{{ goods.title }}</h3>
          <p class="price">单价：￥{{ goods.unitPrice }} 元</p>
          <form class="form-inline qty">
            数量:&nbsp;
            <input class="form-control" style="width:100px;" type="number" v-model="qty" />
            <template v-if="goods.unit2">{{ goods.unit2Name }}</template>
          </form>
          <!-- <p class="primary-prop">剩余数量 : {{goods.remainQty}}</p> -->
          <!-- <p class="primary-prop">最小数量 : {{goods.minQty}}</p> -->
          <p class="primary-prop">商品分类 : {{ goods.goodsName }}</p>
          <p class="primary-prop" style="margin-top:10px;" v-if="goods.wh">仓库: {{ goods.wh.name }}</p>

          <div>
            <button @click="goToBuy" class="btn btn-primary">立即购买</button>
            <button @click="addToCart" class="btn btn-info">加入购物车</button>
          </div>
        </div>
      </div>

      <div class="pull-right">
        <!-- <a @click="im = !im"></a> -->
        <module-loader :app="['im', 'btn']" :args="{ userid:goods.createUser }">
        </module-loader>
      </div>

      <!-- <div class="im-wnd box box-solid" v-if="im">
				<div class="box-header bg-gray bg-solid">
					<h3 class="box-title">对话列表</h3>
					<div class="box-tools pull-right">
						<a class="btn btn-box-tool" @click.prevent="im = false">
							<i class="fa fa-times" />
						</a>
					</div>
				</div>
				<div class="box-body">
					<module-loader :app="['im', 'btn']" :args="{ name: this.goods.title, userid: '${uid}' }" />
				</div>
			</div> -->

      <div class="tabs">
        <a href="#" class="active">商品详情</a>
      </div>

      <div class="detail-props">
        <div class="row">
          <div v-for="(prop, i) of goods.props" :key="i" class="col-md-4">
            {{ prop.propName }} : {{ prop.propValue }}
          </div>
        </div>
      </div>
      <div class="detail">
        <img v-for="img of goods.imgs2" :key="img.id" :src="img.path" />
      </div>
    </div>
  </div>
</template>

<script>
import { comps, dict } from "@f/framework"
import { progress, msg, ajax } from "@f/vendor"
import user from "@/apps/user"
import cart from "@/apps/cart"
import Vue from "Vue"

import framework from "@f/framework"

export default {
  components: { ModuleLoader: framework.ModuleLoader },
  data() {
    return {
      im: false,
      picPath: "",
      qty: 1,
      goods: [],
    }
  },
  props: ["id"],
  methods: {
    async init() {
      try {
        let data = await ajax.post({
          url: "api/portal/sale/getSalesInfo",
          data: { id: this.id },
        })

        var goods = JSON.parse(data.goods)
        goods.props = data.props
        var imgs1 = []
        var imgs2 = []
        for (var i of data.imgs) {
          i.path = "api/trade/upload/" + i.imgPath
          if (i.type == 1) {
            imgs1.push(i)
          } else {
            imgs2.push(i)
          }
        }
        goods.imgs1 = imgs1
        goods.imgs2 = imgs2
        goods.org = JSON.parse(data.org)
        goods.wh = JSON.parse(data.wh)

        this.goods = goods
        this.picPath = this.goods.imgs1[0].path
      } catch (error) {
        msg.info("获取商品信息出错" + error)
      }
    },
    async goToBuy() {
      if (!user.user.org) {
        msg.info("请点击“我的中心”进行企业认证")
        return
      }
      if (user.user.org == this.goods.orgId) {
        msg.info("用户不可以购买本机构商品！")
        return
      }
      var postData = { "sellerId": this.goods.createUser, "goodsId": this.goods.id, "num": this.qty };
      let data = await ajax.post({ url: "api/portal/cart/goDirectBuy", data: postData });
      if ("success" == data.msg) {
        window.location = "settlement.html";
      } else {
        msg.info("提交异常");
      }
    },
    async addToCart() {
      try {
        if (!user.user.org) {
          msg.info("请点击“我的中心”进行企业认证")
          return
        }
        if (user.user.org == this.goods.orgId) {
          msg.info("用户不可以购买本机构商品！")
          return
        }
        progress.show()

        var postData = { goodsId: this.goods.id, sellerId: this.goods.createUser, num: this.qty }
        let data = await ajax.post({
          url: "api/portal/cart/addToCart",
          data: postData,
        })

        cart.init()
      } catch (error) {
        msg.info("添加购物车失败！")
      } finally {
        progress.hide()
      }
    },
    changePic(idx) {
      this.picPath = this.goods.imgs1[idx].path
    },
  },
  mounted() {
    this.init()
  },
}
</script>

<style lang="less" scoped>
@import url("@/apps/template/common.less");

.info {
  background-color: @bg3;

  img.preview {
    width: 100%;
    margin-bottom: 10px;
  }
  p.price {
    font-size: 120%;
    color: red;
  }

  button {
    border-color: @color1;
    border-radius: 0;
    border-color: @color1;
    background-color: @color1;
    width: 200px;
  }
  .btn-info {
    color: @text1;
    background-color: @bg2;
    margin-left: 30px;
  }

  .tabs {
    margin-top: 20px;
    a {
      max-width: 200px;
    }
  }
  .detail-props {
    margin: 30px;
  }

  .detail {
    margin: 30px;

    img {
      margin: 10px;
    }
  }
}

#goods-pics {
  border: 1px solid #e6e6e6;
}
.img-responsive {
  padding: 5px;
}

.im-wnd {
  position: fixed;
  bottom: 10px;
  right: 10px;
  width: 400px;
  height: 500px;
  background-color: #fff;
  z-index: 1000;
}
</style>
