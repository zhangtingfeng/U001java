<template>
  <div>
    <app-header />
    <app-search />
    <div class="border-top" />
    <cart />

    <div class="border-bottom" />
    <app-footer />
  </div>
</template>

<script>
import AppHeader from "@/apps/template/header";
import AppFooter from "@/apps/template/footer";
import AppSearch from "@/apps/template/searchBar";
import Cart from "./cart";

export default {
  components: {
    AppHeader,
    AppSearch,
    AppFooter,
    Cart,
  },
  data() {
    return {};
  },
  methods: {},
  mounted() {},
};
</script>

<style lang="less" scoped>
@import url("@/apps/template/common.less");
</style>