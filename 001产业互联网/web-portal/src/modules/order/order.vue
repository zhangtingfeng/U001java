<template>
  <div class="order-list">
    <div v-if="state==1">
      <div class="lr-padding">
        <h4>我的订单</h4>
      </div>
      <hr />
      <div class="lr-padding">
        <form class="search-form">
          <div class="row">
            <div class="col-md-4">
              <label class="control-label">商品名称</label>
              <input class="form-control" />
            </div>
            <div class="col-md-4">
              <label class="control-label">发布编号</label>
              <input class="form-control" />
            </div>
            <div class="col-md-4">
              <label class="control-label">发布标题</label>
              <input class="form-control" />
            </div>
            <div class="col-md-4">
              <label class="control-label">发布时间</label>
              <div class="multi-controls">
                <comp-date />
                <span>&nbsp;至&nbsp;</span>
                <comp-date />
              </div>
            </div>
            <div class="col-md-4">
              <label class="control-label">&nbsp;</label>
              <div class="multi-controls">
                <button class="btn btn-search">
                  <i class="fa fa-search"></i>搜索
                </button>
              </div>
            </div>
          </div>
        </form>
      </div>
      <hr />
      <div class="lr-padding">
        <template v-for="order in orders">
          <div class="order-item" :key="order.id">
            <div class="lr-padding order-info">
              <h4>
                交易状态:{{status(order.status)}}
                <!-- <a class="btn" href="#" @click.prevent="showDetail(order.id)">订单详情</a> -->
              </h4>
              <p>
                {{order.createTime}}
                |
                订单编号:{{order.id}} |
                卖家: {{order.sellers.orgName}}
              </p>
              <p class="order-amount">
                订单总额:
                <span class="fee">￥{{order.total}}</span>
              </p>

            </div>
            <hr />
            <div class="detail lr-padding">
              <div class="goods" v-for="(ogoods, i) of order.orderGoods" :key="i">
                <img :src="ogoods.goods.img" />
                <div class="info lr-padding">
                  <h4>{{ogoods.goods.title}}</h4>
                  <div class="props">
                    <p>单件价格:{{ogoods.goodsPrice}}</p>
                    <p>数量:{{ogoods.num}}</p>
                    <p v-for="p in ogoods.props" :key="p.propName">{{p.propName}} :{{p.propValue}}</p>
                  </div>
                </div>
                <a class="btn pull-right my-btn" href="#" @click.prevent="showDetail(order.id)">订单详情</a>
              </div>
            </div>
          </div>
        </template>
      </div>
    </div>
    <div v-if="state==2">
      <detail v-model="order"></detail>
    </div>
  </div>
</template>

<script>
import { msg, ajax } from "@f/vendor";
import { comps } from "@f/framework";
import Detail from "./detail";

export default {
  components: {
    Detail,
    CompDate: comps.Date,
  },
  data() {
    return {
      order: {},
      orders: [],
      state: 1,
    };
  },
  methods: {
    init() {
      ajax
        .post({
          url: "api/trade/order/getBuyerOrderList",
          data: {},
        })
        .then((data) => {
          for (var order of data.orders) {
            var orderGoods = [];
            var total = 0;
            for (var og of data.orderGoods) {
              for (var g of data.goods) {
                if (og.goodsId == g.id) {
                  og.goods = g;
                  var props = [];
                  for (var p of data.props) {
                    if (g.id == p.salesId) {
                      props.push(p);
                    }
                  }
                  og.props = props;
                  break;
                }
              }
              if (order.id == og.orderId) {
                og.total = og.goodsPrice * og.num;
                total += og.total;
                orderGoods.push(og);
              }
            }
            order.orderGoods = orderGoods;
            order.total = total;

            for (var s of data.sellers) {
              if (order.sellerId == s.id) {
                order.sellers = s;
                break;
              }
            }
          }
          var i = 1;
          for (var order of data.orders) {
            for (var g of order.orderGoods) {
              for (let m in data.images) {
                if (g.goodsId == m && data.images[m].length > 0) {
                  g.goods.images = data.images[m];
                  g.goods.img = "api/trade/upload/" + g.images[0].path;
                }
              }
              if (!g.goods.images) {
                g.goods.img = "imgs/apples/pic_" + i + ".png";
                if (i == 10) {
                  i = 1;
                } else {
                  i++;
                }
              }
            }
          }
          console.log(data.orders);
          this.orders = data.orders;
        });
    },
    status(val) {
      switch (val) {
        case "1":
          return "待确认";
        default:
          return "";
      }
    },
    showDetail(id) {
      for (var order of this.orders) {
        if (order.id == id) {
          this.order = order;
        }
      }
      this.state = 2;
    },
  },
  mounted() {
    this.init();
  },
};
</script>
<style lang="less" scoped>
@import url("@/apps/template/common.less");

.lr-padding {
  margin: auto 20px;
}
.order-info {
  position: relative;
  .order-amount {
    position: absolute;
    right: 30px;
    bottom: -10px;
  }
}
hr {
  border-color: @bg1;
  margin-left: 0;
  margin-right: 0;
}
.multi-controls {
  display: flex;
  flex-flow: row nowrap;

  input {
    flex: 1;
  }
  span {
    flex: 0;
  }
}

.search-form {
  .btn-search {
    background-color: @color1;
    border-color: @color1;
    color: @text2;
    padding-left: 30px;
    padding-right: 30px;
  }
}

.order-item {
  border: 1px solid @bg1;
  margin-bottom: 10px;
  .fee {
    font-size: 25px;
    color: @color1;
  }
  .detail {
    padding-bottom: 20px;
    .goods {
      flex: 1;
      display: flex;
      flex-flow: row nowrap;
      align-items: center;

      img {
        width: 100px;
        height: 100px;
      }

      .props {
        display: flex;
        flex-flow: row wrap;

        p {
          width: 200px;
        }
      }
      .my-btn {
        color: #6abf4b;
        background-color: #ffffff;
        border-color: #6abf4b;
        width: 100px;
      }
    }
  }
}
</style>