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
            <a
              href="#"
              v-for="d of item.datas"
              :key="d.id"
              @click.prevent="selectProp(d)"
              :class="d.active ? 'active' : ''"
            >{{d.id}}</a>
          </div>
        </td>
      </tr>
    </tbody>
  </table>
</template>

<script>
import CatalogData from "@/apps/catalog";

export default {
  props: ["args"],
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
      if (this.args && this.args.p) {
        let props = {};
        for (let n of this.args.p.split(",")) props[n] = true;
        for (let p of this.lists) {
          for (let d of p.datas) {
            if (props[d.id]) d.active = true;
          }
        }
      }
      this.selectProp();
    },
    selectProp(p) {
      if (p) p.active = !p.active;
      let filters = {};
      for (let p of this.lists) {
        for (let d of p.datas) {
          if (d.active) {
            if (!filters[p.id]) filters[p.id] = [];
            filters[p.id].push(d.id);
          }
        }
      }
      this.args.filters = filters;
      this.$emit("change", filters);
    },
  },
};
</script>

<style lang="less" scoped>
@import url("@/apps/template/common.less");
.filter-box {
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

      &.active {
        background-color: @color1;
        color: @text2;
      }
    }
  }
}
</style>