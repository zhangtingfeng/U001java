<template>
  <div class="wrapper sidebar-collapse">
    <div class="content-wrapper">
      <div class="menu">
        <a
          href="#!app/login"
          class="btn btn-primary dropdown-toggle"
          data-toggle="dropdown"
          aria-haspopup="true"
          aria-expanded="false"
        >Select App</a>
        <ul class="dropdown-menu dropdown-menu-right">
          <li>
            <a href="#!app/login">登录</a>
          </li>
          <li role="separator" class="divider"></li>
          <li v-for="(c, n) of apps" v-bind:key="n">
            <a :href="`#!app/${n}`">{{n}}</a>
            <!-- <a href="#" @click.prevent="loadComp(c)">{{n}}</a> -->
          </li>
        </ul>
      </div>
      <section class="content">
        <component :is="comp" v-bind="args" />
      </section>
    </div>
  </div>
</template>

<script>
import framework from "@f/framework";
import apps from "./apps";
import login from "@/login";
import route from "./route";

export default {
  components: Object.assign({}, framework, { login }),
  data() {
    return { apps, comp: "", args: {}, route };
  },
  watch: {
    "route.comp"() {
      this.loadComp();
    },
    "route.args"() {
      this.loadComp();
    },
  },
  mounted() {
    this.loadComp();
  },
  methods: {
    loadComp() {
      this.args = route.args;
      this.comp = apps[route.comp] || route.comp;
    },
  },
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
