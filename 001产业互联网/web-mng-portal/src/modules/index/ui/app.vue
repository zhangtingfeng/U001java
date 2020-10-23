<template>
  <div :class="error ? '' : 'wrapper'">
    <div class="init-loading" v-if="!loaded && !error">
      <p>
        <i class="fa fa-circle-o-notch fa-spin"></i>
      </p>
    </div>
    <app-header v-if="loaded" />
    <app-sidemnue v-if="loaded" />
    <app-main v-if="loaded" />
    <app-footer v-if="loaded" />
    <chg-pwd v-if="loaded" id="modal-chg-pwd" />
    <app-500 v-if="error" :error="error" />
  </div>
</template>

<script>
import msg from "@c/msg";
import AppHeader from "./header";
import AppSidemnue from "./SideMenu";
import AppMain from "./main";
import AppFooter from "./footer";
import App500 from "./500";
import ChgPwd from "./ChgPwd";
import user from "@m/common/user";
import menu from "../data/menu";
import route from "../data/route";

export default {
  data() {
    return { loaded: false, error: false };
  },
  components: {
    AppHeader,
    AppSidemnue,
    AppMain,
    AppFooter,
    ChgPwd,
    App500,
  },
  methods: {
    async init() {
      try {
        await user.init();
        if (user.user.userid) {
          this.loaded = true;
          menu.load();
          route();
          setTimeout(() => {
            $(window).resize();
          }, 100);
          $("body").addClass("sidebar-mini");
        } else {
          location = `login.html?url=${encodeURIComponent(location)}`; // 系统登录页
        }
      } catch (err) {
        console.error(err);
        this.error = JSON.stringify(err, undefined, 2);
        msg.info("servererror", "error");
      }
    },
  },
  mounted() {
    this.init();
  },
};
</script>

<style lang="less" scoped>
.init-loading {
  position: fixed;
  left: 0;
  right: 0;
  top: 0;
  bottom: 0;
  text-align: center;
  font-size: 300%;
  color: #f39c12;

  p {
    position: relative;
    top: 20%;
  }
}
</style>