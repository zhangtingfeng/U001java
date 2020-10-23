<template>
  <table1 :name="name" :extend="data2" v-if="meta">
    <div slot="extend" v-if="meta && meta.children" class="row">
      <div class="col-md-12">
        <div class="nav-tabs-custom">
          <ul class="nav nav-tabs" role="tablist">
            <li class="pull-right">
              <a href="#" class="btn" @click="subDel">
                <i class="fa fa-minus"></i>
                {{$t("del")}}
              </a>
            </li>
            <li class="pull-right">
              <a href="#" class="btn btn-xs btn-info" @click="subAdd">
                <i class="fa fa-plus"></i>
                {{$t("create")}}
              </a>
            </li>
            <li v-for="(vm, n, idx) of meta.children" v-bind:key="n" :data-name="n" :class="idx == 0 ? 'active' : ''" class="pull-left">
              <a :href="`#tablex-${elid}-${n}`" role="tab" data-toggle="tab">{{$t(`${vm.name2}.title`)}}</a>
            </li>
          </ul>
          <div class="tab-content">
            <div v-for="(vm, n, idx) of meta.children" v-bind:key="n" :class="idx ==0 ? 'active in' : ''" :id="`tablex-${elid}-${n}`" class="tab-pane fade" role="tabpanel">
              <child-table :meta="vm" :datas="datas[vm.name]" />
            </div>
          </div>
        </div>
      </div>
    </div>
  </table1>
</template>

<script>
import Vue from "vue";
import { comps, meta, dict, views, editors, i18n, MetaDatas } from "@f/framework";
import { progress, msg, ajax } from "@f/vendor";
import ChildTable from "./company/ChildTable";

let TableXId = 1;

let Data2Data = {
  searchActions: [
    {
      name: "query",
      clz: "primary",
      icon: "search"
    }
  ],
  formActions: [],
  subAdd() {
    let name = this.getActive();
    this.datas[name].push({ $op: 1, $selected: false });
  },
  subDel() {
    let name = this.getActive();
    let data2 = [];
    for (let i in this.datas[name]) {
      let d = this.datas[name][i];
      if (d.$selected) {
        if (d.$op != 1) {
          d.$op = -1;
          data2.push(d);
        }
      } else {
        data2.push(d);
      }
    }

    this.datas[name] = data2;
    this.updateSub();
  },
  updateSub() {
    for (let $v of this.$children[0].$children) $v.$forceUpdate();
  },
  formEnter() {
    this.$ui.datas = {};
    this.$ui.result = '';
    this.$ui.opinions = '';
    let promises = [];

    // 编辑的时候需要加载子表，所以显示加载加载提示
    if (1 != this.form.$op) progress.show();

    for (let name in this.meta.children) {
      let cm = this.meta.children[name];

      // 编辑
      let args = {};
      for (let f in cm.fk) {
        args[f] = this.form[cm.fk[f]];
      }
      promises.push(
        ajax.post({
          url: `${cm.url}/query`,
          data: args
        })
      );
    }

    $.when(...promises)
      .then((...datas) => {
        for (let d1 of datas) {
          for (let n in d1) {
            Vue.set(this.$ui.datas, n, d1[n]);
          }
        }
      })
      .always(() => {
        progress.hide();
        this._DataForm.formEnter.apply(this);
      });
  },
  formPostData() {
    let postData = this._DataForm.formPostData.apply(this);
    for (let n in this.$ui.datas) postData[n] = this.$ui.datas[n];
    return postData;
  }
};

export default {
  components: {
    Table1: comps.Table1,
    ChildTable
  },
  data() {
    Data2Data.$ui = this;
    return {
      name: "platform/company",
      elid: TableXId++,
      meta: null,
      datas: {},
      opinions: "",
      result: '',
      data2: Data2Data,
    };
  },
  methods: {
    getActive() {
      return $(this.$el)
        .find(".nav-tabs li.active")
        .data("name");
    },
    cancel() {
      this.data2.$data.formCancel();
    },
  },

  mounted() {
    meta.get(this.name).then(data => {
      this.meta = data;
      for (let name in data.children) {
        let cm = data.children[name];
        let msgs = meta.msgs(cm.name2);
        this.$i18n.mergeLocaleMessage("en", msgs.en);
        this.$i18n.mergeLocaleMessage("cn", msgs.cn);
      }
    });
  }
};
</script>
<style scoped>
.sub-form {
  margin: 5px 6px;
}
.nav-tabs-custom {
  margin-bottom: 0;
  background-color: #d2d6de;
}
.nav-tabs-custom > .nav-tabs .btn {
  margin: 5px;
  padding: 5px;
  font-size: 12px;
  line-height: 1.5;
  border-radius: 3px;
}
</style>
