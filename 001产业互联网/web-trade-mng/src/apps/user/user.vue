<template>
  <table1 name="trade/org-user" :extend="UserData">
    <div class="row" slot="extend-inner" v-if="UserData.$data.state == 2">
      <div class="col-md-12 form-group-title">
        <h4 class="bg-gray">{{$t("role")}}</h4>
      </div>
      <div class="checkbox col-md-3" v-for="(r, idx) of UserData.$data.roles" v-bind:key="r.id">
        <checkbox v-model="UserData.$data.rolesChecked[idx]" name="companyroles" :label="r.rolename" />
      </div>
    </div>
  </table1>
</template>

<script>
import Vue from "Vue";
import { comps, Table1, views } from "@f/framework";
import { progress, msg, ajax } from "@f/vendor";

let UserData = {
  $data: {},
  roles: [],
  rolesChecked: [],
  // 查询按钮
  searchActions: [
    {
      name: "query",
      clz: "primary",
      icon: "search"
    },
    {
      name: 'exp',
      icon: 'th'
    },
    {
      name: 'create',
      icon: 'plus'
    },
    {
      name: 'del',
      icon: 'times',
      disabled: true
    },
    {
      name: 'locked',
      icon: 'th',
      disabled: true
    },
    {
      name: 'unlock',
      icon: 'th',
      disabled: true
    }
  ],


  // 锁定
  searchLocked() {
    var selectedData = []
    for (var i in this.rowState.selected) {
      if (this.rowState.selected[i]) {
        selectedData.push(this.data.data[i]);
      }
    };
    progress.show()
    ajax.post({
      url: 'api/trade/org/lock',
      data: selectedData
    }).then(e => {
      if (e.text) {
        msg.info(e.text, "success")
      }
      this.searchQuery();
    }).fail((e) => {
      msg.info("failed", "error");
    }).always(() => {
      progress.hide()
    })
  },
  // 解锁
  searchUnlock() {
    var selectedData = []
    for (var i in this.rowState.selected) {
      if (this.rowState.selected[i]) {
        selectedData.push(this.data.data[i]);
      }
    };
    progress.show()
    ajax.post({
      url: 'api/trade/org/unlock',
      data: selectedData
    }).then(e => {
      if (e.text) {
        msg.info(e.text, "success")
      }
      this.searchQuery();
    }).fail((e) => {
      msg.info("failed", "error");
    }).always(() => {
      progress.hide()
    })
  },

  formEnter() {
    if (UserData.$data.roles && UserData.$data.roles.length > 0) {
      this.formEnter2();
    } else {
      progress.show();
      ajax.post({
        url: "api/platform/meta/org-role-info/query",
        data: { orgid: this.form.orgId ? this.form.orgId : this.org }
      }).then(data => {
        UserData.$data.roles = data["org-role-info"];
        this.formEnter2();
      });
    }
  },
  formEnter2() {
    this.rolesChecked = new Array(this.roles.length);
    if (this.form.$op == 1) {
      this._DataForm.formEnter.apply(this);
      UserData.$user.state = 1;
      progress.hide();
    } else {
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
          this._DataForm.formEnter.apply(this);
          UserData.$user.state = 1;
          progress.hide();
        });
    }
  },
  formPostData() {
    let postData = this._DataForm.formPostData.apply(this);
    let rules = [];
    for (let r in this.roles)
      rules.push({
        roleid: this.roles[r].roleid,
        $op: this.rolesChecked[r] ? 0 : -1
      });
    postData["org-user-role"] = rules;
    return postData;
  },
  formCancel() {
    this._DataForm.formCancel.apply(this);
    UserData.$user.state = 0;
  }
};

export default {
  components: {
    Table1,
    Checkbox: comps.Checkbox
  },
  data() {
    UserData.$user = this;
    return {
      UserData
    };
  },
  watch: {
    "UserData.$data.form.carrierid"(value) {
      console.log(value);

    },
  },
  async mounted() {
    try {
      let data = await ajax.get("api/session");
      UserData.$data.org = data.user.org;
    } catch { }
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
  locked: "locked"
  unlock : "unlock"
cn:
  role: "角色"
  locked: "锁定"
  unlock: "解锁"
</i18n-yaml>