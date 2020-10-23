<template>
  <div>
    <div v-if="applyable">
      <ul class="nav nav-tabs" role="tablist">
        <li role="presentation" class="active"><a href="#company" aria-controls="company" role="tab" data-toggle="tab" @click="chooseTab(1)">公司信息</a></li>
        <li role="presentation"><a href="#member" aria-controls="member" role="tab" data-toggle="tab" @click="chooseTab(2)">成员信息</a></li>
        <li role="presentation"><a href="#account" aria-controls="account" role="tab" data-toggle="tab" @click="chooseTab(3)">基本账户</a></li>
      </ul>

      <div class="tab-content">
        <div role="tabpanel" class="tab-pane fade in active" id="company">
          <div v-if="companyData.state==2">
            <meta-form :data="companyData">
            </meta-form>
          </div>
        </div>
        <div role="tabpanel" class="tab-pane fade" id="member">
          <div v-for="(m,i) of mDatas" v-bind:key="i">
            <h3>成员{{i+1}}</h3>
            <meta-form :data="m">
            </meta-form>
          </div>
        </div>
        <div role="tabpanel" class="tab-pane fade" id="account">
          <div v-for="(a,i) of aDatas" v-bind:key="i">
            <h3>账户{{i+1}}</h3>
            <meta-form :data="a">
            </meta-form>
          </div>
        </div>
      </div>

      <div style="margin-top:50px;">
        <button class="btn btn-primary" @click="doCommit">提交申请</button>
        <button class="btn btn-info" @click="addMembers" v-if="tabVal==2" style="margin-left:50px;">增加成员</button>
        <button class="btn btn-danger" @click="rmvMembers" v-if="tabVal==2" style="margin-left:50px;">删除成员</button>
        <button class="btn btn-info" @click="addAccounts" v-if="tabVal==3" style="margin-left:50px;">增加账户</button>
        <button class="btn btn-danger" @click="rmvAccounts" v-if="tabVal==3" style="margin-left:50px;">删除账户</button>
      </div>
    </div>
    <div v-else>
      <h3 v-if="user.orgId">非临时用户，不可进行申请认证</h3>
      <h3 v-else>已提交申请认证，请等待平台审核结果！</h3>
    </div>
  </div>
</template>

<script>
import { comps, meta, i18n, dict, editors, MetaDatas } from "@f/framework";
import { progress, msg, ajax } from "@f/vendor";
import MetaForm from "./apply/Form";
import Vue from "vue";
import util from '../../common/util';

export default {
  components: { MetaForm },
  data() {
    return {
      user: {},
      applyable: true,
      tabVal: 1,
      companyName: "trade/company-log",
      companyMeta: {},
      companyData: { state: 0 },
      memberName: "trade/member-log",
      memberMeta: {},
      mDatas: [],// 成员数组
      accountName: "trade/account-log",
      accountMeta: {},
      accountData: { state: 0 },
      aDatas: [],// 账户数组
    }
  },
  methods: {
    async init() {
      // let data = await ajax.get("api/trade/company/userinfo");
      // var user = JSON.parse(data.user);
      // if (user.orgId == "" && user.isLocked != 1) {
      this.applyable = true;
      this.initCompany();
      this.initMember();
      this.initAccount();
      // }
    },
    async initCompany() {
      // 加载交易信息固定页面
      this.companyMeta = await meta.get(this.companyName);
      this.companyData = meta.init(this.companyMeta, {
        meta: this.companyMeta,
        state: 2,
        defaultRow: {},
        form: $.extend({}, {}, {})
      });
      for (let i in this.companyData.meta.fields) {
        let f = this.companyData.meta.fields[i];

      }
      let msgs = meta.msgs(this.companyName);
      i18n.mergeLocaleMessage("en", msgs.en);
      i18n.mergeLocaleMessage("cn", msgs.cn);
    },
    async initMember() {
      // 加载交易信息固定页面
      this.memberMeta = await meta.get(this.memberName);
      this.memberData = meta.init(this.memberMeta, {
        meta: this.memberMeta,
        state: 2,
        defaultRow: {},
        form: $.extend({}, {}, {})
      });
      for (let i in this.memberData.meta.fields) {
        let f = this.memberData.meta.fields[i];

      }
      let msgs = meta.msgs(this.memberName);
      i18n.mergeLocaleMessage("en", msgs.en);
      i18n.mergeLocaleMessage("cn", msgs.cn);

      this.addMembers();
    },
    async initAccount() {
      // 加载交易信息固定页面
      this.accountMeta = await meta.get(this.accountName);
      this.accountData = meta.init(this.accountMeta, {
        meta: this.accountMeta,
        state: 2,
        defaultRow: {},
        form: $.extend({}, {}, {})
      });
      for (let i in this.accountData.meta.fields) {
        let f = this.accountData.meta.fields[i];

      }
      let msgs = meta.msgs(this.accountName);
      i18n.mergeLocaleMessage("en", msgs.en);
      i18n.mergeLocaleMessage("cn", msgs.cn);

      this.addAccounts();
    },
    chooseTab(val) {
      this.tabVal = val;
    },
    addMembers() {
      var mData = meta.init(this.memberMeta, {
        meta: this.memberMeta,
        state: 2,
        defaultRow: {},
        form: $.extend({}, {}, {})
      });
      this.mDatas.push(mData);
    },
    rmvMembers() {
      if (this.mDatas.length > 1) {
        this.mDatas.splice(this.mDatas.length - 2, 1);
      }
    },
    addAccounts() {
      var aData = meta.init(this.accountMeta, {
        meta: this.accountMeta,
        state: 2,
        defaultRow: {},
        form: $.extend({}, {}, {})
      });
      this.aDatas.push(aData);
    },
    rmvAccounts() {
      if (this.aDatas.length > 1) {
        this.aDatas.splice(this.aDatas.length - 2, 1);
      }
    },

    async doCommit() {
      var cr = await this.companyData.$v.validateAll();
      if (!cr) {
        msg.info("请检查填写的公司信息！");
        return;
      }

      for (var m of this.mDatas) {
        var mr = await m.$v.validateAll();
        if (!mr) {
          msg.info("请检查填写的公司成员信息！");
          return;
        }
      }
      for (var a of this.aDatas) {
        var ar = await a.$v.validateAll();
        if (!ar) {
          msg.info("请检查填写的基本账户信息！");
          return;
        }
      }

      this.doCommit2();
    },
    doCommit2() {
      progress.show();
      var cPostData = {};
      var form = this.companyData.form;
      for (let c in form) {
        cPostData[c] = form[c];
      }
      var mPostDatas = [];
      for (var m of this.mDatas) {
        var mPostData = {};
        for (let t in m.form) {
          mPostData[t] = m.form[t];
        }
        mPostDatas.push(mPostData);
      }
      var aPostDatas = [];
      for (var a of this.aDatas) {
        var aPostData = {};
        for (let t in a.form) {
          aPostData[t] = a.form[t];
        }
        aPostDatas.push(aPostData);
      }
      let promise = ajax.post({
        url: "api/trade/company/apply",
        data: { company: cPostData, members: mPostDatas, accounts: aPostDatas }
      });

      promise
        .then(data => {
          msg.info("提交申请成功！");
          this.applyable = false;
        })
        .catch(rs => {
          msg.info("提交申请失败！");
        })
        .always(d => {
          progress.hide();
        })
    }
  },
  mounted() {
    this.init();
  }
}
</script>