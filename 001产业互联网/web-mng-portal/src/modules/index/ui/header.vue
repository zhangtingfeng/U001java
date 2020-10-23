<template>
  <header class="main-header">
    <a href="#" class="logo" @click.prevent>
      <span class="logo-mini">{{$t("app.name2")}}</span>
      <span class="logo-lg">{{$t("app.name")}}</span>
    </a>

    <nav class="navbar navbar-static-top">
      <a href="#" class="sidebar-toggle" data-toggle="push-menu" role="button">
        <span class="sr-only">Toggle navigation</span>
      </a>

      <div class="navbar-custom-menu">
        <ul class="nav navbar-nav">
          <li>
            <!--
						<chg-locale/>
            -->
          </li>

          <li class="dropdown">
            <a href="#" class="dropdown-toggle" data-toggle="dropdown">
              <i class="fa fa-user-o"></i>
              <span class="hidden-xs">{{user.name}}</span>
              <span class="caret"></span>
            </a>
            <ul class="dropdown-menu dropdown-menu-right" role="menu">
              <li>
                <a href="#" data-toggle="modal" data-target="#modal-chg-pwd">
                  <i class="fa fa-key text-info"></i>
                  {{$t('chgpwd')}}
                </a>
              </li>
              <li class="divider"></li>
              <li>
                <a href="#" @click.prevent="signout">
                  <i class="fa fa-sign-out text-info"></i>
                  {{$t('signout')}}
                </a>
              </li>
            </ul>
          </li>
        </ul>
      </div>
    </nav>
  </header>
</template>

<script>
import ChgLocale from "@c/ui/ChgLocale";
import msg from "@c/msg";
import ajax from "@c/ajax";
import progress from "@c/progress";
import user from "@m/common/user";
import menu from "../data/menu";

export default {
  data() {
    return { user: { name: "" } };
  },
  methods: {
    signout() {
      progress.show();
      ajax.delete("api/session").always(() => {
        location = "/portal/login.html";
        progress.hide();
      });
    },
  },
  mounted() {
    this.user.name = user.user.name;
  },
  components: {
    ChgLocale,
  },
};
</script>

<style lang="less" scoped>
.logo {
  img {
    height: 24px !important;
    width: 24px !important;
    padding: 0 !important;
    position: relative;
    top: -3px;
  }
}
</style>

<i18n-yaml>
en:
  chgpwd: "Change password"
  signout: "Sign out"
cn:
  chgpwd: "修改密码"
  signout: "退出"
</i18n-yaml>
