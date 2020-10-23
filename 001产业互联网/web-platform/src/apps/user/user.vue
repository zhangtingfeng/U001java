<template>
  <table1 name="user" :extend="UserData">
    <div class="row" slot="extend-inner" v-if="UserData.$data.state == 2">
      <div class="col-md-12 form-group-title">
        <h4 class="bg-gray">{{$t("role")}}</h4>
      </div>
      <div class="checkbox col-md-3" v-for="(r, idx) of UserData.$data.roles" v-bind:key="r.id">
        <checkbox v-model="UserData.$data.rolesChecked[idx]" name="roles" :label="r.name" />
      </div>
    </div>
  </table1>
</template>

<script>
import Vue from "Vue";
import { Table1, dict, comps } from "@f/framework";

let UserData = {
  $data: {},
  roles: [],
  rolesChecked: [],
  formEnter() {
    if (UserData.$data.roles && UserData.$data.roles.length > 0) {
      this.formEnter2();
    } else {
      progress.show();
      ajax.post({ url: "api/meta/role/query", data: {} }).then(data => {
        UserData.$data.roles = data["role"];
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
          url: "api/meta/user-role/query",
          data: { userid: this.form.id }
        })
        .then(data => {
          let userRoles = {};
          for (let ur of data["user-role"] || []) userRoles[ur.roleid] = true;
          for (let r in this.roles)
            Vue.set(this.rolesChecked, r, !!userRoles[this.roles[r].id]);
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
        roleid: this.roles[r].id,
        $op: this.rolesChecked[r] ? 0 : -1
      });
    postData["user-role"] = rules;
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
      if (value) {
        var opt = "<option value=''>--选择--</option>";
        $("#factoryid").html(opt);
        $("#supplierid").html(opt);
      } else {
        dict.get("selectFactory").then(data => {
          var opt = "<option value=''>--选择--</option>";
          $.each(data, function (index, item) {
            opt += "<option value='" + item.id + "'>" + item.text + "</option>";
          });
          $("#factoryid").html(opt);
        });
        dict.get("selectSupper").then(data => {
          var opt = "<option value=''>--选择--</option>";
          $.each(data, function (index, item) {
            opt += "<option value='" + item.id + "'>" + item.text + "</option>";
          });
          $("#supplierid").html(opt);
        });
      }
    },
    "UserData.$data.form.supplierid"(value) {
      if (value) {
        var opt = "<option value=''>--选择--</option>";
        $("#factoryid").html(opt);
        $("#carrierid").html(opt);
      } else {
        dict.get("selectFactory").then(data => {
          var opt = "<option value=''>--选择--</option>";
          $.each(data, function (index, item) {
            opt += "<option value='" + item.id + "'>" + item.text + "</option>";
          });
          $("#factoryid").html(opt);
        });
        dict.get("selectCarrier").then(data => {
          var opt = "<option value=''>--选择--</option>";
          $.each(data, function (index, item) {
            opt += "<option value='" + item.id + "'>" + item.text + "</option>";
          });
          $("#carrierid").html(opt);
        });
      }
    },
    "UserData.$data.form.factoryid"(value) {
      if (value) {
        var opt = "<option value=''>--选择--</option>";
        $("#carrierid").html(opt);
        $("#supplierid").html(opt);
      } else {
        dict.get("selectCarrier").then(data => {
          var opt = "<option value=''>--选择--</option>";
          $.each(data, function (index, item) {
            opt += "<option value='" + item.id + "'>" + item.text + "</option>";
          });
          $("#carrierid").html(opt);
        });
        dict.get("selectSupper").then(data => {
          var opt = "<option value=''>--选择--</option>";
          $.each(data, function (index, item) {
            opt += "<option value='" + item.id + "'>" + item.text + "</option>";
          });
          $("#supplierid").html(opt);
        });
      }
    }
    // "UserData.$data.form.state"(value){
    //   dict.set("selectCarrier", "api/dict/selectCarrier");
    //   dict.set("selectSupper", "api/dict/selectSupper");
    //   dict.set("selectFactory", "api/dict/selectFactory");
    // }
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
cn:
  role: "角色"
</i18n-yaml>