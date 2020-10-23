<template>
  <table1 name="platform/org-info" :extend="UserData">
    <div class="row" slot="extend-inner" v-if="UserData.$data.state == 2">
      <div class="col-md-12 form-group-title">
        <h4 class="bg-gray">{{ $t("role") }}</h4>
      </div>
      <div
        class="checkbox col-md-3"
        v-for="(r, idx) of UserData.$data.roles"
        v-bind:key="r.id"
      >
        <checkbox
          v-model="UserData.$data.rolesChecked[idx]"
          name="roles"
          :label="r.name"
        />
      </div>
    </div>
  </table1>
</template>

<script>
import { Table1, Checkbox, dict, comps } from "@f/framework";
import { progress, msg, ajax } from "@f/vendor";
import Vue from "Vue";
import util from "../common/util";

let UserData = {
  $data: {},
  roles: [],
  searchActions: [
    {
      name: "query",
      clz: "primary",
      icon: "search",
    },
    {
      name: "exp",
      icon: "th",
    },
    {
      name: "lock",
      icon: "th",
      disabled: true,
    },
    {
      name: "unlock",
      icon: "th",
      disabled: true,
    },
  ],
  rolesChecked: [],
  formEnter() {
    if (UserData.$data.roles && UserData.$data.roles.length > 0) {
      this.formEnter2();
    } else {
      progress.show();
      ajax
        .post({ url: "api/platform/meta/role/query", data: {} })
        .then((data) => {
          UserData.$data.roles = data["role"];
          this.formEnter2();
        });
    }
  },
  formEnter2() {
    this.rolesChecked = new Array(this.roles.length);
    //alert(this.form.$op);
    if (this.form.$op == 1) {
      if (this.$v) this.$v.reset();
      Vue.set(this, "state", 2);
      Vue.nextTick(() => {
        $(window).resize();
      });
      UserData.$user.state = 1;
      progress.hide();
    } else {
      ajax
        .post({
          url: "api/platform/meta/org-role/query",
          data: { orgid: this.form.id },
        })
        .then((data) => {
          let orgRoles = {};
          for (let ur of data["org-role"] || []) orgRoles[ur.roleid] = true;
          for (let r in this.roles)
            Vue.set(this.rolesChecked, r, !!orgRoles[this.roles[r].id]);
          if (this.$v) this.$v.reset();
          Vue.set(this, "state", 2);
          Vue.nextTick(() => {
            $(window).resize();
          });
          UserData.$user.state = 1;
          progress.hide();
        });
    }
  },
  formPostData() {
    let postData = {};
    let form = {};
    for (let f in this.form) if ("" != this.form[f]) form[f] = this.form[f];
    postData[this.meta.name] = [form];
    let rules = [];
    for (let r in this.roles)
      rules.push({
        roleid: this.roles[r].id,
        $op: this.rolesChecked[r] ? 0 : -1,
      });
    postData["org-role"] = rules;
    return postData;
  },
  formCancel() {
    Vue.set(this, "state", 1);
    Vue.nextTick(() => {
      $(window).resize();
    });
    UserData.$user.state = 0;
  },

  searchLock() {
    let objs = util.getSelects(this);
    let o = util.clone(objs);
    let idList = [];
    o.forEach((element) => {
      idList.push(element.id);
    });
    if (idList.length > 0) {
      ajax
        .post({
          url: "api/platform/org/lock",
          data: {
            idList: idList,
          },
        })
        .then((d) => {
          msg.info("操作成功");
          this.searchQuery();
        })
        .catch((e) => {
          msg.info(d.msg, "error");
        });
    }
  },
  searchUnlock() {
    let objs = util.getSelects(this);
    let o = util.clone(objs);
    let idList = [];
    o.forEach((element) => {
      idList.push(element.id);
    });
    if (idList.length > 0) {
      ajax
        .post({
          url: "api/platform/org/unlock",
          data: {
            idList: idList,
          },
        })
        .then((d) => {
          msg.info("操作成功");
          this.searchQuery();
        })
        .catch((e) => {
          msg.info(d.msg, "error");
        });
    }
  },
};

export default {
  components: {
    Table1,
    Checkbox: comps.Checkbox,
  },
  data() {
    UserData.$user = this;
    return {
      UserData,
    };
  },
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
  lock: "冻结"
  unlock: "解冻"
</i18n-yaml>