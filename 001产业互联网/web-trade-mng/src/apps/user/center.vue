<template>
  <div>
    <div v-if="state == 1">
      <div class="panel panel-default">
        <div class="panel-heading" style="width:100%;">会员中心</div>
        <div class="panel-body">
          <div class="row">
            <div class="col-md-1 col-md-offset-1">
              <img src="imgs/defaultUser.png" width="100px" class="img-responsive">
            </div>
            <div class="col-md-5">
              <template v-if="!orgInfo.id">
                <p class="font-size">状态：临时用户</p>
                <p class="font-size">用户名称: {{user.userid}}</p>
                <p class="font-size" v-if="!compInfo.companyName">公司名称: 无认证记录，请先企业认证
                  <button class="btn btn-danger" @click="changeState(2)">企业认证</button>
                </p>
                <p class="font-size" v-else>公司名称: {{compInfo.companyName}}(待审核)
                  <button class="btn btn-danger" @click="changeState(3)">申请查看</button>
                </p>
              </template>
              <template v-if="orgInfo.id">
                <p class="font-size">状态：正式用户</p>
                <p class="font-size">用户名称: {{user.userid}}</p>
                <p class="font-size">公司名称: {{orgInfo.name}}</p>
              </template>
            </div>
            <div class="col-md-5"></div>
          </div>
          <div class="row my-tabpanel" v-if="orgInfo.id">
            <div class="col-md-3">
              <img class="img-user" src="imgs/user/icon_manage.png" />
              <img class="img-kuang" src="imgs/user/icon_kuang.png" />
              <div class="text-user">
                <p>用户管理</p>
                <p><a @click="goToUserManage">查看更多></a></p>
              </div>
            </div>
            <div class="col-md-3">
              <img class="img-user" src="imgs/user/icon_change.png" />
              <img class="img-kuang" src="imgs/user/icon_kuang.png" />
              <div class="text-user">
                <p>申请信息</p>
                <p><a @click="goToApplyInfo">查看更多></a></p>
              </div>
            </div>
            <div class="col-md-3">
              <img class="img-user" src="imgs/user/icon_company.png" />
              <img class="img-kuang" src="imgs/user/icon_kuang.png" />
              <div class="text-user">
                <p>{{orgInfo.name}}</p>
                <p><a @click="goToCompInfo">公司信息></a></p>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
    <div v-if="state == 2" class="box-body">
      <company-apply ref="company-apply"></company-apply>
    </div>
    <div v-if="state == 3" class="box-log">
      <company-logview></company-logview>
    </div>
  </div>
</template>

<script>
import { progress, msg, ajax } from "@f/vendor";
import CompanyApply from "../company/apply";
import CompanyLogview from "../company/logview";

export default {
  components: { CompanyApply, CompanyLogview },
  data() {
    return {
      state: 1,
      user: {},
      orgInfo: {},
      compInfo: {}
    };
  },
  methods: {
    changeState(val) {
      this.state = val;
    },
    goToUserManage() {
      window.location = 'index.html#!menu/1002';
    },
    goToApplyInfo() {
      window.location = 'index.html#!menu/1004';
    },
    goToCompInfo() {
      window.location = 'index.html#!menu/1003';
    }
  },
  mounted() {
    progress.show();
    ajax.post({
      url: "api/trade/user/userInfo",
    }).then(d => {
      if (d.user) {
        this.user = JSON.parse(d.user);
      }
      if (d.orgInfo) {
        this.orgInfo = JSON.parse(d.orgInfo);
      }
      if (d.compInfo) {
        this.compInfo = JSON.parse(d.compInfo);
      }
    }).always(() => {
      progress.hide();
    })
  }
}
</script>

<style scoped>
.font-size {
  font-size: 18px;
}
.red {
  color: red;
}
.panel-body {
  min-height: 500px;
}
.img-user {
  position: absolute;
  display: block;
  left: 60px;
  top: 40px;
}
.text-user {
  position: absolute;
  display: block;
  left: 120px;
  top: 35px;
}
.img-kuang {
  position: absolute;
  display: block;
  width: 300px;
  height: 120px;
}

.my-tabpanel {
  padding: 50px 0;
}
</style>