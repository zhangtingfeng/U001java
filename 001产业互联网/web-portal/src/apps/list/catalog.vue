<template>
  <div class="tabs">
    <a href="#!AppList?t=0" :class="'0' === active || !active ? 'active' : ''" @click="selectAll()">全部</a>
    <a v-for="item of lists" :key="item.id" :href="`#!AppList?c=${item.id}`" :class="item.id === active ? 'active' : ''" @click="select(item)" :style="item.degree===1 ?'font-weight:bold;font-size:14px;':''">{{item.name}}</a>
  </div>
</template>

<script>
import CatalogData from "@/apps/catalog";

export default {
  props: ["args"],
  data() {
    return {
      active: this.args && this.args.c ? this.args.c : "0",
      lists: [{ id: "0", name: "全部" }],
    };
  },
  mounted() {
    this.load();
  },
  methods: {
    async load() {
      this.lists = await CatalogData.loadCatalogs();
    },
    select(item) {
      this.active = item.id;
      this.$emit("change", this.active);
    },
    selectAll() {
      this.active = '0';
      this.$emit("change", this.active);
    }
  },
};
</script>

<style lang="less" scoped>
@import url("@/apps/template/common.less");
</style>