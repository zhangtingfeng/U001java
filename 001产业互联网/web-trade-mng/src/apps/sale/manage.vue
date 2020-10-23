<template>
  <div>
    <ul class="sales-status">
      <li>
        <a @click.prevent="chooseSales(0)"> 全部</a>
      </li>
      <li> <img src="imgs/u2350.png"></li>
      <li>
        <a @click.prevent="chooseSales(1)"> 待审核</a>
      </li>
      <li> <img src="imgs/u2350.png"></li>
      <li>
        <a @click.prevent="chooseSales(2)"> 发布中</a>
      </li>
      <li> <img src="imgs/u2350.png"></li>
      <li>
        <a @click.prevent="chooseSales(3)"> 审核未通过</a>
      </li>
      <li> <img src="imgs/u2350.png"></li>
      <li>
        <a @click.prevent="chooseSales(9)"> 已下架</a>
      </li>
    </ul>
    <form class="form-inline">
      <div>
        <label>商品名称</label>
        <input type="text" class="form-control" name="name" v-model="name" placeholder="请输入商品名称">
        <label>发布标题</label>
        <input type="text" class="form-control" name="title" v-model="title" placeholder="请输入发布标题">
        <label>发布时间</label>
        <date name="startDt" data-vv-name="startDt" v-model="startDt"></date>
        <label>-</label>
        <date name="endDt" data-vv-name="endDt" v-model="endDt"></date>
        <input @click="doSearch" type="button" class="btn btn-primary" value="搜索" />
      </div>
    </form>
    <table class="table table-hover page-table" style="margin-top:10px;">
      <thead>
        <tr class="bg-gray">
          <th width="45%">商品信息</th>
          <th width="15%">发布时间</th>
          <th width="10%">失效时间</th>
          <th width="15%">当前状态</th>
          <th width="15%">操作</th>
        </tr>
      </thead>
    </table>
    <template v-for="sale in sales">
      <table :key="sale.id" class="table table-hover page-table sales" v-if="sale.status==state|| state==0">
        <tbody>
          <tr class="item-code">
            <td colspan="5">发布编号：{{sale.code}}
              <span><a data-toggle="modal" @click.prevent="view(sale)" class="pull-right" style="margin-right:30px;">发布预览</a></span>
              <span v-if="sale.status==2"><a style="float:right;margin-right:30px;">审核记录</a></span>
            </td>
          </tr>
          <tr class="item-content">
            <td width="49%">发布标题：{{sale.title}}</td>
            <td width="12%" rowspan="2" class="middle">{{sale.createTime}}</td>
            <td width="12%" rowspan="2" class="middle">{{sale.expiresTime}}</td>
            <td width="10%" rowspan="2" class="middle">{{status(sale.status)}}</td>
            <td width="15%" rowspan="2" class="middle">
              <template v-if="sale.status==1">
                <button @click="doDel(sale.id)" class="btn btn-primary">删除</button>
              </template>
              <template v-else-if="sale.status==2">
                <button @click="doRemove(sale.id)" class="btn btn-primary">下架</button>
              </template>
              <template v-else>
                <button @click="doReleaseAgain(sale.id)" class="btn btn-primary">重新发布</button>
                <button @click="doDel(sale.id)" class="btn btn-primary">删除</button>
              </template>
            </td>
          </tr>
          <tr class="item-content">
            <td><span v-for="prop of sale.props" :key="prop.id">{{prop.propName}} : {{prop.propValue}}</span></td>
          </tr>
          <tr class="item-info">
            <td colspan="5">
              <span>商品分类：{{sale.goodsName}}</span>
              <span>发货仓库：{{sale.whName}}</span>
              <span>含税单价（元/公斤）：{{sale.unitPrice}}</span>
              <span>发布总量（公斤）：{{sale.totalQty}}</span>
              <span>最小成交量（公斤）：{{sale.minQty}}</span>
            </td>
          </tr>
        </tbody>
      </table>
    </template>

    <!-- Modal -->
    <div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
      <div class="modal-dialog" role="document">
        <div class="modal-content">
          <div class="modal-header">
            <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
            <h4 class="modal-title" id="myModalLabel">商品详情预览</h4>
          </div>
          <div class="modal-body">
            <preview :goods="viewGoods" v-if="viewGoods"></preview>
          </div>
          <div class="modal-footer">
            <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { dict, views, editors } from "@f/framework";
import { msg, ajax } from "@f/vendor";
import Preview from "./preview";

export default {
  components: {
    district: views.get("district"),
    date: editors.get("date"),
    Preview
  },
  data() {
    return {
      form: {},
      name: "",
      code: "",
      title: "",
      startDt: "",
      endDt: "",
      state: 0,
      sales: [],
      viewGoods: {//预览对象
        imgs1: [],
        imgs2: [],
        props: [],
      },
    };
  },
  methods: {
    async getSalesList(postData) {
      try {
        let data = await ajax.post({
          url: "api/trade/listing-sale/getSalesList",
          data: postData
        });

        for (var g of data.goods) {
          var props = [];
          for (var p of data.props) {
            if (g.id == p.salesId) {
              props.push(p);
            }
          }
          g.props = props;
          var imgs1 = [];
          var imgs2 = [];
          for (var c of data.imgs) {
            if (g.id == c.salesId) {
              var img = "api/trade/upload/" + c.imgPath;
              if (c.type == "1") {
                imgs1.push(img);
              } else {
                imgs2.push(img);
              }
            }
          }
          g.imgs1 = imgs1;
          g.imgs2 = imgs2;
        }
        this.getWhName(data.goods);
      } catch (err) {
        msg.info("读取失败" + err);
      }
    },
    chooseSales(val) {
      this.state = val;
    },
    async getWhName(sales) {
      let data = await dict.get("org-warehouse");
      for (var sale of sales) {
        for (var d of data) {
          if (sale.whId == d.id) {
            sale.whName = d.text;
          }
        }
      }
      this.sales = sales;
    },
    status(val) {
      if (val == 1) {
        return "待审核";
      } else if (val == 2) {
        return "发布中";
      } else if (val == 3) {
        return "审核未通过";
      } else {
        return "已下架";
      }
    },
    doSearch() {
      this.form = $("form").serializeArray();
      var params = {};
      for (var f of this.form) {
        params[f.name] = f.value;
      }
      this.getSalesList(params);
    },
    doDel(saleId) {
      let promise = ajax.post({
        url: "api/trade/listing-sale/delete",
        data: { saleId: saleId }
      });

      promise
        .then(data => {
          this.getSalesList();
        })
        .catch(rs => {
          msg.info("删除失败");
        })
    },
    doRemove(saleId) {
      let promise = ajax.post({
        url: "api/trade/listing-sale/remove",
        data: { saleId: saleId }
      });

      promise
        .then(data => {
          this.getSalesList();
        })
        .catch(rs => {
          msg.info("下架失败");
        })
    },
    doReleaseAgain(saleId) {
      let promise = ajax.post({
        url: "api/trade/listing-sale/releaseAgain",
        data: { saleId: saleId }
      });

      promise
        .then(data => {
          this.getSalesList();
        })
        .catch(rs => {
          msg.info("重新发布失败");
        })
    },
    view(sale) {
      this.viewGoods = sale;
      $("#myModal").modal();
    },
  },
  mounted() {
    this.getSalesList({});
  }
};
</script>

<style scoped>
ul li {
  display: inline-block;
  font-size: 16px;
  margin: 0 2%;
}
span {
  margin: 0 5% 0 0;
}
span span {
  margin: 0 0.5% 0 0;
}
.sales {
  line-height: 2.5;
  margin-top: 15px;
}

thead tr {
  text-align: center;
  font-size: 13px;
}
th {
  text-align: center;
}
.item-code td {
  border-top: #bfbfbf solid 1px;
  border-left: #bfbfbf solid 1px;
  border-right: #bfbfbf solid 1px;
  border-collapse: separate;
  border-spacing: 10px;
}
.item-content {
  background-color: white;
}
.item-content td {
  border-left: #bfbfbf solid 1px;
  border-right: #f2f2f2 solid 1px;
}
.item-content .middle {
  vertical-align: middle;
  text-align: center;
}
.item-info {
  border-left: #bfbfbf solid 1px;
  border-right: #bfbfbf solid 1px;
  border-bottom: #bfbfbf solid 1px;
}

.modal-dialog {
  width: 100%;
}
</style>