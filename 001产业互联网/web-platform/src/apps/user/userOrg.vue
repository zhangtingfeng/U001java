<template>
  <div>
    <table1 name="platform/userOrg" :extend="UserOrgData">
      <div class="row" slot="extend-inner" v-if="UserOrgData.$data.state == 2">
        <div class="col-md-12 form-group-title">
          <h4 class="bg-gray">{{$t("role")}}</h4>
        </div>
        <div class="checkbox col-md-3" v-for="(r, idx) of UserOrgData.$data.roles" v-bind:key="r.id">
          <checkbox v-model="UserOrgData.$data.rolesChecked[idx]" name="roles" :label="r.rolename" />
        </div>
      </div>
    </table1>
  </div>
</template>

<script>
import { Table1, dict, comps } from "@f/framework";
import { progress, msg, ajax } from "@f/vendor";
import Vue from "Vue";
import util from "../common/util";

let UserOrgData = {
  $data: {},
  roles: [],
  searchActions: [
    {
      name: "query",
      clz: "primary",
      icon: "search"
    },
    {
      name: "exp",
      icon: "th"
    },
    {
      name: "lock",
      icon: "th",
      disabled: true
    },
    {
      name: "unlock",
      icon: "th",
      disabled: true
    }
  ],
  rolesChecked: [],
  formEnter() {
    // if (UserOrgData.$data.roles && UserOrgData.$data.roles.length > 0) {
    //   this.formEnter2();
    // } else {
      progress.show();
      ajax.post({
        url: "api/platform/meta/org-role-info/query",
        data: { orgid: this.form.orgId }
      }).then(data => {
        UserOrgData.$data.roles = data["org-role-info"];
        this.formEnter2();
      });
    // }
  },
  formEnter2() {
    this.rolesChecked = new Array(this.roles.length);
    // if (this.form.$op == 1) {
    //   if (this.$v) this.$v.reset()
    //   Vue.set(this, "state", 2)
    //   Vue.nextTick(() => {
    //     $(window).resize();
    //   })
    //   UserOrgData.$user.state = 1;
    //   progress.hide();
    // } else {
      ajax
        .post({
          url: "api/platform/meta/user-role/query",
          data: { userid: this.form.id }
        })
        .then(data => {
          let userRoles = {};
          for (let ur of data["user-role"] || []) userRoles[ur.roleid] = true;
          for (let r in this.roles)
            Vue.set(this.rolesChecked, r, !!userRoles[this.roles[r].roleid]);
          if (this.$v) this.$v.reset()
          Vue.set(this, "state", 2)
          Vue.nextTick(() => {
            $(window).resize();
          })
          UserOrgData.$user.state = 1;
          progress.hide();
        });
    // }
  },
  formPostData() {
    let postData = {}
    let form = {}
    for (let f in this.form)
      if ("" != this.form[f]) form[f] = this.form[f]
    postData[this.meta.name] = [form]
    let rules = [];
    for (let r in this.roles)
      rules.push({
        roleid: this.roles[r].roleid,
        $op: this.rolesChecked[r] ? 0 : -1
      });
    postData["user-role"] = rules;
    return postData;
  },

  // 锁定
  searchLock() {
    let objs = util.getSelects(this);
    let o = util.clone(objs);
    let userIds = [];
    o.forEach(element => {
      userIds.push(element.id);
    });
    if (userIds.length > 0) {
      ajax.post({
        url: "api/platform/user/lock",
        data: {
          userIds: userIds
        }
      }).then(d => {
        msg.info("操作成功");
        this.searchQuery();
      }).catch(e => {
        msg.info(e.code, "error");
      })
    }
  },
  searchUnlock() {
    let objs = util.getSelects(this);
    let o = util.clone(objs);
    let userIds = [];
    o.forEach(element => {
      userIds.push(element.id);
    });
    if (userIds.length > 0) {
      ajax.post({
        url: "api/platform/user/unlock",
        data: {
          userIds: userIds
        }
      }).then(d => {
        msg.info("操作成功");
        this.searchQuery();
      }).catch(e => {
        msg.info(e.code, "error");
      })
    }
  }
}
export default {
  components: {
    Table1,
    Checkbox: comps.Checkbox
  },

  data() {
    UserOrgData.$user = this;
    return {
      UserOrgData
    };
  }
};
</script>

<style scoped>
.checkbox + .checkbox {
  margin-top: 10px;
}
</style>
<i18n-yaml>
en:
  role: "Roles"
  lock: "lock"
  unlock: "unlock"
cn:
  role: "角色"
  lock: "锁定"
  unlock: "解锁"
</i18n-yaml>