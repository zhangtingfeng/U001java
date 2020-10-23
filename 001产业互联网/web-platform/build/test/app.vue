<template>
  <div class="wrapper sidebar-collapse">
    <div class="content-wrapper">
      <div class="menu">
        <a
          href="#"
          class="btn btn-info dropdown-toggle"
          data-toggle="dropdown"
          aria-haspopup="true"
          aria-expanded="false"
        >Select App</a>
        <ul class="dropdown-menu dropdown-menu-right">
          <li>
            <a href="#" @click.prevent="login">登录</a>
          </li>
          <li role="separator" class="divider"></li>
          <li v-for="(c, n) of apps" v-bind:key="n">
            <a href="#" @click.prevent="loadComp(c)">{{n}}</a>
          </li>
        </ul>
      </div>
      <section class="content">
        <component :is="comp" />
      </section>
    </div>
  </div>
</template>

<script>
import apps from "./apps";
import login from "@/login";

export default {
  data() {
    return { apps, comp: "" };
  },
  methods: {
    login() {
      this.comp = login;
    },
    loadComp(c) {
      this.comp = c;
    }
  }
};
</script>

<style lang="less" scoped>
div.menu {
  position: absolute;
  right: 10px;
  top: 10px;
  z-index: 10000;
}
</style>
