<template>
  <div class="login-box">
    <div class="login-box-body">
      <p class="login-box-msg">{{msg}}</p>
      <form action method="post" @submit.prevent="login">
        <div class="form-group has-feedback" :class="{'has-error': errors.has('userid')}">
          <label>
            {{$t('userid')}}
            <span v-if="errors.has('userid')">({{errors.first('userid')}})</span>
          </label>
          <input type="text" name="userid" class="form-control" v-focus v-model.trim="form.userid" :placeholder="$t('userid')" v-validate="'required'" />
          <span class="fa fa-user form-control-feedback"></span>
        </div>
        <div class="form-group has-feedback">
          <label>{{$t('passwd')}}</label>
          <input type="password" name="passwd" class="form-control" v-model="form.passwd" :placeholder="$t('passwd')" />
          <span class="fa fa-lock form-control-feedback"></span>
        </div>
        <!-- <div class="form-group">
					<label>{{$t('captcha')}}</label>
					<div class="input-group captcha-img">
						<input type="text" class="form-control" v-model="form.captcha" @focus.once="loadCaptcha">
						<span class="input-group-addon">
							<img :src="captchaImg" @click="loadCaptcha" v-if="captchaImg"/>
						</span>
					</div>
        </div>-->
        <div class="row">
          <div class="col-xs-8">
            <checkbox name="remember" :label="$t('remember')" v-model="form.remember" />
          </div>
          <div class="col-xs-4">
            <button type="submit" class="btn btn-primary btn-block btn-flat">
              <i v-if="loging" class="fa fa-spin fa-circle-o-notch"></i>
              {{$t('signin')}}
            </button>
          </div>
        </div>
      </form>

      <div class="social-auth-links text-center">
        <hr />
      </div>
      <a href="#">{{$t('forget')}}</a>
    </div>
    <locale class="btn btn-link locale" />
  </div>
</template>

<script>
import { user, comps } from "@f/framework";
import { msg, ajax } from "@f/vendor";

export default {
  data() {
    return {
      loging: false,
      captchaImg: "",
      msg: "",
      form: { userid: "", passwd: "", captcha: "", remember: false }
    };
  },
  components: {
    locale: comps.ChgLocale,
    Checkbox: comps.Checkbox
  },
  methods: {
    // loadCaptcha() {
    //   this.captchaImg = "api/session/captcha.png?" + new Date().getTime();
    // },
    async login() {
      if (this.loging) {
        return;
      }
      await this.$validator.validateAll();
      if (this.errors.all().length > 0) {
        msg.alarm("error");
        return;
      }
      try {
        this.loging = true;
        let data = await ajax.put({ url: "api/session", data: this.form });
        console.log(data);
        if (!data.user) throw { code: 1 };
        else if (data.user.state == "2") {
          msg.info("该用户已被锁定，请联系平台管理员解决！");
        } else if (data.user.state == "3") {
          msg.info("该用户所在机构已被锁定，请联系平台管理员解决！");
        }
        else this.toapp(data.user);
      } catch (err) {
        console.error(err);
        msg.info(this.$t(`error.${(err && err.code) || -1}`), "error");
      } finally {
        this.loging = false;
      }
    },
    toapp(u) {
      user.set(u);
      this.msg = `${u.name} 已经登录`;
    }
  },
  async created() {
    try {
      let data = await ajax.get("api/session");
      if (data.user) {
        this.toapp(data.user);
      }
      if (data.userid) {
        this.form.userid = resp.data.userid;
      }
    } catch { }
  },
  mounted() {
    $("body").addClass("hold-transition login-page");
  },
  destroyed() {
    $("body").removeClass("hold-transition login-page");
  }
};
</script>

<style lang="less" scoped>
.locale {
  position: absolute;
  top: 10px;
  right: 10px;
}
/*
.captcha-img .input-group-addon {
  padding: 0;
}
.captcha-img .input-group-addon img {
  height: 32px;
  cursor: pointer;
}
*/
.newtitle {
  color: #1179bf;
  font-size: 30px;
}
</style>
<i18n>
{
	"en": {
			"title": "Signin for @:app.name",
			"userid": "User ID",
			"passwd": "Password",
			"captcha": "CAPTCHA",
			"remember": "Remember me?",
			"signin": "Signin",
			"forget": "Forget password?",
			"error":{
				"-1": "@:servererror",
				"captcha": "Invalid CAPTCHA",
				"1": "Invalid userid or password"
			}
	},
	"cn": {
			"title": "登录@:app.name",
			"userid": "用户ID",
			"passwd": "密码",
			"captcha": "验证码",
			"remember": "记住我?",
			"signin": "登录",
			"forget": "忘记密码?",
			"error": {
				"-1": "@:servererror",
				"captcha":"验证码错误",
				"1": "用户名或者密码错误"
			}
	}
}
</i18n>
