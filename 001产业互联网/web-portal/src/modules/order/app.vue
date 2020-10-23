<template>
  <div>
    <app-header />
    <app-search />
    <div class="border-top" />
    <div class="app-order">
      <div class="container">
        <div class="menu">
          <app-menu />
        </div>
        <div class="seprator">&nbsp;</div>
        <div class="order-main">
          <order />
        </div>
      </div>
    </div>
    <div class="border-bottom" />
    <app-footer />
  </div>
</template>

<script>
import AppHeader from "@/apps/template/header";
import AppFooter from "@/apps/template/footer";
import AppSearch from "@/apps/template/searchBar";
import AppMenu from "@/apps/template/menu";
import Order from "./order";

export default {
  components: {
    AppHeader,
    AppSearch,
    AppMenu,
    AppFooter,
    Order,
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

.app-order {
  background-color: @bg1;
  .container {
    display: flex;
    flex-flow: row nowrap;

    .menu,
    .seprator {
      flex: 0;
    }
    .order-main {
      flex: 1;
    }

    .seprator {
      min-width: 10px;
      background-color: @bg1;
    }
    .menu,
    .order-main {
      background-color: @bg2;
    }
  }
}
</style>