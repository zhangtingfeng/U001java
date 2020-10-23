<template>
  <div class="info">
    <div class="container">
      <div class="row">
        <div class="col-md-2">
          <div id="goods-pics">
            <img class="preview" :src="picPath" />
            <template v-if="goods.imgs1">
              <div class="row">
                <div class="col-md-4" v-for="(img,idx) of goods.imgs1" :key="img.id">
                  <img class="img-responsive" :src="img" @click="changePic(idx)" />
                </div>
              </div>
            </template>
          </div>
        </div>
        <div class="col-md-10">
          <h3 class="title">{{goods.title}}</h3>
          <p class="price">单价：￥{{goods.unitPrice}} 元</p>
          <p class="primary-prop">总数量 : {{goods.totalQty}} {{goods.unit1Name}}</p>
          <p class="primary-prop">最小数量 : {{goods.minQty}} {{goods.unit2Name}}</p>
          <p class="primary-prop">商品分类 : {{goods.goodsName}}</p>
          <p class="primary-prop" style="margin-top:10px;" v-if="goods.whName"> 仓库: {{goods.whName}}</p>
        </div>
      </div>

      <div class="tabs">
        <a href="#" class="active">商品详情</a>
      </div>

      <div class="detail-props">
        <div class="row">
          <div v-for="(prop,i) of goods.props" :key="i" class="col-md-4">{{prop.propName}} : {{prop.propValue}}</div>
        </div>
      </div>
      <div class="detail" v-if="goods.imgs2">
        <img v-for="img of goods.imgs2" :key="img.id" :src="img" />
      </div>
    </div>
  </div>
</template>

<script>
import { progress, msg, ajax } from "@f/vendor";
import { dict } from "@f/framework";
import Vue from "Vue";

export default {
  data() {
    return {
      picPath: "",
    }
  },
  watch: {
    "goods": function () {
      this.picPath = this.goods.imgs1[0];
      this.wh();
      this.unit();
    }
  },
  props: ["goods"],
  methods: {
    changePic(idx) {
      this.picPath = this.goods.imgs1[idx];
    },
    async wh() {
      let data = await dict.get("org-warehouse");
      for (var w of data) {
        if (w.id == this.goods.whId) {
          Vue.set(this.goods, "whName", w.text);
        }
      }
    },
    async unit() {
      let data = await dict.get("goodsUnit");
      for (var w of data) {
        if (w.id == this.goods.unit1) {
          Vue.set(this.goods, "unit1Name", w.text);
        }
        if (w.id == this.goods.unit2) {
          Vue.set(this.goods, "unit2Name", w.text);
        }
      }
    },
  },
};
</script>


<style lang="less" scoped>
@import url("@/apps/sale/preview/common.less");

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
</style>