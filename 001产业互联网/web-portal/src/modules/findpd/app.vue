<template>
  <div class="login-page">
    <app-header />
    <div class="app-main" :style="`background-image: url(imgs/bg.png);`">
      <div class="container">
        <div class="row">
          <div class="col-md-offset-8 col-md-4">
            <div class="panel-login">
              <div class="tabs">
                <a href="#" :class="{active: 1 === 1}">找回密码</a>
              </div>
              <div class="panel-form">
                <app-findpasswd />
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
import AppHeader from "../login/header";
import AppFooter from "@/apps/template/footer";
import AppFindpasswd from "./findpasswd";

export default {
  components: { AppHeader, AppFooter, AppFindpasswd },
  data() {
    return {
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