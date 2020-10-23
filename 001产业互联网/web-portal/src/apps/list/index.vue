<template>
  <div class="portal">
    <app-header />
    <app-search :args="args" />
    <app-main :args="args" />
    <app-footer />
  </div>
</template>

<script>
import AppHeader from "../template/header";
import AppFooter from "../template/footer";
import AppSearch from "../template/searchBar";
import AppMain from "./main";

export default {
  components: { AppHeader, AppSearch, AppMain, AppFooter },
  props: ["args"],
  data() {
    return {};
  },
};
</script>