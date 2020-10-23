<template>
  <div>
    <div>
      <ul class="sales-status">
        <template v-for="(item,idx) of items">
          <li :key="item.name">
            <a @click.prevent="chooseSales(item.value)">{{item.name}}</a>
          </li>
          <li :key="idx" v-if="idx<items.length-1">
            <p class="fix-text" :key="item.name"></p>
          </li>
        </template>
      </ul>
    </div>
    <div>
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
    </div>

    <div style="margin-top:20px;">
      <table class="table table-hover page-table">
        <thead>
          <tr class="bg-gray">
            <th width="60%">商品信息</th>
            <th width="10%">发布时间</th>
            <th width="10%">交易商</th>
            <th width="10%">当前状态</th>
            <th width="10%">操作</th>
          </tr>
        </thead>
      </table>
      <template v-for="sale in sales">
        <table :key="sale.id" class="table table-hover page-table sales" v-if="sale.status==state|| state==0">
          <tbody>
            <tr class="item-code">
              <td colspan="6">
                发布编号：{{sale.code}}
                <span><a data-toggle="modal" @click.prevent="view(sale)" class="pull-right" style="margin-right:30px;">发布预览</a></span>
              </td>
            </tr>
            <tr class="item-content">
              <td width="7%" height="100px" rowspan="2" class="middle"><img :src="sale.imgs1[0]" alt="商品图片" width="100px" height="100px"></td>
              <td width="55%">发布标题：{{sale.title}}</td>
              <td width="10%" rowspan="2" class="middle">{{sale.createTime}}</td>
              <td width="10%" rowspan="2" class="middle" v-if="sale.org">{{sale.org.name}}</td>
              <td width="10%" rowspan="2" class="middle" v-else></td>
              <td width="10%" rowspan="2" class="middle">{{status(sale.status)}}</td>
              <td width="10%" rowspan="2" class="middle">
                <template v-if="sale.status==1">
                  <input @click="doapprove(sale.id)" type="button" class="btn btn-primary" value="审批" />
                </template>
                <template v-else-if="sale.status==2">
                  <input @click="offShelf(sale.id)" type="button" class="btn btn-primary" value="下架" />
                </template>
              </td>

            </tr>
            <tr class="item-content">
              <td height="80px" position="absolute" overflow="auto">
                <span v-for="prop of sale.props" :key="prop.id">{{prop.propName}} : {{prop.propValue}}</span>
              </td>
            </tr>
            <tr class="item-info">
              <td colspan="6">
                <span>商品分类：{{sale.goodsName}}</span>
                <span>发货仓库所在地：{{sale.wh.name}}</span>
                <span>含税单价：{{sale.unitPrice}} 元</span>
                <span>发布总量：{{sale.totalQty}} {{sale.unit1Name}}</span>
                <span>最小成交量：{{sale.minQty}} {{sale.unit2Name}}</span>
              </td>
            </tr>
          </tbody>
        </table>
      </template>
    </div>

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

    <div class="modal fade" id="approveModel" tabindex="-1" role="dialog">
      <div class="modal-dialog" role="document">
        <div class="modal-content" style="overflow-y:auto;">
          <div class="modal-header">
            <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
            <h4 class="modal-title" id="myModalLabel">审批</h4>
          </div>
          <div class="modal-body">
            <div class="row">
              <div class="form-group col-md-12">
                <label class="control-label red">审批意见</label>
                <textarea class="form-control" name="remark" v-model="remark" rows="10"></textarea>
              </div>
            </div>
          </div>
          <div class="modal-footer">
            <button type="button" class="btn btn-primary" @click="approve('1')">审批通过</button>
            <button type="button" class="btn btn-danger" @click="approve('2')">审批退回</button>
            <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { dict, views, editors } from "@f/framework";
import { progress, msg, ajax } from "@f/vendor";
import util from "../common/util";
import Preview from "./preview";

let vm;
export default {
  components: {
    district: views.get("district"),
    date: editors.get("date"),
    select2: editors.get("select2"),
    Preview
  },
  data() {
    vm = this;
    return {
      hasSelect: this.select,
      remark: "",
      saleid: "",
      form: {},
      platform: "",
      name: "",
      code: "",
      title: "",
      startDt: "",
      endDt: "",
      state: 0,
      sales: [],
      items: [
        { name: "全部", value: "0" }, { name: "待审核", value: "1" }, { name: "发布中", value: "2" },
        { name: "审核未通过", value: "3" }, { name: "已下架", value: "9" }
      ],
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
          url: "api/platform/listing-sale/getSalesList",
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
          for (var o of data.orgs) {
            if (g.orgId == o.id) {
              g.org = o;
            }
          }
          for (var w of data.whs) {
            if (g.whId == w.id) {
              g.wh = w;
            }
          }

          this.sales = data.goods;
        }
      } catch (error) {
        msg.info("获取商品信息出错" + error);
      }
    },
    chooseSales(val) {
      this.state = val;
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
    doapprove(value) {
      let objs = value;
      if (objs) {
        vm.remark = "";
        vm.saleid = objs;
        $("#approveModel").modal("show");
      }
    },
    view(sale) {
      this.viewGoods = sale;
      $("#myModal").modal();
    },
    approve(result) {
      progress.show();
      ajax.post({
        url: "api/platform/listing-sale/approve",
        data: { salesId: this.saleid, result: result, remark: this.remark }
      }).then(d => {
        if (d.success) {
          msg.info("审批完成");
          this.getSalesList();
          $("#approveModel").modal("hide");
        }
      }).always(() => {
        progress.hide();
      })
    },
    offShelf(value) {
      progress.show();
      ajax.post({
        url: "api/platform/listing-sale/offShelf",
        data: value
      }).then(d => {
        if (d.success) {
          msg.info("下架完成");
          $("#approveModel").modal("hide");
        }
      }).always(() => {
        progress.hide();
      })
    },
  },
  mounted() {
    this.getSalesList();
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
.item-info {
  border-left: #bfbfbf solid 1px;
  border-right: #bfbfbf solid 1px;
  border-bottom: #bfbfbf solid 1px;
}

.middle {
  vertical-align: middle;
  text-align: center;
}

.modal-dialog {
  width: 100%;
}

.fix-text {
  content: "";
  margin: 2px 15px;
  border-left: 3px solid  #323332;
  height: 20px;
  display: inline-block;
  vertical-align: middle;
}
a{
  color:#333;
}
</style>