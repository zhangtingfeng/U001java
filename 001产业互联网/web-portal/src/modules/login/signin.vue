<template>
  <form action @submit.prevent="login">
    <div class="form-group has-feedback pre-feedback">
      <input type="text" class="form-control" placeholder="用户名或手机号" v-model="form.userid" />
      <img class="form-control-feedback" src="imgs/icon_yzm.png" />
    </div>
    <div class="form-group has-feedback pre-feedback">
      <input type="password" class="form-control" placeholder="密码" v-model="form.passwd" />
      <img class="form-control-feedback" src="imgs/icon_pawd.png" />
    </div>
    <div class="form-group">
      <div class="checkbox">
        <label>
          <input type="checkbox" v-model="form.remember" />记住密码
        </label>
      </div>
    </div>
    <div class="form-group">
      <button type="submit" class="form-control signup">
        <i v-if="state === 1" class="fa fa-spin fa-circle-o-notch"></i>
        登录
      </button>
    </div>
    <div class="form-group">
      <a href="#" @click="toFdPd">密码找回</a>
    </div>
  </form>
</template>

<script>
import { ajax, msg } from "@f/vendor";
export default {
  data() {
    return { state: 0, form: { userid: "", passwd: "" } };
  },
  methods: {
    async login() {
      if (this.state === 1) return;
      if (!this.form.userid) {
        msg.info("用户名不能为空！");
        return;
      }
      if (!this.form.passwd) {
        msg.info("密码不能为空！");
        return;
      }
      try {
        this.state = 1;
        let data = await ajax.put({ url: "api/session", data: this.form });
        if (!data.user) throw { code: 1 };
        else if (data.user.state == "2") {
          msg.info("该用户已被锁定，请联系平台管理员解决！");
        } else if (data.user.state == "3") {
          msg.info("该用户所在机构已被锁定，请联系平台管理员解决！");
        }
        else this.toapp();
      } catch (err) {
        msg.info(this.$t(`error.${(err && err.code) || -1}`), "error");
      } finally {
        this.state = 0;
      }
    },
    toapp() {
      window.location = "index.html";
    },
    toFdPd() {
      window.location = "findpd.html";
    }
  },
};
</script>

<style lang="less" scoped>
button.signup {
  background-color: #6abf4b;
  color: #fff;
}
hr {
  border-color: #ccc;
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

<i18n>
{
	"en": {
			"error":{
				"-1": "@:servererror",
				"captcha": "Invalid CAPTCHA",
				"1": "Invalid userid or password"
			}
	},
	"cn": {
			"error": {
				"-1": "@:servererror",
				"captcha":"验证码错误",
				"1": "用户名或者密码错误"
			}
	}
}
</i18n>
