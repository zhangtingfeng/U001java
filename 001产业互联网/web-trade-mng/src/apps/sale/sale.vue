<template>
  <div class="box">
    <div class="box-body">
      <div class="form-meta">
        <div class="row">
          <div class="col-md-12 form-group-title">
            <h4 class="bg-gray">商品信息</h4>
          </div>
        </div>
        <div class="row">
          <div class="col-md-12">
            <label>商品名称</label>
            <treeselect :options="goodsclass" v-model="goodsId" :multiple="false" :default-expand-level="2" placeholder="请选择商品名称"></treeselect>
            <div class="box-body" v-if="data2.state==2">
              <!--商品属性配置-->
              <meta-form2 :data="data2">
              </meta-form2>
            </div>
          </div>
        </div>
        <div class="row">
          <div class="col-md-6">
            <upload :id="id1" v-model="images1" :title="title1"></upload>
          </div>
          <div class="col-md-6">
            <upload :id="id2" v-model="images2" :title="title2"></upload>
          </div>
        </div>
      </div>
    </div>

    <div class="box-body" v-if="data.state==2">
      <meta-form1 :data="data">
      </meta-form1>
    </div>
    <div class="btn-panel">
      <button href="#" class="btn btn-primary" style="margin: 0 2% 0 15%;" @click.prevent="release">
        <i></i>发布
      </button>
      <button type="button" class="btn btn-info" data-toggle="modal" @click.prevent="view">
        <i></i>预览
      </button>
      <button href="#" class="btn btn-default" style="margin: 0 2%;" @click.prevent="cancel">
        <i></i>取消
      </button>
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

  </div>
</template>

<script>
import { comps, meta, i18n, dict, editors } from "@f/framework";
import { progress, msg, ajax } from "@f/vendor";
import Treeselect from "@riophae/vue-treeselect";
import "@riophae/vue-treeselect/dist/vue-treeselect.css";
import { load, msgTrans } from "./sale/goodsProps";
import MetaForm1 from "./sale/Form1";// 交易信息
import MetaForm2 from "./sale/Form2";// 商品信息
import Upload from "./sale/Upload"; // 照片上传
import Preview from "./preview";

// 商品分类树形结构处理
function parseTree(datas) {
  if (!datas) datas = [];

  let objs = {};
  let trees = [];
  for (let t of datas) {
    let d = { id: t.id, pid: t.pid, label: t.text };

    objs[d.id] = d;
    if (d.pid && objs[d.pid]) {
      if (!objs[d.pid].children) objs[d.pid].children = [];
      objs[d.pid].children.push(d);
    } else {
      trees.push(d);
    }
  }

  return trees;
}

export default {
  components: { MetaForm1, MetaForm2, Treeselect, Upload, Preview },
  data() {
    return {
      name: "trade/listing-sale",
      id1: "input-img1",
      id2: "input-img2",
      title1: "轮播图片 （请选择三张图片上传）",
      title2: "详情图片 （请至少选择一张图片上传）",
      images1: [],
      images2: [],
      meta1: {},
      meta2: {}, // 自由加载对象
      goodsclass: [],// 商品分类树
      classes: [],
      goodsId: null,
      data: { state: 0 },
      data2: { state: 0 },// 自由加载对象
      viewGoods: {//预览对象
        imgs1: [],
        imgs2: [],
        props: [],
      },
    };
  },
  watch: {
    async goodsId(value, oldValue) {
      // 加载属性配置页面
      this.meta2 = await load(this.name, { goodsId: value });
      this.data2 = meta.init(this.meta2, {
        meta: this.meta2,
        state: 2,
        defaultRow: {},
        form: {}
      });
      let msgs = msgTrans(this.name);
      i18n.mergeLocaleMessage("en", msgs.en);
      i18n.mergeLocaleMessage("cn", msgs.cn);
    }
  },
  methods: {
    async init() {
      // 加载交易信息固定页面
      this.meta1 = await meta.get(this.name);
      this.data = meta.init(this.meta1, {
        meta: this.meta1,
        state: 2,
        defaultRow: {},
        form: $.extend({}, {}, {})
      });
      for (let i in this.data.meta.fields) {
        let f = this.data.meta.fields[i];
        if (f.name == "deliveryArea") {
          f.lineBreaks = true;
        }
      }
      let msgs = meta.msgs(this.name);
      i18n.mergeLocaleMessage("en", msgs.en);
      i18n.mergeLocaleMessage("cn", msgs.cn);
      dict.get("goods-tree").then(data => {
        this.classes = data;
        this.goodsclass = parseTree(data);
      });
    },
    async release() {
      if (!this.goodsId) {
        msg.info("请选择商品！");
        return false;
      }
      var rs1 = await this.$children[4].$validator.validateAll();
      if (!rs1) {
        msg.info("请完善商品属性信息！");
        return false;
      }
      if (this.images1.length != 3) {
        msg.info("轮播图片（仅允许上传三张图片）！");
        return false;
      }
      if (this.images2.length == 0) {
        msg.info("详情图片 （请至少选择一张图片上传）！");
        return false;
      }
      var rs2 = await this.$children[3].$validator.validateAll();
      if (!rs2) {
        msg.info("请检查并修改资源信息！");
        return false;
      } else {
        var images1 = [];
        for (var img of this.images1) {
          images1.push(img.id);
        }
        var images2 = [];
        for (var img of this.images2) {
          images2.push(img.id);
        }
        var _this = this;
        var postData = {
          goodsId: this.goodsId,
          images1: images1,
          images2: images2,
          meta: this.data.form,
          meta2: this.data2.form
        };

        progress.show();

        let promise = ajax.post({
          url: "api/trade/listing-sale/release",
          data: postData
        });

        promise
          .then(data => {
            msg.info("发布成功，待审核，请留意汇农苹果服务号通知");
            location.reload();
          })
          .catch(rs => {
            msg.info("发布失败");
          })
          .always(() => {
            progress.hide();
          });
      }
    },
    view() {
      var goods = {};
      goods.title = this.data.form["title"];
      goods.unitPrice = this.data.form["unitPrice"];
      goods.minQty = this.data.form["minQty"];
      goods.goodsName = this.data.form["goodsName"];
      goods.totalQty = this.data.form["totalQty"];
      goods.unit1 = this.data.form["unit1"];
      goods.unit2 = this.data.form["unit2"];
      // 仓库处理
      goods.whId = this.data.form["whId"];
      // 图片处理
      var imgs1 = [];
      for (var img of this.images1) {
        imgs1.push("api/trade/upload/" + img.id);
      }
      var imgs2 = [];
      for (var img of this.images2) {
        imgs2.push("api/trade/upload/" + img.id);
      }
      goods.imgs1 = imgs1;
      goods.imgs2 = imgs2;
      // 商品分类
      for (var c of this.classes) {
        if (c.id == this.goodsId) {
          goods.goodsName = c.text;
        }
      }
      // 属性处理
      if (this.data2.meta) {
        var fields = this.data2.meta.fields;
        var form = this.data2.form;
        var props = [];
        for (var f of fields) {
          for (let m in form) {
            if (f.name == m) {
              props.push({ propName: f.title, propValue: form[m] });
            }
          }
        }

      }
      goods.props = props;
      this.viewGoods = goods;
      $("#myModal").modal();
    },
    cancel() {
      location.reload();
    }
  },
  mounted() {
    this.init();
  }
};
</script>

<style scoped>
.title {
  float: left;
}
.history {
  float: right;
  font-size: 20px;
  margin: 18px 50px 0 0;
}
.modal-dialog {
  width: 100%;
}
</style>