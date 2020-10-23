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
    <!-- <div class="form-group has-feedback pre-feedback">
      <input type="text" class="form-control" placeholder="公司名称" v-model="form.company" />
      <img class="form-control-feedback" src="imgs/icon_company.png" />
    </div> -->
    <div class="form-group has-feedback pre-feedback">
      <input type="text" class="form-control" placeholder="用户名" v-model="form.userid" />
      <img class="form-control-feedback" src="imgs/icon_username.png" />
    </div>
    <div class="form-group has-feedback pre-feedback">
      <input type="password" class="form-control" placeholder="密码" v-model="form.passwd" />
      <img class="form-control-feedback" src="imgs/icon_pawd.png" />
    </div>
    <div class="form-group">
      <div class="checkbox">
        <label>
          <input type="checkbox" v-model="form.agreement" />
          我已同意并阅读
          <a href="html/user-service-agreement.html" target="_blank">《汇农交易平台用户服务协议》</a>
        </label>
      </div>
    </div>
    <div class="form-group">
      <button type="submit" class="form-control signup">
        <i v-if="state === 1" class="fa fa-spin fa-circle-o-notch"></i>
        免费注册
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
      form: { mobile: "", userid: "", captcha: "", passwd: "", agreement: true },
    };
  },
  methods: {
    async signup() {
      if (this.state === 1) return;
      if (!(/^1(3|4|5|6|7|8|9)\d{9}$/.test(this.form.mobile))) {
        msg.info("请正确填写手机号码！");
        return;
      }
      if (!this.form.captcha || this.form.captcha.length != 6) {
        msg.info("请正确填写验证码！");
        return;
      }
      if (!this.form.userid) {
        msg.info("用户名不能为空！");
        return;
      }
      if (this.form.userid.length > 20) {
        msg.info("用户名长度不能大于20位！");
        return;
      }
      if (!this.form.userid) {
        msg.info("用户名不能为空！");
        return;
      }
      if (this.form.userid.length > 20) {
        msg.info("用户名长度不能大于20位！");
        return;
      }
      if (!this.form.passwd) {
        msg.info("密码不能为空！");
        return;
      }
      if (this.form.passwd.length > 20) {
        msg.info("密码长度不能大于20位！");
        return;
      }
      if (!this.form.agreement) {
        msg.info("请同意并阅读《汇农交易平台用户服务协议》");
        return;
      }
      try {
        this.state = 1;
        let d = await ajax.post({
          url: "api/platform/register/registerUser",
          data: this.form,
        });
        msg.info("注册成功，请登录");
        window.location.href = "login.html";
      } catch (err) {
        console.error(err);
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
