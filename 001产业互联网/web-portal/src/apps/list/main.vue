<template>
  <div class="list-main">
    <div class="border-top"></div>
    <div class="container">
      <catalog :args="args" @change="changeCatalog" />
    </div>
    <div class="container">
      <filter-box :args="args" @change="changeFilter" />
    </div>
    <div class="container">
      <list-box :lists="lists" />
    </div>
    <div class="border-bottom"></div>
  </div>
</template>

<script>
import catalog from "./catalog";
import FilterBox from "./filter";
import ListBox from "./list";
import { progress, msg, ajax } from "@f/vendor";

export default {
  components: { catalog, FilterBox, ListBox },
  props: ["args"],
  data() {
    return {
      catalog: this.args.c,
      search: this.args.q || "",
      filter: {},
      lists: []
    };
  },
  watch: {
    "args.q"() {
      this.loadList(this.catalog, this.filter, this.args.q);
    },
  },
  methods: {
    changeCatalog(val) {
      this.catalog = val;
      this.loadList(this.catalog, this.filter, this.args.q);
    },
    changeFilter(val) {
      this.filter = val;
      this.loadList(this.catalog, this.filter, this.args.q);
    },
    async loadList(catalog, filter, search) {
      try {
        let data = await ajax.post({
          url: "api/portal/sale/getSalesList",
          data: { catalog: catalog, filter: filter, search: search }
        });

        for (var g of data.goods) {
          var props = [];
          for (var p of data.props) {
            if (g.id == p.salesId) {
              props.push(p);
            }
          }
          g.props = props;
          for (var i of data.imgs) {
            if (g.id == i.salesId) {
              g.img = "api/trade/upload/" + i.imgPath;
            }
          }
          for (var o of data.orgs) {
            if (g.orgId == o.id) {
              g.org = o;
            }
          }
        }

        this.lists = data.goods;
      } catch (error) {
        msg.info("获取商品信息出错" + error);
      }
    },
  },
};
</script>

<style lang="less" scoped>
@import url("@/apps/template/common.less");

.container {
  margin-bottom: 20px;
}
</style>