<template>
  <div>
    <app-header />
    <app-search />
    <div class="border-top" />
    <settlement />

    <div class="border-bottom" />
    <app-footer />
  </div>
</template>

<script>
import AppHeader from "@/apps/template/header";
import AppFooter from "@/apps/template/footer";
import AppSearch from "@/apps/template/searchBar";
import Settlement from "./settlement";

export default {
  components: {
    AppHeader,
    AppSearch,
    AppFooter,
    Settlement
  },
  data() {
    return {};
  },
  methods: {},
  mounted() { },
};
</script>

<style lang="less" scoped>
@import url("@/apps/template/common.less");
</style>