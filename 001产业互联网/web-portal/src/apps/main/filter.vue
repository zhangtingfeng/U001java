<template>
  <table class="table table-bordered filter-box">
    <colgroup>
      <col width="134px" />
      <col />
    </colgroup>
    <tbody>
      <tr v-for="item of lists" :key="item.id">
        <th>{{item.name}}</th>
        <td>
          <div class="filter-items">
            <a href="#" v-for="d of item.datas" :key="d.id" @click.prevent="goto(d.id)">{{d.id}}</a>
          </div>
        </td>
      </tr>
    </tbody>
  </table>
</template>

<script>
import CatalogData from "@/apps/catalog";

export default {
  props: ["catalog"],
  data() {
    return {
      lists: [{ id: "0", name: "全部", datas: [""] }],
    };
  },
  mounted() {
    this.load();
  },
  methods: {
    async load() {
      this.lists = await CatalogData.loadProps();
    },
    goto(id) {
      location = `index.html#!AppList?c=${
        this.catalog || "0"
      }&p=${encodeURIComponent(id)}`;
    },
  },
};
</script>

<style lang="less" scoped>
@import url("@/apps/template/common.less");
.filter-box {
  background-color: @bg2;
  th {
    background-color: #eee;
    padding-left: 20px;
    vertical-align: middle;
  }
  td,
  th {
    border-color: #e6e6e6 !important;
  }

  .filter-items {
    display: flex;
    flex-flow: row wrap;

    a {
      display: block;
      padding: 3px 10px;
      margin: auto 10px;
    }
  }
}
</style>