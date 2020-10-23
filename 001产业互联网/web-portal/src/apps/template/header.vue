<template>
  <nav class="navbar navbar-default">
    <div class="container">
      <template v-if="!user.user.userid">
        <ul class="nav navbar-nav">
          <li>
            <a href="index.html#!AppMain" class="first">首页</a>
          </li>
          <li>
            <a href="login.html">登录</a>
          </li>
          <li>
            <a href="login.html#!signup">注册</a>
          </li>
        </ul>
        <ul class="nav navbar-nav navbar-right">
          <li>
            <a href="login.html" class="last">购物车</a>
          </li>
          <li>
            <a href="login.html" class="last">我的订单</a>
          </li>
          <li>
            <a href="login.html" class="last"> <i class="fa fa-user"></i>&nbsp;我的中心 </a>
          </li>
        </ul>
      </template>
      <template v-else>
        <ul class="nav navbar-nav">
          <li>
            <a href="index.html#!AppMain" class="first">首页</a>
          </li>
        </ul>
        <ul class="nav navbar-nav navbar-right">
          <li>
            <a href="#">欢迎{{ user.user.name}}<a v-if="!user.user.org" style="color:red;" href="/platform/index.html#!menu/1001">(需进行企业认证)</a></a>
          </li>

          <li>
            <a href="#" class="last" @click.prevent="im = !im"><i class="fa fa-commenting"></i>&nbsp;消息</a>
          </li>
          <li>
            <a href="cart.html">购物车<span v-if="cart.cartNums > 0" style="color:red;">({{ cart.cartNums }})</span></a>
          </li>
          <li>
            <a href="/platform/index.html#!menu/3001">我的订单</a>
          </li>
          <li>
            <a href="/platform/index.html"> <i class="fa fa-user"></i>&nbsp;我的中心 </a>
          </li>
          <li>
            <a href="#" @click.prevent="user.logout">注销</a>
          </li>
        </ul>
        <div class="im-wnd box box-solid" v-if="im">
          <div class="box-header bg-gray bg-solid">
            <h3 class="box-title">对话列表</h3>
            <div class="box-tools pull-right">
              <button class="btn btn-box-tool" @click.prevent="im = false">
                <i class="fa fa-times" />
              </button>
            </div>
          </div>
          <div class="box-body">
            <module-loader :app="['im', 'list']" />
          </div>
        </div>
      </template>
    </div>
  </nav>
</template>

<script>
import user from "@/apps/user"
import cart from "@/apps/cart"

import framework from "@f/framework"

export default {
  components: { ModuleLoader: framework.ModuleLoader },
  data() {
    console.log(!user.user.userid)
    return {
      im: false,
      user,
      cart,
    }
  },
  methods: {},
  mounted() {
    cart.init()
  },
}
</script>

<style lang="less" scoped>
@import url("./common.less");

.navbar {
  min-height: auto;
  margin-bottom: 0;
  border: none;
  background-color: @bg1;

  .nav {
    li {
      a {
        padding-top: 6px;
        padding-bottom: 6px;
        color: @text3;

        &.first {
          padding-left: 0;
        }
        &.last {
          padding-right: 0;
        }
      }
    }
  }

  .im-wnd {
    position: fixed;
    bottom: 10px;
    right: 10px;
    width: 400px;
    height: 500px;
    background-color: #fff;
    z-index: 1000;
  }
}
</style>
