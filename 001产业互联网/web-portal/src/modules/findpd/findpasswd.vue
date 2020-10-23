<template>
  <form action @submit.prevent="signup">
    <div class="form-group has-feedback pre-feedback">
      <div class="row">
        <div class="col-md-7">
          <input type="text" class="form-control" placeholder="手机号码" v-model="form.mobile" />
        </div>
        <img class="form-control-feedback" src="imgs/icon_tel.png" />
        <div class="col-md-5">
          <button @click.prevent="getCaptcha" class="btn" :disabled="btnDisabled">{{btnText}}</button>
        </div>
      </div>
    </div>
    <div class="form-group has-feedback pre-feedback">
      <input type="text" class="form-control" v-model="form.captcha" placeholder="验证码" />
      <img class="form-control-feedback" src="imgs/icon_yzm.png" />
    </div>
    <div class="form-group has-feedback pre-feedback">
      <input type="password" class="form-control" placeholder="输入新密码" v-model="form.passwd1" />
      <img class="form-control-feedback" src="imgs/icon_pawd.png" />
    </div>
    <div class="form-group has-feedback pre-feedback">
      <input type="password" class="form-control" placeholder="新密码确认" v-model="form.passwd2" />
      <img class="form-control-feedback" src="imgs/icon_pawd.png" />
    </div>
    <div class="form-group">
      <button type="submit" class="form-control signup">
        <i v-if="state === 1" class="fa fa-spin fa-circle-o-notch"></i>
        修改密码
      </button>
    </div>
  </form>
</template>

<script>
import { ajax, msg } from "@f/vendor";
export default {
  data() {
    return {
      state: 0,
      btnText: "获取验证码",
      btnDisabled: false,
      form: { mobile: "", userid: "", captcha: "", passwd1: "", passwd2: "" },
    };
  },
  methods: {
    async signup() {
      if (this.state === 1) return;
      if (!(/^1(3|4|5|6|7|8|9)\d{9}$/.test(this.form.mobile))) {
        msg.info("请正确填写手机号码！");
        return;
      }
      if (!(this.form.passwd1 && this.form.passwd2)) {
        msg.info("请填写密码信息！");
        return;
      }
      if (this.form.passwd1 != this.form.passwd2) {
        msg.info("两次密码输入不相同！");
        this.form.passwd1 = this.form.passwd2 = "";
        return;
      }
      try {
        this.state = 1;
        let d = await ajax.post({
          url: "api/platform/register/findPasswd",
          data: this.form,
        });
        msg.info("找回密码成功，请登录");
        window.location.href = "login.html";
      } catch (err) {
        if (err.code) {
          msg.info(err.msg);
        } else msg.info("访问服务器错误", "error");
      } finally {
        this.state = 0;
      }
    },
    getCaptcha() {
      if (!(/^1(3|4|5|6|7|8|9)\d{9}$/.test(this.form.mobile))) {
        msg.info("请正确填写手机号码！");
        return false;
      }

      try {
        let d = ajax.post({
          url: "api/platform/register/captcha",
          data: this.form,
        });

        let count = 60;
        const countDown = setInterval(() => {
          if (count == 0) {
            this.btnText = "重新发送";
            this.btnDisabled = false;
            clearInterval(countDown);
          } else {
            this.btnDisabled = true;
            this.btnText = count + '秒后可重新获取';
          }
          count--;
        }, 1000);
      } catch (err) {
        console.error(err);
        msg.info(err.msg);
      } finally {
      }
    }
  },
};
</script>

<style lang="less" scoped>
button.signup {
  background-color: #6abf4b;
  color: #fff;
}

.pre-feedback  {
  input  {
    padding-left: 30px;
    padding-right: 0;
  }
  .form-control-feedback  {
    left: 7px;
    top: 7px;
    right: inherit;
    width: 16px;
    height: 16px;
  }
}

form {
  height: 256px;
}
</style>