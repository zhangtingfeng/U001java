<template>
  <section class="content">
    <transition
      v-on:before-enter="loadStart"
      v-on:before-leave="loadStart"
      v-on:after-enter="loadStop"
      v-on:after-leave="loadStop"
    >
      <component v-if="comp" :is="comp" v-bind="args" v-bind:key="compName" />
    </transition>
  </section>
</template>

<script>
import menu from "../data/menu";
import Framework from "@f/framework";
import Apps from "@m/common/apps";
import NoAuths from "./NoAuths";
import "../data/route";

let INDEX = 0;

export default {
  data() {
    return {
      menu: menu.data,
      compName: "",
      comp: "",
      args: {},
    };
  },
  methods: {
    // loadMenu() {
    //   let m = menu.get(this.menu.curr);
    //   if (m) {
    //     if (m.data && m.data.args) {
    //       this.menu.component = m.data.args;
    //     }
    //   } else if (this.menu.curr) {
    //     this.menu.component = NoAuths;
    //   }
    // },
    load() {
      // let comp = /[^\(]+/.exec(this.menu.component)[0];
      // comp = comp.substr(0, 1).toUpperCase() + comp.substr(1);
      // let tmp = /\((.*)\)/.exec(this.menu.component);
      // let args = tmp ? JSON.parse(tmp[1]) : {};
      // this.compName = INDEX++;
      // this.comp = this.getApps(comp);
      // this.args = args;

      // let m = /^([^\?]+)\??(.*)$/.exec(this.menu.component);
      // if (m === null) return;
      // let comp = m[1];
      // let args = {};
      // if (m[2]) {
      //   for (let t of m[2].split("&")) {
      //     let t1 = t.split("=");
      //     if (t1[0]) args[t1[0]] = t1[1];
      //   }
      // }

      // this.args = args;
      // this.comp = this.getApps(comp);

      this.comp = this.getApps(this.menu.component);
      this.args = this.menu.args;
    },
    getApps(comp) {
      return typeof comp === "string"
        ? Framework[comp] ||
            Apps[comp] || { template: "<h3>Not implements!</h3>" }
        : comp;
    },
    loadStart(el) {
      $(el).css({ position: "absolute", top: "15px" });
    },
    loadStop(el) {
      $(el).css({ position: "inherit", top: "" });
    },
  },
  watch: {
    // "menu.curr"(val) {
    //   if (val) this.loadMenu();
    // },
    "menu.component"(val) {
      if (val) this.load();
    },
    "menu.args"() {
      this.load();
    },
  },
  mounted() {
    // if (this.menu.component) {
    this.load();
    // } else {
    //   this.loadMenu();
    // }
  },
};
</script>
<style lang="less" scoped>
section.content {
  position: relative;
}
@keyframes fadeIn {
  from {
    transform: translateX(100%);
  }
  to {
    transform: translateX(0);
  }
}
@keyframes fadeOut {
  from {
    transform: translateX(0);
  }
  to {
    transform: translateX(-100%);
  }
}

.v-enter-active {
  animation-name: fadeIn;
  animation-duration: 0.5s;
}
.v-leave-active {
  animation-name: fadeOut;
  animation-duration: 0.5s;
}
</style>
<i18n-yaml>
en:
  empty: "Empty"
cn:
  empty: "没有定义菜单内容或者菜单功能还没有实现"
</i18n-yaml>