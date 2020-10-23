<template>
  <div class="portal">
    <app-header />
    <app-search />
    <div class="border-top"></div>
    <info-main :id="id" />
    <div class="border-bottom"></div>
    <app-footer />
  </div>
</template>

<script>
import AppHeader from "../template/header";
import AppSearch from "../template/searchBar";
import AppFooter from "../template/footer";
import InfoMain from "./main";

export default {
  components: { AppHeader, AppSearch, InfoMain, AppFooter },
  props: ["args"],
  data() {
    return {
      id: this.args.id
    };
  },
};
</script>

<style lang="less" scoped>
@import url("@/apps/template/common.less");
</style>