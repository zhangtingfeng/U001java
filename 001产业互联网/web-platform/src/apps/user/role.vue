<template>
  <div class="box">
    <div class="box-header with-border bg-gray" v-if="data.state &gt; 0">
      <!-- <h3 class="box-title">{{$t(`${data.meta.name}.title`)}}</h3> -->
      <h3 class="box-title">角色管理</h3>
    </div>
    <div class="box-header with-border" v-if="data.state == 1">
      <search-form :data="data" />
    </div>
    <div class="box-body" v-if="data.state == 1">
      <page-table :data="data" />
    </div>
    <div class="box-footer clearfix" v-if="data.state == 1 && data.data.data">
      <pager :data="data" />
    </div>
    <div v-if="data.state == 2" class="box-body">
      <meta-form :data="data" />
    </div>
    <div class="box-body" v-if="data.state == 2">
      <div class="row">
        <div class="col-md-12">
          <div class="box">
            <div class="box-header with-border bg-gray">
              <h3 class="box-title">菜单权限</h3>
            </div>
            <div class="box-body">
              <tree
                :data="data.formData.config.privileges"
                v-model="data.formData.data.privileges"
              />
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { meta, MetaDatas, comps, locale,dict} from "@f/framework";
import { progress, msg, ajax } from "@f/vendor";
import DataRole from "./DataRole";

function parseTree(datas) {
  if (!datas) datas = [];

  let objs = {};
  let trees = [];
  for (let t of datas) {
    let d = {
      id: t.id,
      pid: t.pid,
      text: locale.currMsg(t.text || t.name),
      t: t.t,
      icon: "m" === t.t ? "fa fa-bars" : "p" === t.t ? "fa fa-link" : ""
    };

    objs[d.id] = d;
    if (d.pid && objs[d.pid]) {
      if (!objs[d.pid].children) objs[d.pid].children = [];
      objs[d.pid].children.push(d);
    } else trees.push(d);
  }
  return { objs, trees };
}

export default {
  data() {
    return {
      name: "platform/role",
      data: {},
      state: 0
    };
  },
  components: comps,
  methods: {
    init(data) {
      try {
        if (data.error) throw "1";
        this.meta = data;
        let $self = this;
        let msgs = meta.msgs(this.meta.name2);
        this.$i18n.mergeLocaleMessage("en", msgs.en);
        this.$i18n.mergeLocaleMessage("cn", msgs.cn);
        this.data = this.createData();
        this.data.state = 1;
        ajax.get("api/platform/dict/menus,privileges").then(data => {
          let t = parseTree(data.menus.concat(data.privileges));
          $self.data.formData.config.privileges = t.trees;
          $self.data.formData.config.objs = t.objs;
        });
      } catch (err) {
        this.data.state = -1;
      }
    },
    createData() {
      let data = meta.init(
        this.meta,
        MetaDatas.Search,
        MetaDatas.Form,
        MetaDatas.Export,
        DataRole
      );
      data.$vm = this;
      return data;
    }
  },
  mounted() {
    meta.get(this.name).then(data => this.init(data));
  }
};
</script>
