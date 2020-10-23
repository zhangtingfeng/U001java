<template>
  <div class="login-page">
    <app-header />
    <div class="app-main" :style="`background-image: url(imgs/bg.png);`">
      <div class="container">
        <div class="row">
          <div class="col-md-offset-8 col-md-4">
            <div class="panel-login">
              <div class="tabs">
                <a href="#" :class="{active: page === 1}" @click.prevent="page = 1">注册</a>
                <a href="#" :class="{active: page === 0}" @click.prevent="page = 0">登录</a>
              </div>
              <div class="panel-form">
                <app-signup v-if="page === 1" />
                <app-signin v-if="page === 0" />
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
    <app-footer />
  </div>
</template>

<script>
import AppHeader from "./header";
import AppFooter from "@/apps/template/footer";
import AppSignup from "./signup";
import AppSignin from "./signin";
import router from "./route";

export default {
  components: { AppHeader, AppFooter, AppSignup, AppSignin },
  data() {
    return {
      page: router.comp === "signup" ? 1 : 0,
    };
  },
};
</script>

<style lang="less" scoped>
.app-main {
  background-size: cover;

  .container {
    .row {
      margin-top: 70px;
      margin-bottom: 50px;
    }

    .panel-login {
      background-color: #fff;

      .tabs {
        display: flex;
        flex-flow: row nowrap;

        a {
          line-height: 50px;
          flex: 1 1 auto;
          text-align: center;
          color: #333;
          border-bottom: 1px solid #e6e6e6;

          &.active {
            color: #6abf4b;
            border-bottom: 3px solid #6abf4b;
          }
        }
      }

      .panel-form {
        padding: 30px 30px;
      }
    }
  }
}
</style>