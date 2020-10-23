<template>
  <div class="modal fade" style="display: none;">
    <div class="modal-dialog">
      <form action method="post" @submit.prevent="chgpwd()">
        <div class="modal-content">
          <div class="modal-header bg-solid bg-primary">
            <button type="button" class="close" data-dismiss="modal" aria-label="Close">
              <i class="fa fa-times"></i>
            </button>
            <h4 class="modal-title">
              <i class="fa fa-key">&nbsp;</i>
              {{$t('title')}}
            </h4>
          </div>
          <div class="modal-body">
            <div class="form-group" :class="{'has-error': errors.has('pwd1')}">
              <label>
                {{$t('pwd1')}}
                <span v-if="errors.has('pwd1')">({{errors.first('pwd1')}})</span>
              </label>
              <input type="password" name="pwd1" class="form-control" autocomplete="false" v-focus v-model.trim="form.pwd1" :placeholder="$t('pwd1')" v-validate="'required'" />
            </div>
            <div class="form-group" :class="{'has-error': errors.has('pwd2')}">
              <label>
                {{$t('pwd2')}}
                <span v-if="errors.has('pwd2')">({{errors.first('pwd2')}})</span>
              </label>
              <input type="password" name="pwd2" class="form-control" autocomplete="false" v-model.trim="form.pwd2" :placeholder="$t('pwd2')" v-validate="'required'" ref="pwd2" />
            </div>
            <div class="form-group" :class="{'has-error': errors.has('pwd3')}">
              <label>
                {{$t('pwd3')}}
                <span v-if="errors.has('pwd3')">({{errors.first('pwd3')}})</span>
              </label>
              <input type="password" name="pwd3" class="form-control" v-model.trim="form.pwd3" autocomplete="false" :placeholder="$t('pwd3')" v-validate="'required|confirmed:pwd2'" />
            </div>
          </div>
          <div class="modal-footer">
            <button type="submit" class="btn btn-primary pull-left">{{$t('ok')}}</button>
            <button type="button" class="btn btn-default" data-dismiss="modal">{{$t('cancel')}}</button>
          </div>
        </div>
      </form>
    </div>
  </div>
</template>

<script>
import ajax from "@c/ajax";
import msg from "@c/msg";
import progress from "@c/progress";
import "@c/focus";
// import md5 from 'js-md5';

export default {
  data() {
    return { form: { pwd1: "", pwd2: "" } };
  },
  methods: {
    async chgpwd() {
      // this.form.pwd1 = md5(this.form.pwd1);
      // this.form.pwd2 = md5(this.form.pwd2);
      // this.form.pwd3 = md5(this.form.pwd3);
      if (!this.form.pwd1) {
        msg.info("请输入当前密码！");
        return;
      }
      if (!this.form.pwd2) {
        msg.info("请输入新密码！");
        return;
      }
      if (!this.form.pwd3) {
        msg.info("请输入确认密码！");
        return;
      }
      if (this.form.pwd1 == this.form.pwd2) {
        msg.info("新密码和当前密码相同！");
        return;
      }
      progress.show();
      try {
        let data = await ajax.post({ url: "api/session", data: this.form });
        if (data.msg) {
          msg.info("修改密码失败！" + data.msg);
          return;
        }

        $(this.$el).modal("hide");
        msg.info("修改密码成功！");
      } catch (error) {
        msg.info("修改密码出错！");
      } finally {
        progress.hide();
      }
    }
  },
  mounted() {
    let self = this;
    $(this.$el).on("show.bs.modal", () => {
      $.extend(self.form, { pwd1: "", pwd2: "", pwd3: "" });
      self.$validator.reset();
    });
  }
};
</script>

<style>
</style>
<i18n-yaml>
en:
  title: "Change password"
  pwd1: "Current password"
  pwd2: "New password"
  pwd3: "Repeate password"
  error1: "Current password is not match"
  info: "Password has changed"
cn:
  title: "修改密码"
  pwd1: "当前密码"
  pwd2: "新密码"
  pwd3: "再输入新密码"
  error1: "当前密码错误"
  info: "密码修改成功"
</i18n-yaml>